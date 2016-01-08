package com.example.mime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Tab_MakeGif extends Activity implements View.OnClickListener {

	
	ImageButton shortGif_Btn,longGif_Btn;

	static public Project_ListAdapter pj_ListAdapter;

	ListView pj_ListView;

	Project_ListViewItem item;

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

		pj_ListView = (ListView) findViewById(R.id.longGif_List);

		pj_ListAdapter = new Project_ListAdapter(this);
		pj_ListView.setAdapter(pj_ListAdapter);
		pj_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Tab_MakeGif.this, Project_info.class);
				// i.putExtra("pos", Integer.valueOf(position).toString());
				startActivity(i);

			}
		});

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