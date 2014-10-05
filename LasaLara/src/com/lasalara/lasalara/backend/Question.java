package com.lasalara.lasalara.backend;

/**
 * Class responsible for holding a question's information
 * @author Ants-Oskar Mäesalu
 */
public class Question {
	String question;		// Text of the question
	String answer;			// Text of the answer
	//int position;			// The position of the question in the book (the order is set by the book owner but students can change the order locally)
	// int knownCount;		// The number of times that the student has marked the question as “known”
	// Date knownDate;		// The question should be hidden from the student until this date is reached
	
	/**
	 * Constructor, used when downloading all of the questions in a chapter.
	 * @param question	The question string.
	 * @param answer	The question's answer string.
	 */
	Question(String question, String answer) {
		this.question = question;
		this.answer = answer;
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
}
