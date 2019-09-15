package com.rebusgenerator.exception;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
public class ParserException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Exceptional situation in parsing a rebus sequence";
	private String separator = ": ";
	
	public ParserException () {
	}
	
	public ParserException (String specifiedMessage) {
		message += separator + specifiedMessage; 
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
