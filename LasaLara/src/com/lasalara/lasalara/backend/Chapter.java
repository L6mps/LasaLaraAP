package com.lasalara.lasalara.backend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.constants.StringConstants;
import com.lasalara.lasalara.database.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;

/**
 * Class responsible for holding a chapter's information and querying it's questions' information.
 * @author Ants-Oskar Mäesalu
 */
public class Chapter {
	private DatabaseHelper databaseHelper;	// SQLite database helper class
	private String key;						// The chapter's UUID
	private String title;					// Name of the chapter
	private int version;					// If the author updates a chapter, its version number is incremented. Version numbers let the app know when to re-download chapter questions.
	private String authorEmail;				// E-mail address of the person who wrote the chapter, TODO: Own class for e-mail?
	private String authorName;				// Name aof the person who wrote the chapter (if blank, the e-mail is used)
	private String authorInstitution;		// Institution of the person who wrote the chapter (if blank, the e-mail is used)
	private boolean proposalsAllowed;		// Has the author allowed question proposals for the chapter?
	//private int position;					// The position of the chapter in the book (the order is set by the book owner), TODO: How?
	private String bookKey;					// The book the chapter is located in.
	private List<Question> questions;		// The list of questions in this book.

	/**
	 * Constructor, used when downloading a chapter from the web.
	 * @param context			The current activity's context (needed for network connection check and SQLite database).
	 * @param key				The chapter's UUID key.
	 * @param title				The chapter's title.
	 * @param version			The chapter's version. Version numbers let the app know when to re-download chapter questions.
	 * @param authorEmail		The chapter's author's e-mail address.
	 * @param authorName		The chapter's author's name (or left null if blank).
	 * @param authorInstitution	The chapter's author's institution (or left null if blank).
	 * @param proposalsAllowed	Boolean value, whether the author allows question proposals or not.
	 * @param bookKey			The book the chapter is located in.
	 */
	Chapter(Context context, String key, String title, int version, String authorEmail, 
			String authorName, String authorInstitution, boolean proposalsAllowed, String bookKey) {
		databaseHelper = new DatabaseHelper(context);
		this.key = key;
		this.title = title;
		this.version = version;
		this.authorEmail = authorEmail;
		this.authorName = authorName;
		this.authorInstitution = authorInstitution;
		this.proposalsAllowed = proposalsAllowed;
		this.bookKey = bookKey;
		databaseHelper.getChapterHelper().insertChapter(this); // TODO: Test
	}
	
	/**
	 * Constructor, used when querying data from the internal SQLite database.
	 * @param databaseHelper	The SQLite database helper class.
	 * @param dbResults			Database query results.
	 */
	public Chapter(DatabaseHelper databaseHelper, Cursor dbResults) {
		this.databaseHelper = databaseHelper;
		key = dbResults.getString(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_KEY));
		title = dbResults.getString(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_TITLE));
		version = dbResults.getInt(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_VERSION));
		authorEmail = dbResults.getString(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_AUTHOR_EMAIL));
		if (dbResults.isNull(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_AUTHOR_NAME))) {
			authorName = null;
		} else {
			authorName = dbResults.getString(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_AUTHOR_NAME));
		}
		if (dbResults.isNull(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_AUTHOR_INSTITUTION))) {
			authorInstitution = null;
		} else {
			authorInstitution = dbResults.getString(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_AUTHOR_INSTITUTION));
		}
		proposalsAllowed = (dbResults.getInt(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_PROPOSALS_ALLOWED)) == 1) ? true : false;
		bookKey = dbResults.getString(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_BOOK_KEY));
	}

	/**
	 * Load the questions in this book.
	 * @param context			The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	public void loadQuestions(Context context) throws IOException, JSONException {
		questions = new ArrayList<Question>();
		String url = StringConstants.URL_GET_QUESTIONS;
		UrlParameters urlParameters = new UrlParameters();
		urlParameters.addPair("ck", URLEncoder.encode(key, "UTF-8"));
		WebRequest request = new WebRequest(context, url, urlParameters);
		try {
			JSONObject result = request.getJSONObject();
			System.out.println(result);
			JSONObject questionList = result.getJSONObject("questions");
			JSONObject answerList = result.getJSONObject("answers");
			if (questionList.length() == answerList.length()) {
				for (int i = 0; i < questionList.length(); i++) {
					String question = questionList.getJSONObject(Integer.toString(i)).toString();
					String answer = answerList.getJSONObject(Integer.toString(i)).toString();
					questions.add(new Question(context, question, answer, key));
				}
			} else {
				throw new RuntimeException();
				// TODO: Parse error
			}
		} catch (JSONException e) {
			throw new JSONException(e.toString());
		}
	}
	
	/**
	 * Reload the question list.
	 * TODO: Currently never used but should be used to update the content when the chapter version number is updated.
	 * @param context			The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	public void reloadQuestions(Context context) throws IOException, JSONException {
		loadQuestions(context);
	}

	/**
	 * @return the chapter's UUID key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the chapter's title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the the chapter's version number.
	 * Version numbers let the app know when to re-download chapter questions.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the the chapter's author's e-mail address.
	 */
	public String getAuthorEmail() {
		return authorEmail;
	}

	/**
	 * @return the chapter's author's name (or left null if blank).
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @return the chapter's author's institution (or left null if blank).
	 */
	public String getAuthorInstitution() {
		return authorInstitution;
	}

	/**
	 * @return a boolean value, whether the author allows question proposals or not.
	 */
	public boolean areProposalsAllowed() {
		return proposalsAllowed;
	}

	/**
	 * @return the book's key the chapter is located in.
	 */
	public String getBookKey() {
		return bookKey;
	}

	/**
	 * Return a list of questions in this chapter. Used when the user loads the chapters in a book.
	 * If the user has internet connection, the questions are queried from the web
	 * and the data in the SQLite database is overwritten.
	 * If the user doesn't have internet connection, the questions are read from the
	 * SQLite database.
	 * @param context			The current activity's context (needed for network connection check).
	 * @return the list of questions in this chapter.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public List<Question> getQuestions(Context context) throws IOException, JSONException {
		// TODO: If the user has internet connection, rewrite the questions in the database.
		// If not, use the questions from the SQLite database.
		loadQuestions(context);
		return questions;
	}
}