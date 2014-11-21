package com.lasalara.lasalara.frontend.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lasalara.lasalara.R;

public class AddBookFragment extends Fragment {
	
	private boolean bookListened;
	
	public void onPause() {
		((MainActivity) getActivity()).hideSoftwareKeyboard();
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
		EditText author = ((EditText) getView().findViewById(R.id.author));
		author.setText("");
		author.requestFocus();
		EditText book = ((EditText) getView().findViewById(R.id.book));
		book.setText("");
		if(!bookListened) {
			bookListened = true;
			book.setOnEditorActionListener(new OnEditorActionListener() {
		        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
		                ((MainActivity) getActivity()).downloadBookOnAddBookDialogClick();
		            }    
		            return false;
		        }
		    });
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.addbook, container, false);
	        return v;
	    }
}
