package com.lasalara.lasalara.database;

import com.lasalara.lasalara.constants.NumericalConstants;
import com.lasalara.lasalara.constants.StringConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private BookHelper bookHelper;
	private ChapterHelper chapterHelper;
	// TODO: private QuestionHelper questionHelper; // Resolves #37

	public DatabaseHelper(Context context) {
		super(context, StringConstants.DATABASE_NAME, null, NumericalConstants.DATABASE_VERSION);
		bookHelper = new BookHelper(this);
		chapterHelper = new ChapterHelper(this);
		// TODO: QuestionHelper questionHelper = new QuestionHelper(this); // Resolves #37
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		bookHelper.onCreate(db);
		chapterHelper.onCreate(db);
		// TODO: questionHelper.onCreate(db); // Resolves #37
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		bookHelper.onUpgrade(db, oldVersion, newVersion);
		chapterHelper.onUpgrade(db, oldVersion, newVersion);
		// TODO: questionHelper.onUpgrade(db, oldVersion, newVersion); // Resolves #37
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
	/*public QuestionHelper getQuestionHelper() { // TODO: Resolves #37
		return QuestionHelper;
	}*/
}
