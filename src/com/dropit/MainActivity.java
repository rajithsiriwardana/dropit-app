package com.dropit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Client client = null;
	private Button sendBtn = null;
	private Button fileSeletBtn = null;
	private EditText inputText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		client = new Client();

		inputText = (EditText) findViewById(R.id.inputText);
		final EditText ipText = (EditText) findViewById(R.id.ipText);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

					String messsage = inputText.getText().toString();
					String ip = ipText.getText().toString();
					new SendingTask().execute(messsage,ip);
			}
		});
		
		fileSeletBtn = (Button) findViewById(R.id.fileSeletBtn);
		fileSeletBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
				//fileintent.setType("file/*");
		        try {
		            startActivityForResult(fileintent, 123);
		        } catch (Exception e) {
		            Log.d("Pahan", "No activity can handle picking a file. Showing alternatives.");
		        }
			}
		});
	}

	private void makeToast(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (data == null)
            return;
        switch (requestCode) {
        case 123:
            if (resultCode == RESULT_OK) {
                String FilePath = data.getData().getPath();
                inputText.setText(FilePath);
            }
        }
	}

	class SendingTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected Void doInBackground(String... params) {

				client.sendFile(params[0],params[1]);
				
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			sendBtn.setText("SENDING " + values);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			sendBtn.setText("SEND");
			super.onPostExecute(result);
		}

	}

}
