export class CertificateID {
    serialNumber: string;
    issuerNameHash: string;
    hashAlgorithm: string;
    issuerKeyHash: string;

    constructor(serialNumber: string, issuerNameHash: string, hashAlgorithm: string, issuerKeyHash: string) {
        this.serialNumber = serialNumber;
        this.issuerNameHash = issuerNameHash;
        this.hashAlgorithm = hashAlgorithm;
        this.issuerKeyHash = issuerKeyHash;
    }


}