package com.kcpradeep.yamba;

import android.content.Intent;
import android.database.Cursor;
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

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		yamba = (YambaApplication) getApplication();

		// Setup UI
		setContentView(R.layout.timeline);

		listStatuses = (ListView) findViewById(R.id.statuses);

		// Get the data
		cursor = yamba.statusData.query();
		startManagingCursor(cursor);

		String[] from = { StatusData.C_USER, StatusData.C_TEXT,
				StatusData.C_CREATEDAT };
		int[] to = { R.id.textUser, R.id.textText, R.id.textCreatedAt };
		// Setup Adapter

		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
		adapter.setViewBinder(VIEW_BINDER);
		listStatuses.setAdapter(adapter);

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
		cursor.requery();
	}

}
