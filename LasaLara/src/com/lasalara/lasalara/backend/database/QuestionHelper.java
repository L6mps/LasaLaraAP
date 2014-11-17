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
import android.util.Log;

/**
 * Class that handles all of the SQLite database operations on questions.
 * @author Ants-Oskar Mäesalu
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
		Log.d(StringConstants.APP_NAME, "QuestionHelper constructor.");
    }

    /**
     * Actions conducted on database creation.
     */
    public void onCreate() {
    	Log.d(StringConstants.APP_NAME, "QuestionHelper onCreate()");
    	Log.d(StringConstants.APP_NAME, TABLE_CREATE);
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
	 * Insert a new question into the SQLite database.
	 * @param question	The question object's instance.
	 */
	public void insertQuestion(Question question) {
		// TODO: Check existence - update if exists?
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.QUESTION_COLUMN_QUESTION, question.getQuestion());
		contentValues.put(StringConstants.QUESTION_COLUMN_ANSWER, question.getAnswer());
		contentValues.put(StringConstants.QUESTION_COLUMN_REVIEW_COUNT, question.getReviewCount());
		contentValues.put(StringConstants.QUESTION_COLUMN_KNOWN_COUNT, question.getKnownCount());
		contentValues.put(StringConstants.QUESTION_COLUMN_REVIEW_TIME, question.getReviewTime().toString()); // TODO: Test if string conversion is the correct way to handle this
		contentValues.put(StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME, question.getKnownUntilTime().toString()); // TODO: Test if string conversion is the correct way to handle this
		contentValues.put(StringConstants.QUESTION_COLUMN_CHAPTER_KEY, question.getChapterKey());
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
		String whereClause = StringConstants.QUESTION_COLUMN_QUESTION + "='" + question.getQuestion() + "'" +
				" AND " + StringConstants.QUESTION_COLUMN_ANSWER + "='" + question.getAnswer() + "'" +
				" AND " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "='" + question.getChapterKey() + "'";
		database.delete(StringConstants.QUESTION_TABLE_NAME, whereClause, null);
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
		String selectQuestionsQuery =
				"SELECT * FROM " + StringConstants.QUESTION_TABLE_NAME +  
				" WHERE " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "='" + chapterKey + "'" +
				" ORDER BY " + StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + " ASC";
		Log.d(StringConstants.APP_NAME, selectQuestionsQuery);
		Cursor results =  database.rawQuery(selectQuestionsQuery, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			questionList.add(new Question(results));
			moveSucceeded = results.moveToNext();
		}
		return questionList;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the next question in the list for this chapter.
	 */
	public Question getNextQuestion(String chapterKey) {
		Question nextQuestion = null;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		String selectQuestionsQuery =
				"SELECT * FROM " + StringConstants.QUESTION_TABLE_NAME +  
				" WHERE " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "='" + chapterKey + "'" +
				" AND " + StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + "<='" + currentTime.toString() + "'" + // TODO: Test timestamp comparison
				" ORDER BY " + StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + " ASC" +
				" LIMIT 1";
		Log.d(StringConstants.APP_NAME, selectQuestionsQuery);
		Cursor results =  database.rawQuery(selectQuestionsQuery, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			nextQuestion = new Question(results);
			moveSucceeded = results.moveToNext();
		}
		return nextQuestion;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the total number of questions in this chapter.
	 */
	public int getNumberOfQuestions(String chapterKey) {
		int numberOfQuestions = 0;
		String selectQuestionsQuery =
				"SELECT COUNT(*) FROM " + StringConstants.QUESTION_TABLE_NAME +  
				" WHERE " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "='" + chapterKey + "'";
		Log.d(StringConstants.APP_NAME, selectQuestionsQuery);
		Cursor results =  database.rawQuery(selectQuestionsQuery, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			numberOfQuestions = results.getInt(0);
			moveSucceeded = results.moveToNext();
		}
		return numberOfQuestions;
	}
	
	/**
	 * @param chapterKey	The current chapter's UUID.
	 * @return the number of answered questions in this chapter.
	 */
	public int getNumberOfAnsweredQuestions(String chapterKey) {
		int numberOfAnsweredQuestions = 0;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		String selectQuestionsQuery =
				"SELECT COUNT(*) FROM " + StringConstants.QUESTION_TABLE_NAME +  
				" WHERE " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + "='" + chapterKey + "'" +
				" AND " + StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME + ">'" + currentTime.toString() + "'"; // TODO: Test timestamp comparison
		Log.d(StringConstants.APP_NAME, selectQuestionsQuery);
		Cursor results =  database.rawQuery(selectQuestionsQuery, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			numberOfAnsweredQuestions = results.getInt(0);
			moveSucceeded = results.moveToNext();
		}
		return numberOfAnsweredQuestions;
	}
}