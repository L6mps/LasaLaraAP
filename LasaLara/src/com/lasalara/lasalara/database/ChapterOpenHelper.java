package com.lasalara.lasalara.database;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.backend.Book;
import com.lasalara.lasalara.backend.Chapter;
import com.lasalara.lasalara.constants.NumericalConstants;
import com.lasalara.lasalara.constants.StringConstants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChapterOpenHelper extends SQLiteOpenHelper {
	
	// Basic database queries
	private static final String TABLE_CREATE =
			"CREATE TABLE " + 
			StringConstants.CHAPTER_TABLE_NAME + " (" +
			StringConstants.CHAPTER_COLUMN_KEY + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_TITLE + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_VERSION + " INT, " +
			StringConstants.CHAPTER_COLUMN_AUTHOR_EMAIL + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_AUTHOR_NAME + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_AUTHOR_INSTITUTION + " TEXT), " +
			StringConstants.CHAPTER_COLUMN_PROPOSALS_ALLOWED + " INT), " +
			StringConstants.CHAPTER_COLUMN_BOOK_KEY + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.CHAPTER_TABLE_NAME;

    ChapterOpenHelper(Context context) {
        super(context, StringConstants.DATABASE_NAME, null, NumericalConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: Create a better method of upgrading the database.
		// Currently, the most straigthforward way to upgrade the database is to drop the
		// previous database, create a new one and then repopulate it.
		db.execSQL(TABLE_DROP);
		onCreate(db);
	}
	
	/**
	 * Insert a new chapter into the SQLite database.
	 * @param chapter	The chapter object's instance.
	 */
	public void insertChapter(Chapter chapter) {
		// TODO: Check existence - update if exists?
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.CHAPTER_COLUMN_KEY, chapter.getKey());
		contentValues.put(StringConstants.CHAPTER_COLUMN_TITLE, chapter.getTitle());
		contentValues.put(StringConstants.CHAPTER_COLUMN_VERSION, chapter.getVersion());
		contentValues.put(StringConstants.CHAPTER_COLUMN_AUTHOR_EMAIL, chapter.getAuthorEmail());
		contentValues.put(StringConstants.CHAPTER_COLUMN_AUTHOR_NAME, chapter.getAuthorName());
		contentValues.put(StringConstants.CHAPTER_COLUMN_AUTHOR_INSTITUTION, chapter.getAuthorInstitution());
		contentValues.put(StringConstants.CHAPTER_COLUMN_PROPOSALS_ALLOWED, chapter.areProposalsAllowed() ? 1 : 0);
		contentValues.put(StringConstants.CHAPTER_COLUMN_BOOK_KEY, chapter.getBookKey());
		db.insert(StringConstants.CHAPTER_TABLE_NAME, null, contentValues);
	}
	
	/**
	 * @param bookKey	The UUID of a book.
	 * @return a list of chapters in a certain book saved to the SQLite database.
	 */
	public List<Chapter> getChapters(String bookKey) {
		List<Chapter> chapterList = new ArrayList<Chapter>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectChaptersQuery =
				"SELECT * FROM " + StringConstants.CHAPTER_TABLE_NAME +  
				" WHERE " + StringConstants.CHAPTER_COLUMN_BOOK_KEY + 
				"=" + bookKey;
		Cursor results =  db.rawQuery(selectChaptersQuery, null);
		results.moveToFirst();
		while (!results.isAfterLast()) {
			chapterList.add(new Chapter(results));
			results.moveToNext();
		}
		return chapterList;
	}
}