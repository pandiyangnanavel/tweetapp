package com.tweetapp.usersDetails;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.exception.InvalidCredentialsException;

@Service
public class UserService implements UserDetailsService {
	
	private static final Logger log = LogManager.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bencoder;
	
	@Value("${userDetails.errorMessage}")
	private String USER_DOES_NOT_EXIST_MESSAGE;
	
	public Users register(Users user) {
		return userRepository.save(user);
	}
	
	public List<Users> getAllUsers(){
		return userRepository.findAll();
	}
	
	public Users getUser(String loginId) {
		return userRepository.findByloginId(loginId);
	}
	
	public Users updatePassword(String loginId,Users user) {
		userRepository.DeleteByloginId(loginId);
		Users updatedUser = userRepository.save(user);
		
		return updatedUser;
	}
	
	public List<Users> searchByRegex(String str){
		return userRepository.searchByRegex(str);
	}
	
	public boolean authenticate(String loginId, String password) {
		Users user = userRepository.findByloginId(loginId);
		
		if(user==null)
			return false;
		
		if(user.getPassword().equals(password)){
			return true;
		}
		else
			return false;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		Users user = getUser(username);
		if (user == null) {
			throw new InvalidCredentialsException(USER_DOES_NOT_EXIST_MESSAGE);
		} else {
			log.info("Username: {} is valid", username);
			return new org.springframework.security.core.userdetails.User(username, bencoder.encode(user.getPassword()),
					Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
		}
	}

}
