package com.lasalara.lasalara.activities;

import java.util.ArrayList;

import com.lasalara.lasalara.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {

	private class BookInfo {
		String bookTitle;
		String author;
		String progress_percent;
		String progress_fraction;
		
		BookInfo(String a, String b, String c, String d) {
			this.bookTitle = a;
			this.author = b;
			this.progress_percent = c;
			this.progress_fraction = d;
		}
	}
	
	
	private LayoutInflater inflater;
	private ArrayList<BookInfo> info;
	
	public BookListAdapter(Context context, ArrayList<String[]> i, int layout) {
		this.info = new ArrayList<BookInfo>();
		inflater = LayoutInflater.from(context);
	    for(String[] s: i) {
	    	this.info.add(new BookInfo(s[0],s[1],s[2],s[3]));
	    }
	}
	
	@Override
	public int getCount() {
		if(info != null)
			return info.size();
		else return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if(info!=null)
			return info.get(arg0);
		else
			return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
	    View v = arg1;
	    if(v == null) {
	    	LayoutInflater vi = inflater;
	    	v = vi.inflate(R.layout.listelement_book, null);
	    }
	    
	    
	    TextView t1 = (TextView)v.findViewById(R.id.bookTitle);
	    TextView t2 = (TextView)v.findViewById(R.id.bookAuthorInfo);
	    TextView t3 = (TextView)v.findViewById(R.id.bookDonePercentage);
	    TextView t4 = (TextView)v.findViewById(R.id.bookFractionDone);
	    
	    BookInfo bi = info.get(position);
	    t1.setText(bi.bookTitle);
	    t2.setText(bi.author);
	    t3.setText(bi.progress_percent);
	    t4.setText(bi.progress_fraction);
	    
	    return v;
	}
}

