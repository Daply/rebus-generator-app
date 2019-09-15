package com.rebusgenerator.exception;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
public class AlgorithmException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Exceptional situation in algorithm of creating a rebus";
	private String separator = ": ";
	
	public AlgorithmException () {
	}
	
	public AlgorithmException (String specifiedMessage) {
		message += separator + specifiedMessage; 
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
