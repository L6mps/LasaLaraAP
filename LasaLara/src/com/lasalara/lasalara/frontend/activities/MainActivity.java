package com.lasalara.lasalara.frontend.activities;

import java.util.Locale;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Progress;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends FragmentActivity implements BookFragment.OnBookSelectedListener,
															  ChapterFragment.OnChapterSelectedListener,
															  QuestionFragment.ProgressBarRefreshListener,
															  OnGestureListener {
	
	private BookFragment bFragment;
	private ChapterFragment cFragment;
	private QuestionFragment qFragment;
	private AddBookFragment abFragment;
	private GestureDetector gd;
	private ProgressBar progressBar;
	
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
			
			Progress bProgress = new Progress(25, 50);//Backend.getInstance().getProgress(); //When progress works, use this
			progressBar = (ProgressBar) findViewById(R.id.listProgressBar);
			progressBar.setProgress((int)bProgress.getPercentage());
		}
		
	}

	@Override
	public void onBookSelected(int position, Book bk) {
		cFragment.changeData(bFragment.getBookChapters(position), bk);
		changeFragment(cFragment);
	}

	@Override
	public void onChapterSelected(int position, Chapter cp)  {
		qFragment.changeData(cFragment.getChapter(position).getQuestions(), cp);
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
		getActionBar().setDisplayHomeAsUpEnabled(true);
        if(qFragment.isVisible())
        	inflater.inflate(R.menu.question_fragment_settings, menu);
        else if(cFragment.isVisible())
        	inflater.inflate(R.menu.chapter_fragment_settings, menu);
        else if(bFragment.isVisible()) {
        	inflater.inflate(R.menu.book_fragment_settings, menu);
        	getActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else if(abFragment.isVisible())
        	inflater.inflate(R.menu.addbook_fragment_settings, menu);
        else
        	inflater.inflate(R.menu.question_fragment_settings, menu);
        updateProgressBar();
        return super.onCreateOptionsMenu(menu);
    }
	
	public void updateProgressBar() {
		progressBar.setVisibility(View.VISIBLE);
		if(qFragment.isVisible()) {
			Progress qProgress = qFragment.getProgress();
        	progressBar.setProgress(qProgress.getPercentage());
        	setTitle(qProgress.getCurrent() + "/" + qProgress.getMaximum());
        }
        else if(cFragment.isVisible()) {
        	Progress cProgress = cFragment.getProgress();
        	progressBar.setProgress(cProgress.getPercentage());
        	setTitle(cProgress.getCurrent() + "/" + cProgress.getMaximum());
        }
        else if(bFragment.isVisible()) {
        	Progress bProgress = bFragment.getProgress();
        	progressBar.setProgress(bProgress.getPercentage());
        	setTitle(bProgress.getCurrent() + "/" + bProgress.getMaximum());
        }
        else if(abFragment.isVisible()) {
        	progressBar.setEnabled(false);
        	setTitle("");
        	progressBar.setVisibility(View.GONE);        	
        }
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
	    else if(item.getItemId() == android.R.id.home) {
	    	getSupportFragmentManager().popBackStack();
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressRefresh() {
		updateProgressBar();
	}
}
