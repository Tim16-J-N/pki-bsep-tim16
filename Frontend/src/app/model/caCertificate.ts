import { ExtendedKeyUsage } from './extended.key.usage';
import { KeyUsage } from './key.usage';

import { DateTime } from 'luxon';
export class CACertificate {
    serialNumber: number;
    issuerUniqueId: number;
    validFrom: DateTime;
    validTo: DateTime;
    authorityKeyIdentifier: boolean;
    subjectKeyIdentifier: boolean;
    keyUsage: KeyUsage;
    extendedKeyUsage: ExtendedKeyUsage;
    alias: string;

    constructor(issuerUniqueId: number, validFrom: DateTime, validTo: DateTime, authorityKeyIdentifier: boolean,
        subjectKeyIdentifier: boolean, keyUsage: KeyUsage, extendedKeyUsage: ExtendedKeyUsage, alias: string, serialNumber: number, publicKey: string) {
        this.issuerUniqueId = issuerUniqueId;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.serialNumber = serialNumber;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.alias = alias;
    }
}