package com.example.workingmap;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlayGame extends FragmentActivity implements
														ConnectionCallbacks,
														OnConnectionFailedListener,
														LocationListener,
														AsyncData {
	
	private GoogleMap gMap;
    private LocationClient locationClient;
    private double lat, lng;
	private boolean isFirstDraw = true, isAlert = false, isScanner = false;
	private Marker marker, circleRadius;
    private ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> closeToPlayerGhosts = new ArrayList<Ghost>();
    private ArrayList<Marker> ghostMarkers = new ArrayList<Marker>();
    private ArrayList<Marker> otherMarkers = new ArrayList<Marker>();
    private ArrayList<Integer> ghostsToRemove = new ArrayList<Integer>();
    private ArrayList<Integer> ghostsToRemoveNearby = new ArrayList<Integer>();
    private int width, height, timeBegin, pauseTime, storeTime, difficulty, respawn, ghostsKilled, timeSurvived;
    private LatLng latlng;
    private int bombsNumber = 2, ghostAttack = 0, coinsNumber = 100;
    private Player player = new Player(this.lat, this.lng);
    private UpdateGhostMap gMapUpdate;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(16)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.difficulty = extras.getInt("level");
		}
        
		timeBegin = (int) System.currentTimeMillis();
		gMapUpdate = new UpdateGhostMap();
		gMapUpdate.execute(closeToPlayerGhosts);
		gMapUpdate.dataPasser = this;
    }
    
    @Override
    public void updatedHealth(int playerHealth) {
    	closeToPlayerGhosts.get(0).setPlayerHealth(playerHealth);
    }
    
	@Override
	public void gameOver() {
		int timeEnd = (int) System.currentTimeMillis();
		int time = (timeEnd - timeBegin - pauseTime - storeTime)/1000;
		coinsNumber += (ghostsKilled*10);
		Intent intent = new Intent(this, GameOver.class);
		intent.putExtra("ghostskills", ghostsKilled);
		intent.putExtra("coins", coinsNumber);
		intent.putExtra("time", time);
		startActivity(intent);
	}
    
	public void pause(View view) {
		Intent intent = new Intent(this, PauseGame.class);
		startActivityForResult(intent, 1);
	}
    
	public void itemStore(View view){
		Intent intent = new Intent(this, ItemStore.class);
		intent.putExtra("bombnum", bombsNumber);
		intent.putExtra("coins", coinsNumber);
		startActivityForResult(intent, 1);
	}
	
	public void scanner(View view){
		if(closeToPlayerGhosts.size() > 0) {
			int timeMapEnd = (int) System.currentTimeMillis();
			int timeMapToScan = timeMapEnd - timeBegin - pauseTime - storeTime;
			Intent intent = new Intent(this, Scanner.class);
			intent.putExtra("player", this.player);
			intent.putExtra("ghosts", this.closeToPlayerGhosts);
			intent.putExtra("bombnum", bombsNumber);
			intent.putExtra("coins", coinsNumber);
			intent.putExtra("timeMap", timeMapToScan);
			intent.putExtra("ghostskills", ghostsKilled);
			startActivityForResult(intent, 1);
		} else {
			Toast.makeText(getApplicationContext(), "There are no nearby ghosts", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == -3) {
        	pauseTime = data.getExtras().getInt("pauseTime");
        }
		if (resultCode == -1) {
        	bombsNumber = data.getExtras().getInt("bombnum");
        	coinsNumber = data.getExtras().getInt("coins");
        	storeTime = data.getExtras().getInt("storeTime");
        }
        if (resultCode == -2) {
        	bombsNumber = data.getExtras().getInt("bombnum");
        	coinsNumber = data.getExtras().getInt("coins");
        	updatedHealth(data.getExtras().getInt("playerHealth"));
        	ghostsKilled = data.getExtras().getInt("numKilled");
        	ArrayList<Ghost> killedGhosts = new ArrayList<Ghost>();
        	killedGhosts = (ArrayList<Ghost>) data.getExtras().getSerializable("ghostsKilled");
        	for(int i = 0; i < ghosts.size(); i++) {
        		for(Ghost k : killedGhosts) {
        			if(ghosts.get(i).compareTo(k) == 0) {
        				ghostsToRemove.add(i);
        				break;
        			}
        		}
        	}
        	for(int i = 0; i < closeToPlayerGhosts.size(); i++) {
        		for(Ghost k : killedGhosts) {
        			if(ghosts.get(i).compareTo(k) == 0) {
        				ghostsToRemoveNearby.add(i);
        				break;
        			}
        		}
        	}
        	for(int j = ghostsToRemove.size()-1; j >= 0; j--) {
        		ghosts.remove(j);
        		ghostMarkers.get(j).remove();
        	}
        	for(int j = ghostsToRemoveNearby.size()-1; j >= 0; j--) {
        		closeToPlayerGhosts.remove(j);
        	}
        	isScanner = true;
        }
        if(closeToPlayerGhosts.size() != 0 && isScanner == false) {
			gMapUpdate = new UpdateGhostMap();
			gMapUpdate.execute(closeToPlayerGhosts);
			gMapUpdate.dataPasser = this;
        }
        isScanner = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        locationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationClient != null) {
            locationClient.disconnect();
        }
        gMapUpdate.cancel(true);
    }

    private void setUpMapIfNeeded() {
        if (gMap == null) {
            gMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (gMap != null) {
                gMap.setMyLocationEnabled(true);
                setUpMap();
            }
        }
    }
    
    private void setUpMap() {
        gMap.getUiSettings().setZoomControlsEnabled(false);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
        gMap.getUiSettings().setAllGesturesEnabled(false);
    }

    private void setUpLocationClientIfNeeded() {
        if (locationClient == null) {
            locationClient = new LocationClient(
                    getApplicationContext(),
                    this,
                    this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    	closeToPlayerGhosts.clear();
    	this.lat = location.getLatitude();
    	this.lng = location.getLongitude();
        latlng = new LatLng(this.lat, this.lng);
        player.setLat(this.lat);
        player.setLng(this.lng);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 20));
        if(isFirstDraw == true) {
    		for (int i = 0; i < (6 + this.difficulty*2); i++) {
    			Ghost g = new Ghost(this.lat, this.lng, this.difficulty);
    			this.ghosts.add(g);
    			while (ghosts.get(i).getLat() > this.getLat() - 0.0001 && ghosts.get(i).getLat() < this.getLat() + 0.0001 && ghosts.get(i).getLng() > this.getLng() - 0.0001 && ghosts.get(i).getLng() < this.getLng() + 0.0001) {
					ghosts.remove(i);
    				g = new Ghost(this.getLat(), this.getLng(), this.difficulty);
    				this.ghosts.add(i, g);
    			}
			    Marker marker = gMap.addMarker(new MarkerOptions()
			        .position(new LatLng(ghosts.get(i).getLat(), ghosts.get(i).getLng()))
			        .anchor(0.5f, 0.5f)
			        .icon(BitmapDescriptorFactory.fromAsset("ghost.png")));
		    	ghostMarkers.add(i, marker);
    		}
    		circleRadius = gMap.addMarker(new MarkerOptions()
		        .position(latlng)
		        .anchor(0.5f, 0.5f)
		        .icon(BitmapDescriptorFactory.fromAsset("blue_circle_radius_overlay.png")));
    		otherMarkers.add(0, circleRadius);
        	isFirstDraw = false;
        }
        while(ghosts.size() < (6 + this.difficulty*2))
    		ghosts.add(new Ghost(this.getLat(), this.getLng(), this.difficulty));
        drawGhosts(latlng);
    }
    
    public void drawGhosts(LatLng l) {
    	for(int i = 0; i < ghosts.size(); i++) {
    		if(ghosts.get(i).getLat() < this.getLat() - 0.0003 && ghosts.get(i).getLat() > this.getLat() + 0.0003 && ghosts.get(i).getLng() < this.getLng() - 0.00085 && ghosts.get(i).getLng() > this.getLng() + 0.00085) {
				ghosts.remove(i);
				Ghost g = new Ghost(this.getLat(), this.getLng(), this.difficulty);
				ghosts.add(i, g);
				Marker marker = gMap.addMarker(new MarkerOptions()
		        	.position(new LatLng(ghosts.get(i).getLat(), ghosts.get(i).getLng()))
		        	.anchor(0.5f, 0.5f)
		        	.icon(BitmapDescriptorFactory.fromAsset("ghost.png")));
				ghostMarkers.add(i, marker);
			}
    		
			ghostMarkers.get(i).remove();
			Marker marker = gMap.addMarker(new MarkerOptions()
		        .position(new LatLng(ghosts.get(i).getLat(), ghosts.get(i).getLng()))
		        .anchor(0.5f, 0.5f)
		        .icon(BitmapDescriptorFactory.fromAsset("ghost.png")));
			ghostMarkers.set(i, marker);
			
			if(ghosts.get(i).getLat() > this.getLat() - 0.00012 && ghosts.get(i).getLat() < this.getLat() + 0.00012 && ghosts.get(i).getLng() > this.getLng() - 0.00012 && ghosts.get(i).getLng() < this.getLng() + 0.00012) {
				isAlert = true;
				closeToPlayerGhosts.add(ghosts.get(i));
			}
    	}
    	if(isAlert == true) {
			otherMarkers.get(0).remove();
			circleRadius = gMap.addMarker(new MarkerOptions()
		        .position(l)
		        .anchor(0.5f, 0.5f)
		        .icon(BitmapDescriptorFactory.fromAsset("red_circle_radius_overlay.png")));
			otherMarkers.set(0, circleRadius);
		} else {
			otherMarkers.get(0).remove();
			circleRadius = gMap.addMarker(new MarkerOptions()
		        .position(l)
		        .anchor(0.5f, 0.5f)
		        .icon(BitmapDescriptorFactory.fromAsset("blue_circle_radius_overlay.png")));
			otherMarkers.set(0, circleRadius);
		}
    	isAlert = false;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        locationClient.requestLocationUpdates(
                REQUEST,
                this);
    }

    @Override
    public void onDisconnected() {}

    @Override
    public void onConnectionFailed(ConnectionResult result) {}
    
    public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}
}
