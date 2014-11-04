package com.lasalara.lasalara.backend.structure;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Class responsible for holding messages sent from the back end to the front end.
 * @author Ants-Oskar Mäesalu
 */
public class Message {
	private Timestamp time;		// The time the message was sent.
	private String message;		// The message string itself.
	
	/**
	 * Public constructor with time and message parameters.
	 * @param time		The time the message was sent.
	 * @param message	The message string itself.
	 */
	public Message(Timestamp time, String message) {
		this.time = time;
		this.message = message;
	}
	
	/**
	 * Public constructor with only the message parameter. The sending time is set to the current time.
	 * @param message	The message string.
	 */
	public Message(String message) {
		this(new Timestamp(Calendar.getInstance().getTime().getTime()), message);
	}

	/**
	 * @return the time the message was sent.
	 */
	public Timestamp getTime() {
		return time;
	}

	/**
	 * @return the message string.
	 */
	public String getMessage() {
		return message;
	}
}
