package com.lasalara.lasalara.frontend.activities;

import java.io.IOException;

import org.json.JSONException;

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
		
		Log.d(StringConstants.APP_NAME, "Initialising database helper.");
		DatabaseHelper.initialiseInstance(this);
		Log.d(StringConstants.APP_NAME, "Getting books.");
		Backend.getInstance().preloadData(DatabaseHelper.getInstance());
		
		this.gd = new GestureDetector(this,this);
		setContentView(R.layout.contentlists);
		if(findViewById(R.id.fragment_container) != null) {
			if(savedInstanceState != null) {
				return;
			}
			bFragment = new BookFragment(Backend.getInstance().getBooks());
			bFragment.setArguments(getIntent().getExtras());
			
			cFragment = new ChapterFragment();
			cFragment.setArguments(getIntent().getExtras());
			
			qFragment = new QuestionFragment();
			qFragment.setArguments(getIntent().getExtras());
			abFragment = new AddBookFragment();
			abFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bFragment).commit();
		}
	}

	@Override
	public void onBookSelected(int position) {
		//If the last element of the list "Add a book..." has been selected
		if(bFragment.getBookCount() == position)
			changeFragment(abFragment);
		else {
			cFragment.changeData(bFragment.getBookChapters(position));
			changeFragment(cFragment);
		}
	}

	@Override
	public void onChapterSelected(int position)  {
		try {
			qFragment.changeData(cFragment.getChapter(position).getQuestions(this));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Backend.getInstance().downloadBook(this, ((EditText)abFragment.getView().findViewById(R.id.author)).getText().toString(),((EditText)abFragment.getView().findViewById(R.id.book)).getText().toString());
		bFragment.bookAddedNotification();
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
