package com.lasalara.lasalara.activities;

import com.lasalara.lasalara.Backend;
import com.lasalara.lasalara.LasaLaraApplication;
import com.lasalara.lasalara.R;
import com.lasalara.lasalara.constants.StringConstants;
import com.lasalara.lasalara.database.DatabaseHelper;

import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Ants-Oskar Mäesalu
 */
public class BookListPageActivity extends Activity {
	protected LasaLaraApplication application;
	protected DatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (LasaLaraApplication) getApplication();
		try {
			databaseHelper = new DatabaseHelper(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContentView(R.layout.activity_booklistpage);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Backend.getInstance().preloadData(databaseHelper);
		// TODO: ChapterListPageActivity should use getChapters() on startup, and on all of 
		// the returned chapters getQuestions(). This way, the data is always up to date, 
		// yet we do not (probably) use too much memory.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_booklistpage, container,
					false);
			return rootView;
		}
	}
}
