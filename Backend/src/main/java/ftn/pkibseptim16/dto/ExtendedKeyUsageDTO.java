package ftn.pkibseptim16.dto;

import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    public ExtendedKeyUsageDTO(List<String> extendedKeyUsage) {
        String IdKP = "1.3.6.1.5.5.7.3";
        this.serverAuth =false;
        this.clientAuth=false;
        this.codeSigning=false;
        this.emailProtection=false;
        this.ocspSigning=false;
        this.timeStamping=false;
        if(extendedKeyUsage.contains(IdKP + ".1")){
            this.serverAuth =true;
        }
        if(extendedKeyUsage.contains(IdKP + ".2")){
            this.clientAuth =true;
        }

        if(extendedKeyUsage.contains(IdKP + ".3")){
            this.codeSigning =true;
        }

        if(extendedKeyUsage.contains(IdKP + ".8")){
            this.timeStamping =true;
        }

        if(extendedKeyUsage.contains(IdKP + ".4")){
            this.emailProtection =true;
        }
        if(extendedKeyUsage.contains(IdKP + ".9")){
            this.ocspSigning =true;
        }
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

        List<KeyPurposeId> setPurposes = new ArrayList<>();

        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                setPurposes.add(keyPurposeIds[i]);
            }
        }

        KeyPurposeId[] newKeyPurposeIds = new KeyPurposeId[setPurposes.size()];
        for(int i = 0; i < setPurposes.size(); i++)
            newKeyPurposeIds[i] = setPurposes.get(i);
        return newKeyPurposeIds;
    }
}
