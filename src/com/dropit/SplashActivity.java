package com.dropit;

import java.io.File;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Window;
import android.widget.TextView;

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
		new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, UploadActivity.class);
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


}
