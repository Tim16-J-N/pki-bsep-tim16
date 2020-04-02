package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.enumeration.CertificateRole;
import ftn.pkibseptim16.service.KeyStoreService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    private final String ROOT_KEYSTORE = "src/main/resources/data/root_keystore.pkcs12";
    private final String INTERMEDIATE_KEYSTORE = "src/main/resources/data/intermediate_keystore.pkcs12";
    private final String END_ENTITY_KEYSTORE = "src/main/resources/data/end_entity_keystore.pkcs12";

    @Override
    public void store(String keyStorePassword, String alias, PrivateKey privateKey, String keyPassword, Certificate certificate)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        char[] keyPassArray = keyPassword.toCharArray();
        char[] keyStorePassArray = keyStorePassword.toCharArray();
        CertificateRole certificateRole = getCertificateRole(certificate);
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        keyStore.setKeyEntry(alias, privateKey, keyPassArray, new Certificate[]{certificate});
        keyStore.store(new FileOutputStream(keyStorePath), keyStorePassArray);
    }

    @Override
    public PrivateKey getPrivateKey(CertificateRole certificateRole, String keyStorePassword, String alias, String keyPassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
        char[] keyPassArray = keyPassword.toCharArray();
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        return (PrivateKey) keyStore.getKey(alias, keyPassArray);
    }

    @Override
    public PublicKey getPublicKey(CertificateRole certificateRole, String keyStorePassword, String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String keyStorePath = getKeyStorePath(certificateRole);

        KeyStore keyStore = getKeyStore(keyStorePath, keyStorePassword);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate.getPublicKey();
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

    private CertificateRole getCertificateRole(Certificate cert) {
        X509Certificate certificate = (X509Certificate) cert;
        if (Arrays.equals(certificate.getSubjectUniqueID(), certificate.getIssuerUniqueID())) {
            return CertificateRole.ROOT;
        } else if (certificate.getBasicConstraints() != -1) {   // if path length is set, it's a CA certificate
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
}
