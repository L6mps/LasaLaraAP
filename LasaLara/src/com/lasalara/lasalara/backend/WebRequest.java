package com.lasalara.lasalara.backend;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.lasalara.lasalara.LasaLaraApplication;
import com.lasalara.lasalara.constants.StringConstants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
		if (LasaLaraApplication.isNetworkConnected(context)) {
			this.url = url.toLowerCase(Locale.ENGLISH);
			this.parameterList = parameterList;
			sendRequest();
			getResponse();
		} else {
			// TODO: Throw error message: No networks are connected
			Log.d(StringConstants.APP_NAME, "No networks are connected.");
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
	 * @throws IOException 
	 * @throws UnsupportedEncodingException
	 * @throws ClientProtocolException 
	 */
	private void sendRequest() throws IOException, UnsupportedEncodingException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (int i = 0; i < parameterList.getSize(); i++) {
			pairs.add(new BasicNameValuePair(parameterList.getKey(i), parameterList.getValue(i)));
		}
		post.setEntity(new UrlEncodedFormEntity(pairs));
		response = client.execute(post);
	}
	
	/**
	 * Get a response from the web request.
	 * @throws ParseException 
	 * @throws IOException
	 */
	private void getResponse() throws ParseException, IOException {
		result = null;
		result = EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * @return the result of the web request as a JSONObject.
	 * @throws JSONException
	 */
	public JSONObject getJSONObject() throws JSONException {
		return new JSONObject(result);
	}
}
