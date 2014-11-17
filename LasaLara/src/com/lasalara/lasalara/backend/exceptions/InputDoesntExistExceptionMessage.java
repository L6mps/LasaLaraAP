package com.lasalara.lasalara.backend.exceptions;

/**
 * Enumerator for messages of the exceptions caused by an input not (yet?) existing in the database.
 * @author Ants-Oskar Mäesalu
 */
public enum InputDoesntExistExceptionMessage {
	DEFAULT("The input doesn't exist in the database."),
	BOOK_DOWNLOAD_EMAIL("The specified e-mail address was not found."),
	BOOK_DOWNLOAD_TITLE("No book with that title was found.");
	
	private String message;
	
	/**
	 * Private constructor.
	 * @param message
	 */
	private InputDoesntExistExceptionMessage(String message) {
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
