package com.lasalara.lasalara.database;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.backend.Question;
import com.lasalara.lasalara.constants.StringConstants;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class that handles all of the SQLite database operations on questions.
 * @author Ants-Oskar Mäesalu
 */
public class QuestionHelper {
	// SQLite database helper class
	private DatabaseHelper databaseHelper;
	// Basic database queries
	private static final String TABLE_CREATE =
			"CREATE TABLE " + 
			StringConstants.QUESTION_TABLE_NAME + " (" +
			StringConstants.QUESTION_COLUMN_QUESTION + " TEXT, " +
			StringConstants.QUESTION_COLUMN_ANSWER + " TEXT, " +
			StringConstants.QUESTION_COLUMN_CHAPTER_KEY + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.QUESTION_TABLE_NAME;

	/**
	 * Constructor.
	 * @param databaseHelper	The SQLite database helper class.
	 */
    QuestionHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Actions conducted on database creation.
     * @param database	The SQLite database.
     */
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    /**
     * Actions conducted on database upgrade.
     * @param database			The SQLite database.
     * @param oldVersion	The old database's version number.
     * @param newVersion	The new database's version number.
     */
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO: Create a better method of upgrading the database.
		// Currently, the most straigthforward way to upgrade the database is to drop the
		// previous database, create a new one and then repopulate it.
		database.execSQL(TABLE_DROP);
		onCreate(database);
	}
	
	/**
	 * Insert a new question into the SQLite database.
	 * @param question	The question object's instance.
	 */
	public void insertQuestion(Question question) {
		// TODO: Check existence - update if exists?
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.QUESTION_COLUMN_QUESTION, question.getQuestion());
		contentValues.put(StringConstants.QUESTION_COLUMN_ANSWER, question.getAnswer());
		contentValues.put(StringConstants.QUESTION_COLUMN_CHAPTER_KEY, question.getChapterKey());
		database.insert(StringConstants.QUESTION_TABLE_NAME, null, contentValues);
	}
	
	/**
	 * @param chapterKey	The UUID of a chapter.
	 * @return a list of questions in a certain chapter saved into the SQLite database.
	 */
	public List<Question> getQuestions(String chapterKey) {
		List<Question> questionList = new ArrayList<Question>();
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		String selectQuestionsQuery =
				"SELECT * FROM " + StringConstants.QUESTION_TABLE_NAME +  
				" WHERE " + StringConstants.QUESTION_COLUMN_CHAPTER_KEY + 
				"=" + chapterKey;
		Cursor results =  database.rawQuery(selectQuestionsQuery, null);
		results.moveToFirst();
		while (!results.isAfterLast()) {
			questionList.add(new Question(databaseHelper, results));
			results.moveToNext();
		}
		return questionList;
	}
}