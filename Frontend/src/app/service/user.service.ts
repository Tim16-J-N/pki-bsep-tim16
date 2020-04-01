import { UserLoginRequest } from './../model/userLoginRequest';
import { UserTokenState } from './../model/userTokenState';
import { map } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  url = environment.baseUrl + environment.auth;
  access_token = null;
  req: UserTokenState
  loggedInUserSubject: BehaviorSubject<UserTokenState>;
  loggedInUser: Observable<UserTokenState>;
  loggedInSuccess: BehaviorSubject<UserTokenState> = new BehaviorSubject<UserTokenState>(null);

  constructor(private httpClient: HttpClient, private router: Router) {
    this.loggedInUserSubject = new BehaviorSubject<UserTokenState>(JSON.parse(localStorage.getItem('LoggedInUser')));
    this.loggedInUser = this.loggedInUserSubject.asObservable();
  }

  getLoggedInUser(): UserTokenState {
    return this.loggedInUserSubject.value;
  }

  login(user: UserLoginRequest) {
    return this.httpClient.post(this.url + "/login", user).pipe(map((res: UserTokenState) => {
      this.access_token = res.accessToken;
      localStorage.setItem('LoggedInUser', JSON.stringify(res));
      this.loggedInUserSubject.next(res);
    }));
  }

  getToken() {
    return this.access_token;
  }

  logout() {
    this.access_token = null;
    localStorage.removeItem('LoggedInUser');
    this.router.navigate(['/']);
  }

  isLoggedIn() {
    return localStorage.getItem('LoggedInUser') !== null;
  }

}
