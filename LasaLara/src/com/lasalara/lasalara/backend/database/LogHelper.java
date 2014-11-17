package com.lasalara.lasalara.backend.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Message;

/**
 * Class that handles all of the SQLite database operations on the application log.
 * @author Ants-Oskar Mäesalu
 */
public class LogHelper {
	// SQLite database class
	private SQLiteDatabase database;
	// Basic database queries
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + 
			StringConstants.LOG_TABLE_NAME + " (" +
			StringConstants.LOG_COLUMN_EVENT + " TEXT, " +
			StringConstants.LOG_COLUMN_TIME + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.LOG_TABLE_NAME;
	
	/**
	 * Constructor.
	 * @param database		The SQLite database class.
	 */
    LogHelper(SQLiteDatabase database) {
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
	 * Insert a new log event into the SQLite database.
	 * @param logEvent	The log event object's instance.
	 */
	public void insertEvent(Message logEvent) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.LOG_COLUMN_EVENT, logEvent.getMessage());
		contentValues.put(StringConstants.LOG_COLUMN_TIME, logEvent.getTime().toString()); // TODO: Test if string conversion is the correct way to handle this
		database.insert(StringConstants.LOG_TABLE_NAME, null, contentValues);
	}
	
	/**
	 * Delete all of the log events from the SQLite database.
	 */
	public void deleteLog() {
		for (Book book: Backend.getInstance().getBooks()) {
			DatabaseHelper.getInstance().getChapterHelper().deleteChapters(book);
		}
		database.delete(StringConstants.LOG_TABLE_NAME, null, null);
	}
}
