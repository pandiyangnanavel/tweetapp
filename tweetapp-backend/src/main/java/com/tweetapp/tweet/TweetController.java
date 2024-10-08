package com.tweetapp.tweet;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tweetapp.authorization.AuthorizationController;
import com.tweetapp.exception.InvalidTokenException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TweetController {

	Logger log = LoggerFactory.getLogger(TweetController.class);

	@Autowired
	private TweetService tweetService;

	@Autowired
	AuthorizationController authController;

//	get details of a tweet through tweetId

	@GetMapping("/tweet/{id}")
	public Tweet getTweet(@RequestHeader(name = "Authorization") String token, @PathVariable long id)
			throws InvalidTokenException {
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		Tweet t = tweetService.find(id);
		log.info("Details of tweet of id" + id + " : " + t);
		return t;
	}

//	get all tweets

	@GetMapping(path = "/tweets/all")
	public List<Tweet> getAllTweets(@RequestHeader(name = "Authorization") String token) throws InvalidTokenException {
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		return tweetService.findAll();
	}

//	get all tweets of a particular user through loginId

	@GetMapping(path = "/tweets/{loginId}")
	public List<Tweet> getAllTweetsOfUser(@RequestHeader(name = "Authorization") String token,
			@PathVariable String loginId) throws InvalidTokenException {
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		return tweetService.findByLoginId(loginId);
	}

//	post a new tweet by providing tweet details and loginId

	@PostMapping("/tweets/{loginId}/add")
	public ResponseEntity<Void> postTweets(@RequestHeader(name = "Authorization") String token, @PathVariable String loginId, @RequestBody Tweet t) throws InvalidTokenException {

		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		Tweet createdTweet = tweetService.updateTweets(t);

		log.info("Posting a new Tweet: " + createdTweet);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTweet.getId())
				.toUri();

		return ResponseEntity.created(uri).build();
	}

//	delete a tweet through tweetId

	@DeleteMapping("/tweets/delete/{id}")
	public ResponseEntity<Void> deleteTweets(@RequestHeader(name = "Authorization") String token, @PathVariable long id) throws InvalidTokenException {
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		tweetService.deleteTweet(id);

		log.info("Deleted tweet for id " + id);

		return ResponseEntity.noContent().build();
	}

//	updating a tweet through tweetId

	@PutMapping("/tweets/update/{id}")
	public ResponseEntity<Tweet> updateTweets(@RequestHeader(name = "Authorization") String token,@PathVariable long id, @RequestBody Tweet tweet) throws InvalidTokenException {
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		tweetService.deleteTweet(id);
		Tweet t = tweetService.updateTweet(id, tweet);

		log.info("updating tweet for id " + id + " : " + t);

		return new ResponseEntity<Tweet>(t, HttpStatus.OK);
	}

}
