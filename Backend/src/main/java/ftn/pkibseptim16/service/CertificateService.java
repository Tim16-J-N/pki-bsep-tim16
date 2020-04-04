package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CreateCertificateDTO;
import ftn.pkibseptim16.dto.CreatedCertificateDTO;
import ftn.pkibseptim16.dto.DownloadCertificateDTO;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public interface CertificateService {
    CreatedCertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws Exception;

    CreatedCertificateDTO create(CreateCertificateDTO createCertificateDTO) throws Exception;

    void download(DownloadCertificateDTO downloadCertDTO) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;
}
