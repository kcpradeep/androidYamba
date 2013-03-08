package com.kcpradeep.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StatusActivity extends Activity implements OnClickListener {

	EditText editStatus;
	Button buttonUpdate;

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
	}

	@Override
	public void onClick(View v) {
		String status = editStatus.getText().toString(); 
		Log.d("StatusActivity","OnClick with status"+status);
		
		//update it online
		Twitter twitter = new Twitter("pradeep999","pradeep");
		twitter.setAPIRootUrl("http://identi.ca/api/");
		twitter.setStatus(status);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
