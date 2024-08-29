import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  loginId = ''

  constructor() {}
  
  isUserLoggedIn() {
    let user = localStorage.getItem('token');
    return !(user === null);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem("authUser");
  }

  getToken() {
    return localStorage.getItem("token");
  }

  getUser(){
    return localStorage.getItem("authUser");
  }
}
