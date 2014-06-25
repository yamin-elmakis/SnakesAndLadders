package com.example.mah;

import com.example.mah.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class NewGame extends Activity{

	private ImageButton ibEasy, ibMedium, ibHard;
	private Class gameClass;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_layout);
		ibEasy = (ImageButton) findViewById(R.id.ibEasy);
		ibMedium = (ImageButton) findViewById(R.id.ibMedium);
		ibHard = (ImageButton) findViewById(R.id.ibHard);
		ibEasy.setOnClickListener(new ChooseLevel());
		ibMedium.setOnClickListener(new ChooseLevel());
		ibHard.setOnClickListener(new ChooseLevel());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	class ChooseLevel implements OnClickListener {

		@Override
		public void onClick(View imageButton) {
			try {
				gameClass = Class.forName(AppRes.GAME_LOGIC);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			intent = new Intent (NewGame.this, gameClass);
			intent.putExtra(AppRes.IS_NEW_GAME , true);
			
			switch (imageButton.getId()) {
			case R.id.ibEasy:
				intent.putExtra(AppRes.DIFFICULTY , 0);
				break;
			case R.id.ibMedium:
				intent.putExtra(AppRes.DIFFICULTY , 1);
				break;
			case R.id.ibHard:
				intent.putExtra(AppRes.DIFFICULTY , 2);
				break;
			default:
				break;
			}
			startActivity(intent);
		}
	}
}
