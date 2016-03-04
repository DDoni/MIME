package com.example.mime;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class EditPics extends Activity implements View.OnClickListener {
    GridView gridView;
    ImageButton nextBtn, backBtn;
    Intent intent;

    static ImageView imageview;

    static LinearLayout sview;

    static String gif_name = null;


    // edit set
    static ArrayList<Drawable> drawble_temp;
    static ArrayList<Boolean> drawble_temp_flag;
    static ArrayList<Drawable> drawble_temp_rev;
    static ArrayList<Boolean> drawble_temp_rev_flag;
    static AnimationDrawable ani;
    int duration = 240;
    static int speed_temp = 240;
    SeekBar speedBar;
    TextView speed;

    static CheckBox rev_Check;

    // grideview set
    TextView total_Cnt;
    static TextView pick_Cnt;

    LinearLayout gridLayout;
    GifImageAdapter adapter;
    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpic);

        backBtn = (ImageButton) findViewById(R.id.editpic_backBtn);
        findViewById(R.id.editpic_backBtn).setOnClickListener(this);
        nextBtn = (ImageButton) findViewById(R.id.editpic_nextBtn);
        findViewById(R.id.editpic_nextBtn).setOnClickListener(this);

        imageview = (ImageView) findViewById(R.id.editpic_imageview);
        System.out.print("path : " + GetFilePath());

        drawble_temp = new ArrayList<Drawable>();
        drawble_temp_rev = new ArrayList<Drawable>();

        drawble_temp_flag = new ArrayList<Boolean>();
        drawble_temp_rev_flag = new ArrayList<Boolean>();

        for (int i = 0; i < GifCamera.image_temp.size(); i++) {
            drawble_temp.add(new BitmapDrawable(GifCamera.image_temp.get(i)));
            drawble_temp_rev.add(new BitmapDrawable(GifCamera.image_temp.get(GifCamera.image_temp.size() - i - 1)));

            drawble_temp_flag.add(true);
            drawble_temp_rev_flag.add(true);
        }


        ani = new AnimationDrawable();
        ani.setOneShot(false);
        for (int i = 0; i < drawble_temp.size(); i++)
            ani.addFrame(drawble_temp.get(i), duration);
        imageview.setBackgroundDrawable(ani);
        ani.start();

        // speed setting - st
        rev_Check = (CheckBox) findViewById(R.id.editpic_reverse);
        findViewById(R.id.editpic_reverse).setOnClickListener(this);

        speed = (TextView) findViewById(R.id.editpic_speed);
        speedBar = (SeekBar) findViewById(R.id.editpic_bar);
        speedBar.setMax(10);
        speedBar.setProgress(5);
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
                if (rev_Check.isChecked() == false) {
                    if (progress - 5 == 0) {
                        speed.setText("재생 속도 : 기본    ");
                        speed_temp = duration;
                        aniSetting(drawble_temp, drawble_temp_flag, speed_temp);
                    } else {
                        speed.setText("재생 속도 : " + (progress - 5) + "    ");
                        speed_temp = duration - (progress - 5) * 25;
                        aniSetting(drawble_temp, drawble_temp_flag, speed_temp);
                    }
                } else {
                    if (progress - 5 == 0) {
                        speed.setText("재생 속도 : 기본    ");
                        speed_temp = duration;
                        aniSetting(drawble_temp_rev, drawble_temp_rev_flag, speed_temp);
                    } else {
                        speed.setText("재생 속도 : " + (progress - 5) + "    ");
                        speed_temp = duration - (progress - 5) * 25;
                        aniSetting(drawble_temp_rev, drawble_temp_rev_flag, speed_temp);
                    }
                }
            }
        });
        // speed setting - ed

        // gridview setting - st
        total_Cnt = (TextView) findViewById(R.id.editpic_total_cnt);
        pick_Cnt = (TextView) findViewById(R.id.editpic_pick_cnt);

        total_Cnt.setText("Total : " + drawble_temp.size());
        pick_Cnt.setText("Pick : " + drawble_temp.size());

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);


        adapter = new GifImageAdapter(this, mMetrics, R.layout.gridview_items);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(gridviewOnItemClickListener);

//        gifEncoder = new AnimatedGifEncoder();
//        bos = new ByteArrayOutputStream();
//        gifEncoder.start(bos);
//        gifEncoder.setDelay(50);
//        gifEncoder.setRepeat(0);
//
//        for (int i = 0; i < GifCamera.image_temp.size(); i++)
//            gifEncoder.addFrame(GifImageAdapter.rotate(GifCamera.image_temp.get(i), 90));
//
//        gifEncoder.finish();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.KOREA);
//        gif_name = df.format(new Date());
//
//        File filePath = new File(GetFilePath(), gif_name + ".gif");
//        FileOutputStream ops;
//        try {
//            ops = new FileOutputStream(filePath);
//            ops.write(bos.toByteArray());
//        } catch (FileNotFoundException e) {
//
//        } catch (IOException e) {
//            // TODO: handle exception
//        }


//        imageview = (ImageView) findViewById(R.id.editpic_imageview);
//        System.out.print("path : " + GetFilePath());
//
//        Glide.with(this).load(GetFilePath() + gif_name + ".gif").into(imageview);
//        imageview.setScaleType(ImageView.ScaleType.FIT_XY);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        sview = (LinearLayout) findViewById(R.id.sview);

    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {


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
                if (ani.getNumberOfFrames() < 2)
                    Toast.makeText(this, "2장 이상 선택하세요.", Toast.LENGTH_SHORT).show();
                else {
                    intent = new Intent(this, EditContent.class);
                    intent.putExtra("ani_class", ani.getClass());

                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.editpic_reverse:
                if (rev_Check.isChecked() == true)
                    aniSetting(drawble_temp_rev, drawble_temp_rev_flag, speed_temp);
                else
                    aniSetting(drawble_temp, drawble_temp_flag, speed_temp);
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

    static public void aniSetting(ArrayList<Drawable> arry, ArrayList<Boolean> flag, int speed) {
        ani.stop();
        ani = new AnimationDrawable();

        ani.setOneShot(false);
        for (int i = 0; i < drawble_temp.size(); i++) {
            if (flag.get(i) == true)
                ani.addFrame(arry.get(i), speed);
        }
        imageview.setBackgroundDrawable(ani);

        ani.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("@@@OnPause@@@");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("@@@OnStart@@@");
    }

    @Override
    protected void onDestroy() {
        System.out.println("@@@onDestory@@@");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        System.out.println("@@@onResume@@@");
        super.onResume();
    }

    @Override
    protected void onStop() {
        System.out.println("@@@onStop@@@");
        super.onStop();
    }

}
