package com.example.mah;

import com.example.mah.db.GamePreferences;
import com.example.mah.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WinDialog extends Dialog implements android.view.View.OnClickListener{

	private static final String TAG = "WinDialog";
	private Button ok;
	private Activity activity;
	private Class gameClass = null;  
	private Intent intent;
	private ImageView winPic;
	private int winNum, winImages [] = {R.drawable.player_1win , R.drawable.player_2win};
	private GamePreferences prefs;
	
	public WinDialog(Activity activity, int winNum, String name) {
		super(activity);
		this.activity = activity;
		this.winNum = winNum;
		prefs = new GamePreferences(activity);
		setTitle(name);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_view);
		winPic = (ImageView) findViewById(R.id.ivDialod);
		winPic.setBackgroundResource(winImages[winNum-1]);
		ok = (Button) findViewById(R.id.bDialod);
		ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		closeDialog();
	}

	private void closeDialog() {
		try {
			gameClass = Class.forName(AppRes.MAIN);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		intent = new Intent(activity, gameClass);
		activity.startActivity(intent);
		dismiss();
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		closeDialog();
	}
}
