package com.lasalara.lasalara.frontend.fragments;

import java.util.List;

import com.lasalara.lasalara.backend.structure.Question;
import com.lasalara.lasalara.frontend.fragments.QuestionFragment.ManualBack;
import com.lasalara.lasalara.frontend.fragments.QuestionFragment.ProgressBarRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lasalara.lasalara.R;


public class PageViewFragment extends Fragment{
	ProgressBarRefreshListener refCallback;
	ManualBack refBack;
	View rootView;
	ExpandableListView lv;
	private String[] groups;
	private String[][] children;
	
	public PageViewFragment(){
		
	}
	
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
	
	public void changeData(List<Question> questions){
		groups = new String[questions.size()];
		children = new String[questions.size()][1];
		int i = 0;
		for(Question question: questions){
			groups[i]=question.getQuestion();
			children[i][0]=question.getAnswer();
			i++;
		}
		if(lv!=null)
			lv.setAdapter(new ExpandableListAdapter(getActivity(), groups,children));
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		rootView = inflater.inflate(R.layout.contentlists, container, false);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		
		lv = (ExpandableListView) view.findViewById(R.id.expListView);
		lv.setAdapter(new ExpandableListAdapter(getActivity(), groups, children));
		lv.setGroupIndicator(null);
	}
	
	public class ExpandableListAdapter extends BaseExpandableListAdapter {
		private String[] groups;
		private String[][] children;
		private Context context;
		
		public ExpandableListAdapter(FragmentActivity activity, String[] groups, String[][] children){
			context = activity;
			this.groups = groups;
			this.children = children;
		}
		
		@Override
		public int getGroupCount(){
			return groups.length;
		}
		
		@Override
		public int getChildrenCount(int groupPosition){
			return children[groupPosition].length;
		}
		
		@Override
		public Object getGroup(int groupPosition){
			return groups[groupPosition];
		}
		
		@Override
		public long getGroupId(int groupPosition){
			return groupPosition;
		}
		
		@Override
		public long getChildId(int groupPosition, int childPosition){
			return childPosition;
		}
		
		@Override
		public boolean hasStableIds(){
			return true;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
			    LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inf.inflate(R.layout.list_group, parent, false);
				holder = new ViewHolder();
				
				holder.text = (TextView) convertView.findViewById(R.id.lbListHeader);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(getGroup(groupPosition).toString());
			
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inf.inflate(R.layout.list_item, parent, false);
				holder = new ViewHolder();
				
				holder.text = (TextView) convertView.findViewById(R.id.lbListItem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(getChild(groupPosition, childPosition).toString());
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
	}
	
	private class ViewHolder {
		TextView text;
	}
}