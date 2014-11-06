package com.lasalara.lasalara.frontend.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lasalara.lasalara.R;

public class AddBookFragment extends Fragment {
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
		((EditText) getView().findViewById(R.id.author)).setText("");
		((EditText) getView().findViewById(R.id.book)).setText("");
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.addbook, container, false);
	        return v;
	    }
}
