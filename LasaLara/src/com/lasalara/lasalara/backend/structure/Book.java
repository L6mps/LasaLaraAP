package com.lasalara.lasalara.backend.structure;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.FormatExceptionMessage;
import com.lasalara.lasalara.backend.exceptions.InputDoesntExistException;
import com.lasalara.lasalara.backend.exceptions.InputDoesntExistExceptionMessage;
import com.lasalara.lasalara.backend.webRequest.UrlParameters;
import com.lasalara.lasalara.backend.webRequest.WebRequest;

import android.database.Cursor;

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
	
	/**
	 * Constructor, used when downloading a book from the web.
	 * The chapters are not downloaded when using this constructor, instead, they are downloaded,
	 * if need be, using the load() method. This allows the user to see the book in the interface
	 * before it has been completely downloaded from the web.
	 * @param ownerEmail			The book's owner's e-mail address.
	 * @param title					The book's title.
	 * @param insertIntoDatabase	Whether to insert the book into the database or not.
	 * @throws InputDoesntExistException 
	 * @throws FormatException 
	 */
	public Book(String ownerEmail, String title, boolean insertIntoDatabase) throws InputDoesntExistException, FormatException {
		String url = StringConstants.URL_GET_BOOK;
		UrlParameters urlParameters = new UrlParameters();
		urlParameters.addPair("em", ownerEmail.toLowerCase(Locale.ENGLISH));
		urlParameters.addPair("bt", title.toLowerCase(Locale.ENGLISH));
		WebRequest request = null;
		try {
			request = new WebRequest(url, urlParameters);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			JSONObject result = request.getJSONObject();
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
					if (insertIntoDatabase) {
						DatabaseHelper.getInstance().getBookHelper().insertBook(this);
					}
				} else {
					throw new FormatException(FormatExceptionMessage.BOOK_DOWNLOAD_EMAIL);
				}
			} catch (JSONException e) {
				int errorCode = result.getInt("err");
				if (errorCode == 0) {
					throw new InputDoesntExistException(InputDoesntExistExceptionMessage.BOOK_DOWNLOAD_EMAIL);
				} else {
					throw new InputDoesntExistException(InputDoesntExistExceptionMessage.BOOK_DOWNLOAD_TITLE);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chapters = new ArrayList<Chapter>();
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
		preloadChapters();
	}
	
	/**
	 * Preload all of the chapter data for this book from the SQLite database.
	 */
	private void preloadChapters() {
		chapters = DatabaseHelper.getInstance().getChapterHelper().getChapters(key);
	}
	
	/**
	 * Load the book's contents from the SQLite database. If there are no chapters in the SQLite
	 * database, they are downloaded from the web. Used when the user clicks on the book.
	 * @param insertIntoDatabase	Whether to insert the chapters into the database or not.
	 */
	private void load(boolean insertIntoDatabase) {
		if (chapters.isEmpty()) {
			downloadChapters(insertIntoDatabase);
		}
	}
	
	/**
	 * Delete this book from the database.
	 * Also deletes all of the associated chapters and their questions from the database.
	 */
	private void deleteFromDatabase() {
		DatabaseHelper.getInstance().getBookHelper().deleteBook(this);
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
	 * Download the chapters in this book from the web.
	 * @param context		The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	private void downloadChapters(boolean insertIntoDatabase) {
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
			request = new WebRequest(url, urlParameters);
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
				chapters.add(new Chapter(chapterKey, chapterTitle, chapterVersion, 
						chapterAuthorEmail, chapterAuthorName, chapterAuthorInstitution, 
						chapterProposalsAllowed, i, key, insertIntoDatabase));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Update this book. Used when the user clicks the corresponding menu item.
	 * All of the chapters are checked. If a version number of a chapter has been changed,
	 * the database is due to be updated.
	 * Appropriate notification messages are sent to the user.
	 * @throws FormatException 
	 * @throws InputDoesntExistException 
	 */
	public void update() throws InputDoesntExistException, FormatException {
		Book updatedBook = new Book(ownerEmail, title, false);
		List<Chapter> updatedChapters = updatedBook.getChapters(false);
		int numberOfInsertedChapters = 0;
		int numberOfUpdatedChapters = 0;
		int numberOfDeletedChapters = 0;
		// Check already existing chapters
		for (Chapter existingChapter: chapters) {
			boolean existsInUpdatedBook = false;
			for (Chapter updatedChapter: updatedChapters) {
				if (existingChapter.getKey() == updatedChapter.getKey()) {
					if (existingChapter.getVersion() != updatedChapter.getVersion()) {
						existingChapter.update(updatedChapter);
						numberOfUpdatedChapters++;
					}
					existsInUpdatedBook = true;
				}
			}
			if (!existsInUpdatedBook) {
				existingChapter.delete();
				chapters.remove(existingChapter);
				numberOfDeletedChapters++;
			}
		}
		// Check new chapters
		for (int i = 0; i < updatedChapters.size(); i++) {
			boolean exists = false;
			for (int j = 0; j < chapters.size(); j++) {
				if (chapters.get(j).getKey() == updatedChapters.get(i).getKey()) {
					exists = true;
				}
			}
			if (!exists) {
				chapters.add(i, updatedChapters.get(i)); // TODO: Test
				numberOfInsertedChapters++;
			}
		}
		// Send appropriate notification messages to the user and update chapter position numbers (if necessary).
		if (numberOfInsertedChapters == 0 && numberOfUpdatedChapters == 0 && numberOfDeletedChapters == 0) {
			Backend.getInstance().addMessage("No changes were made to the book.");
		} else {
			String insertedVerb = (numberOfInsertedChapters == 1) ? "was" : "were";
			String updatedVerb = (numberOfUpdatedChapters == 1) ? "was" : "were";
			String deletedVerb = (numberOfDeletedChapters == 1) ? "was" : "were";
			Backend.getInstance().addMessage(numberOfInsertedChapters + " chapters " + insertedVerb + " inserted, " + numberOfDeletedChapters + " chapters " + deletedVerb + " deleted, and " + numberOfUpdatedChapters + " chapters " + updatedVerb + " updated in the book.");
			// Update chapter position numbers
			for (int i = 0; i < chapters.size(); i++) {
				chapters.get(i).updatePosition(i);
			}
		}
	}
	
	/**
	 * Delete a chapter with the specified index from the application.
	 * @param index		The chapter's index in the chapter list.
	 */
	public void deleteChapter(int index) {
		chapters.get(index).delete();
		chapters.remove(index);
	}
	
	/**
	 * Delete all of the chapters from this book.
	 */
	private void deleteChapters() {
		for (Chapter chapter: chapters) {
			chapter.delete();
		}
		chapters.clear();
	}
	
	/**
	 * Delete this book from the application.
	 */
	public void delete() {
		deleteChapters();
		deleteFromDatabase();
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
	 * @return whether the book is completed or not.
	 */
	public boolean isCompleted() {
		for (Chapter chapter: chapters) {
			if (!chapter.isCompleted()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Return a list of chapters in this book. Used when the user has opened a book.
	 * The chapters are read from the SQLite database. If the book is used for the first time,
	 * the data is first downloaded from the web.
	 * If the value of insertIntoDatabase is false, the chapters are not inserted into the database.
	 * @param context		The current activity's context (needed for network connection check).
	 * @return the list of chapters in this book.
	 */
	private List<Chapter> getChapters(boolean insertIntoDatabase) {
		load(insertIntoDatabase);
		return chapters;
	}
	
	/**
	 * Return a list of chapters in this book. Used when the user has opened a book.
	 * The chapters are read from the SQLite database. If the book is used for the first time,
	 * the data is first downloaded from the web.
	 * @param context		The current activity's context (needed for network connection check).
	 * @return the list of chapters in this book.
	 */
	public List<Chapter> getChapters() {
		load(true);
		return chapters;
	}
}
