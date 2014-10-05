package com.lasalara.lasalara.backend;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.constants.WebRequestURLs;

import android.content.Context;

/**
 * Class responsible for holding and querying a book's information.
 * @author Ants-Oskar Mäesalu
 */
public class Book {
	String key;				// The book's UUID
	String title;			// Name of the book
	String ownerEmail;		// E-mail of the person who created the book
	String ownerName;		// Name of the person who created the book (if blank, the e-mail is used)
	String ownerInstitution;// Institution of the person who created the book (if blank, the e-mail is used)
	String lastChapter;		// Last chapter (UUID) of this book opened by the student
	
	/**
	 * Constructor used when downloading a book from the web.
	 * @param context		The current activity's context (needed for network connection check).
	 * @param ownerEmail	The book's owner's e-mail address.
	 * @param title			The book's title.
	 * @throws IOException
	 * @throws JSONException
	 */
	Book(Context context, String ownerEmail, String title) throws IOException, JSONException {
		String url = WebRequestURLs.GET_BOOK.getAddress();
		String urlParameters =
				"em=" + URLEncoder.encode(ownerEmail, "UTF-8") +
				"&bt=" + URLEncoder.encode(title, "UTF-8");
		WebRequest request = new WebRequest(context, url, urlParameters);
		try {
			JSONObject result = request.getJSONObject();
			try {
				this.ownerEmail = ownerEmail;
				this.title = title;
				if (result.isNull("name")) {
					ownerName = null;
				} else {
					ownerName = result.get("name").toString();
				}
				if (result.isNull("institution")) {
					ownerInstitution = null;
				} else {
					ownerInstitution = result.get("institution").toString();
				}
				key = result.get("bk").toString();
			} catch (JSONException e) {
				int errorCode = result.getInt("err");
				System.out.println(errorCode);
				// TODO: Handle error code
			}
		} catch (JSONException e) {
			throw new JSONException(e.toString());
		}
	}

	/**
	 * @return the book's UUID key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the book's title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the owner's e-mail address.
	 */
	public String getOwnerEmail() {
		return ownerEmail;
	}

	/**
	 * @return the book owner's name (or null if left blank).
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @return the book owner's institution (or null if left blank).
	 */
	public String getOwnerInstitution() {
		return ownerInstitution;
	}

	/**
	 * @return the last chapter UUID of this book opened by the student.
	 */
	public String getLastChapter() {
		return lastChapter;
	}
}
