package com.lasalara.lasalara.backend;

import java.util.ArrayList;
import java.util.List;

import com.lasalara.lasalara.LasaLaraApplication;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.InputDoesntExistException;
import com.lasalara.lasalara.backend.exceptions.NumericExceptionMessage;
import com.lasalara.lasalara.backend.exceptions.WebRequestException;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Message;
import com.lasalara.lasalara.backend.structure.Progress;

/**
 * The main class of the back end of the application.
 * The whole back end is referred to through this class.
 * @author Ants-Oskar M�esalu
 */
public class Backend {
	private static Backend instance;		// The back end instance
	static MessageListener messageCallback;	// The message queue callback class
	private List<Book> books;				// The downloaded books' list
	private boolean pageViewOn;				// Questions' page view setting - whether the questions are displayed on a single page or separately
	
	/**
	 * Constructor.
	 * Private because this is a singleton.
	 */
	private Backend() {
		super();
		books = new ArrayList<Book>();
		pageViewOn = false;
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
	 * Initialize the callback object.
	 */
	public static void initializeCallback() {
		messageCallback = (MessageListener) LasaLaraApplication.getCurrentContext();
	}
	
	/**
	 * Add a new message to the message queue.
	 * @param message	The Message object.
	 */
	public void addMessage(Message message) {
		messageCallback.messageAdded(message);
	}
	
	/**
	 * Add a new message to the message queue.
	 * @param message	The message string.
	 */
	public void addMessage(String message) {
		messageCallback.messageAdded(new Message(message));
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
	 * If the book already exists in the user's book list, an error message is shown.
	 * @param ownerEmail	The book's owner's e-mail address.
	 * @param bookTitle		The book's title.
	 */
	public Book downloadBook(final String ownerEmail, final String bookTitle) {
		Book newBook = null;
		try {
			newBook = new Book(ownerEmail, bookTitle, true);
			int index = getBookFromBookList(newBook);
			if (index == -1) {
				books.add(newBook);
			} else {
				books.set(index, newBook);
				books.get(index).update();
				addMessage(StringConstants.MESSAGE_BOOK_DOWNLOAD_UPDATED);
			}
		} catch (InputDoesntExistException e) {
			addMessage(e.getMessage());
		} catch (FormatException e) {
			addMessage(e.getMessage());
		} catch (WebRequestException e) {
			addMessage(e.getMessage());
		}
		return newBook;
	}
	
	/**
	 * Update a book with the specified index.
	 * @param index
	 */
	public void updateBook(int index) {
		try {
			if (index < books.size()) {
				books.get(index).update();
			} else {
				addMessage(NumericExceptionMessage.INVALID_BOOK_INDEX.getMessage());
			}
		} catch (InputDoesntExistException e) {
			addMessage(e.getMessage());
		} catch (FormatException e) {
			addMessage(e.getMessage());
		} catch (WebRequestException e) {
			addMessage(e.getMessage());
		}
	}
	
	/**
	 * Delete a book with the specified index from the application.
	 * @param index		The book's index in the book list.
	 */
	public void deleteBook(int index) {
		if (index < books.size()) {
			books.get(index).delete();
			books.remove(index);
		} else {
			addMessage(NumericExceptionMessage.INVALID_BOOK_INDEX.getMessage());
		}
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

	/**
	 * @return whether the page view of questions is switched on or not.
	 */
	public boolean isPageViewOn() {
		return pageViewOn;
	}

	/**
	 * @param pageViewOn	Whether questions in a chapter should be displayed on a single page or separately.
	 */
	public void setPageViewOn(boolean pageViewOn) {
		this.pageViewOn = pageViewOn;
	}
	
	/**
	 * The message queue listener.
	 * @author Ants-Oskar M�esalu
	 */
	public interface MessageListener {
		public void messageAdded(Message message);
	}
}
