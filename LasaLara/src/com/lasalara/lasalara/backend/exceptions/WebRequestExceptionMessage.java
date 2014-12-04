package com.lasalara.lasalara.backend.exceptions;

/**
 * Enumerator for messages of the exceptions caused by a web request's error.
 * @author Ants-Oskar Mäesalu
 */
public enum WebRequestExceptionMessage {
	DEFAULT("The web request could not be completed at this time."),
	NO_CONNECTION("You do not appear to have internet connection."),
	INTERRUPTED("The web request was interrupted."),
	CLIENT_PROTOCOL("The web request's client protocol is faulty."),
	PARSE("The web request's response could not be parsed."),
	BOOK_DOWNLOAD("The book download web request could not be completed at this time."),
	CHAPTERS_DOWNLOAD("The chapters' download web request could not be completed at this time."),
	QUESTIONS_DOWNLOAD("The questions' download web request could not be completed at this time."),
	QUESTION_POSING("The question proposition request could not be completed at this time.");
	
	private String message;
	
	/**
	 * Private constructor.
	 * @param message
	 */
	private WebRequestExceptionMessage(String message) {
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
