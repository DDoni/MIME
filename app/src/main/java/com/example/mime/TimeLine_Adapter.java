package com.example.mime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by 돈영 on 2016-03-11.
 */
public class TimeLine_Adapter extends RecyclerView.Adapter<TimeLine_Adapter.ViewHolder> {
    ArrayList<TimeLine_ListViewItem> mDataset = new ArrayList<>();
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview;
        ImageView tl_profile;

        TextView tl_id;
        TextView tl_content;

        public ViewHolder(View view) {
            super(view);
            imageview = (ImageView) view.findViewById(R.id.timeline_imageview);
            tl_profile = (ImageView) view.findViewById(R.id.timeline_profile);

            tl_id = (TextView) view.findViewById(R.id.timeline_id);
            tl_content = (TextView) view.findViewById(R.id.timeline_text);
        }
    }

    public TimeLine_Adapter(ArrayList<TimeLine_ListViewItem> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    @Override
    public TimeLine_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //데이터 셋
        TimeLine_ListViewItem item = mDataset.get(position);
        Glide.with(this.context).load("http://tadalab.dothome.co.kr/core/umzzal/" + item.getTl_picname()).into(holder.imageview);
        holder.tl_id.setText("@ddoni");
        holder.tl_content.setText("date : " + item.getTl_date() + "\n" + "pickname : " + item.getTl_picname() + "\n" + "dir : "
                + item.getTl_dir() + "\n" + "content : " + item.getTl_content().toString() + "\n");

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

