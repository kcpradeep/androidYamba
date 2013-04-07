package com.kcpradeep.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	private static final String TAG = DbHelper.class.getSimpleName();
	public static final String DB_NAME = "timeline.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "statuses";

	public static final String C_ID = BaseColumns._ID;
	public static final String C_CREATEDAT = "createdAt";
	public static final String C_USER = "user";
	public static final String C_TEXT = "txt";

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = String.format(
				"create table %s(%s int primary key,%s INT,%s TEXT,%s TEXT)",
				TABLE, C_ID, C_CREATEDAT, C_USER, C_TEXT);
		Log.d(TAG, "OnCreate: SQL : " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists " + TABLE);
		Log.d(TAG, "onUpdate drop table " + TABLE);
		this.onCreate(db);

	}

}
