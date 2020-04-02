package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.PublicKey;

public class CACertificateDTO {
    @NotNull
    private BigInteger serialNumber;

    @NotNull
    private Long issuerUniqueId;

    @NotNull
    private Boolean authorityKeyIdentifier;

    @NotNull
    private Boolean subjectKeyIdentifier;

    @NotEmpty(message = "Valid from is empty.")
    private String validFrom;

    @NotEmpty(message = "Valid to is empty.")
    private String validTo;

    @NotNull
    private KeyUsageDTO keyUsage;

    @NotNull
    private ExtendedKeyUsageDTO extendedKeyUsage;

    @NotNull
    private String alias;

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getIssuerUniqueId() {
        return issuerUniqueId;
    }

    public void setIssuerUniqueId(Long issuerUniqueId) {
        this.issuerUniqueId = issuerUniqueId;
    }

    public Boolean getAuthorityKeyIdentifier() {
        return authorityKeyIdentifier;
    }

    public void setAuthorityKeyIdentifier(Boolean authorityKeyIdentifier) {
        this.authorityKeyIdentifier = authorityKeyIdentifier;
    }

    public Boolean getSubjectKeyIdentifier() {
        return subjectKeyIdentifier;
    }

    public void setSubjectKeyIdentifier(Boolean subjectKeyIdentifier) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
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

    public KeyUsageDTO getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(KeyUsageDTO keyUsage) {
        this.keyUsage = keyUsage;
    }

    public ExtendedKeyUsageDTO getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(ExtendedKeyUsageDTO extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
    }
}
