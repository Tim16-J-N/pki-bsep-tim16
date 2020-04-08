import { HttpClient } from '@angular/common/http';
import { CertificateID } from './../model/certificate.id';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class OCSPService {
    url = environment.baseUrl + environment.ocsp;

    constructor(private httpClient: HttpClient) { }

    public checkStatus(certificateID: CertificateID) {
        const OCSPRequest = {
            "OCSP Request Data": {
                "Version": "1 (0x0)",
                "Requestor List": {
                    "Cerificate ID": {
                        "Hash Algorithm": certificateID.hashAlgorithm,
                        "Issuer Name Hash": certificateID.issuerNameHash,
                        "Issuer Key Hash": certificateID.issuerKeyHash,
                        "Serial Number": certificateID.serialNumber
                    }
                }
            }

        }
        return this.httpClient.post(this.url + "/check-status", OCSPRequest);
    }

    public revoke(serialNumber: string, alias: string, certRole: string, rootKeyStorePass: string,
        intermediateKeyStorePass: string, endEntityKeyStorePass: string) {
        return this.httpClient.put(this.url,
            { serialNumber, alias, certRole, rootKeyStorePass, intermediateKeyStorePass, endEntityKeyStorePass });
    }
}