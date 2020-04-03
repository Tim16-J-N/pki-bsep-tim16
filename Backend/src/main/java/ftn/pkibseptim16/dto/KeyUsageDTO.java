package ftn.pkibseptim16.dto;

import org.bouncycastle.asn1.x509.KeyUsage;

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

    public KeyUsageDTO() {

    }

    public KeyUsageDTO(boolean[] keyUsage) {
       this.digitalSignature = keyUsage[0];
       this.nonRepudiation = keyUsage[1];
       this.keyEncipherment = keyUsage[2];
       this.dataEncipherment = keyUsage[3];
       this.keyAgreement = keyUsage[4];
       this.certificateSigning = keyUsage[5];
       this.crlSign = keyUsage[6];
       this.enchiperOnly = keyUsage[7];
       this.decipherOnly = keyUsage[8];
    }
    public Boolean getCertificateSigning() {
        return certificateSigning;
    }

    public int getCertificateSigningInt() {
        return certificateSigning ? KeyUsage.keyCertSign : 0;
    }

    public void setCertificateSigning(Boolean certificateSigning) {
        this.certificateSigning = certificateSigning;
    }

    public Boolean getCrlSign() {
        return crlSign;
    }

    public int getCrlSignInt() {
        return crlSign ? KeyUsage.cRLSign : 0;
    }

    public void setCrlSign(Boolean crlSign) {
        this.crlSign = crlSign;
    }

    public Boolean getDataEncipherment() {
        return dataEncipherment;
    }

    public int getDataEnciphermentInt() {
        return dataEncipherment ? KeyUsage.dataEncipherment : 0;
    }

    public void setDataEncipherment(Boolean dataEncipherment) {
        this.dataEncipherment = dataEncipherment;
    }

    public Boolean getDecipherOnly() {
        return decipherOnly;
    }

    public int getDecipherOnlyInt() {
        return decipherOnly ? KeyUsage.decipherOnly : 0;
    }

    public void setDecipherOnly(Boolean decipherOnly) {
        this.decipherOnly = decipherOnly;
    }

    public Boolean getDigitalSignature() {
        return digitalSignature;
    }

    public int getDigitalSignatureInt() {
        return digitalSignature ? KeyUsage.digitalSignature : 0;
    }

    public void setDigitalSignature(Boolean digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public Boolean getEnchiperOnly() {
        return enchiperOnly;
    }

    public int getEnchiperOnlyInt() {
        return enchiperOnly ? KeyUsage.encipherOnly : 0;
    }

    public void setEnchiperOnly(Boolean enchiperOnly) {
        this.enchiperOnly = enchiperOnly;
    }

    public Boolean getKeyAgreement() {
        return keyAgreement;
    }

    public int getKeyAgreementInt() {
        return keyAgreement ? KeyUsage.keyAgreement : 0;
    }

    public void setKeyAgreement(Boolean keyAgreement) {
        this.keyAgreement = keyAgreement;
    }

    public Boolean getKeyEncipherment() {
        return keyEncipherment;
    }

    public int getKeyEnciphermentInt() {
        return keyEncipherment ? KeyUsage.keyEncipherment : 0;
    }

    public void setKeyEncipherment(Boolean keyEncipherment) {
        this.keyEncipherment = keyEncipherment;
    }

    public Boolean getNonRepudiation() {
        return nonRepudiation;
    }

    public int getNonRepudiationInt() {
        return nonRepudiation ? KeyUsage.nonRepudiation : 0;
    }

    public void setNonRepudiation(Boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }

    public boolean isEnabled() {
        return certificateSigning || crlSign || dataEncipherment || decipherOnly || digitalSignature || enchiperOnly ||
                keyAgreement || keyEncipherment || nonRepudiation;
    }


}
