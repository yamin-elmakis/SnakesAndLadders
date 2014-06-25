package com.example.mah;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.mah.db.DBAdapter;
import com.example.mah.db.GamePreferences;
import com.example.mah.services.GameService;
import com.example.mah.services.Notifications;
import com.example.mah.R;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class GameLogic extends Activity implements Imoveable {
	
	private String TAG = "GameLogic";
	private MediaPlayer diceSound, goldSound, monsterSound;
	private int numOfRows;
	private int gameDifficulty;
	private ImageView ivturn;
	private Dice dice;
	private Pawn player1, player2;
	private Board gameBoard;
	private RelativeLayout mainLayout;
	private TextView tvTurn, tvPlayerName, tvPlayerScore;
	private Point boardPoints[];
	private boolean whosTurn, win, isNewGame, backPressed;	
	private Vibrator vibrator;
	private DBAdapter dbAdapter;
	private GamePreferences prefs;
	private Intent intent;
	private Notifications notify;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		setRes();
		setLayout();
		Log.e(TAG, "end onCreate");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");
		intent.putExtra(AppRes.IS_NEW_GAME, false);
		prefs.setPawn(player1, gameDifficulty);
		prefs.setPawn(player2, gameDifficulty);
		prefs.setWhosTurn(whosTurn, gameDifficulty);
		prefs.setWin(win, gameDifficulty);
		if (!win && !backPressed && prefs.getAutoPlayFleg()){
			Intent intent = new Intent(this, GameService.class);
			intent.putExtra(AppRes.DIFFICULTY , gameDifficulty);
			AppRes.RUN_IN_BACK = true;
			startService(intent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		backPressed = false;
		AppRes.RUN_IN_BACK = false;
		stopService(new Intent(this, GameService.class));
		notify.cancelNotification(gameDifficulty);
		intent = this.getIntent();
		isNewGame = intent.getBooleanExtra(AppRes.IS_NEW_GAME, false);
		Log.e(TAG, "onResume isNewGame: " + isNewGame);
		if(isNewGame){
			prefs.resetGame(gameDifficulty, boardPoints[0]);
			player2.movePawn(boardPoints[0]);
			player1.movePawn(boardPoints[0]);
		}
		win = prefs.getWin(gameDifficulty);
		if (win){
			whosTurn = prefs.getWhosTurn(gameDifficulty);
			String name;
			int winNum, winScore;
			if (whosTurn){
				name = prefs.getPlayerName();
				winNum = 1;
			}else {
				name = "Android";
				winNum = 2;
			}
			winScore = prefs.getPawnScore(winNum, gameDifficulty);
			showWinDialog(name, winNum, winScore);
		}else{
			// set the properties of payer 1
			player1.setCellNumber(prefs.getPawnCellNum(1, gameDifficulty));
			player1.movePawn(boardPoints[prefs.getPawnCellNum(1, gameDifficulty)]);
			player1.setScore(prefs.getPawnScore(1, gameDifficulty), false);
			// set the properties of payer 2
			player2.setCellNumber(prefs.getPawnCellNum(2, gameDifficulty));
			player2.movePawn(boardPoints[prefs.getPawnCellNum(2, gameDifficulty)]);
			player2.setScore(prefs.getPawnScore(2, gameDifficulty), false);
			// get the turn of the last player how played and switch the turn to the current player
			whosTurn = !prefs.getWhosTurn(gameDifficulty);
			switchPlayer();
		}
	}
	
	private void setRes() {
		intent = this.getIntent();
		gameDifficulty = intent.getIntExtra(AppRes.DIFFICULTY, 0);
		isNewGame = intent.getBooleanExtra(AppRes.IS_NEW_GAME, false);
		numOfRows = 4 + gameDifficulty;
		dbAdapter = new DBAdapter(getApplicationContext());
		dbAdapter.openDB_w();
		vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
		mainLayout = (RelativeLayout) findViewById(R.id.llmainLayout);
		gameBoard = new Board(this, gameDifficulty);
		getPointes();
		ivturn = (ImageView) findViewById(R.id.ivturn);
		tvTurn = (TextView) findViewById(R.id.tvturn);
		tvPlayerName = (TextView) findViewById(R.id.tvplayername);
		tvPlayerScore = (TextView) findViewById(R.id.tvscoreingame);
		dice = new Dice(this, this);
		player1 = new Pawn(this, boardPoints[0], 1);
		player2 = new Pawn(this,boardPoints[0], 2);
		gameBoard.addView(player2);
		gameBoard.addView(player1);
		mainLayout.addView(gameBoard);
		mainLayout.addView(dice);
		diceSound = MediaPlayer.create(this, R.raw.shaking);
		diceSound.setLooping(false); // Set looping
		goldSound = MediaPlayer.create(this, R.raw.jump_lo);
		goldSound.setLooping(false); // Set looping
		monsterSound = MediaPlayer.create(this, R.raw.goingup);
		monsterSound.setLooping(false); // Set looping
	}

	private void setLayout() {
		win = false;
		whosTurn = true;
		gameBoard.setRes();
		dice.setRes();
		player1.setRes(AppRes.PLAYER_IMAGES[1]);
		player2.setRes(AppRes.PLAYER_IMAGES[2]);
		prefs = new GamePreferences(this);
		player1.setName(prefs.getPlayerName());
		player2.setName("Android");
		notify = new Notifications(this);
	}
	
	// figure from the whosTurn which player should play this move
	// move the player according to the moveNumber it go from the Dice.throwDice 
	// if moveNumber is 6 - give the player another turn
	@Override
	public void playDice(final int moveNumber) {
		if (AppRes.vibrationFlag)
			vibrator.vibrate(AppRes.vibrationTime);
		if (AppRes.soundFlag)
			diceSound.start();
		// get the current player
		final Pawn curPlayer = (whosTurn)? player1 : player2;
		curPlayer.setScore(moveNumber, true);
		updateScore();
		int newCell = moveNumber + curPlayer.getCellNumber();
		// if the current player won the game
		if (newCell >= AppRes.WIN_CELL[gameDifficulty]){
			curPlayer.movePawn(boardPoints[AppRes.WIN_CELL[gameDifficulty]]);
			// curPlayer won the game
			win = true;
			new Handler().postDelayed(new Runnable() {
		        @Override
		        public void run() {
		        	String name = (curPlayer.getPlayerNumber() == 1) ? prefs.getPlayerName() : "Android" ;
		        	int playerNUmber = curPlayer.getPlayerNumber();
		        	int score = curPlayer.getScore();
		        	showWinDialog(name, playerNUmber, score);
		        }				
		    }, AppRes.PAWN_SEC_DELAY);
		}
		else {
			curPlayer.setCellNumber(newCell);
			curPlayer.movePawn(boardPoints[newCell]);
		}
		int deley;
		if (specialPosition(curPlayer))
			deley = AppRes.PAWN_LONG_DELAY;
		else 
			deley = AppRes.PAWN_SEC_DELAY;
		
		new Handler().postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	if (moveNumber != AppRes.DICE_GET_EXTRA_TURN)
	        		switchPlayer();
	        	else 
	        		// when throw 6 you get 1 extra point
	        		curPlayer.setScore(AppRes.DICE_GET_BONUS, true);
	        	if (whosTurn && !win){
	        		dice.setDiceClickable(true);
	        		dice.setImage(moveNumber);
	        	}
				else{
					try {
						Thread.sleep(AppRes.GAME_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!win)
						dice.throwDice();
				}
	        }
	    }, deley);
	}
	
	// switch the player turn and nextPlayer image
	private void switchPlayer(){
		whosTurn = !whosTurn;
		if (whosTurn){
			ivturn.setBackgroundResource(AppRes.PLAYER_IMAGES[1]);
			tvPlayerName.setText(player1.getName());
		}
		else{
			ivturn.setBackgroundResource(AppRes.PLAYER_IMAGES[2]);
			tvPlayerName.setText(player2.getName());
		}
		updateScore();
	}
	
	public void showWinDialog(String name, int playerNumber, int score) {
    	WinDialog dialog = new WinDialog(GameLogic.this, playerNumber, name);
    	dialog.show();
    	long timeStemp = System.currentTimeMillis();
		Date date = new Date(timeStemp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm");
    	dbAdapter.insertToDB(name, AppRes.GAME_DIFFICULTY[gameDifficulty], dateFormat.format(date), score);
	}

	public void updateScore() {
		tvPlayerScore.setText(AppRes.SCORE + ((whosTurn)? player1.getScore() : player2.getScore()));
	}
	
	// check if the player got to a special Position
	// and if he - so act accordingly
	private boolean specialPosition(final Pawn curPlayer) {
		final int curCell = curPlayer.getCellNumber(); 
		final Integer newCell = gameBoard.getSpecialPosition(curCell);
		if (newCell != 0 && !win){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					int bonusScore;
					if (curCell > newCell) {
						Toast.makeText(GameLogic.this, "steped on Monster",Toast.LENGTH_SHORT).show();
						// when step on monster you loss points according to the fall plus the game difficulty
						bonusScore = newCell - curCell - gameDifficulty;
						if (AppRes.soundFlag)
							monsterSound.start();
					} else {
						Toast.makeText(GameLogic.this, "steped on Gold",Toast.LENGTH_SHORT).show();
						// when step on gold you get points according to the jump plus the game difficulty
						bonusScore = newCell - curCell + gameDifficulty;
						if (AppRes.soundFlag)
							goldSound.start();
					}
					curPlayer.setScore(bonusScore, true);
					updateScore();
					curPlayer.setCellNumber(newCell);
					curPlayer.movePawn(boardPoints[newCell]);
				}
			}, AppRes.PAWN_SEC_DELAY);
			return true;
		}
		else return false;
	}
	
	// calculate the (x,y) position of each cell in the board
	public void getPointes() {
		int counter=0;
		boardPoints = new Point[numOfRows*AppRes.COLS+1];
		boardPoints[counter] = new Point(-2*AppRes.CELL_HEIGHT, gameBoard.getBoardHeight()- AppRes.CELL_HEIGHT);
		for (int i = 0; i < numOfRows; i++) {
			if (i != 0)
				if (i%2 != 0)
					counter += AppRes.COLS + 1;
				else counter += AppRes.COLS - 1; 
			for (int j = 0; j < AppRes.COLS; j++) {
				if (i%2 != 0)
					counter--;
				else counter++;
				boardPoints[counter] = new Point(j*AppRes.CELL_WIDTH, gameBoard.getBoardHeight() - AppRes.CELL_HEIGHT*(i+1));
			}
		}
	}

	@Override
	public void onBackPressed() {
		backPressed = true;
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		dbAdapter.closeDB();
		super.onDestroy();
	}
}
