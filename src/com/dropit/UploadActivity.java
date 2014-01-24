package com.dropit;

import java.net.URISyntaxException;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

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
				fileintent.setType("*/*");
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

		if (data == null) {
			return;
		}
		switch (requestCode) {
		case 123:
			if (resultCode == RESULT_OK) {
				fileNameText.setText(getPath(UploadActivity.this,
						data.getData()));
				fileIconImg.setImageDrawable(getResources().getDrawable(
						R.drawable.file));
				uploadBtn.setText("Upload and Share");
				uploadBtn.setVisibility(View.VISIBLE);
			}
		}
	}

	private String getPath(Context context, Uri uri) {

		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {

			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return "";
	}

	class uploadTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean s = uploadHandler.uploadFile(UploadActivity.this,
					fileNameText.getText().toString());
			return s;
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
		protected void onPostExecute(Boolean result) {
			// fileIconImg.clearAnimation();
			// uploadBtn.setVisibility(View.GONE);
			// uploadBtn.setEnabled(true);
			// if(result){
			// fileIconImg.setImageDrawable(getResources().getDrawable(
			// R.drawable.upload_ok));
			// }
			// else{
			// fileIconImg.setImageDrawable(getResources().getDrawable(
			// R.drawable.upload_notok));
			// Toast.makeText(UploadActivity.this, "Error while uploading File",
			// Toast.LENGTH_LONG).show();
			// }
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
