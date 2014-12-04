package com.lasalara.lasalara.frontend.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.InputDoesntExistException;
import com.lasalara.lasalara.backend.exceptions.WebRequestException;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Progress;
import com.lasalara.lasalara.frontend.adapters.CustomListAdapter;

public class ChapterFragment extends ListFragment{
OnChapterSelectedListener mCallback;
	
	private List<Chapter> chapters;
	private Book parentBook;
	private CustomListAdapter listAdapter;
	
	public interface OnChapterSelectedListener {
		public void onChapterSelected(Chapter cp);
	}
	
	public ChapterFragment() {
		this.chapters = new ArrayList<Chapter>();
		this.parentBook = null;
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
		try {
			this.chapters = parentBook.getChapters();
		} catch (FormatException e) {
			Backend.getInstance().addMessage(e.getMessage());
			this.chapters = new ArrayList<Chapter>();
		} catch (WebRequestException e) {
			Backend.getInstance().addMessage(e.getMessage());
			this.chapters = new ArrayList<Chapter>();
		}
		listAdapter.setChapterData(chapters);
		listAdapter.notifyDataSetChanged();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listAdapter = new CustomListAdapter(getActivity(), chapters, true);
		setListAdapter(listAdapter);
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
		mCallback.onChapterSelected(chapters.get(position));
		getListView().setItemChecked(position,  true);
	}

	public void changeData(List<Chapter> chapters, Book bk) {
		this.chapters = chapters;
		this.parentBook = bk;
		if(this.getListAdapter()!=null)
			listAdapter.setChapterData(chapters);
		else
			setListAdapter(new CustomListAdapter(getActivity(), chapters, true));
	}
	
	public Chapter getChapter(int position) {
		return chapters.get(position);
	}

	public Progress getProgress() {
		return parentBook.getProgress();
	}

	public void refreshChapters() {
		try {
			this.setListShown(false);
			parentBook.update();
			setListAdapter(new CustomListAdapter(getActivity(), chapters, true));
		} catch (InputDoesntExistException e) {
			Backend.getInstance().addMessage(e.getMessage());
		} catch (FormatException e) {
			Backend.getInstance().addMessage(e.getMessage());
		} catch (WebRequestException e) {
			Backend.getInstance().addMessage(e.getMessage());
		}
		
	}
}
