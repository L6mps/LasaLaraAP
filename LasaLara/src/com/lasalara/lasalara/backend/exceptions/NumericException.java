package com.lasalara.lasalara.backend.exceptions;

import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.structure.Message;

/**
 * Exception class for situations when user input causes numeric errors.
 * @author Ants-Oskar Mäesalu
 */
public class NumericException extends Exception {
	private static final long serialVersionUID = 4641444288764957047L;

	/**
	 * Constructor without a message, using a default message.
	 */
	public NumericException() {
		super(NumericExceptionMessage.DEFAULT.getMessage());
		logEvent(NumericExceptionMessage.DEFAULT.getMessage());
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public NumericException(String message) {
		super(message);
		logEvent(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public NumericException(NumericExceptionMessage exception) {
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
