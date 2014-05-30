package com.example.simpletest;

import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Dice extends ImageView implements android.view.View.OnClickListener {

	public Imoveable game;
	private int[] diceImages = new int[] { 
			R.drawable.cube_black_1, R.drawable.cube_black_2, R.drawable.cube_black_3,
			R.drawable.cube_black_4, R.drawable.cube_black_5, R.drawable.cube_black_6};
	private int[] diceFadeImages = new int[] { 
			R.drawable.cube_black_1_f, R.drawable.cube_black_2_f, R.drawable.cube_black_3_f,
			R.drawable.cube_black_4_f, R.drawable.cube_black_5_f, R.drawable.cube_black_6_f};
	private Random rand;
	public Context context;
	
	public Dice(Context context, Imoveable game) {
		super(context);
		setOnClickListener(this);
		this.context = context;
		this.game = game;
		rand = new Random();
	}

	public void setRes(){
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.ivnewgameheadline);
		p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.ivnewgameheadline);
		setBackgroundResource(diceImages[0]);
		setLayoutParams(p);
	}
	public void throwDice (){
		setClickable(false);
		int moveToPlay = rand.nextInt(6);
		setBackgroundResource(diceFadeImages[moveToPlay]);
		game.playDice(moveToPlay + 1);
	}
	@Override
	public void onClick(View arg0) {
		throwDice();
	}

	public void setImage(int diceNum){
		setBackgroundResource(diceImages[diceNum - 1]);
	}
	
	public void setDiceClickable(boolean flag){
		setClickable(flag);
	}
}
