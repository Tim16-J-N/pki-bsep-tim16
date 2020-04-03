package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.CreateCertificateDTO;
import ftn.pkibseptim16.dto.CreatedCertificateDTO;

public interface CertificateService {
     CreatedCertificateDTO createSelfSigned(CreateCertificateDTO createCertificateDTO) throws Exception;

     CreatedCertificateDTO create(CreateCertificateDTO createCertificateDTO) throws Exception;
}
