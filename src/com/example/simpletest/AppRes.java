package com.example.simpletest;

public class AppRes {

	// game resources
	public static final int 	GAME_DELAY = 650;
	public static final int 	vibrationTime = 800;
	public static final String 	DIFFICULTY = "difficulty";
	public static final String 	SCORE = "Score: ";
	public static final String 	IS_NEW_GAME = "isNewGame";
	public static final String [] GAME_DIFFICULTY = {"easy", "medium", "hard"};
	public static boolean 		soundFlag, musicFlag, vibrationFlag;
	public static boolean 		RUN_IN_BACK;
	
	// board resources
	public static final int CELL_HEIGHT = 120;
	public static final int CELL_WIDTH = 144;
	public static final int COLS = 5;
	public static final int WIN_CELL [] = {20, 25, 30};

	// dice resources
	public static final int		DICE_DELAY = 350;
	public static final int 	DICE_GET_EXTRA_TURN = 6;
	public static final int 	DICE_GET_BONUS = 1;
	
	// pawn resources
	public static final int	PAWN_DELAY = 1050;
	public static final int	PAWN_SEC_DELAY = 1350;
	public static final int	PAWN_LONG_DELAY = 2550;
	public static final int PLAYER_IMAGES [] = {R.drawable.players2, R.drawable.player_1, R.drawable.player_2};

	// DB resources	
	public static final String 	DB_NAME = "monsters_and_heroes_db";
	public static final int 	DB_VERSION = 1;
	public static final String 	DB_GAME_TABLE_NAME = "monsters_and_heroes";
	public static final String 	FIELD_ROW_ID = "rowid";
	public static final String 	FIELD_KEY_NAME = "name";
	public static final String 	FIELD_DIFFICULTY = "difficulty";
	public static final String 	FIELD_DATE = "date";
	public static final String 	FIELD_SCORE = "score";
	
	
	
}
