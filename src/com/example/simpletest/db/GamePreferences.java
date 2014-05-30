package com.example.simpletest.db;

import com.example.simpletest.Pawn;
import com.example.simpletest.Point;

import android.content.Context;
import android.content.SharedPreferences;

public class GamePreferences {

	private SharedPreferences prefs;
	private Context context;
	private final String CLASS_NAME = "com.example.simpletest";
	private final String PLAYER_NAME = "playerName";
	private final String SOUND_FLEG = "sound_fleg";
	private final String MUSIC_FLEG = "music_fleg";
	private final String VIBRATION_FLEG = "vibration_fleg";
	private final String TURN = "whosTurn";
	private final String WIN = "win";
	private final String PAWN_POINT_X = "playerPointX";
	private final String PAWN_POINT_Y = "playerPointY";
	private final String PAWN_CELL_NUM = "playerCellNum";
	private final String PAWN_SCORE = "playerScore";
	
	public GamePreferences(Context context) {
		this.context = context;
		prefs = context.getSharedPreferences(CLASS_NAME, context.MODE_PRIVATE);
	}
	
	public void resetGame(int difficulty, Point initPoint){
		setWin(false, difficulty);
		setWhosTurn(true, difficulty);
		// reset player 1
		setPawnPoint(1,initPoint.getX(), initPoint.getY(), difficulty);
		setPawnCell(1, 0, difficulty);
		setPawnScore(1,0, difficulty);
		// reset player 2
		setPawnPoint(2,initPoint.getX(), initPoint.getY(), difficulty);
		setPawnCell(2, 0, difficulty);
		setPawnScore(2,0, difficulty);
	}

	public String getPlayerName() {
		return prefs.getString(PLAYER_NAME, "Player1");
	}

	public void setPlayerName(String playerName) {
		prefs.edit().putString(PLAYER_NAME, playerName).commit();
	}
	
	// save the player turn in the game according to the difficulty
	public void setWhosTurn(Boolean whosTurn, int difficulty) {
		prefs.edit().putBoolean(TURN + difficulty, whosTurn).commit();
	}
	
	// save the current state of the game according to the difficulty
	public void setWin(Boolean win, int difficulty) {
		prefs.edit().putBoolean(WIN + difficulty, win).commit();
	}

	// save the pawn data with the player number and the difficulty
	// example for the score of player1 in easy level: playerScore10
	public void setPawn (Pawn pawn, int difficulty) {
		setPawnPoint(pawn.getPlayerNumber(),pawn.getPoint().getX(),pawn.getPoint().getY(), difficulty);
		setPawnCell(pawn.getPlayerNumber(),  pawn.getCellNumber(), difficulty);
		setPawnScore(pawn.getPlayerNumber(),pawn.getScore(), difficulty);
	}

	public void setPawnScore(int playerNumber, int score, int difficulty) {
		prefs.edit().putInt(PAWN_SCORE + playerNumber + difficulty, score).commit();
	}

	public void setPawnCell(int playerNumber, int cellNumber, int difficulty) {
		prefs.edit().putInt(PAWN_CELL_NUM + playerNumber + difficulty, cellNumber).commit();
	}

	public void setPawnPoint(int playerNumber, int x, int y, int difficulty) {
		prefs.edit().putInt(PAWN_POINT_X + playerNumber + difficulty, x).commit();
		prefs.edit().putInt(PAWN_POINT_Y + playerNumber + difficulty, y).commit();
	}

	public int getPawnPointX(int playerNumber, int difficulty){
		return prefs.getInt(PAWN_POINT_X + playerNumber + difficulty, 0);
	}
	
	public int getPawnPointY(int playerNumber, int difficulty){
		return prefs.getInt(PAWN_POINT_Y + playerNumber + difficulty, 0);
	}

	public int getPawnCellNum(int playerNumber, int difficulty){
		return prefs.getInt(PAWN_CELL_NUM + playerNumber + difficulty, 0);
	}

	public int getPawnScore(int playerNumber, int difficulty){
		return prefs.getInt(PAWN_SCORE + playerNumber + difficulty, 0);
	}
	
	public boolean getWin(int difficulty){
		return prefs.getBoolean(WIN + difficulty, false);
	}
	
	public boolean getMusicFleg(){
		return prefs.getBoolean(MUSIC_FLEG, false);
	}
	
	public void setMusicFleg(Boolean musicFleg) {
		prefs.edit().putBoolean(MUSIC_FLEG, musicFleg).commit();
	}
	
	public boolean getSoundFleg(){
		return prefs.getBoolean(SOUND_FLEG, false);
	}
	
	public void setSoundFleg(Boolean soundFleg) {
		prefs.edit().putBoolean(SOUND_FLEG, soundFleg).commit();
	}
	
	public boolean getVibrationFleg(){
		return prefs.getBoolean(VIBRATION_FLEG, false);
	}
	
	public void setVibrationFleg(Boolean vibrationFleg) {
		prefs.edit().putBoolean(VIBRATION_FLEG, vibrationFleg).commit();
	}
	
	public boolean getWhosTurn(int difficulty){
		return prefs.getBoolean(TURN + difficulty, true);
	}
}
