package com.example.mime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class EditPics extends Activity implements View.OnClickListener {
    GridView gridView;
    ImageButton nextBtn, backBtn;
    Intent intent;

    GifImageAdapter adapter;
    DisplayMetrics mMetrics;
    ImageView imageview;

    AnimatedGifEncoder gifEncoder;
    ByteArrayOutputStream bos;

    GifView gv;
    static LinearLayout sview;
    boolean isPlayingGif = false;

    SeekBar speedBar;
    TextView speed;

    static String gif_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpic);

        backBtn = (ImageButton) findViewById(R.id.editpic_backBtn);
        findViewById(R.id.editpic_backBtn).setOnClickListener(this);
        nextBtn = (ImageButton) findViewById(R.id.editpic_nextBtn);
        findViewById(R.id.editpic_nextBtn).setOnClickListener(this);


        speed = (TextView) findViewById(R.id.editpic_speed);
        speedBar = (SeekBar) findViewById(R.id.editpic_bar);

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                if (progress < 1) {
                    progress = 1;
                    speedBar.setProgress(progress);
                }

                speed.setText("재생 속도 : " + progress);

            }
        });

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        adapter = new GifImageAdapter(this, mMetrics);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(gridviewOnItemClickListener);

        gifEncoder = new AnimatedGifEncoder();
        bos = new ByteArrayOutputStream();
        gifEncoder.start(bos);
        gifEncoder.setDelay(100);
        gifEncoder.setRepeat(0);

        for (int i = 0; i < GifCamera.image_temp.size(); i++)
            gifEncoder.addFrame(GifImageAdapter.rotate(GifCamera.image_temp.get(i), 90));

        gifEncoder.finish();

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.KOREA);
        gif_name = df.format(new Date());

        File filePath = new File(GetFilePath(), gif_name + ".gif");
        FileOutputStream ops;
        try {
            ops = new FileOutputStream(filePath);
            ops.write(bos.toByteArray());
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            // TODO: handle exception
        }
        imageview = (ImageView) findViewById(R.id.editpic_imageview);
        System.out.print("path : " + GetFilePath());
        Glide.with(this).load(GetFilePath() + gif_name + ".gif").into(imageview);
        imageview.setScaleType(ImageView.ScaleType.FIT_XY);

//		gv.setGif("/sdcard/Pictures/MIME/" + gif_name + ".gif");
//		gv.play();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        sview = (LinearLayout) findViewById(R.id.sview);

    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {

            System.out.println("pos : " + position);
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.editpic_backBtn:
                intent = new Intent(this, GifCamera.class);
                startActivity(intent);
                finish();
                break;

            case R.id.editpic_nextBtn:
                intent = new Intent(this, EditContent.class);
                intent.putExtra("gif_name", gif_name);
                startActivity(intent);

                finish();
                break;

        }
    }

    public static synchronized String GetFilePath() {
        String sdcard = Environment.getExternalStorageState();
        File file = null;

        if (!sdcard.equals(Environment.MEDIA_MOUNTED)) {
            // SDcard not Monunted
            file = Environment.getRootDirectory();
        } else {
            // SDcard Mounted
            file = Environment.getExternalStorageDirectory();
        }
        String dir = file.getAbsolutePath() + String.format("/MIME/");
        String path = file.getAbsolutePath();

        file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("path : " + path);
        return dir;
    }
}
