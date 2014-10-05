package com.lasalara.lasalara;

import java.io.IOException;
import java.net.URLEncoder;

import android.content.Context;
import android.util.Log;

import com.lasalara.lasalara.backend.WebRequest;

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
	
	public void testRequest(Context context) throws IOException {
		String url = "http://www.lasalara.com/getbook";
		String urlParameters =
				"em=" + URLEncoder.encode("lasalara.help@gmail.com", "UTF-8") +
				"&bt=" + URLEncoder.encode("Welcome to LasaLara", "UTF-8");
		Log.d("LasaLara", "Initializing web request.");
		WebRequest request = new WebRequest(context, url, urlParameters);
		Log.d("LasaLara", "Result: " + request.getResult());
	}
	
	/**
	 * Return the instance of the backend.
	 * @return
	 */
	public static Backend getInstance() {
		return instance;
	}
}
