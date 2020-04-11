package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CertAccessInfoDTO;
import ftn.pkibseptim16.enumeration.CertificateValidity;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public interface ValidationService {

    boolean validate(Certificate[] certChain) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException;

    CertificateValidity checkValidity(CertAccessInfoDTO certAccessInfoDTO) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException;

}
