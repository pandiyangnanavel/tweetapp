package com.tweetapp.authorization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.exception.InvalidCredentialsException;
import com.tweetapp.exception.InvalidTokenException;


/**
 * Authorization Controller to handle requests for logging in a valid user and
 * validating the JWT Token for other services.
 *
 * 
 */
@RestController
@CrossOrigin("http://localhost:4200")
public class AuthorizationController {

	private static final Logger log = LogManager.getLogger(AuthorizationController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService userService;

	@Value("${userDetails.badCredentialsMessage}")
	private String BAD_CREDENTIALS_MESSAGE;

	@Value("${userDetails.disabledAccountMessage}")
	private String DISABLED_ACCOUNT_MESSAGE;
	
	@Value("${userDetails.lockedAccountMessage}")
	private String LOCKED_ACCOUNT_MESSAGE;

	/**
	 * 
	 * @param userRequest {username, password}
	 * @return token on successful login else throws exception handled by
	 *         GlobalExceptionHandler
	 */
	@GetMapping("/authenticate/{loginId}/{password}")
	public ResponseEntity<String> login(@PathVariable String loginId,@PathVariable String password) {
		log.info("START - login()");
		try {
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginId,password));
			if (authenticate.isAuthenticated()) {
				log.info("Valid User detected - logged in");
			}
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException(BAD_CREDENTIALS_MESSAGE);
		} catch (DisabledException e) {
			throw new InvalidCredentialsException(DISABLED_ACCOUNT_MESSAGE);
		} catch (LockedException e) {
			throw new InvalidCredentialsException(LOCKED_ACCOUNT_MESSAGE);
		}

		String token = jwtUtil.generateToken(loginId);
		log.info("END - login()");
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	/**
	 * Checks if the token is a valid administrator token
	 * 
	 * @URL: http://localhost:8081/validate
	 * 
	 * @Header: [Authorization] = JWT Token
	 * 
	 * @param token
	 * @return
	 * @throws InvalidTokenException 
	 */
	@GetMapping("/validate")
	public boolean validateAdmin(@RequestHeader(name = "Authorization") String token) throws InvalidTokenException {
		log.info("START - validateAdmin() token: {}",token);

		// throws custom exception and response if token is invalid
		try {
			jwtUtil.isTokenExpiredOrInvalidFormat(token);
		}
		catch(InvalidTokenException iTE) {
			log.error("Exception Occurred, {}",iTE.getMessage());
			return false;
		}

		// else the user is loaded and role is checked, if role is valid, access is
		// granted

		UserDetails user = userService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
			log.info("END - validateAdmin()");
			return true;
		}
		return false;

	}

	/**
	 * @URL: http://localhost:8081/statusCheck
	 * @return "OK" if the server and controller is up and running
	 */
	@GetMapping(value = "/statusCheck")
	public String statusCheck() {
		log.info("OK");
		return "OK";
	}
}
