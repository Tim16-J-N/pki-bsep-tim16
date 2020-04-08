package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CertificateItemDTO {

    @NotEmpty(message = "Serial Number is empty.")
    private String serialNumber;

    @NotNull
    private EntityDTO subject;

    @NotNull
    private EntityDTO issuer;

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

    @NotEmpty(message = "Alias is empty.")
    private String alias;

    @NotNull
    private CertificateIdDTO certificateID;

    public CertificateItemDTO() {

    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public EntityDTO getSubject() {
        return subject;
    }

    public void setSubject(EntityDTO subject) {
        this.subject = subject;
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

    public CertificateIdDTO getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(CertificateIdDTO certificateID) {
        this.certificateID = certificateID;
    }
}
