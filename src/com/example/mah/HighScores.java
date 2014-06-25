package com.example.mah;

import java.util.ArrayList;
import java.util.List;
import com.example.mah.db.DBAdapter;
import com.example.mah.db.ScoreAdapter;
import com.example.mah.db.ScoreElement;
import com.example.mah.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

public class HighScores extends Activity {
	
	private DBAdapter dbAdapter;
	private Cursor cursor;
	private List<ScoreElement> scoreList;
	private ListView scoreListView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayout();
		setRes();
	}

	private void setLayout() {
		setContentView(R.layout.highscore_layout);
		scoreListView = (ListView) findViewById(R.id.lvScoreList);
	}

	private void setRes() {
		scoreList = new ArrayList<ScoreElement>();
		dbAdapter = new DBAdapter(getApplicationContext());
		dbAdapter.openDB_w();
		cursor = dbAdapter.selectAll();
		buildScoreList();
		scoreListView.setAdapter(new ScoreAdapter(
				getApplicationContext(), R.id.lvScoreList, scoreList, R.layout.score_layout));
	}

	
	@Override
	protected void onDestroy() {
		dbAdapter.closeDB();
		super.onDestroy();
	}

	private void buildScoreList() {
		cursor.moveToFirst();
		String name, diff, date;
		int score;
		if (cursor.getCount() > 0) {
			while (!cursor.isAfterLast()) {
				name = cursor.getString(cursor.getColumnIndex(AppRes.FIELD_KEY_NAME));
				diff = cursor.getString(cursor.getColumnIndex(AppRes.FIELD_DIFFICULTY));
				date = cursor.getString(cursor.getColumnIndex(AppRes.FIELD_DATE));
				score = cursor.getInt(cursor.getColumnIndex(AppRes.FIELD_SCORE));
				scoreList.add(new ScoreElement(name, diff, date, score));
				cursor.moveToNext();
			}
		}
	}


}
