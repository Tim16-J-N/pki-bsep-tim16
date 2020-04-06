package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.dto.EntityDTO;
import ftn.pkibseptim16.dto.ExtendedKeyUsageDTO;
import ftn.pkibseptim16.dto.KeyUsageDTO;
import ftn.pkibseptim16.enumeration.CertificateRole;
import ftn.pkibseptim16.service.KeyStoreService;
import ftn.pkibseptim16.service.ValidationService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private final String ROOT_KEYSTORE = "root_keystore.pkcs12";
    private final String INTERMEDIATE_KEYSTORE = "intermediate_keystore.pkcs12";
    private final String END_ENTITY_KEYSTORE = "end_entity_keystore.pkcs12";

    @Autowired
    private ValidationService validationService;

    @Override
    public void store(String keyStorePassword, String alias, PrivateKey privateKey, String keyPassword,
                      Certificate[] certificateChain)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
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
    public List<CertificateDTO> getCertificates(String role, String keyStorePassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException {
        String keyStorePath = getKeyStorePath(getCertificateRole(role));
        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        Enumeration<String> aliases = keyStore.aliases();
        List<CertificateDTO> certificateDTOS = new ArrayList<>();

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate[] certificateChain = keyStore.getCertificateChain(alias);
            CertificateDTO certificateDTO = createCertificateDTO((X509Certificate) certificateChain[0], alias);
            certificateDTO.setExpired(!validationService.validateForOverview(certificateChain));
            certificateDTOS.add(certificateDTO);
        }
        return certificateDTOS;
    }

    @Override
    public List<CertificateDTO> getCACertificates(Long subjectId, String rootKeyStoragePassword, String intermediateKeyStoragePassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        if (rootKeyStoragePassword.isEmpty() && intermediateKeyStoragePassword.isEmpty()) {
            throw new BadCredentialsException("You have to enter at least one of the passwords.");
        }

        List<CertificateDTO> certificateDTOS = new ArrayList<>();
        if (!rootKeyStoragePassword.isEmpty()) {
            loadCertificates(subjectId, certificateDTOS, rootKeyStoragePassword, "root");
        }

        if (!intermediateKeyStoragePassword.isEmpty()) {
            loadCertificates(subjectId, certificateDTOS, intermediateKeyStoragePassword, "intermediate");
        }

        return certificateDTOS;
    }

    private List<CertificateDTO> loadCertificates(Long subjectId, List<CertificateDTO> certificateDTOS, String keyStorePassword,
                                                  String role) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        String keyStorePath = getKeyStorePath(getCertificateRole(role));
        char[] keyStorePassArray = keyStorePassword.toCharArray();

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(new FileInputStream(keyStorePath), keyStorePassArray);
        } catch (FileNotFoundException e) {
            return certificateDTOS;
        }

        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate[] certificatesChain = keyStore.getCertificateChain(alias);
            try {
                X509Certificate x509Certificate = (X509Certificate) certificatesChain[0];
                Long subjectUniqueID = booleanArrayToLong(x509Certificate.getSubjectUniqueID());

                if (subjectUniqueID != subjectId && validationService.validate(certificatesChain)) {
                    certificateDTOS.add(createCertificateDTO(x509Certificate, alias));
                }
            } catch (NoSuchProviderException | CertificateException | NoSuchAlgorithmException e) {
            }
        }
        return certificateDTOS;
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

    private CertificateRole getCertificateRole(String role) {
        if (role.equals("root")) {
            return CertificateRole.ROOT;
        } else if (role.equals("intermediate")) {
            return CertificateRole.INTERMEDIATE;
        } else {
            return CertificateRole.END_ENTITY;
        }
    }

    private KeyStore getKeyStore(String keyStorePath, String keyStorePassword)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        char[] keyStorePassArray = keyStorePassword.toCharArray();

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(new FileInputStream(keyStorePath), keyStorePassArray);
        } catch (FileNotFoundException e) {
            keyStore.load(null, keyStorePassArray);
        }

        return keyStore;
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

    private String getKeyStorePath(CertificateRole certificateRole) {
        switch (certificateRole) {
            case ROOT:
                return ROOT_KEYSTORE;
            case INTERMEDIATE:
                return INTERMEDIATE_KEYSTORE;
            default:
                return END_ENTITY_KEYSTORE;
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
