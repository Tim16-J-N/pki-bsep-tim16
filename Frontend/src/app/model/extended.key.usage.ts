
export class ExtendedKeyUsage {

    serverAuth: boolean;
    clientAuth: boolean;
    codeSigning: boolean;
    emailProtection: boolean;
    timeStamping: boolean;
    ocspSigning: boolean;
    constructor(serverAuth: boolean, clientAuth: boolean, codeSigning: boolean, emailProtection: boolean, timeStamping: boolean,
        ocspSigning: boolean) {
        this.serverAuth = serverAuth;
        this.clientAuth = clientAuth;
        this.codeSigning = codeSigning;
        this.emailProtection = emailProtection;
        this.timeStamping = timeStamping;
        this.ocspSigning = ocspSigning;
    }
}