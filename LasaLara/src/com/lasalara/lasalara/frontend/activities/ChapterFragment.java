package com.lasalara.lasalara.frontend.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Chapter;

public class ChapterFragment extends ListFragment{
OnChapterSelectedListener mCallback;
	
	private int layout;
	private List<String[]> info;
	private List<Chapter> chapters;
	private Context context;
	
	public interface OnChapterSelectedListener {
		public void onChapterSelected(int position);
	}
	
	public ChapterFragment() {
		this.info = new ArrayList<String[]>();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
		setListAdapter(new CustomListAdapter(getActivity(), info, layout));
	}
	
	public void onStart() {
		super.onStart();
		if(getFragmentManager().findFragmentById(R.id.fragment_container) != null)
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
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

	public void changeData(List<Chapter> list) {
		this.chapters = list;
		List<String[]> info = new ArrayList<String[]>();
		for(Chapter i:list) {
			String[] tmp = {i.getTitle(),i.getAuthorName(),"0%","0/?"};
			info.add(tmp);
		}
		this.info = info;
		if(this.getListAdapter()!=null)
			setListAdapter(new CustomListAdapter(context, this.info, layout));
	}
	
	public Chapter getChapter(int position) {
		return chapters.get(position);
	}
}
