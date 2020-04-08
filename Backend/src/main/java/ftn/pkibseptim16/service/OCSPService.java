package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CertificateIdDTO;
import ftn.pkibseptim16.dto.ResponseCertificateDTO;
import ftn.pkibseptim16.dto.RevokeCertificateDTO;
import ftn.pkibseptim16.enumeration.CertificateStatus;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public interface OCSPService {

    CertificateStatus checkStatus(CertificateIdDTO certificateIdDTO);

    ResponseCertificateDTO revoke(RevokeCertificateDTO revokeCertDTO) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

}
