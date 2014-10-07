package com.lasalara.lasalara.database;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.backend.Book;
import com.lasalara.lasalara.backend.Chapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookOpenHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "LasaLaraAP.db";;
	private static final int DATABASE_VERSION = 1;

	// TODO: Move to constants enumerator
	private static final String TABLE_NAME = "book";
	private static final String COLUMN_KEY = "key";
	private static final String COLUMN_TITLE = "title";
	private static final String COLUMN_OWNER_EMAIL = "ownerEmail";
	private static final String COLUMN_OWNER_NAME = "ownerName";
	private static final String COLUMN_OWNER_INSTITUTION = "ownerInstitution";
	private static final String COLUMN_LAST_CHAPTER = "lastChapter";
	
	private static final String CHAPTERS_CONNECTION_TABLE_NAME = "chapterInBook";
	private static final String CHAPTERS_CONNECTION_COLUMN_BOOK = "bookKey";
	private static final String CHAPTERS_CONNECTION_COLUMN_CHAPTER = "chapterKey";
	
	private static final String CHAPTERS_TABLE_NAME = "chapters";
	private static final String CHAPTERS_COLUMN_KEY = "key";
	
	private static final String TABLE_CREATE =
			"CREATE TABLE " + 
			TABLE_NAME + " (" +
			COLUMN_KEY + " TEXT, " +
			COLUMN_TITLE + " TEXT, " +
			COLUMN_OWNER_EMAIL + " TEXT, " +
			COLUMN_OWNER_NAME + " TEXT, " +
			COLUMN_OWNER_INSTITUTION + " TEXT, " +
			COLUMN_LAST_CHAPTER + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + TABLE_NAME;

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
		contentValues.put(COLUMN_KEY, book.getKey());
		contentValues.put(COLUMN_TITLE, book.getTitle());
		contentValues.put(COLUMN_OWNER_EMAIL, book.getOwnerEmail());
		contentValues.put(COLUMN_OWNER_NAME, book.getOwnerName());
		contentValues.put(COLUMN_OWNER_INSTITUTION, book.getOwnerInstitution());
		contentValues.put(COLUMN_LAST_CHAPTER, book.getLastChapter());
		db.insert(TABLE_NAME, null, contentValues);
		return true; // TODO: Why do we need this?
	}
	
	public List<Book> getBooks(String bookKey) {
		List<Book> bookList = new ArrayList<Book>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor results =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_KEY + "=" + bookKey, null);
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
		Cursor results =  db.rawQuery("SELECT * FROM " + CHAPTERS_TABLE_NAME + " INNER JOIN " + CHAPTERS_CONNECTION_TABLE_NAME + " ON " + CHAPTERS_CONNECTION_TABLE_NAME + "." + CHAPTERS_CONNECTION_COLUMN_CHAPTER + "=" + CHAPTERS_TABLE_NAME + "." + CHAPTERS_COLUMN_KEY + " WHERE " + CHAPTERS_CONNECTION_TABLE_NAME + "." + CHAPTERS_CONNECTION_COLUMN_BOOK + "=" + COLUMN_KEY, null);
		results.moveToFirst();
		while (!results.isAfterLast()) {
			// TODO: chapterList.add(new Chapter(results));
			results.moveToNext();
		}
		return chapterList;
	}
}