package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateCertificateDTO {

    @NotNull
    private CertificateDTO certificateData;

    @NotNull
    private CertificateDTO issuerCertificate;

    @NotEmpty(message = "Alias is empty.")
    private String alias;

    @NotEmpty(message = "Password to is empty.")
    private String keyStorePassword;


    @NotEmpty(message="Password for private key is empty")
    private String privateKeyPassword;

    private String issuerKeyStorePassword;

    private String issuerPrivateKeyPassword;

    public CreateCertificateDTO(){

    }
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }

    public void setPrivateKeyPassword(String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }

    public String getIssuerPrivateKeyPassword() {
        return issuerPrivateKeyPassword;
    }

    public void setIssuerPrivateKeyPassword(String issuerPrivateKeyPassword) {
        this.issuerPrivateKeyPassword = issuerPrivateKeyPassword;
    }

    public CertificateDTO getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(CertificateDTO certificateData) {
        this.certificateData = certificateData;
    }

    public CertificateDTO getIssuerCertificate() {
        return issuerCertificate;

    }

    public void setIssuerCertificate(CertificateDTO issuerCertificate) {
        this.issuerCertificate = issuerCertificate;
    }

    public String getIssuerKeyStorePassword() {
        return issuerKeyStorePassword;
    }

    public void setIssuerKeyStorePassword(String issuerKeyStorePassword) {
        this.issuerKeyStorePassword = issuerKeyStorePassword;

    }

}
