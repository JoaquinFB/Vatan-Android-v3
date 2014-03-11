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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ColumnistArticles extends Activity {
	
	private String ID;
	ListView listView;
	TextView SelectedIDtext;
	private String SelectedID;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_columnist_articles);
		
		mGaInstance = GoogleAnalytics.getInstance(this); //sayım kodu
		mGaTracker = mGaInstance.getTracker("UA-15581378-14");  //sayım kodu
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());
		
		final AppMap appmap=new AppMap(getApplicationContext(),ColumnistArticles.this);
		String[] in={"in"};
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     ID = extras.getString("id");
		}
		TextView IDtextView=(TextView)findViewById(R.id.conText);
		IDtextView.setText(ID);
		listView=(ListView)findViewById(R.id.list);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	if(position!=0)
		    	{
		    		try
					{
		    			SelectedIDtext=(TextView)view.findViewById(R.id.ID);
						SelectedID=SelectedIDtext.getText().toString();
						appmap.RunActivity("ColumnistArticle", "", SelectedID,"");
					} catch (Exception e)
					{
						// TODO: handle exception
					}
						
		    	}
		    }
		});
		
		ColumnistArticlesAccessData accessData=new ColumnistArticlesAccessData(getApplicationContext(), listView,ID,ColumnistArticles.this);
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
		
		ImageView logo=(ImageView)findViewById(R.id.logoImage);
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
				// TODO Auto-generated method stub
				//appmap.RunActivity(context, controller, ref, title, CID)
			}
		});
		ImageView menuLogo=(ImageView)findViewById(R.id.backImage);
		menuLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				AppMap.DownloadBannerData.bannerEnabled="false";
				//appmap.RunActivity("MainMenu", "", "","");
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
	    FlurryAgent.onStartSession(this, "J7LIJ4D8612IYIAF6REK");
	    
	    // Send a screen view when the Activity is displayed to the user.
	    mGaTracker.sendView("ColumnistArticles - "+ID); //sayma kodu
	    mGaTrackerGlobal.sendView("ColumnistArticles - "+ID);
	  }
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	public void onBackPressed() {
	   AppMap.DownloadBannerData.bannerEnabled="false";
	   finish();
	}
}
