package com.kcpradeep.yamba;

import java.util.List;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	private static final String TAG = "UpdaterService";
	private Updater updater;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		updater = new Updater();
		Log.d(TAG, "onCreated");
	}

	@Override
	public synchronized void onDestroy() {
		super.onDestroy();
		if (updater.isRunning()) {
			updater.interrupt();
		}
		updater = null;
		Log.d(TAG, "onDestroyed");
	}

	@Override
	public synchronized int onStartCommand(Intent intent,int flags, int startId) {
		super.onStartCommand(intent,flags, startId);
		if (!updater.isRunning()) {
			updater.start();
		}
		 Log.d(TAG, "onStarted");
		 return 0;
	}

	// //// Thread
	class Updater extends Thread {
		static final long DELAY = 30000;
		public boolean isRunning = false;

		public Updater() {
			super("Updater");
		}

		@Override
		public void run() {
			isRunning = true;
			while (isRunning) {
				Log.d(TAG, "Updater running");
				Twitter twitter = ((YambaApplication) getApplication())
						.getTwitter();
				String screenName = twitter.getScreenName();
				Log.d(TAG,screenName);
				
				List<Status> statuses = twitter.getFriendsTimeline();
				for (Status status : statuses) {

					Log.d(TAG, String.format("%s %s", status.user.name,
							status.text));

				}
				
				try {
					this.sleep(DELAY);
				} catch (InterruptedException e) {
					isRunning = false;

				}
			}
		}

		public boolean isRunning() {
			return isRunning;
		}
	}

}
