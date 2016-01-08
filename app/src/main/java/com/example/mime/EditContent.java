package com.example.mime;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class EditContent extends Activity implements View.OnClickListener {

    ImageButton finBtn, backBtn;
    Intent intent;
    static EditText content;
    static String tmp_content;
    TextView editText_id;
    GifImageAdapter adapter;

    AnimatedGifEncoder gifEncoder;
    ByteArrayOutputStream bos;

    GifView gv;

    boolean isPlayingGif = false;

    String upLoadServerUri = null;
    int serverResponseCode = 0;
    static String gif_name;
    static String temp_date;

    File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittext);
        intent = this.getIntent();
        gif_name = intent.getStringExtra("gif_name");

        backBtn = (ImageButton) findViewById(R.id.editText_backBtn);
        findViewById(R.id.editText_backBtn).setOnClickListener(this);
        finBtn = (ImageButton) findViewById(R.id.editText_finBtn);
        findViewById(R.id.editText_finBtn).setOnClickListener(this);
        content = (EditText) findViewById(R.id.editText_text);
        editText_id = (TextView) findViewById(R.id.editText_id);
        editText_id.setText("@ddoni");


        System.out.println("gifname : " + gif_name);
        ImageView v = (ImageView) findViewById(R.id.edittext_imageview);
        Glide.with(this).load(GetFilePath() + gif_name + ".gif").into(v);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        temp_date = df.format(new Date());

        filePath = new File(GetFilePath(), gif_name + ".gif");


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        SendData send;
        send = new SendData();
        switch (v.getId()) {
            case R.id.editText_backBtn:
                intent = new Intent(this, EditPics.class);
                startActivity(intent);
                finish();
                break;

            case R.id.editText_finBtn:
                tmp_content = content.getText().toString();
                (new UserThread()).start();

                String url = "http://tadalab.dothome.co.kr/core/api_test.php?method=uploadUmzzalDB&dir=umzzal/&date=20151109160630&content="
                        + content.getText().toString() + "&picnamce=" + gif_name + ".gif";
                System.out.println("url : " + url);
                send.execute(url);
                break;

        }

    }

    private class UserThread extends Thread {

        public UserThread() {
        }
        @Override
        public void run() {

            String fileName = "/sdcard/Pictures/MIME/" + gif_name + ".gif";
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            if (!filePath.isFile()) {

                Log.e("uploadFile", "Source File not exist :" + "/sdcard/Pictures/MIME/" + gif_name + ".gif");

            } else {
                try {
                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    URL url = new URL("http://tadalab.dothome.co.kr/core/api_test.php?method=uploadUmzzal");

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName
                            + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                    if (serverResponseCode == 200) {

                        System.out.println("code 200 : complete");
                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    ex.printStackTrace();

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    e.printStackTrace();
                    System.out.println("Upload file server Exception : " + e.getMessage());

                }

                System.out.println("end : " + serverResponseCode);

                finish();
            } // End else block

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

class SendData extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... urls) {
        // TODO Auto-generated method stub
        BufferedReader reader = null;

        System.out.println("send");
        DefaultHttpClient client = new DefaultHttpClient();
        String line;
        String rs = "";
        // // ������ ����
        try {
            System.out.println("111111111");
            String cont, name, tdate = null;
            System.out.println("2222222222");
            cont = URLEncoder.encode(EditContent.tmp_content, "utf-8");
            System.out.println("333333333333");
            name = URLEncoder.encode(EditContent.gif_name + ".gif", "utf-8");
            System.out.println("444444444");
            tdate = URLEncoder.encode(EditContent.temp_date, "utf-8");
            System.out.println("55555555555555");
            String url = "http://tadalab.dothome.co.kr/core/api_test.php?method=uploadUmzzalDB&dir=umzzal/&date="
                    + tdate + "&content=" + cont + "&picname=" + name;

            HttpPost post = new HttpPost(url);
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            HttpConnectionParams.setSoTimeout(params, 3000);

            HttpResponse response = client.execute(post);

            BufferedReader bufreader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            while ((line = bufreader.readLine()) != null)
                rs += line;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }

}