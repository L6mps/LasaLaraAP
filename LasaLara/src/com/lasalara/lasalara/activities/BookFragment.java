package com.lasalara.lasalara.activities;

import java.util.ArrayList;

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
		String[] b = new String[5];
		String[] c = new String[5];
		String[] d = new String[5];
		a[0] = "Book 1"; a[1]="Book 2"; a[2]="Book 3"; a[3]="Book 4"; a[4]="Book 5";
		b[0] = "Abe"; b[1]="Ben"; b[2]="Chuck"; b[3]="Dylan"; b[4]="Edward";
		c[0] = "17%"; c[1]="20%"; c[2]="50%"; c[3]="10%"; c[4]="5%";
		d[0] = "2/7"; d[1]="1/5"; d[2]="2/4"; d[3]="1/10"; d[4]="1/20";
		
		ArrayList<String[]> info = new ArrayList<String[]>();
		for(int i=0; i<5; i++) {
			String[] tmp = new String[4];
			tmp[0]=a[i];
			tmp[1]=b[i];
			tmp[2]=c[i];
			tmp[3]=d[i];
			info.add(tmp);
		}
		setListAdapter(new BookListAdapter(getActivity(), info, layout));
		//setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.listelement_book,R.id.bookTitle.toString(), R.id.bookAuthorInfo.toString(),a, b));
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
	
	
}
