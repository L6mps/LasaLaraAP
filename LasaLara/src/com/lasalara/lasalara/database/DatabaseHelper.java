package com.lasalara.lasalara.database;

import com.lasalara.lasalara.constants.NumericalConstants;
import com.lasalara.lasalara.constants.StringConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class that handles all of the SQLite database operations.
 * @author Ants-Oskar Mäesalu
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private BookHelper bookHelper;
	private ChapterHelper chapterHelper;
	private QuestionHelper questionHelper;

	/**
	 * Constructor.	 
	 * @param context	The current activity's context.
	 */
	public DatabaseHelper(Context context) {
		super(context, StringConstants.DATABASE_NAME, null, NumericalConstants.DATABASE_VERSION);
		bookHelper = new BookHelper(this);
		chapterHelper = new ChapterHelper(this);
		questionHelper = new QuestionHelper(this);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		bookHelper.onCreate(db);
		chapterHelper.onCreate(db);
		questionHelper.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		bookHelper.onUpgrade(db, oldVersion, newVersion);
		chapterHelper.onUpgrade(db, oldVersion, newVersion);
		questionHelper.onUpgrade(db, oldVersion, newVersion);
	}

	/**
	 * @return the book helper class for the SQLite database.
	 */
	public BookHelper getBookHelper() {
		return bookHelper;
	}

	/**
	 * @return the chapter helper class for the SQLite database.
	 */
	public ChapterHelper getChapterHelper() {
		return chapterHelper;
	}
	
	/**
	 * @return the question helper class for the SQLite database.
	 */
	public QuestionHelper getQuestionHelper() {
		return questionHelper;
	}
}
