package ftn.pkibseptim16.controller;

import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.dto.CreateCertificateDTO;
import ftn.pkibseptim16.dto.CreatedCertificateDTO;
import ftn.pkibseptim16.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;
@CrossOrigin
@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping(value="/self-signed",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatedCertificateDTO> createSelfSigned(@Valid @RequestBody CreateCertificateDTO createCertificateDTO) {
        try {
            createCertificateDTO.getCertificate().getSubject().setId(1L);
            createCertificateDTO.getCertificate().getIssuerCertificate().setIssuerUniqueId(1L);
            CreatedCertificateDTO createdCertificate = certificateService.createSelfSigned(createCertificateDTO);

            if (createdCertificate == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(createdCertificate, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
