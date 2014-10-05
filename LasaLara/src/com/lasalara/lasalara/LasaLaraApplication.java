package com.lasalara.lasalara;

import android.app.Application;
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
		Backend.getInstance().testRequest();
	}
}
