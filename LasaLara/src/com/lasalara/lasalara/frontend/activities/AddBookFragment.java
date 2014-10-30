package com.lasalara.lasalara.frontend.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lasalara.lasalara.R;

//To change question/answer text, see nextQuestion() example

public class AddBookFragment extends Fragment {
	
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
	}
	
	public void onStart() {
		super.onStart();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.addbook, container, false);
	        return v;
	    }	
}
