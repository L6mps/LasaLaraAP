package com.lasalara.lasalara.backend.webRequest;

import android.os.Build;
import android.util.Log; // TODO: Remove as soon as posing questions works

import com.lasalara.lasalara.LasaLaraApplication;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.FormatExceptionMessage;
import com.lasalara.lasalara.backend.exceptions.NumericException;
import com.lasalara.lasalara.backend.exceptions.WebRequestException;
import com.lasalara.lasalara.backend.exceptions.WebRequestExceptionMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that handles all of the web requests.
 * @author Ants-Oskar Mäesalu
 */
public class WebRequest {
	private String url;
	private UrlParameters parameterList;
	private HttpResponse response;
	private String result;
	
	/**
	 * @param url			The URL of the web request.
	 * @param urlParameters	The URL parameters string.
	 * @throws IOException
	 * @throws WebRequestException 
	 */
	public WebRequest(String url, UrlParameters parameterList) throws IOException, WebRequestException {
		disableConnectionReuseIfNecessary();
		if (LasaLaraApplication.isNetworkConnected()) {
			this.url = url;
			this.parameterList = parameterList;
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						sendRequest();
						getResponse();
					} catch (FormatException e) {
						Backend.getInstance().addMessage(e.getMessage());
					} catch (WebRequestException e) {
						Backend.getInstance().addMessage(e.getMessage());
					} catch (NumericException e) {
						Backend.getInstance().addMessage(e.getMessage());
					}
				}
			};
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				throw new WebRequestException(WebRequestExceptionMessage.INTERRUPTED);
			}
		} else {
			throw new WebRequestException(WebRequestExceptionMessage.NO_CONNECTION);
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
	 * Send a POST web request to the site.
	 * @throws FormatException 
	 * @throws WebRequestException 
	 * @throws NumericException 
	 */
	private void sendRequest() throws FormatException, WebRequestException, NumericException {
		Log.d(StringConstants.APP_NAME, "Sending request.");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (int i = 0; i < parameterList.getSize(); i++) {
			//Log.d(StringConstants.APP_NAME, "Web request parameter: " + parameterList.getKey(i) + ", " + parameterList.getValue(i) + ".");
			pairs.add(new BasicNameValuePair(parameterList.getKey(i), parameterList.getValue(i)));
		}
		try {
			Log.d(StringConstants.APP_NAME, "Setting URL entity.");
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			throw new FormatException(FormatExceptionMessage.WEB_REQUEST_ENCODING);
		}
		try {
			Log.d(StringConstants.APP_NAME, "Getting HTTP response.");
			response = client.execute(post);
			Log.d(StringConstants.APP_NAME, "Got HTTP response.");
		} catch (ClientProtocolException e) {
			throw new WebRequestException(WebRequestExceptionMessage.CLIENT_PROTOCOL);
		} catch (IOException e) {
			throw new WebRequestException();
		} catch (Exception e) {
			throw new WebRequestException();
		}
	}
	
	/**
	 * Get a response from the web request.
	 * @throws WebRequestException 
	 */
	private void getResponse() throws WebRequestException {
		result = null;
		try {
			Log.d(StringConstants.APP_NAME, "Getting response.");
			result = EntityUtils.toString(response.getEntity());
			Log.d(StringConstants.APP_NAME, "Got response.");
		} catch (ParseException e) {
			throw new WebRequestException(WebRequestExceptionMessage.PARSE);
		} catch (IOException e) {
			throw new WebRequestException();
		} catch (Exception e) {
			throw new WebRequestException();
		}
	}
	
	/**
	 * @return the result of the web request as a JSONObject.
	 * @throws JSONException
	 */
	public JSONObject getJSONObject() throws JSONException {
		return new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}")+1));
	}
	
	/**
	 * @return the result of the web request as a JSONArray.
	 * @throws JSONException
	 */
	public JSONArray getJSONArray() throws JSONException {
		return new JSONArray(result);
	}
}