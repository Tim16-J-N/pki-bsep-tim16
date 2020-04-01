import { UserLoginRequest } from './../../model/userLoginRequest';
import { UserService } from './../../service/user.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  ngOnInit() {
    this.redirectToHomePage();

    this.loginForm = this.formBuilder.group({
      username: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required)
    });
  }

  login() {
    const user = new UserLoginRequest(this.loginForm.value.username, this.loginForm.value.password);

    this.userService.login(user).subscribe(
      () => {
        console.log("login page login");
        this.toastr.success("You have successfuly logged in!", 'Login');
        this.redirectToHomePage();
      },
      (err) => {
        console.log(err);
        this.toastr.error("Invalid username/password. Please try again.", 'Login');
      }
    )
  }

  redirectToHomePage() {
    if (this.userService.isLoggedIn()) {
      this.router.navigate(['/certificates']);
    }
  }

}
