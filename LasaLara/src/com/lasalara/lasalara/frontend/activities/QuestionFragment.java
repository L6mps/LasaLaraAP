package com.lasalara.lasalara.frontend.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lasalara.lasalara.R;

//To change question/answer text, see nextQuestion() example

public class QuestionFragment extends Fragment {
	
	private Boolean isAnswerShowing;
	private String question = "Is this a question?";
	private String answer = "This is a question, indeed!";
	
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		this.isAnswerShowing = false;
	}
	
	public void onStart() {
		super.onStart();
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.questionlayout, container, false);
	        TextView q = (TextView) v.findViewById(R.id.questionView);
			TextView a = (TextView) v.findViewById(R.id.answerView);
			q.setText(question);
			a.setText(answer);
			a.setVisibility(View.INVISIBLE);
	        return v;
	    }
	
	public void changeQuestionAnswerText() {
		View v = this.getView();
		TextView q = (TextView) v.findViewById(R.id.questionView);
		TextView a = (TextView) v.findViewById(R.id.answerView);
		q.setText(question);
		a.setText(answer);
		showHideAnswer();
	}
	
	private void showHideAnswer() {
		this.getView().findViewById(R.id.answerView).setVisibility(isAnswerShowing?View.VISIBLE:View.INVISIBLE);
	}
	
	public void screenSwiped() {
		if(isAnswerShowing) {
			isAnswerShowing = false;
			nextQuestion();
		}
		else 
			isAnswerShowing = true;
		showHideAnswer();
	}

	private void nextQuestion() {
		this.question = new String(question + " + 1");
		this.answer = new String(answer + "- 1");
		changeQuestionAnswerText();
	}
	
}
