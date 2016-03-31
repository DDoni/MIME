package com.example.mime;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class JsonDownLoad extends AsyncTask<String, String, String> {

    Tab_MakeGif pj_tabFrame_context;
    Tab_Timeline tl_tabFrame_context;
    ProgressDialog dialog;
    LoadManager load;
    String page;

    public JsonDownLoad(Context context, String page, String url) {

        // TODO Auto-generated constructor stub
        if (page.equalsIgnoreCase("Tab_MakeGif") == true) {
            this.pj_tabFrame_context = (Tab_MakeGif) context;
            load = new LoadManager(url);
            this.page = page;
        }

        if (page.equalsIgnoreCase("Tab_Timeline") == true) {
            this.tl_tabFrame_context = (Tab_Timeline) context;
            load = new LoadManager(url);
            this.page = page;
        }
    }

    @Override
    protected String doInBackground(String... arg0) {
        // TODO Auto-generated method stub
        String data = load.request();
        return data;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (page.equalsIgnoreCase("Tab_MakeGif") == true) {
            dialog = new ProgressDialog(pj_tabFrame_context);
            dialog.show();
        }
        if (page.equalsIgnoreCase("Tab_Timeline") == true) {
            dialog = new ProgressDialog(tl_tabFrame_context);
            dialog.show();
        }
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        dialog.dismiss();
        if (page.equalsIgnoreCase("Tab_MakeGif") == true) {

            ArrayList<Project_ListViewItem> pj_list = new ArrayList<>();
            pj_list.removeAll(pj_list);

            try {
                JSONArray jArray = new JSONArray(result);
                JSONObject json = null;
                System.out.println("res : " + result);
                Project_ListViewItem data = null;
                for (int i = 0; i < jArray.length(); i++) {
                    json = jArray.getJSONObject(i);
                    if (json != null) {
                        data = new Project_ListViewItem();
                        data.setPj_title(json.getString("pj_title").toString());
                        data.setNum(Integer.parseInt(json.getString("pj_num").toString()));

                        pj_list.add(data);
                        pj_tabFrame_context.mDataset.add(i, data);

                    }
                }
                pj_tabFrame_context.pj_RecyclerView.invalidate();


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        if (page.equalsIgnoreCase("Tab_Timeline") == true) {

            ArrayList<TimeLine_ListViewItem> tl_list = new ArrayList<>();
            tl_list.removeAll(tl_list);

            try {
                JSONArray jArray = new JSONArray(result);
                JSONObject json = null;
                System.out.println("count : " + jArray.length());
                TimeLine_ListViewItem data = null;
                for (int i = 0; i < jArray.length(); i++) {
                    json = jArray.getJSONObject(i);
                    if (json != null) {
                        data = new TimeLine_ListViewItem();
                        data.setTl_content(json.getString("umzzal_content").toString());
                        data.setTl_date(json.getString("umzzal_date").toString());
                        data.setTl_picname(json.getString("umzzal_picname").toString());
                        data.setTl_dir(json.getString("umzzal_dir").toString());

                        tl_list.add(data);
                        tl_tabFrame_context.mDataset.add(i, data);
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
