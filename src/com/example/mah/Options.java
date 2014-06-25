package com.example.mah;

import com.example.mah.db.GamePreferences;
import com.example.mah.services.MusicService;
import com.example.mah.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Options extends Activity implements OnClickListener{

	private String TAG = "Options";
	private Button bSound, bMusic, bVibration, bAutoPlay;
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
		bAutoPlay = (Button) findViewById(R.id.b_auto_play);
		ibabout = (ImageButton) findViewById(R.id.ibabout);
		bVibration = (Button) findViewById(R.id.bvibration);
		playerName = (EditText) findViewById(R.id.etPlayerName);
		
	}
	
	private void setParams() {
		bSound.setOnClickListener(this);
		bMusic.setOnClickListener(this);
		bAutoPlay.setOnClickListener(this);
		bVibration.setOnClickListener(this);
		ibabout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Class gameClass = null;
				try {
					gameClass = Class.forName(AppRes.ABOUT);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent (Options.this, gameClass);
				startActivity(intent);			
			}
		});
		prefs = new GamePreferences(this);
		bSound.setBackgroundResource((AppRes.soundFlag) ? onOff[1] : onOff[0]);
		bMusic.setBackgroundResource((AppRes.musicFlag) ? onOff[1] : onOff[0]);
		bAutoPlay.setBackgroundResource((prefs.getAutoPlayFleg()) ? onOff[1] : onOff[0]);
		bVibration.setBackgroundResource((AppRes.vibrationFlag) ? onOff[1] : onOff[0]);
	}

	@Override
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.bsound:
			AppRes.soundFlag = !AppRes.soundFlag;
			bSound.setBackgroundResource((AppRes.soundFlag) ? onOff[1] : onOff[0]);
			break;
		case R.id.b_auto_play:
			boolean autoPlay = !prefs.getAutoPlayFleg();
			prefs.setAutoPlayFleg(autoPlay);
			bAutoPlay.setBackgroundResource((autoPlay) ? onOff[1] : onOff[0]);
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
