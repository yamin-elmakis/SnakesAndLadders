package com.example.mah.db;

import com.example.mah.AppRes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter {

	private String TAG = "DBAdapter";
	private Context context;
	private SQLiteDatabase database;
	private DBHandler dbHelper;
	
	public DBAdapter(Context context) {
		this.context = context;
	}
	
	private ContentValues createContentValues (String name, String difficulty, String date, int score) {
		ContentValues values = new ContentValues();
		values.put(AppRes.FIELD_KEY_NAME, name);
		values.put(AppRes.FIELD_DIFFICULTY, difficulty);
		values.put(AppRes.FIELD_DATE, date);
		values.put(AppRes.FIELD_SCORE, score);
		return values;
	}
	
	public long insertToDB (String name, String difficulty, String date, int score){
		ContentValues initialValues = createContentValues(name, difficulty, date, score);
		return database.insert(AppRes.DB_GAME_TABLE_NAME, null, initialValues);
	}
	
	public boolean updateDB (long rowId, String name, String difficulty, String date, int score){
		ContentValues updateValues = createContentValues(name, difficulty, date, score);
		return database.update(AppRes.DB_GAME_TABLE_NAME, updateValues, AppRes.FIELD_ROW_ID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteFromDB(long rowId){
		return database.delete(AppRes.DB_GAME_TABLE_NAME, AppRes.FIELD_ROW_ID+ "=" + rowId, null) > 0;
	}
	
	public Cursor selectAll(){
		Log.e(TAG, "SELECT ALL");
		return database.query(AppRes.DB_GAME_TABLE_NAME, new String[] { 
			AppRes.FIELD_KEY_NAME, AppRes.FIELD_DIFFICULTY, AppRes.FIELD_DATE, AppRes.FIELD_SCORE}
			, null, null, null, null, AppRes.FIELD_SCORE + " desc");
	}
	
	public Cursor selectBy(String FieldName, String value){
		return database.query(AppRes.DB_GAME_TABLE_NAME, new String[] { 
				AppRes.FIELD_KEY_NAME, AppRes.FIELD_DIFFICULTY, AppRes.FIELD_DATE, AppRes.FIELD_SCORE}
				,FieldName + "=" + value, null, null, null, AppRes.FIELD_SCORE + " desc");
	}
	public DBAdapter openDB_w() {
		dbHelper = new DBHandler(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void closeDB(){
		database.close();
		dbHelper.close();
	}
}
