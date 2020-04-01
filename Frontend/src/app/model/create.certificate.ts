import { Certificate } from './certificate';

export class CreateCertificate {
    certificate: Certificate;
    alias: string;
    password: string;
    constructor(certificate: Certificate, alias: string, password: string) {
        this.certificate = certificate;
        this.alias = alias;
        this.password = password;
    }
}