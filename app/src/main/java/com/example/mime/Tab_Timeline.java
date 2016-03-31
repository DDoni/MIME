package com.example.mime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Tab_Timeline extends Activity {

    String api_url;
    JsonDownLoad task;

    public RecyclerView tl_RecyclerView;
    public RecyclerView.Adapter tl_Adapter;
    public RecyclerView.LayoutManager tl_LayoutManager;

    public ArrayList<TimeLine_ListViewItem> mDataset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_timeline);

        api_url = "http://tadalab.dothome.co.kr/core/api_test.php?method=downloadUmzzalDB";
        connectCheck("Tab_Timeline", api_url);

        tl_RecyclerView = (RecyclerView) findViewById(R.id.timeline_listview);
        tl_RecyclerView.setHasFixedSize(true);
        tl_LayoutManager = new LinearLayoutManager(this);
        tl_RecyclerView.setLayoutManager(tl_LayoutManager);

        mDataset = new ArrayList<>();
        tl_Adapter = new TimeLine_Adapter(mDataset, this.getApplicationContext());
        tl_RecyclerView.setAdapter(tl_Adapter);

    }

    public void connectCheck(String page, String url) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            task = new JsonDownLoad(this, page, url);
            task.execute();

        } else
            Toast.makeText(this, "연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        api_url = "http://tadalab.dothome.co.kr/core/api_test.php?method=downloadUmzzalDB";
        connectCheck("Tab_Timeline", api_url);
    }
}
