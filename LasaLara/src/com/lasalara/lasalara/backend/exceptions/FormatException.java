package com.lasalara.lasalara.backend.exceptions;

import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.structure.Message;

/**
 * Exception class for situations when user input doesn't correspond to a specified format.
 * @author Ants-Oskar Mäesalu
 */
public class FormatException extends Exception {
	private static final long serialVersionUID = 6570526755105543146L;

	/**
	 * Constructor without a message, using a default message.
	 */
	public FormatException() {
		super(FormatExceptionMessage.DEFAULT.getMessage());
		logEvent(FormatExceptionMessage.DEFAULT.getMessage());
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public FormatException(String message) {
		super(message);
		logEvent(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public FormatException(FormatExceptionMessage exception) {
		super(exception.getMessage());
		logEvent(exception.getMessage());
	}
	
	/**
	 * Log the exception into the database.
	 * @param logEventMessage	The event's message string.
	 */
	private void logEvent(String logEventMessage) {
		DatabaseHelper.getInstance().getLogHelper().insertEvent(new Message(logEventMessage));
	}
}
