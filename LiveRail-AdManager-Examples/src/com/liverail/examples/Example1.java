package com.liverail.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.liverail.library.AdView;
import com.liverail.library.events.VPAIDEvent;
import com.liverail.library.events.VPAIDEventListener;


public class Example1 extends Activity {

    // ========================================================================
    // Class members
    // ========================================================================

    // The tag used by this class for logging
    private static final String TAG = "LiveRailExample1";

    // Main layout of this screen
    private ViewGroup layout;

    // Indicator that will be displayed from the moment the screen
    // is loaded and until the ad is received and ready to be played.
    protected View progressBar;

    // The view that will display the actual ad video player
    protected AdView adView;

    // ========================================================================
    // Activity lifecycle methods
    // ========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example1);

        // Get references to the screen layout and relevant components
        layout = (ViewGroup) findViewById(R.id.sample1_layout);
        progressBar = findViewById(R.id.sample1_progressContainer);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start initializing the ad when the screen loads
        if (adView == null) {
            initAd();
        }
    }

    @Override
    public void onBackPressed() {
        // Stop the ad before quiting the current screen
        if (adView != null) {
            //adView.stopAd();
        }

        // Allow the default behaviour of the Back key
        super.onBackPressed();
    }

    // ========================================================================
    // Private methods
    // ========================================================================

    /**
     * Initialize the ad view and make the call to the SDK.
     */
    private void initAd() {
        Log.i(TAG, "----- Ad init start -----");

        // Create the ad view and add it to the view hierarchy
        adView = new AdView(this);
        
        // Enable debugging for this specific sample
        adView.setDebug(true);
                
        RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(adView, adViewLayoutParams);
        
        // Display the progress indicator again, which is now under the ad view
        layout.bringChildToFront(progressBar);
        progressBar.setVisibility(View.VISIBLE);
        
        // Initialize the LR_ Run-time Parameters
        Map<String, String> parameters = new HashMap<String, String>();
        
        // Retrieve LR_ parameters from the Main activity
        Bundle be = getIntent().getExtras();
        if(be != null){
        	Set<String> keys = be.keySet();
            for (String key : keys) {
                if(key.startsWith("LR_")){
                	parameters.put(key, be.get(key).toString());
                }
            }
        }
        
        // Listen for ad events
        adView.addEventListener(VPAIDEvent.AdLoaded, onAdLoaded);
        adView.addEventListener(VPAIDEvent.AdImpression, onAdImpression);
        adView.addEventListener(VPAIDEvent.AdStopped, onAdStopped);
        adView.addEventListener(VPAIDEvent.AdError, onAdError);

        // Call the ad SDK in order to retrieve an ad
        adView.initAd(parameters);
    }

    // ========================================================================
    // Ad listener methods
    // ========================================================================
    private final VPAIDEventListener onAdLoaded = new VPAIDEventListener() {
		public void onEvent(VPAIDEvent event) {
			Log.d(TAG, "VPAID Event AdLoaded");
			// Display the ad
	        adView.startAd();
		}
	};
	
	private final VPAIDEventListener onAdImpression = new VPAIDEventListener() {
		public void onEvent(VPAIDEvent event) {
			Log.d(TAG, "VPAID Event AdImpression");
	        // Hide the progress on ad impression
			progressBar.setVisibility(View.INVISIBLE);
		}
	};
	
	private final VPAIDEventListener onAdStopped = new VPAIDEventListener() {
		public void onEvent(VPAIDEvent event) {
			Log.d(TAG, "VPAID Event AdStopped");
			progressBar.setVisibility(View.INVISIBLE);
	        finish();
		}
	};

	private final VPAIDEventListener onAdError = new VPAIDEventListener() {
		public void onEvent(VPAIDEvent event) {
			Log.d(TAG, "VPAID Event AdError");
			progressBar.setVisibility(View.INVISIBLE);
	        finish();
		}
	};
}
