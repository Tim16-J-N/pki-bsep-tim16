import { HttpClient } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ValidationService {
    url = environment.baseUrl + environment.validation;

    constructor(private httpClient: HttpClient) { }

    public checkValidity(certRole: string, keyStorePassword: string, alias: string) {
        return this.httpClient.post(this.url, { certRole, keyStorePassword, alias });
    }
}