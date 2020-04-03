package ftn.pkibseptim16.controller;

import ftn.pkibseptim16.dto.CertificateDTO;
import ftn.pkibseptim16.service.KeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/key-store")
public class KeyStoreController {

    @Autowired
    private KeyStoreService keyStoreService;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDTO>> getCertificates(@RequestParam(value = "role", required = true) String role,
                                                                @RequestParam(value = "keyStorePassword", required = true) String keyStorePassword) {
        try{
            return new ResponseEntity<>(keyStoreService.getCertificates(role,keyStorePassword), HttpStatus.OK);
        }catch (Exception e ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
