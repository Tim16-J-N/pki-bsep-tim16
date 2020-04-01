export class Template {
    authorityKeyId: boolean;
    subjectKeyId: boolean;
    CA: boolean;
    digitalSigniture: boolean;
    keyEncipherment: boolean;
    certSigning: boolean;
    CRLSign: boolean;
    TLSWebServerAuth: boolean;
    TLSWebClientAuth: boolean;
    codeSigning: boolean;

    constructor(
        authorityKeyId: boolean, subjectKeyId: boolean, CA: boolean, digitalSigniture: boolean, keyEncipherment: boolean,
        certSigning: boolean, CRLSign: boolean, TLSWebServerAuth: boolean, TLSWebClientAuth: boolean, codeSigning: boolean
    ) {
        this.authorityKeyId = authorityKeyId;
        this.subjectKeyId = subjectKeyId;
        this.CA = CA;
        this.digitalSigniture = digitalSigniture;
        this.keyEncipherment = keyEncipherment;
        this.certSigning = certSigning;
        this.CRLSign = CRLSign;
        this.TLSWebServerAuth = TLSWebServerAuth;
        this.TLSWebClientAuth = TLSWebClientAuth;
        this.codeSigning = codeSigning;
    }
}