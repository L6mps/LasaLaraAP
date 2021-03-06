package com.lasalara.lasalara.backend.database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Question;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class that handles all of the SQLite database operations on questions.
 * @author Ants-Oskar M�esalu
 */
public class QuestionHelper {
	// SQLite database class
	private SQLiteDatabase database;
	// Basic database queries
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + 
			StringConstants.QUESTION_TABLE_NAME + " (" +
			StringConstants.QUESTION_COLUMN_QUESTION + " TEXT, " +
			StringConstants.QUESTION_COLUMN_ANSWER + " TEXT, " +
			StringConstants.QUESTION_COLUMN_REVIEW_COUNT + " INT, " +
			StringConstants.QUESTION_COLUMN_KNOWN_COUNT + " INT, " +
			StringConstants.QUESTION_COLUMN_REVIEW_TIME + " TEXT, " +
			StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + " TEXT, " +
			StringConstants.QUESTION_COLUMN_CHAPTER_KEY + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.QUESTION_TABLE_NAME;

	/**
	 * Constructor.
	 * @param database		The SQLite database class.
	 */
    QuestionHelper(SQLiteDatabase database) {
        this.database = database;
    }

    /**
     * Actions conducted on database creation.
     */
    public void onCreate() {
        database.execSQL(TABLE_CREATE);
    }

    /**
     * Actions conducted on database upgrade.
     * @param oldVersion	The old database's version number.
     * @param newVersion	The new database's version number.
     */
	public void onUpgrade(int oldVersion, int newVersion) {
		// TODO: Create a better method of upgrading the database.
		// Currently, the most straigthforward way to upgrade the database is to drop the
		// previous database, create a new one and then repopulate it.
		database.execSQL(TABLE_DROP);
		onCreate();
	}
	
	/**
	 * @param question		The question string.
	 * @param answer		The answer string.
	 * @param chapterKey	The parent chapter's UUID.
	 * @return whether the question already exists in the database or not.
	 */
	private boolean existsInDatabase(String question, String answer, String chapterKey) {
		String[] columns = {StringConstants.QUESTION_COLUMN_QUESTION};
		String whereClause = StringConstants.QUESTION_COLUMN_QUESTION + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_ANSWER + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?";
		String[] whereArguments = {question, answer, chapterKey};
		Cursor results = database.query(StringConstants.QUESTION_TABLE_NAME, columns, whereClause, whereArguments, null, null, null);
		boolean exists = results.moveToFirst();
		results.close();
		return exists;
	}
	
	/**
	 * Insert a new question into the SQLite database.
	 * @param question			The question string.
	 * @param answer			The answer string.
	 * @param reviewCount		The amount of times the question has been reviewed.
	 * @param knownCount		The amount of times the question has been set as known.
	 * @param reviewTime		The time the question was last reviewed.
	 * @param knownUntilTime	The time until which the question remains hidden.
	 * @param chapterKey		The parent chapter's UUID.
	 */
	public void insertQuestion(String question, String answer, int reviewCount, int knownCount, Timestamp reviewTime, Timestamp knownUntilTime, String chapterKey) {
		if (existsInDatabase(question, answer, chapterKey)) {
			deleteQuestion(question, answer, chapterKey);
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.QUESTION_COLUMN_QUESTION, question);
		contentValues.put(StringConstants.QUESTION_COLUMN_ANSWER, answer);
		contentValues.put(StringConstants.QUESTION_COLUMN_REVIEW_COUNT, reviewCount);
		contentValues.put(StringConstants.QUESTION_COLUMN_KNOWN_COUNT, knownCount);
		contentValues.put(StringConstants.QUESTION_COLUMN_REVIEW_TIME, reviewTime.toString()); // TODO: Test if string conversion is the correct way to handle this
		contentValues.put(StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME, knownUntilTime.toString()); // TODO: Test if string conversion is the correct way to handle this
		contentValues.put(StringConstants.QUESTION_COLUMN_CHAPTER_KEY, chapterKey);
		database.insert(StringConstants.QUESTION_TABLE_NAME, null, contentValues);
	}
	
