package com.example.mime;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;

public class TabMain extends TabActivity {

    TabHost th;
    TabHost.TabSpec spec;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_frame);

        th = getTabHost();

        ImageView tab1_widget = new ImageView(this);
        tab1_widget.setImageResource(R.drawable.tab1_click);
        tab1_widget.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView tab2_widget = new ImageView(this);
        tab2_widget.setImageResource(R.drawable.tab2_click);
        tab2_widget.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView tab3_widget = new ImageView(this);
        tab3_widget.setImageResource(R.drawable.tab3_click);
        tab3_widget.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView tab4_widget = new ImageView(this);
        tab4_widget.setImageResource(R.drawable.tab4_click);
        tab4_widget.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView tab5_widget = new ImageView(this);
        tab5_widget.setImageResource(R.drawable.tab5_click);
        tab5_widget.setScaleType(ImageView.ScaleType.FIT_XY);

        i = new Intent(this, Tab_Timeline.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = th.newTabSpec("tab1").setIndicator(tab1_widget).setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        th.addTab(spec);

        i = new Intent(this, Tab_Notice.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = th.newTabSpec("tab2").setIndicator(tab2_widget).setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        th.addTab(spec);

        i = new Intent(this, Tab_MakeGif.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = th.newTabSpec("tab3").setIndicator(tab3_widget).setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        th.addTab(spec);

        i = new Intent(this, Tab_Dm.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = th.newTabSpec("tab4").setIndicator(tab4_widget).setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        th.addTab(spec);

        i = new Intent(this, Tab_MyPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = th.newTabSpec("tab5").setIndicator(tab5_widget).setContent(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        th.addTab(spec);
        th.setCurrentTab(2);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub

        super.onStop();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        // TODO Auto-generated method stub
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
}
