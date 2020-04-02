package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotNull;

public class KeyUsageDTO {
    @NotNull
    private Boolean certificateSigning;

    @NotNull
    private Boolean crlSign;

    @NotNull
    private Boolean dataEncipherment;

    @NotNull
    private Boolean decipherOnly;

    @NotNull
    private Boolean digitalSignature;

    @NotNull
    private Boolean enchiperOnly;

    @NotNull
    private Boolean keyAgreement;

    @NotNull
    private Boolean keyEncipherment;

    @NotNull
    private Boolean nonRepudiation;

    public KeyUsageDTO(){

    }
    public Boolean getCertificateSigning() {
        return certificateSigning;
    }

    public void setCertificateSigning(Boolean certificateSigning) {
        this.certificateSigning = certificateSigning;
    }

    public Boolean getCrlSign() {
        return crlSign;
    }

    public void setCrlSign(Boolean crlSign) {
        this.crlSign = crlSign;
    }

    public Boolean getDataEncipherment() {
        return dataEncipherment;
    }

    public void setDataEncipherment(Boolean dataEncipherment) {
        this.dataEncipherment = dataEncipherment;
    }

    public Boolean getDecipherOnly() {
        return decipherOnly;
    }

    public void setDecipherOnly(Boolean decipherOnly) {
        this.decipherOnly = decipherOnly;
    }

    public Boolean getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(Boolean digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public Boolean getEnchiperOnly() {
        return enchiperOnly;
    }

    public void setEnchiperOnly(Boolean enchiperOnly) {
        this.enchiperOnly = enchiperOnly;
    }

    public Boolean getKeyAgreement() {
        return keyAgreement;
    }

    public void setKeyAgreement(Boolean keyAgreement) {
        this.keyAgreement = keyAgreement;
    }

    public Boolean getKeyEncipherment() {
        return keyEncipherment;
    }

    public void setKeyEncipherment(Boolean keyEncipherment) {
        this.keyEncipherment = keyEncipherment;
    }

    public Boolean getNonRepudiation() {
        return nonRepudiation;
    }

    public void setNonRepudiation(Boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }
}