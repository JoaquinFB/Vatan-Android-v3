package com.amvg.vatan.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;

import com.amvg.vatan.main.Home.AnimParams;
import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BreakingNews extends Activity implements AnimationListener {
	
	ListView listView;
	TextView typeText;
	TextView newsArticleText;
	TextView idText;
	TextView categoryIdText;
	TextView IDtext;
	String type;
	String newsArticle;
	String ID;
	String categoryId;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	
	
	public  View menu;
	public AppMap appmap;
	public ViewUtils viewUtils;
	   public  View app;
	     public boolean menuOut = false;
	     public static Context context; 
	     public  AnimParams animParams = new AnimParams();
	    public  ListView listView_menu;
	
	    
	    class ClickListener implements OnClickListener 
		{
	        @Override
	        public void onClick(View v) 
	        {
//	        	if (Home.HasBanner)
//				{
//					MadView.setVisibility(View.GONE);
//					ConvertView.setVisibility(View.GONE);
//				}
	            System.out.println("onClick " + new Date());
	            BreakingNews me = BreakingNews.this;
	            context = me;
	            Animation anim;
	            int w = app.getMeasuredWidth();
	            int h = app.getMeasuredHeight();
	            int left = (int) (getResources().getDisplayMetrics().density*255);
	            if (!menuOut) 
	            {//slide menünün açılışı
	                anim = new TranslateAnimation(0, left, 0, 0);
	                menu.setVisibility(View.VISIBLE);
	                animParams.init(left, 0, left + w, h);
	            } 
	            else 
	            { //slide menünün kapanışı
	                anim = new TranslateAnimation(0, -left, 0, 0);
	                animParams.init(0, 0, w, h);
	            }
	            anim.setDuration(500);
	            anim.setAnimationListener(me);
	            anim.setFillAfter(true);
	            anim.setRepeatCount(0);
	            app.startAnimation(anim);
	        }
	    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_breaking_news);
		 
		mGaInstance = GoogleAnalytics.getInstance(this);  //sayma kodu
		mGaTracker = mGaInstance.getTracker("UA-15581378-14"); //sayma kodu
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());
		
		
		viewUtils=new ViewUtils();
		
			menu = findViewById(R.id.menu);
	        app = findViewById(R.id.app);

	        viewUtils.printView("menu", menu);
	        viewUtils.printView("app", app);
			
	        listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
	        listView_menu.invalidateViews();
	        //listView_menu.setCacheColorHint(0);
//	        if (!isSlideFilled) 
//			{
		        MainMenuAccessData mainMenuAccessData=new MainMenuAccessData(getApplicationContext(),"Home", listView_menu);
		        mainMenuAccessData.execute("");
//			}
		        viewUtils.initListView(this, listView_menu, "Item ", 30, R.id.list_View_Menu, getApplicationContext(), BreakingNews.this);
		
		
		listView=(ListView)findViewById(R.id.list);
		
		
		
		final BreakingNewsAccessData breakingNews=new BreakingNewsAccessData(getApplicationContext(), listView, this);
		appmap=new AppMap(getApplicationContext(), BreakingNews.this);
		ImageView logo=(ImageView)findViewById(R.id.logoImage);
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				appmap.RunActivity("Home", "","", "");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
				//overridePendingTransition(R.anim.push_left_in, android.R.anim.slide_in_left);  
				//overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
				//appmap.LogoClick();
			}
		});
		ImageView menuLogo=(ImageView)findViewById(R.id.menuImage);
		menuLogo.setOnClickListener(new ClickListener());
