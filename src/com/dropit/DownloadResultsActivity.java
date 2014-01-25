package com.dropit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DownloadResultsActivity extends Activity {

	private TextView uploadTxt;
	private Typeface typeface;
	private TextView downloadTxt;
	private LinearLayout downloadBtn;
	private LinearLayout uploadBtn;
	private TextView statusTxt;
	private ImageView statusImg;
	private boolean status = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download_results);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		init();
		setData();
	}

	private void init() {
		uploadTxt = (TextView) findViewById(R.id.druloadTxt);
		uploadTxt.setTypeface(typeface);
		downloadTxt = (TextView) findViewById(R.id.drdownloadTxt);
		downloadTxt.setTypeface(typeface);

		statusTxt = (TextView) findViewById(R.id.drfileNameTxt);
		statusTxt.setTypeface(typeface);

		downloadBtn = (LinearLayout) findViewById(R.id.druDownloadBtn);
		downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(DownloadResultsActivity.this,
						DownloadActivity.class);
				startActivity(in);
			}
		});

		uploadBtn = (LinearLayout) findViewById(R.id.drdownuploadBtn);
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(DownloadResultsActivity.this,
						UploadActivity.class);
				startActivity(in);
			}
		});
	}

	private void setData() {
		statusImg = (ImageView) findViewById(R.id.drfileIconImg);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			status = extras.getBoolean("status");
		}

		if (!status) {
			statusImg.setImageDrawable(getResources().getDrawable(
					R.drawable.upload_notok));
			statusTxt.setText("Error while downloading file");
		}
	}

}
