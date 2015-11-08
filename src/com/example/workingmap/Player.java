package com.example.workingmap;

import java.io.Serializable;
import java.util.TreeMap;

import android.content.ClipData.Item;
import android.graphics.Rect;

public class Player implements Serializable {


	private int health, coins;
	private double lat, lng;
	private boolean isDead = false;

	Player(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
		this.health = 17;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int d) {
		if ( this.health - d > 0 ) {
			this.health = health - d;
		} else {
			this.health = 0;
		}
	}

	public Boolean isDead() {
		if (this.health <= 0 ) {
			setIsDead(true);
			return true;
		}
		
		else return false;
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

	public boolean getIsDead() {
		return isDead;
	}

	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}	
}
