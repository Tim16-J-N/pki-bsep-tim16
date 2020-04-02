package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateCertificateDTO {

    @NotNull
    private CertificateDTO certificate;

    @NotEmpty(message = "Alias is empty.")
    private String alias;

    @NotEmpty(message = "Password to is empty.")
    private String password;

    public CreateCertificateDTO(){

    }
    public CertificateDTO getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateDTO certificate) {
        this.certificate = certificate;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
