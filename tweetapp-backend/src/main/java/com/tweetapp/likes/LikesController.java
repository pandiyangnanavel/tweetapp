package com.tweetapp.likes;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.authorization.AuthorizationController;
import com.tweetapp.exception.InvalidTokenException;

@CrossOrigin("http://localhost:4200")
@RestController
public class LikesController {
	
	Logger log = LoggerFactory.getLogger(LikesController.class);
	
	@Autowired
	private LikesService likesService;
	
	@Autowired
	AuthorizationController authController;
	
//	get likes count for a particular tweetId
	
	@GetMapping("/tweet/{id}/likes")
	public Likes getLikeCount(@RequestHeader(name = "Authorization") String token, @PathVariable long id) throws InvalidTokenException {
		
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		Likes l = likesService.getLikesCount(id);
		log.info("Details of likes for tweetId "+id+" : "+l);
		return l;
		
	}
	
//	get details of likes for all tweetId
	
	@GetMapping("/tweet/all/likes")
	public List<Likes> getAll(@RequestHeader(name = "Authorization") String token) throws InvalidTokenException{
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		return likesService.getAll();
	}
	
//	updating likes (like/dislike)
	
	@PutMapping("/tweet/{id}/likesUpdate/{loginId}")
	public ResponseEntity<Likes> updateLikes(@RequestHeader(name = "Authorization") String token, @PathVariable long id,@PathVariable String loginId, @RequestBody Likes likes) throws InvalidTokenException {
		
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		
		Likes updatedLikes = likesService.updateLikes(id, loginId, likes);
		
		log.info("Updating likes "+updatedLikes);
		
		return new ResponseEntity<Likes>(updatedLikes, HttpStatus.OK);
	}
	
//	delete details of a particular tweetId
	
	@DeleteMapping("/tweet/{id}/delete")
	public ResponseEntity<Void> Delete(@RequestHeader(name = "Authorization") String token, @PathVariable long id) throws InvalidTokenException{
		
		log.debug("Start");
		if (!authController.validateAdmin(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.debug("End");
		likesService.Delete(id);
		log.info("Deleting details of tweetId : "+id);
		return ResponseEntity.noContent().build();
	}

}
