package com.lasalara.lasalara.backend.exceptions;

/**
 * Exception class for situations when user input doesn't correspond to a specified format.
 * @author Ants-Oskar Mäesalu
 */
public class FormatException extends Exception {

	/**
	 * Constructor without a message, using a default message.
	 */
	public FormatException() {
		super(FormatExceptionMessage.DEFAULT.getMessage());
		// TODO: Log the exception
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public FormatException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public FormatException(FormatExceptionMessage exception) {
		super(exception.getMessage());
	}
}
