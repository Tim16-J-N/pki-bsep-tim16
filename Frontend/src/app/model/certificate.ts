import { ExtendedKeyUsage } from './extended.key.usage';
import { KeyUsage } from './key.usage';
import { Entity } from './entity';
import { DateTime } from 'luxon';
export class Certificate {
    serialNumber: number;
    subject: Entity;
    issuer: Entity;
    validFrom: DateTime;
    validTo: DateTime;
    authorityKeyIdentifier: boolean;
    subjectKeyIdentifier: boolean;
    subjectIsCa: boolean;
    keyUsage: KeyUsage;
    extendedKeyUsage: ExtendedKeyUsage;
    constructor(subject: Entity, issuer: Entity, validFrom: DateTime, validTo: DateTime, authorityKeyIdentifier: boolean,
        subjectKeyIdentifier: boolean, subjectIsCa: boolean, keyUsage: KeyUsage, extendedKeyUsage: ExtendedKeyUsage, serialNumber?: number) {
        this.subject = subject;
        this.issuer = issuer;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.subjectIsCa = subjectIsCa;
        this.serialNumber = serialNumber;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
    }
}