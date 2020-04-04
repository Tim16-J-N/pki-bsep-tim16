package ftn.pkibseptim16.controller;

import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.dto.CreateCertificateDTO;
import ftn.pkibseptim16.dto.CreatedCertificateDTO;
import ftn.pkibseptim16.dto.DownloadCertificateDTO;
import ftn.pkibseptim16.service.CertificateService;
import ftn.pkibseptim16.service.KeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private KeyStoreService keyStoreService;

    @PostMapping(value = "/self-signed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatedCertificateDTO> createSelfSigned(@Valid @RequestBody CreateCertificateDTO createCertificateDTO) {
        try {
            CreatedCertificateDTO createdCertificate = certificateService.createSelfSigned(createCertificateDTO);

            if (createdCertificate == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(createdCertificate, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatedCertificateDTO> create(@Valid @RequestBody CreateCertificateDTO createCertificateDTO) {
        try {
            CreatedCertificateDTO createdCertificate = certificateService.create(createCertificateDTO);

            if (createdCertificate == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(createdCertificate, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getCertificates(@RequestParam(value = "role", required = true) String role,
                                                                @RequestParam(value = "keyStorePassword", required = true) String keyStorePassword) {
        try {
            return new ResponseEntity<>(keyStoreService.getCertificates(role, keyStorePassword), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getCACertificates(@RequestParam(value = "rootKeyStoragePassword") String rootKeyStoragePassword,
                                                                  @RequestParam(value = "intermediateKeyStoragePassword") String intermediateKeyStoragePassword) {
        try {
            return new ResponseEntity<>(keyStoreService.getCACertificates(rootKeyStoragePassword, intermediateKeyStoragePassword), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/download", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> download(@Valid @RequestBody DownloadCertificateDTO downloadCertDTO) {
        try {
            certificateService.download(downloadCertDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CertificateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (KeyStoreException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
