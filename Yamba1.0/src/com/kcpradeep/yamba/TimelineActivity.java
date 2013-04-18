package com.kcpradeep.yamba;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class TimelineActivity extends Activity {

	TextView statuses;
	YambaApplication yamba;
	Cursor cursor;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		yamba = (YambaApplication) getApplication();

		// Setup UI
		setContentView(R.layout.timeline);
		statuses = (TextView) findViewById(R.id.TextStatuses);

		// Get the data
		cursor = yamba.statusData.query();
		startManagingCursor(cursor);
		
		final int USER_INDEX_COLUMN = cursor.getColumnIndex(StatusData.C_USER);
		final int TEXT_INDEX_COLUMN = cursor.getColumnIndex(StatusData.C_TEXT);

		// Output it
		String user, text ;
		while (cursor.moveToNext()) { // moveToNext() initially to the first record
			user = cursor.getString(USER_INDEX_COLUMN);
			text = cursor.getString(TEXT_INDEX_COLUMN);
			statuses.append(String.format("\n%s: %s",user,text));
		}
	}
	
	

}
