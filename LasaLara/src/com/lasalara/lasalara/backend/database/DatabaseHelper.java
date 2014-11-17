package com.lasalara.lasalara.backend.database;

import com.lasalara.lasalara.backend.constants.NumericalConstants;
import com.lasalara.lasalara.backend.constants.StringConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class that handles all of the SQLite database operations.
 * @author Ants-Oskar Mäesalu
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper instance;
	private SQLiteDatabase database;
	private BookHelper bookHelper;
	private ChapterHelper chapterHelper;
	private QuestionHelper questionHelper;

	/**
	 * Constructor.
	 * Private because this is a singleton class.
	 * @param context	The current activity's context.
	 */
	private DatabaseHelper(Context context) {
		super(context, StringConstants.DATABASE_NAME, null, NumericalConstants.DATABASE_VERSION);
		database = getWritableDatabase(); // Responsible for calling the onCreate(db) method
	}
	
	/**
	 * Initialize the singleton database helper class.
	 */
	
	public static void initialiseInstance(Context context) {
		if (instance == null) { // NB: Not thread-safe
			instance = new DatabaseHelper(context);
		}
	}
	
	/**
	 * @return the instance of the database helper.
	 */
	public static DatabaseHelper getInstance() {
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		bookHelper = new BookHelper(db);
		chapterHelper = new ChapterHelper(db);
		questionHelper = new QuestionHelper(db);
		bookHelper.onCreate();
		chapterHelper.onCreate();
		questionHelper.onCreate();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		bookHelper.onUpgrade(oldVersion, newVersion);
		chapterHelper.onUpgrade(oldVersion, newVersion);
		questionHelper.onUpgrade(oldVersion, newVersion);
	}

	/**
	 * @return the book helper class for the SQLite database.
	 */
	public BookHelper getBookHelper() {
		return bookHelper;
	}
	
	//Joos addition
	@Override
	public void onOpen(SQLiteDatabase db) {
		bookHelper = new BookHelper(db);
		chapterHelper = new ChapterHelper(db);
		questionHelper = new QuestionHelper(db);
		bookHelper.onCreate();
		chapterHelper.onCreate();
		questionHelper.onCreate();
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
	
	/**
	 * @return the writable SQLiteDatabase.
	 */
	public SQLiteDatabase getDatabase() {
		return database;
	}
}
