import { UserService } from '../service/user.service';
import { UserTokenState } from '../model/userTokenState';
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    userTokenState: UserTokenState;
    constructor(public userService: UserService) { }
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        this.userTokenState = JSON.parse(localStorage.getItem("LoggedInUser"));

        if (this.userTokenState) {
            if (this.userTokenState.accessToken) {
                request = request.clone({
                    setHeaders: {
                        Authorization: `Bearer ${this.userTokenState.accessToken}`
                    }
                });
            }
        }

        return next.handle(request);
    }
}
