package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.dto.*;
import ftn.pkibseptim16.enumeration.CertificateRole;
import ftn.pkibseptim16.model.Entity;
import ftn.pkibseptim16.model.IssuerData;
import ftn.pkibseptim16.model.SubjectData;
import ftn.pkibseptim16.repository.EntityRepository;
import ftn.pkibseptim16.service.CertificateService;
import ftn.pkibseptim16.service.KeyStoreService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private KeyStoreService keyStoreService;

    @Override
    public CreatedCertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws Exception {
        CertificateDTO certificateDTO = createCertificateDTO.getCertificateData();
        Entity subject = entityRepository.findByCommonName(certificateDTO.getSubject().getCommonName());

        Date validFrom = getDate(certificateDTO.getValidFrom());
        Date validTo = getDate(certificateDTO.getValidTo());
        if (validFrom.before(new Date()) || validFrom.after(validTo)) {
            throw new BadCredentialsException("Start date of validity period must be before end date.");
        }

        SubjectData subjectData = getSubject(subject);
        IssuerData issuerData = getIssuerForSelfSigned(subjectData);

        X509Certificate cert = generateCertificate(subjectData, issuerData, validFrom, validTo, certificateDTO);

        keyStoreService.store(createCertificateDTO.getKeyStorePassword(), createCertificateDTO.getAlias(),
                subjectData.getPrivateKey(), createCertificateDTO.getPrivateKeyPassword(), cert);
        subject.setNumberOfRootCertificates(subject.getNumberOfRootCertificates() + 1);
        entityRepository.save(subject);
        return new CreatedCertificateDTO(cert);
    }

    @Override
    public CreatedCertificateDTO create(CreateCertificateDTO createCertificateDTO) throws Exception {
        CertificateDTO certificateDTO = createCertificateDTO.getCertificateData();
        Entity subject = entityRepository.findByCommonName(certificateDTO.getSubject().getCommonName());
        CertificateDTO issuerCertificate = createCertificateDTO.getIssuerCertificate();
        Entity issuer = entityRepository.findByCommonName(issuerCertificate.getSubject().getCommonName());
        if (subject.getCommonName().equals(issuer.getCommonName())) {
            throw new IllegalArgumentException("Issuer and subject is the same entity.");
        }

        Date validFrom = getDate(certificateDTO.getValidFrom());
        Date validTo = getDate(certificateDTO.getValidTo());
        if (validFrom.before(new Date()) || validFrom.after(validTo)) {
            throw new IllegalArgumentException("Start date of validity period must be before end date.");
        }

        // proveri da li je vreme ok
        // dodaj proveru da li se ekstenzije poklapaju
        // dodaj proveru da li je sertifikat kojim zelis da potpises validan
        if (!certificateDataIsValid(certificateDTO, issuerCertificate, validFrom, validTo)) {
            throw new IllegalArgumentException("Certificate is invalid");
        }
        SubjectData subjectData = getSubject(subject);

        IssuerData issuerData = getIssuer(issuer, issuerCertificate, createCertificateDTO.getIssuerKeyStorePassword(),
                createCertificateDTO.getIssuerPrivateKeyPassword());

        X509Certificate cert = generateCertificate(subjectData, issuerData, validFrom, validTo, certificateDTO);

        keyStoreService.store(createCertificateDTO.getKeyStorePassword(), createCertificateDTO.getAlias(),
                subjectData.getPrivateKey(), createCertificateDTO.getPrivateKeyPassword(), cert);
        return new CreatedCertificateDTO(cert);
    }

    @Override
    public void download(DownloadCertificateDTO downloadCertDTO)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String certRoleStr = downloadCertDTO.getCertRole().toLowerCase();
        String alias = downloadCertDTO.getAlias();
        CertificateRole certRole = downloadCertDTO.returnCertRoleToEnum();
        if (certRole == null) {
            throw new NullPointerException("Undefined certificate role.");
        }

        Certificate certificate = keyStoreService.getCertificate(certRole, downloadCertDTO.getKeyStorePassword(),
                alias);

        FileOutputStream os = new FileOutputStream(certRoleStr + "_" + alias + ".cer");
        os.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
        os.write(Base64.encodeBase64(certificate.getEncoded(), true));
        os.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
        os.close();
    }

    private SubjectData getSubject(Entity entity) {
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
        CertificateRole certificateRole = CertificateRole.INTERMEDIATE;
        if (issuerCertificate.getSubject().getCommonName().equals(issuerCertificate.getIssuer().getCommonName())) {
            certificateRole = CertificateRole.ROOT;
        }
        PrivateKey privateKey = keyStoreService.getPrivateKey(certificateRole, keyStorePassword,
                issuerCertificate.getAlias(), issuerPrivateKeyPassword);
        PublicKey publicKey = keyStoreService.getPublicKey(certificateRole, keyStorePassword,
                issuerCertificate.getAlias());
        return new IssuerData(x500Name, privateKey, publicKey, issuer.getId());
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "SunEC");

            ECGenParameterSpec ecsp;
            ecsp = new ECGenParameterSpec("secp256k1");
            kpg.initialize(ecsp);
            // KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); //ok
            // SecureRandom random = SecureRandom.getInstance("Windows-PRNG");
            // keyGen.initialize(3072, random);
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            // e.printStackTrace();
        }
        return null;
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
                                                Date validTo, CertificateDTO certificateDTO) throws Exception {

        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withECDSA");
        builder = builder.setProvider("BC");
        ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());
        BigInteger serialNumber = new BigInteger(1,getSerialNumber());
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(), serialNumber,
                validFrom, validTo, subjectData.getX500name(), subjectData.getPublicKey());

        certGen.setSubjectUniqueID(toBooleanArray(subjectData.getId()));
        certGen.setIssuerUniqueID(toBooleanArray(issuerData.getId()));

        if (certificateDTO.getAuthorityKeyIdentifier()) {
            certGen.addExtension(Extension.authorityKeyIdentifier, true,
                    new AuthorityKeyIdentifier(issuerData.getPublicKey().getEncoded()));
        }
        if (certificateDTO.getSubjectKeyIdentifier()) {
            certGen.addExtension(Extension.subjectKeyIdentifier, true,
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
            certGen.addExtension(Extension.keyUsage, true, keyUsage);
        }
        if (certificateDTO.getExtendedKeyUsage().isEnabled()) {
            ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(
                    certificateDTO.getExtendedKeyUsage().methodKeyPurposeIds());
            certGen.addExtension(Extension.extendedKeyUsage, true, extendedKeyUsage);
        }

        return new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider())
                .getCertificate(certGen.build(contentSigner));
    }

    // proveri da li je vreme ok
    // dodaj proveru da li se ekstenzije poklapaju
    // DODAJ PROVERU DA LI JE SERTIFIKAT KOJIM ZELIS DA POTPUSES VALIDAN
    private boolean certificateDataIsValid(CertificateDTO newCertificateDTO, CertificateDTO issuerCertificate,
                                           Date validFrom, Date validTo) throws ParseException {

        if (validFrom.before(getDate(issuerCertificate.getValidFrom()))
                || validTo.after(getDate(issuerCertificate.getValidTo()))) {
            return false;
        }

        if (issuerCertificate.getKeyUsage() != null && newCertificateDTO.getKeyUsage() != null) {
            List<Integer> subjectFalseKeyUsages = newCertificateDTO.getKeyUsage().methodFalseKeyUsageIdentifiers();
            List<Integer> issuerFalseKeyUsages = issuerCertificate.getKeyUsage().methodFalseKeyUsageIdentifiers();
            for (Integer identifier : issuerFalseKeyUsages) {
                if (!subjectFalseKeyUsages.contains(identifier)) {
                    return false;
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
                    return false;
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
}
