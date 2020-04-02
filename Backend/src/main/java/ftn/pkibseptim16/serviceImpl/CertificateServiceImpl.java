package ftn.pkibseptim16.serviceImpl;

import com.google.common.primitives.Longs;
import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.dto.CreateCertificateDTO;
import ftn.pkibseptim16.dto.KeyUsageDTO;
import ftn.pkibseptim16.model.Entity;
import ftn.pkibseptim16.model.IssuerData;
import ftn.pkibseptim16.model.SubjectData;
import ftn.pkibseptim16.repository.EntityRepository;
import ftn.pkibseptim16.service.CertificateService;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private EntityRepository entityRepository;

    @Override
    public CertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws Exception {
        CertificateDTO certificateDTO = createCertificateDTO.getCertificate();
        Entity subject = entityRepository.getById(certificateDTO.getSubject().getId());
        Entity issuer = entityRepository.getById(certificateDTO.getIssuerCertificate().getIssuerUniqueId());
        if (subject.getId() != issuer.getId()) {
            throw new BadCredentialsException("Subject and issuer must be the same person ");
        }

        Date validFrom = getDate(certificateDTO.getValidFrom());
        Date validTo = getDate(certificateDTO.getValidTo());
        if (validFrom.before(new Date()) || validFrom.after(validTo)) {
            throw new BadCredentialsException("Start date of validity period must be before end date.");
        }

        SubjectData subjectData = getSubject(subject);
        IssuerData issuerData = getIssuer(issuer);

        X509Certificate cert = generateCertificate(subjectData, issuerData, validFrom, validTo, certificateDTO);

        return null;
    }

    public SubjectData getSubject(Entity entity) {
        KeyPair keyPairSubject = generateKeyPair();
        if (keyPairSubject == null) {
            return null;
        }
        X500Name x500Name = getX500Name(entity);
        return new SubjectData(keyPairSubject.getPublic(), x500Name, entity.getId());
    }

    private IssuerData getIssuer(Entity entity) {
//        PrivateKey issuerKey =
        X500Name x500Name = getX500Name(entity);
        return new IssuerData(x500Name, null, null, entity.getId());
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); //ok
            SecureRandom random = SecureRandom.getInstance("Windows-PRNG");
            keyGen.initialize(3072, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public X500Name getX500Name(Entity entity) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, entity.getCommonName());
        builder.addRDN(BCStyle.O, entity.getOrganization());
        builder.addRDN(BCStyle.C, entity.getCountryCode());
        if (entity.getType().toString() == "USER") {
            builder.addRDN(BCStyle.SURNAME, entity.getSurname());
            builder.addRDN(BCStyle.GIVENNAME, entity.getSurname());
            builder.addRDN(BCStyle.EmailAddress, entity.getEmail());
        } else {
            builder.addRDN(BCStyle.OU, entity.getOrganizationUnitName());
            builder.addRDN(BCStyle.L, entity.getLocalityName());
            builder.addRDN(BCStyle.ST, entity.getState());
        }
        return builder.build();
    }

    private Date getDate(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(date);
    }

    private X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, Date validFrom, Date validTo,
                                                CertificateDTO certificateDTO) throws Exception {

        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        builder = builder.setProvider("BC");
        ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());
        BigInteger serialNumber = new BigInteger(getSerialNumber());
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                serialNumber, validFrom, validTo, subjectData.getX500name(), subjectData.getPublicKey());

        certGen.setSubjectUniqueID(toBooleanArray(subjectData.getId()));
        certGen.setIssuerUniqueID(toBooleanArray(issuerData.getId()));

        if (certificateDTO.getAuthorityKeyIdentifier()) {
            certGen.addExtension(Extension.authorityKeyIdentifier, true, createAuthorityKeyId(issuerData.getPublicKey()));
        }
        if (certificateDTO.getSubjectKeyIdentifier()) {
            certGen.addExtension(Extension.subjectKeyIdentifier, true, createSubjectKeyId(subjectData.getPublicKey()));
        }
        if (certificateDTO.getSubjectIsCa()) {
            certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        }
        if (certificateDTO.getKeyUsage().isEnabled()) {
            KeyUsageDTO keyUsageDTO = certificateDTO.getKeyUsage();
            KeyUsage keyUsage = new KeyUsage(keyUsageDTO.getDigitalSignatureInt() | keyUsageDTO.getNonRepudiationInt() |
                    keyUsageDTO.getKeyEnciphermentInt() | keyUsageDTO.getDataEnciphermentInt() | keyUsageDTO.getKeyAgreementInt() |
                    keyUsageDTO.getCertificateSigningInt() | keyUsageDTO.getCrlSignInt() | keyUsageDTO.getEnchiperOnlyInt() |
                    keyUsageDTO.getDecipherOnlyInt());
            certGen.addExtension(Extension.keyUsage, true, keyUsage);
        }
        if (certificateDTO.getExtendedKeyUsage().isEnabled()) {
            ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(certificateDTO.getExtendedKeyUsage().getKeyPurposeIds());
            certGen.addExtension(Extension.extendedKeyUsage, true, extendedKeyUsage);
        }


        return new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider()).getCertificate(certGen.build(contentSigner));
    }

    private static SubjectKeyIdentifier createSubjectKeyId(PublicKey publicKey) throws OperatorCreationException {
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        DigestCalculator digCalc =
                new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

        return new X509ExtensionUtils(digCalc).createSubjectKeyIdentifier(publicKeyInfo);
    }

    private static AuthorityKeyIdentifier createAuthorityKeyId(PublicKey publicKey)
            throws OperatorCreationException {
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        DigestCalculator digCalc =
                new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

        return new X509ExtensionUtils(digCalc).createAuthorityKeyIdentifier(publicKeyInfo);
    }

    private byte[] getSerialNumber() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

    private boolean[] toBooleanArray(Long number) {
        byte[] bytes = Longs.toByteArray(number);
        boolean[] booleans = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            booleans[i] = bytes[i] != 0;
        }

        return booleans;
    }
}
