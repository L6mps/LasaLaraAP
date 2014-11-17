package com.lasalara.lasalara.backend.exceptions;

/**
 * Enumerator for messages of the exceptions caused by an input not corresponding to a specified format.
 * @author Ants-Oskar Mäesalu
 */
public enum FormatExceptionMessage {
	DEFAULT("The input doesn't correspond to the correct format."),
	BOOK_DOWNLOAD_EMAIL("The e-mail address does not correspond to an e-mail address' format.");
	
	private String message;
	
	/**
	 * Private constructor.
	 * @param message
	 */
	private FormatExceptionMessage(String message) {
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
