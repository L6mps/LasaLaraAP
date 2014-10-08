package com.lasalara.lasalara.activities;

import com.lasalara.lasalara.R;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChapterFragment extends ListFragment{
OnChapterSelectedListener mCallback;
	
	public interface OnChapterSelectedListener {
		public void onChapterSelected(int position);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
		
		//List of books
		String[] a = new String[5];
		a[0] = "Chapter 1"; a[1]="Chapter 2"; a[2]="Chapter 3"; a[3]="Chapter 4"; a[4]="Chapter 5";
		
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
			mCallback = (OnChapterSelectedListener) activity;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement OnChapterSelectedListener");
		}
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onChapterSelected(position);
		getListView().setItemChecked(position,  true);
	}
}
