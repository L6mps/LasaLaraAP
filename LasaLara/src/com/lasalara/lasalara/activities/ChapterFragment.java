package com.lasalara.lasalara.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.lasalara.lasalara.R;

public class ChapterFragment extends ListFragment{
OnChapterSelectedListener mCallback;
	
	private int parentBook;
	private int layout;
	private ArrayList<String[]> info;
	private CustomListAdapter cla;

	public interface OnChapterSelectedListener {
		public void onChapterSelected(int position);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
		if(info==null)
			this.info = new ArrayList<String[]>();
		cla = new CustomListAdapter(getActivity(), info, layout);
		setListAdapter(cla);
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

	public void changeData(int position) {
		parentBook = position+1;
		//List of chapters
		String[] a = new String[5];
		String[] b = new String[5];
		String[] c = new String[5];
		String[] d = new String[5];
		a[0] = "Chapter 1, book "+parentBook; a[1]="Chapter 2, book "+parentBook; a[2]="Chapter 3, book "+parentBook; a[3]="Chapter 4, book "+parentBook; a[4]="Chapter 5, book "+parentBook;
		b[0] = "Abe"; b[1]="Ben"; b[2]="Chuck"; b[3]="Dylan"; b[4]="Edward";
		c[0] = "0%"; c[1]="0%"; c[2]="0%"; c[3]="0%"; c[4]="0%";
		d[0] = "0/0"; d[1]="0/0"; d[2]="0/0"; d[3]="0/0"; d[4]="0/0";
		
		ArrayList<String[]> info = new ArrayList<String[]>();
		for(int i=0; i<5; i++) {
			String[] tmp = new String[4];
			tmp[0]=a[i];
			tmp[1]=b[i];
			tmp[2]=c[i];
			tmp[3]=d[i];
			info.add(tmp.clone());
		}
		this.info = info;
		if(cla!=null)
			cla.setAllListItems(info);
			
	}
}
