package ftn.pkibseptim16.dto;

import org.bouncycastle.cert.ocsp.CertificateID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CertificateIdDTO {

    @NotEmpty(message = "Serial Number is empty.")
    private String serialNumber;

    @NotEmpty(message = "Issuer Name Hash is empty.")
    private String issuerNameHash;

    @NotEmpty(message = "Issuer Key Hash is empty.")
    private String issuerKeyHash;

    @NotEmpty(message = "Hash Algorithm is empty.")
    private String hashAlgorithm;

    public CertificateIdDTO() {

    }

    public CertificateIdDTO(CertificateID certificateID) {
        this.serialNumber = certificateID.getSerialNumber().toString();
        this.issuerNameHash = new String(certificateID.getIssuerNameHash());
        this.issuerKeyHash = new String(certificateID.getIssuerKeyHash());
        this.hashAlgorithm = "sha1";
    }

    public CertificateIdDTO(@NotNull String serialNumber,
                            @NotEmpty(message = "Issuer Name Hash is empty.") String issuerNameHash,
                            @NotEmpty(message = "Issuer Key Hash is empty.") String issuerKeyHash,
                            @NotEmpty(message = "Hash Algorithm is empty.") String hashAlgorithm) {
        this.serialNumber = serialNumber;
        this.issuerNameHash = issuerNameHash;
        this.issuerKeyHash = issuerKeyHash;
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getIssuerNameHash() {
        return issuerNameHash;
    }

    public void setIssuerNameHash(String issuerNameHash) {
        this.issuerNameHash = issuerNameHash;
    }

    public String getIssuerKeyHash() {
        return issuerKeyHash;
    }

    public void setIssuerKeyHash(String issuerKeyHash) {
        this.issuerKeyHash = issuerKeyHash;
    }
}
