package com.lasalara.lasalara.frontend.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.WebRequestException;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Progress;
import com.lasalara.lasalara.frontend.adapters.CustomListAdapter;
import com.lasalara.lasalara.frontend.activities.MainActivity;

public class BookFragment extends ListFragment{
	OnBookSelectedListener mCallback;
	private List<Book> books;
	private CustomListAdapter listAdapter;
	private ActionMode actionMode;
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
			
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.book_delete_menu, menu);
	        return true;
	    }

	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; 
	    }

	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.deleteBook: {
	                books.get((int)maybeDeleteThisBook).delete();
	                mode.finish();
	                books.remove((int)maybeDeleteThisBook);
	                
	                ((MainActivity) getActivity()).updateProgressBar();
	                listAdapter.setBookData(books);
	                listAdapter.notifyDataSetChanged();
	                return true;
	            }
	            default:
	                return false;
	        }
	    }

	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	getListView().clearChoices();
	    	getListView().setItemChecked(getListView().getCheckedItemPosition(), false);
	        mode = null;
	    }
	};
	protected long maybeDeleteThisBook;
	
	public interface OnBookSelectedListener {
		public void onBookSelected(int position, Book bk);
	}
	
	public BookFragment(List<Book> books) {
		this.books = books;
	}
	
	@Override
    public void onActivityCreated(Bundle b) {
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(books.size() >= arg3) {
					actionMode = getActivity().startActionMode(mActionModeCallback);
					maybeDeleteThisBook = arg3;
					return true;
				}
				return false;
			}
		});
		super.onActivityCreated(b);
    }
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listAdapter = new CustomListAdapter(getActivity(), books);
		setListAdapter(listAdapter);
		
	}
	
	
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
		this.books = Backend.getInstance().getBooks();
		listAdapter.setBookData(books);
		listAdapter.notifyDataSetChanged();
	}
	
	public void onStart() {
		super.onStart();
		if(getFragmentManager().findFragmentById(R.id.fragment_container) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			getListView().setSelector(android.R.color.darker_gray);
		}
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnBookSelectedListener) activity;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement OnBookSelectedListener");
		}
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(actionMode != null)
			actionMode.finish();
		mCallback.onBookSelected(position, books.get(position));
	}

	public List<Chapter> getBookChapters(int position) {
		try {
			return books.get(position).getChapters();
		} catch (FormatException e) {
			Backend.getInstance().addMessage(e.getMessage());
			return new ArrayList<Chapter>();
		} catch (WebRequestException e) {
			Backend.getInstance().addMessage(e.getMessage());
			return new ArrayList<Chapter>();
		}
	}

	public Progress getProgress() {
		return Backend.getInstance().getProgress();
	}
	
	public void notifyDataChanged() {
		listAdapter.setBookData(Backend.getInstance().getBooks());
		listAdapter.notifyDataSetChanged();
	}
}
