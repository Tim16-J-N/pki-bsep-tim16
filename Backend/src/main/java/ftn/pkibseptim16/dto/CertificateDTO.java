package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class CertificateDTO {

    private BigInteger serialNumber;

    @NotNull
    private EntityDTO subject;

    @NotNull
    private EntityDTO issuer;

    @NotNull
    private Boolean authorityKeyIdentifier;

    @NotNull
    private Boolean subjectKeyIdentifier;

    @NotNull
    private Boolean subjectIsCa;

    @NotEmpty(message = "Valid from is empty.")
    private String validFrom;

    @NotEmpty(message = "Valid to is empty.")
    private String validTo;

    @NotNull
    private KeyUsageDTO keyUsage;

    @NotNull
    private ExtendedKeyUsageDTO extendedKeyUsage;

    private String alias;

    public CertificateDTO(){

    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public EntityDTO getSubject() {
        return subject;
    }

    public void setSubject(EntityDTO subject) {
        this.subject = subject;
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

    public Boolean getSubjectIsCa() {
        return subjectIsCa;
    }

    public void setSubjectIsCa(Boolean subjectIsCa) {
        this.subjectIsCa = subjectIsCa;
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

    public EntityDTO getIssuer() {
        return issuer;
    }

    public void setIssuer(EntityDTO issuer) {
        this.issuer = issuer;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
