package com.example.mime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 돈영 on 2016-03-11.
 */
public class Project_Adapter extends RecyclerView.Adapter<Project_Adapter.ViewHolder> {
    ArrayList<Project_ListViewItem> mDataset = new ArrayList<>();
    Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pj_num;
        public TextView pj_title;
        public TextView pj_st_date;
        public TextView pj_goal;
        public TextView pj_alarm;
        public TextView pj_percent;

        public ViewHolder(View view) {
            super(view);
            pj_num = (TextView) view.findViewById(R.id.pj_num);
            pj_title = (TextView) view.findViewById(R.id.pj_title);
            pj_st_date = (TextView) view.findViewById(R.id.pj_st_date);
            pj_goal = (TextView) view.findViewById(R.id.pj_goal);
            pj_alarm = (TextView) view.findViewById(R.id.pj_alarm);
            pj_percent = (TextView) view.findViewById(R.id.pj_percent);
        }
    }

    public Project_Adapter(ArrayList<Project_ListViewItem> mDataset, Context mContext) {
        this.mDataset = mDataset;
        this.mContext = mContext;
    }

    @Override
    public Project_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //데이터 셋
        Project_ListViewItem item = mDataset.get(position);
        holder.pj_num.setText(String.valueOf(item.getNum()).toString());
        holder.pj_title.setText(item.getPj_title().toString());
        System.out.println("num : " + String.valueOf(item.getNum()).toString());
        System.out.println("title : " + item.getPj_title().toString());


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub

        return mDataset.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}

