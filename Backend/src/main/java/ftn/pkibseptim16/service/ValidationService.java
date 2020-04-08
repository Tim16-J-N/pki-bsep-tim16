package ftn.pkibseptim16.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public interface ValidationService {

    boolean validate(Certificate[] certChain) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException;

}
