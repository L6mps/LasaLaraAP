package com.lasalara.lasalara.frontend.activities;

import java.io.IOException;
import java.util.Locale;

import org.json.JSONException;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

public class MainActivity extends FragmentActivity implements BookFragment.OnBookSelectedListener,
															  ChapterFragment.OnChapterSelectedListener, 
															  OnGestureListener {
	
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
		cFragment.changeData(bFragment.getBookChapters(position));
		changeFragment(cFragment);
	}

	@Override
	public void onChapterSelected(int position)  {
		try {
			qFragment.changeData(cFragment.getChapter(position).getQuestions(this));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		changeFragment(qFragment);
	}
	
	private void changeFragment(Fragment f) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
		ft.replace(R.id.fragment_container, f).addToBackStack(f.getTag());
		ft.commit();
		this.invalidateOptionsMenu();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(qFragment.isVisible())
        	inflater.inflate(R.menu.question_fragment_settings, menu);
        else if(cFragment.isVisible())
        	inflater.inflate(R.menu.chapter_fragment_settings, menu);
        else if(bFragment.isVisible())
        	inflater.inflate(R.menu.book_fragment_settings, menu);
        else if(abFragment.isVisible())
        	inflater.inflate(R.menu.addbook_fragment_settings, menu);
        else
        	inflater.inflate(R.menu.question_fragment_settings, menu);
        return super.onCreateOptionsMenu(menu);
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
		if(qFragment.isVisible())
			qFragment.screenTapped();
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gd.onTouchEvent(me);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    if(item.getItemId() == R.id.add_book) {
	        changeFragment(abFragment);
	    	return true;
	    }
	    else if(item.getItemId() == R.id.done) {
	    	Backend.getInstance().downloadBook(this, 
	    									 ((EditText)abFragment.getView().findViewById(R.id.author)).getText().toString().toLowerCase(Locale.ENGLISH),
	    									 ((EditText)abFragment.getView().findViewById(R.id.book)).getText().toString().toLowerCase(Locale.ENGLISH));
	    	getSupportFragmentManager().popBackStack(); //takes back the transaction from bFragment to abFragment, animating back
	    	bFragment.refresh();
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
