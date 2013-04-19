package com.kcpradeep.yamba;

import winterwell.jtwitter.Twitter.Status;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusData {

	private static final String TAG = StatusData.class.getSimpleName();

	public static final String C_ID = BaseColumns._ID;
	public static final String C_CREATEDAT = "createdAt";
	public static final String C_USER = "user";
	public static final String C_TEXT = "txt";

	Context context;
	DbHelper dbHelper;

	public StatusData(Context context) {
		this.context = context;
		dbHelper = new DbHelper();
	}

	public void close() {
		dbHelper.close();
	}

	public Cursor query() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// get the data
		return db.query(DbHelper.TABLE, null, null, null, null, null,
				C_CREATEDAT + " DESC");
	}

	/**
	 * Inserts into database
	 * 
	 * @param values
	 */
	public void insert(ContentValues values) {
		// Open Database
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// insert
		db.insertWithOnConflict(DbHelper.TABLE, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		// close
		db.close();
	}

	public void insert(Status status) {
		ContentValues values = new ContentValues();
		// create content values
		values.put(StatusData.C_ID, status.id);
		values.put(StatusData.C_CREATEDAT, status.createdAt.getTime());
		values.put(StatusData.C_USER, status.user.name);
		values.put(StatusData.C_TEXT, status.text);
		// insert data
		insert(values);
	}

	/**
	 * Delete all the data in database
	 */
	public void delete() {
		// Open Database
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// delete
		db.delete(dbHelper.TABLE, null, null);
		// close
		db.close();
	}

	/**
	 * Database class to insert/update/delete database
	 * 
	 * @author kcpradeep
	 * 
	 */
	private class DbHelper extends SQLiteOpenHelper {
		public static final String DB_NAME = "timeline.db";
		public static final int DB_VERSION = 1;
		public static final String TABLE = "statuses";

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = String
					.format("create table %s(%s int primary key,%s INT,%s TEXT,%s TEXT)",
							TABLE, C_ID, C_CREATEDAT, C_USER, C_TEXT);
			Log.d(TAG, "OnCreate: SQL : " + sql);
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Typically alter table here...
			db.execSQL("drop table if exists " + TABLE);
			Log.d(TAG, "onUpdate drop table " + TABLE);
			this.onCreate(db);

		}

	}

}
