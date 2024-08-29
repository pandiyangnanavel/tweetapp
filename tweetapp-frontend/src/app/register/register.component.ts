import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserDataService, Users } from '../service/data/users/user-data.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user : Users[] = []

  firstName = ''
  lastName = ''
  loginId = ''
  password = ''
  confirmPassword = ''
  contactNumber = ''
  Email = ''

  errorBool=false;

  constructor(private service: UserDataService, private router : Router) { }

  ngOnInit(): void {
  }

  register() {

    this.service.register(new Users(this.firstName,this.lastName,this.Email,this.loginId,this.password,this.contactNumber)).subscribe(
      response => {
        this.router.navigate(['tweets/login'])
      },
      error => {
        this.errorBool=true
      }
    )

  }

}
