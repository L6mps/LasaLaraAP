package com.lasalara.lasalara.backend.structure;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.FormatExceptionMessage;
import com.lasalara.lasalara.backend.exceptions.NumericException;
import com.lasalara.lasalara.backend.exceptions.NumericExceptionMessage;
import com.lasalara.lasalara.backend.exceptions.WebRequestException;
import com.lasalara.lasalara.backend.exceptions.WebRequestExceptionMessage;
import com.lasalara.lasalara.backend.webRequest.UrlParameters;
import com.lasalara.lasalara.backend.webRequest.WebRequest;

import android.database.Cursor;

/**
 * Class responsible for holding a chapter's information and querying it's questions' information.
 * @author Ants-Oskar Mäesalu
 */
public class Chapter {
	private String key;						// The chapter's UUID
	private String title;					// Name of the chapter
	private int version;					// If the author updates a chapter, its version number is incremented. Version numbers let the app know when to re-download chapter questions.
	private String authorEmail;				// E-mail address of the person who wrote the chapter
	private String authorName;				// Name of the person who wrote the chapter (if blank, the e-mail is used)
	private String authorInstitution;		// Institution of the person who wrote the chapter (if blank, the e-mail is used)
	private boolean proposalsAllowed;		// Has the author allowed question proposals for the chapter?
	private int position;					// The position of the chapter in the book (the order is set by the book owner)
	private String bookKey;					// The book the chapter is located in.

	/**
	 * Constructor, used when downloading a chapter from the web.
	 * The questions in all of these chapters are also downloaded.
	 * TODO: Recursive asynchronous downloading.
	 * @param key					The chapter's UUID key.
	 * @param title					The chapter's title.
	 * @param version				The chapter's version. Version numbers let the app know when to re-download chapter questions.
	 * @param authorEmail			The chapter's author's e-mail address.
	 * @param authorName			The chapter's author's name (or left null if blank).
	 * @param authorInstitution		The chapter's author's institution (or left null if blank).
	 * @param proposalsAllowed		Boolean value, whether the author allows question proposals or not.
	 * @param bookKey				The book the chapter is located in.
	 * @param insertIntoDatabase	Whether to insert the downloaded chapter into the database or not.
	 * @throws WebRequestException 
	 * @throws FormatException 
	 */
	public Chapter(String key, String title, int version, String authorEmail, 
			String authorName, String authorInstitution, boolean proposalsAllowed, int position,
			String bookKey, boolean insertIntoDatabase) throws WebRequestException, FormatException {
		this.key = key;
		this.title = title;
		this.version = version;
		this.authorEmail = authorEmail;
		this.authorName = authorName;
		this.authorInstitution = authorInstitution;
		this.proposalsAllowed = proposalsAllowed;
		this.position = position;
		this.bookKey = bookKey;
		if (insertIntoDatabase) {
			DatabaseHelper.getInstance().getChapterHelper().insertChapter(this);
			downloadQuestions();
		}
	}
	
	/**
	 * Constructor, used when querying data from the internal SQLite database.
	 * The question data is not queried right away - there are separate methods for that.
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
	}
	
	/**
	 * Update this chapter's database row.
	 */
	private void updateInDatabase() {
		DatabaseHelper.getInstance().getChapterHelper().updateChapter(this);
	}
	
	/**
	 * Update this chapter's position value in the database.
	 */
	private void updatePositionInDatabase() {
		DatabaseHelper.getInstance().getChapterHelper().updateChapterPosition(this);
	}
	
	/**
	 * Delete this chapter from the database.
	 * Also deletes all of the associated questions from the database.
	 */
	private void deleteFromDatabase() {
		DatabaseHelper.getInstance().getChapterHelper().deleteChapter(this);
	}
	
	/**
	 * Add a new question into the SQLite database.
	 * @param question		The question string.
	 * @param answer		The answer string.
	 * @param chapterKey	The parent chapter's UUID.
	 */
	private void addNewQuestionToDatabase(String question, String answer, String chapterKey) {
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		DatabaseHelper.getInstance().getQuestionHelper().insertQuestion(question, answer, 0, 0, currentTime, currentTime, chapterKey);
	}

