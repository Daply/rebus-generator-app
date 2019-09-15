package com.rebusgenerator.exception;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
public class FileLoadingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message = "Exceptional situation in file loading";
	private String separator = ": ";
	
	public FileLoadingException () {
	}
	
	public FileLoadingException (String specifiedMessage) {
		message += separator + specifiedMessage; 
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}
