package com.lasalara.lasalara.frontend.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Question;

//To change question/answer text, see nextQuestion() example

public class QuestionFragment extends Fragment {
	
	private List<Question> qa;
	private int questionPointer;
	private int questionTotal;
	
	public QuestionFragment() {
		questionPointer = 0;
		questionTotal = 0;
		qa = new LinkedList<Question>();
	}
	
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
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
			q.setText(getCurrentQuestion());
			a.setText(getCurrentAnswer());
			a.setVisibility(View.INVISIBLE);
	        return v;
	    }
	
	private void changeQuestion() {
		View v = this.getView();
		TextView q = (TextView) v.findViewById(R.id.questionView);
		TextView a = (TextView) v.findViewById(R.id.answerView);
		q.setText(getCurrentQuestion());
		a.setText(getCurrentAnswer());
		hideAnswer();
	}
	
	private CharSequence getCurrentAnswer() {
		return this.qa.get(questionPointer).getAnswer();
	}

	private CharSequence getCurrentQuestion() {
		return this.qa.get(questionPointer).getQuestion();
	}
	
	private void hideAnswer() {
		this.getView().findViewById(R.id.answerView).setVisibility(View.INVISIBLE);
	}
	
	private void showAnswer() {
		View answer = this.getView().findViewById(R.id.answerView);
		if(answer.getVisibility()!=View.VISIBLE)
			answer.setVisibility(View.VISIBLE);
	}
	
	public void screenTapped() {
		showAnswer();
	}
	
	public void screenSwiped(char c) {
		if(c=='r')
			nextQuestion();
		else if(c=='l')
			previousQuestion();
	}

	private void previousQuestion() {
		questionPointer--;
		if(questionPointer < 0)
			questionPointer = questionTotal - 1;
		changeQuestion();		
	}

	private void nextQuestion() {
		questionPointer++;
		if(questionPointer == questionTotal)
			questionPointer = 0;
		changeQuestion();
	}
	
	public void changeData(List<Question> qa) {
		this.qa = qa;
		this.questionTotal = qa.size();
	}
	
}
