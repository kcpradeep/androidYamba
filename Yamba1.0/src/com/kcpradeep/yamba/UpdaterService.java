package com.kcpradeep.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
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
	public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if (!updater.isRunning()) {
			updater.start();
		}
		Log.d(TAG, "onStarted");
		return 0;
	}

	// //// Thread
	class Updater extends Thread {
		static final long DELAY = 30000;
		YambaApplication yamba;

		public Updater() {
			super("Updater");
			yamba = ((YambaApplication) getApplication());
		}

		@Override
		public void run() {
			boolean newStatus = false;
			yamba.setRunning(true);
			while (yamba.isRunning()) {
				Log.d(TAG, "Updater running");
				Twitter twitter = yamba.getTwitter();
				List<Status> statuses = twitter.getHomeTimeline();
				for (Status status : statuses) {
					Log.d(TAG, status.getText());
					if (yamba.statusData.insert(status) > 0) {
						newStatus = true;
					}
				}
				if (newStatus) {
					sendBroadcast(new Intent("yamba.newStatus"));
				}
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					yamba.setRunning(false);
				}
			}
		}

		public boolean isRunning() {
			return yamba.isRunning();
		}
	}

}
