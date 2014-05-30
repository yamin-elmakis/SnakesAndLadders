package com.example.simpletest.services;

import com.example.simpletest.R;
import com.example.simpletest.R.raw;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service{

	private MediaPlayer player;
	
	@Override
	public void onCreate() {
		Log.e("MusicService", "onCreate");
		
		player = MediaPlayer.create(this, R.raw.gremlins);
		player.setLooping(true); // Set looping
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "Music Service Started", Toast.LENGTH_LONG).show();
		Log.e("MusicService", "onStart");
		player.start();
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Music Service Stopped", Toast.LENGTH_LONG).show();
		Log.e("MusicService", "onDestroy");
		player.pause();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
