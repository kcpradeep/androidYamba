package com.kcpradeep.yamba;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TimelineActivity extends Activity {

	//TextView statuses;
	ListView listStatuses;
	YambaApplication yamba;
	Cursor cursor;
	SimpleCursorAdapter adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		yamba = (YambaApplication) getApplication();

		// Setup UI
		setContentView(R.layout.timeline);
		listStatuses = (ListView) findViewById(R.id.statuses);

		// Get the data
		cursor = yamba.statusData.query();
		startManagingCursor(cursor);
		
		
		String[] from = {StatusData.C_USER,StatusData.C_TEXT};
		int[] to = {R.id.textUser, R.id.textText};
		//Setup Adapter
		
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
		listStatuses.setAdapter(adapter);
		
		
		
/*		final int USER_INDEX_COLUMN = cursor.getColumnIndex(StatusData.C_USER);
		final int TEXT_INDEX_COLUMN = cursor.getColumnIndex(StatusData.C_TEXT);

		// Output it
		String user, text ;
		while (cursor.moveToNext()) { // moveToNext() initially to the first record
			user = cursor.getString(USER_INDEX_COLUMN);
			text = cursor.getString(TEXT_INDEX_COLUMN);
			listStatuses.append(String.format("\n%s: %s",user,text));
		}*/
		
		Log.d("TimelineActivity","onCreated");
	}
	
	

}
