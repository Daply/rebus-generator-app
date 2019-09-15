package com.rebusgenerator.exception;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
public class ImageProcessorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message = "Exceptional situation in processing the final rebus image";
	private String separator = ": ";
	
	public ImageProcessorException () {
	}
	
	public ImageProcessorException (String specifiedMessage) {
		message += separator + specifiedMessage; 
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}