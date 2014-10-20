package com.lasalara.lasalara.activities;

import com.lasalara.lasalara.Backend;
import com.lasalara.lasalara.R;
import com.lasalara.lasalara.constants.StringConstants;
import com.lasalara.lasalara.database.DatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;

public class MainActivity extends FragmentActivity implements BookFragment.OnBookSelectedListener,
															  ChapterFragment.OnChapterSelectedListener, OnGestureListener {
	
	private BookFragment bFragment;
	private ChapterFragment cFragment;
	private QuestionFragment qFragment;
	private GestureDetector gd;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.gd = new GestureDetector(this);
		setContentView(R.layout.contentlists);
		if(findViewById(R.id.fragment_container) != null) {
			if(savedInstanceState != null) {
				return;
			}
			cFragment = new ChapterFragment();
			cFragment.setArguments(getIntent().getExtras());
			bFragment = new BookFragment();
			bFragment.setArguments(getIntent().getExtras());
			qFragment = new QuestionFragment();
			qFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bFragment).commit();
		}
		Backend.initializeInstance();
		DatabaseHelper.initializeInstance(this);
		Log.d(StringConstants.APP_NAME, "Downloading book.");
		Backend.getInstance().downloadBook(this, "lasalara.help@gmail.com", "Welcome to LasaLara"); // TODO: Only for testing
		Log.d(StringConstants.APP_NAME, "Downloaded book.");
		Log.d(StringConstants.APP_NAME, "Downloaded book.");
		Backend.getInstance().preloadData(DatabaseHelper.getInstance());
	}

	@Override
	public void onBookSelected(int position) {
		cFragment.changeData(position);
		changeFragment(cFragment);
	}

	@Override
	public void onChapterSelected(int position) {
		changeFragment(qFragment);
	}
	
	private void changeFragment(Fragment f) {
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).addToBackStack(f.getTag()).commit();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if((e1.getX() - e2.getX()) > 150) {
			qFragment.screenSwiped();
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gd.onTouchEvent(me);
	}
	
	
}
