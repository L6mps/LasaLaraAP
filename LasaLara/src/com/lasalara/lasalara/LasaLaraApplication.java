package com.lasalara.lasalara;

import com.lasalara.lasalara.backend.Backend;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * The main class of the Android application.
 * @author Ants-Oskar Mäesalu
 */
public class LasaLaraApplication extends Application {
	private static Context currentContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Backend.initializeInstance();
	}
	
	/**
	 * Check if the device is connected to a network.
	 * @return			Boolean value, whether the device is connected to a network or not.
	 */
	public static boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = 
				(ConnectivityManager) currentContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	/**
	 * @return the current context object.
	 */
	public static Context getCurrentContext() {
		return currentContext;
	}

	/**
	 * @param currentContext	The new current context.
	 */
	public static void setCurrentContext(Context currentContext) {
		LasaLaraApplication.currentContext = currentContext;
	}
}
