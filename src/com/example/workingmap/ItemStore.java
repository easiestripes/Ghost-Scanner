package com.example.workingmap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ItemStore extends Activity {

	public int bombs, coins, storeTimeBegin;
	public final int cost = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_store);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    bombs = extras.getInt("bombnum");
		    coins = extras.getInt("coins");
		}
		
		storeTimeBegin = (int) System.currentTimeMillis();
		final TextView currentBombs = (TextView) findViewById(R.id.textView4);
		currentBombs.setText("" + bombs);	
		final TextView currentCoins = (TextView) findViewById(R.id.textView1);
		currentCoins.setText("" + coins);	
		
		final TextView costOfBomb = (TextView) findViewById(R.id.costOfBombs);
		costOfBomb.setText("" + "cost " + cost + " coins");	
	}
	
	public void back(View view){
		int storeTimeEnd = (int) System.currentTimeMillis();
		int storeTime = storeTimeEnd - storeTimeBegin;
		Intent intent = new Intent();
		intent.putExtra("bombnum", bombs);
		intent.putExtra("coins", coins);
		intent.putExtra("storeTime", storeTime);
		setResult(-1, intent);
		this.finish();
	}
	
	public void buyBomb(View view){
		if((coins - cost) >= 0){
			bombs++;
			coins = coins - cost; 
		}
		final TextView currentBombs = (TextView) findViewById(R.id.textView4);
		currentBombs.setText("" + bombs);
		
		final TextView currentCoins = (TextView) findViewById(R.id.textView1);
		currentCoins.setText("" + coins);	
		
	}
}
