package com.example.simpletest;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.RelativeLayout;

public class Board extends RelativeLayout{
	
	private int boardImage[] = {R.drawable.board_easy ,R.drawable.board_medium, R.drawable.board_hard};
	private int boardWidth = AppRes.CELL_WIDTH * AppRes.COLS;
	private int boardHeight, gameDifficulty;
	private Map<Integer, Integer> goldAndMonsters;
	
	public Board(Context context, int gameDifficulty) {
		super(context);
		this.gameDifficulty = gameDifficulty;
		boardHeight = (4 + gameDifficulty) * AppRes.CELL_HEIGHT;
		buildSpecialPositionMap(gameDifficulty);
	}
	
	public void setRes(){
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(boardWidth , boardHeight);
		p.addRule(RelativeLayout.BELOW, R.id.ivnewgameheadline);
		setLayoutParams(p);
		setBackgroundResource(boardImage[gameDifficulty]);
	}
	
	public int getSpecialPosition(int position){
		Integer newCell = goldAndMonsters.get(position); 
		if (newCell == null)
			return 0;
		else return newCell;
	}
	
	// build the map of the Special Positions according to the Difficulty 
	public void buildSpecialPositionMap(int gameDifficulty){
		goldAndMonsters = new HashMap<Integer, Integer>();
		goldAndMonsters.clear();
		switch (gameDifficulty) {
		case 0:
			goldAndMonsters.put(4,8);
			goldAndMonsters.put(15,7);
			goldAndMonsters.put(12,18);
			break;
		case 1:
			goldAndMonsters.put(9,7);
			goldAndMonsters.put(15,17);
			goldAndMonsters.put(18,13);
			goldAndMonsters.put(19,23);
			break;
		case 2:
			goldAndMonsters.put(3,9);
			goldAndMonsters.put(10,1);
			goldAndMonsters.put(11,19);
			goldAndMonsters.put(16,7);
			goldAndMonsters.put(22,28);
			goldAndMonsters.put(23,17);
			break;
			}	
	}

	public int getBoardHeight() {
		return boardHeight;
	}
	
}