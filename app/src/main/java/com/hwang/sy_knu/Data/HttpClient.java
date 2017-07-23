package com.hwang.sy_knu.Data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class HttpClient extends Observable {

	final public static int HTTP_GET_TERM=98;
	final public static int HTTP_GET_SEASON=99;
	final public static int HTTP_GET_MAIN=0;
	final public static int HTTP_GET_SUB=1;
	final public static int HTTP_GET_MAJOR=2;
	final public static int HTTP_GET_SY_LIST=3;
	final public static int HTTP_GET_SAVED_SY_LIST=4;
	final public static int HTTP_GET_SY=5;

	private int type;
	private String url;
	
	public HttpClient (int type, String url) {
		
		this.type=type;
		this.url=url;

	}
	
	
	public void connect(){
		
		getHtmlSource getHtml = new getHtmlSource();
		getHtml.execute(url);
		
	}
	
	
	
	class getHtmlSource extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... urls) {

			StringBuilder Html = new StringBuilder();
			try {
				Html.setLength(0);
				
				URL url = new URL(urls[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				if (conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(conn.getInputStream(),
										"UTF-8"));
						for (;;) {
							String line = br.readLine();
							if (line == null)
								break;
							Html.append(line + "\n");
						}
						br.close();
					}
					conn.disconnect();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return Html.toString();

		}

		protected void onPostExecute(String str) {

			switch (type) {
			
			case HTTP_GET_TERM:

				setChanged();
				notifyObservers("1"+str);

				break;

			case HTTP_GET_SEASON:

				setChanged();
				notifyObservers("2"+str);

				break;

			case HTTP_GET_MAIN:				
			case HTTP_GET_SUB:
			case HTTP_GET_MAJOR:
			case HTTP_GET_SY_LIST:
			case HTTP_GET_SAVED_SY_LIST:
			case HTTP_GET_SY:
				
				setChanged();
				notifyObservers(str);
				
				break;

			default:
				break;
			}
			
		}
	}



	class takeImgData extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected void onPreExecute() {

		}

		@SuppressLint("NewApi")
		@Override
		protected Bitmap doInBackground(String... urls) {

			final AndroidHttpClient client = AndroidHttpClient
					.newInstance("Android");
			final HttpGet getRequest = new HttpGet(urls[0]);

			try {
				HttpResponse response = client.execute(getRequest);
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + urls[0]);
					return null;
				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						inputStream = entity.getContent();
						final Bitmap bitmap = BitmapFactory
								.decodeStream(inputStream);

						return bitmap;
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				getRequest.abort();
				Log.w("ImageDownloader", "Error while retrieving bitmap from "
						+ urls[0]);
			} finally {
				if (client != null) {
					client.close();
				}
			}
			return null;
		}

		protected void onPostExecute(Bitmap bmp) {
			
		}
	}
	
	
}






