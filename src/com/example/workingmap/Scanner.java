package com.example.workingmap;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Scanner extends Activity implements AsyncData {
	
	private int health, coinsNumber, timeMap, timeBegin;
	private ArrayList<Double> angles = new ArrayList<Double>();
	private ArrayList<Ghost> ghosts, origGhosts;
	private ArrayList<Integer> deadGhostIndexes = new ArrayList<Integer>();
	private Player player;
	private int dirFacing = 0, bombs = 0, ghostsKilled = 0, count = 0;
	private LayoutInflater inflater;
    private View view;
    private Toast toast;
    private boolean isAttacking = false;
    private UpdateScannerPlayer scannerPlUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);  
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Intent intent = getIntent();
			ghosts = (ArrayList<Ghost>) intent.getSerializableExtra("ghosts");
			origGhosts = (ArrayList<Ghost>) intent.getSerializableExtra("ghosts");
			player = (Player) intent.getSerializableExtra("player");
			bombs = extras.getInt("bombnum");
			coinsNumber = extras.getInt("coins");
			timeMap = extras.getInt("timeMap");
			ghostsKilled = extras.getInt("ghostskills");
			
		    for (Ghost g : ghosts) {
		    	double gLat = g.getLng();
		    	double gLng = g.getLat();
		    	double angle = Math.toDegrees(Math.atan2(gLat - player.getLat(), gLng - player.getLng()));
		    	if(angle < 0)
		            angle += 360;
		    	angles.add(angle);
		    }
		    health = player.getHealth();
	    	isGhostInView(angles, 0);
	    	
		}
		
		timeBegin = (int) System.currentTimeMillis();
		
		scannerPlUpdate = new UpdateScannerPlayer();
		scannerPlUpdate.execute(ghosts);
		scannerPlUpdate.dataPasser = this;
		
		inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.display_ghost_scanner, (ViewGroup) findViewById(R.id.relativeLayout1));
		toast = new Toast(this);
		
	}
	
	@Override
	public void updatedHealth(int playerHealth) {
		ghosts.get(0).setPlayerHealth(playerHealth);
	}
	
	@Override
	public void gameOver() {
		int timeEnd = (int) System.currentTimeMillis();
		int time = ((timeEnd - timeBegin) + timeMap)/1000;
		coinsNumber += (ghostsKilled*10);
		Intent intent = new Intent(this, GameOver.class);
		intent.putExtra("ghostskills", ghostsKilled);
		intent.putExtra("coins", coinsNumber);
		intent.putExtra("time", time);
		startActivity(intent);
	}
	
	public void isGhostInView(ArrayList<Double> angles, int count) {
		for (int i = 0; i < angles.size(); i++) {
			if(angles.get(i) >= 0 && angles.get(i) < 180 && this.dirFacing == 0) {
				if(count == 0) {
					toast.setView(view);
				    toast.show();
				}
			    if(isAttacking == true) {
					hitGhost(ghosts.get(i));
					isAttacking = false;
			    }
			}
			if(angles.get(i) >= 180 && angles.get(i) < 360 && this.dirFacing == 1) {
				if(count == 0) {
					toast.setView(view);
				    toast.show();
				}
			    if(isAttacking == true) {
					hitGhost(ghosts.get(i));
					isAttacking = false;
			    }
			}
		}
	}
	
	public void scanLeft(View view) {
		this.dirFacing--;
		if(this.dirFacing == -1)
			this.dirFacing = 1;
		isGhostInView(angles, 0);
	}
	
	public void scanRight(View view) {
		this.dirFacing++;
		if(this.dirFacing == 2)
			this.dirFacing = 0;
		isGhostInView(angles, 0);
	}

	public void bombGhost(View view) {
		if(bombs > 0) {
			scannerPlUpdate.cancel(true);
			bombs--;
			ghostsKilled += (angles.size()-1);
			angles.clear();
		}
	}
	
	public void attackGhost(View view) {
		if(ghosts.size() > 0) {
			isAttacking = true;
			isGhostInView(angles, count);
			count++;
			if(count == 5)
				count = 0;
		}
	}
	
	public void hitGhost(Ghost g) {
		if(ghosts.size() > 0) {
			if(g.getHitCount() < 5)
				g.setHitCount(g.getHitCount()+1);
			if(g.getHitCount() == 5) {
				ghosts.remove(ghosts.indexOf(g));
				this.ghostsKilled++;
				g.setHitCount(0);
			}
		}
	}
	
	public void map(View view) {
		Intent intent = new Intent ();
		intent.putExtra("ghostsKilled", origGhosts);
		intent.putExtra("numKilled", ghostsKilled);
		if(origGhosts.size() > 0)
			intent.putExtra("playerHealth", origGhosts.get(0).getPlayerHealth());
		intent.putExtra("coins", coinsNumber);
		intent.putExtra("bombnum", bombs);
		setResult(-2, intent);
		this.finish();
	}
	
	public int getDirFacing() {
		return dirFacing;
	}

	public void setDirFacing(int dirFacing) {
		this.dirFacing = dirFacing;
	}
}