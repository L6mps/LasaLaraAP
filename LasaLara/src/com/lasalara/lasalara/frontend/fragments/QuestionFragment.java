package com.lasalara.lasalara.frontend.fragments;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.lasalara.lasalara.backend.Backend;
import com.lasalara.lasalara.backend.exceptions.NumericException;
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
	
	public Chapter getParentChapter() {
		return parentChapter;
	}

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
        	qa = null;
			try {
				qa = parentChapter.getNextQuestion();
				if(qa == null) {
					refBack.manualBack();
					return v;
				} // TODO: Revise whether the whole method should be done inside the "try" block.
			} catch (NumericException e) {
				Backend.getInstance().addMessage(e.getMessage());
			}
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
			q.setInAnimation(getActivity(), R.anim.fade_in);
			q.setOutAnimation(getActivity(), R.anim.exit);
			a.setInAnimation(getActivity(), R.anim.fade_in);
			a.setOutAnimation(getActivity(), R.anim.exit);
			
			q.setText(getCurrentQuestion());
			a.setText("");
			answered = false;
			f.setVisibility(View.GONE);
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

	private void nextQuestion() {
		try {
			qa = parentChapter.getNextQuestion();
			if(qa != null)
				changeQuestion();
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Chapter complete! You will need to review this chapter after 5 seconds"); // TODO: Remove this after the message queue callback has been implemented
				builder.setPositiveButton("OK", null);
				builder.show();			
				refBack.manualBack();
			}
		} catch (NumericException e) {
			Backend.getInstance().addMessage(e.getMessage());
		}
	}
	
	public void changeData(Chapter cp) {
		this.parentChapter = cp;
		this.answered = false;
	}

	public Progress getProgress() {
		return parentChapter.getProgress();
	}

	public void resetProgress() {
		parentChapter.resetProgress();
	}
	
	//0 is positive, 1 is between, 2 is negative
	public void onFeedback(int pos) {
		if(pos==0)
			this.qa.setKnown();
		else if(pos == 1)
			this.qa.setPartiallyKnown();
		else if(pos == 2)
			this.qa.setUnknown();
		nextQuestion();
	}
	
}
