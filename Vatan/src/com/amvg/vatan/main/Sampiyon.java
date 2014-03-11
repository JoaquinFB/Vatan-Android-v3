package com.amvg.vatan.main;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.amvg.vatan.main.Home.AnimParams;
import com.amvg.vatan.main.Home.ClickListener;
import com.amvg.vatan.main.Video.ViewUtils;
import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class Sampiyon extends FragmentActivity implements AnimationListener
{
	public int dikeyVideoSay_hp;
	private AppMap appmap;
	public static Context context;
	public static int dikeyVideoSay;
	public View menu;
	public ViewUtils viewUtils;
	public View app;
	public boolean menuOut = false;
	public AnimParams animParams = new AnimParams();
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	public ListView listView_menu;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private View BannerDivider;
	public static float scale;
	public static boolean imageClickControl;
	public static ArrayList<DataModelNewsImageLoad> Array_HomePage_Load;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	public ScrollViewExt sv;
	private int videoRowShowCount = 0;
	public static ArrayList<DataModelNewsImageLoad> Array_TopHeadlinesC_Load;
	private String DirName = "/sdcard/Vatan/sampiyon/";
	private LinearLayout TopHeadlinesC_layout; // guncel
//	private LinearLayout HomePage_layout;
	private LayoutInflater LayInfalater; // guncel
	public static TextView weatherDate;
	public static TextView weatherInfo;
	public static ImageView weatherImage;
	public static LinearLayout homeMainLayout;
	private Display display;
	private DisplayMetrics outMetrics;
	public float dpHeight;
	private ListView listView;
	private TestFragmentAdapter mAdapter;
	private int videoRowShowCount_hp = 0;
	public static float dpHeight_hp;
	
	private boolean AutoRefresh;
	
	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Log.e("bas", "bas");
			System.out.println("onClick " + new Date());
			Sampiyon me = Sampiyon.this;
			context = me;
			Animation anim;
			int w = app.getMeasuredWidth();
			int h = app.getMeasuredHeight();
			int left = (int) (getResources().getDisplayMetrics().density * 255);
			Log.e("Menu Genisligi=", Integer.toString(left));
			if (!menuOut)//slide menünün açılışı
			{
				anim = new TranslateAnimation(0, left, 0, 0);
				menu.setVisibility(View.VISIBLE);
				animParams.init(left, 0, left + w, h);
			} 
			else
			{ // slide menünün kapanışı
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sampiyon);
		AutoRefresh=false;
		
		ImageView menuLogo=(ImageView)findViewById(R.id.menuImage);
	    menuLogo.setOnClickListener(new ClickListener());
	    
	    dikeyVideoSay_hp = 0;
		viewUtils = new ViewUtils();
		menu = findViewById(R.id.menu);
		app = findViewById(R.id.app);
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
		listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
		listView_menu.invalidateViews();
		MainMenuAccessData mainMenuAccessData = new MainMenuAccessData(getApplicationContext(), "sampiyon" ,  listView_menu);
		mainMenuAccessData.execute("");
		viewUtils.initListView(this, listView_menu, "Item ", 30,R.id.list_View_Menu, getApplicationContext(), Sampiyon.this); // ,
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-14"); // Placeholder
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());
//		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-27");
		
		//comScore.setAppContext(this.getApplicationContext());
		
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.BannerEnabled)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		imageClickControl = true;
		try
		{
			Sampiyon.scale = this.getResources().getDisplayMetrics().density;
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		((TextView)findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Vatan Gazetesi");
		
		Array_TopHeadlinesC_Load = new ArrayList<DataModelNewsImageLoad>();
//		Array_HomePage_Load = new ArrayList<DataModelNewsImageLoad>();
		appmap = new AppMap(getApplicationContext(), Sampiyon.this);
		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		sv = new ScrollViewExt(getApplicationContext());
		sv = (ScrollViewExt) findViewById(R.id.scrollview);
		sv.setScrollViewListener(new OnScrollViewListener()
		{
			@Override
			public void onScrollChanged(ScrollViewExt scrollView, int x, int y,int oldx, int oldy)
			{
				// TODO Auto-generated method stub
				boolean flag = true;
				flag = true;
				
				
				Log.e("y:",Integer.toString(y));
				Log.e("scale",Float.toString(scale));
				Log.e("videoRowShowCount",Integer.toString(videoRowShowCount));
				Log.e("dikeyVideoSay",Integer.toString(dikeyVideoSay));
				Log.e("Sampiyon.Array_TopHeadlinesC_Load.size()",Integer.toString(Sampiyon.Array_TopHeadlinesC_Load.size()));
				
				
				if ((y / scale) / 89 > videoRowShowCount && dikeyVideoSay + videoRowShowCount < Sampiyon.Array_TopHeadlinesC_Load.size())
				{
					Log.e("videoRowShowCount+dikeyVideoSay && dikeyVideoSay","videoRowShowCount+dikeyVideoSay && dikeyVideoSay");
					String[] urlAddress;
					urlAddress = Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getImageURL().split("/");
					try
					{
						if (!(new File(DirName + urlAddress[urlAddress.length - 1])).exists())
						{
							new DownloadImageTask(Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getNewsImage(),urlAddress[urlAddress.length - 1], DirName, Sampiyon.Array_TopHeadlinesC_Load.get( dikeyVideoSay + videoRowShowCount).getIsShown()).execute(Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getImageURL());
						} 
						else
						{
							Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getNewsImage().setImageURI(Uri.fromFile(new File(DirName + urlAddress[urlAddress.length - 1])));
							Sampiyon.Array_TopHeadlinesC_Load.get( dikeyVideoSay + videoRowShowCount).setIsShown(true);
						}
					} 
					catch (Exception e)
					{
						flag = false;
					}

					if (flag)
					{
						videoRowShowCount++;
					}
				}
			}
		});
		
		mIndicator.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub
			}
		});
		
		mPager.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            v.getParent().requestDisallowInterceptTouchEvent(true);
	            return false;
	        }
	    });

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

	        @Override
	        public void onPageSelected(int arg0) {
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	            mPager.getParent().requestDisallowInterceptTouchEvent(true);
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    });
		
		LayInfalater = getLayoutInflater();
