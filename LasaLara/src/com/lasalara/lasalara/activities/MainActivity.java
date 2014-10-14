package com.lasalara.lasalara.activities;

import com.lasalara.lasalara.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends FragmentActivity implements BookFragment.OnBookSelectedListener,ChapterFragment.OnChapterSelectedListener  {
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        return super.onCreateOptionsMenu(menu);
    }
	
	
	
}
