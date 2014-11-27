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

public class ProposeQuestionFragment extends Fragment {
	
	// COPIED STRAIGHT FROM ADDBOOKFRAGMENT, TODO!!!
	
	private boolean questionListened;
	
	public void onPause() {
		((MainActivity) getActivity()).hideSoftwareKeyboard();
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
		EditText question = ((EditText) getView().findViewById(R.id.question));
		question.setText("");
		question.requestFocus();
		EditText answer = ((EditText) getView().findViewById(R.id.answer));
		answer.setText("");
		if(!questionListened) {
			questionListened = true;
			answer.setOnEditorActionListener(new OnEditorActionListener() {
		        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
		                ((MainActivity) getActivity()).sendQuestionPropositionOnQuestionPropositionDialogClick();
		            }    
		            return false;
		        }
		    });
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.proposequestion, container, false);
	        return v;
	    }
	
}

