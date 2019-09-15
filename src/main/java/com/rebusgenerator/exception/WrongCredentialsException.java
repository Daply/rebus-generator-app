package com.rebusgenerator.exception;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
public class WrongCredentialsException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Exceptional situation in authentication";
	private String separator = ": ";
	
	public WrongCredentialsException () {
	}
	
	public WrongCredentialsException (String specifiedMessage) {
		message += separator + specifiedMessage; 
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}