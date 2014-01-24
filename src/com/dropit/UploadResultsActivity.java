package com.dropit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UploadResultsActivity extends Activity {

	private TextView uploadTxt;
	private Typeface typeface;
	private TextView downloadTxt;
	private LinearLayout downloadBtn;
	private LinearLayout uploadBtn;
	private TextView statusTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upload_results);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		init();
	}
	
	private void init(){
		uploadTxt = (TextView)findViewById(R.id.uruloadTxt);
		uploadTxt.setTypeface(typeface);
		downloadTxt = (TextView)findViewById(R.id.urdownloadTxt);
		downloadTxt.setTypeface(typeface); 
		
		statusTxt = (TextView)findViewById(R.id.urfileNameTxt);
		statusTxt.setTypeface(typeface);
		
		downloadBtn = (LinearLayout)findViewById(R.id.uruDownloadBtn);
		downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(UploadResultsActivity.this,
						DownloadActivity.class);
				startActivity(in);
			}
		});
		
		uploadBtn = (LinearLayout)findViewById(R.id.urdownuploadBtn);
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(UploadResultsActivity.this,
						UploadActivity.class);
				startActivity(in);
			}
		});
	}


}
