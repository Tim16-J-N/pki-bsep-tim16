package ftn.pkibseptim16.dto;

import ftn.pkibseptim16.enumeration.CertificateRole;

import javax.validation.constraints.NotEmpty;

public class DownloadCertificateDTO {

    @NotEmpty(message = "Certificate role is empty.")
    private String certRole;

    @NotEmpty(message = "Password is empty.")
    private String keyStorePassword;

    @NotEmpty(message = "Alias is empty.")
    private String alias;

    public String getCertRole() {
        return certRole;
    }

    public CertificateRole returnCertRoleToEnum() {
        switch (certRole.toLowerCase()) {
            case "root":
                return CertificateRole.ROOT;
            case "intermediate":
                return CertificateRole.INTERMEDIATE;
            case "end-entity":
                return CertificateRole.END_ENTITY;
            default:
                return null;
        }
    }

    public void setCertRole(String certRole) {
        this.certRole = certRole;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
