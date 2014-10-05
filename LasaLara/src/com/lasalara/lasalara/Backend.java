package com.lasalara.lasalara;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;

import com.lasalara.lasalara.backend.Book;

/**
 * The main class of the back end of the application.
 * The whole back end is referred to through this class.
 * @author Ants-Oskar Mäesalu
 */
public class Backend {
	private static Backend instance;
	List<Book> books;
	
	/**
	 * Constructor.
	 * Private because this is a singleton.
	 */
	private Backend() {
		super();
		books = new ArrayList<Book>();
		// TODO: Load already existing books from internal memory.
	}
	
	/**
	 * Initialize the singleton backend class.
	 */
	
	public static void initializeInstance() {
		if (instance == null) { // NB: Not thread-safe
			instance = new Backend();
		}
	}
	
	/**
	 * @return the instance of the backend.
	 */
	public static Backend getInstance() {
		return instance;
	}
	
	/**
	 * Find if the book list already contains a book with the same key.
	 * The method is used before adding a downloaded book to the book list.
	 * The method is needed because the chapter and question lists may vary, hence the objects might be different.
	 * The method uses linear search.
	 * @param book
	 * @return
	 */
	private boolean bookListContains(Book book) {
		for (int i = 0; i < books.size(); i++) {
			if (books.get(i).getKey().equals(book.getKey())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Download a book.
	 * If the book already exists in the user's book list, an error message is shown. // TODO
	 * @param context		The current activity's context (needed for network connection check).
	 * @param ownerEmail	The book's owner's e-mail address.
	 * @param bookTitle		The book's title.
	 */
	public void downloadBook(Context context, String ownerEmail, String bookTitle) {
		try {
			Book book = new Book(context, ownerEmail, bookTitle);
			if (bookListContains(book)) {
				books.add(book);
			} else {
				// TODO: Book already exists, throw error message
				// Maybe update the book?
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the list of books currently downloaded.
	 */
	public List<Book> getBooks() {
		return books;
	}
}
