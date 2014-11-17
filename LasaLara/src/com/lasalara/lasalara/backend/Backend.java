package com.lasalara.lasalara.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import android.content.Context;
import android.util.Log;

import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.InputDoesntExistException;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Message;
import com.lasalara.lasalara.backend.structure.Progress;

/**
 * The main class of the back end of the application.
 * The whole back end is referred to through this class.
 * @author Ants-Oskar Mäesalu
 */
public class Backend {
	private static Backend instance;	// The back end instance
	private Queue<Message> messages;	// The application message queue
	private List<Book> books;			// The downloaded books' list
	
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
	 * Add a new message to the message queue.
	 * @param message	The Message object.
	 */
	public void addMessage(Message message) {
		messages.add(message);
	}
	
	/**
	 * Add a new message to the message queue.
	 * @param message	The message string.
	 */
	public void addMessage(String message) {
		messages.add(new Message(message));
	}
	
	/**
	 * Retrieve and remove the next message in the message queue.
	 * @return the next message in the message queue.
	 */
	public Message nextMessage() {
		return messages.poll();
	}
	
	/**
	 * Preload all of the book data from the SQLite database.
	 */
	void preloadBooks() {
		books = DatabaseHelper.getInstance().getBookHelper().getBooks();
	}
	
	/**
	 * Preload all of the data from the SQLite database.
	 */
	public void preloadData() {
		preloadBooks();
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
	public void downloadBook(final Context context, final String ownerEmail, final String bookTitle) {
		Book newBook;
		try {
			newBook = new Book(context, ownerEmail, bookTitle);
			int index = getBookFromBookList(newBook);
			if (index == -1) {
				Log.d(StringConstants.APP_NAME, "Book didn't exist, added it to the list.");
				books.add(newBook);
			} else {
				Log.d(StringConstants.APP_NAME, "Book already existed, updated it.");
				books.set(index, newBook); // Update the book (TODO?)
				addMessage(StringConstants.MESSAGE_BOOK_DOWNLOAD_UPDATED);
			}
		} catch (InputDoesntExistException e) {
			addMessage(e.getMessage());
			e.printStackTrace();
		} catch (FormatException e) {
			addMessage(e.getMessage());
		}
	}
	
	/**
	 * Update a book with the specified index.
	 * @param index
	 */
	public void updateBook(int index) {
		try {
			books.get(index).update();
		} catch (InputDoesntExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete a book with the specified index from the application.
	 * @param index		The book's index in the book list.
	 */
	public void deleteBook(int index) {
		books.get(index).delete();
		books.remove(index);
	}
	
	/**
	 * Delete all of the books from the application.
	 */
	public void deleteBooks() {
		DatabaseHelper.getInstance().getBookHelper().deleteBooks();
		books.clear(); // TODO: Garbage collection
	}
	
	/**
	 * Reset the entire study progress.
	 */
	public void resetProgress() {
		for (Book book: books) {
			book.resetProgress();
		}
	}
	
	/**
	 * Calculate the entire study progress based on the downloaded books' progresses.
	 * @return the entire study Progress object.
	 */
	public Progress getProgress() {
		List<Progress> progressList = new ArrayList<Progress>();
		for (Book book: books) {
			progressList.add(book.getProgress());
		}
		return new Progress(progressList);
	}
	
	/**
	 * @return the list of books currently downloaded.
	 */
	public List<Book> getBooks() {
		return books;
	}
}
