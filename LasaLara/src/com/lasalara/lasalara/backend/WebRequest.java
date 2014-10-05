package com.lasalara.lasalara.backend;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.lasalara.lasalara.Backend;
import com.lasalara.lasalara.LasaLaraApplication;
import com.lasalara.lasalara.constants.StringConstants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that handles all of the web requests.
 * @author Ants-Oskar Mäesalu
 */
public class WebRequest {
	private String url;
	private String urlParameters;
	private HttpURLConnection connection;
	private String result;
	
	/**
	 * @param context		The current activity's context (needed for network connection check).
	 * @param url			The URL of the web request.
	 * @param urlParameters	The URL parameters string.
	 * @throws IOException
	 */
	public WebRequest(Context context, String url, String urlParameters) throws IOException {
		disableConnectionReuseIfNecessary();
		if (LasaLaraApplication.isNetworkConnected(context)) {
			this.url = url;
			this.urlParameters = urlParameters;
			createConnection();
			sendRequest();
			getResponse();
		} else {
			// TODO
			Log.d(StringConstants.APP_NAME.getValue(), "No networks are connected.");
		}
	}
	
	/**
	 * Required to prevent issues in earlier Android versions.
	 */
	private static void disableConnectionReuseIfNecessary() {
	    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}
	
	/**
	 * Create a connection with the specified URL.
	 * @throws IOException
	 */
	private void createConnection() throws IOException {
		URL urlObject = new URL(url);
		connection = (HttpURLConnection) urlObject.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
	}
	
	/**
	 * Send a POST web request to the site.
	 * @throws IOException
	 */
	private void sendRequest() throws IOException {
		DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
		writer.writeBytes(urlParameters);
		writer.flush();
		writer.close();
	}
	
	/**
	 * Get a response from the web request.
	 * @throws IOException
	 */
	private void getResponse() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();
		result = response.toString();
	}
	
	/**
	 * @return the result of the web request as a JSONObject.
	 * @throws JSONException
	 */
	public JSONObject getJSONObject() throws JSONException {
		return new JSONObject(result);
	}
}
