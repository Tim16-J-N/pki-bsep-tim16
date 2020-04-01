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

    public add(certificate: Certificate) {
        return this.httpClient.post(this.url, certificate);
    }


}
