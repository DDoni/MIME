package com.example.mime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class Tab_MakeGif extends Activity implements View.OnClickListener {


    ImageButton shortGif_Btn, longGif_Btn;

    public RecyclerView pj_RecyclerView;
    public RecyclerView.Adapter pj_Adapter;
    public RecyclerView.LayoutManager pj_LayoutManager;

    public ArrayList<Project_ListViewItem> mDataset;

    String api_url;
    JsonDownLoad task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_makegif);

        api_url = "http://tadalab.dothome.co.kr/core/api_test.php?method=getProjectList";
        connectCheck("Tab_MakeGif", api_url);

        shortGif_Btn = (ImageButton) findViewById(R.id.shortGif_Btn);
        findViewById(R.id.shortGif_Btn).setOnClickListener(this);

        longGif_Btn = (ImageButton) findViewById(R.id.longGif_Btn);
        findViewById(R.id.longGif_Btn).setOnClickListener(this);


        pj_RecyclerView = (RecyclerView) findViewById(R.id.longGif_List);
        pj_RecyclerView.setHasFixedSize(true);
        pj_LayoutManager = new LinearLayoutManager(this);
        pj_RecyclerView.setLayoutManager(pj_LayoutManager);

        mDataset = new ArrayList<>();
        pj_Adapter = new Project_Adapter(mDataset, this.getApplicationContext());
        pj_RecyclerView.setAdapter(pj_Adapter);

        pj_RecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(Tab_MakeGif.this, Project_info.class);
                // i.putExtra("pos", Integer.valueOf(position).toString());
                startActivity(i);

            }
        }));

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId()) {
            case R.id.shortGif_Btn:
                intent = new Intent(this, GifCamera.class);
                startActivity(intent);
                break;

            case R.id.longGif_Btn:
                intent = new Intent(Tab_MakeGif.this, Project_info.class);
                startActivity(intent);
                break;

        }
    }

    public void connectCheck(String page, String url) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            task = new JsonDownLoad(this, page, url);
            task.execute();

        } else
            Toast.makeText(this, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
    }

}