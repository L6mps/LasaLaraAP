package com.lasalara.lasalara;

import android.app.Application;

/**
 * The main class of the Android application.
 * @author Ants-Oskar M�esalu
 */
public class LasaLaraApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Backend.initializeInstance();
	}
}
