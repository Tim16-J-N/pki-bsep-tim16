package ftn.pkibseptim16.dto;

import org.bouncycastle.asn1.x509.KeyPurposeId;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class ExtendedKeyUsageDTO {
    @NotNull
    private Boolean serverAuth;

    @NotNull
    private Boolean clientAuth;

    @NotNull
    private Boolean codeSigning;

    @NotNull
    private Boolean emailProtection;

    @NotNull
    private Boolean timeStamping;

    @NotNull
    private Boolean ocspSigning;

    public ExtendedKeyUsageDTO() {

    }

    public Boolean getServerAuth() {
        return serverAuth;
    }

    public void setServerAuth(Boolean serverAuth) {
        this.serverAuth = serverAuth;
    }

    public Boolean getClientAuth() {
        return clientAuth;
    }

    public void setClientAuth(Boolean clientAuth) {
        this.clientAuth = clientAuth;
    }

    public Boolean getCodeSigning() {
        return codeSigning;
    }

    public void setCodeSigning(Boolean codeSigning) {
        this.codeSigning = codeSigning;
    }

    public Boolean getEmailProtection() {
        return emailProtection;
    }

    public void setEmailProtection(Boolean emailProtection) {
        this.emailProtection = emailProtection;
    }

    public Boolean getTimeStamping() {
        return timeStamping;
    }

    public void setTimeStamping(Boolean timeStamping) {
        this.timeStamping = timeStamping;
    }

    public Boolean getOcspSigning() {
        return ocspSigning;
    }

    public void setOcspSigning(Boolean ocspSigning) {
        this.ocspSigning = ocspSigning;
    }

    public boolean isEnabled() {
        return serverAuth || clientAuth || codeSigning || emailProtection || timeStamping || ocspSigning;
    }

    public KeyPurposeId[] getKeyPurposeIds() {
        Boolean[] booleans = {serverAuth, clientAuth, codeSigning, emailProtection, timeStamping, ocspSigning};
        KeyPurposeId[] keyPurposeIds = {KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth, KeyPurposeId.id_kp_codeSigning,
                KeyPurposeId.id_kp_emailProtection, KeyPurposeId.id_kp_timeStamping, KeyPurposeId.id_kp_OCSPSigning};

        ArrayList<KeyPurposeId> setPurposes = new ArrayList<>();

        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                setPurposes.add(keyPurposeIds[i]);
            }
        }

        return (KeyPurposeId[]) setPurposes.toArray();
    }
}
