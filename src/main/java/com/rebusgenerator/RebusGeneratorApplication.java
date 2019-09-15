package com.rebusgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
@SpringBootApplication
public class RebusGeneratorApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(RebusGeneratorApplication.class, args);
	}
	
}
