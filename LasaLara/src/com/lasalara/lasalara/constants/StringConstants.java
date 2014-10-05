package com.lasalara.lasalara.constants;

/**
 * Enumerator for all of the string constants in the application.
 * @author Ants-Oskar Mäesalu
 */
public enum StringConstants {
	APP_NAME("LasaLara");
	
	private String value;
	
	/**
	 * Private constructor.
	 * @param value	The constant's value.
	 */
	private StringConstants(String value) {
		this.value = value;
	}
	
	/**
	 * @return the string constant's value.
	 */
	public String getValue() {
		return value;
	}
}
