package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.service.ValidationService;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validate(Certificate[] certChain) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
        if (!validateRoot(certChain[certChain.length - 1])) {
            return false;
        }

        for (int i = certChain.length - 1; i > 0; i--) {
            if (!validateCert(certChain[i - 1], certChain[i].getPublicKey())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean validateForOverview(Certificate[] certChain) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
        if (!validateRootForOverview(certChain[certChain.length - 1])) {
            return false;
        }

        for (int i = certChain.length - 1; i > 0; i--) {
            if (!validateCertForOverview(certChain[i - 1], certChain[i].getPublicKey())) {
                return false;
            }
        }

        return true;
    }

    private boolean validateRoot(Certificate cert)
            throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {
        try {
            PublicKey key = cert.getPublicKey();
            cert.verify(key);
            if (!validateCert(cert, key)) {
                return false;
            }
            return true;
        } catch (SignatureException | InvalidKeyException e) {
            return false;
        }
    }

    private boolean verifySignature(Certificate cert, PublicKey issuerPublicKey)
            throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {
        try {
            cert.verify(issuerPublicKey);
            return true;
        } catch (SignatureException | InvalidKeyException e) {
            return false;
        }
    }

    //TODO: ADD CHECK OCSP LIST -> CHECK IF IS REVOKED
    private boolean validateCert(Certificate certificate, PublicKey issuerPublicKey) throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {

        try {
            ((X509Certificate) certificate).checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            return false;
        }

        if (verifySignature(certificate, issuerPublicKey)) {
            return true;
        }

        return false;
    }

    private boolean validateRootForOverview(Certificate cert)
            throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {
        try {
            PublicKey key = cert.getPublicKey();
            cert.verify(key);
            if (!validateCertForOverview(cert, key)) {
                return false;
            }
            return true;
        } catch (SignatureException | InvalidKeyException e) {
            return false;
        }
    }

    private boolean validateCertForOverview(Certificate certificate, PublicKey issuerPublicKey) throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {

        try {
            ((X509Certificate) certificate).checkValidity();
        } catch (CertificateExpiredException e) {
            return false;
        } catch (CertificateNotYetValidException e) {

        }

        if (verifySignature(certificate, issuerPublicKey)) {
            return true;
        }

        return false;
    }
}
