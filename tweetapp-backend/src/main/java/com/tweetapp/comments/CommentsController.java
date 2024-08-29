package com.tweetapp.comments;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tweetapp.authorization.AuthorizationController;
import com.tweetapp.exception.InvalidTokenException;

@CrossOrigin("http://localhost:4200")
@RestController
public class CommentsController {
	
	Logger log = LoggerFactory.getLogger(Comments.class);
	
	@Autowired
	private CommentsService commentsService;
	
	@Autowired
	AuthorizationController authController;
	
//	get all comments for a particular tweetId
	
	@GetMapping("/comments/{tweetId}")
	public List<Comments> getAll(@RequestHeader(name = "Authorization") String token,@PathVariable long tweetId) throws InvalidTokenException{
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		return commentsService.getAll(tweetId);
	}
	
//	post a comment
	
	@PostMapping("/tweets/reply/{tweetId}")
	public ResponseEntity<Void> postComment(@RequestHeader(name = "Authorization") String token, @PathVariable long tweetId, @RequestBody Comments comments) throws InvalidTokenException{
		
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		
		Comments postedComment = commentsService.postComment(comments);
		
		log.info("Posted a comment for tweetId "+tweetId+" : "+postedComment);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(postedComment.getComment()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
}
