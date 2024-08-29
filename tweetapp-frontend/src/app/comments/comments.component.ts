import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Comments, Tweets, TweetsDataService } from '../service/data/tweets-data.service';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

  comments: Comments[] = []

  tweetid: number = 0

  tweet: Tweets = {
    id: 0,
    loginId: '',
    tweets: '',
    date: new Date()
  }

  noComments = ''
  comment = ''
  messageInComment = ''
  name = ''
  tags = ''

  constructor(private route: ActivatedRoute, private service: TweetsDataService, private router: Router) { }

  ngOnInit(): void {

    this.tweetid = this.route.snapshot.params['tweetId']
    this.name = this.route.snapshot.params['loginId']
    this.service.getTweet(this.tweetid).subscribe(response => {
      this.tweet = response
    })

    this.service.getComments(this.tweetid).subscribe(data => {
      this.comments = data
      if (!this.comments.length) {
        this.noComments = "No comments to show."
      }
    })
  }

  postComment() {
    if (this.comment === '') {
      this.messageInComment = "Please type something before replying to tweet"
    }
    else {
      this.messageInComment = ''
      this.service.postComment(this.tweetid, new Comments(this.comment + " " + this.tags, this.tweetid, new Date(), this.name)).subscribe(response => {
        this.router.navigate(['tweets', this.name, 'comments', this.tweetid]).then(() => {
          window.location.reload();
        });
      })
    }
  }

}
