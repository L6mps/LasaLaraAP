package com.lasalara.lasalara.frontend.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;

public class BookFragment extends ListFragment{
	OnBookSelectedListener mCallback;
	private List<Book> books;
	
	public interface OnBookSelectedListener {
		public void onBookSelected(int position);
	}
	
	public BookFragment(List<Book> books) {
		this.books = books;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refresh();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}
	
	public void refresh() {
		setListAdapter(new CustomListAdapter(getActivity(), books));
	}
	
	public void onStart() {
		super.onStart();
		if(getFragmentManager().findFragmentById(R.id.fragment_container) != null)
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnBookSelectedListener) activity;
			refresh();
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement OnBookSelectedListener");
		}
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onBookSelected(position);
	}

	public List<Chapter> getBookChapters(int position) {
		return books.get(position).getChapters();
	}
}
