package com.example.mime;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class TabMain extends TabActivity {
    TabHost th;
    TabHost.TabSpec spec;
    Intent i;

    //Gcm set
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    public static final String PROPERTY_REG_ID = "registration_id";

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "ICELANCER";

    String SENDER_ID = "Your-Sender-ID";

    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;

    String regid;

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


        // GCM
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this); //GCM 클래스의 인스턴스를 생성한다.
            regid = getRegistrationId(context); // 기존에 발급받은 등록 아이디를 가져온다.

            if (regid.isEmpty()) {
                registerInBackground(); // 기존에 발급된 등록 아이디가 없으면 registerInBackground 메서드를 호출해 GCM 서버에 발급을 요청한다.
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }


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

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("ICELANCER", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID); //gcm.register 메서드가 호출되면 등록이 완료된다.
                    msg = "Device registered, registration ID=" + regid;

                    // 서버에 발급받은 등록 아이디를 전송한다.
                    // 등록 아이디는 서버에서 앱에 푸쉬 메시지를 전송할 때 사용된다.
                    sendRegistrationIdToBackend();

                    // 등록 아이디를 저장해 등록 아이디를 매번 받지 않도록 한다.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                System.out.println("push" + msg + "\n");
            }

        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regid) {  //SharedPreferences에 발급받은 등록 아이디를 저장해 등록 아이디를 여러 번 받지 않도록 하는데 사용한다.
        final SharedPreferences prefs = getGCMPreferences(context);

        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void sendRegistrationIdToBackend() { //등록 아이디를 서드 파티 서버(앱이랑 통신하는 서버에 전달한다. 서드 파티 서버는 이 등록 아이디를 사용자마다 따로 저장해두었다가 특정 사용자에게 푸쉬 메시지를 전송할 때 사용한다.
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context); //이전에 저장해둔 등록 아이디를 SharedPreferences에서 가져온다.
        String registrationId = prefs.getString(PROPERTY_REG_ID, ""); //저장해둔 등록 아이디가 없으면 빈 문자열을 반환한다.
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        // 앱이 업데이트 되었는지 확인하고, 업데이트 되었다면 기존 등록 아이디를 제거한다.
        // 새로운 버전에서도 기존 등록 아이디가 정상적으로 동작하는지를 보장할 수 없기 때문이다.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) { //이전에 등록 아이디를 저장한 앱의 버전과 현재 버전을 비교해 버전이 변경되었으면 빈 문자열을 반환한다.
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(TabMain.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

}
