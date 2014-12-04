package com.lasalara.lasalara.backend.exceptions;

/**
 * Enumerator for messages of the exceptions caused by an input not corresponding to a specified format.
 * @author Ants-Oskar Mäesalu
 */
public enum FormatExceptionMessage {
	DEFAULT("The input does not correspond to the correct format."),
	BOOK_DOWNLOAD("The book's data does not correspond to the correct format."),
	BOOK_DOWNLOAD_EMAIL("The e-mail address does not correspond to an e-mail address' format."),
	CHAPTERS_DOWNLOAD("The chapters' data does not correspond to the correct format."),
	QUESTIONS_DOWNLOAD("The questions' data does not correspond to the correct format."),
	QUESTIONS_DOWNLOAD_LIST("The questions' data does not hold the same amount of questions and answers."),
	WEB_REQUEST_ENCODING("The web request's encoding does not correspond to the correct format.");
	
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
