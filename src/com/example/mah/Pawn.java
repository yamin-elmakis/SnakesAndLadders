package com.example.mah;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class Pawn extends LinearLayout{

	private static final String TAG = "Pawn";
	private Point point;
	private int cellNumber = 0, playerNumber, score;
	private String name;
	
	public Pawn(Context context, Point p, int playerNumber) {
		super(context);
		point = p;
		this.playerNumber = playerNumber;
		score = 0;
	}
	
	public void setRes(int image){
		getLayoutParams().height = AppRes.CELL_HEIGHT;
		getLayoutParams().width = AppRes.CELL_WIDTH;
		setBackgroundResource(image);
	}

	@Override
	public boolean equals(Object o) {
		Pawn temp = (Pawn) o;
		if (this.cellNumber == temp.cellNumber)
			return true;
		else return false;
	}

	public void movePawn (Point newPoint){
		TranslateAnimation animation = new TranslateAnimation(
				Animation.ABSOLUTE, point.getX(), 
				Animation.ABSOLUTE, newPoint.getX(),
				Animation.ABSOLUTE, point.getY(),
				Animation.ABSOLUTE, newPoint.getY());
		animation.setDuration(1000);
		animation.setFillAfter(true); 
		this.startAnimation(animation);
		point = newPoint;
	}

	public int getScore() {
		return score;
	}

	// set the player score.
	// if not inGame then it's an absolute score
	public void setScore(int score, boolean inGame) {
		if (inGame)
			this.score += score;
		else 
			this.score = score;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public void setImage(int image){
		this.setBackgroundResource(image);
	}
	
	public int getCellNumber() {
		return cellNumber;
	}

	public void setCellNumber(int cellNumber) {
		this.cellNumber = cellNumber;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getPoint() {
		return point;
	}

	public void goInvisible(boolean flag){
		if (flag)
			setVisibility(View.INVISIBLE);
		else
			setVisibility(View.VISIBLE);
	}
}
