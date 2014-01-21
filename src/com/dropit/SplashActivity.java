package com.dropit;

import java.io.File;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private TextView logonText;
	private Typeface typeface;
	private TextView loadingStatusText;
	private String[] statusTexts = new String[] { "Checking Connectivity ...",
			"Finding a Server ...", "Connecting to Server ...",
			"Intializing Sync ...", "Good to Go" };
	private TextView errorText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		init();
		new InitLoadingTask().execute();
	}

	private void init() {
		// logonText = (TextView)findViewById(R.id.logoNName);
		// logonText.setTypeface(typeface);
		loadingStatusText = (TextView) findViewById(R.id.loadingStatusText);
		loadingStatusText.setTypeface(typeface);
		errorText = (TextView) findViewById(R.id.errorText);
		errorText.setTypeface(typeface);
	}

	class InitLoadingTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				publishProgress(0);
				makeFileDir();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			setProgressStatus(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent in = new Intent(SplashActivity.this, UploadActivity.class);
			startActivity(in);
		}

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

	private void setProgressStatus(int val) {

		loadingStatusText.setText(statusTexts[val]);
	}

}
