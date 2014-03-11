package com.amvg.vatan.main;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;

import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoCategory extends Activity {
	private String categoryNameText;
	private String categoryIDtext;
	private ListView listView;
	private String isVideo;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_category);
		
		mGaInstance = GoogleAnalytics.getInstance(this);  //sayım kodu
		mGaTracker = mGaInstance.getTracker("UA-15581378-14"); //sayım kodu
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());

		
		final String[] in={"in"};
		final AppMap appmap=new AppMap(getApplicationContext(),VideoCategory.this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			categoryNameText = extras.getString("categoryName");
			categoryIDtext=extras.getString("categoryID");
			isVideo=extras.getString("isVideo");
		    //categoryName.setText(categoryNameText);  //kategori adını burada yakalıyoruz.
		}
		listView=(ListView)findViewById(R.id.list);
		
		ImageView logoImage=(ImageView)findViewById(R.id.logoImage);
		logoImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				finish();
			}
		});
		
		ImageView refreshImage=(ImageView)findViewById(R.id.refreshImage);
		refreshImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isVideo.equals("1")) 
				{
					VideoCategoryAccessData accessData=new VideoCategoryAccessData(getApplicationContext(), listView, VideoCategory.this, categoryNameText, categoryIDtext,isVideo);
					accessData.execute(in);
				} 
				else
				{
					GalleryCategoryAccessData accessData=new GalleryCategoryAccessData(getApplicationContext(), listView, VideoCategory.this, categoryNameText, categoryIDtext,isVideo);
					accessData.execute(in);
				}
			}
		});
		
		if (isVideo.equals("1")) 
		{
			((ImageView)findViewById(R.id.logoImage)).setImageResource(R.drawable.navigation_bar_logo_vatantv);
			final VideoCategoryAccessData accessData=new VideoCategoryAccessData(getApplicationContext(), listView, this, categoryNameText, categoryIDtext,isVideo);
			
			try { 
				if (accessData.isFileOK()) {
					accessData.fillData();
				}else{
					accessData.execute(in);
				}
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			((ImageView)findViewById(R.id.logoImage)).setImageResource(R.drawable.navigation_bar_logo);
			final GalleryCategoryAccessData accessData=new GalleryCategoryAccessData(getApplicationContext(), listView, this, categoryNameText, categoryIDtext,isVideo);
			
			try { 
				if (accessData.isFileOK()) {
					accessData.fillData();
				}else{
					accessData.execute(in);
				}
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
				finish();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try
				{
					appmap.RunActivity(((TextView)arg1.findViewById(R.id.contentType)).getText().toString(), "1", ((TextView)arg1.findViewById(R.id.ID)).getText().toString(), ((TextView)arg1.findViewById(R.id.haberText)).getText().toString(), categoryIDtext);
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				// TODO Auto-generated method stub
				
			}
		});
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
//	    // Send a screen view when the Activity is displayed to the user.  //sayım kodu
	    if (isVideo.equals("1"))
		{
	    	mGaTracker.sendView("VatanTVCategory - "+categoryNameText);
	    	mGaTrackerGlobal.sendView("VatanTVCategory - "+categoryNameText);
		}
	    else
	    {
	    	mGaTracker.sendView("GalleryCategory - "+categoryNameText);
	    	mGaTrackerGlobal.sendView("GalleryCategory - "+categoryNameText);
	    }
	    FlurryAgent.onStartSession(this, "J7LIJ4D8612IYIAF6REK");
	  }
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
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
	   finish();
	}

}
