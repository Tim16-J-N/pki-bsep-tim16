import { CreateCertificate } from './../model/create.certificate';
import { Certificate } from './../model/certificate';
import { HttpClient } from '@angular/common/http';
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
}
