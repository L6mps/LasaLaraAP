package com.lasalara.lasalara;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * The main class of the Android application.
 * @author Ants-Oskar Mäesalu
 */
public class LasaLaraApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Backend.initializeInstance();
		try {
			Backend.getInstance().testRequest(getApplicationContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isNetworkConnected(Context context) {
		Log.d("LasaLara", "Checking network connection.");
		ConnectivityManager connectivityManager = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
}
