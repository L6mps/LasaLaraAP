package com.lasalara.lasalara.backend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Class responsible for holding a chapter's information and querying it's questions' information.
 * @author Ants-Oskar Mäesalu
 */
public class Chapter {
	String key;					// The chapter's UUID, TODO: Own class for UUID?
	String title;				// Name of the chapter
	int version;				// If the author updates a chapter, its version number is incremented. Version numbers let the app know when to re-download chapter questions.
	String authorEmail;			// E-mail address of the person who wrote the chapter, TODO: Own class for e-mail?
	String authorName;			// Name aof the person who wrote the chapter (if blank, the e-mail is used)
	String authorInstitution;	// Institution of the person who wrote the chapter (if blank, the e-mail is used)
	boolean proposalsAllowed;	// Has the author allowed question proposals for the chapter?
	//int position;				// The position of the chapter in the book (the order is set by the book owner), TODO: How?
	List<Question> questions;	// The list of questions in this book.

	/**
	 * Constructor, used when downloading a chapter from the web.
	 * @param key				The chapter's UUID key.
	 * @param title				The chapter's title.
	 * @param version			The chapter's version. Version numbers let the app know when to re-download chapter questions.
	 * @param authorEmail		The chapter's author's e-mail address.
	 * @param authorName		The chapter's author's name (or left null if blank).
	 * @param authorInstitution	The chapter's author's institution (or left null if blank).
	 * @param proposalsAllowed	Boolean value, whether the author allows question proposals or not.
	 */
	Chapter(String key, String title, int version, String authorEmail, String authorName, 
			String authorInstitution, boolean proposalsAllowed) {
		this.key = key;
		this.title = title;
		this.version = version;
		this.authorEmail = authorEmail;
		this.authorName = authorName;
		this.authorInstitution = authorInstitution;
		this.proposalsAllowed = proposalsAllowed;
	}
	
	/**
	 * Load the questions in this book.
	 * @param context			The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	public void loadQuestions(Context context) throws IOException, JSONException {
		questions = new ArrayList<Question>();
		String url = "http://www.lasalara.com/getquestions";
		String urlParameters = "ck=" + URLEncoder.encode(key, "UTF-8");
		WebRequest request = new WebRequest(context, url, urlParameters);
		try {
			JSONObject result = request.getJSONObject();
			System.out.println(result);
			JSONObject questionList = result.getJSONObject("questions");
			JSONObject answerList = result.getJSONObject("answers");
			if (questionList.length() == answerList.length()) {
				for (int i = 0; i < questionList.length(); i++) {
					String question = questionList.getJSONObject(Integer.toString(i)).toString();
					String answer = answerList.getJSONObject(Integer.toString(i)).toString();
					questions.add(new Question(question, answer));
				}
			} else {
				throw new RuntimeException();
				// TODO: Parse error
			}
		} catch (JSONException e) {
			throw new JSONException(e.toString());
		}
	}
	
	/**
	 * Reload the question list.
	 * TODO: Currently never used but should be used to update the content when the chapter version number is updated.
	 * @param context			The current activity's context (needed for network connection check).
	 * @throws IOException
	 * @throws JSONException
	 */
	public void reloadQuestions(Context context) throws IOException, JSONException {
		loadQuestions(context);
	}

	/**
	 * @return the chapter's UUID key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the chapter's title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the the chapter's version number.
	 * Version numbers let the app know when to re-download chapter questions.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the the chapter's author's e-mail address.
	 */
	public String getAuthorEmail() {
		return authorEmail;
	}

	/**
	 * @return the chapter's author's name (or left null if blank).
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @return the chapter's author's institution (or left null if blank).
	 */
	public String getAuthorInstitution() {
		return authorInstitution;
	}

	/**
	 * @return a boolean value, whether the author allows question proposals or not.
	 */
	public boolean areProposalsAllowed() {
		return proposalsAllowed;
	}

	/**
	 * @param context			The current activity's context (needed for network connection check).
	 * @return the list of questions in this chapter.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public List<Question> getQuestions(Context context) throws IOException, JSONException {
		loadQuestions(context);
		return questions;
	}
}