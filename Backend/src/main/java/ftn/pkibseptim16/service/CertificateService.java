package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CreateCertificateDTO;
import ftn.pkibseptim16.dto.CreatedCertificateDTO;
import ftn.pkibseptim16.dto.DownloadCertificateDTO;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;

public interface CertificateService {
    CreatedCertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws ParseException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, IOException, OperatorCreationException, KeyStoreException;

    CreatedCertificateDTO create(CreateCertificateDTO createCertificateDTO) throws ParseException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException,
            OperatorCreationException;

    void download(DownloadCertificateDTO downloadCertDTO) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;
}
