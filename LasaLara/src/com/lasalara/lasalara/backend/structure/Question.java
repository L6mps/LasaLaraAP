package com.lasalara.lasalara.backend.structure;

import java.sql.Timestamp;
import java.util.Calendar;

import android.database.Cursor;

import com.lasalara.lasalara.backend.constants.NumericalConstants;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;

/**
 * Class responsible for holding a question's information
 * @author Ants-Oskar Mäesalu
 */
public class Question {
	private String question;				// Text of the question
	private String answer;					// Text of the answer
	private int reviewCount;				// The number of times the student has reviewed the question.
	private int knownCount;					// The number of times the student has set the question as known.
	private Timestamp reviewTime;			// The last time the student reviewed the question.
	private Timestamp knownUntilTime;		// The time until which the question remains until.
	private String chapterKey;				// The chapter the question is located in.
	
	/**
	 * Constructor, used when downloading all of the questions in a chapter.
	 * @param question	The question string.
	 * @param answer	The question's answer string.
	 * @param bookKey	The book the chapter is located in.
	 */
	Question(String question, String answer, String chapterKey) {
		this.question = question;
		this.answer = answer;
		reviewCount = 0;
		knownCount = 0;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		reviewTime = currentTime;
		knownUntilTime = currentTime;
		this.chapterKey = chapterKey;
		DatabaseHelper.getInstance().getQuestionHelper().insertQuestion(this);
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
		knownUntilTime = Timestamp.valueOf(dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_KNOWN_UNTIL_TIME)));
		chapterKey = dbResults.getString(dbResults.getColumnIndex(StringConstants.QUESTION_COLUMN_CHAPTER_KEY));
	}
	
	/**
	 * Update this question's database row.
	 */
	private void updateInDatabase() {
		DatabaseHelper.getInstance().getQuestionHelper().updateQuestion(this);
	}
	
	/**
	 * Delete this question from the database.
	 */
	private void deleteFromDatabase() {
		DatabaseHelper.getInstance().getQuestionHelper().deleteQuestion(this);
	}
	
	/**
	 * Delete this question from the application.
	 */
	public void delete() {
		deleteFromDatabase();
	}
	
	/**
	 * Set the question unknown. Used when the user skips the question.
	 */
	public void setUnknown() {
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		long secondsToAdd = NumericalConstants.TWENTYFIVESECONDS;
		if (reviewCount > 0) {
			secondsToAdd += (knownUntilTime.getTime() - reviewTime.getTime()) * knownCount * 1.0 / reviewCount;
		}
		reviewCount++;
		reviewTime = currentTime;
		knownUntilTime = new Timestamp(currentTime.getTime() + secondsToAdd);
		updateInDatabase();
	}
	
	/**
	 * Set the question known. Used when the user says he/she knows the answer to the question.
	 */
	public void setKnown() {
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		long secondsSinceReview = currentTime.getTime() - reviewTime.getTime();
		long secondsToAdd = NumericalConstants.TWENTYFIVESECONDS;
		if (reviewCount == 0) {
			secondsToAdd = NumericalConstants.ONEDAY;
		} else if (secondsSinceReview > NumericalConstants.TWOYEARS) {
			secondsToAdd = NumericalConstants.TENYEARS;
		} else if (secondsSinceReview > NumericalConstants.HUNDREDTWENTYFIVEDAYS) {
			secondsToAdd = NumericalConstants.TWOYEARS;
		} else if (secondsSinceReview > NumericalConstants.TWENTYFIVEDAYS) {
			secondsToAdd = NumericalConstants.HUNDREDTWENTYFIVEDAYS;
		} else if (secondsSinceReview > NumericalConstants.FIVEDAYS) {
			secondsToAdd = NumericalConstants.TWENTYFIVEDAYS;
		} else if (secondsSinceReview > NumericalConstants.ONEDAY) {
			secondsToAdd = NumericalConstants.FIVEDAYS;
		} else if (secondsSinceReview > NumericalConstants.FIVEHOURS) {
			secondsToAdd = NumericalConstants.ONEDAY;
		} else if (secondsSinceReview > NumericalConstants.ONEHOUR) {
			secondsToAdd = NumericalConstants.FIVEHOURS;
		} else if (secondsSinceReview > NumericalConstants.TENMINUTES) {
			secondsToAdd = NumericalConstants.ONEHOUR;
		} else if (secondsSinceReview > NumericalConstants.TWOMINUTES) {
			secondsToAdd = NumericalConstants.TENMINUTES;
		} else {
			secondsToAdd = NumericalConstants.TWOMINUTES;
		}
		reviewCount++;
		reviewTime = currentTime;
		knownCount++;
		knownUntilTime = new Timestamp(currentTime.getTime() + secondsToAdd);
		updateInDatabase();
	}
	
	/**
	 * Reset the question's progress.
	 */
	public void resetProgress() {
		reviewCount = 0;
		knownCount = 0;
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
		reviewTime = currentTime;
		knownUntilTime = currentTime;
		updateInDatabase();
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
	 * @return the number of times the student has reviewed the question.
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
	 * @return the last time the student reviewed the question.
	 */
	public Timestamp getReviewTime() {
		return reviewTime;
	}

	/**
	 * @return the time until which the question remains hidden.
	 */
	public Timestamp getKnownUntilTime() {
		return knownUntilTime;
	}

	/**
	 * @return the chapter's key the question is located in.
	 */
	public String getChapterKey() {
		return chapterKey;
	}
}
