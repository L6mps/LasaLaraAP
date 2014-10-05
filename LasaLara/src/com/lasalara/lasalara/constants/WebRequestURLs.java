package com.lasalara.lasalara.constants;

/**
 * Enumerator for all of the web request addresses in the application.
 * @author Ants-Oskar Mäesalu
 */
public enum WebRequestURLs {
	GET_BOOK("http://lasalara.com/getbook"),
	GET_CHAPTERS("http://lasalara.com/getmchapters"),
	GET_QUESTIONS("http://lasalara.com/getquestions"),
	POSE_QUESTION("http://lasalara.com/posequestion");
	
	private String address;
	
	/**
	 * Private constructor.
	 * @param value	The constant's value.
	 */
	private WebRequestURLs(String value) {
		this.address = value;
	}
	
	/**
	 * @return the web request's address.
	 */
	public String getAddress() {
		return address;
	}
}
