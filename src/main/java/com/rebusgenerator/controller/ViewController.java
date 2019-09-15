package com.rebusgenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Controller
@RequestMapping("/")
public class ViewController {
	
	@GetMapping({"/home", "/not_found"})
    public String home() {
           return "rebus/index.html";
    }
	
	@GetMapping({"/login", "/admin"})
    public String login() {
           return "admin/index.html";
    }
}