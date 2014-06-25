package com.example.mah;

import com.example.mah.db.GamePreferences;
import com.example.mah.services.MusicService;
import com.example.mah.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private ImageButton newGame, options, highScores;
	private Class gameClass = null;
	private Intent intent;
	private GamePreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		newGame = (ImageButton) findViewById(R.id.ibNewGame);
		options = (ImageButton) findViewById(R.id.ibOptions);
		highScores = (ImageButton) findViewById(R.id.ibHighScores);
		highScores.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					gameClass = Class.forName(AppRes.HIGH_SCORES);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				intent = new Intent (MainActivity.this, gameClass);
				startActivity(intent);			
			}
		});
		newGame.setOnClickListener(new clicked());
		options.setOnClickListener(new clicked());
		prefs = new GamePreferences(this);
		AppRes.soundFlag = prefs.getSoundFleg();
		AppRes.musicFlag = prefs.getMusicFleg();
		AppRes.vibrationFlag = prefs.getVibrationFleg();
		if (AppRes.musicFlag){
			Log.d("MainActivity", "starting srvice");
			startService(new Intent(this, MusicService.class));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class clicked implements OnClickListener {

		@Override
		public void onClick(View imageButton) {
			if (imageButton == options) {
				try {
					gameClass = Class.forName(AppRes.OPTIONS);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (imageButton == newGame) {
				try {
					gameClass = Class.forName(AppRes.NEW_GAME);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			intent = new Intent (MainActivity.this, gameClass);
			startActivity(intent);
		}
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder ald = new AlertDialog.Builder(this);
		ald.setTitle("EXIT THE GAME");
		ald.setMessage("are you sure?");
		ald.setNegativeButton(android.R.string.no, null);
		ald.setPositiveButton(android.R.string.yes,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						MainActivity.super.onBackPressed();
						if (AppRes.musicFlag){
							stopService(new Intent(MainActivity.this, MusicService.class));
						}
					}
				});
		ald.create().show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (AppRes.musicFlag){
			stopService(new Intent(this, MusicService.class));
		}
	}
}
