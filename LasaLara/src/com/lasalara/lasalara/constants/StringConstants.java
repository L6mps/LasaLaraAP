package com.lasalara.lasalara.constants;

/**
 * Class for all of the string constants in the application.
 * @author Ants-Oskar Mäesalu
 */
public class StringConstants {
	// General constants
	public static final String APP_NAME = "LasaLara";
	// Web request URL constants
	public static final String URL_GET_BOOK = "http://www.lasalara.com/getbook";
	public static final String URL_GET_CHAPTERS = "http://www.lasalara.com/getmchapters";
	public static final String URL_GET_QUESTIONS = "http://www.lasalara.com/getquestions";
	public static final String URL_POSE_QUESTION = "http://www.lasalara.com/posequestion";
	// Database book table constants
	public static final String BOOK_TABLE_NAME = "book";
	public static final String BOOK_COLUMN_KEY = "key";
	public static final String BOOK_COLUMN_TITLE = "title";
	public static final String BOOK_COLUMN_OWNER_EMAIL = "ownerEmail";
	public static final String BOOK_COLUMN_OWNER_NAME = "ownerName";
	public static final String BOOK_COLUMN_OWNER_INSTITUTION = "ownerInstitution";
	public static final String BOOK_COLUMN_LAST_CHAPTER = "lastChapter";
	// Database chapter connection table constants
	public static final String CHAPTER_CONNECTION_TABLE_NAME = "chapterInBook";
	public static final String CHAPTER_CONNECTION_COLUMN_BOOK = "bookKey";
	public static final String CHAPTER_CONNECTION_COLUMN_CHAPTER = "chapterKey";
	// Database chapter table constants
	public static final String CHAPTER_TABLE_NAME = "chapter";
	public static final String CHAPTER_COLUMN_KEY = "key";
}
