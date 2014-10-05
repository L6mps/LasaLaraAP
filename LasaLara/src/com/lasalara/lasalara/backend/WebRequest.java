package com.lasalara.lasalara.backend;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that handles all of the web requests.
 * @author Ants-Oskar Mäesalu
 */
public class WebRequest {
	private String url;
	private String urlParameters;
	private HttpURLConnection connection;
	private String result;
	
	public WebRequest(String url, String urlParameters) throws IOException {
		this.url = url;
		this.urlParameters = urlParameters;
		createConnection();
		sendRequest();
		getResponse();
	}
	
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
	
	private void sendRequest() throws IOException {
		DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
		writer.writeBytes(urlParameters);
		writer.flush();
		writer.close();
	}
	
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
	
	public String getResult() {
		return result;
	}
}
