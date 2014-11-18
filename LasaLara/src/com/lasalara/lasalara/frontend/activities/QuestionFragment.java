package com.lasalara.lasalara.frontend.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.lasalara.lasalara.R;
import com.lasalara.lasalara.backend.structure.Chapter;
import com.lasalara.lasalara.backend.structure.Progress;
import com.lasalara.lasalara.backend.structure.Question;

public class QuestionFragment extends Fragment {
	ProgressBarRefreshListener refCallback;
	ManualBack refBack;
	
	public interface ProgressBarRefreshListener {
		public void onProgressRefresh();
	}
	
	public interface ManualBack {
		public void manualBack();
	}
	
	private Chapter parentChapter;
	
	private Question qa;
	private boolean answered = false;
	private TextSwitcher q;
	private TextSwitcher a;
	private TableLayout f;
	
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			refCallback = (ProgressBarRefreshListener) activity;
			refBack = (ManualBack) activity;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement ProgressBarRefreshListener");
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.questionlayout, container, false);
	        q = (TextSwitcher) v.findViewById(R.id.questionView);
			a = (TextSwitcher) v.findViewById(R.id.answerView);
			f = (TableLayout) v.findViewById(R.id.feedback);
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
			qa = parentChapter.getNextQuestion();
			if(qa==null)
				refBack.manualBack();
			else {
				q.setText(getCurrentQuestion());
				a.setText("");
				answered = false;
				f.setVisibility(View.GONE);
			}
	        return v;
	    }
	
	private void changeQuestion() {
		
		q.setText(qa.getQuestion());
		answered = false;
		a.setText("");
		f.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.exit));
		f.setVisibility(View.GONE);
		
		//Proof-of-concept, shows which question is showing as progress
		refCallback.onProgressRefresh();
	}
	
	private CharSequence getCurrentAnswer() {
		return qa.getAnswer();
	}

	private CharSequence getCurrentQuestion() {
		return qa.getQuestion();
	}
	
	private void showAnswer() {
		if(!answered) {
			answered = true;
			a.setText(getCurrentAnswer());
			f.setVisibility(View.VISIBLE);
			Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
			f.setAnimation(slide);
		}
	}
	
	public void screenTapped() {
		showAnswer();
	}
	
	public void screenSwiped(char c) {
		if(c=='r')
			nextQuestion();
		else if(c=='l')
			return;
			//previousQuestion();
	}

	/*
	private void previousQuestion() {
		if(!answered)
			changeToPreviousQuestion();		
	}
	
	private void changeToPreviousQuestion() {
		q.setInAnimation(getActivity(), R.anim.enter_right);
		q.setOutAnimation(getActivity(), R.anim.exit_right);
		a.setInAnimation(getActivity(), R.anim.enter_right);
		a.setOutAnimation(getActivity(), R.anim.exit_right);
		q.setText(getCurrentQuestion());
		answered = false;
		a.setText("");
		f.setVisibility(View.GONE);
		q.setInAnimation(getActivity(), R.anim.enter);
		q.setOutAnimation(getActivity(), R.anim.exit);
		a.setInAnimation(getActivity(), R.anim.enter);
		a.setOutAnimation(getActivity(), R.anim.exit);
		
		//Proof-of-concept, shows which question is showing as progress
		refCallback.onProgressRefresh();
	}*/

	private void nextQuestion() {
		qa = parentChapter.getNextQuestion();
		if(qa!=null)
			changeQuestion();
		else
			refBack.manualBack();
	}
	
	public void changeData(Chapter cp) {
		this.parentChapter = cp;
		this.answered = false;
	}

	public Progress getProgress() {
		return parentChapter.getProgress();
	}

	public void resetProgress() {
		//TODO
		
	}
	
	//0 is positive, 1 is between, 2 is negative
	public void onFeedback(int pos) {
		if(pos==0)
			this.qa.setKnown();
		nextQuestion();
	}
	
}
