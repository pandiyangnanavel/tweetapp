import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Observer } from 'rxjs';

export class Users {
  constructor(public firstName: string, public lastName: string, public email: string, public loginId: string, public password: string, public contactNumber: string) { }
}

@Injectable({
  providedIn: 'root'
})
export class UserDataService {

  constructor(private http: HttpClient) { }

  register(user: Users) {
    return this.http.post("http://localhost:8080/tweets/register", user)
  }

  getUsers() {
    return this.http.get<Users[]>("http://localhost:8080/tweets/users/all")
  }

  getUser(loginId: String) {
    return this.http.get<Users>(`http://localhost:8080/tweets/user/${loginId}`)
  }

  updatePassword(loginId: String, user: Users) {
    return this.http.put(`http://localhost:8080/tweets/${loginId}/forgot`, user);
  }

  searchByRegex(str : string){
    return this.http.get<Users[]>(`http://localhost:8080/tweets/user/search/${str}`)
  }
  authenticateUser(loginId : string, password:string): Observable<string>{
    return this.http.get(`http://localhost:8080/authenticate/${loginId}/${password}`, { responseType: 'text'})
  }
}
