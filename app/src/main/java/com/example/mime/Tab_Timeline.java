package com.example.mime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Tab_Timeline extends Activity {
    ListView tl_ListView;
    static TimeLine_ListAdapter tl_ListAdapter;
    String api_url;
    JsonDownLoad task;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_timeline);

        api_url = "http://tadalab.dothome.co.kr/core/api_test.php?method=downloadUmzzalDB";
        connectCheck("Tab_Timeline", api_url);

        tl_ListView = (ListView) findViewById(R.id.timeline_listview);
        tl_ListAdapter = new TimeLine_ListAdapter(this);
        tl_ListView.setAdapter(tl_ListAdapter);
        tl_ListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Tab_Timeline.this, Project_info.class);
                // i.putExtra("pos", Integer.valueOf(position).toString());
                startActivity(i);

            }
        });
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
