package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.dto.CertificateItemDTO;
import ftn.pkibseptim16.enumeration.CertificateRole;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;

public interface KeyStoreService {
    void store(String keyStorePassword, String alias, PrivateKey privateKey, String keyPassword, Certificate[] certificateChain)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

    PrivateKey getPrivateKey(CertificateRole certificateRole, String keyStorePassword, String alias, String keyPassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            UnrecoverableKeyException;

    PublicKey getPublicKey(CertificateRole certificateRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

    Certificate getCertificate(CertificateRole certificateRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

    List<CertificateItemDTO> getCertificates(String role, String keyStorePassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException, OCSPException, OperatorCreationException;

    List<CertificateDTO> getCACertificates(Long subjectId, String rootKeyStoragePassword, String intermediateKeyStoragePassword)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException;

    Certificate[] getCertificateChain(CertificateRole certificateRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

    KeyStore getKeyStore(String keyStorePath, String keyStorePassword)
            throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException;

    String getKeyStorePath(CertificateRole certificateRole);
}
