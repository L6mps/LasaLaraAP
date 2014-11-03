package com.lasalara.lasalara.backend.exceptions;

/**
 * Enumerator for messages of the exceptions caused by an input already existing in the database.
 * @author Ants-Oskar Mäesalu
 */
public enum InputExistsExceptionMessage {
	DEFAULT("");
	
	private String message;
	
	/**
	 * Private constructor.
	 * @param message
	 */
	private InputExistsExceptionMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Return the exception message.
	 * @return
	 */
	public String getMessage() {
		return message;
	}
}