//		TopHeadlinesC_layout = (LinearLayout) findViewById(R.id.TopHeadlinesC_layout);
		TopHeadlinesC_layout = (LinearLayout) findViewById(R.id.HomePage_layout);
		homeMainLayout = (LinearLayout) findViewById(R.id.homeMainLayout);
		homeMainLayout.setVisibility(View.VISIBLE);
		weatherDate = (TextView) findViewById(R.id.date);
		weatherInfo = (TextView) findViewById(R.id.weatherData);
		weatherImage = (ImageView) findViewById(R.id.image);
		
		display = getWindowManager().getDefaultDisplay();
		outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		dpHeight = outMetrics.heightPixels / (getResources().getDisplayMetrics().density);
		dpHeight -= 265;
		SampiyonAccessData accessData = new SampiyonAccessData(getApplicationContext(),listView, this, getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout,LayInfalater, dpHeight);
		final String[] in = { "in" };
		try
		{
			if (accessData.areFilesOK())
			{
				accessData.readData();
			} 
			else
			{
				accessData.execute(in);
			}
		} 
		catch (IOException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			e.printStackTrace();
		} 
		catch (ParseException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			e.printStackTrace();
		}
		
//		ImageView menuLogo = (ImageView) findViewById(R.id.menuImage);
//		menuLogo.setOnClickListener(new ClickListener());
		final ImageView refreshLogo = (ImageView) findViewById(R.id.refreshImage);
		refreshLogo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Array_TopHeadlinesC_Load.clear();
//				Array_HomePage_Load.clear();3
				videoRowShowCount = 0;
				videoRowShowCount_hp = 0;
				SampiyonAccessData access = new SampiyonAccessData(getApplicationContext(), listView, Sampiyon.this,getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout ,LayInfalater, dpHeight);
				access.execute(in);
				sv.scrollTo(0, 0);
			}
		});
		final ImageView mainLogo = (ImageView) findViewById(R.id.logoImage);
		mainLogo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

			}
		});
		
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    // Send a screen view when the Activity is displayed to the user.
	    mGaTracker.sendView("Sampiy10"); //sayma kodu
	    mGaTrackerGlobal.sendView("Sampiy10"); 
	    FlurryAgent.onStartSession(this, "J7LIJ4D8612IYIAF6REK");
	  }
	
	@Override
	public void onStop()
	{
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	   // your code
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sampiyon, menu);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		comScore.onEnterForeground();
		SampiyonAccessData accessData = new SampiyonAccessData(getApplicationContext(),listView, this, getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout,LayInfalater, dpHeight);
		try
		{
			if (AutoRefresh)
			{
				if (accessData.areFilesOK())
				{
//					accessData.readData();
				} 
				else
				{
					Array_TopHeadlinesC_Load.clear();
//					Array_HomePage_Load.clear();3
					videoRowShowCount = 0;
					videoRowShowCount_hp = 0;
					accessData.execute("in");
					sv.scrollTo(0, 0);
				}
			}
		} 
		catch (IOException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			e.printStackTrace();
		} 
		catch (ParseException e)
		{
			Toast.makeText(getApplicationContext(), "Bağlantı Hatası", Toast.LENGTH_LONG).show();
			finish();
			e.printStackTrace();
		}
		AutoRefresh=true;
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		// Notify comScore about lifecycle usage
		comScore.onExitForeground();
	}
	
	public static void setClickable(boolean value)
	{
		for (int i = 0; i < Array_TopHeadlinesC_Load.size(); i++)
		{
			Array_TopHeadlinesC_Load.get(i).getItemLayout().setClickable(value);
		}
//		for (int i = 0; i < Array_HomePage_Load.size(); i++)
//		{
//			Array_HomePage_Load.get(i).getItemLayout().setClickable(value);
//		}
	}
	
	
	void layoutApp(boolean menuOut)
	{
		System.out.println("layout [" + animParams.left + "," + animParams.top+ "," + animParams.right + "," + animParams.bottom + "]");
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)app.getLayoutParams();
		params.gravity=Gravity.TOP;
		if (menuOut)
		{
			
			params.setMargins(animParams.left, 0, -animParams.left, 0); 	
//			params.gravity=Gravity.TOP;
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
		// TODO Auto-generated method stub
		Log.e("onAnimationEnd","onAnimationEnd");
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
            	if (!Controller.endsWith("Sampiyon") && IsMenuClicked)
    			{
    				appmap.RunActivity(Controller,CategoryName, CategoryID,"");
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
			Context context = Sampiyon.context;
			Animation anim;
			int w = app.getMeasuredWidth();
			int h = app.getMeasuredHeight();
			int left = (int) (getResources().getDisplayMetrics().density * 255);
			anim = new TranslateAnimation(0, -left, 0, 0);
			animParams.init(0, 0, w, h);
			anim.setDuration(500);
			anim.setAnimationListener((AnimationListener) context);
			anim.setFillAfter(true);
			app.startAnimation(anim);
		}
	}

}
