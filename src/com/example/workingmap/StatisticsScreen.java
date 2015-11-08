package com.example.workingmap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class StatisticsScreen extends Activity {

	private int ghostsKilled, coinsNumber, time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics_screen);
		
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.ghostsKilled = extras.getInt("ghostskills");
			this.coinsNumber = extras.getInt("coins");
			this.time = extras.getInt("time");
		}

		final TextView nine = (TextView) findViewById(R.id.ghosts);
		nine.setText("" + this.ghostsKilled);
		
		final TextView textView13 = (TextView) findViewById(R.id.textView13);
		textView13.setText("" + this.coinsNumber);
		
		final TextView textView14 = (TextView) findViewById(R.id.textView14);
		textView14.setText("" + this.time + " ");

		final TextView textView15 = (TextView) findViewById(R.id.textView15);
		textView15.setText("" + (this.ghostsKilled*100 + this.coinsNumber + this.time));		
	}
	
	public void back(View view) {
		this.finish();
	}
}
