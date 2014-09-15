package com.lasalara.lasalara.backend;

/**
 * 
 * @author Ants-Oskar Mäesalu
 */
public class Chapter {
	String key;					// The chapter's UUID, TODO: Own class for UUID?
	String title;				// Name of the chapter
	String authorEmail;			// E-mail address of the person who wrote the chapter, TODO: Own class for e-mail?
	String authorName;			// Name and institution of the person who wrote the chapter (if these are blank, use e-mail)
	int position;				// The position of the chapter in the book (the order is set by the book owner)
	int version;				// If the author updates a chapter, its version number is incremented. Version numbers let the app know when to re-download chapter questions.
	boolean allowPropositions;	// Has the author allowed question proposals for the chapter?

	Chapter(String key) {
		this.key = key;
		// TODO: Load data
	}
	
	// TODO: Generate getters and setters
}
