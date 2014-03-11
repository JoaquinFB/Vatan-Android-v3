package com.amvg.vatan.main;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class InterstitialBanner extends Activity {
	private WebView webView;
	private String URL;
	private String HTML;
	private String ShowCloseButton;
	private RelativeLayout rl;
	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interstitial_banner);
		
		mGaInstance = GoogleAnalytics.getInstance(this);  //sayım kodu
	    mGaTracker = mGaInstance.getTracker("UA-15581378-14");
		
		this.webView=(WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
		
		rl=(RelativeLayout)findViewById(R.id.closeButtonLayout);
		rl.bringToFront();
		rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     URL = extras.getString("URL");
		     HTML = extras.getString("HTML");
		     ShowCloseButton=extras.getString("ShowCloseButton");
		}
		if (ShowCloseButton.equals("false")) 
		{
			((ImageView)findViewById(R.id.closeButton)).setVisibility(View.GONE);
		}
		if (URL.equals("")) 
		{
			webView.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(false, HTML), "text/html", "UTF-8", "");
		}
		else
		{
			webView.loadUrl(URL);
		}
		
	}
	@Override
	  public void onStart() {
	    super.onStart();
	    // Send a screen view when the Activity is displayed to the user.
	    mGaTracker.sendView("/Interstitial"); //sayma kodu
	  }

}
