package ftn.pkibseptim16.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.pkibseptim16.dto.CertificateIdDTO;
import ftn.pkibseptim16.dto.ResponseCertificateDTO;
import ftn.pkibseptim16.dto.RevokeCertificateDTO;
import ftn.pkibseptim16.enumeration.CertificateStatus;
import ftn.pkibseptim16.service.OCSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/ocsp")
public class OCSPController {

    @Autowired
    private OCSPService ocspService;

    @PostMapping(value = "/check-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificateStatus> checkStatus(@RequestBody String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonString);
        JsonNode certNode = jsonNode.get("OCSP Request Data").get("Requestor List").get("Cerificate ID");
        String serialNumber = certNode.get("Serial Number").asText();
        String issuerNameHash = certNode.get("Issuer Name Hash").asText();
        String issuerKeyHash = certNode.get("Issuer Key Hash").asText();
        String hashAlgorithm = certNode.get("Hash Algorithm").asText();
        CertificateIdDTO certificateIdDTO = new CertificateIdDTO(serialNumber, issuerNameHash, issuerKeyHash, hashAlgorithm);

        return new ResponseEntity<>(ocspService.checkStatus(certificateIdDTO), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCertificateDTO> revoke(@Valid @RequestBody RevokeCertificateDTO revokeCertDTO) throws Exception {
        return new ResponseEntity<>(ocspService.revoke(revokeCertDTO), HttpStatus.OK);
    }
}
