import { Certificate } from './certificate';

export class CreateCertificate {
    certificateData: Certificate;
    issuerCertificate: Certificate;
    alias: string;
    keyStorePassword: string;
    privateKeyPassword: string;
<<<<<<< HEAD
    issuerKeyStorePassword: string;
    issuerPrivateKeyPassword: string;

    constructor(certificateData: Certificate, issuerCertificate: Certificate, alias: string, keyStorePassword: string, privateKeyPassword: string,
        issuerPrivateKeyPassword?: string, issuerKeyStorePassword?: string) {
=======
    issuerPrivateKeyPassword: string;

    constructor(certificateData: Certificate, issuerCertificate: Certificate, alias: string, keyStorePassword: string, privateKeyPassword: string, issuerPrivateKeyPassword?: string) {
>>>>>>> feature/listCertificates
        this.certificateData = certificateData;
        this.issuerCertificate = issuerCertificate;
        this.alias = alias;
        this.keyStorePassword = keyStorePassword;
        this.privateKeyPassword = privateKeyPassword;
        this.issuerPrivateKeyPassword = issuerPrivateKeyPassword;
<<<<<<< HEAD
        this.issuerKeyStorePassword = issuerKeyStorePassword;
=======
>>>>>>> feature/listCertificates
    }
}