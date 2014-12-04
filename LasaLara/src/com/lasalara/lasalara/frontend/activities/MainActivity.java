package com.lasalara.lasalara.frontend.activities;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lasalara.lasalara.LasaLaraApplication;
import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.constants.StringConstants;
import com.lasalara.lasalara.backend.database.DatabaseHelper;
import com.lasalara.lasalara.backend.exceptions.FormatException;
import com.lasalara.lasalara.backend.exceptions.FormatExceptionMessage;
import com.lasalara.lasalara.backend.exceptions.WebRequestException;
import com.lasalara.lasalara.backend.exceptions.WebRequestExceptionMessage;
import com.lasalara.lasalara.backend.structure.Book;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Message;
import com.lasalara.lasalara.backend.structure.Progress;
import com.lasalara.lasalara.backend.webRequest.UrlParameters;
import com.lasalara.lasalara.backend.webRequest.WebRequest;
import com.lasalara.lasalara.frontend.fragments.AddBookFragment;
import com.lasalara.lasalara.frontend.fragments.BookFragment;
import com.lasalara.lasalara.frontend.fragments.ChapterFragment;
import com.lasalara.lasalara.frontend.fragments.PageViewFragment;
import com.lasalara.lasalara.frontend.fragments.ProposeQuestionFragment;
import com.lasalara.lasalara.frontend.fragments.QuestionFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends FragmentActivity implements BookFragment.OnBookSelectedListener,
															  ChapterFragment.OnChapterSelectedListener,
															  QuestionFragment.ProgressBarRefreshListener,
															  QuestionFragment.ManualBack,
															  OnGestureListener,
															  Backend.MessageListener {
	
	private BookFragment bFragment;
	private ChapterFragment cFragment;
	private QuestionFragment qFragment;
	private AddBookFragment abFragment;
	private PageViewFragment pvFragment;
	private ProposeQuestionFragment pqFragment;
	private GestureDetector gd;
	private ProgressBar progressBar;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LasaLaraApplication.setCurrentContext(this);
		Backend.initializeCallback();
		Log.d(StringConstants.APP_NAME, "Initialising database helper.");
		DatabaseHelper.initialiseInstance(this);
		Log.d(StringConstants.APP_NAME, "Getting books.");
		Backend.getInstance().preloadData();
		
		this.gd = new GestureDetector(this, this);
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
			
			pvFragment = new PageViewFragment();
			pvFragment.setArguments(getIntent().getExtras());
			
			pqFragment = new ProposeQuestionFragment();
			pqFragment.setArguments(getIntent().getExtras());
			
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, bFragment).commit();
			
			Progress bProgress = Backend.getInstance().getProgress();
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
	public void onChapterSelected(Chapter cp)  {
		if(cp.isCompleted()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("This chapter has been completed. You can review it in Page View");
			builder.setPositiveButton("OK", null);
			builder.show();		
	    	changeFragment(pvFragment);
	    	pvFragment.changeData(cp.getAllQuestions());
		}
		qFragment.changeData(cp);
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
		getActionBar().setDisplayShowHomeEnabled(false);
        if(qFragment.isVisible())
        	inflater.inflate(R.menu.question_fragment_settings, menu);
        else if(cFragment.isVisible())
        	inflater.inflate(R.menu.chapter_fragment_settings, menu);
        else if(bFragment.isVisible()) {
        	inflater.inflate(R.menu.book_fragment_settings, menu);
        	getActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else if(pvFragment.isVisible()){
        	inflater.inflate(R.menu.pageview_fragment_settings, menu);
        }
        else if(abFragment.isVisible())
        	inflater.inflate(R.menu.addbook_fragment_settings, menu);
        else if(pqFragment.isVisible())
        	inflater.inflate(R.menu.proposequestion_fragment_settings, menu);
        else
        	inflater.inflate(R.menu.question_fragment_settings, menu);
        updateProgressBar();
        return super.onCreateOptionsMenu(menu);
    }
	
	public void updateProgressBar() {
		progressBar.setVisibility(View.VISIBLE);
		if(qFragment.isVisible() || pvFragment.isVisible()) {
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
        else if(abFragment.isVisible() || pqFragment.isVisible()) {
        	progressBar.setEnabled(false);
        	setTitle("return");
        	progressBar.setVisibility(View.GONE);        	
        }
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if((qFragment.isVisible() && (e1.getX() - e2.getX()) > 150)) {
			qFragment.screenSwiped('r');
			return true;
		}
		else if((e2.getX() - e1.getX()) < 150 && !bFragment.isVisible()) {
			manualBack();
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
	    	downloadBookOnAddBookDialogClick();
	    	return true;
	    }
	    else if(item.getItemId() == android.R.id.home) {
	    	getSupportFragmentManager().popBackStack();
	    	updateProgressBar();
	    }
	    else if(item.getItemId() == R.id.refreshChapters) {
	    	cFragment.refreshChapters();
	    }
	    else if(item.getItemId() == R.id.reset_progress) {
	    	qFragment.resetProgress();
	    	//changeFragment(qFragment);
	    	Progress qProgress = qFragment.getProgress();
        	progressBar.setProgress(qProgress.getPercentage());
        	setTitle(qProgress.getCurrent() + "/" + qProgress.getMaximum());
	    }
	    else if(item.getItemId() == R.id.turn_page_view_on) {
	    	changeFragment(pvFragment);
	    	pvFragment.changeData(qFragment.getParentChapter().getAllQuestions());
	    }
	    else if(item.getItemId() == R.id.turn_page_view_off) {
	    	getSupportFragmentManager().popBackStack();
	    }
	    else if(item.getItemId() == R.id.Switch_QA) {
	    	//TODO
	    }
	    else if(item.getItemId() == R.id.propose_question) {
	    	changeFragment(pqFragment);
	    	return true;
	    }
	    else if(item.getItemId() == R.id.send) {
	    	sendQuestionPropositionOnQuestionPropositionDialogClick();
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	
	public void hideSoftwareKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(
  		      Context.INPUT_METHOD_SERVICE);
  		if(this.getCurrentFocus()!=null)
  			imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), 0);
	}

	@Override
	public void onProgressRefresh() {
		updateProgressBar();
	}
	
	public void onFeedback(View v) {
		if(v.getId() == R.id.positiveFeedback)
			qFragment.onFeedback(0);
		else if(v.getId() == R.id.neutralFeedback)
			qFragment.onFeedback(1);
		else if(v.getId() == R.id.negativeFeedback)
			qFragment.onFeedback(2);
	}

	@Override
	public void manualBack() {
		this.getSupportFragmentManager().popBackStack();
	}

	public void downloadBookOnAddBookDialogClick() {
		Book newBook = Backend.getInstance().downloadBook(((EditText)abFragment.getView().findViewById(R.id.author)).getText().toString(),
				 ((EditText)abFragment.getView().findViewById(R.id.book)).getText().toString());
		DownloadBookOnAddTask task = new DownloadBookOnAddTask();
		task.execute(newBook);
		getSupportFragmentManager().popBackStack(); //takes back the transaction from bFragment to abFragment, animating back
		hideSoftwareKeyboard();
	}
	
	public void sendQuestionPropositionOnQuestionPropositionDialogClick() {
		try {
			qFragment.getParentChapter().poseQuestion(((EditText)pqFragment.getView().findViewById(R.id.question)).getText().toString(),
					((EditText)pqFragment.getView().findViewById(R.id.answer)).getText().toString());
		} catch (WebRequestException e) {
			Backend.getInstance().addMessage(e.getMessage());
		}
		getSupportFragmentManager().popBackStack(); // Goes back to last screen
		hideSoftwareKeyboard();
		// TODO: an alert thanking for the proposition
	}
	
	private class DownloadBookOnAddTask extends AsyncTask<Book, Integer, String> {
		ProgressDialog progress;
		int chapterCount=0;
		
		@Override
		protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this,"","Downloading...",true);
		}
		@Override
		protected String doInBackground(Book... params){
			String key = params[0].getKey();
			String url = StringConstants.URL_GET_CHAPTERS;
			UrlParameters urlParameters = new UrlParameters();
			urlParameters.addPair("bk", key);
			WebRequest request = null;
			try {
				request = new WebRequest(url, urlParameters);
			} catch (IOException e1) {
				try {
					throw new WebRequestException(WebRequestExceptionMessage.CHAPTERS_DOWNLOAD);
				} catch (WebRequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (WebRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONArray resultArray = request.getJSONArray();
				chapterCount=resultArray.length();
				for (int i = 0; i < chapterCount; i++) {
					JSONObject result = resultArray.getJSONObject(i);
					String chapterKey = result.get("ck").toString();
					String chapterTitle = result.get("title").toString();
					int chapterVersion = result.getInt("version");
					String chapterAuthorEmail = result.get("email").toString();
					String chapterAuthorName = null;
					if (!result.isNull("name")) {
						chapterAuthorName = result.get("name").toString();
					}
					String chapterAuthorInstitution = null;
					if (!result.isNull("institution")) {
						chapterAuthorInstitution = result.get("institution").toString();
					}
					boolean chapterProposalsAllowed = result.getBoolean("allowProp");
					params[0].getChapters().add(new Chapter(chapterKey, chapterTitle, chapterVersion, 
							chapterAuthorEmail, chapterAuthorName, chapterAuthorInstitution, 
							chapterProposalsAllowed, i, key, true));
					publishProgress(i+1);
					
				}
			} catch (JSONException e) {
				try {
					throw new FormatException(FormatExceptionMessage.CHAPTERS_DOWNLOAD);
				} catch (FormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (WebRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String string) {
		     progress.dismiss();
		  }
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		     progress.setMessage("Downloaded " + values[0] + " of " + chapterCount + " chapters");
		}
	}

	@Override
	public void messageAdded(Message message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message.getMessage());
		builder.setPositiveButton("OK", null);
		builder.show();
	}
}
