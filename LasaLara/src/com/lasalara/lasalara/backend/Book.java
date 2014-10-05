package com.lasalara.lasalara.backend;

/**
 * 
 * @author Ants-Oskar Mäesalu
 */
public class Book {
	String key;				// The book's UUID
	String title;			// Name of the book
	String ownerEmail;		// E-mail of the person who created the book
	String ownerName;		// Name and institution of the person who created the book (if blank, the e-mail is used)
	String lastChapter;		// Last chapter (UUID) of this book opened by the student
	
	Book(String key) {
		this.key = key;
		// TODO: Load data
	}
	
	// TODO: Generate getters and setters
}
