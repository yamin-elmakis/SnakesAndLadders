package com.example.mah.db;

import com.example.mah.AppRes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper{
		
	public DBHandler(Context context) {
		super(context, AppRes.DB_NAME, null, AppRes.DB_VERSION);
		Log.e("DBHandler", "constractor");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("DBHandler", "enter onCreate");
		// Creating game Table
		try {
			String CREATE_GAME_TABLE = "CREATE TABLE " + AppRes.DB_GAME_TABLE_NAME + "("
					+ AppRes.FIELD_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
					+ AppRes.FIELD_KEY_NAME + " TEXT," 
					+ AppRes.FIELD_DIFFICULTY + " TEXT,"
					+ AppRes.FIELD_DATE + " TEXT UNIQUE," 
					+ AppRes.FIELD_SCORE + " INTEGER)";
			db.execSQL(CREATE_GAME_TABLE);
			Log.e("DBHandler", "table " + AppRes.DB_GAME_TABLE_NAME + " created");
		} catch (SQLException e) {
			Log.e("DBHandler", "SQLException: " + e.toString());
		}		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop the old table
		db.execSQL("DROP TABLE IF EXISTS " + AppRes.DB_GAME_TABLE_NAME);
		// Create tables again
		onCreate(db);
	}
}
