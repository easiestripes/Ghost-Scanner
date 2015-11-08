package com.example.workingmap;

import java.io.Serializable;
import java.util.Random;

import android.graphics.Rect;

public class Ghost implements Serializable {

    private double lat, lng;
	private Random rand = new Random();
	private int numOfGhosts, difficulty, hitCount = 0, playerHealth = 13;
	
	public Ghost(double pLat, double pLng, int difficulty) {
		this.lat = pLat + (rand.nextDouble() - 0.5)*0.0004;
		this.lng = pLng + (rand.nextDouble() - 0.5)*0.0013;
		this.numOfGhosts = 6 + difficulty*2;
		this.difficulty = difficulty;
	}
	
	public boolean equals(Object o) {
		return (o instanceof Ghost 
				 && ((Ghost) o).getLat() == this.getLat() 
				 && ((Ghost) o).getLng() == this.getLng());
	}
    
	public double compareTo(Ghost that) {
		if (this.getLat() != that.getLat()) 
			return this.getLat() - that.getLat();
		return this.getLng() - that.getLng();
	}
	
	public int getHitCount() {
		return this.hitCount;
	}
	
	public void setHitCount(int i) {
		this.hitCount = i;
	}
	
	public int setHitCountAndNewCount(int i) {
		this.hitCount = i;
		return this.hitCount;
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getNumOfGhosts() {
		return numOfGhosts;
	}

	public void setNumOfGhosts(int numOfGhosts) {
		this.numOfGhosts = numOfGhosts;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}
}
