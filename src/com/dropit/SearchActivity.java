package com.dropit;

import java.io.File;
import java.util.ArrayList;

import com.dropit.DownloadActivity.downloadTask;

import android.os.Bundle;
import android.os.Environment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends ListActivity {

	private Typeface typeface;
	private ArrayList<String> resultList = new ArrayList<String>();
	private DownloadHandler downloadHandler;
	private ImageView sideLoader;
	private TextView listNullText;
	private TextView uploadText;
	private TextView dwText;
	private LinearLayout uploadBtn;
	private LinearLayout downloadBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		downloadHandler = new DownloadHandler();

		Bundle extras = getIntent().getExtras();

		if (extras != null) {

			boolean listnull = extras.getBoolean("list_null");
			
			if (!listnull) {
				String r = extras.getString("list");
				String[] list = r.split(",");

				for (int i = 0; i < list.length; i++) {
					resultList.add(list[i]);
				}
			}
			else{
				listNullText = (TextView) findViewById(R.id.listNullText);
				listNullText.setTypeface(typeface);
				listNullText.setVisibility(View.VISIBLE);
			}
		}
		init();
	}

	private void init() {
		uploadText = (TextView) findViewById(R.id.ssuloadTxt);
		uploadText.setTypeface(typeface); 
		dwText = (TextView) findViewById(R.id.ssdownloadTxt);
		dwText.setTypeface(typeface); 

		downloadBtn = (LinearLayout) findViewById(R.id.ssuDownloadBtn);
		downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(SearchActivity.this,
						DownloadActivity.class);
				startActivity(in);
			}
		});

		uploadBtn = (LinearLayout) findViewById(R.id.ssdownuploadBtn);
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(SearchActivity.this,
						UploadActivity.class);
				startActivity(in);
			}
		});
		
		sideLoader = (ImageView) findViewById(R.id.sideuploadsImg);
		setListAdapter(new FileArrayAdapter(SearchActivity.this, resultList));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		makeFileDir();
		sideLoader.setVisibility(View.VISIBLE);
		rotateAnimation(sideLoader);
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, "Please wait while downloading " + selectedValue,
				Toast.LENGTH_SHORT).show();
		downloadHandler.downloadFile(SearchActivity.this, selectedValue);
	}

	public class FileArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> values;

		public FileArrayAdapter(Context context, ArrayList<String> values) {
			super(context, R.layout.list_item, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.list_item, parent, false);

			TextView tv = (TextView) rowView.findViewById(R.id.listItemText);
			tv.setTypeface(typeface);
			tv.setText(values.get(position));

			return rowView;
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
