package com.lasalara.lasalara.backend.structure;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.webRequest.UrlParameters;
import com.lasalara.lasalara.backend.webRequest.WebRequest;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Class responsible for holding and querying a book's information and querying it's chapters' information.
 * @author Ants-Oskar Mäesalu
 */
public class Book {
	private String key;						// The book's UUID
	private String title;					// Name of the book
	private String ownerEmail;				// E-mail of the person who created the book
	private String ownerName;				// Name of the person who created the book (if blank, the e-mail is used)
	private String ownerInstitution;		// Institution of the person who created the book (if blank, the e-mail is used)
	private List<Chapter> chapters; 		// The list of chapters in this book.
	
	private Context context;
	
	/**
	 * Constructor, used when downloading a book from the web.
	 * @param context		The current activity's context (needed for network connection check and SQLite database).
	 * @param ownerEmail	The book's owner's e-mail address.
	 * @param title			The book's title.
	 */
	public Book(Context context, String ownerEmail, String title) {
		this.context = context;
		Log.d(StringConstants.APP_NAME, "Book constructor.");
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
		String url = StringConstants.URL_GET_BOOK;
		UrlParameters urlParameters = new UrlParameters();
		try {
			urlParameters.addPair("em", ownerEmail);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			urlParameters.addPair("bt", title);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		WebRequest request = null;
		try {
			request = new WebRequest(context, url, urlParameters);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Log.d(StringConstants.APP_NAME, "Book: Getting JSONObject.");
			JSONObject result = request.getJSONObject();
			Log.d(StringConstants.APP_NAME, "Book: Got JSONObject.");
			try {
				if (validateEmail(ownerEmail)) {
					this.ownerEmail = ownerEmail;
					this.title = result.get("title").toString();
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
					
					if(databaseHelper.getBookHelper() == null)
						Log.e("debug","Database helper's book helper is null");
					
					databaseHelper.getBookHelper().insertBook(this); // TODO: Test
				} else {
					// TODO: Throw error: The e-mail address does not correspond to an e-mail address' format.
				}
			} catch (JSONException e) {
				int errorCode = result.getInt("err");
				if (errorCode == 0) {
					// TODO: Throw error: The e-mail address was not found.
				} else {
					// TODO: Throw error: No book with that title was found.
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor, used when querying data from the internal SQLite database.
	 * @param dbResults			Database query results.
	 */
	public Book(Cursor dbResults) {
		key = dbResults.getString(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_KEY));
		title = dbResults.getString(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_TITLE));
		ownerEmail = dbResults.getString(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_OWNER_EMAIL));
		if (dbResults.isNull(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_OWNER_NAME))) {
			ownerName = null;
		} else {
			ownerName = dbResults.getString(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_OWNER_NAME));
		}
		if (dbResults.isNull(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_OWNER_INSTITUTION))) {
			ownerInstitution = null;
		} else {
			ownerInstitution = dbResults.getString(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_OWNER_INSTITUTION));
		}
	}
	
	private void updateInDatabase() {
		// TODO
	}
	
	/**
	 * Delete this book from the database.
	 * Also deletes all of the associated chapters and their questions from the database.
	 */
	private void deleteFromDatabase() {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
		databaseHelper.getBookHelper().deleteBook(this);
	}
	
	/**
	 * Validate the e-mail.
	 * @param email	The e-mail string.
	 * @return if the e-mail corresponds to an e-mail's format.
	 */
	private boolean validateEmail(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
		"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * Load the chapters in this book from the web.
	 * @param context		The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	private void loadChapters() {
		chapters = new ArrayList<Chapter>();
		String url = StringConstants.URL_GET_CHAPTERS;
		UrlParameters urlParameters = new UrlParameters();
		try {
			urlParameters.addPair("bk", URLEncoder.encode(key, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		WebRequest request = null;
		try {
			request = new WebRequest(context, url, urlParameters);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			JSONArray resultArray = request.getJSONArray();
			for (int i = 0; i < resultArray.length(); i++) {
				JSONObject result = resultArray.getJSONObject(i);
				String chapterKey = result.get("ck").toString();
				String chapterTitle = result.get("title").toString();
				int chapterVersion = result.getInt("version");
				String chapterAuthorEmail = result.get("email").toString();
				String chapterAuthorName = null;
				if (!result.isNull("name")) {
					chapterAuthorName = result.get("name").toString();
				}
				String chapterAuthorInstitution = null;
				if (!result.isNull("institution")) {
					chapterAuthorInstitution = result.get("institution").toString();
				}
				boolean chapterProposalsAllowed = result.getBoolean("allowProp");
				chapters.add(new Chapter(context, chapterKey, chapterTitle, chapterVersion, 
						chapterAuthorEmail, chapterAuthorName, chapterAuthorInstitution, 
						chapterProposalsAllowed, i, key));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update() {
		// TODO
		// Update book data from the web.
		// Check if chapters have been changed.
		// If the version numbers have been changed, update the questions in the database.
		// Send appropriate notification messages to the user.
	}
	
	/**
	 * Delete this book from the application.
	 */
	public void delete() {
		// TODO
	}
	
	/**
	 * Delete all of the chapters from this book.
	 */
	public void deleteChapters() {
		// TODO
	}
	
	/**
	 * Reset the book's progress.
	 */
	public void resetProgress() {
		for (Chapter chapter: chapters) {
			chapter.resetProgress();
		}
	}
	
	/**
	 * Calculate the book's progress based on it's chapters' progresses.
	 * @return the book's Progress object.
	 */
	public Progress getProgress() {
		List<Progress> progressList = new ArrayList<Progress>();
		for (Chapter chapter: chapters) {
			progressList.add(chapter.getProgress());
		}
		return new Progress(progressList);
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
	 * Return a list of chapters in this book. Used when the user has opened a book.
	 * If the user has internet connection, the chapters are queried from the web
	 * and the data in the SQLite database is overwritten.
	 * If the user doesn't have internet connection, the chapters are read from the
	 * SQLite database.
	 * @param context		The current activity's context (needed for network connection check).
	 * @return the list of chapters in this book.
	 */
	public List<Chapter> getChapters() {
		// TODO: If the user has internet connection, rewrite the chapters in the database.
		// If not, use the chapters from the SQLite database.
		loadChapters();
		return chapters;
	}
}
