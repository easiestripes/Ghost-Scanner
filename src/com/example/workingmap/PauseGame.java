package com.example.workingmap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class PauseGame extends Activity {

	private int pauseTimeBegin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pause_game);
		
		pauseTimeBegin = (int) System.currentTimeMillis();
	}
	
	public void resume(View view) {
		int pauseTimeEnd = (int) System.currentTimeMillis();
		int pauseTime = pauseTimeEnd - pauseTimeBegin;
		Intent intent = new Intent ();
		intent.putExtra("pauseTime", pauseTime);
		setResult(-3, intent);
		this.finish();
	}
	public void quit(View view) {
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}

}
