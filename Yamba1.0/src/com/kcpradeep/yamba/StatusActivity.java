package com.kcpradeep.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {

	private static final String TAG = "StatusActivity";
	EditText editStatus;
	Button buttonUpdate;
	// Twitter twitter;
	ProgressDialog postingDiaglog;
	static final int DIALOG_ID = 47;

	// Called when the activity is first created
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Debug.startMethodTracing("Yamba.trace");
		setContentView(R.layout.status);

		// Get values of text and button from view
		editStatus = (EditText) findViewById(R.id.editStatus);
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

		// Register the listener, call "this" when the button is clicked
		buttonUpdate.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Debug.stopMethodTracing();
	}

	@Override
	public void onClick(View v) {
		String status = editStatus.getText().toString();
		Log.d("StatusActivity", "OnClick with status" + status);

		// update it online
		showDialog(DIALOG_ID);
		new PostToTwitter().execute(status);
		Log.d(TAG, "onClick");

	}

	private class PostToTwitter extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... statuses) {

			try {
				// twitter = new Twitter("student", "password");
				// twitter.setAPIRootUrl(("http://yamba.marakana.com/api"));

				((YambaApplication) StatusActivity.this.getApplication())
						.getTwitter().updateStatus(statuses[0]);
				return StatusActivity.this
						.getString(R.string.msgStatusUpdatedSuccess);
			} catch (TwitterException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return StatusActivity.this
						.getString(R.string.msgStatusUpdatedFail);
			}
		}

		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_ID);
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
					.show();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.itemPrefs:
			Log.d("MENU_SWITCH", "Preference selected");
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemServiceStart:
			Log.d("MENU_SWITCH", "Service Start");
			startService(new Intent(this, UpdaterService.class));
			break;
		case R.id.itemServiceStop:
			Log.d("MENU_SWITCH", "Service Stop");
			startService(new Intent(this, UpdaterService.class));
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID: {
			ProgressDialog dialog = new ProgressDialog(this);
			// dialog.setTitle("Posting to Twitter");
			dialog.setMessage(this.getString(R.string.msgPostingTweet));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;

	}

}
