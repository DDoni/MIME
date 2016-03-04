package com.example.mime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GifCamera extends Activity implements View.OnClickListener {
    String TAG = "CAMERA";
    private Context mContext = this;
    private Camera mCamera;
    private CameraPreview mPreview;
    ImageButton backBtn, nextBtn, changeBtn, albumBtn;
    TextView shutCount;
    private boolean isPhotoTaken = false;

    static ArrayList<Bitmap> image_temp;
    FrameLayout preview;
    static int count = 0;
    Intent intent;
    private FrameLayout frame;

    // 연속 촬영 필요 변수 정리 - st
    // 시작 위치 저장 변수
    private float mLastMotionX = 0;
    private float mLastMotionY = 0;

    // 터치 move로 일정범위 벗어나면 취소하기 위한 값
    private int mTouchSlop;

    // Long Click을 위한 변수
    private boolean mHasPerformedLongPress;
    private CheckForLongPress mPendingCheckForLongPress;
    private Handler mHandler = null;
    // 연속 촬영 필요 변수 정리 - ed

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("@@@onCreate@@@");
        image_temp = new ArrayList<Bitmap>();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.gifcamera);
        mContext = this;

        if (checkCameraHardware(mContext)) {
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(this, mCamera);
            preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }
        count = 0;

        frame = (FrameLayout) findViewById(R.id.camera_preview);
        findViewById(R.id.camera_preview).setOnClickListener(this);
        backBtn = (ImageButton) findViewById(R.id.gifCamera_backBtn);
        findViewById(R.id.gifCamera_backBtn).setOnClickListener(this);
        nextBtn = (ImageButton) findViewById(R.id.gifCamera_nextBtn);
        findViewById(R.id.gifCamera_nextBtn).setOnClickListener(this);
        changeBtn = (ImageButton) findViewById(R.id.gifCamera_change);
        findViewById(R.id.gifCamera_change).setOnClickListener(this);
        albumBtn = (ImageButton) findViewById(R.id.gifCamera_album);
        findViewById(R.id.gifCamera_album).setOnClickListener(this);
        shutCount = (TextView) findViewById(R.id.gifCamera_count);
        shutCount.setText("0장");

        mHandler = new Handler();
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        mCamera.startPreview();

        frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        Log.d("CLICK", "ACTION_DOWN");
                        mLastMotionX = event.getX();
                        mLastMotionY = event.getY(); // 시작 위치 저장

                        mHasPerformedLongPress = false;
                        postCheckForLongClick(250); // LongClick Message set - 150

                        break;

                    case MotionEvent.ACTION_MOVE:

                        Log.d("CLICK", "ACTION_MOVE");

                        if (mHasPerformedLongPress == true) {
                            CameraPreview.mCheck = true;
                            mCamera.setPreviewCallback(mPreview);
                            count++;
                            System.out.println("long / shut count : " + count);
                            shutCount.setText(image_temp.size() + 1 + "장");
                        }

                        break;

                    case MotionEvent.ACTION_CANCEL:
                        if (!mHasPerformedLongPress) {
                            removeLongPressCallback();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d("CLICK", "ACTION_UP");
                        if (!mHasPerformedLongPress) {
                            // LongClick을 처리하지 않았으면 제거
                            removeLongPressCallback();
                            // ShortClick 처리 루틴
                            performOneClick();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.i(TAG, "Number of available camera : " + Camera.getNumberOfCameras());
            return true;
        } else {
            Toast.makeText(mContext, "No camera found!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static Camera getCameraInstance() {

        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("@@@OnPause@@@");
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
        }
        if (isPhotoTaken)
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));

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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.gifCamera_backBtn:
                finish();
                break;
            case R.id.gifCamera_nextBtn:
                System.out.println("next");
                if (image_temp.size() < 2)
                    Toast.makeText(this, "2장 이상 촬영하세요.", Toast.LENGTH_SHORT).show();
                else {
                    intent = new Intent(this, EditPics.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.gifCamera_change:
                System.out.println("Camera Change");
                int cameraCount = 0;
                break;
            case R.id.gifCamera_album:
                System.out.println("Album");
                break;
        }
    }


    // LongClick 처리 Runnable
    class CheckForLongPress implements Runnable {
        public void run() {
            if (performLongClick()) {
                mHasPerformedLongPress = true;
            }
        }

    }

    // LongClick 처리 설정을 위한 함수
    private void postCheckForLongClick(int delayOffset) {
        mHasPerformedLongPress = false;
        if (mPendingCheckForLongPress == null) {
            mPendingCheckForLongPress = new CheckForLongPress();
        }

        mHandler.postDelayed(mPendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - delayOffset);
        // 여기서 시스템의 getLongPresTimeout() 후에 Message 수행
        // 추가 delay가 필요한 경우를 위해서 파라미터로 조절 가능
    }

    /**
     * Remove the longpress detection timer
     * 중간에 취소하는 용도
     */
    private void removeLongPressCallback() {
        if (mPendingCheckForLongPress != null) {
            mHandler.removeCallbacks(mPendingCheckForLongPress);
        }
    }

    public boolean performLongClick() {
        // 실제 LongClick 처리하는 부분
        Log.d("CLICK", "Long Click OK@@@@@@@");

        return true;
    }

    private void performOneClick() {
        Log.d("CLICK", "One Click OK");
        CameraPreview.mCheck = true;
        mCamera.setPreviewCallback(mPreview);
        count++;
        System.out.println("short / shut count : " + count);
        shutCount.setText(image_temp.size() + 1 + "장");

    }
}

