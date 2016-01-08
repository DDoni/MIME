package com.example.mime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadManager {

	URL url;
	HttpURLConnection conn;
	BufferedReader buffer = null;

	public LoadManager(String strUrl) {
		try {
			url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String request() {
		String data = "";
		try {
			conn.connect();
			InputStream is = conn.getInputStream();

			buffer = new BufferedReader(new InputStreamReader(is));

			StringBuffer str = new StringBuffer();

			String d = null;

			while ((d = buffer.readLine()) != null) {
				str.append(d);
			}
			data = str.toString();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return data;
	}
}