package com.lasalara.lasalara.activities;

import com.lasalara.lasalara.Backend;
import com.lasalara.lasalara.R;
import com.lasalara.lasalara.database.DatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MainActivity extends FragmentActivity implements BookFragment.OnBookSelectedListener, ChapterFragment.OnChapterSelectedListener  {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contentlists);
		if(findViewById(R.id.fragment_container) != null) {
			if(savedInstanceState != null) {
				return;
			}
			BookFragment bFragment = new BookFragment();
			bFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bFragment).addToBackStack("bookList").commit();
		}
		Backend.initializeInstance();
		Backend.getInstance().downloadBook(this, "lasalara.help@gmail.com", "Welcome to LasaLara"); // TODO: Only for testing
		Backend.getInstance().preloadData(new DatabaseHelper(this));
	}

	@Override
	public void onBookSelected(int position) {
		ChapterFragment cFragment = new ChapterFragment();
		cFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cFragment).addToBackStack("chapterList").commit();
	}

	@Override
	public void onChapterSelected(int position) {
		Log.e("debug","Chapter "+position+" selected");
		
	}
	
	
}
