package com.lasalara.lasalara.backend.exceptions;

/**
 * Exception class for situations when user input should exist in the database but doesn't.
 * @author Ants-Oskar Mäesalu
 */
public class InputDoesntExistException extends Exception {

	/**
	 * Constructor without a message, using a default message.
	 */
	public InputDoesntExistException() {
		super(InputDoesntExistExceptionMessage.DEFAULT.getMessage());
		// TODO: Log the exception
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public InputDoesntExistException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public InputDoesntExistException(InputDoesntExistExceptionMessage exception) {
		super(exception.getMessage());
	}
}
