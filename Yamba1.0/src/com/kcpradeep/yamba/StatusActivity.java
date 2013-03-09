package com.kcpradeep.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {

	private static final String TAG = "StatusActivity";
	EditText editStatus;
	Button buttonUpdate;
	Twitter twitter;

	// Called when the activity is first created
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		// Get values of text and button from view
		editStatus = (EditText) findViewById(R.id.editStatus);
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

		// Register the listener, call "this" when the button is clicked
		buttonUpdate.setOnClickListener(this);

		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl(("http://yamba.marakana.com/api"));
	}

	@Override
	public void onClick(View v) {
		String status = editStatus.getText().toString();
		Log.d("StatusActivity", "OnClick with status" + status);

		// update it online

		new PostToTwitter().execute(status);
		Log.d(TAG, "onClick");

	}

	class PostToTwitter extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... statuses) {

			try {
				winterwell.jtwitter.Status status = twitter
						.updateStatus(statuses[0]);
				return status.text;
			} catch (TwitterException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
		}

		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(String result) {
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
