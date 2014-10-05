package com.lasalara.lasalara.backend;

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
	boolean allowProposals;		// Has the author allowed question proposals for the chapter?
	//int position;				// The position of the chapter in the book (the order is set by the book owner)

	/**
	 * Constructor, used when downloading a chapter from the web.
	 * @param context			The current activity's context (needed for network connection check).
	 * @param key				The chapter's UUID key.
	 * @param title				The chapter's title.
	 * @param version			The chapter's version. Version numbers let the app know when to re-download chapter questions.
	 * @param authorEmail		The chapter's author's e-mail address.
	 * @param authorName		The chapter's author's name (or left null if blank).
	 * @param authorInstitution	The chapter's author's institution (or left null if blank).
	 * @param allowProposals	Boolean value, whether the author allows question proposals or not.
	 */
	Chapter(String key, String title, int version, String authorEmail, String authorName, 
			String authorInstitution, boolean allowProposals) {
		this.key = key;
		this.title = title;
		this.version = version;
		this.authorEmail = authorEmail;
		this.authorName = authorName;
		this.authorInstitution = authorInstitution;
		this.allowProposals = allowProposals;
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
	public boolean isAllowProposals() {
		return allowProposals;
	}
}