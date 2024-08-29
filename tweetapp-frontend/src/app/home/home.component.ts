import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Likes, Tweets, TweetsDataService } from '../service/data/tweets-data.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  tweets: Tweets[] = [];

  like: Likes = {
    id: 0,
    likes: 0
  }


  likes: Likes[] = []

  name = ''
  tweet = ''
  tweetInValid = false
  messageForTweet = ''
  id = -1
  tweetsToShow = ''


  tags = ''


  constructor(private route: ActivatedRoute, private service: TweetsDataService, private router: Router) { }

  ngOnInit(): void {
    this.name = this.route.snapshot.params['loginId']

    this.service.getTweetsofUser(this.name).subscribe(response => {
      this.tweets = response
      if (!this.tweets.length) {
        this.tweetsToShow = "You haven't tweeted recently."
      }
    })



    this.service.getAllLikes().subscribe(data => {
      this.likes = data
    })

  }

  postTweet() {
    if (this.tweet === '') {
      this.tweetInValid = true
      this.messageForTweet = "Please type something before posting the tweet."
    }
    else {
      this.tweetInValid = false
      this.service.postTweets(this.name, new Tweets(this.id, this.name, this.tweet + " " + this.tags, new Date())).subscribe(response => {
        this.tweet = ''
        this.router.navigate(['tweets', this.name, 'home']).then(() => {
          window.location.reload();
        });

      });
    }

  }

  deleteTweet(id: number) {

    this.service.deleteTweet(id).subscribe(response => {
      this.router.navigate(['tweets', this.name, 'home']).then(() => {
        window.location.reload();
      });
    })

    this.service.deleteLikes(id).subscribe()

  }

  updateTweet(id: number) {
    this.router.navigate(['tweets', this.name, 'update-tweet', id])
  }

  updateLikes(id: number) {
    this.service.getLikes(id).subscribe(response => {
      this.like = response

      if (this.like === null) {
        this.service.updateLikes(-1, new Likes(id, 1), this.name).subscribe(data => {
          this.router.navigate(['tweets', this.name, 'home']).then(() => {
            window.location.reload();
          });
        })
      }
      else {
        this.like.likes = this.like.likes + 1;

        this.service.updateLikes(this.like.id, this.like, this.name).subscribe(data => {
          this.router.navigate(['tweets', this.name, 'home']).then(() => {
            window.location.reload();
          });
        })
      }

    })
  }

  checkComments(tweetId: number) {
    this.router.navigate(['tweets', this.name, 'comments', tweetId])
  }
}

