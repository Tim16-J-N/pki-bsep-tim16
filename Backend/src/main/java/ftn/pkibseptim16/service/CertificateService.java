package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.dto.CreateCertificateDTO;

public interface CertificateService {
     CertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws Exception;
}
