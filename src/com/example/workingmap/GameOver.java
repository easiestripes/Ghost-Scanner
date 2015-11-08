package com.example.workingmap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class GameOver extends Activity {

	private int ghostsKilled, coinsNumber, time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.ghostsKilled = extras.getInt("ghostskills");
			this.coinsNumber = extras.getInt("coins");
			this.time = extras.getInt("time");
		}
	}
	
	public void playAgain(View view){
		Intent intent = new Intent(this, PlayGame.class);
		startActivity(intent);
	}
	public void viewStats(View view){
		Intent intent = new Intent(this, StatisticsScreen.class);
		intent.putExtra("ghostskills", ghostsKilled);
		intent.putExtra("coins", coinsNumber);
		intent.putExtra("time", time);
		startActivity(intent);
		
	}
	public void menu(View view){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
		
	}

}
