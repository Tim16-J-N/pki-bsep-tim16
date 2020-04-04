
export class KeyUsage {

    certificateSigning: boolean;
    crlSign: boolean;
    dataEncipherment: boolean;
    decipherOnly: boolean;
    digitalSignature: boolean;
    enchiperOnly: boolean;
    keyAgreement: boolean;
    keyEncipherment: boolean;
    nonRepudiation: boolean;

    constructor(certificateSigning: boolean, crlSign: boolean, dataEncipherment: boolean, decipherOnly: boolean, digitalSignature: boolean,
        enchiperOnly: boolean, keyAgreement: boolean, keyEncipherment: boolean, nonRepudiation: boolean, ) {
        this.certificateSigning = certificateSigning;
        this.crlSign = crlSign;
        this.dataEncipherment = dataEncipherment;
        this.decipherOnly = decipherOnly;
        this.digitalSignature = digitalSignature;
        this.enchiperOnly = enchiperOnly;
        this.keyAgreement = keyAgreement;
        this.keyEncipherment = keyEncipherment;
        this.nonRepudiation = nonRepudiation;
    }


}