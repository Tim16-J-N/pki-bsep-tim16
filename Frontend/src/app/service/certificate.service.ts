import { CertificateID } from './../model/certificate.id';
import { CreateCertificate } from './../model/create.certificate';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';


@Injectable({
    providedIn: 'root'
})
export class CertificateService {
    url = environment.baseUrl + environment.certificate;

    constructor(private httpClient: HttpClient) { }

    public add(certificate: CreateCertificate) {
        return this.httpClient.post(this.url, certificate);
    }

    public addSelfSigned(certificate: CreateCertificate) {
        return this.httpClient.post(this.url + "/self-signed", certificate);
    }

    public getCACertificates(id: number, rootKeyStoragePassword: string, intermediateKeyStoragePassword: string): any {
        let params = new HttpParams();
        if (rootKeyStoragePassword != null) {
            params = params.append('rootKeyStoragePassword', rootKeyStoragePassword);
        }
        if (intermediateKeyStoragePassword != null) {
            params = params.append('intermediateKeyStoragePassword', intermediateKeyStoragePassword);
        }

        return this.httpClient.get(this.url + "/" + id, {
            params: params
        });
    }

    public getCertificates(keyStoreLevel: string, keyStorePassword: string) {
        let params = new HttpParams();
        params = params.append('role', keyStoreLevel);
        params = params.append('keyStorePassword', keyStorePassword);

        return this.httpClient.get(this.url + '/all', {
            params: params
        });
    }

    public download(certRole: string, keyStorePassword: string, alias: string) {
        return this.httpClient.post(this.url + "/download", { certRole, keyStorePassword, alias });
    }

}
