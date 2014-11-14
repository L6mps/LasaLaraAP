package com.lasalara.lasalara.backend.structure;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.exceptions.NumericException;
import com.lasalara.lasalara.backend.webRequest.UrlParameters;
import com.lasalara.lasalara.backend.webRequest.WebRequest;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Class responsible for holding a chapter's information and querying it's questions' information.
 * @author Ants-Oskar Mäesalu
 */
public class Chapter {
	private String key;						// The chapter's UUID
	private String title;					// Name of the chapter
	private int version;					// If the author updates a chapter, its version number is incremented. Version numbers let the app know when to re-download chapter questions.
	private String authorEmail;				// E-mail address of the person who wrote the chapter, TODO: Own class for e-mail?
	private String authorName;				// Name of the person who wrote the chapter (if blank, the e-mail is used)
	private String authorInstitution;		// Institution of the person who wrote the chapter (if blank, the e-mail is used)
	private boolean proposalsAllowed;		// Has the author allowed question proposals for the chapter?
	private int position;					// The position of the chapter in the book (the order is set by the book owner)
	private String bookKey;					// The book the chapter is located in.
	private List<Question> questions;		// The list of questions in this book. // TODO: Kestarafor nerta

	private Context context;
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
			String authorName, String authorInstitution, boolean proposalsAllowed, int position,
			String bookKey) {
		//Log.d(StringConstants.APP_NAME, "Chapter constructor: " + key + ", " + title + ".");
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
		this.key = key;
		this.title = title;
		this.version = version;
		this.authorEmail = authorEmail;
		this.authorName = authorName;
		this.authorInstitution = authorInstitution;
		this.proposalsAllowed = proposalsAllowed;
		this.position = position;
		this.bookKey = bookKey;
		databaseHelper.getChapterHelper().insertChapter(this); // TODO: Test
		loadQuestions(); // TODO: Kestarafor nerta
		
		this.context = context;
	}
	
	/**
	 * Constructor, used when querying data from the internal SQLite database.
	 * @param dbResults			Database query results.
	 */
	public Chapter(Cursor dbResults) {
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
		position = dbResults.getInt(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_POSITION));
		bookKey = dbResults.getString(dbResults.getColumnIndex(StringConstants.CHAPTER_COLUMN_BOOK_KEY));
		preloadQuestions(); // TODO: Kestarafor nerta
	}

	// TODO: Kestarafor nerta
	/**
	 * Preload all of the question data for this chapter from the SQLite database.
	 */
	void preloadQuestions() {
		questions = DatabaseHelper.getInstance().getQuestionHelper().getQuestions(key);
	}
	
	/**
	 * Update this chapter's database row.
	 */
	private void updateInDatabase() {
		DatabaseHelper.getInstance().getChapterHelper().updateChapter(this);
	}
	
	/**
	 * Delete this chapter from the database.
	 * Also deletes all of the associated questions from the database.
	 */
	private void deleteFromDatabase() {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
		databaseHelper.getChapterHelper().deleteChapter(this);
	}

	// TODO: Kestarafor nerta
	/**
	 * Load the questions in this book.
	 * @param context			The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	public void loadQuestions() {
		questions = new ArrayList<Question>();
		String url = StringConstants.URL_GET_QUESTIONS;
		UrlParameters urlParameters = new UrlParameters();
		try {
			urlParameters.addPair("ck", key);
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
			JSONObject result = request.getJSONObject();
			System.out.println(result);
			JSONArray questionList = result.getJSONArray("questions");
			JSONArray answerList = result.getJSONArray("answers");
			if (questionList.length() == answerList.length()) {
				for (int i = 0; i < questionList.length(); i++) {
					String question = questionList.getString(i);
					String answer = answerList.getString(i);
					questions.add(new Question(context, question, answer, key));
				}
			} else {
				throw new RuntimeException();
				// TODO: Parse error
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update() {
		// TODO
	}
	
	/**
	 * Delete a question with the specified index from the application.
	 * @param index		The question's index in the question list.
	 */
	public void deleteQuestion(int index) {
		questions.get(index).delete();
		questions.remove(index);
	}
	
	/**
	 * Delete all of the questions from this chapter.
	 */
	public void deleteQuestions() {
		for (Question question: questions) {
			question.delete();
		}
		questions.clear();
	}
	
	/**
	 * Delete this chapter from the application.
	 */
	public void delete() {
		deleteQuestions();
		deleteFromDatabase();
	}
	
	/**
	 * Reset the chapter's progress.
	 */
	public void resetProgress() {
		for (Question question: questions) {
			question.resetProgress();
		}
		// TODO: Do we have to also reset the number of answered questions stored? We probably should.
	}
	
	/**
	 * Calculate the chapter's progress based on the number of questions and the number of questions answered.
	 * @return the chapter's Progress object.
	 */
	public Progress getProgress() {
		try {
			return new Progress(getNumberOfAnsweredQuestions(), getNumberOfQuestions());
		} catch (NumericException e) {
			Backend.getInstance().addMessage("Chapter " + getKey() + ": " + e.getMessage());
			try {
				return new Progress(0, 0);
			} catch (NumericException e1) {
				return null;
			}
		}
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
	 * @return the chapter's position in the chapter list.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return the book's key the chapter is located in.
	 */
	public String getBookKey() {
		return bookKey;
	}
	
	/**
	 * @return the number of questions in this chapter.
	 */
	public int getNumberOfQuestions() {
		return DatabaseHelper.getInstance().getQuestionHelper().getNumberOfQuestions(key);
	}
	
	/**
	 * @return the number of questions the user has answered in this chapter.
	 */
	public int getNumberOfAnsweredQuestions() {
		return DatabaseHelper.getInstance().getQuestionHelper().getNumberOfAnsweredQuestions(key);
	}

	// TODO: Kestarafor nerta
	/**
	 * Return a list of questions in this chapter. Used when the user loads the chapters in a book.
	 * The chapters are read from the SQLite database.
	 * @return the list of questions in this chapter.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public List<Question> getQuestions() {
		return questions;
	}
	
	/**
	 * Return the next question in the list for this chapter.
	 * The chapters are read from the SQLite database and only the first one is returned.
	 * @return the next question in the list for this chapter.
	 */
	public Question getNextQuestion() {
		return DatabaseHelper.getInstance().getQuestionHelper().getNextQuestion(key);
	}
}