package com.lasalara.lasalara.activities;

import com.lasalara.lasalara.R;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BookFragment extends ListFragment{
	OnBookSelectedListener mCallback;
	
	public interface OnBookSelectedListener {
		public void onBookSelected(int position);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
		
		//List of books
		String[] a = new String[5];
		a[0] = "Book 1"; a[1]="Book 2"; a[2]="Book 3"; a[3]="Book 4"; a[4]="Book 5";
		
		setListAdapter(new ArrayAdapter<String>(getActivity(), layout, a));
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
		getListView().setItemChecked(position,  true);
	}
	
	
}
