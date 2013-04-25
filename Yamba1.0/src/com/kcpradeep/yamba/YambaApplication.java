package com.kcpradeep.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class YambaApplication extends Application implements
		OnSharedPreferenceChangeListener {
	SharedPreferences prefs;
	private Twitter twitter = null;
	StatusData statusData;
	private boolean isRunning = false;
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		statusData = new StatusData(this);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		statusData.close();
	}

	/**
	 * Lazily initialize the connection to twitter
	 * 
	 * @return connection to twitter object
	 */
	public Twitter getTwitter() {
		if (twitter == null) {
			try {
				String username = prefs.getString("username", "");
				String password = prefs.getString("password", "");
				String server = prefs.getString("server", "");
				twitter = new Twitter(username, password);
				twitter.setAPIRootUrl(server);
				Log.d("YambaApplication", String.format(
						"Getting twitter with %s %s %s", username, password,
						server));
			} catch (Exception ex) {
				Toast.makeText(this, "wrong username password",
						Toast.LENGTH_LONG).show();
			}
		}
		return twitter;
	}

	//Called when preferences change
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		//Invalidate twitter object
		twitter = null;
		
	}
}
