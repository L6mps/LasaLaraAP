package com.lasalara.lasalara.backend.webRequest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.lasalara.lasalara.LasaLaraApplication;
import com.lasalara.lasalara.backend.constants.StringConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class WebRequest extends AsyncTask<WebRequestParameters, Void, Void> {
	private Context context;
	private String url;
	private UrlParameters parameterList;
	private HttpResponse response;
	private String result;

	/**
	 * Constructor.
	 * @param context		The current application context (needed for network connection check).
	 */
	public WebRequest(Context context) {
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(WebRequestParameters... params) {
		for (int i = 0; i < params.length; i++) {
			disableConnectionReuseIfNecessary();
			if (LasaLaraApplication.isNetworkConnected(context)) {
				Log.d(StringConstants.APP_NAME, "Network is connected.");
				this.url = params[i].getUrl().toLowerCase(Locale.ENGLISH);
				this.parameterList = params[i].getParameterList();
				new Thread(){ // Android no longer supports running network requests on the UI thread
				    public void run(){
						sendRequest();
						getResponse();
				    }
				}.start();
				Log.d(StringConstants.APP_NAME, "Ending web request.");
			} else {
				// TODO: Throw error message: No networks are connected
				Log.d(StringConstants.APP_NAME, "No networks are connected.");
			}
		}
		return null;
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
			Log.d(StringConstants.APP_NAME, "ClientProtocolException: " + e.getMessage() + ": " + e.getStackTrace()); // TODO
		} catch (IOException e) {
			Log.d(StringConstants.APP_NAME, "IOException: " + e.getMessage() + ": " + e.getStackTrace()); // TODO
		} catch (Exception e) {
			Log.d(StringConstants.APP_NAME, "Jumaliku käe exception (sendRequest): " + e.getMessage() + ": " + e.getStackTrace()); // TODO
		}
	}
	
	/**
	 * Get a response from the web request.
	 */
	private void getResponse() {
		result = null;
		try {
			Log.d(StringConstants.APP_NAME, "Getting response.");
			Log.d(StringConstants.APP_NAME, "Response: " + response.toString());
			Log.d(StringConstants.APP_NAME, "Response entity: " + response.getEntity().toString());
			result = EntityUtils.toString(response.getEntity()).toString();
			Log.d(StringConstants.APP_NAME, "Got response.");
			Log.d(StringConstants.APP_NAME, "Result: " + result);
		} catch (ParseException e) {
			Log.d(StringConstants.APP_NAME, "ParseException: " + e.getMessage() + ": " + e.getStackTrace()); // TODO
		} catch (IOException e) {
			Log.d(StringConstants.APP_NAME, "IOException: " + e.getMessage() + ": " + e.getStackTrace()); // TODO
		} catch (Exception e) {
			Log.d(StringConstants.APP_NAME, "Jumaliku käe exception (getResponse): " + e.getMessage() + ": " + e.getStackTrace()); // TODO
		}
	}
	
	/**
	 * @return the result of the web request as a JSONObject.
	 * @throws JSONException
	 */
	public JSONObject getJSONObject() throws JSONException {
		Log.d(StringConstants.APP_NAME, "Converting to JSONObject: " + result);
		return new JSONObject(result);
	}
}
