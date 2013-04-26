package com.kcpradeep.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity {

	// TextView statuses;
	ListView listStatuses;
	YambaApplication yamba;
	Cursor cursor;
	SimpleCursorAdapter adapter;
	TimelineReceiver timelineReceiver;

	/***
	 * custom binder to bind createdAt column to its view and change data from
	 * timestamp to relative date
	 */
	static final ViewBinder VIEW_BINDER = new ViewBinder() {
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (cursor.getColumnIndex(StatusData.C_CREATEDAT) != columnIndex) {
				// not processing
				return false;
			} else {
				long timestamp = cursor.getLong(columnIndex);
				CharSequence relativeTime = DateUtils
						.getRelativeTimeSpanString(timestamp);
				((TextView) view).setText(relativeTime);
				return true;
			}
		}
	};

	private void setupList() {
		// Get the data
		cursor = yamba.statusData.query();
		//startManagingCursor(cursor);

		String[] from = { StatusData.C_USER, StatusData.C_TEXT,
				StatusData.C_CREATEDAT };
		int[] to = { R.id.textUser, R.id.textText, R.id.textCreatedAt };
		// Setup Adapter

		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
		adapter.setViewBinder(VIEW_BINDER);
		listStatuses.setAdapter(adapter);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		yamba = (YambaApplication) getApplication();

		// Setup UI
		setContentView(R.layout.timeline);

		listStatuses = (ListView) findViewById(R.id.statuses);

		this.setupList();

		// check if pref has been set
		if (yamba.prefs.getString("username", null) == null) {
			startActivity(new Intent(this, PrefsActivity.class));
			Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG)
					.show();
			return;
		}
		Log.d("TimelineActivity", "onCreated");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Register timeline receiver
		if (timelineReceiver == null) {
			timelineReceiver = new TimelineReceiver();
		}

		registerReceiver(timelineReceiver, new IntentFilter("yamba.newStatus"));
	}

	@Override
	protected void onPause() {
		super.onPause();

		// unregister timeline receiver
		unregisterReceiver(timelineReceiver);
	}

	class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// refresh the list
			setupList();

		}

	}
}
