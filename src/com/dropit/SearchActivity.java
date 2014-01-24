package com.dropit;

import android.os.Bundle;
import android.app.ListActivity;
import android.graphics.Typeface;
import android.view.Window;
import android.widget.TextView;

public class SearchActivity extends ListActivity {

	private TextView loadingText;
	private Typeface typeface;
	private TextView searchText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		init();
	}
	
	private void init(){
		loadingText = (TextView)findViewById(R.id.listLoadingTxt);
		loadingText.setTypeface(typeface);
		searchText = (TextView)findViewById(R.id.searchuloadTxt);
		searchText.setTypeface(typeface);
		this.getListView().setEmptyView(loadingText);
	}
	
	
	



}
