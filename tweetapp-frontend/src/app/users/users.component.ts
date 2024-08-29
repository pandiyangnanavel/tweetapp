import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserDataService, Users } from '../service/data/users/user-data.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  user: Users[] = []

  loginId = ''

  str = ''
  searchMessage = ''
  val = false
  noUser = ''

  constructor(private service: UserDataService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.loginId = this.route.snapshot.params['loginId']

    this.service.getUsers().subscribe(response => {
      this.user = response
    })
  }

  search() {
    this.noUser = ''

    if (this.str === '') {
      this.searchMessage = "Please type something before searching"
    }
    else {
      this.searchMessage = ''
      this.service.searchByRegex(this.str).subscribe(data => {
        this.user = data
        if (!this.user.length) {
          this.noUser = "No Results for '" + this.str+"'"
          this.service.getUsers().subscribe(response => {
            this.user = response
          })
        }

      })
    }

  }
  all() {
    this.service.getUsers().subscribe(response => {
      this.user = response
    })
  }

}
