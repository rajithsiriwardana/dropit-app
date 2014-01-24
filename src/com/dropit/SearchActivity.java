package com.dropit;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchActivity extends ListActivity {

	private Typeface typeface;
	private TextView searchText;
	private ArrayList<String> resultList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			String r = extras.getString("list");
			String[] list = r.split(",");

			for (int i = 0; i < list.length; i++) {
				Log.d("Pahan", list[i]);
				resultList.add(list[i]);
			}
		}
		init();
	}

	private void init() {
		searchText = (TextView) findViewById(R.id.searchuloadTxt);
		searchText.setTypeface(typeface);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				resultList));
	}

}
