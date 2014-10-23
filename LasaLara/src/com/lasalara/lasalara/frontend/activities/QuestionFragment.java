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

//To change question/answer text, see nextQuestion() example

public class QuestionFragment extends Fragment {
	
	private List<String> questions;
	private List<String> answers;
	private int questionPointer;
	private int questionTotal;
	
	public QuestionFragment() {
		this.questions = new LinkedList<String>();
		this.answers = new LinkedList<String>();
		questions.add("How does LasaLara work?");
		answers.add("1. At LasaLara.com, your teacher creates a study book of questions and answers for your class.\n2. This book may include chapters that were created by other teachers. \n3. Your teacher might ask you and your classmates to propose additional questions and answers for the book. \n4. You study the book - in an optimized way - on your mobile phone, or at LasaLara.com.");
		questions.add("How will a study book help me learn faster?");
		answers.add("A study book clarifies what your teacher expects you to know by the end of the course. With studying, half the battle is knowing what you don't know.");
		questions.add("Why is answering questions so important?");
		answers.add("When you answer questions about something, you are much more likely to remember key points than reading alone. Also, answering questions is great exam preparation.");
		questions.add("LasaLara tracks my progress as I learn. How does this help?");
		answers.add("Progress tracking lets LasaLara highlight for you how much you still need to learn. For each chapter (and each book) you see the percentage of questions you can currently answer. You know exactly whether you are keeping up. \nNOTE: Your progress is only tracked for you, on your computer or mobile phone. No progress data is kept at LasaLara.com or sent to your teacher.");
		questionPointer = 0;
		questionTotal = 4;
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
		return this.answers.get(questionPointer);
	}

	private CharSequence getCurrentQuestion() {
		return this.questions.get(questionPointer);
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
	
}
