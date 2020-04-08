package ftn.pkibseptim16.dto;

import ftn.pkibseptim16.enumeration.CertificateRole;

import javax.validation.constraints.NotEmpty;

public class RevokeCertificateDTO {

    @NotEmpty(message = "Serial Number is empty.")
    private String serialNumber;

    @NotEmpty(message = "Alias is empty.")
    private String alias;

    @NotEmpty(message = "Certificate role is empty.")
    private String certRole;

    @NotEmpty(message = "Root key store password is empty.")
    private String rootKeyStorePass;

    @NotEmpty(message = "Intermediate key store password is empty.")
    private String intermediateKeyStorePass;

    @NotEmpty(message = "End-entity key store password is empty.")
    private String endEntityKeyStorePass;

    public RevokeCertificateDTO() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

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

    public String getRootKeyStorePass() {
        return rootKeyStorePass;
    }

    public void setRootKeyStorePass(String rootKeyStorePass) {
        this.rootKeyStorePass = rootKeyStorePass;
    }

    public String getIntermediateKeyStorePass() {
        return intermediateKeyStorePass;
    }

    public void setIntermediateKeyStorePass(String intermediateKeyStorePass) {
        this.intermediateKeyStorePass = intermediateKeyStorePass;
    }

    public String getEndEntityKeyStorePass() {
        return endEntityKeyStorePass;
    }

    public void setEndEntityKeyStorePass(String endEntityKeyStorePass) {
        this.endEntityKeyStorePass = endEntityKeyStorePass;
    }
}
