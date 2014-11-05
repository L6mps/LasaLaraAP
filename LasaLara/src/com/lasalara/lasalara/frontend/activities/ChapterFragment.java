package com.lasalara.lasalara.frontend.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Chapter;

public class ChapterFragment extends ListFragment{
OnChapterSelectedListener mCallback;
	
	private List<Chapter> chapters;
	
	public interface OnChapterSelectedListener {
		public void onChapterSelected(int position);
	}
	
	public ChapterFragment() {
		this.chapters = new ArrayList<Chapter>();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new CustomListAdapter(getActivity(), chapters, true));
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

	public void changeData(List<Chapter> chapters) {
		this.chapters = chapters;
		if(this.getListAdapter()!=null)
			setListAdapter(new CustomListAdapter(getActivity(), chapters, true));
	}
	
	public Chapter getChapter(int position) {
		return chapters.get(position);
	}
}
