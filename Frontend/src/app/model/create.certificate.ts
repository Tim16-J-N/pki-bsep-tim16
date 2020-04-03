import { Certificate } from './certificate';

export class CreateCertificate {
    certificateData: Certificate;
    issuerCertificate: Certificate;
    alias: string;
    keyStorePassword: string;
    privateKeyPassword: string;
    issuerKeyStorePassword: string;
    issuerPrivateKeyPassword: string;

    constructor(certificateData: Certificate, issuerCertificate: Certificate, alias: string, keyStorePassword: string, privateKeyPassword: string,
        issuerPrivateKeyPassword?: string, issuerKeyStorePassword?: string) {
        this.certificateData = certificateData;
        this.issuerCertificate = issuerCertificate;
        this.alias = alias;
        this.keyStorePassword = keyStorePassword;
        this.privateKeyPassword = privateKeyPassword;
        this.issuerPrivateKeyPassword = issuerPrivateKeyPassword;
        this.issuerKeyStorePassword = issuerKeyStorePassword;
    }
}