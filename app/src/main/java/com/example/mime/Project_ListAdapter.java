package com.example.mime;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Project_ListAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<Project_ListViewItem> data = new ArrayList<Project_ListViewItem>();
	Context context;
	
	// public ListViewAdapter(LayoutInflater inflater, ArrayList<ListViewItem>
	// data) {
	public Project_ListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
		// this.data = data;
		// this.inflater = inflater;
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView == null) {
			//
			view = inflater.inflate(R.layout.project_row, parent, false);
		} else
			view = convertView;

		TextView pj_num = (TextView) view.findViewById(R.id.pj_num);
		TextView pj_title = (TextView) view.findViewById(R.id.pj_title);
		TextView pj_st_date = (TextView) view.findViewById(R.id.pj_st_date);
		TextView pj_goal = (TextView) view.findViewById(R.id.pj_goal);
		TextView pj_alarm = (TextView) view.findViewById(R.id.pj_alarm);
		TextView pj_percent = (TextView) view.findViewById(R.id.pj_percent);
		
		Project_ListViewItem item = (Project_ListViewItem) data.get(position);
		
		pj_num.setText(String.valueOf(item.getNum()).toString());
		pj_title.setText(item.getPj_title().toString());



		return view;
	}

}
