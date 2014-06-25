package com.example.mah;

import com.example.mah.R;
import android.app.Activity;
import android.os.Bundle;

public class About extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}