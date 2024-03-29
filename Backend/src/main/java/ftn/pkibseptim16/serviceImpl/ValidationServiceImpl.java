package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.dto.CertAccessInfoDTO;
import ftn.pkibseptim16.enumeration.CertificateRole;
import ftn.pkibseptim16.enumeration.CertificateValidity;
import ftn.pkibseptim16.service.KeyStoreService;
import ftn.pkibseptim16.service.OCSPService;
import ftn.pkibseptim16.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    private OCSPService ocspService;

    @Autowired
    private KeyStoreService keyStoreService;

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
    public CertificateValidity checkValidity(CertAccessInfoDTO certAccessInfoDTO)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchProviderException {
        String alias = certAccessInfoDTO.getAlias();

        CertificateRole certRole = certAccessInfoDTO.returnCertRoleToEnum();
        if (certRole == null) {
            throw new NullPointerException("Undefined certificate role.");
        }

        Certificate[] certificateChain = keyStoreService.getCertificateChain(certRole, certAccessInfoDTO.getKeyStorePassword(), alias);

        return validate(certificateChain) ? CertificateValidity.VALID : CertificateValidity.INVALID;
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

    private boolean validateCert(Certificate certificate, PublicKey issuerPublicKey) throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {

        try {
            ((X509Certificate) certificate).checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            return false;
        }

        if (!verifySignature(certificate, issuerPublicKey)) {
            return false;
        }

        return !ocspService.isRevoked(certificate);
    }

}
