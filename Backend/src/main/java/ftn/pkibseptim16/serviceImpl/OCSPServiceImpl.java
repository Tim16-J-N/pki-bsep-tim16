package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.dto.ResponseCertificateDTO;
import ftn.pkibseptim16.dto.RevokeCertificateDTO;
import ftn.pkibseptim16.enumeration.CertificateRole;
import ftn.pkibseptim16.enumeration.CertificateStatus;
import ftn.pkibseptim16.exceptionHandler.InvalidOCSPDataException;
import ftn.pkibseptim16.model.OCSPItem;
import ftn.pkibseptim16.repository.OCSPRepository;
import ftn.pkibseptim16.service.KeyStoreService;
import ftn.pkibseptim16.service.OCSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Service
public class OCSPServiceImpl implements OCSPService {

    @Autowired
    private OCSPRepository ocspRepository;

    @Autowired
    private KeyStoreService keyStoreService;

    @Override
    public CertificateStatus checkStatus(String serialNumber) {
        OCSPItem ocspItem = ocspRepository.findBySerialNumber(serialNumber);
        if (ocspItem == null) {
            return CertificateStatus.GOOD;
        } else {
            return CertificateStatus.REVOKED;
        }
    }

    @Override
    public ResponseCertificateDTO revoke(RevokeCertificateDTO revokeCertDTO)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        BigInteger serialNumber = new BigInteger(revokeCertDTO.getSerialNumber());
        CertificateRole certRole = revokeCertDTO.returnCertRoleToEnum();

        if (ocspRepository.findBySerialNumber(revokeCertDTO.getSerialNumber()) != null) {
            throw new InvalidOCSPDataException("Certificate is already revoked.");
        }

        switch (certRole) {
            case END_ENTITY: {
                X509Certificate certificate = getCertificate(serialNumber, certRole, revokeCertDTO.getEndEntityKeyStorePass(), revokeCertDTO.getAlias());
                ocspRepository.save(new OCSPItem(revokeCertDTO.getSerialNumber()));
                return new ResponseCertificateDTO(certificate);
            }
            case INTERMEDIATE: {
                X509Certificate certificate = getCertificate(serialNumber, certRole, revokeCertDTO.getIntermediateKeyStorePass(), revokeCertDTO.getAlias());
                ocspRepository.save(new OCSPItem(revokeCertDTO.getSerialNumber()));
                revokeChildren(CertificateRole.END_ENTITY, certificate, revokeCertDTO.getEndEntityKeyStorePass());
                revokeChildren(CertificateRole.INTERMEDIATE, certificate, revokeCertDTO.getIntermediateKeyStorePass());
                return new ResponseCertificateDTO(certificate);
            }
            case ROOT: {
                X509Certificate certificate = getCertificate(serialNumber, certRole, revokeCertDTO.getRootKeyStorePass(), revokeCertDTO.getAlias());
                ocspRepository.save(new OCSPItem(revokeCertDTO.getSerialNumber()));
                revokeChildren(CertificateRole.END_ENTITY, certificate, revokeCertDTO.getEndEntityKeyStorePass());
                revokeChildren(CertificateRole.INTERMEDIATE, certificate, revokeCertDTO.getIntermediateKeyStorePass());
                return new ResponseCertificateDTO(certificate);
            }
            default:
                throw new InvalidOCSPDataException("Unsuccessful certificate revocation.");
        }
    }

    @Override
    public boolean isRevoked(Certificate certificate) {
        OCSPItem ocspItem = ocspRepository.findBySerialNumber(((X509Certificate) certificate).getSerialNumber().toString());
        return ocspItem == null ? false : true;
    }

    private X509Certificate getCertificate(BigInteger serialNumber, CertificateRole certRole, String keyStorePassword, String alias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        X509Certificate certificate = (X509Certificate) keyStoreService.getCertificate(certRole, keyStorePassword, alias);
        if (certificate == null || !certificate.getSerialNumber().equals(serialNumber)) {
            throw new InvalidOCSPDataException("Non-existing certificate for revocation.");
        }

        return certificate;
    }

    private void revokeChildren(CertificateRole certRole, Certificate certificate, String keyStorePass)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String keyStorePath = keyStoreService.getKeyStorePath(certRole);
        KeyStore keyStore = keyStoreService.getKeyStore(keyStorePath, keyStorePass);

        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate[] certificatesChain = keyStore.getCertificateChain(alias);
            List<Certificate> certificateList = new ArrayList<>(Arrays.asList(certificatesChain));
            if (certificateList.contains(certificate)) {
                for (Certificate cert : certificateList) {
                    String serialNo = ((X509Certificate) cert).getSerialNumber().toString();
                    if (cert.equals(certificate)) {
                        break;
                    }
                    if (ocspRepository.findBySerialNumber(serialNo) == null) {
                        ocspRepository.save(new OCSPItem(serialNo));
                    }
                }
            }
        }
    }

}
