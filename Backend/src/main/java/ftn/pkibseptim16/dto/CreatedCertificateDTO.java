package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.security.cert.X509Certificate;

public class CreatedCertificateDTO {
    @NotEmpty
    private BigInteger serialNumber;

    @NotEmpty(message = "Valid from is empty.")
    private String validFrom;

    @NotEmpty(message = "Valid to is empty.")
    private String validTo;

    public CreatedCertificateDTO(){

    }
    public CreatedCertificateDTO(X509Certificate x509Certificate){
        this.serialNumber = x509Certificate.getSerialNumber();
        this.validFrom = x509Certificate.getNotBefore().toString();
        this.validTo = x509Certificate.getNotAfter().toString();
    }
    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}
