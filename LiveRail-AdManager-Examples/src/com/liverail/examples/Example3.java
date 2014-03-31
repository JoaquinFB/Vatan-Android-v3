package com.liverail.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.liverail.library.events.VPAIDEventListener;
import com.liverail.library.events.VPAIDEvent;
import com.liverail.library.AdView;

/**
 * This example will demonstrate the use of the LiveRail SDK to display an ad
 * before the publisher's content video. 
 * LiveRail LogCat debug messages are disabled for this particular example.
 */
public class Example3 extends Activity {

    // ========================================================================
    // Class members
    // ========================================================================

    // The tag used by this class for logging
    private static final String TAG = "LiveRailExample2";

    // Main layout of this screen
    private RelativeLayout layout;

    // View used to display the local demo video after the ad
    // will have finished playing. This view will be synchronized
    // with the ad video player using the ad listener methods.
    private VideoView videoView;

    // Indicator that will be displayed from the moment the screen
    // is loaded and until the ad is received and ready to be played.
    private ProgressBar progressBar;

    // The view that will display the actual ad video player
    private AdView adView;

    // ========================================================================
    // Activity lifecycle methods
    // ========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example2);

        // Get references to the screen layout and relevant components
        layout = (RelativeLayout) findViewById(R.id.sample2_layout);
        videoView = (VideoView) findViewById(R.id.sample2_videoView);
        progressBar = (ProgressBar) findViewById(R.id.sample2_progress);
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
            adView.stopAd();
        }

        // Also stop the local video player in case it's running
        stopPlayer();

        // Allow the default behavior of the Back key
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
        
        // Disable debugging for this specific sample
        adView.setDebug(false);
        
        RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(adView, adViewLayoutParams);
        
        // Display the progress indicator again, which is now under the ad view
        layout.bringChildToFront(progressBar);
        
        // Initialize the LiveRail Runtime Parameters
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

        // The current activity will listen for ad events
        adView.addEventListener(VPAIDEvent.AdLoaded, onAdLoaded);
        adView.addEventListener(VPAIDEvent.AdImpression, onAdImpression);
        adView.addEventListener(VPAIDEvent.AdStopped, onAdStopped);
        adView.addEventListener(VPAIDEvent.AdError, onAdError);
       
        // Initialize the ad, and wait for initComplete or initError callbacks
        adView.initAd(parameters);
    }

    /**
     * Initialize and start the main content video player
     */
    private void startPlayer() {
        String uri = "http://cdn-static.liverail.com/swf/ui/uivideo.mp4";
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener(videoViewOnCompletionListener);
        videoView.start();
    }

    /**
     * Stop the main video player
     */
    private void stopPlayer() {
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
    }

    private final MediaPlayer.OnCompletionListener videoViewOnCompletionListener = new MediaPlayer.OnCompletionListener() {
  		public void onCompletion(MediaPlayer mp) {
  			Log.d(TAG, "MediaPlayer.onCompletion()");
  			finish();
  		}
  	};
    
    //========================================================================
    // LiveRail AdManager listener methods
    //========================================================================
    private final VPAIDEventListener onAdLoaded = new VPAIDEventListener() {
		public void onEvent(VPAIDEvent event) {
			Log.d(TAG, "VPAID Event AdLoaded");
			
			adView.bringToFront();
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
	        layout.removeView(adView);
	        adView = null;
			startPlayer();
		}
	};

	private final VPAIDEventListener onAdError = new VPAIDEventListener() {
		public void onEvent(VPAIDEvent event) {
			Log.d(TAG, "VPAID Event AdError");
	        layout.removeView(adView);
	        adView = null;
			startPlayer();
		}
	};
   

}