	/**
	 * Load the questions in this book.
	 * @param context			The current activity's context (needed for network connection check).
	 * @throws WebRequestException 
	 * @throws FormatException 
	 * @throws IOException
	 * @throws JSONException
	 */
	private void downloadQuestions() throws WebRequestException, FormatException {
		String url = StringConstants.URL_GET_QUESTIONS;
		UrlParameters urlParameters = new UrlParameters();
		urlParameters.addPair("ck", key);
		WebRequest request = null;
		try {
			request = new WebRequest(url, urlParameters);
		} catch (IOException e1) {
			throw new WebRequestException(WebRequestExceptionMessage.QUESTIONS_DOWNLOAD);
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
					addNewQuestionToDatabase(question, answer, key);
				}
			} else {
				throw new FormatException(FormatExceptionMessage.QUESTIONS_DOWNLOAD_LIST);
			}
		} catch (JSONException e) {
			throw new FormatException(FormatExceptionMessage.QUESTIONS_DOWNLOAD);
		}
	}
	
	/**
	 * Update this chapter. Used when the book updating system detects a change in the chapter's
	 * version number.
	 * All of the currently existing questions are purged from the database and new ones are 
	 * downloaded.
	 * @throws WebRequestException 
	 * @throws FormatException 
	 */
	public void update(Chapter updatedChapter) throws WebRequestException, FormatException {
		updateProperties(updatedChapter);
		deleteQuestions();
		downloadQuestions();
	}
	
	/**
	 * Set a new position to the chapter. Used for correctly ordering the chapters in the book.
	 * @param newPosition	The new position in the chapter list.
	 */
	public void updatePosition(int newPosition) {
		if (position != newPosition) {
			position = newPosition;
			updatePositionInDatabase();
		}
	}
	
	/**
	 * Update only the main properties of this chapter without changing any of the questions.
	 * Used when the book updating system detects no change in the chapter's version number, yet
	 * the metadata of the chapter might still be updated.
	 * @param updatedChapter	The updated chapter from where to get the new properties.
	 */
	public void updateProperties(Chapter updatedChapter) {
		title = updatedChapter.getTitle();
		version = updatedChapter.getVersion();
		authorEmail = updatedChapter.getAuthorEmail();
		authorName = updatedChapter.getAuthorName();
		authorInstitution = updatedChapter.getAuthorInstitution();
		proposalsAllowed = updatedChapter.areProposalsAllowed();
		updateInDatabase();
	}
	
	/**
	 * Delete all of the questions from this chapter.
	 */
	private void deleteQuestions() {
		List<Question> questions = getAllQuestions();
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
		List<Question> questions = getAllQuestions();
		for (Question question: questions) {
			question.resetProgress();
		}
	}
	
	/**
	 * Pose a question for this chapter to the chapter's author.
	 * The data is sent to the author through a web request.
	 * @param question	The new question's question string.
	 * @param answer	The new question's answer.
	 * @throws WebRequestException 
	 */
	public void poseQuestion(String question, String answer) throws WebRequestException { // TODO: Test
		String url = StringConstants.URL_POSE_QUESTION;
		UrlParameters urlParameters = new UrlParameters();
		urlParameters.addPair("ck", key);
		urlParameters.addPair("question", question);
		urlParameters.addPair("answer", answer);
		try {
			new WebRequest(url, urlParameters);
		} catch (IOException e1) {
			throw new WebRequestException(WebRequestExceptionMessage.QUESTION_POSING);
		}
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
	
	/**
	 * @return the number of questions the user has still got to answer in this chapter.
	 */
	public int getNumberOfUnansweredQuestions() {
		return DatabaseHelper.getInstance().getQuestionHelper().getNumberOfUnansweredQuestions(key);
	}
	
	/**
	 * @return whether the chapter is completed or not.
	 */
	public boolean isCompleted() {
		return (getNumberOfUnansweredQuestions() == 0);
	}

	/**
	 * Return a list of all of the questions in this chapter.
	 * Used for the page view or deleting the chapter.
	 * The questions are read from the SQLite database.
	 * @return the list of questions in this chapter.
	 */
	public List<Question> getAllQuestions() {
		return DatabaseHelper.getInstance().getQuestionHelper().getAllQuestions(key);
	}
	
	/**
	 * Return the next question in the list for this chapter.
	 * The chapters are read from the SQLite database and only the first one is returned.
	 * @return the next question in the list for this chapter.
	 * @throws NumericException 
	 */
	public Question getNextQuestion() throws NumericException {
		Question nextQuestion = DatabaseHelper.getInstance().getQuestionHelper().getNextQuestion(key);
		if (nextQuestion == null) {
			String nextTime = DatabaseHelper.getInstance().getQuestionHelper().getNextAvailableTime(key);
			if (nextTime != null) {
				Backend.getInstance().addMessage("The chapter has been completed. You can revise the chapter starting from " + nextTime + ".");
			} else {
				throw new NumericException(NumericExceptionMessage.CHAPTER_NEXT_TIME_MISSING);
			}
		}
		return nextQuestion;
	}
}