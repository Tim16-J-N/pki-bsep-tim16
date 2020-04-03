import { Entity } from 'src/app/model/entity';
import { HttpClient } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SubjectService {
    url = environment.baseUrl + environment.entity;
    createSuccessEmitter = new Subject<Entity>();

    constructor(private httpClient: HttpClient, private router: Router) { }

    public add(subject: Entity) {
        return this.httpClient.post(this.url + "/create-subject", subject);
    }

    public getAll(): any {
        return this.httpClient.get(this.url + "/all");
    }

}
