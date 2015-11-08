package com.example.workingmap;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/**
 * 
 * http://developer.android.com/reference/android/os/AsyncTask.html
 * http://stackoverflow.com/questions/19154153/in-android-how-can-i-send-the-result-of-from-onpostexecute-to-other-activity?rq=1
 * http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
 * http://stackoverflow.com/questions/19607658/bitmap-too-large-to-be-uploaded-in-a-texture-differences-between-emulator-and-a
 * http://stackoverflow.com/questions/2748830/how-to-change-background-color-in-android-app
 * http://stackoverflow.com/questions/9801580/screen-orientation-and-values-in-manifest-xml
 * http://www.venatu.com/images/ge/red-circle.png
 * http://www.venatu.com/images/ge/blue-circle.png
 * http://developer.android.com/design/patterns/navigation.html#up-vs-back
 * http://stackoverflow.com/questions/15739526/centering-bitmap-marker-google-maps-android-api-v2
 * https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/model/BitmapDescriptor
 * https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/model/BitmapDescriptorFactory
 * https://developers.google.com/maps/documentation/javascript/examples/marker-remove
 * http://stackoverflow.com/questions/16005223/android-google-map-api-v2-current-location
 * http://stackoverflow.com/questions/17577935/how-to-get-current-lat-lng-position-on-gmaps-api-v3
 * http://stackoverflow.com/questions/16752015/how-would-i-make-my-google-maps-app-start-with-zoom-on-my-current-location
 * http://developer.android.com/reference/android/location/LocationListener.html
 * http://developer.android.com/reference/com/google/android/gms/common/GooglePlayServicesUtil.html#isGooglePlayServicesAvailable%28android.content.Context%29
 * http://developer.android.com/tools/help/proguard.html
 * http://developer.android.com/google/play-services/index.html
 * http://stackoverflow.com/questions/11278319/importing-project-gave-unable-to-resolve-target-android-7
 * http://developer.android.com/tools/publishing/app-signing.html#setup
 * http://stackoverflow.com/questions/12214467/how-to-obtain-signing-certificate-fingerprint-sha1-for-oauth-2-0-on-android
 * https://developers.google.com/maps/documentation/android/start#getting_the_google_maps_android_api_v2
 * http://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
 * http://www.mkyong.com/android/android-toast-example/
 * http://stackoverflow.com/questions/12243635/how-to-pass-arraylistbeanclass-from-one-activity-to-another
 * http://stackoverflow.com/questions/7571917/adding-image-to-toast
 * http://stackoverflow.com/questions/14216777/how-to-stop-an-asynctask-in-a-fragment-after-the-application-closes
 * http://stackoverflow.com/questions/4934333/android-how-to-count-time-over-a-long-period
 *
 */
public class SplashScreen extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() 
		    {
				Intent intent = new Intent(SplashScreen.this, MainMenu.class);
				startActivity(intent);
		    }
		};
		handler.postDelayed(r, 4000);		
	}
	

}
