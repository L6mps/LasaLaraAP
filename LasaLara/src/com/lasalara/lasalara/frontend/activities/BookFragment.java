package com.lasalara.lasalara.frontend.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;

public class BookFragment extends ListFragment{
	OnBookSelectedListener mCallback;
	private List<Book> books;
	private int bookCount;
	private int layout;
	
	public interface OnBookSelectedListener {
		public void onBookSelected(int position);
	}
	
	public BookFragment(List<Book> books) {
		this.books = books;
		this.bookCount = books.size();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
		refresh();
		
	}
	
	public void refresh() {
		List<String[]> info = new ArrayList<String[]>();
		for(Book i: books) {
			String[] s = {i.getTitle(),i.getOwnerName(),"0%","0/?"};
			info.add(s);
			Log.e("debug",i.getKey());
		}
		String[] newBook = {"Add new book...","","",""};
		info.add(newBook);
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
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement OnBookSelectedListener");
		}
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onBookSelected(position);
	}
	
	public int getBookCount() {
		return this.bookCount;
	}

	public List<Chapter> getBookChapters(int position) {
		// TODO Auto-generated method stub
		try {
			return books.get(position).getChapters();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void bookAddedNotification() {
		bookCount++;
		refresh();
	}
	
	
}
