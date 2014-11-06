package com.lasalara.lasalara.frontend.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Progress;
import com.lasalara.lasalara.backend.structure.Question;
import com.lasalara.lasalara.frontend.activities.ChapterFragment.OnChapterSelectedListener;

//To change question/answer text, see nextQuestion() example

public class QuestionFragment extends Fragment {
	ProgressBarRefreshListener refCallback;
	
	public interface ProgressBarRefreshListener {
		public void onProgressRefresh();
	}
	
	private Chapter parentChapter;
	
	private List<Question> qa;
	private int questionPointer;
	private int questionTotal;
	private boolean answered = false;
	private TextSwitcher q;
	private TextSwitcher a;
	
	public QuestionFragment() {
		questionPointer = 0;
		questionTotal = 0;
		qa = new LinkedList<Question>();
	}
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			refCallback = (ProgressBarRefreshListener) activity;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement ProgressBarRefreshListener");
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.questionlayout, container, false);
	        
	        q = (TextSwitcher) v.findViewById(R.id.questionView);
			a = (TextSwitcher) v.findViewById(R.id.answerView);
			q.setFactory(new ViewFactory() {

				@Override
				public View makeView() {
					return new TextView(getActivity());
				}
				
			});
			a.setFactory(new ViewFactory() {

				@Override
				public View makeView() {
					return new TextView(getActivity());
				}
				
			});
			q.setInAnimation(getActivity(), R.anim.enter);
			q.setOutAnimation(getActivity(), R.anim.exit);
			a.setInAnimation(getActivity(), R.anim.enter);
			a.setOutAnimation(getActivity(), R.anim.exit);
			q.setText(getCurrentQuestion());
			a.setText("");
			answered = false;
	        return v;
	    }
	
	private void changeQuestion() {
		q.setText(getCurrentQuestion());
		answered = false;
		a.setText("");
		
		//Proof-of-concept, shows which question is showing as progress
		refCallback.onProgressRefresh();
	}
	
	private void changeToPreviousQuestion() {
		q.setInAnimation(getActivity(), R.anim.enter_right);
		q.setOutAnimation(getActivity(), R.anim.exit_right);
		a.setInAnimation(getActivity(), R.anim.enter_right);
		a.setOutAnimation(getActivity(), R.anim.exit_right);
		q.setText(getCurrentQuestion());
		answered = false;
		a.setText("");
		q.setInAnimation(getActivity(), R.anim.enter);
		q.setOutAnimation(getActivity(), R.anim.exit);
		a.setInAnimation(getActivity(), R.anim.enter);
		a.setOutAnimation(getActivity(), R.anim.exit);
		
		//Proof-of-concept, shows which question is showing as progress
		refCallback.onProgressRefresh();
	}
	
	private CharSequence getCurrentAnswer() {
		return this.qa.get(questionPointer).getAnswer();
	}

	private CharSequence getCurrentQuestion() {
		return this.qa.get(questionPointer).getQuestion();
	}
	
	private void showAnswer() {
		if(!answered) {
			answered = true;
			a.setText(getCurrentAnswer());
		}
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
		changeToPreviousQuestion();		
	}

	private void nextQuestion() {
		questionPointer++;
		if(questionPointer == questionTotal)
			questionPointer = 0;
		changeQuestion();
	}
	
	public void changeData(List<Question> qa, Chapter cp) {
		this.qa = qa;
		this.questionTotal = qa.size();
		this.parentChapter = cp;
		this.questionPointer = 0;
		this.answered = false;
	}

	public Progress getProgress() {
		
		//return parentChapter.getProgress();
		
		return new Progress(questionPointer+1, questionTotal);
	}
	
}
