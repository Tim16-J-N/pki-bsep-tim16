package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CertDownloadInfoDTO;
import ftn.pkibseptim16.dto.CreateCertificateDTO;
import ftn.pkibseptim16.dto.ResponseCertificateDTO;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.ietf.jgss.GSSException;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;

public interface CertificateService {
    ResponseCertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws ParseException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, IOException, OperatorCreationException, KeyStoreException, GSSException;

    ResponseCertificateDTO create(CreateCertificateDTO createCertificateDTO) throws ParseException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException,
            OperatorCreationException, GSSException;

    void download(CertDownloadInfoDTO certDownloadInfoDTO) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, CMSException, OperatorCreationException, UnrecoverableKeyException;
}
