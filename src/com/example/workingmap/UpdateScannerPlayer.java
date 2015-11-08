package com.example.workingmap;

import java.util.ArrayList;
import android.os.AsyncTask;

public class UpdateScannerPlayer extends AsyncTask<ArrayList<Ghost>, ArrayList<Ghost>, ArrayList<Ghost>> {

	public AsyncData dataPasser = null;
	private int ghostAttack = 0, difficulty = 0, playerHealth = 13;
	private boolean isDead = false;
	
	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected ArrayList<Ghost> doInBackground(ArrayList<Ghost>... params) {
		while(isDead == false && !this.isCancelled()) {
			if(params[0].size() > 0) {
				ghostAttack++;
			} else if(ghostAttack <= 4) {
				ghostAttack = 0;
			}
			
			if(ghostAttack == 5) {
				params[0].get(0).setPlayerHealth(params[0].get(0).getPlayerHealth() - 1);
				this.setPlayerHealth(params[0].get(0).getPlayerHealth() - 1);
				ghostAttack = 4;
			}
			
			if(params[0].size() > 0) {
				difficulty = params[0].get(0).getDifficulty();
			} else {
				difficulty = 0;
			}
			if(ghostAttack >= this.getPlayerHealth() - difficulty)
				isDead = true;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		return params[0];
	}
	
	@Override
	protected void onProgressUpdate(ArrayList<Ghost>... params) {

	}
	
	@Override
	protected void onCancelled() {
		dataPasser.updatedHealth(this.getPlayerHealth());
	}
	
	@Override
	protected void onPostExecute(ArrayList<Ghost> result) {
		dataPasser.gameOver();
	}

	public int getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}
}
