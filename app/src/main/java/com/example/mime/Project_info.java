package com.example.mime;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.GridViewWithHeaderAndFooter;


public class Project_info extends Activity implements View.OnClickListener {

    ImageButton pjinfo_backBtn, pjinfo_finBtn;
    Intent intent;

    Button st_date_btn, alarm_btn;

    EditText pjinfo_title, pjinfo_text, cycleValue;

    DatePickerDialog datePick;
    TimePickerDialog timePick;


    ProjectInfo_GifImageAdapter adapter;
    DisplayMetrics mMetrics;

    CheckBox cycleCheck;


    GridViewWithHeaderAndFooter gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_info);

        pjinfo_title = (EditText) findViewById(R.id.pjinfo_title);
        pjinfo_text = (EditText) findViewById(R.id.pjinfo_content);

        pjinfo_backBtn = (ImageButton) findViewById(R.id.pjinfo_backBtn);
        findViewById(R.id.pjinfo_backBtn).setOnClickListener(this);
        pjinfo_finBtn = (ImageButton) findViewById(R.id.pjinfo_saveBtn);
        findViewById(R.id.pjinfo_saveBtn).setOnClickListener(this);

        st_date_btn = (Button) findViewById(R.id.pjinfo_dateBtn);
        findViewById(R.id.pjinfo_dateBtn).setOnClickListener(this);
        alarm_btn = (Button) findViewById(R.id.pjinfo_alarm);
        findViewById(R.id.pjinfo_alarm).setOnClickListener(this);

        long now = System.currentTimeMillis();


        cycleCheck = (CheckBox) findViewById(R.id.pjinfo_cycle_check);
        findViewById(R.id.pjinfo_cycle_check).setOnClickListener(this);
        cycleValue = (EditText) findViewById(R.id.pjinfo_cycle_value);

        if (cycleCheck.isChecked() == false)
            cycleValue.setEnabled(false);
        else
            cycleValue.setEnabled(true);


        Date date = new Date(now);

        SimpleDateFormat curYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat curMonth = new SimpleDateFormat("MM");
        SimpleDateFormat curDay = new SimpleDateFormat("dd");
        SimpleDateFormat curHour = new SimpleDateFormat("HH");
        SimpleDateFormat curMinute = new SimpleDateFormat("mm");

        String strYear = curYear.format(date);
        String strMonth = curMonth.format(date);
        String strDay = curDay.format(date);
        String strHour = curHour.format(date);
        String strMinute = curMinute.format(date);

        datePick = new DatePickerDialog(this, dateListener, Integer.parseInt(strYear), Integer.parseInt(strMonth), Integer.parseInt(strDay));
        timePick = new TimePickerDialog(this, timeListener, Integer.parseInt(strHour), Integer.parseInt(strMinute), true);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        adapter = new ProjectInfo_GifImageAdapter(this, mMetrics);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        View headerView = layoutInflater.inflate(R.layout.gridview_header, null);
        headerView.findViewById(R.id.gridAddBtn).setOnClickListener(this);

        gridView.addFooterView(headerView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(gridviewOnItemClickListener);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {

            System.out.println("pos : " + position);
        }
    };
    public
    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            st_date_btn.setText(year + "." + monthOfYear + "." + dayOfMonth);
        }
    };
    public TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            alarm_btn.setText(hourOfDay + " 시 " + minute + " 분");
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.pjinfo_backBtn:
                // intent = new Intent(this, Tab_MakeGif.class);
                // startActivity(intent);
                finish();
                break;

            case R.id.pjinfo_saveBtn:
                // intent = new Intent(this, Tab_MakeGif.class);
                // startActivity(intent);

                finish();
                break;

            case R.id.pjinfo_dateBtn:
                datePick.show();
                break;
            case R.id.pjinfo_alarm:
                timePick.show();
                break;

            case R.id.gridAddBtn:

                break;

            case  R.id.pjinfo_cycle_check:
                if (cycleCheck.isChecked() == false)
                    cycleValue.setEnabled(false);
                else
                    cycleValue.setEnabled(true);
                break;

        }
    }

}
