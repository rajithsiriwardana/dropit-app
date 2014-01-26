package com.dropit;

import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private Typeface typeface;
	private TextView uploadTxt;
	private TextView downloadTxt;
	private TextView statusTxt;
	private LinearLayout downloadBtn;
	private LinearLayout uploadBtn;
	private EditText ipportEdit;
	private Button saveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		typeface = Typeface.createFromAsset(getAssets(), "fonts/hm.ttf");
		init();
	}

	private void init() {
		uploadTxt = (TextView) findViewById(R.id.setuloadTxt);
		uploadTxt.setTypeface(typeface);
		downloadTxt = (TextView) findViewById(R.id.setdownloadTxt);
		downloadTxt.setTypeface(typeface);

		statusTxt = (TextView) findViewById(R.id.settEdittEXTinto);
		statusTxt.setTypeface(typeface);

		ipportEdit = (EditText) findViewById(R.id.settiandportEdit);
		ipportEdit.setTypeface(typeface);
		ipportEdit.setText(Utils.IP + ":" + Utils.PORT);

		downloadBtn = (LinearLayout) findViewById(R.id.setuDownloadBtn);
		downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(SettingsActivity.this,
						DownloadActivity.class);
				startActivity(in);
			}
		});

		uploadBtn = (LinearLayout) findViewById(R.id.setdownuploadBtn);
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(SettingsActivity.this,
						UploadActivity.class);
				startActivity(in);
			}
		});

		saveBtn = (Button) findViewById(R.id.settsaveBtn);
		saveBtn.setTypeface(typeface);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String input = ipportEdit.getText().toString();
				try {
					URI uri = new URI("my://" + input);
					String host = uri.getHost();
					int port = uri.getPort();
					if (host != null && port != -1) {
						saveSettings(host, port);
					} else {
						showErrorToast();
					}
				} catch (URISyntaxException e) {
					showErrorToast();
				}

				Log.d("Pahan", Utils.IP + ":" + Utils.PORT);

			}
		});
	}

	private void saveSettings(String ip, int port) {

		String FILENAME = Utils.CONFIG_FILENAME;
		String string = ip+":"+port;

		try {
			FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_PRIVATE);
			fos.write(string.getBytes());
			fos.close();
			Utils.IP = ip;
			Utils.PORT = port;
			Toast.makeText(SettingsActivity.this,
					"configuration saved sucessfully", Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			Toast.makeText(SettingsActivity.this,
					"Error while saving configurations", Toast.LENGTH_LONG).show();
		}
	}

	private void showErrorToast() {
		Toast.makeText(SettingsActivity.this,
				"Invalid IP and PORT (e.g IP:PORT)", Toast.LENGTH_LONG).show();
	}

}
