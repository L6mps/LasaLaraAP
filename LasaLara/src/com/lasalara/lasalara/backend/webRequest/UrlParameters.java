package com.lasalara.lasalara.backend.webRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class responsible for holding a web request's URL parameter data.
 * @author Ants-Oskar Mäesalu
 */
public class UrlParameters {
	List<String> keyList;
	List<String> valueList;
	
	/**
	 * COnstructor without any parameters. Initializes the key and value lists.
	 */
	public UrlParameters() {
		keyList = new ArrayList<String>();
		valueList = new ArrayList<String>();
	}
	
	/**
	 * Add a new parameter pair to the list of URL parameters.
	 * @param key		The parameter's key string.
	 * @param value		The parameter's value string.
	 */
	public void addPair(String key, String value) {
		keyList.add(key);
		valueList.add(value);
	}
	
	/**
	 * @return the size of the URL parameter list.
	 */
	public int getSize() {
		return keyList.size();
	}
	
	/**
	 * @param index
	 * @return the key from the URL parameter list corresponding to the specified index.
	 */
	public String getKey(int index) {
		return keyList.get(index);
	}
	
	/**
	 * @param index
	 * @return the key from the URL parameter list corresponding to the specified index.
	 */
	public String getValue(int index) {
		return valueList.get(index);
	}
}
