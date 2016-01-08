package com.example.mime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class GifCamera extends Activity implements View.OnClickListener {
    String TAG = "CAMERA";
    private Context mContext = this;
    private Camera mCamera;
    private CameraPreview mPreview;
    ImageButton shutBtn, backBtn, nextBtn, changeBtn, albumBtn;
    private boolean isPhotoTaken = false;
    private boolean isFocused = false;

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

    int longCount = 0;

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
//        mCheck = (RadioButton) findViewById(R.id.focus);
//        mCheck.setChecked(false);
//        mCheck.setClickable(false);
//        mCheck.setAlpha(0.15f); // ???????????? 15-> 0.15f

//        shutBtn = (ImageButton) findViewById(R.id.gifCamera_shutBtn);
//        findViewById(R.id.gifCamera_shutBtn).setOnClickListener(this);
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
                        postCheckForLongClick(250); // LongClick Message set

                        break;

                    case MotionEvent.ACTION_MOVE:

                        Log.d("CLICK", "ACTION_MOVE");

                        if (mHasPerformedLongPress == true) {
                            mCamera.setPreviewCallback(mPreview);
                            count++;
                            System.out.println("long / shut count : " + count);
                            Toast.makeText(GifCamera.this , image_temp.size()+1 + "장", Toast.LENGTH_SHORT).show();
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
        if (isPhotoTaken)
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    class ImageSaveTask extends AsyncTask<byte[], Void, Boolean> {
        @Override
        protected Boolean doInBackground(byte[]... data) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return false;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data[0]);
                fos.close();
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isDone) {
            if (isDone) {
                Toast.makeText(mContext, "Image saved!", Toast.LENGTH_SHORT).show();
            }
        }

        private File getOutputMediaFile() {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MIME");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            Log.i(TAG, "Saved at" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
            return mediaFile;
        }
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
                intent = new Intent(this, EditPics.class);
                startActivity(intent);
                finish();
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
        mCamera.setPreviewCallback(mPreview);
        count++;
        System.out.println("short / shut count : " + count);
        Toast.makeText(GifCamera.this , image_temp.size()+1 + "장", Toast.LENGTH_SHORT).show();
    }
}

