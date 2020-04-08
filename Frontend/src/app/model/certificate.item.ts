import { CertificateID } from './certificate.id';
import { ExtendedKeyUsage } from './extended.key.usage';
import { KeyUsage } from './key.usage';
import { Entity } from './entity';
import { DateTime } from 'luxon';

export class CertificateItem {
    serialNumber: string;
    subject: Entity;
    issuer: Entity;
    validFrom: DateTime;
    validTo: DateTime;
    subjectIsCa: boolean;
    keyUsage: KeyUsage;
    extendedKeyUsage: ExtendedKeyUsage;
    alias: string;
    certificateID: CertificateID;

    constructor(subject: Entity, issuer: Entity, validFrom: DateTime, validTo: DateTime, subjectIsCa: boolean,
        keyUsage: KeyUsage, extendedKeyUsage: ExtendedKeyUsage, alias: string, serialNumber: string, certificateID: CertificateID) {
        this.subject = subject;
        this.issuer = issuer;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.subjectIsCa = subjectIsCa;
        this.serialNumber = serialNumber;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.alias = alias;
        this.certificateID = certificateID;
    }

}