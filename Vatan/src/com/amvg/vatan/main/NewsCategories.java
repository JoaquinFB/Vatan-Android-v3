package com.amvg.vatan.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;

import com.amvg.vatan.main.Columnists.ViewUtils;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class NewsCategories extends Activity implements AnimationListener{

	private ListView listView;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	
	private AppMap appmap;
	
	public  View menu;
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
		        System.out.println("onClick " + new Date());
		        NewsCategories me = NewsCategories.this;
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
		        app.startAnimation(anim);
		    }
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_categories);
		
		mGaInstance = GoogleAnalytics.getInstance(this);  //sayım kodu
		mGaTracker = mGaInstance.getTracker("UA-15581378-14");  //sayım kodu
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
        
        MainMenuAccessData mainMenuAccessData=new MainMenuAccessData(getApplicationContext(),"Home", listView_menu);
        mainMenuAccessData.execute("");
        viewUtils.initListView(this, listView_menu, "Item ", 30, R.id.list_View_Menu, getApplicationContext(), NewsCategories.this);
		
		appmap=new AppMap(getApplicationContext(), NewsCategories.this);
		listView=(ListView)findViewById(R.id.list);
		String[] in={"in"};
		/*String[] in=new String[23];
		final String[][] vals = {{"Siyaset","NewsCategory"}, 
								{"Ekonomi","NewsCategory"}, 
								{"Spor","NewsCategory"}, 
								{"Dünya","NewsCategory"}, 
								{"Gündem","NewsCategory"}, 
								{"Magazin","NewsCategory"}, 
								{"Bebek ve Çocuk","Other"}, 
								{"Cadde","Other"}, 
								{"Cumartesi","Other"}, 
								{"Eğitim","Other"}, 
								{"Hayat","Other"}, 
								{"Kadın","Other"},
								{"Kişisel Bakım","Other"},
								{"Kitap","Other"},
								{"Konut - Emlak","Other"},
								{"Kültür Sanat","Other"},
								{"Milliyet Tatil","Other"},
								{"Moda","Other"},
								{"Mutfak","Other"},
								{"Otomobil","Other"},
								{"Pazar","Other"},
								{"Sağlık","Other"},
								{"Teknoloji","Other"}};
		NewsCategoriesAdapter adapter=new NewsCategoriesAdapter(getApplicationContext(), vals, in);
        listView.setAdapter(adapter);
        itemToggled = new boolean[vals.length];
        Arrays.fill(itemToggled, false);*/
		
		NewsCategoriesAccessData accessData=new NewsCategoriesAccessData(getApplicationContext(), listView,NewsCategories.this);
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
        
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	//categoryName=(TextView)view.findViewById(R.id.empty);
		    	//Log.e("categoryName",categoryName.getText().toString());
		    	try
				{
		    		appmap.RunActivity("NewsCategory", ((TextView)view.findViewById(R.id.empty)).getText().toString(), "",((TextView)view.findViewById(R.id.CID)).getText().toString());
				} catch (Exception e)
				{
					// TODO: handle exception
				}
		    	
		    	
		    }
		});
        
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
	    mGaTracker.sendView("NewsCategories"); //sayma kodu
	    mGaTrackerGlobal.sendView("NewsCategories");
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
	   AppMap.DownloadBannerData.bannerEnabled="false";
	   finish();
	}
	
	void layoutApp(boolean menuOut) 
	{
        //System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","+ animParams.bottom + "]");
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
	public void onAnimationEnd(Animation animation) 
	{
		System.out.println("onAnimationEnd");
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
        menuOut = !menuOut;
        if (!menuOut) 
        {
            menu.setVisibility(View.INVISIBLE);
            listView_menu.setEnabled(true);
            if (Controller!=null)
			{
            	if (!Controller.endsWith("NewsCategories") && IsMenuClicked)
    			{
    				appmap.RunActivity(Controller,CategoryName, "",CategoryID);
    				IsMenuClicked=false;
    			}
			}
        }
        layoutApp(menuOut);
	}
 
	@Override
	public void onAnimationRepeat(Animation animation) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationStart(Animation animation) 
	{
		// TODO Auto-generated method stub
	}
	
	class ViewUtils 
	{
	    private ViewUtils() 
	    {
	    	
	    }

	    public void setViewWidths(View view, View[] views) 
	    {
	        int w = view.getWidth();
	        int h = view.getHeight();
	        for (int i = 0; i < views.length; i++) 
	        {
	            View v = views[i];
	            v.layout((i + 1) * w, 0, (i + 2) * w, h);
	            printView("view[" + i + "]", v);
	        }
	    }

	    public void printView(String msg, View v) 
	    {
	        System.out.println(msg + "=" + v);
	        if (null == v)
	        {
	            return;
	        }
	        System.out.print("[" + v.getLeft());
	        System.out.print(", " + v.getTop());
	        System.out.print(", w=" + v.getWidth());
	        System.out.println(", h=" + v.getHeight() + "]");
	        System.out.println("mw=" + v.getMeasuredWidth() + ", mh=" + v.getMeasuredHeight());
	        System.out.println("scroll [" + v.getScrollX() + "," + v.getScrollY() + "]");
	    }

	    public void initListView(Context context, final ListView listView, String prefix, int numItems, int layout, final Context contextGetApplication, final Context contextThis) 
	    {
	        listView.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	            {
	            	listView.setEnabled(false);
	            	IsMenuClicked=true;
					Controller=((TextView) view.findViewById(R.id.controller)).getText().toString();
					CategoryName=((TextView) view.findViewById(R.id.empty)).getText().toString();
					CategoryID=((TextView) view.findViewById(R.id.CID)).getText().toString();
	                itemClickMenuCloser();
	            }
	        });
	    }
	    
	    public void itemClickMenuCloser()
	    {
	        Context context = NewsCategories.context;
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
