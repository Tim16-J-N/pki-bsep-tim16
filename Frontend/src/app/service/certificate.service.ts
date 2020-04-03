import { CreateCertificate } from './../model/create.certificate';
import { Certificate } from './../model/certificate';
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

    public getCACertificates(rootKeyStoragePassword, intermediateKeyStoragePassword): any {
        let params = new HttpParams();
        console.log(rootKeyStoragePassword)
        console.log(intermediateKeyStoragePassword)
        if (rootKeyStoragePassword != null) {
            console.log("Haj")
            params = params.append('rootKeyStoragePassword', rootKeyStoragePassword);
        }
        if (intermediateKeyStoragePassword != null) {
            console.log("Haj 4")
            params = params.append('intermediateKeyStoragePassword', intermediateKeyStoragePassword);
        }

        return this.httpClient.get(this.url, {
            params: params
        });
    }
}
