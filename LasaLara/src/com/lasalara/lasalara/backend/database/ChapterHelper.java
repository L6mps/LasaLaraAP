package com.lasalara.lasalara.backend.database;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Class that handles all of the SQLite database operations on chapters.
 * @author Ants-Oskar Mäesalu
 */
public class ChapterHelper {
	// SQLite database class
	private SQLiteDatabase database;
	// Basic database queries
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + 
			StringConstants.CHAPTER_TABLE_NAME + " (" +
			StringConstants.CHAPTER_COLUMN_KEY + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_TITLE + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_VERSION + " INT, " +
			StringConstants.CHAPTER_COLUMN_AUTHOR_EMAIL + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_AUTHOR_NAME + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_AUTHOR_INSTITUTION + " TEXT, " +
			StringConstants.CHAPTER_COLUMN_PROPOSALS_ALLOWED + " INT, " +
			StringConstants.CHAPTER_COLUMN_POSITION + " INT, " +
			StringConstants.CHAPTER_COLUMN_BOOK_KEY + " TEXT);";
	private static final String TABLE_DROP = 
			"DROP TABLE IF EXISTS " + StringConstants.CHAPTER_TABLE_NAME;

	/**
	 * Constructor.
	 * @param database		The SQLite database class.
	 */
    ChapterHelper(SQLiteDatabase database) {
        this.database = database;
		Log.d(StringConstants.APP_NAME, "ChapterHelper constructor.");
    }

    /**
     * Actions conducted on database creation.
     */
    public void onCreate() {
    	Log.d(StringConstants.APP_NAME, "ChapterHelper onCreate()");
    	Log.d(StringConstants.APP_NAME, TABLE_CREATE);
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
	
	private ContentValues getContentValues(Chapter chapter) {
		// TODO
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Insert a new chapter into the SQLite database.
	 * @param chapter	The chapter object's instance.
	 */
	public void insertChapter(Chapter chapter) {
		// TODO: Check existence - update if exists?
		ContentValues contentValues = new ContentValues();
		contentValues.put(StringConstants.CHAPTER_COLUMN_KEY, chapter.getKey());
		contentValues.put(StringConstants.CHAPTER_COLUMN_TITLE, chapter.getTitle());
		contentValues.put(StringConstants.CHAPTER_COLUMN_VERSION, chapter.getVersion());
		contentValues.put(StringConstants.CHAPTER_COLUMN_AUTHOR_EMAIL, chapter.getAuthorEmail());
		contentValues.put(StringConstants.CHAPTER_COLUMN_AUTHOR_NAME, chapter.getAuthorName());
		contentValues.put(StringConstants.CHAPTER_COLUMN_AUTHOR_INSTITUTION, chapter.getAuthorInstitution());
		contentValues.put(StringConstants.CHAPTER_COLUMN_PROPOSALS_ALLOWED, chapter.areProposalsAllowed() ? 1 : 0);
		contentValues.put(StringConstants.CHAPTER_COLUMN_POSITION, chapter.getPosition());
		contentValues.put(StringConstants.CHAPTER_COLUMN_BOOK_KEY, chapter.getBookKey());
		database.insert(StringConstants.CHAPTER_TABLE_NAME, null, contentValues);
	}
	
	public void updateChapter(Chapter chapter) {
		// TODO
	}
	
	/**
	 * Delete a chapter from the SQLite database.
	 * Also deletes all of the questions associated with the deleted chapter.
	 * @param chapter	The chapter object's instance.
	 */
	public void deleteChapter(Chapter chapter) {
		DatabaseHelper.getInstance().getQuestionHelper().deleteQuestions(chapter);
		String whereClause = StringConstants.CHAPTER_COLUMN_KEY + "='" + chapter.getKey() + "'" +
				StringConstants.CHAPTER_COLUMN_BOOK_KEY + "='" + chapter.getBookKey() + "'";
		database.delete(StringConstants.CHAPTER_TABLE_NAME, whereClause, null);
	}
	
	/**
	 * Delete all of the chapters associated with a certain book from the SQLite database.
	 * Also deletes all of the questions associated with the deleted chapters.
	 * @param book		The book object's instance.
	 */
	public void deleteChapters(Book book) {
		for (Chapter chapter: book.getChapters()) {
			DatabaseHelper.getInstance().getQuestionHelper().deleteQuestions(chapter);
		}
		String whereClause = StringConstants.CHAPTER_COLUMN_BOOK_KEY + "='" + book.getKey() + "'";
		database.delete(StringConstants.CHAPTER_TABLE_NAME, whereClause, null);
	}
	
	/**
	 * @param bookKey	The UUID of a book.
	 * @return a list of chapters in a certain book saved into the SQLite database.
	 */
	public List<Chapter> getChapters(String bookKey) {
		Log.d(StringConstants.APP_NAME, "ChapterHelper getChapters: " + bookKey);
		List<Chapter> chapterList = new ArrayList<Chapter>();
		String selectChaptersQuery =
				"SELECT * FROM " + StringConstants.CHAPTER_TABLE_NAME +  
				" WHERE " + StringConstants.CHAPTER_COLUMN_BOOK_KEY + "='" + bookKey + "'" +
				" ORDER BY " + StringConstants.CHAPTER_COLUMN_POSITION + " ASC";
		Log.d(StringConstants.APP_NAME, selectChaptersQuery);
		Cursor results =  database.rawQuery(selectChaptersQuery, null);
		boolean moveSucceeded = results.moveToFirst();
		while (moveSucceeded) {
			chapterList.add(new Chapter(results));
			moveSucceeded = results.moveToNext();
		}
		return chapterList;
	}
}