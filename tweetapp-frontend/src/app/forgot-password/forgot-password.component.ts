import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserDataService, Users } from '../service/data/users/user-data.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {


  loginId = ''
  newPassword = ''
  c_newPassword = ''

  errorBool=false;

  constructor(private service: UserDataService, private router: Router) { }

  ngOnInit(): void {
  }

  updatePassword() {
    this.service.getUser(this.loginId).subscribe(response => {
      if(response==null){
        this.errorBool=true
        return
      }
      response.password = this.newPassword
      this.service.updatePassword(this.loginId, response).subscribe(data => this.router.navigate(['tweets/login']))
    })

  }

}
