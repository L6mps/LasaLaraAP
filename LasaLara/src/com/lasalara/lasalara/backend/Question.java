package com.lasalara.lasalara.backend;

import android.content.Context;
import android.database.Cursor;

import com.lasalara.lasalara.constants.StringConstants;
import com.lasalara.lasalara.database.DatabaseHelper;

/**
 * Class responsible for holding a question's information
 * @author Ants-Oskar Mäesalu
 */
public class Question {
	private String question;				// Text of the question
	private String answer;					// Text of the answer
	// private int position;				// The position of the question in the book (the order is set by the book owner but students can change the order locally)
	// private int knownCount;				// The number of times that the student has marked the question as “known”
	// private Date knownDate;				// The question should be hidden from the student until this date is reached
	private String chapterKey;				// The chapter the question is located in.
	
	/**
	 * Constructor, used when downloading all of the questions in a chapter.
	 * @param context	The current activity's context (needed for network connection check and SQLite database).
	 * @param question	The question string.
	 * @param answer	The question's answer string.
	 * @param bookKey	The book the chapter is located in.
	 */
	Question(Context context, String question, String answer, String chapterKey) {
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		this.question = question;
		this.answer = answer;
		this.chapterKey = chapterKey;
		databaseHelper.getQuestionHelper().insertQuestion(this); // TODO: Test
	}
	
	/**
	 * Constructor, used when querying data from the internal SQLite database.
	 * @param dbResults			Database query results.
	 */
	public Question(Cursor dbResults) {
		question = dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_QUESTION));
		answer = dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_ANSWER));
		chapterKey = dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_CHAPTER_KEY));
	}

	/**
	 * @return the question string.
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @return the answer to the question as a string.
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @return the chapter's key the question is located in.
	 */
	public String getChapterKey() {
		return chapterKey;
	}
}
