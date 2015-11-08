package com.example.workingmap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class OptionsMenu extends Activity {

	private int difficulty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options_menu);
	}
		
	public void easyOption (View view) {
		difficulty = 1;
		Intent intent = new Intent(this, PlayGame.class);		
		intent.putExtra("level", difficulty);
		startActivity(intent);
	}
		
	public void mediumOption (View view) {
		difficulty = 2;
		Intent intent = new Intent(this, PlayGame.class);		
		intent.putExtra("level", difficulty);
		startActivity(intent);
	}	

	public void hardOption (View view) {
		difficulty = 3;
		Intent intent = new Intent(this, PlayGame.class);		
		intent.putExtra("level", difficulty);
		startActivity(intent);
	}
}