package com.lasalara.lasalara.backend;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.constants.StringConstants;

import android.content.Context;
import android.database.Cursor;

/**
 * Class responsible for holding and querying a book's information and querying it's chapters' information.
 * @author Ants-Oskar Mäesalu
 */
public class Book {
	String key;				// The book's UUID
	String title;			// Name of the book
	String ownerEmail;		// E-mail of the person who created the book
	String ownerName;		// Name of the person who created the book (if blank, the e-mail is used)
	String ownerInstitution;// Institution of the person who created the book (if blank, the e-mail is used)
	String lastChapter;		// Last chapter (UUID) of this book opened by the student
	List<Chapter> chapters; // The list of chapters in this book.
	
	/**
	 * Constructor, used when downloading a book from the web.
	 * @param context		The current activity's context (needed for network connection check).
	 * @param ownerEmail	The book's owner's e-mail address.
	 * @param title			The book's title.
	 * @throws IOException
	 * @throws JSONException
	 */
	public Book(Context context, String ownerEmail, String title) throws IOException, JSONException {
		String url = StringConstants.URL_GET_BOOK;
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
				// TODO: Insert into database (or update if already exists?)
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
	 * Constructor, used when querying data from the internal SQLite database.
	 * @param dbResults
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
		if (dbResults.isNull(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_LAST_CHAPTER))) {
			lastChapter = null;
		} else {
			lastChapter = dbResults.getString(dbResults.getColumnIndex(StringConstants.BOOK_COLUMN_LAST_CHAPTER));
		}
	}
	
	/**
	 * Load the chapters in this book.
	 * @param context		The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	private void loadChapters(Context context) throws IOException, JSONException {
		chapters = new ArrayList<Chapter>();
		String url = StringConstants.URL_GET_CHAPTERS;
		String urlParameters = "bk=" + URLEncoder.encode(key, "UTF-8");
		WebRequest request = new WebRequest(context, url, urlParameters);
		try {
			JSONObject result = request.getJSONObject();
			System.out.println(result);
			for (int i = 0; i < result.length(); i++) {
				JSONObject chapterObject = result.getJSONObject(Integer.toString(i));
				System.out.println(chapterObject.toString());
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
				chapters.add(new Chapter(chapterKey, chapterTitle, chapterVersion, 
						chapterAuthorEmail, chapterAuthorName, chapterAuthorInstitution, 
						chapterProposalsAllowed, key));
			}
		} catch (JSONException e) {
			throw new JSONException(e.toString());
		}
	}
	
	/**
	 * Reload the chapter list.
	 * TODO: Currently never used but should be periodically used to update the content.
	 * @param context		The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	public void reloadChapters(Context context) throws IOException, JSONException {
		loadChapters(context);
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
	
	/**
	 * @param context		The current activity's context (needed for network connection check).
	 * @return the list of chapters in this book.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public List<Chapter> getChapters(Context context) throws IOException, JSONException {
		loadChapters(context);
		return chapters;
	}
}
