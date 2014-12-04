package com.lasalara.lasalara.frontend.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.WebRequestException;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Progress;
import com.lasalara.lasalara.frontend.adapters.CustomListAdapter;

public class BookFragment extends ListFragment{
	OnBookSelectedListener mCallback;
	private List<Book> books;
	private CustomListAdapter listAdapter;
	
	public interface OnBookSelectedListener {
		public void onBookSelected(int position, Book bk);
	}
	
	public BookFragment(List<Book> books) {
		this.books = books;
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
		if(getFragmentManager().findFragmentById(R.id.fragment_container) != null)
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
}
