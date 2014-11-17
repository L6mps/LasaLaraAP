package com.lasalara.lasalara.backend.exceptions;

import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.structure.Message;

/**
 * Exception class for situations when user input should exist in the database but doesn't.
 * @author Ants-Oskar Mäesalu
 */
public class InputDoesntExistException extends Exception {
	private static final long serialVersionUID = 1740253510395206918L;

	/**
	 * Constructor without a message, using a default message.
	 */
	public InputDoesntExistException() {
		super(InputDoesntExistExceptionMessage.DEFAULT.getMessage());
		logEvent(InputDoesntExistExceptionMessage.DEFAULT.getMessage());
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public InputDoesntExistException(String message) {
		super(message);
		logEvent(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public InputDoesntExistException(InputDoesntExistExceptionMessage exception) {
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
