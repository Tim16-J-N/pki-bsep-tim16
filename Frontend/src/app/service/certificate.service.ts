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

    constructor(private httpClient: HttpClient, private router: Router) { }

    public add(certificate: CreateCertificate) {
        return this.httpClient.post(this.url, certificate);
    }

    public addSelfSigned(certificate: CreateCertificate) {
        return this.httpClient.post(this.url + "/self-signed", certificate);
    }

    public getCertificates(keyStoreLevel: string, keyStorePassword: string) {
        let params = new HttpParams();
        params = params.append('role', keyStoreLevel);
        params = params.append('keyStorePassword', keyStorePassword);

        return this.httpClient.get(this.url + '/all-from-keystore', {
            params: params
        });
    }

}
