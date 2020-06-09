package ftn.pkibseptim16.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CertDownloadInfoDTO {

    @NotNull(message = "Certificate info not provided.")
    CertAccessInfoDTO certAccessInfo;

    @NotEmpty(message = "Root's key store password is empty.")
    String rootKeyStorePass;

    @NotEmpty(message = "Root's alias is empty.")
    String rootCertAlias;

    @NotEmpty(message = "Root's key password is empty.")
    String rootCertKeyPass;

    public CertAccessInfoDTO getCertAccessInfo() {
        return certAccessInfo;
    }

    public void setCertAccessInfo(CertAccessInfoDTO certAccessInfo) {
        this.certAccessInfo = certAccessInfo;
    }

    public String getRootKeyStorePass() {
        return rootKeyStorePass;
    }

    public void setRootKeyStorePass(String rootKeyStorePass) {
        this.rootKeyStorePass = rootKeyStorePass;
    }

    public String getRootCertAlias() {
        return rootCertAlias;
    }

    public void setRootCertAlias(String rootCertAlias) {
        this.rootCertAlias = rootCertAlias;
    }

    public String getRootCertKeyPass() {
        return rootCertKeyPass;
    }

    public void setRootCertKeyPass(String rootCertKeyPass) {
        this.rootCertKeyPass = rootCertKeyPass;
    }
}
