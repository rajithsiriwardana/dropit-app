package com.dropit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends Activity {

	private LinearLayout uploadBtn;
	private TextView uploadText;
	private ImageView dwloadIconImg;
	private Typeface typeface;
	private TextView introTxt;
	private Button downloadBtn;
	private EditText fileNameEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download);
		typeface=Typeface.createFromAsset(getAssets(),"fonts/hm.ttf");
		init();
	}
	
	private void init(){
		
		uploadText = (TextView)findViewById(R.id.uloadTxt);
		uploadText.setTypeface(typeface);
		
		dwloadIconImg = (ImageView)findViewById(R.id.dmIconImg);
		
		introTxt = (TextView)findViewById(R.id.EdittEXTinto);
		introTxt.setTypeface(typeface);
		
		downloadBtn = (Button)findViewById(R.id.downloadBtntn);
		downloadBtn.setTypeface(typeface);
		
		fileNameEditText = (EditText)findViewById(R.id.fileNameEditText);
		fileNameEditText.setTypeface(typeface);
		
		downloadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("Pahan","sdsdsd");
				new downloadTask().execute();
			}
		});
		
		uploadBtn = (LinearLayout)findViewById(R.id.downuploadBtn);
		uploadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent in = new Intent(DownloadActivity.this,UploadActivity.class);
				startActivity(in);
			}
		});
	}
	
	
	class downloadTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			dwloadIconImg.setImageDrawable(getResources().getDrawable(R.drawable.loading));
			downloadBtn.setEnabled(false);
			introTxt.setText("Please wait while downloading your file.. ");
			fileNameEditText.setVisibility(View.GONE);
			downloadBtn.setText("Downloading.....");
			rotateAnimation(dwloadIconImg);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dwloadIconImg.clearAnimation();
			fileNameEditText.setVisibility(View.VISIBLE);
			downloadBtn.setEnabled(true);
			downloadBtn.setText("Download and Save");
			dwloadIconImg.setImageDrawable(getResources().getDrawable(R.drawable.download2));
			Toast.makeText(DownloadActivity.this, "downloading finished..", Toast.LENGTH_SHORT).show();
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
