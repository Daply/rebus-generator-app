package com.rebusgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebusgenerator.entity.User;
import com.rebusgenerator.service.UserService;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@RestController
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/admin")
    public boolean login(@RequestBody User admin) throws Exception {
		return userService.login(admin.getUsername(), admin.getPassword());
    }

}
