package com.dropit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private static int SPLASH_TIME_OUT = 3000;
	private Typeface typeface;
	private TextView loadingStatusText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		init();
		setIPandPORT();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this,
						UploadActivity.class);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	private void init() {
		loadingStatusText = (TextView) findViewById(R.id.loadingStatusText);
		loadingStatusText.setTypeface(typeface);
		makeFileDir();

	}

	private void makeFileDir() {

		File sdDir = Environment.getExternalStorageDirectory();
		try {
			File f = new File(sdDir.getCanonicalPath() + "/" + Utils.DIR_NAME);
			if (!f.isDirectory()) {
				boolean success = f.mkdir();
			}
		} catch (Exception e) {

		}
	}

	private void setIPandPORT() {
		String FILENAME = Utils.CONFIG_FILENAME;

		try {
			FileInputStream fis = openFileInput(FILENAME);

			InputStreamReader inputStreamReader = new InputStreamReader(fis);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			
		
			fis.close();
			String out = sb.toString();
			String[] ipandport = out.split(":");
			if (ipandport.length == 2) {
				Utils.IP = ipandport[0];
				Utils.PORT = Integer.parseInt(ipandport[1]);
			}
			
		} catch (Exception e) {
			Toast.makeText(SplashActivity.this,
					"Error while reading configurations", Toast.LENGTH_LONG).show();
		}
	}

}
