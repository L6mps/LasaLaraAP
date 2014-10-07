package com.lasalara.lasalara.database;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.backend.Book;
import com.lasalara.lasalara.backend.Chapter;
import com.lasalara.lasalara.constants.StringConstants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookOpenHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "LasaLaraAP.db";;
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_CREATE =
			"CREATE TABLE " + 
			StringConstants.BOOK_TABLE_NAME + " (" +
			StringConstants.BOOK_COLUMN_KEY + " TEXT, " +
			StringConstants.BOOK_COLUMN_TITLE + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_EMAIL + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_NAME + " TEXT, " +
			StringConstants.BOOK_COLUMN_OWNER_INSTITUTION + " TEXT, " +
			StringConstants.BOOK_COLUMN_LAST_CHAPTER + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.BOOK_TABLE_NAME;

    BookOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: Create a better method of upgrading the database
		// Currently, the most straigthforward way to upgrade the database is to drop the
		// previous database, create a new one and then repopulate it.
		db.execSQL(TABLE_DROP);
		onCreate(db);
	}
	
	public boolean insertBook(Book book) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.BOOK_COLUMN_KEY, book.getKey());
		contentValues.put(StringConstants.BOOK_COLUMN_TITLE, book.getTitle());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_EMAIL, book.getOwnerEmail());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_NAME, book.getOwnerName());
		contentValues.put(StringConstants.BOOK_COLUMN_OWNER_INSTITUTION, book.getOwnerInstitution());
		contentValues.put(StringConstants.BOOK_COLUMN_LAST_CHAPTER, book.getLastChapter());
		db.insert(StringConstants.BOOK_TABLE_NAME, null, contentValues);
		return true; // TODO: Why do we need this?
	}
	
	public List<Book> getBooks(String bookKey) {
		List<Book> bookList = new ArrayList<Book>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectBooksQuery = "SELECT * FROM " + StringConstants.BOOK_TABLE_NAME + 
				" WHERE " + StringConstants.BOOK_COLUMN_KEY + "=" + bookKey;
		Cursor results =  db.rawQuery(selectBooksQuery, null);
		results.moveToFirst();
		while (!results.isAfterLast()) {
			bookList.add(new Book(results));
			results.moveToNext();
		}
		return bookList;
	}
	
	public List<Chapter> getChapters(String bookKey) {
		List<Chapter> chapterList = new ArrayList<Chapter>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectChaptersQuery =
				"SELECT * FROM " + StringConstants.CHAPTER_TABLE_NAME + 
				" INNER JOIN " + StringConstants.CHAPTER_CONNECTION_TABLE_NAME + 
				" ON " + StringConstants.CHAPTER_CONNECTION_TABLE_NAME + 
				"." + StringConstants.CHAPTER_CONNECTION_COLUMN_CHAPTER + 
				"=" + StringConstants.CHAPTER_TABLE_NAME + "." + StringConstants.CHAPTER_COLUMN_KEY + 
				" WHERE " + StringConstants.CHAPTER_CONNECTION_TABLE_NAME + 
				"." + StringConstants.CHAPTER_CONNECTION_COLUMN_BOOK + 
				"=" + bookKey;
		Cursor results =  db.rawQuery(selectChaptersQuery, null);
		results.moveToFirst();
		while (!results.isAfterLast()) {
			// TODO: chapterList.add(new Chapter(results));
			results.moveToNext();
		}
		return chapterList;
	}
}