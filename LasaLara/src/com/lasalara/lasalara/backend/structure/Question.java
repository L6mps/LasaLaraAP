package com.lasalara.lasalara.backend.structure;

import java.sql.Timestamp;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;

/**
 * Class responsible for holding a question's information
 * @author Ants-Oskar Mäesalu
 */
public class Question {
	private String question;				// Text of the question
	private String answer;					// Text of the answer
	private int reviewCount;				// The number of times the student has skipped the question.
	private int knownCount;					// The number of times the student has set the question as known.
	private Timestamp reviewTime;			// The last time the student skipped the question.
	private Timestamp knownTime;			// The last time the student set the question as known.
	private String chapterKey;				// The chapter the question is located in.
	
	/**
	 * Constructor, used when downloading all of the questions in a chapter.
	 * @param context	The current activity's context (needed for network connection check and SQLite database).
	 * @param question	The question string.
	 * @param answer	The question's answer string.
	 * @param bookKey	The book the chapter is located in.
	 */
	Question(Context context, String question, String answer, String chapterKey) {
		Log.d(StringConstants.APP_NAME, "Question constructor: " + chapterKey + ", " + question + ", " + answer + ".");
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
		this.question = question;
		this.answer = answer;
		reviewCount = 0;
		knownCount = 0;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		reviewTime = currentTime;
		knownTime = currentTime;
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
		reviewCount = dbResults.getInt(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_REVIEW_COUNT));
		knownCount = dbResults.getInt(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_KNOWN_COUNT));
		reviewTime = Timestamp.valueOf(dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_REVIEW_TIME)));
		knownTime = Timestamp.valueOf(dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_KNOWN_TIME)));
		chapterKey = dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_CHAPTER_KEY));
	}
	
	/**
	 * Set the question unknown. Used when the user skips the question.
	 */
	public void setUnknown() {
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		long secondsSinceReview = currentTime.getTime() - reviewTime.getTime();
		reviewCount++;
		// TODO
	}
	
	/**
	 * Set the question known. Used when the user says he/she knows the answer to the question.
	 */
	public void setKnown() {
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		long secondsSinceReview = currentTime.getTime() - knownTime.getTime();
		knownCount++;
		// TODO
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
	 * @return the number of times the student has skipped the question.
	 */
	public int getReviewCount() {
		return reviewCount;
	}

	/**
	 * @return the number of times the student has set the question as known.
	 */
	public int getKnownCount() {
		return knownCount;
	}

	/**
	 * @return the last time the student skipped the question.
	 */
	public Timestamp getReviewTime() {
		return reviewTime;
	}

	/**
	 * @return the last time the student set the question as known.
	 */
	public Timestamp getKnownTime() {
		return knownTime;
	}

	/**
	 * @return the chapter's key the question is located in.
	 */
	public String getChapterKey() {
		return chapterKey;
	}
}
