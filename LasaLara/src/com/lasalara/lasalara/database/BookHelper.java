package com.lasalara.lasalara.database;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.backend.Book;
import com.lasalara.lasalara.constants.StringConstants;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class that handles all of the SQLite database operations on books.
 * @author Ants-Oskar Mäesalu
 */
public class BookHelper {
	// SQLite database helper class
	private DatabaseHelper databaseHelper;
	// Basic database queries
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + 
			StringConstants.CHAPTER_TABLE_NAME + " (" +
			StringConstants.CHAPTER_COLUMN_KEY + " TEXT, " +
			StringConstants.BOOK_COLUMN_TITLE + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_EMAIL + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_NAME + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_INSTITUTION + " TEXT, " +
			StringConstants.BOOK_COLUMN_LAST_CHAPTER + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.CHAPTER_TABLE_NAME;

	/**
	 * Constructor.
	 * @param databaseHelper	The SQLite database helper class.
	 */
    BookHelper(DatabaseHelper databaseHelper) {
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
	 * Insert a new book into the SQLite database.
	 * @param book		The book object's instance.
	 */
	public void insertBook(Book book) {
		// TODO: Check existence - update if exists?
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.BOOK_COLUMN_KEY, book.getKey());
		contentValues.put(StringConstants.BOOK_COLUMN_TITLE, book.getTitle());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_EMAIL, book.getOwnerEmail());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_NAME, book.getOwnerName());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_INSTITUTION, book.getOwnerInstitution());
		contentValues.put(StringConstants.BOOK_COLUMN_LAST_CHAPTER, book.getLastChapter());
		database.insert(StringConstants.BOOK_TABLE_NAME, null, contentValues);
	}
	
	/**
	 * @return a list of books saved into the SQLite database.
	 */
	public List<Book> getBooks() {
		List<Book> bookList = new ArrayList<Book>();
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		String selectBooksQuery = "SELECT * FROM " + StringConstants.BOOK_TABLE_NAME;
		Cursor results =  database.rawQuery(selectBooksQuery, null);
		results.moveToFirst();
		while (!results.isAfterLast()) {
			bookList.add(new Book(databaseHelper, results));
			results.moveToNext();
		}
		return bookList;
	}
	
	// TODO: getBook(String bookKey) - do we need this?
}