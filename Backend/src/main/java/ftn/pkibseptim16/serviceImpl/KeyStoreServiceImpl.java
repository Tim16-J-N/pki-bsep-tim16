package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.dto.*;
import ftn.pkibseptim16.enumeration.CertificateRole;
import ftn.pkibseptim16.service.KeyStoreService;
import ftn.pkibseptim16.service.ValidationService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    private final String PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "data" + File.separator;
    private final String ROOT_KEYSTORE = PATH + "root_keystore.p12";
    private final String INTERMEDIATE_KEYSTORE = PATH + "intermediate_keystore.p12";
    private final String END_ENTITY_KEYSTORE = PATH + "end_entity_keystore.p12";

    @Autowired
    private ValidationService validationService;

    @Override
    public void store(String keyStorePassword, String alias, PrivateKey privateKey, String keyPassword,
                      Certificate[] certificateChain) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        char[] keyPassArray = keyPassword.toCharArray();
        char[] keyStorePassArray = keyStorePassword.toCharArray();
        CertificateRole certificateRole = getCertificateRole(certificateChain[0]);
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        keyStore.setKeyEntry(alias, privateKey, keyPassArray, certificateChain);
        keyStore.store(new FileOutputStream(keyStorePath), keyStorePassArray);
    }

    @Override
    public PrivateKey getPrivateKey(CertificateRole certificateRole, String keyStorePassword, String alias,
                                    String keyPassword) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            UnrecoverableKeyException {
        char[] keyPassArray = keyPassword.toCharArray();
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        return (PrivateKey) keyStore.getKey(alias, keyPassArray);
    }

    @Override
    public PublicKey getPublicKey(CertificateRole certificateRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate.getPublicKey();
    }

    @Override
    public Certificate getCertificate(CertificateRole certificateRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate;
    }

    @Override
    public Certificate[] getCertificateChain(CertificateRole certificateRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        Certificate[] certificateChain = keyStore.getCertificateChain(alias);
        return certificateChain;
    }

    @Override
    public List<CertificateItemDTO> getCertificates(String role, String keyStorePassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, OCSPException, OperatorCreationException {
        String keyStorePath = getKeyStorePath(getCertificateRole(role));
        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        Enumeration<String> aliases = keyStore.aliases();
        List<CertificateItemDTO> certificateItems = new ArrayList<>();
        X509Certificate issuerCert = null;

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate[] certificateChain = keyStore.getCertificateChain(alias);
            if (certificateChain.length == 1) {
                issuerCert = (X509Certificate) certificateChain[0];
            } else {
                issuerCert = (X509Certificate) certificateChain[1];
            }
            CertificateItemDTO certificateItemDTO = createCertificateItemDTO((X509Certificate) certificateChain[0], issuerCert, alias);
            certificateItems.add(certificateItemDTO);
        }
        return certificateItems;
    }

    @Override
    public List<CertificateDTO> getCACertificates(Long subjectId, String rootKeyStoragePassword, String intermediateKeyStoragePassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException {
        if ((rootKeyStoragePassword == null || rootKeyStoragePassword.isEmpty()) && (intermediateKeyStoragePassword == null || intermediateKeyStoragePassword.isEmpty())) {
            throw new BadCredentialsException("You have to enter at least one of the passwords.");
        }

        List<CertificateDTO> certificateDTOS = new ArrayList<>();
        if (rootKeyStoragePassword != null && !rootKeyStoragePassword.isEmpty()) {
            loadCertificates(subjectId, certificateDTOS, rootKeyStoragePassword, "root");
        }

        if (intermediateKeyStoragePassword != null && !intermediateKeyStoragePassword.isEmpty()) {
            loadCertificates(subjectId, certificateDTOS, intermediateKeyStoragePassword, "intermediate");
        }

        return certificateDTOS;
    }

    @Override
    public KeyStore getKeyStore(String keyStorePath, String keyStorePassword)
            throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {
        char[] keyStorePassArray = keyStorePassword.toCharArray();

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(new FileInputStream(keyStorePath), keyStorePassArray);
        } catch (FileNotFoundException e) {
            keyStore.load(null, keyStorePassArray);
        }

        return keyStore;
    }

    @Override
    public String getKeyStorePath(CertificateRole certificateRole) {
        switch (certificateRole) {
            case ROOT:
                return ROOT_KEYSTORE;
            case INTERMEDIATE:
                return INTERMEDIATE_KEYSTORE;
            default:
                return END_ENTITY_KEYSTORE;
        }
    }

    private void loadCertificates(Long subjectId, List<CertificateDTO> certificateDTOS, String keyStorePassword, String role)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException {

        String keyStorePath = getKeyStorePath(getCertificateRole(role));
        char[] keyStorePassArray = keyStorePassword.toCharArray();

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(new FileInputStream(keyStorePath), keyStorePassArray);
        } catch (FileNotFoundException e) {
            return;
        }

        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate[] certificatesChain = keyStore.getCertificateChain(alias);

            X509Certificate x509Certificate = (X509Certificate) certificatesChain[0];
            Long subjectUniqueID = booleanArrayToLong(x509Certificate.getSubjectUniqueID());

            if (!subjectUniqueID.equals(subjectId) && validationService.validate(certificatesChain)) {
                certificateDTOS.add(createCertificateDTO(x509Certificate, alias));
            }
        }
    }

    private CertificateDTO createCertificateDTO(X509Certificate x509Certificate, String alias)
            throws CertificateEncodingException, CertificateParsingException {
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setSerialNumber(x509Certificate.getSerialNumber().toString(16));
        X500Name x500name = new JcaX509CertificateHolder(x509Certificate).getSubject();
        boolean[] subjectUniqueID = x509Certificate.getSubjectUniqueID();

        certificateDTO.setSubject(new EntityDTO(x500name, booleanArrayToLong(subjectUniqueID)));

        X500Name x500nameIssuer = new JcaX509CertificateHolder(x509Certificate).getIssuer();
        boolean[] issuerUniqueID = x509Certificate.getIssuerUniqueID();
        certificateDTO.setIssuer(new EntityDTO(x500nameIssuer, booleanArrayToLong(issuerUniqueID)));

        if (x509Certificate.getExtensionValue("2.5.29.35") != null) {
            certificateDTO.setAuthorityKeyIdentifier(true);
        }
        if (x509Certificate.getExtensionValue("2.5.29.14") != null) {
            certificateDTO.setSubjectKeyIdentifier(true);
        }
        if (x509Certificate.getBasicConstraints() != -1) {
            certificateDTO.setSubjectIsCa(true);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        certificateDTO.setValidFrom(dateFormat.format(x509Certificate.getNotBefore()));
        certificateDTO.setValidTo(dateFormat.format(x509Certificate.getNotAfter()));

        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage != null) {
            certificateDTO.setKeyUsage(new KeyUsageDTO(x509Certificate.getKeyUsage()));
        }
        List<String> extendedKeyUsage = x509Certificate.getExtendedKeyUsage();
        if (extendedKeyUsage != null && !extendedKeyUsage.isEmpty()) {
            certificateDTO.setExtendedKeyUsage(new ExtendedKeyUsageDTO(extendedKeyUsage));
        }
        certificateDTO.setAlias(alias);
        return certificateDTO;
    }

    private CertificateItemDTO createCertificateItemDTO(X509Certificate x509Certificate, X509Certificate issuerCertificate, String alias)
            throws CertificateEncodingException, CertificateParsingException, OCSPException, IOException, OperatorCreationException {
        CertificateItemDTO certificateItem = new CertificateItemDTO();
        certificateItem.setSerialNumber(x509Certificate.getSerialNumber().toString(16));
        X500Name x500name = new JcaX509CertificateHolder(x509Certificate).getSubject();
        boolean[] subjectUniqueID = x509Certificate.getSubjectUniqueID();

        certificateItem.setSubject(new EntityDTO(x500name, booleanArrayToLong(subjectUniqueID)));

        X500Name x500nameIssuer = new JcaX509CertificateHolder(x509Certificate).getIssuer();
        boolean[] issuerUniqueID = x509Certificate.getIssuerUniqueID();
        certificateItem.setIssuer(new EntityDTO(x500nameIssuer, booleanArrayToLong(issuerUniqueID)));

        if (x509Certificate.getBasicConstraints() != -1) {
            certificateItem.setSubjectIsCa(true);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        certificateItem.setValidFrom(dateFormat.format(x509Certificate.getNotBefore()));
        certificateItem.setValidTo(dateFormat.format(x509Certificate.getNotAfter()));

        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage != null) {
            certificateItem.setKeyUsage(new KeyUsageDTO(x509Certificate.getKeyUsage()));
        }
        List<String> extendedKeyUsage = x509Certificate.getExtendedKeyUsage();
        if (extendedKeyUsage != null && !extendedKeyUsage.isEmpty()) {
            certificateItem.setExtendedKeyUsage(new ExtendedKeyUsageDTO(extendedKeyUsage));
        }
        certificateItem.setAlias(alias);

        X509CertificateHolder certificateHolder = new X509CertificateHolder(issuerCertificate.getEncoded());
        JcaDigestCalculatorProviderBuilder digestCalculatorProviderBuilder = new JcaDigestCalculatorProviderBuilder();
        DigestCalculatorProvider digestCalculatorProvider = digestCalculatorProviderBuilder.build();
        DigestCalculator digestCalculator = digestCalculatorProvider.get(CertificateID.HASH_SHA1);
        CertificateID certificateID = new CertificateID(digestCalculator, certificateHolder, x509Certificate.getSerialNumber());
        certificateItem.setCertificateID(new CertificateIdDTO(certificateID));

        return certificateItem;
    }

    private CertificateRole getCertificateRole(String role) {
        if (role.equals("root")) {
            return CertificateRole.ROOT;
        } else if (role.equals("intermediate")) {
            return CertificateRole.INTERMEDIATE;
        } else {
            return CertificateRole.END_ENTITY;
        }
    }

    private CertificateRole getCertificateRole(Certificate cert) throws CertificateEncodingException {
        X509Certificate certificate = (X509Certificate) cert;
        X500Name subject = new JcaX509CertificateHolder(certificate).getSubject();
        X500Name issuer = new JcaX509CertificateHolder(certificate).getIssuer();
        if (subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString()
                .equals(issuer.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString())) {
            return CertificateRole.ROOT;
        } else if (certificate.getBasicConstraints() != -1) { // if path length is set, it's a CA certificate
            return CertificateRole.INTERMEDIATE;
        } else {
            return CertificateRole.END_ENTITY;
        }
    }

    private long booleanArrayToLong(boolean[] attributes) {
        char[] chars = new char[7];
        Arrays.fill(chars, '0');
        if (attributes != null) {
            for (int i = 0; i < attributes.length; ++i) {
                if (attributes[i])
                    chars[i] = '1';
            }
        }
        return new BigInteger(new String(chars), 2).longValue();
    }
}
