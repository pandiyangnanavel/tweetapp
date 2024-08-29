import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../service/auth/authentication.service';
import { UserDataService } from '../service/data/users/user-data.service';
import { of } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginId = ''
  password = ''

  errorMessage = 'Invalid Credentials'
  invalidLogin = false

  constructor(private router: Router, private authenticationService: AuthenticationService, private service : UserDataService) { }

  ngOnInit(): void {
  }

  handleLogin() {

    this.service.authenticateUser(this.loginId, this.password).subscribe(
      {
        next: data => {
          localStorage.setItem('token', data)
          localStorage.setItem('authUser', this.loginId)
          this.router.navigate(['tweets', this.loginId,'home'])
          this.invalidLogin = false
          console.log("Login Success");
        },
        error: err => {
          this.invalidLogin = true
          console.error(err);
        }
      }
      
    )

  }

}