//		menuLogo.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				appmap.RunActivity("MainMenu", "", "","");
//			}
//		});
//		menuLogo.setOnClickListener(new ClickListener());
		
		final String[] in={"TopCount=6"};
		ImageView refreshLogo=(ImageView)findViewById(R.id.refreshImage);
		refreshLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BreakingNewsAccessData refreshBreakingNews =new BreakingNewsAccessData(getApplicationContext(), listView, BreakingNews.this);
				refreshBreakingNews.execute(in);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	if(position!=0)
		    	{
//		    		try
//					{
//		    			
//					}
//		    		catch{
//		    			
//		    		}
//		    		
		    		try
					{
		    			appmap.RunActivity(((TextView)view.findViewById(R.id.contentType)).getText().toString(), "",((TextView)view.findViewById(R.id.ID)).getText().toString() ,"");
					} catch (Exception e)
					{
						// TODO: handle exception
					}
						
						
						
					
			        //Toast.makeText(getBaseContext(), (CharSequence) listView.getItemAtPosition(position), 1000).show();
			        //appmap.RunActivity(values[position][0], values[position][1], values[position][2], values[position][3]);
		    	}
		    }
		});
		try { 
			if (breakingNews.areFilesOK()) {
				breakingNews.readData();
			}else{
				breakingNews.execute(in);
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
//	     Send a screen view when the Activity is displayed to the user.
	    FlurryAgent.onStartSession(this, "J7LIJ4D8612IYIAF6REK");
	    mGaTracker.sendView("BreakingNews"); //sayma kodu
	    mGaTrackerGlobal.sendView("BreakingNews");
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




	void layoutApp(boolean menuOut) {
		Log.e("layoutApp","layoutApp");
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
                + animParams.bottom + "]");
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)app.getLayoutParams();
		if (menuOut)
		{
			params.setMargins(animParams.left, 0, -animParams.left, 0); 	
			params.gravity=Gravity.TOP;
		}
		else
		{
			params.setMargins(0, 0, 0, 0); 
		}
		app.setLayoutParams(params);
        app.clearAnimation();
    }
	
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		Log.e("onAnimationEnd","onAnimationEnd");
		System.out.println("onAnimationEnd");
		viewUtils.printView("menu", menu); 
		viewUtils.printView("app", app);
        menuOut = !menuOut;
        if (!menuOut) {
            menu.setVisibility(View.INVISIBLE);
            listView_menu.setEnabled(true);
            if (Controller!=null)
			{
            	if (!Controller.endsWith("BreakingNews") && IsMenuClicked)
    			{
    				appmap.RunActivity(Controller,CategoryName, "",CategoryID);
    				IsMenuClicked=false;
    			}
			}
        }
        layoutApp(menuOut);
	}


	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		Log.e("onAnimationRepeat","onAnimationRepeat");
	}


	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		Log.e("onAnimationStart","onAnimationStart");
	}

class ViewUtils {
		
	    private ViewUtils() {
	    	Log.e("ViewUtils","ViewUtils");
	    }

	    public void setViewWidths(View view, View[] views) {
	    	Log.e("setViewWidths","setViewWidths");
	        int w = view.getWidth();
	        int h = view.getHeight();
	        for (int i = 0; i < views.length; i++) {
	            View v = views[i];
	            v.layout((i + 1) * w, 0, (i + 2) * w, h);
	            printView("view[" + i + "]", v);
	        }
	    }

	    public void printView(String msg, View v) {
	    	Log.e("printView","printView");
	        System.out.println(msg + "=" + v);
	        if (null == v) {
	            return;
	        }
	        System.out.print("[" + v.getLeft());
	        System.out.print(", " + v.getTop());
	        System.out.print(", w=" + v.getWidth());
	        System.out.println(", h=" + v.getHeight() + "]");
	        System.out.println("mw=" + v.getMeasuredWidth() + ", mh=" + v.getMeasuredHeight());
	        System.out.println("scroll [" + v.getScrollX() + "," + v.getScrollY() + "]");
	    }

	    public void initListView(Context context, final ListView listView, String prefix, int numItems, int layout, final Context contextGetApplication, final Context contextThis) {
	        Log.e("initListView","initListView");
	        listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	listView.setEnabled(false);
	            	IsMenuClicked=true;
					Controller=((TextView) view.findViewById(R.id.controller)).getText().toString();
					CategoryName=((TextView) view.findViewById(R.id.empty)).getText().toString();
					CategoryID=((TextView) view.findViewById(R.id.CID)).getText().toString();
	                itemClickMenuCloser();
	            }
	        });
	    }
	    
	    public void itemClickMenuCloser(){
	    	Log.e("itemClickMenuCloser","itemClickMenuCloser");
	        Context context = BreakingNews.context;
	        Animation anim;
	        int w = app.getMeasuredWidth();
	        int h = app.getMeasuredHeight();
	        int left = (int) (getResources().getDisplayMetrics().density*255);
	        anim = new TranslateAnimation(0, -left, 0, 0);
	        animParams.init(0, 0, w, h);
	        anim.setDuration(500);
	        anim.setAnimationListener((AnimationListener) context);
	        anim.setFillAfter(true);
	        app.startAnimation(anim);
	    }
	}
}
