package com.lasalara.lasalara.activities;

import java.util.ArrayList;
import com.lasalara.lasalara.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

	private class Info {
		String title;
		String author;
		String progress_percent;
		String progress_fraction;
		
		Info(String a, String b, String c, String d) {
			this.title = a;
			this.author = b;
			this.progress_percent = c;
			this.progress_fraction = d;
		}
	}
	
	private LayoutInflater inflater;
	private ArrayList<Info> info;
	
	public CustomListAdapter(Context context, ArrayList<String[]> i, int layout) {
		this.info = new ArrayList<Info>();
		inflater = LayoutInflater.from(context);
	    for(String[] s: i) {
	    	this.info.add(new Info(s[0],s[1],s[2],s[3]));
	    }
	}
	
	public void setAllListItems(ArrayList<String[]> i) {
		for(String[] s: i) {
	    	this.info.add(new Info(s[0],s[1],s[2],s[3]));
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

	@SuppressLint("InflateParams") @Override  //warning about inflating with null ViewGroup
	public View getView(int position, View arg1, ViewGroup arg2) {
	    View v = arg1;
	    if(v == null) {
	    	LayoutInflater vi = inflater;
	    	v = vi.inflate(R.layout.listelement, null);
	    }
	    
	    TextView t1 = (TextView)v.findViewById(R.id.title);
	    TextView t2 = (TextView)v.findViewById(R.id.author);
	    TextView t3 = (TextView)v.findViewById(R.id.donePercentage);
	    TextView t4 = (TextView)v.findViewById(R.id.doneFraction);
	    
	    Info i = info.get(position);
	    t1.setText(i.title);
	    t2.setText(i.author);
	    t3.setText(i.progress_percent);
	    t4.setText(i.progress_fraction);
	    
	    return v;
	}
}

