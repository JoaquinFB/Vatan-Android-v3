package com.liverail.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
 * This sample will demonstrate the use of the ad SDK to display an ad
 * during a demo video that will be shown by the current activity.
 */
public class Example4 extends Activity {

    // ========================================================================
    // Class members
    // ========================================================================

    // The tag used by this class for logging
    private static final String TAG = "LiveRailExample3";

    // Main layout of this screen
    private RelativeLayout layout;

    // View used to display the main video content. This view will be
    // synchronized with the ad player using the ad listener methods.
    private VideoView videoView;

    // The position of the demo video when the ad is displayed.
    // The actual resume method of the VideoView class doesn't
    // work properly, so we'll resume the main video when the ad
    // ends by seeking to this position and restarting the
    // video from there.
    private int lastVideoPosition;
    
    // Indicator that will be displayed from the moment the screen
    // is loaded and until the ad is received and ready to be played.
    private ProgressBar progressBar;
    
    // The view that will display the actual ad video player
    private AdView adView;

    // This timer will be used to trigger the midroll ad
    // at a given moment after the main video has played.
    private CountDownTimer timer;

    // ========================================================================
    // Activity lifecycle methods
    // ========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example3);

        // Get references to the screen layout and relevant components
        layout = (RelativeLayout) findViewById(R.id.sample3_layout);
        videoView = (VideoView) findViewById(R.id.sample3_videoView);
        progressBar = (ProgressBar) findViewById(R.id.sample3_progress);

        // Start the main video content when the current activity is created
        startPlayer();
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

    private void initAd() {
        Log.d(TAG, "Ad init start");

        lastVideoPosition = videoView.getCurrentPosition();
        videoView.pause();
        videoView.setVisibility(View.INVISIBLE);
        
        progressBar.setVisibility(View.VISIBLE);
                
        // Create the ad view and add it to the view hierarchy
        adView = new AdView(this);
        
        // Enable debugging for this specific sample
        adView.setDebug(true);
        
        RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(adView, adViewLayoutParams);

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

        // Call the ad SDK in order to retrieve an ad
        adView.initAd(parameters);
    }

    private void startPlayer() {
        // Create and start the timer that will call the
        // ad SDK after the specified number of seconds
        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                initAd();
            }
        };
        timer.start();
        
        String uri = "http://cdn-static.liverail.com/swf/ui/uivideo.mp4";
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener(videoViewOnCompletionListener);
        videoView.start();
    }

    private void stopPlayer() {
        // Stop the demo video player
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
    }

    private void resumePlayer() {
    	videoView.setVisibility(View.VISIBLE);
        videoView.seekTo(lastVideoPosition);
        videoView.start();
        lastVideoPosition = 0;
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
	        resumePlayer();
		}
	};

	private final VPAIDEventListener onAdError = new VPAIDEventListener() {
		public void onEvent(VPAIDEvent event) {
			Log.d(TAG, "VPAID Event AdError");
			 layout.removeView(adView);
		     adView = null;
	         resumePlayer();
		}
	};   

}
