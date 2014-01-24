package com.dropit;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends ListActivity {

	private Typeface typeface;
	private TextView searchText;
	private ArrayList<String> resultList = new ArrayList<String>();
	private DownloadHandler downloadHandler;
	private ImageView sideLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		downloadHandler = new DownloadHandler();

		// Bundle extras = getIntent().getExtras();

		// if (extras != null) {
		String r = "text,dfdf,dfdfd,dfdssssssssss,aaa";// extras.getString("list");
		String[] list = r.split(",");

		for (int i = 0; i < list.length; i++) {
			Log.d("Pahan", list[i]);
			resultList.add(list[i]);
		}
		// }
		init();
	}

	private void init() {
		searchText = (TextView) findViewById(R.id.searchuloadTxt);
		searchText.setTypeface(typeface);
		
		sideLoader = (ImageView)findViewById(R.id.sideuploadsImg);
		setListAdapter(new FileArrayAdapter(SearchActivity.this, resultList));

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		sideLoader.setVisibility(View.VISIBLE);
		rotateAnimation(sideLoader);
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, "Please wait while downloading "+selectedValue, Toast.LENGTH_SHORT).show();
		//downloadHandler.downloadFile(selectedValue);
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

}
