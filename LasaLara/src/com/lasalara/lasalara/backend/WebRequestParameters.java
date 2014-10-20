package com.lasalara.lasalara.backend;

import android.content.Context;

/**
 * 
 * @author Ants-Oskar Mäesalu
 */
public class WebRequestParameters {
	private String url;
	private UrlParameters parameterList;
	
	/**
	 * Constructor.
	 * @param url			The request's URL.
	 * @param parameterList	The parameter list sent as a POST header.
	 */
	WebRequestParameters(String url, UrlParameters parameterList) {
		this.url = url;
		this.parameterList = parameterList;
	}

	/**
	 * @return the URL string
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the parameter list sent as a POST header.
	 */
	public UrlParameters getParameterList() {
		return parameterList;
	}
}
