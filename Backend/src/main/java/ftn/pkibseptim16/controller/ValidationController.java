package ftn.pkibseptim16.controller;

import ftn.pkibseptim16.dto.CertAccessInfoDTO;
import ftn.pkibseptim16.enumeration.CertificateValidity;
import ftn.pkibseptim16.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/validation")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificateValidity> checkValidity(@Valid @RequestBody CertAccessInfoDTO certAccessInfoDTO) throws Exception {
        return new ResponseEntity<>(validationService.checkValidity(certAccessInfoDTO), HttpStatus.OK);
    }
}
