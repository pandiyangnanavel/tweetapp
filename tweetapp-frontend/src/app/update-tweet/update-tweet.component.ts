import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Tweets, TweetsDataService } from '../service/data/tweets-data.service';

@Component({
  selector: 'app-update-tweet',
  templateUrl: './update-tweet.component.html',
  styleUrls: ['./update-tweet.component.css']
})
export class UpdateTweetComponent implements OnInit {

  tweet: Tweets = {
    id: 0,
    loginId: '',
    tweets: '',
    date: new Date()
  }


  tweetid: number = 0

  tweetInValid = ''

  constructor(private route: ActivatedRoute, private service: TweetsDataService, private router: Router) { }

  ngOnInit(): void {
    this.tweetid = this.route.snapshot.params['tweetId']
    this.service.getTweet(this.tweetid).subscribe(response => {
      this.tweet = response
    })
  }

  updateTweet() {

    if (!this.tweet.tweets.length) {
      this.tweetInValid = "Do not leave the space blank."
    }
    else {
      this.service.updateTweet(this.tweetid, this.tweet).subscribe(response => {
        this.router.navigate(['tweets', this.tweet.loginId, 'home'])
      })
    }

  }

}
