package com.lasalara.lasalara.backend.database;

import java.util.ArrayList;
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
			StringConstants.QUESTION_COLUMN_REVIEW_TIME + " TIMESTAMP, " +
			StringConstants.QUESTION_COLUMN_KNOWN_TIME + " TIMESTAMP, " +
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
		contentValues.put(StringConstants.QUESTION_COLUMN_REVIEW_TIME, question.getReviewTime());
		contentValues.put(StringConstants.QUESTION_COLUMN_KNOWN_TIME, question.getKnownTime());
		contentValues.put(StringConstants.QUESTION_COLUMN_CHAPTER_KEY, question.getChapterKey());
		database.insert(StringConstants.QUESTION_TABLE_NAME, null, contentValues);
	}
	
	public void deleteQuestion(Question question) {
		// TODO
	}
	
	public void deleteQuestions(Chapter chapter) {
		// TODO
	}
	
	/**
	 * @param chapterKey	The UUID of a chapter.
	 * @return a list of questions in a certain chapter saved into the SQLite database.
	 */
	public List<Question> getQuestions(String chapterKey) {
		List<Question> questionList = new ArrayList<Question>();
		String selectQuestionsQuery =
				"SELECT * FROM " + StringConstants.QUESTION_TABLE_NAME +  
				" WHERE " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + 
				"=" + chapterKey;
		Cursor results =  database.rawQuery(selectQuestionsQuery, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			questionList.add(new Question(results));
			moveSucceeded = results.moveToNext();
		}
		return questionList;
	}
}