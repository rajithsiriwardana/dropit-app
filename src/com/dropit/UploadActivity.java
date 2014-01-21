package com.dropit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UploadActivity extends Activity {

	private Button uploadBtn;
	private TextView fileNameText;
	private TextView downloadTxt;
	private Typeface typeface;
	private LinearLayout fileSelectBtn;
	private LinearLayout downloadBtn;
	private ImageView fileIconImg;
	private UploadHandler uploadHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upload);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		uploadHandler = new UploadHandler();
		init();
	}

	private void init() {
		uploadBtn = (Button) findViewById(R.id.uploadBtn);
		uploadBtn.setTypeface(typeface);

		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new uploadTask().execute();
			}
		});

		fileNameText = (TextView) findViewById(R.id.fileNameTxt);
		fileNameText.setTypeface(typeface);

		downloadTxt = (TextView) findViewById(R.id.downloadTxt);
		downloadTxt.setTypeface(typeface);

		fileIconImg = (ImageView) findViewById(R.id.fileIconImg);

		fileSelectBtn = (LinearLayout) findViewById(R.id.fileSelectBtnLayout);
		fileSelectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
				 fileintent.setType("file/*"); 
				try {
					startActivityForResult(fileintent, 123);
				} catch (Exception e) {
					Log.d("Pahan",
							"No activity can handle picking a file. Showing alternatives.");
				}

			}
		});

		downloadBtn = (LinearLayout) findViewById(R.id.uDownloadBtn);
		downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(UploadActivity.this,
						DownloadActivity.class);
				startActivity(in);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data == null)
			return;
		switch (requestCode) {
		case 123:
			if (resultCode == RESULT_OK) {
				String filePath = data.getData().getPath();
				fileNameText.setText(filePath);
				fileIconImg.setImageDrawable(getResources().getDrawable(
						R.drawable.file));
				uploadBtn.setText("Upload and Share");
				uploadBtn.setVisibility(View.VISIBLE);
			}
		}
	}

	class uploadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			uploadHandler.uploadFile(fileNameText.getText().toString());

			return null;
		}

		@Override
		protected void onPreExecute() {
			fileIconImg.setImageDrawable(getResources().getDrawable(
					R.drawable.loading));
			uploadBtn.setEnabled(false);
			uploadBtn.setText("Uploading.....");
			rotateAnimation(fileIconImg);
		}

		@Override
		protected void onPostExecute(Void result) {
			fileIconImg.clearAnimation();
			uploadBtn.setVisibility(View.GONE);
			uploadBtn.setEnabled(true);
			fileIconImg.setImageDrawable(getResources().getDrawable(
					R.drawable.upload_ok));
		}

	}

	public void rotateAnimation(View v) {

		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		v.setAnimation(rotateAnimation);
	}

}
