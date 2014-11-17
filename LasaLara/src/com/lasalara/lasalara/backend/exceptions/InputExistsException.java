package com.lasalara.lasalara.backend.exceptions;

import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.structure.Message;

/**
 * Exception class for situations when user input shouldn't exist in the database but does.
 * @author Ants-Oskar Mäesalu
 */
public class InputExistsException extends Exception {
	private static final long serialVersionUID = -855169030862660682L;

	/**
	 * Constructor without a message, using a default message.
	 */
	public InputExistsException() {
		super(InputExistsExceptionMessage.DEFAULT.getMessage());
		logEvent(InputExistsExceptionMessage.DEFAULT.getMessage());
	}

	/**
	 * Constructor with a string message.
	 * @param message		The exception message as a string.
	 */
	public InputExistsException(String message) {
		super(message);
		logEvent(message);
	}
	
	/**
	 * Constructor with an exception message enumerator.
	 * @param exception		The exception message enumerator.
	 */
	public InputExistsException(InputExistsExceptionMessage exception) {
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
