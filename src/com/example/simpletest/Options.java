package com.example.simpletest;

import com.example.simpletest.db.GamePreferences;
import com.example.simpletest.services.MusicService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Options extends Activity implements OnClickListener{

	private String TAG = "Options";
	private Button bSound, bMusic, bVibration;
	private int onOff[] = {R.drawable.off, R.drawable.on};
	private Vibrator vibrator;
	private ImageButton ibabout;
	private EditText playerName;
	private GamePreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_layout);
		setRes();
		setParams();
	}

	private void setRes() {
		vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
		bSound = (Button) findViewById(R.id.bsound);
		bMusic = (Button) findViewById(R.id.bmusic);
		ibabout = (ImageButton) findViewById(R.id.ibabout);
		bVibration = (Button) findViewById(R.id.bvibration);
		playerName = (EditText) findViewById(R.id.etPlayerName);
		bSound.setBackgroundResource((AppRes.soundFlag) ? onOff[1] : onOff[0]);
		bMusic.setBackgroundResource((AppRes.musicFlag) ? onOff[1] : onOff[0]);
		bVibration.setBackgroundResource((AppRes.vibrationFlag) ? onOff[1] : onOff[0]);
	}
	
	private void setParams() {
		bSound.setOnClickListener(this);
		bMusic.setOnClickListener(this);
		bVibration.setOnClickListener(this);
		ibabout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Class gameClass = null;
				try {
					gameClass = Class.forName("com.example.simpletest.About");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent (Options.this, gameClass);
				startActivity(intent);			
			}
		});
		prefs = new GamePreferences(this);
	}

	@Override
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.bsound:
			AppRes.soundFlag = !AppRes.soundFlag;
			bSound.setBackgroundResource((AppRes.soundFlag) ? onOff[1] : onOff[0]);
			break;
		case R.id.bmusic:
			AppRes.musicFlag = !AppRes.musicFlag;
			bMusic.setBackgroundResource((AppRes.musicFlag) ? onOff[1] : onOff[0]);
			if (AppRes.musicFlag){
				startService(new Intent(this, MusicService.class));
			}
			else {
				stopService(new Intent(this, MusicService.class));
			}
			break;
		case R.id.bvibration:
			AppRes.vibrationFlag = !AppRes.vibrationFlag;
			bVibration.setBackgroundResource((AppRes.vibrationFlag) ? onOff[1] : onOff[0]);
			if (AppRes.vibrationFlag)
				vibrator.vibrate(800);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (playerName.getText().length() > 0){
			prefs.setPlayerName(playerName.getText().toString());
		}
		prefs.setSoundFleg(AppRes.soundFlag);
		prefs.setMusicFleg(AppRes.musicFlag);
		prefs.setVibrationFleg(AppRes.vibrationFlag);
	}

	@Override
	protected void onResume() {
		super.onResume();
		playerName.setText(prefs.getPlayerName());
		AppRes.soundFlag = prefs.getSoundFleg();
		AppRes.musicFlag = prefs.getMusicFleg();
		AppRes.vibrationFlag = prefs.getVibrationFleg();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	
	}
}
