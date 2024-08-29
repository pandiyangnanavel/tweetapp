import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Likes, Tweets, TweetsDataService } from '../service/data/tweets-data.service';
import { UserDataService, Users } from '../service/data/users/user-data.service';

@Component({
  selector: 'app-user-tweets',
  templateUrl: './user-tweets.component.html',
  styleUrls: ['./user-tweets.component.css']
})
export class UserTweetsComponent implements OnInit {

  name1 = ''
  name2 = ''
  tweetsToShow = ''

  tweets: Tweets[] = [];

  user: Users = {
    firstName: '',
    lastName: '',
    email: '',
    loginId: '',
    password: '',
    contactNumber: ''
  }

  like: Likes = {
    id: 0,
    likes: 0
  }

  likes: Likes[] = []
  

  constructor(private route: ActivatedRoute, private service: TweetsDataService, private router: Router, private serv: UserDataService) { }

  ngOnInit(): void {
    this.name1 = this.route.snapshot.params['loginId1']
    this.name2 = this.route.snapshot.params['loginId2']

    this.serv.getUser(this.name1).subscribe(data => {
      this.user = data
    })

    this.service.getTweetsofUser(this.name1).subscribe(response => {
      this.tweets = response
      if (!this.tweets.length) {
        this.tweetsToShow = "No tweets to show for this user."
      }
    })



    this.service.getAllLikes().subscribe(data => {
      this.likes = data
    })

  }

  updateLikes(id: number) {
    this.service.getLikes(id).subscribe(response => {
      this.like = response

      if (this.like === null) {
        this.service.updateLikes(-1, new Likes(id, 1),this.name2).subscribe(data => {
          this.router.navigate(['tweets', this.name2, 'user',this.name1]).then(() => {
            window.location.reload();
          });
        })
      }
      else {
        this.like.likes = this.like.likes + 1;

        this.service.updateLikes(this.like.id, this.like,this.name2).subscribe(data => {
          this.router.navigate(['tweets', this.name2, 'user',this.name1]).then(() => {
            window.location.reload();
          });
        })
      }

    })
  }

  checkComments(tweetId: number) {
    this.router.navigate(['tweets', this.name2, 'comments', tweetId])
  }


}