	/**
	 * Update a question in the SQLite database.
	 * @param question	The question object's instance.
	 */
	public void updateQuestion(Question question) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.QUESTION_COLUMN_REVIEW_COUNT, question.getReviewCount());
		contentValues.put(StringConstants.QUESTION_COLUMN_KNOWN_COUNT, question.getKnownCount());
		contentValues.put(StringConstants.QUESTION_COLUMN_REVIEW_TIME, question.getReviewTime().toString()); // TODO: Test if string conversion is the correct way to handle this
		contentValues.put(StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME, question.getKnownUntilTime().toString()); // TODO: Test if string conversion is the correct way to handle this
		String whereClause = StringConstants.QUESTION_COLUMN_QUESTION + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_ANSWER + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?";
		String[] whereArguments = {question.getQuestion(), question.getAnswer(), question.getChapterKey()};
		database.update(StringConstants.QUESTION_TABLE_NAME, contentValues, whereClause, whereArguments);
	}
	
	/**
	 * Delete a question from the SQLite database.
	 * @param question	The question object's instance.
	 */
	public void deleteQuestion(Question question) {
		deleteQuestion(question.getQuestion(), question.getAnswer(), question.getChapterKey());
	}
	
	/**
	 * Delete a question from the SQLite database.
	 * @param question		The question string.
	 * @param answer		The answer string.
	 * @param chapterKey	The parent chapter's UUID.
	 */
	public void deleteQuestion(String question, String answer, String chapterKey) {
		String whereClause = StringConstants.QUESTION_COLUMN_QUESTION + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_ANSWER + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?";
		String[] whereArguments = {question, answer, chapterKey};
		database.delete(StringConstants.QUESTION_TABLE_NAME, whereClause, whereArguments);
	}
	
	/**
	 * Delete all of the questions associated with a certain chapter from the SQLite database.
	 * @param chapter	The chapter object's instance.
	 */
	public void deleteQuestions(Chapter chapter) {
		String whereClause = StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "='" + chapter.getKey() + "'";
		database.delete(StringConstants.QUESTION_TABLE_NAME, whereClause, null);
	}
	
	/**
	 * @param chapterKey	The UUID of a chapter.
	 * @return a list of all of the questions in a certain chapter saved into the SQLite database.
	 */
	public List<Question> getAllQuestions(String chapterKey) {
		List<Question> questionList = new ArrayList<Question>();
		String whereClause = StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?";
		String[] whereArguments = {chapterKey};
		String orderClause = StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + " ASC";
		Cursor results = database.query(StringConstants.QUESTION_TABLE_NAME, null, whereClause, whereArguments, null, null, orderClause);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			questionList.add(new Question(results));
			moveSucceeded = results.moveToNext();
		}
		results.close();
		return questionList;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the next question in the list for this chapter.
	 */
	public Question getNextQuestion(String chapterKey) {
		Question nextQuestion = null;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		String whereClause = StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?" + 
				" AND " + StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + "<=?";
		String[] whereArguments = {chapterKey, currentTime.toString()}; // TODO: Test timestamp comparison
		String orderClause = StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + " ASC";
		Cursor results = database.query(StringConstants.QUESTION_TABLE_NAME, null, whereClause, whereArguments, null, null, orderClause, "1");
		boolean moveSucceeded = results.moveToFirst();
		if (moveSucceeded) {
			nextQuestion = new Question(results);
		}
		results.close();
		return nextQuestion;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the earliest time the chapter could be revised.
	 */
	public String getNextAvailableTime(String chapterKey) {
		String nextTime = null;
		String[] columns = {StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME};
		String whereClause = StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?";
		String[] whereArguments = {chapterKey};
		String orderClause = StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + " ASC";
		Cursor results = database.query(StringConstants.QUESTION_TABLE_NAME, columns, whereClause, whereArguments, null, null, orderClause, "1");
		boolean moveSucceeded = results.moveToFirst();
		if (moveSucceeded) {
			nextTime = results.getString(0);
		}
		results.close();
		return nextTime;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the total number of questions in this chapter.
	 */
	public int getNumberOfQuestions(String chapterKey) {
		int numberOfQuestions = 0;
		String[] columns = {"COUNT(*)"};
		String whereClause = StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?";
		String[] whereArguments = {chapterKey}; // TODO: Test timestamp comparison
		Cursor results = database.query(StringConstants.QUESTION_TABLE_NAME, columns, whereClause, whereArguments, null, null, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			numberOfQuestions = results.getInt(0);
			moveSucceeded = results.moveToNext();
		}
		results.close();
		return numberOfQuestions;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the number of answered questions in this chapter.
	 */
	public int getNumberOfAnsweredQuestions(String chapterKey) {
		int numberOfAnsweredQuestions = 0;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		String[] columns = {"COUNT(*)"};
		String whereClause = StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + ">?";
		String[] whereArguments = {chapterKey, currentTime.toString()}; // TODO: Test timestamp comparison
		Cursor results = database.query(StringConstants.QUESTION_TABLE_NAME, columns, whereClause, whereArguments, null, null, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			numberOfAnsweredQuestions = results.getInt(0);
			moveSucceeded = results.moveToNext();
		}
		results.close();
		return numberOfAnsweredQuestions;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the number of unanswered questions in this chapter.
	 */
	public int getNumberOfUnansweredQuestions(String chapterKey) {
		int numberOfAnsweredQuestions = 0;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		String[] columns = {"COUNT(*)"};
		String whereClause = StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "=?" +
				" AND " + StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + "<=?";
		String[] whereArguments = {chapterKey, currentTime.toString()}; // TODO: Test timestamp comparison
		Cursor results = database.query(StringConstants.QUESTION_TABLE_NAME, columns, whereClause, whereArguments, null, null, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			numberOfAnsweredQuestions = results.getInt(0);
			moveSucceeded = results.moveToNext();
		}
		results.close();
		return numberOfAnsweredQuestions;
	}
}