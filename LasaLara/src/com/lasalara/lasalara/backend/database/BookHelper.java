package com.lasalara.lasalara.backend.database;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.structure.Book;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class that handles all of the SQLite database operations on books.
 * @author Ants-Oskar Mäesalu
 */
public class BookHelper {
	// SQLite database class
	private SQLiteDatabase database;
	// Basic database queries
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + 
			StringConstants.BOOK_TABLE_NAME + " (" +
			StringConstants.BOOK_COLUMN_KEY + " TEXT, " +
			StringConstants.BOOK_COLUMN_TITLE + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_EMAIL + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_NAME + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_INSTITUTION + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.CHAPTER_TABLE_NAME;

	/**
	 * Constructor.
	 * @param database		The SQLite database class.
	 */
	BookHelper(SQLiteDatabase database) {
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
	 * Insert a new book into the SQLite database.
	 * @param book		The book object's instance.
	 */
	public void insertBook(Book book) {
		//Log.d(StringConstants.APP_NAME, "BookHelper: insertBook \"" + book.getTitle() + "\"");
		// TODO: Check existence - update if exists?
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.BOOK_COLUMN_KEY, book.getKey());
		contentValues.put(StringConstants.BOOK_COLUMN_TITLE, book.getTitle());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_EMAIL, book.getOwnerEmail());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_NAME, book.getOwnerName());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_INSTITUTION, book.getOwnerInstitution());
		database.insert(StringConstants.BOOK_TABLE_NAME, null, contentValues);
	}
	
	/**
	 * Delete a book from the SQLite database.
	 * Also deletes all of the chapters (and their corresponding questions) associated with the deleted book.
	 * @param book		The book object's instance.
	 */
	public void deleteBook(Book book) {
		DatabaseHelper.getInstance().getChapterHelper().deleteChapters(book);
		String whereClause = StringConstants.BOOK_COLUMN_KEY + "='" + book.getKey() + "'";
		database.delete(StringConstants.BOOK_TABLE_NAME, whereClause, null);
	}
	
	/**
	 * Delete all of the books from the SQLite database.
	 * Also deletes all of the chapters (and their corresponding questions) associated with the deleted books.
	 */
	public void deleteBooks() {
		for (Book book: Backend.getInstance().getBooks()) {
			DatabaseHelper.getInstance().getChapterHelper().deleteChapters(book);
		}
		database.delete(StringConstants.BOOK_TABLE_NAME, null, null);
	}
	
	/**
	 * @return a list of books saved into the SQLite database.
	 */
	public List<Book> getBooks() {
		List<Book> bookList = new ArrayList<Book>();
		String selectBooksQuery = "SELECT * FROM " + StringConstants.BOOK_TABLE_NAME;
		Cursor results =  database.rawQuery(selectBooksQuery, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			bookList.add(new Book(results));
			moveSucceeded = results.moveToNext();
		}
		return bookList;
	}
	
	// TODO: getBook(String bookKey) - do we need this?
}