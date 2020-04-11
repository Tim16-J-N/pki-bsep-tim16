package ftn.pkibseptim16.controller;

import ftn.pkibseptim16.dto.*;
import ftn.pkibseptim16.service.CertificateService;
import ftn.pkibseptim16.service.KeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private KeyStoreService keyStoreService;

    @PostMapping(value = "/self-signed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCertificateDTO> createSelfSigned(@Valid @RequestBody CreateCertificateDTO createCertificateDTO) throws Exception {
        return new ResponseEntity<>(certificateService.createSelfSigned(createCertificateDTO), HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCertificateDTO> create(@Valid @RequestBody CreateCertificateDTO createCertificateDTO) throws Exception {
        return new ResponseEntity<>(certificateService.create(createCertificateDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateItemDTO>> getCertificates(@RequestParam(value = "role", required = true) String role,
                                                                    @RequestParam(value = "keyStorePassword", required = true) String keyStorePassword) throws Exception {

        return new ResponseEntity<>(keyStoreService.getCertificates(role, keyStorePassword), HttpStatus.OK);
    }

    @GetMapping(value = "/{subjectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getCACertificates(@PathVariable Long subjectId, @RequestParam(value = "rootKeyStoragePassword", required = false) String rootKeyStoragePassword,
                                                                  @RequestParam(value = "intermediateKeyStoragePassword", required = false) String intermediateKeyStoragePassword) throws Exception {

        return new ResponseEntity<>(keyStoreService.getCACertificates(subjectId, rootKeyStoragePassword, intermediateKeyStoragePassword), HttpStatus.OK);
    }

    @PostMapping(value = "/download", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> download(@Valid @RequestBody CertAccessInfoDTO downloadCertDTO) throws Exception {
        certificateService.download(downloadCertDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
