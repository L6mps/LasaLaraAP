package com.lasalara.lasalara.backend.webRequest;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.lasalara.lasalara.backend.constants.StringConstants;

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
	 * @param context		The current activity's context (needed for network connection check).
	 * @param url			The URL of the web request.
	 * @param urlParameters	The URL parameters string.
	 * @throws IOException
	 */
	public WebRequest(Context context, String url, UrlParameters parameterList) throws IOException {
		disableConnectionReuseIfNecessary();
			Log.d(StringConstants.APP_NAME, "Network is connected.");
			//this.url = url.toLowerCase(Locale.ENGLISH);
			this.url = url;
			this.parameterList = parameterList;
			Log.e("debug",parameterList.valueList.get(0));
			Thread thread = new Thread() {
				@Override
				public void run() {
					sendRequest();
					getResponse();
				}
			};
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	 */
	private void sendRequest() {
		Log.d(StringConstants.APP_NAME, "Sending request.");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (int i = 0; i < parameterList.getSize(); i++) {
			Log.d(StringConstants.APP_NAME, "Web request parameter: " + parameterList.getKey(i) + ", " + parameterList.getValue(i) + ".");
			pairs.add(new BasicNameValuePair(parameterList.getKey(i), parameterList.getValue(i)));
		}
		try {
			Log.d(StringConstants.APP_NAME, "Setting URL entity.");
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.d(StringConstants.APP_NAME, "Getting HTTP response.");
			response = client.execute(post);
			Log.d(StringConstants.APP_NAME, "Got HTTP response.");
		} catch (ClientProtocolException e) {
			Log.d(StringConstants.APP_NAME, "ClientProtocolException: " + e.getStackTrace()); // TODO
		} catch (IOException e) {
			Log.d(StringConstants.APP_NAME, "IOException: " + e.getStackTrace()); // TODO
		} catch (Exception e) {
			Log.d(StringConstants.APP_NAME, "Jumaliku käe exception: " + e.getStackTrace()); // TODO
		}
	}
	
	/**
	 * Get a response from the web request.
	 */
	private void getResponse() {
		result = null;
		try {
			Log.d(StringConstants.APP_NAME, "Getting response.");
			result = EntityUtils.toString(response.getEntity());
			Log.d(StringConstants.APP_NAME, "Got response.");
		} catch (ParseException e) {
			Log.d(StringConstants.APP_NAME, "ParseException: " + e.getStackTrace());
		} catch (IOException e) {
			Log.d(StringConstants.APP_NAME, "IOException: " + e.getStackTrace());
		} catch (Exception e) {
			Log.d(StringConstants.APP_NAME, "Jumaliku käe exception: " + e.getStackTrace());
		}
	}
	
	/**
	 * @return the result of the web request as a JSONObject.
	 * @throws JSONException
	 */
	public JSONObject getJSONObject() throws JSONException {
		Log.e("debug",result.substring(result.indexOf("{"), result.lastIndexOf("}")+1));
		return new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}")+1));
	}
}