package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.dto.*;
import ftn.pkibseptim16.enumeration.CertificateRole;
import ftn.pkibseptim16.exceptionHandler.InvalidCertificateDataException;
import ftn.pkibseptim16.model.Entity;
import ftn.pkibseptim16.model.IssuerData;
import ftn.pkibseptim16.model.SubjectData;
import ftn.pkibseptim16.repository.EntityRepository;
import ftn.pkibseptim16.service.CertificateService;
import ftn.pkibseptim16.service.KeyStoreService;
import ftn.pkibseptim16.service.ValidationService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.ietf.jgss.GSSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final String PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "data" + File.separator;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private ValidationService validationService;

    @Override
    public ResponseCertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws ParseException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException, CertificateException, IOException, OperatorCreationException, KeyStoreException, GSSException {
        CertificateDTO certificateDTO = createCertificateDTO.getCertificateData();
        Entity subject = entityRepository.findByCommonName(certificateDTO.getSubject().getCommonName());

        Date validFrom = getDate(certificateDTO.getValidFrom());
        Date validTo = getDate(certificateDTO.getValidTo());
        Date today = getToday();
        if (validFrom.before(today) || validFrom.after(validTo)) {
            throw new InvalidCertificateDataException("Start date of validity period must be before end date.");
        }
        if (!certificateDTO.getKeyUsage().isEnabled()) {
            throw new InvalidCertificateDataException("You have to select at least one Key Usage.");
        }

        if (!certificateDTO.getExtendedKeyUsage().isEnabled()) {
            throw new InvalidCertificateDataException("You have to select at least one Extended Key Usage.");
        }

        SubjectData subjectData = getSubject(subject);
        IssuerData issuerData = getIssuerForSelfSigned(subjectData);

        X509Certificate cert = generateCertificate(subjectData, issuerData, validFrom, validTo, certificateDTO);

        keyStoreService.store(createCertificateDTO.getKeyStorePassword(), createCertificateDTO.getAlias(),
                subjectData.getPrivateKey(), createCertificateDTO.getPrivateKeyPassword(), new Certificate[]{cert});
        subject.setNumberOfRootCertificates(subject.getNumberOfRootCertificates() + 1);
        entityRepository.save(subject);
        return new ResponseCertificateDTO(cert);
    }


    @Override
    public ResponseCertificateDTO create(CreateCertificateDTO createCertificateDTO) throws ParseException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException,
            OperatorCreationException, GSSException {
        CertificateDTO certificateDTO = createCertificateDTO.getCertificateData();
        Entity subject = entityRepository.findByCommonName(certificateDTO.getSubject().getCommonName());
        CertificateDTO issuerCertificate = createCertificateDTO.getIssuerCertificate();
        Entity issuer = entityRepository.findByCommonName(issuerCertificate.getSubject().getCommonName());
        if (subject.getCommonName().equals(issuer.getCommonName())) {
            throw new InvalidCertificateDataException("Issuer and subject can not be the same entity.");
        }

        Date validFrom = getDate(certificateDTO.getValidFrom());
        Date validTo = getDate(certificateDTO.getValidTo());
        if (validFrom.before(getToday()) || validFrom.after(validTo)) {
            throw new InvalidCertificateDataException("Start date of validity period must be before end date.");
        }

        if (!certificateDataIsValid(certificateDTO, issuerCertificate, validFrom, validTo)) {
            throw new InvalidCertificateDataException("Certificate is invalid");
        }
        SubjectData subjectData = getSubject(subject);

        String issuerKeyStorePassword = createCertificateDTO.getIssuerKeyStorePassword();
        IssuerData issuerData = getIssuer(issuer, issuerCertificate, issuerKeyStorePassword,
                createCertificateDTO.getIssuerPrivateKeyPassword());

        CertificateRole certificateRole = getCertificateRole(issuerCertificate);

        X509Certificate cert = generateCertificate(subjectData, issuerData, validFrom, validTo, certificateDTO);

        Certificate[] newCertificateChain = getCertificateChain(certificateRole, issuerKeyStorePassword,
                issuerCertificate.getAlias(), cert);

        keyStoreService.store(createCertificateDTO.getKeyStorePassword(), createCertificateDTO.getAlias(),
                subjectData.getPrivateKey(), createCertificateDTO.getPrivateKeyPassword(), newCertificateChain);
        return new ResponseCertificateDTO(cert);
    }

    @Override
    public void download(CertDownloadInfoDTO certDownloadInfoDTO)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, CMSException, OperatorCreationException,
            UnrecoverableKeyException {

        CertAccessInfoDTO certAccessInfoDTO = certDownloadInfoDTO.getCertAccessInfo();
        String certRoleStr = certAccessInfoDTO.getCertRole().toLowerCase();
        String alias = certAccessInfoDTO.getAlias();
        CertificateRole certRole = certAccessInfoDTO.returnCertRoleToEnum();
        if (certRole == null) {
            throw new NullPointerException("Undefined certificate role.");
        }

        PrivateKey key = keyStoreService.getPrivateKey(CertificateRole.ROOT, certDownloadInfoDTO.getRootKeyStorePass(),
                certDownloadInfoDTO.getRootCertAlias(), certDownloadInfoDTO.getRootCertKeyPass());
        X509Certificate certificate = (X509Certificate) keyStoreService.getCertificate(certRole, certAccessInfoDTO.getKeyStorePassword(), alias);
        Certificate[] certificates = keyStoreService.getCertificateChain(certRole, certAccessInfoDTO.getKeyStorePassword(), alias);
        List<Certificate> certificateList = new ArrayList<>(Arrays.asList(certificates));
        byte[] certs = encryptCertToPKCS7(certificate, key, certificateList);

        FileOutputStream os = new FileOutputStream(PATH + certRoleStr + "_" + alias + ".p7b");
        os.write("-----BEGIN CERTIFICATE-----\n".getBytes(StandardCharsets.US_ASCII));
        os.write(Base64.encodeBase64(certs, true));
        os.write("-----END CERTIFICATE-----\n".getBytes(StandardCharsets.US_ASCII));
        os.close();
    }

    private byte[] encryptCertToPKCS7(X509Certificate certificate, Key key, List<Certificate> certificates)
            throws CertificateEncodingException, CMSException, IOException, OperatorCreationException {
        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        ContentSigner sha256Signer = new JcaContentSignerBuilder("SHA256withECDSA").setProvider("BC").build((PrivateKey) key);
        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder()
                .setProvider("BC").build())
                .build(sha256Signer, certificate));
        generator.addCertificates(new JcaCertStore(certificates));
        CMSTypedData content = new CMSProcessableByteArray(certificate.getEncoded());

        CMSSignedData signedData = generator.generate(content, true);

        return signedData.getEncoded();
    }


    private Certificate[] getCertificateChain(CertificateRole certificateRole, String issuerKeyStorePassword,
                                              String alias, Certificate cert) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException {
        Certificate[] certificateChain = keyStoreService.getCertificateChain(certificateRole, issuerKeyStorePassword,
                alias);
        if (!validationService.validate(certificateChain)) {
            throw new CertificateException("Issuer's certificate is not valid");
        }
        List<Certificate> certificateList = new ArrayList<>(Arrays.asList(certificateChain));
        certificateList.add(0, cert);

        Certificate[] newCertificates = new Certificate[certificateList.size()];
        for (int i = 0; i < certificateList.size(); i++)
            newCertificates[i] = certificateList.get(i);

        return newCertificates;
    }

    private SubjectData getSubject(Entity entity) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPairSubject = generateKeyPair();
        if (keyPairSubject == null) {
            return null;
        }
        X500Name x500Name = getX500Name(entity);
        return new SubjectData(keyPairSubject.getPublic(), x500Name, entity.getId(), keyPairSubject.getPrivate());
    }

    private IssuerData getIssuerForSelfSigned(SubjectData subjectData) {
        return new IssuerData(subjectData.getX500name(), subjectData.getPrivateKey(), subjectData.getPublicKey(),
                subjectData.getId());
    }

    private IssuerData getIssuer(Entity issuer, CertificateDTO issuerCertificate, String keyStorePassword,
                                 String issuerPrivateKeyPassword) throws UnrecoverableKeyException, CertificateException,
            NoSuchAlgorithmException, KeyStoreException, IOException {
        X500Name x500Name = getX500Name(issuer);
        CertificateRole certificateRole = getCertificateRole(issuerCertificate);
        PrivateKey privateKey = keyStoreService.getPrivateKey(certificateRole, keyStorePassword,
                issuerCertificate.getAlias(), issuerPrivateKeyPassword);
        PublicKey publicKey = keyStoreService.getPublicKey(certificateRole, keyStorePassword,
                issuerCertificate.getAlias());
        return new IssuerData(x500Name, privateKey, publicKey, issuer.getId());
    }

    private KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "SunEC");

        ECGenParameterSpec ecsp;
        ecsp = new ECGenParameterSpec("secp256k1");
        kpg.initialize(ecsp);
        return kpg.generateKeyPair();
    }

    private X500Name getX500Name(Entity entity) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, entity.getCommonName());
        builder.addRDN(BCStyle.O, entity.getOrganization());
        builder.addRDN(BCStyle.C, entity.getCountryCode());
        if (entity.getType().toString() == "USER") {
            builder.addRDN(BCStyle.SURNAME, entity.getSurname());
            builder.addRDN(BCStyle.GIVENNAME, entity.getGivename());
            builder.addRDN(BCStyle.EmailAddress, entity.getEmail());
        } else {
            builder.addRDN(BCStyle.OU, entity.getOrganizationUnitName());
            builder.addRDN(BCStyle.L, entity.getLocalityName());
            builder.addRDN(BCStyle.ST, entity.getState());
        }
        return builder.build();
    }

    private Date getDate(String date) throws ParseException {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            return df.parse(date);
        } catch (ParseException e) {
            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            return dateFormat.parse(date);
        }

    }


    private X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, Date validFrom,
                                                Date validTo, CertificateDTO certificateDTO) throws OperatorCreationException, CertIOException, CertificateException, GSSException {

        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withECDSA");
        builder = builder.setProvider("BC");
        ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());
        BigInteger serialNumber = new BigInteger(1, getSerialNumber());
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(), serialNumber,
                validFrom, validTo, subjectData.getX500name(), subjectData.getPublicKey());

        certGen.setSubjectUniqueID(toBooleanArray(subjectData.getId()));
        certGen.setIssuerUniqueID(toBooleanArray(issuerData.getId()));

        if (certificateDTO.getAuthorityKeyIdentifier()) {
            certGen.addExtension(Extension.authorityKeyIdentifier, false,
                    new AuthorityKeyIdentifier(issuerData.getPublicKey().getEncoded()));
        }
        if (certificateDTO.getSubjectKeyIdentifier()) {
            certGen.addExtension(Extension.subjectKeyIdentifier, false,
                    new SubjectKeyIdentifier(subjectData.getPublicKey().getEncoded()));
        }
        if (certificateDTO.getSubjectIsCa()) {
            certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        }
        if (certificateDTO.getKeyUsage().isEnabled()) {
            KeyUsageDTO keyUsageDTO = certificateDTO.getKeyUsage();
            KeyUsage keyUsage = new KeyUsage(keyUsageDTO.getDigitalSignatureInt() | keyUsageDTO.getNonRepudiationInt()
                    | keyUsageDTO.getKeyEnciphermentInt() | keyUsageDTO.getDataEnciphermentInt()
                    | keyUsageDTO.getKeyAgreementInt() | keyUsageDTO.getCertificateSigningInt()
                    | keyUsageDTO.getCrlSignInt() | keyUsageDTO.getEnchiperOnlyInt()
                    | keyUsageDTO.getDecipherOnlyInt());
            certGen.addExtension(Extension.keyUsage, false, keyUsage);
        }

        if (certificateDTO.getExtendedKeyUsage().isEnabled()) {
            ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(
                    certificateDTO.getExtendedKeyUsage().methodKeyPurposeIds());
            certGen.addExtension(Extension.extendedKeyUsage, false, extendedKeyUsage);
        }

        String url = "http://localhost:8080/api/ocsp?sn=" + serialNumber.toString();
        GeneralName generalName = new GeneralName(GeneralName.uniformResourceIdentifier, url);

        AuthorityInformationAccess authorityInformationAccess = new AuthorityInformationAccess(new ASN1ObjectIdentifier("1.3.6.1.5.5.7.48.1"), generalName);
        certGen.addExtension(Extension.authorityInfoAccess, false, authorityInformationAccess);
        return new JcaX509CertificateConverter().setProvider("BC")
                .getCertificate(certGen.build(contentSigner));
    }

    private String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("d.M.yyyy.");
        return df.format(date);
    }

    private boolean certificateDataIsValid(CertificateDTO newCertificateDTO, CertificateDTO issuerCertificate,
                                           Date validFrom, Date validTo) throws ParseException {

        if (validFrom.before(getDate(issuerCertificate.getValidFrom()))
                || validTo.after(getDate(issuerCertificate.getValidTo()))) {
            throw new InvalidCertificateDataException("Validity period of a certificate must be within validity perod of the issuer's certificate, which is from " + formatDate(validFrom) + "  to " + formatDate(validTo));
        }

        if (newCertificateDTO.getSubjectIsCa() && !newCertificateDTO.getKeyUsage().isEnabled()) {
            throw new InvalidCertificateDataException("You have to select at least one Key Usage.");
        }

        if (newCertificateDTO.getSubjectIsCa() && !newCertificateDTO.getExtendedKeyUsage().isEnabled()) {
            throw new InvalidCertificateDataException("You have to select at least one Extended Key Usage.");
        }

        if (issuerCertificate.getKeyUsage() != null && newCertificateDTO.getKeyUsage() != null) {
            List<Integer> subjectFalseKeyUsages = newCertificateDTO.getKeyUsage().methodFalseKeyUsageIdentifiers();
            List<Integer> issuerFalseKeyUsages = issuerCertificate.getKeyUsage().methodFalseKeyUsageIdentifiers();
            for (Integer identifier : issuerFalseKeyUsages) {
                if (!subjectFalseKeyUsages.contains(identifier)) {
                    throw new InvalidCertificateDataException("Some of the selected Key Usages cannot be set because issuer's certificate cannot sign them");
                }
            }
        }

        if (issuerCertificate.getExtendedKeyUsage() != null && newCertificateDTO.getExtendedKeyUsage() != null) {
            List<KeyPurposeId> subjectFalseExtendedKeyUsages = newCertificateDTO.getExtendedKeyUsage()
                    .methodFalseExtendedKeyUsageIdentifiers();
            List<KeyPurposeId> issuerFalseExtendedKeyUsages = issuerCertificate.getExtendedKeyUsage()
                    .methodFalseExtendedKeyUsageIdentifiers();
            for (KeyPurposeId identifier : issuerFalseExtendedKeyUsages) {
                if (!subjectFalseExtendedKeyUsages.contains(identifier)) {
                    throw new InvalidCertificateDataException("Some of the selected Extended Key Usages cannot be set because issuer's certificate cannot sign them");
                }
            }
        }

        return true;
    }

    private byte[] getSerialNumber() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("Windows-PRNG");
        } catch (NoSuchAlgorithmException e) {
            random = new SecureRandom();
        }
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return bytes;
    }

    private boolean[] toBooleanArray(Long value) {

        char[] cArray = new char[7];
        Arrays.fill(cArray, '0');
        if (value != -1) {
            char[] chars = Long.toBinaryString(value).toCharArray();

            int offset = 0;
            if (chars.length < cArray.length) {
                offset = cArray.length - chars.length;
            }

            System.arraycopy(chars, 0, cArray, offset, chars.length);

        }
        boolean[] bits = new boolean[cArray.length];
        for (int i = 0; i < cArray.length; i++) {
            bits[i] = cArray[i] == '1';
        }
        return bits;
    }

    private CertificateRole getCertificateRole(CertificateDTO certificateDTO) {
        CertificateRole certificateRole = CertificateRole.INTERMEDIATE;
        if (certificateDTO.getSubject().getCommonName().equals(certificateDTO.getIssuer().getCommonName())) {
            certificateRole = CertificateRole.ROOT;
        }
        return certificateRole;
    }

    public static Date getToday() {
        Calendar date = new GregorianCalendar();

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }
}
