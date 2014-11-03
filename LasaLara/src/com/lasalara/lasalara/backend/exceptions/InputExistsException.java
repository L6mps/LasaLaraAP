package com.lasalara.lasalara.backend.exceptions;

/**
 * Exception class for situations when user input shouldn't exist in the database but does.
 * @author Ants-Oskar Mäesalu
 */
public class InputExistsException extends Exception {
	
	/**
	 * Constructor without a message, using a default message.
	 */
	public InputExistsException() {
		super("The input already exists in the database.");
		// TODO: Log the exception
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public InputExistsException(String message) {
		super(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public InputExistsException(InputExistsExceptionMessage exception) {
		super(exception.getMessage());
	}
}
