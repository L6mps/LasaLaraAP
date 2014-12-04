package com.lasalara.lasalara.frontend.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Progress;

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
	
	private Context context;
	private ArrayList<Info> info;
	
	public CustomListAdapter(FragmentActivity activity, List<Chapter> chapters, boolean isChapter) {
		this.info = new ArrayList<Info>();
		context = activity;
		setChapterData(chapters);
	}
	
	public CustomListAdapter(FragmentActivity activity, List<Book> books) {
		context = activity;
		this.info = new ArrayList<Info>();
		setBookData(books);
	}
	
	public void setChapterData(List<Chapter> chapters) {
		info.clear();
		for(Chapter i:chapters) {
			Progress prg = i.getProgress();
			int currentPercentage = (int) prg.getPercentage();
			String author = null;
			if(i.getAuthorName() != null) {
				author = i.getAuthorName();
			    if(i.getAuthorInstitution() != null)
			    	author = author + ", " + i.getAuthorInstitution();
			}
			else if(i.getAuthorEmail() != null)
				author = i.getAuthorEmail();
			else
				author = "";
			this.info.add(new Info(i.getTitle(),author,currentPercentage+"%",prg.getCurrent()+"/"+prg.getMaximum()));
		}
	}
	
	public void setBookData(List<Book> books) {
		info.clear();
		for(Book i:books) {
			Progress prg = i.getProgress();
			int currentPercentage = (int) prg.getPercentage();
			String author = null;
			if(i.getOwnerName() != null) {
				author = i.getOwnerName();
			    if(i.getOwnerInstitution() != null)
			    	author = author + ", " + i.getOwnerInstitution();
			}
			else if(i.getOwnerEmail() != null)
				author = i.getOwnerEmail();
			else
				author = "";
			this.info.add(new Info(i.getTitle(),author,currentPercentage+"%",prg.getCurrent()+"/"+prg.getMaximum()));
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

	public View getView(int position, View arg1, ViewGroup arg2) {
	    View v = arg1;
	    if(v == null) {
	    	LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	v = vi.inflate(R.layout.listelement, arg2, false);
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

