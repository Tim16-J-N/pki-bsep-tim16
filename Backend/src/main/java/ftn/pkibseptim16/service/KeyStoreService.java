package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.enumeration.CertificateRole;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;

public interface KeyStoreService {
    void store(String keyStorePassword, String alias, PrivateKey privateKey, String keyPassword, Certificate certificate)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException;

    PrivateKey getPrivateKey(CertificateRole certificateRole, String keyStorePassword, String alias, String keyPassword)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException;

    PublicKey getPublicKey(CertificateRole certificateRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

    List<CertificateDTO> getCertificates(String role,String keyStorePassword) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;
}
