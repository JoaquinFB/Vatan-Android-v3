package com.amvg.vatan.main;


import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.liquidm.sdk.Ad;
import com.liquidm.sdk.AdListener;
import com.liquidm.sdk.AdView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoGallery extends FragmentActivity {
	private String ID;
	private String galleryTitleText;
	public static LinearLayout linearLayout;
	public static int width;
	public static int height;
	public static TextView categoryName;
	public static TextView galleryTitle;
	private TestFragmentAdapterPhotoGallery mAdapter;
	private ViewPager mPager;
	private PhotoGalleryAccessData accessData;
	public static boolean stopThread;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private View BannerDivider;
//	public static WebView webView;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_gallery);
		
		mGaInstance = GoogleAnalytics.getInstance(this); //sayım kodu
		
		mGaTracker = mGaInstance.getTracker("UA-15581378-14");  //sayım kodu
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());
		
		Display display = getWindowManager().getDefaultDisplay();
		
		final Point point = new Point();
	    try {
	        display.getSize(point);
	    } catch (java.lang.NoSuchMethodError ignore) { // Older device
	        point.x = display.getWidth();
	        point.y = display.getHeight();
	    }
		
		
		width = point.x;
		height = point.y;
		linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
		
		linearLayout.getLayoutParams().width=width;
//		linearLayout.getLayoutParams().height=height;
		
		mAdapter=new TestFragmentAdapterPhotoGallery(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		
		mPager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
	    });
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     ID = extras.getString("id");
		     galleryTitleText=extras.getString("galleryTitle");
		}
		
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.BannerEnabled)
		{
			BannerDivider.setVisibility(View.VISIBLE);	
		}
		
		
		ImageView logoImage=(ImageView)findViewById(R.id.logoImage);
		logoImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppMap appmap=new AppMap(getApplicationContext(),PhotoGallery.this);
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
		
		ImageView backImage=(ImageView)findViewById(R.id.backImage);
		backImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					AppMap.DownloadBannerData.bannerEnabled="false";
				} catch (Exception e) {
					// TODO: handle exception
				}
			   try {
				   Video.backgroungLayout.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO: handle exception
				}
				   try {
					   Home.setClickable(true);
					   Home.homeMainLayout.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO: handle exception
				}
				finish();
			}
		});
		
		AdView adView = (AdView)findViewById(R.id.adView);
		adView.setListener(new AdListener() 
		{
			@Override
			public void onAdDismissScreen(Ad ads) {				
			}

			@Override
			public void onAdFailedToLoad(Ad ads) {				
			}

			@Override
			public void onAdLeaveApplication(Ad ads) {				
			}

			@Override
			public void onAdLoad(Ad ads) {				
			}

			@Override
			public void onAdPresentScreen(Ad ads) {				
			}			
		});
		
		
//		webView=(WebView)findViewById(R.id.webView);
//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//		
//		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
//		{
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				webView.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}
//			else
//			{
//				webView.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//			webView.getLayoutParams().height=(int) ((Integer.parseInt(AppMap.DownloadBannerData.bannerHeight)+2)*SplashScreen.scale);
//		}
//		else
//		{
//			webView.getLayoutParams().width=0;
//			webView.getLayoutParams().height=0;
//		}
		
		
		
//		((TextView)findViewById(R.id.date)).setText("Galeri - "+categoryNameText);
//		((TextView)findViewById(R.id.galleryTitle)).setText(galleryTitleText);
		categoryName=(TextView)findViewById(R.id.date);
		galleryTitle=(TextView)findViewById(R.id.galleryTitle);
//		PhotoGallery.categoryName.setText("Galeri - "+categoryNameText);
		PhotoGallery.galleryTitle.setText(galleryTitleText);
		
		final String[] in={"in"};
		accessData= new PhotoGalleryAccessData(getApplicationContext(), ID, mAdapter, mPager,PhotoGallery.this);
		
		accessData.execute(in);
		
		setTab();
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		comScore.onEnterForeground();
}

@Override
	public void onPause() 
	{
		super.onPause();
		// Notify comScore about lifecycle usage
		comScore.onExitForeground();
	}

	
	@Override
	  public void onStart() {
	    super.onStart();
	    // Send a screen view when the Activity is displayed to the user.
	    mGaTracker.sendView("PhotoGallery - "+ID); //sayma kodu
	    mGaTrackerGlobal.sendView("PhotoGallery - "+ID);
	    FlurryAgent.onStartSession(this, "J7LIJ4D8612IYIAF6REK");
	  }
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	    // Send a screen view when the Activity is displayed to the user.
//	    mGaTracker.sendView("/PhotoGallery"); //sayım kodu
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	//PhotoGalleryAccessData.DownloadData.cancel(true);
	    	stopThread=true;
	    	finish();
	    	try {
				AppMap.DownloadBannerData.bannerEnabled="false";
			} catch (Exception e) {
				// TODO: handle exception
			}
		   try {
			   Video.backgroungLayout.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
			}
			   try {
				   Home.homeMainLayout.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
			}
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	@Override
	public void onBackPressed() {
		try {
			AppMap.DownloadBannerData.bannerEnabled="false";
		} catch (Exception e) {
			// TODO: handle exception
		}
	   try {
		   Video.backgroungLayout.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		   try {
			   Home.setClickable(true);
			   Home.homeMainLayout.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO: handle exception
		}
	   finish();
	}
	
	private void setTab()
	{
			mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			
				
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				try {
					PhotoGallery.linearLayout.getLayoutParams().height=(int) (60+Global.calculateTextHeight(PhotoGalleryAccessData.text[arg0], PhotoGalleryAccessData.textLength[arg0], PhotoGallery.width)*getApplication().getResources().getDisplayMetrics().density+(int) Math.round(PhotoGallery.width*PhotoGalleryAccessData.bitmapHeight[arg0]/PhotoGalleryAccessData.bitmapWidth[arg0]));
				} catch (Exception e) {
					// TODO: handle exception
					PhotoGallery.linearLayout.getLayoutParams().height=850;
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.share_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case R.id.shareButton:
        	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://fotogaleri.gazetevatan.com/m/"+this.ID+"/1/m");
			startActivity(Intent.createChooser(sharingIntent,"Share via"));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    } 

}
