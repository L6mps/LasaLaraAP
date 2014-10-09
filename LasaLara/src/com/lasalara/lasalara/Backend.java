package com.lasalara.lasalara;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.lasalara.lasalara.backend.Book;
import com.lasalara.lasalara.constants.StringConstants;
import com.lasalara.lasalara.database.DatabaseHelper;

/**
 * The main class of the back end of the application.
 * The whole back end is referred to through this class.
 * @author Ants-Oskar Mäesalu
 */
public class Backend {
	private static Backend instance;
	private List<Book> books;
	bug
	/**
	 * Constructor.
	 * Private because this is a singleton.
	 */
	private Backend() {
		super();
		books = new ArrayList<Book>();
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
	 * Preload all of the book data from the SQLite database.
	 * @param context	The current activity's context (needed for network connection check and SQLite database).
	 * @return list of books in the SQLite database.
	 */
	private List<Book> preloadBooks(DatabaseHelper databaseHelper) {
		return databaseHelper.getBookHelper().getBooks();
	}
	
	/**
	 * Preload all of the data from the SQLite database.
	 * @param context	The current activity's context (needed for network connection check and SQLite database).
	 */
	public void preloadData(DatabaseHelper databaseHelper) {
		try {
			books = preloadBooks(databaseHelper); // TODO: Somewhy, this brings up the debugger
		} catch (Exception e) {
			Log.d(StringConstants.APP_NAME, e.toString());
		}
		// Chapters should be preloaded only when a book is opened - saves time.
		// Questions should be preloaded at the same time the book is opened - the user
		// needs to see chapters' progress. Maybe we could optimise it?
	}
	
	/**
	 * Find if the book list already contains a book with the same key. If so, return the index,
	 * if not, return -1.
	 * The method is used before adding a downloaded book to the book list.
	 * The method is needed because the chapter and question lists may vary, 
	 * hence the objects might be different.
	 * The method uses linear search.
	 * @param book
	 * @return
	 */
	private int getBookFromBookList(Book book) {
		for (int i = 0; i < books.size(); i++) {
			if (books.get(i).getKey().equals(book.getKey())) {
				return i;
			}
		}
		return -1;
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
			Book newBook = new Book(context, ownerEmail, bookTitle);
			int index = getBookFromBookList(newBook);
			if (index == -1) {
				books.add(newBook);
			} else {
				// TODO: Throw error message: book already exists
				books.set(index, newBook);// Update the book
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
