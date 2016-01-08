package com.example.mime;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TimeLine_ListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<TimeLine_ListViewItem> data = new ArrayList<TimeLine_ListViewItem>();
    Context context;

    public TimeLine_ListAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
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
            view = inflater.inflate(R.layout.timeline_row, parent, false);
        } else
            view = convertView;
        TimeLine_ListViewItem item = (TimeLine_ListViewItem) data.get(position);
        ImageView imageview = (ImageView) view.findViewById(R.id.timeline_imageview);

        Glide.with(this.context).load("http://tadalab.dothome.co.kr/core/umzzal/" + item.getTl_picname()).into(imageview);
        ImageView tl_profile = (ImageView) view.findViewById(R.id.timeline_profile);

        TextView tl_id = (TextView) view.findViewById(R.id.timeline_id);
        TextView tl_content = (TextView) view.findViewById(R.id.timeline_text);

        tl_id.setText("@ddoni");
        tl_content.setText("date : " + item.getTl_date() + "\n" + "pickname : " + item.getTl_picname() + "\n" + "dir : "
                + item.getTl_dir() + "\n" + "content : " + item.getTl_content().toString() + "\n");

        return view;
    }
}
