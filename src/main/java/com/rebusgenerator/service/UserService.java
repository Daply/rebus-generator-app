package com.rebusgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.rebusgenerator.entity.User;
import com.rebusgenerator.exception.WrongCredentialsException;
import com.rebusgenerator.repository.UserRepository;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    public boolean login(String username, String password) throws Exception {
        if (username != null && password != null) {
        	User user = userRepository.findByUsername(username);
        	if (user != null) {
	        	try {
	        		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	        	}
	        	catch (AuthenticationException e) {
	        		throw new WrongCredentialsException("wrong credentials");
	        	}
	        	return true;
        	}
    	}
        return false;
    }
}
