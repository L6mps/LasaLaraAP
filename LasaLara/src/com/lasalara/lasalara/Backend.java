package com.lasalara.lasalara;

/**
 * The main class of the back end of the application.
 * The whole back end is referred to through this class.
 * @author Ants-Oskar Mäesalu
 */
public class Backend {
	private static Backend instance;
	
	/**
	 * Constructor.
	 * Private because this is a singleton.
	 */
	private Backend() {
		super();
	}
	
	/**
	 * Initialize the singleton backend class.
	 */
	public static void initializeInstance() {
		if (instance == null) { // NB: Not thread-safe
			instance = new Backend();
		}
	}
	
	/**
	 * Return the instance of the backend.
	 * @return
	 */
	public static Backend getInstance() {
		return instance;
	}
}
