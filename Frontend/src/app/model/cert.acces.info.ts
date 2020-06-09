export class CertAccessInfo {
    certRole: string;
    keyStorePassword: string;
    alias: string;

    constructor(certRole: string, keyStorePassword: string, alias: string) {
        this.certRole = certRole;
        this.keyStorePassword = keyStorePassword;
        this.alias = alias;
    }

}