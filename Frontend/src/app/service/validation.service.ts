import { CertAccessInfo } from './../model/cert.acces.info';
import { HttpClient } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ValidationService {
    url = environment.baseUrl + environment.validation;

    constructor(private httpClient: HttpClient) { }

    public checkValidity(certAccessInfo: CertAccessInfo) {
        return this.httpClient.post(this.url, certAccessInfo);
    }
}