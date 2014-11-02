package com.lasalara.lasalara.backend.constants;

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
	// Database information
	public static final String DATABASE_NAME = "LasaLaraAP.db";
	// Database book table constants
	public static final String BOOK_TABLE_NAME = "book";
	public static final String BOOK_COLUMN_KEY = "key";
	public static final String BOOK_COLUMN_TITLE = "title";
	public static final String BOOK_COLUMN_OWNER_EMAIL = "ownerEmail";
	public static final String BOOK_COLUMN_OWNER_NAME = "ownerName";
	public static final String BOOK_COLUMN_OWNER_INSTITUTION = "ownerInstitution";
	// Database chapter table constants
	public static final String CHAPTER_TABLE_NAME = "chapter";
	public static final String CHAPTER_COLUMN_KEY = "key";
	public static final String CHAPTER_COLUMN_TITLE = "title";
	public static final String CHAPTER_COLUMN_VERSION = "version";
	public static final String CHAPTER_COLUMN_AUTHOR_EMAIL = "authorEmail";
	public static final String CHAPTER_COLUMN_AUTHOR_NAME = "authorName";
	public static final String CHAPTER_COLUMN_AUTHOR_INSTITUTION = "authorInstitution";
	public static final String CHAPTER_COLUMN_PROPOSALS_ALLOWED = "proposalsAllowed";
	public static final String CHAPTER_COLUMN_POSITION = "position";
	public static final String CHAPTER_COLUMN_BOOK_KEY = "bookKey";
	// Database question table constants
	public static final String QUESTION_TABLE_NAME = "question";
	public static final String QUESTION_COLUMN_QUESTION = "question";
	public static final String QUESTION_COLUMN_ANSWER = "answer";
	public static final String QUESTION_COLUMN_CHAPTER_KEY = "chapterKey";
}
