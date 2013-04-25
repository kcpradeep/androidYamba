package com.kcpradeep.yamba;

import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

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
		// Setup UI
		setContentView(R.layout.timeline);
		yamba = (YambaApplication) getApplication();
		listStatuses = (ListView) findViewById(R.id.statuses);

		// Get the data
		cursor = yamba.statusData.query();
		startManagingCursor(cursor);

		String[] from = { StatusData.C_USER, StatusData.C_TEXT,StatusData.C_CREATEDAT };
		int[] to = { R.id.textUser, R.id.textText, R.id.textCreatedAt };
		// Setup Adapter

		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
		adapter.setViewBinder(VIEW_BINDER);
		listStatuses.setAdapter(adapter);

		/*
		 * final int USER_INDEX_COLUMN =
		 * cursor.getColumnIndex(StatusData.C_USER); final int TEXT_INDEX_COLUMN
		 * = cursor.getColumnIndex(StatusData.C_TEXT);
		 * 
		 * // Output it String user, text ; while (cursor.moveToNext()) { //
		 * moveToNext() initially to the first record user =
		 * cursor.getString(USER_INDEX_COLUMN); text =
		 * cursor.getString(TEXT_INDEX_COLUMN);
		 * listStatuses.append(String.format("\n%s: %s",user,text)); }
		 */

		Log.d("TimelineActivity", "onCreated");
	}

}
