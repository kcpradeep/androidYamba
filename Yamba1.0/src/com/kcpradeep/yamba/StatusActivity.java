package com.kcpradeep.yamba;

import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener,
		LocationListener {

	private static final String TAG = "StatusActivity";
	EditText editStatus;
	Button buttonUpdate;
	// Twitter twitter;
	ProgressDialog postingDiaglog;
	static final int DIALOG_ID = 47;
	YambaApplication yamba;
	LocationManager locationManager;
	Location location;
	String provider = LocationManager.GPS_PROVIDER;

	// Called when the activity is first created
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		yamba = (YambaApplication) getApplication();
		// Debug.startMethodTracing("Yamba.trace");
		setContentView(R.layout.status);

		// location stuff
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			yamba.getTwitter().setMyLocation(
					new double[] { location.getLatitude(),
							location.getLongitude() });
		}
		// Get values of text and button from view
		editStatus = (EditText) findViewById(R.id.editStatus);
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

		// Register the listener, call "this" when the button is clicked
		buttonUpdate.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 60000, 1000, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@SuppressWarnings("deprecation")
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
				yamba.getTwitter().updateStatus(statuses[0]);

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

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_ID);
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
					.show();

		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(this.getString(R.string.msgPostingTweet));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;

	}

	@Override
	public void onLocationChanged(Location location) {
		yamba.getTwitter()
				.setMyLocation(
						new double[] { location.getLatitude(),
								location.getLongitude() });
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
