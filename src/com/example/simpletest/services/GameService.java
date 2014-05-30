package com.example.simpletest.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.simpletest.AppRes;
import com.example.simpletest.GameLogic;
import com.example.simpletest.db.GamePreferences;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GameService extends Service{

	private String TAG = "Game Service";
	private Random r;
	private Map<Integer, Integer> goldAndMonsters;
	private int gameDifficulty, diceNum;
	private int player1Cell, player2Cell, player1Score, player2Score, curCell, curScore;
	private GamePreferences pref;
	private boolean turn, win;
	private Notifications notify;
	private Thread mythread;
	
	@Override
	public void onCreate() { // before on start
		super.onCreate();
		notify = new Notifications(getApplicationContext());
		Log.e(TAG, "ON create service");
		r = new Random(System.currentTimeMillis());
		pref = new GamePreferences(getApplicationContext());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		gameDifficulty = intent.getIntExtra(AppRes.DIFFICULTY, 0);
		getState();
		mythread = new Thread() {
			@Override
			public void run() {
				while (AppRes.RUN_IN_BACK) {
					playGame();
				}
			}
		};
		mythread.start();
	}

	private void getState() {
		buildSpecialPositionMap(gameDifficulty);
		player1Cell = pref.getPawnCellNum(1, gameDifficulty);
		player2Cell = pref.getPawnCellNum(2, gameDifficulty);
		player1Score = pref.getPawnScore(1, gameDifficulty);
		player2Score = pref.getPawnScore(2, gameDifficulty);
		turn = pref.getWhosTurn(gameDifficulty);
		win = pref.getWin(gameDifficulty);
	}

	private void saveState() {
		pref.setPawnCell(1, player1Cell, gameDifficulty);
		pref.setPawnCell(2, player2Cell, gameDifficulty);
		pref.setPawnScore(1, player1Score, gameDifficulty);
		pref.setPawnScore(2, player2Score, gameDifficulty);
		pref.setWhosTurn(turn, gameDifficulty);
		pref.setWin(win, gameDifficulty);
	}
	
	private void playGame() {
			if (!win) {
				// get the place and score of the current player
				curScore = (turn)? player1Score : player2Score;
				curCell = (turn)? player1Cell : player2Cell;
				// throw the dice
				diceNum = r.nextInt(6) + 1;
				Log.e(TAG, "p"+(turn ? "1":"2")+ "- diceNum: "+diceNum+" cell: " + curCell +" score: "+curScore);
				// move the current player
				curCell += diceNum;
				// update the player score
				curScore += diceNum;
				if (diceNum == AppRes.DICE_GET_EXTRA_TURN)
					curScore += AppRes.DICE_GET_BONUS;
				// check if current player is on a monster or gold
				Integer newCell = goldAndMonsters.get(curCell);
				if (newCell != null){
					int jump = newCell - curCell;
					if (jump > 0){ // step on gold
						Log.e(TAG, "steped on Gold");
						showToast("steped on Gold");
						curScore += jump + gameDifficulty; 
					}
					else { // step on monster
						Log.e(TAG, "steped on Monster");
						showToast("steped on Monster");
						curScore += (jump - gameDifficulty);
					}
					curCell = newCell;
				}
				
				// check if the player won the game 
				if (curCell >= AppRes.WIN_CELL[gameDifficulty]){
					curCell = AppRes.WIN_CELL[gameDifficulty];
					win = true;
					Log.e(TAG, "The Game ended");
					showToast("The Game ended");
					// get the data back to the player
					if (turn){
						player1Cell = curCell;
						player1Score = curScore;
					}
					else {
						player2Cell = curCell;
						player2Score = curScore;
					}
					saveState();
					AppRes.RUN_IN_BACK = false;
					return;
				}
				// get the data back to the player
				if (turn){
					player1Cell = curCell;
					player1Score = curScore;
				}
				else {
					player2Cell = curCell;
					player2Score = curScore;
				}
				// update player turn
				if (diceNum != AppRes.DICE_GET_EXTRA_TURN)
					turn = !turn;
				saveState();
				try {
					Thread.sleep(AppRes.PAWN_LONG_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				AppRes.RUN_IN_BACK = false;
			}
	}
	
	private void showToast(String text){
		notify.initNotification(gameDifficulty, GameLogic.class, text);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		AppRes.RUN_IN_BACK = false;
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	// build the map of the Special Positions according to the Difficulty 
	@SuppressLint("UseSparseArrays")
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
}
