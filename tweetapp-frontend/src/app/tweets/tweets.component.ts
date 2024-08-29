import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Likes, Tweets, TweetsDataService } from '../service/data/tweets-data.service';

@Component({
  selector: 'app-tweets',
  templateUrl: './tweets.component.html',
  styleUrls: ['./tweets.component.css']
})
export class TweetsComponent implements OnInit {

  like: Likes = {
    id: 0,
    likes: 0
  }

  likes: Likes[] = []

  tweets: Tweets[] = [];
  tweetsToShow = ''
  loginId = ''

  constructor(private service: TweetsDataService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.loginId = this.route.snapshot.params['loginId']
      this.service.getAllTweets().subscribe(response => {
        this.tweets = response

        if (!this.tweets.length) {
          this.tweetsToShow = "No tweets to show."
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
        this.service.updateLikes(-1, new Likes(id, 1), this.loginId).subscribe(data => {
          this.router.navigate(['tweets', this.loginId, 'all']).then(() => {
            window.location.reload();
          });
        })
      }
      else {
        this.like.likes = this.like.likes + 1;

          this.service.updateLikes(this.like.id, this.like, this.loginId).subscribe(data => {
            this.router.navigate(['tweets', this.loginId, 'all']).then(() => {
              window.location.reload();
            });
          })
      }

    })
  }

  checkComments(tweetId: number) {
    this.router.navigate(['tweets', this.loginId, 'comments', tweetId])
  }

}
