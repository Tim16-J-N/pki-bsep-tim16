package ftn.pkibseptim16.model;


import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SubjectData {

    private PublicKey publicKey;
    private X500Name x500name;
    private Long id;
    private PrivateKey privateKey;

    public SubjectData() {

    }

    public SubjectData(PublicKey publicKey, X500Name x500name,Long id,PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.x500name = x500name;
        this.id=id;
        this.privateKey = privateKey;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setX500name(X500Name x500name) {
        this.x500name = x500name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
