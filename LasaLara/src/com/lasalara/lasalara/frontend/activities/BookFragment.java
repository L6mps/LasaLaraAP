package com.lasalara.lasalara.frontend.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
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
	private int layout;
	private Context context;
	
	public interface OnBookSelectedListener {
		public void onBookSelected(int position);
	}
	
	public BookFragment(List<Book> books) {
		this.books = books;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
		refresh();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}
	
	public void refresh() {
		List<String[]> info = new ArrayList<String[]>();
		for(Book i: books) {
			String[] s = {i.getTitle(),i.getOwnerEmail(),"0%","0/?"};
			info.add(s);
		}
		setListAdapter(new CustomListAdapter(getActivity(), info, layout));
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
	
	
	public void bookAddedNotification() {
		refresh();
	}
}
