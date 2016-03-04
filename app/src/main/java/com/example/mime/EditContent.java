package com.example.mime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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

public class EditContent extends Activity implements View.OnClickListener {

    ImageButton finBtn, backBtn;
    Intent intent;
    static EditText content;
    static String tmp_content = "";
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

    // ImageView set - st
    ImageView imageView;
    int duration = 240;
    static int speed_temp = 240;
    // ImageView set - ed

    ProgressDialog dialog;
    int pos_dialog = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittext);


        backBtn = (ImageButton) findViewById(R.id.editText_backBtn);
        findViewById(R.id.editText_backBtn).setOnClickListener(this);
        finBtn = (ImageButton) findViewById(R.id.editText_finBtn);
        findViewById(R.id.editText_finBtn).setOnClickListener(this);
        content = (EditText) findViewById(R.id.editText_text);
        editText_id = (TextView) findViewById(R.id.editText_id);
        editText_id.setText("@ddoni");

        // imageView set - st
        imageView = (ImageView) findViewById(R.id.edittext_imageview);
        imageView.setBackgroundDrawable(EditPics.ani);
        EditPics.ani.start();
//        makeGif();
        // imageView set - ed


//        SendData send = new SendData();
//        String url = "http://tadalab.dothome.co.kr/core/api_test.php?method=uploadUmzzalDB&dir=umzzal/&date=20151109160630&content="
//                + content.getText().toString() + "&picnamce=" + gif_name + ".gif";
//        System.out.println("url : " + url);
//        send.execute(url);
//
//        System.out.println("gifname : " + gif_name);
//        ImageView v = (ImageView) findViewById(R.id.edittext_imageview);
//        Glide.with(this).load(GetFilePath() + gif_name + ".gif").into(v);
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
//        temp_date = df.format(new Date());
//
//        filePath = new File(GetFilePath(), gif_name + ".gif");
    }

    public void makeGif() {
        gifEncoder = new AnimatedGifEncoder();
        bos = new ByteArrayOutputStream();
        gifEncoder.start(bos);
        gifEncoder.setDelay(EditPics.ani.getDuration(0));
        gifEncoder.setRepeat(0);

        BitmapDrawable bitmapDrawble;

        for (int i = 0; i < GifCamera.image_temp.size(); i++) {
            bitmapDrawble = (BitmapDrawable) EditPics.ani.getFrame(i);
            gifEncoder.addFrame(bitmapDrawble.getBitmap());
        }

        gifEncoder.finish();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.KOREA);
        gif_name = df.format(new Date());

        df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        temp_date = df.format(new Date());


        File filePath = new File(GetFilePath(), gif_name + ".gif");
        FileOutputStream ops;
        try {
            ops = new FileOutputStream(filePath);
            ops.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
            System.out.println("@@@@@Not found");

        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("@@@@@IO error");
        }
//        Glide.with(this).load(GetFilePath() + gif_name + ".gif").into(imageView);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.editText_backBtn:
                intent = new Intent(this, EditPics.class);
                startActivity(intent);
                finish();
                break;

            case R.id.editText_finBtn:

                tmp_content = content.getText().toString();
                if (dialog != null)
                    return;
//                (new UserThread()).start();
                String sUrl = "http://tadalab.dothome.co.kr/core/api_test.php?method=uploadUmzzalDB&dir=umzzal/&date=" + temp_date + "&content="
                        + content.getText().toString() + "&picname=" + gif_name + ".gif";
                System.out.println("url : " + sUrl);

                new AsyncProgressDialog().execute(sUrl);
                break;

        }

    }

    // 파일 생성 및 서버 파일 업로드
    public static synchronized String GetFilePath() {
        String sdcard = Environment.getExternalStorageState();
        System.out.println("sdcard : " + sdcard);
        File file = null;

        if (!sdcard.equals(Environment.MEDIA_MOUNTED)) {
            // SDcard not Monunted
            System.out.println("nope!!!!!");
            file = Environment.getRootDirectory();
        } else {
            System.out.println("yeah!!!!!");
            // SDcard Mounted
            file = Environment.getExternalStorageDirectory();
        }
        String dir = file.getAbsolutePath() + String.format("/MIME/");
        String path = file.getAbsolutePath();

        file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    class AsyncProgressDialog extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditContent.this);
            dialog.setTitle("upload");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("UpLoading");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            System.out.println("1111111111111");
            gifEncoder = new AnimatedGifEncoder();
            bos = new ByteArrayOutputStream();
            gifEncoder.start(bos);
            gifEncoder.setDelay(EditPics.speed_temp);
            gifEncoder.setRepeat(0);
            System.out.println("22222222222");

            BitmapDrawable bitmapDrawble;

            for (int i = 0; i < GifCamera.image_temp.size(); i++) {
                pos_dialog = (int) ((float) i / GifCamera.image_temp.size() * 100);
                publishProgress(pos_dialog);

                bitmapDrawble = (BitmapDrawable) EditPics.ani.getFrame(i);
                gifEncoder.addFrame(bitmapDrawble.getBitmap());
            }

            gifEncoder.finish();
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.KOREA);
            gif_name = df.format(new Date());

            df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
            temp_date = df.format(new Date());


            File filePath = new File(GetFilePath(), gif_name + ".gif");
            FileOutputStream ops;
            try {
                ops = new FileOutputStream(filePath);
                ops.write(bos.toByteArray());
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
                // TODO: handle exception
            }
            System.out.println("55555555555555");
            // db 전송

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
                System.out.println("6666666666666");
                System.out.println("end : " + serverResponseCode);

            }

            // TODO Auto-generated method stub
            // DB 전송
            BufferedReader reader = null;

            System.out.println("7777777777777777");
            DefaultHttpClient client = new DefaultHttpClient();
            String line;
            String rs = "";

            try {

                String cont, name, tdate = null;
                cont = URLEncoder.encode(EditContent.tmp_content, "utf-8");
                name = URLEncoder.encode(EditContent.gif_name + ".gif", "utf-8");
                tdate = URLEncoder.encode(EditContent.temp_date, "utf-8");

                String url = "http://tadalab.dothome.co.kr/core/api_test.php?method=uploadUmzzalDB&dir=umzzal/&date="
                        + tdate + "&content=" + cont + "&picname=" + name;
                System.out.println("888888888888");
                HttpPost post = new HttpPost(url);
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 3000);
                HttpConnectionParams.setSoTimeout(params, 3000);

                HttpResponse response = client.execute(post);
                System.out.println("999999999");
                BufferedReader bufreader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                while ((line = bufreader.readLine()) != null)
                    rs += line;
                System.out.println("1010101010");
            } catch (Exception e) {
                e.printStackTrace();
            }

            publishProgress(100);
            return "Complete Upload";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            dialog = null;
            Toast.makeText(EditContent.this, result, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
