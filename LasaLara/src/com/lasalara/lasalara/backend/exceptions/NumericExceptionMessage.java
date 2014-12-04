package com.lasalara.lasalara.backend.exceptions;

/**
 * Enumerator for messages of the exceptions caused by an input already existing in the database.
 * @author Ants-Oskar Mäesalu
 */
public enum NumericExceptionMessage {
	DEFAULT("The number used is invalid or out of bounds."),
	PROGRESS_CURRENT_TOO_BIG("The current progress is larger than the maximum progress."),
	PROGRESS_MAXIMUM_NEGATIVE("The maximum progress is negative."),
	PROGRESS_CURRENT_NEGATIVE("The current progress is negative."),
	CHAPTER_NEXT_TIME_MISSING("The next time the chapter should be revised could not be queried.");
	
	private String message;
	
	/**
	 * Private constructor.
	 * @param message
	 */
	private NumericExceptionMessage(String message) {
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
