import { ExtendedKeyUsage } from './extended.key.usage';
import { KeyUsage } from './key.usage';
import { Entity } from './entity';
import { DateTime } from 'luxon';

export class Certificate {
    serialNumber: string;
    subject: Entity;
    issuer: Entity;
    validFrom: DateTime;
    validTo: DateTime;
    authorityKeyIdentifier: boolean;
    subjectKeyIdentifier: boolean;
    subjectIsCa: boolean;
    keyUsage: KeyUsage;
    extendedKeyUsage: ExtendedKeyUsage;
    alias: string;
    expired: boolean;
    constructor(subject: Entity, issuer: Entity, validFrom: DateTime, validTo: DateTime, authorityKeyIdentifier: boolean,
        subjectKeyIdentifier: boolean, subjectIsCa: boolean, keyUsage: KeyUsage, extendedKeyUsage: ExtendedKeyUsage, alias?: string, serialNumber?: string, expired?: boolean) {
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
        this.alias = alias;
        this.expired = expired;
    }


}