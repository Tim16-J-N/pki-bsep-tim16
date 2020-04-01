
import { UserService } from './../../service/user.service';
import { UserTokenState } from './../../model/userTokenState';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Breakpoints, BreakpointObserver } from '@angular/cdk/layout';
import { Router, NavigationEnd } from '@angular/router';
import { map, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  user: UserTokenState

  constructor(
    private userService: UserService,
    private router: Router,
    private breakpointObserver: BreakpointObserver
  ) { }

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map((result) => result.matches),
      shareReplay()
    );

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        if (this.isLoggedIn()) {
          this.user = this.userService.getLoggedInUser();
        }
      }
    });
  }

  isLoggedIn() {
    return this.userService.isLoggedIn();
  }

  logout() {
    this.userService.logout();
  }

}
