package com.lasalara.lasalara.backend.exceptions;

/**
 * Exception class for situations when user input causes numeric errors.
 * @author Ants-Oskar Mäesalu
 */
public class NumericException extends Exception {
	
	/**
	 * Constructor without a message, using a default message.
	 */
	public NumericException() {
		super(NumericExceptionMessage.DEFAULT.getMessage());
		// TODO: Log the exception
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public NumericException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public NumericException(NumericExceptionMessage exception) {
		super(exception.getMessage());
	}
}
