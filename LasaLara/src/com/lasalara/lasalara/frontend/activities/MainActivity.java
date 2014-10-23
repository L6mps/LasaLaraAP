package com.lasalara.lasalara.frontend.activities;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends FragmentActivity implements BookFragment.OnBookSelectedListener,
															  ChapterFragment.OnChapterSelectedListener, OnGestureListener {
	
	private BookFragment bFragment;
	private ChapterFragment cFragment;
	private QuestionFragment qFragment;
	private AddBookFragment abFragment;
	private GestureDetector gd;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.gd = new GestureDetector(this,this);
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
			abFragment = new AddBookFragment();
			abFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bFragment).commit();
		}
		/*
		Log.d(StringConstants.APP_NAME, "Initialising database helper.");
		DatabaseHelper.initialiseInstance(this);
		Log.d(StringConstants.APP_NAME, "Downloading book.");
		Backend.getInstance().downloadBook(this, "lasalara.help@gmail.com", "Welcome to LasaLara"); // TODO: Only for testing
		Log.d(StringConstants.APP_NAME, "Downloaded book.");
		Backend.getInstance().preloadData(DatabaseHelper.getInstance());
		*/
	}

	@Override
	public void onBookSelected(int position) {
		if(position == 4)
			changeFragment(abFragment);
		else {
			cFragment.changeData(position);
			changeFragment(cFragment);
		}
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
        inflater.inflate(R.menu.question_fragment_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if(!qFragment.isVisible())
			return false;
		else if((e1.getX() - e2.getX()) > 150) {
			qFragment.screenSwiped('r');
			return true;
		}
		else if((e2.getX() - e1.getX()) > 150) {
			qFragment.screenSwiped('l');
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {	
		if(qFragment.isVisible())
			qFragment.screenTapped();
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
	
	public void addBookListener(View v) {
		changeFragment(bFragment);
		((EditText) abFragment.getView().findViewById(R.id.author)).setText("");
		((EditText) abFragment.getView().findViewById(R.id.book)).setText("");
	}
	
	public void cancelListener(View v){
		changeFragment(bFragment);
		((EditText) abFragment.getView().findViewById(R.id.author)).setText("");
		((EditText) abFragment.getView().findViewById(R.id.book)).setText("");
	}
	
	
}
