package com.amvg.vatan.main;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.amvg.vatan.main.Home.AnimParams;
import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("DefaultLocale")
public class Video extends FragmentActivity implements AnimationListener
{
	TestFragmentAdapterVideo mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
//	public static LinearLayout featuredClick0; //Click olayını yakalayıabilmek için tanımlandı
//	public static LinearLayout featuredClick1;
//	public static LinearLayout featuredClick2;
//	public static LinearLayout featuredClick3;
//	public static LinearLayout featuredClick4;
//	public static LinearLayout featuredClick5;
//	public static LinearLayout featuredClick6;
//	public static LinearLayout featuredClick7;
//	public static LinearLayout featuredClick8;
//	public static LinearLayout featuredClick9;
//	public static LinearLayout featuredClick10;
//	public static LinearLayout featuredClick11;
//	public static LinearLayout featuredClick12;
//	public static LinearLayout featuredClick13;
//	public static LinearLayout featuredClick14;
//	public static LinearLayout[] featuredLayout; //kullanılmayanları for içerisinde gizleyebilmek için dizi olarak tanımladık
//	public static TextView[] featuredText;
//	public static TextView[] featuredID;
//	public static ImageView[] featuredImage;
//	public static LinearLayout[] categoriesLayout;
//	public static TextView[] categoriesText;
//	public static TextView[] categoriesID;
	
	
	public static TextView weatherDate;
	public static TextView weatherInfo;
	public static ImageView weatherImage;
	private AppMap appmap;
	private LinearLayout FeaturedLayout;
	private LinearLayout CategoriesLayout;
	private LayoutInflater LayInfalater;
	
	public static Context context;
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
	
	private String controller;
	public static LinearLayout backgroungLayout;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
//	public static WebView webView;
	private View BannerDivider;
	public float dpHeight;
	private Display display;
	private DisplayMetrics outMetrics;
	public HorizontalScrollViewExt ScrollViewHorizontal;
	public float scale;
	public int videoRowShowCount=0;
	public int YatayVideoSay;
	private String DirName="/sdcard/Vatan/vatantv/";
	public boolean IsVideo;
	private boolean AutoRefresh;
	
	public static ArrayList<DataModelVideoImageLoad> Array_FeaturedVideo_Load;
	
	
	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Log.e("bas", "bas");
			System.out.println("onClick " + new Date());
			Video me = Video.this;
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
	
	private String activityName()
	{
		if (controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatanttv")) 
		{
			this.IsVideo=true;
			return "Video";
		}
		else 
		{
			return "Gallery";
		}
	}
	
	
	private void runCategory(String catName, String catID, Context context, Context contextDialog)
	{
		AppMap apmap=new AppMap(context,contextDialog);
		if (controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatanttv")) 
		{
			apmap.RunActivity("VideoCategory", catName, "1", catID);
		}
		else
		{
			apmap.RunActivity("VideoCategory", catName, "0", catID);
		}
	}
	private void runGallery(String ID, Context context, Context contextDialog)
	{
		
//		clickableFalse();
		AppMap apmap=new AppMap(context, contextDialog);
		if (controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatanttv"))
		{
			apmap.RunActivity("VideoClip", "1", ID, "");
		}
		else
		{
			apmap.RunActivity("PhotoGallery", "", ID, "");
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		AutoRefresh=false;
		
		
		viewUtils = new ViewUtils();
		menu = findViewById(R.id.menu);
		app = findViewById(R.id.app);
		viewUtils.printView("menu", menu);
		viewUtils.printView("app", app);
		listView_menu = (ListView) menu.findViewById(R.id.list_View_Menu);
		listView_menu.invalidateViews();
		MainMenuAccessData mainMenuAccessData = new MainMenuAccessData(getApplicationContext(),"Home" ,listView_menu);
		mainMenuAccessData.execute("");
		viewUtils.initListView(this, listView_menu, "Item ", 30,R.id.list_View_Menu, getApplicationContext(), Video.this);
		
//		clickableTrue();
		
		mGaInstance = GoogleAnalytics.getInstance(this);  //sayım kodu
		mGaTracker = mGaInstance.getTracker("UA-15581378-14");  //sayım kodu
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());
		
		mAdapter=new TestFragmentAdapterVideo(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		backgroungLayout=(LinearLayout)findViewById(R.id.background);
		
		FeaturedLayout=(LinearLayout)findViewById(R.id.VideoFeaturedLayout);
		CategoriesLayout=(LinearLayout)findViewById(R.id.VideoCategoriesLayout);
		LayInfalater = getLayoutInflater();
		Array_FeaturedVideo_Load=new ArrayList<DataModelVideoImageLoad>();
		
		scale = this.getResources().getDisplayMetrics().density;
		videoRowShowCount=0;
		display = getWindowManager().getDefaultDisplay();
		outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		dpHeight = outMetrics.widthPixels / (getResources().getDisplayMetrics().density);
		YatayVideoSay=(int) Math.ceil(dpHeight/122.0);
		
		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		((TextView)findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 " + Integer.toString(year) + " Vatan Gazetesi");
		
		final ScrollView sv = (ScrollView) findViewById(R.id.scrollview);
		
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.BannerEnabled)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		
		mPager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				sv.requestDisallowInterceptTouchEvent(true);
				return false;
			}
	    });
		
		
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
                    sv.requestDisallowInterceptTouchEvent(true);
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
		
		//PhotoGalleryAccessData stopThread=new PhotoGalleryAccessData(getApplicationContext(), "1", mAdapter, mPager);
		
		
		final String[] in={"in"};
		appmap=new AppMap(getApplicationContext(),Video.this);
		
		
		weatherDate = (TextView) findViewById(R.id.date);
		weatherInfo = (TextView) findViewById(R.id.weatherData);
		weatherImage = (ImageView) findViewById(R.id.image);
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     controller = extras.getString("controller");
		}
		
		if (controller.equals("Gallery") || controller.equals("gallery") || controller.toLowerCase().equals("dummygallery")) 
		{
			TextView galleryTitle=(TextView)findViewById(R.id.date);
			galleryTitle.setText("Galeri");
			((ImageView)findViewById(R.id.logoImage)).setImageResource(R.drawable.navigation_bar_logo);
//			ImageView playIcon=(ImageView)findViewById(R.id.playIcon0);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon1);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon2);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon3);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon4);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon5);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon6);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon7);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon8);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon9);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon10);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon11);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon12);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon13);
//			playIcon.setVisibility(View.INVISIBLE);
//			playIcon=(ImageView)findViewById(R.id.playIcon14);
//			playIcon.setVisibility(View.INVISIBLE);
		}
		else
		{
			this.IsVideo=true;
			((ImageView)findViewById(R.id.logoImage)).setImageResource(R.drawable.navigation_bar_logo_vatantv);
		}
		
		ImageView logoImage=(ImageView)findViewById(R.id.logoImage);
		logoImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
		
		ImageView menuImage=(ImageView)findViewById(R.id.menuImage);
		menuImage.setOnClickListener(new ClickListener());
//		menuImage.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				try {
//					Home.homeMainLayout.setVisibility(View.VISIBLE);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				try {
//					AppMap.DownloadBannerData.bannerEnabled="false";
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				finish();
//			}
//		});
		
		ImageView refreshImage=(ImageView)findViewById(R.id.refreshImage);
		refreshImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatantv")) 
				{
					VideoAccessData refreshData=new VideoAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight,controller);
					refreshData.execute(in);
				}
				else
				{
					GalleryAccessData refreshData=new GalleryAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight);
					refreshData.execute(in);
				}
			}
		});
		
		ScrollViewHorizontal=(HorizontalScrollViewExt)findViewById(R.id.HorizontalScroll);
		ScrollViewHorizontal.setScrollViewListener(0, new OnScrollViewHorizontalListener()
		{
			@Override
			public void onScrollChanged(HorizontalScrollViewExt scrollView, int x,int y, int oldx, int oldy, int index)
			{
				// TODO Auto-generated method stub
				boolean flag = true;
				flag = true;
				if ((x / scale) / 122 > videoRowShowCount && YatayVideoSay + videoRowShowCount < Array_FeaturedVideo_Load.size()) //&& dikeyVideoSay + videoRowShowCount < Home.Array_TopHeadlinesC_Load.size()
				{
					String[] urlAddress;
					urlAddress = Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getImageURL().split("/");
					try
					// dataVideoLoad dizisi verilerle dolduru	lmadan önce,
					// kullanıcı scroll yapıp onları da görüntülemek isterse try
					// catch bloğu bunu önler.
					{
						if (!(new File(DirName + urlAddress[urlAddress.length - 1])).exists())
						{
							Log.e("Downlaod Image","Download Image");
							new DownloadImageTaskVideo(Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getNewsImage(),Array_FeaturedVideo_Load.get(YatayVideoSay+videoRowShowCount).getPlayIcon(),urlAddress[urlAddress.length - 1], DirName, Array_FeaturedVideo_Load.get( YatayVideoSay + videoRowShowCount).getIsShown(),IsVideo).execute(Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getImageURL());
						} 
						else
						{
							Log.e("Set Image","Set Image");
							Array_FeaturedVideo_Load.get(YatayVideoSay + videoRowShowCount).getNewsImage().setImageURI(Uri.fromFile(new File(DirName + urlAddress[urlAddress.length - 1])));
							if (IsVideo)
							{
								Array_FeaturedVideo_Load.get(YatayVideoSay+videoRowShowCount).getPlayIcon().setVisibility(View.VISIBLE);
							}
							Array_FeaturedVideo_Load.get( YatayVideoSay + videoRowShowCount).setIsShown(true);
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
		
		
//		featuredLayout=new LinearLayout[16];
//		featuredText=new TextView[16];
//		featuredImage=new ImageView[16];
//		featuredID=new TextView[16];
//		categoriesLayout=new LinearLayout[16];
//		categoriesText=new TextView[16];
//		categoriesID=new TextView[16];
		
		
//		featuredLayout[0]=(LinearLayout)findViewById(R.id.featuredLayout0);
//		featuredLayout[1]=(LinearLayout)findViewById(R.id.featuredLayout1);
//		featuredLayout[2]=(LinearLayout)findViewById(R.id.featuredLayout2);
//		featuredLayout[3]=(LinearLayout)findViewById(R.id.featuredLayout3);
//		featuredLayout[4]=(LinearLayout)findViewById(R.id.featuredLayout4);
//		featuredLayout[5]=(LinearLayout)findViewById(R.id.featuredLayout5);
//		featuredLayout[6]=(LinearLayout)findViewById(R.id.featuredLayout6);
//		featuredLayout[7]=(LinearLayout)findViewById(R.id.featuredLayout7);
//		featuredLayout[8]=(LinearLayout)findViewById(R.id.featuredLayout8);
//		featuredLayout[9]=(LinearLayout)findViewById(R.id.featuredLayout9);
//		featuredLayout[10]=(LinearLayout)findViewById(R.id.featuredLayout10);
//		featuredLayout[11]=(LinearLayout)findViewById(R.id.featuredLayout11);
//		featuredLayout[12]=(LinearLayout)findViewById(R.id.featuredLayout12);
//		featuredLayout[13]=(LinearLayout)findViewById(R.id.featuredLayout13);
//		featuredLayout[14]=(LinearLayout)findViewById(R.id.featuredLayout14);
//		
//		featuredText[0]=(TextView)findViewById(R.id.featuredText0);
//		featuredText[1]=(TextView)findViewById(R.id.featuredText1);
//		featuredText[2]=(TextView)findViewById(R.id.featuredText2);
//		featuredText[3]=(TextView)findViewById(R.id.featuredText3);
//		featuredText[4]=(TextView)findViewById(R.id.featuredText4);
//		featuredText[5]=(TextView)findViewById(R.id.featuredText5);
//		featuredText[6]=(TextView)findViewById(R.id.featuredText6);
//		featuredText[7]=(TextView)findViewById(R.id.featuredText7);
//		featuredText[8]=(TextView)findViewById(R.id.featuredText8);
//		featuredText[9]=(TextView)findViewById(R.id.featuredText9);
//		featuredText[10]=(TextView)findViewById(R.id.featuredText10);
//		featuredText[11]=(TextView)findViewById(R.id.featuredText11);
//		featuredText[12]=(TextView)findViewById(R.id.featuredText12);
//		featuredText[13]=(TextView)findViewById(R.id.featuredText13);
//		featuredText[14]=(TextView)findViewById(R.id.featuredText14);
//		
//		featuredImage[0]=(ImageView)findViewById(R.id.featuredImage0);
//		featuredImage[1]=(ImageView)findViewById(R.id.featuredImage1);
//		featuredImage[2]=(ImageView)findViewById(R.id.featuredImage2);
//		featuredImage[3]=(ImageView)findViewById(R.id.featuredImage3);
//		featuredImage[4]=(ImageView)findViewById(R.id.featuredImage4);
//		featuredImage[5]=(ImageView)findViewById(R.id.featuredImage5);
//		featuredImage[6]=(ImageView)findViewById(R.id.featuredImage6);
//		featuredImage[7]=(ImageView)findViewById(R.id.featuredImage7);
//		featuredImage[8]=(ImageView)findViewById(R.id.featuredImage8);
//		featuredImage[9]=(ImageView)findViewById(R.id.featuredImage9);
//		featuredImage[10]=(ImageView)findViewById(R.id.featuredImage10);
//		featuredImage[11]=(ImageView)findViewById(R.id.featuredImage11);
//		featuredImage[12]=(ImageView)findViewById(R.id.featuredImage12);
//		featuredImage[13]=(ImageView)findViewById(R.id.featuredImage13);
//		featuredImage[14]=(ImageView)findViewById(R.id.featuredImage14);
//		
//		featuredClick0=(LinearLayout)findViewById(R.id.featuredClick0);
//		featuredClick1=(LinearLayout)findViewById(R.id.featuredClick1);
//		featuredClick2=(LinearLayout)findViewById(R.id.featuredClick2);
//		featuredClick3=(LinearLayout)findViewById(R.id.featuredClick3);
//		featuredClick4=(LinearLayout)findViewById(R.id.featuredClick4);
//		featuredClick5=(LinearLayout)findViewById(R.id.featuredClick5);
//		featuredClick6=(LinearLayout)findViewById(R.id.featuredClick6);
//		featuredClick7=(LinearLayout)findViewById(R.id.featuredClick7);
//		featuredClick8=(LinearLayout)findViewById(R.id.featuredClick8);
//		featuredClick9=(LinearLayout)findViewById(R.id.featuredClick9);
//		featuredClick10=(LinearLayout)findViewById(R.id.featuredClick10);
//		featuredClick11=(LinearLayout)findViewById(R.id.featuredClick11);
//		featuredClick12=(LinearLayout)findViewById(R.id.featuredClick12);
//		featuredClick13=(LinearLayout)findViewById(R.id.featuredClick13);
//		featuredClick14=(LinearLayout)findViewById(R.id.featuredClick14);
//		
//		featuredID[0]=(TextView)findViewById(R.id.featuredID0);
//		featuredID[1]=(TextView)findViewById(R.id.featuredID1);
//		featuredID[2]=(TextView)findViewById(R.id.featuredID2);
//		featuredID[3]=(TextView)findViewById(R.id.featuredID3);
//		featuredID[4]=(TextView)findViewById(R.id.featuredID4);
//		featuredID[5]=(TextView)findViewById(R.id.featuredID5);
//		featuredID[6]=(TextView)findViewById(R.id.featuredID6);
//		featuredID[7]=(TextView)findViewById(R.id.featuredID7);
//		featuredID[8]=(TextView)findViewById(R.id.featuredID8);
//		featuredID[9]=(TextView)findViewById(R.id.featuredID9);
//		featuredID[10]=(TextView)findViewById(R.id.featuredID10);
//		featuredID[11]=(TextView)findViewById(R.id.featuredID11);
//		featuredID[12]=(TextView)findViewById(R.id.featuredID12);
//		featuredID[13]=(TextView)findViewById(R.id.featuredID13);
//		featuredID[14]=(TextView)findViewById(R.id.featuredID14);
		
//		featuredClick0.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID0)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID1)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID2)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick3.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID3)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick4.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID4)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick5.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID5)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick6.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID6)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick7.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID7)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick8.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID8)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick9.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID9)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick10.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID10)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick11.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID11)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick12.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID12)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick13.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID13)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		featuredClick14.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runGallery(((TextView)findViewById(R.id.featuredID14)).getText().toString(), getApplicationContext(),Video.this);
//			}
//		});
//		
//		categoriesText[0]=(TextView)findViewById(R.id.categoriesText0);
//		categoriesText[1]=(TextView)findViewById(R.id.categoriesText1);
//		categoriesText[2]=(TextView)findViewById(R.id.categoriesText2);
//		categoriesText[3]=(TextView)findViewById(R.id.categoriesText3);
//		categoriesText[4]=(TextView)findViewById(R.id.categoriesText4);
//		categoriesText[5]=(TextView)findViewById(R.id.categoriesText5);
//		categoriesText[6]=(TextView)findViewById(R.id.categoriesText6);
//		categoriesText[7]=(TextView)findViewById(R.id.categoriesText7);
//		categoriesText[8]=(TextView)findViewById(R.id.categoriesText8);
//		categoriesText[9]=(TextView)findViewById(R.id.categoriesText9);
//		categoriesText[10]=(TextView)findViewById(R.id.categoriesText10);
//		categoriesText[11]=(TextView)findViewById(R.id.categoriesText11);
//		categoriesText[12]=(TextView)findViewById(R.id.categoriesText12);
//		categoriesText[13]=(TextView)findViewById(R.id.categoriesText13);
//		categoriesText[14]=(TextView)findViewById(R.id.categoriesText14);
//		categoriesText[15]=(TextView)findViewById(R.id.categoriesText15);
//		
//		categoriesID[0]=(TextView)findViewById(R.id.categoriesID0);
//		categoriesID[1]=(TextView)findViewById(R.id.categoriesID1);
//		categoriesID[2]=(TextView)findViewById(R.id.categoriesID2);
//		categoriesID[3]=(TextView)findViewById(R.id.categoriesID3);
//		categoriesID[4]=(TextView)findViewById(R.id.categoriesID4);
//		categoriesID[5]=(TextView)findViewById(R.id.categoriesID5);
//		categoriesID[6]=(TextView)findViewById(R.id.categoriesID6);
//		categoriesID[7]=(TextView)findViewById(R.id.categoriesID7);
//		categoriesID[8]=(TextView)findViewById(R.id.categoriesID8);
//		categoriesID[9]=(TextView)findViewById(R.id.categoriesID9);
//		categoriesID[10]=(TextView)findViewById(R.id.categoriesID10);
//		categoriesID[11]=(TextView)findViewById(R.id.categoriesID11);
//		categoriesID[12]=(TextView)findViewById(R.id.categoriesID12);
//		categoriesID[13]=(TextView)findViewById(R.id.categoriesID13);
//		categoriesID[14]=(TextView)findViewById(R.id.categoriesID14);
//		categoriesID[15]=(TextView)findViewById(R.id.categoriesID15);
//		
//		categoriesLayout[0]=(LinearLayout)findViewById(R.id.categoriesLayout0);
//		categoriesLayout[1]=(LinearLayout)findViewById(R.id.categoriesLayout1);
//		categoriesLayout[2]=(LinearLayout)findViewById(R.id.categoriesLayout2);
//		categoriesLayout[3]=(LinearLayout)findViewById(R.id.categoriesLayout3);
//		categoriesLayout[4]=(LinearLayout)findViewById(R.id.categoriesLayout4);
//		categoriesLayout[5]=(LinearLayout)findViewById(R.id.categoriesLayout5);
//		categoriesLayout[6]=(LinearLayout)findViewById(R.id.categoriesLayout6);
//		categoriesLayout[7]=(LinearLayout)findViewById(R.id.categoriesLayout7);
//		categoriesLayout[8]=(LinearLayout)findViewById(R.id.categoriesLayout8);
//		categoriesLayout[9]=(LinearLayout)findViewById(R.id.categoriesLayout9);
//		categoriesLayout[10]=(LinearLayout)findViewById(R.id.categoriesLayout10);
//		categoriesLayout[11]=(LinearLayout)findViewById(R.id.categoriesLayout11);
//		categoriesLayout[12]=(LinearLayout)findViewById(R.id.categoriesLayout12);
//		categoriesLayout[13]=(LinearLayout)findViewById(R.id.categoriesLayout13);
//		categoriesLayout[14]=(LinearLayout)findViewById(R.id.categoriesLayout14);
//		categoriesLayout[15]=(LinearLayout)findViewById(R.id.categoriesLayout15);
		
		
		
//		categoriesLayout[0].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[0].getText().toString(), categoriesID[0].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				Log.e(categoriesText[0].getText().toString(),categoriesID[0].getText().toString());
////				categoryName=categoriesText[0].getText().toString();
////				categoryID=categoriesID[0].getText().toString();
////				runVideoCategory=true;
//				//appmap.RunActivity("VideoCategory", categoriesText[0].getText().toString(), "", categoriesID[0].getText().toString());
//			}
//		});
//		categoriesLayout[1].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[1].getText().toString(), categoriesID[1].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[1].getText().toString();
////				categoryID=categoriesID[1].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[1].getText().toString(),categoriesID[1].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[1].getText().toString(), "", categoriesID[1].getText().toString());
//			}
//		});
//		categoriesLayout[2].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[2].getText().toString(), categoriesID[2].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[2].getText().toString();
////				categoryID=categoriesID[2].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[2].getText().toString(),categoriesID[2].getText().toString());
//				//appmap.RunActivity("VideoCategory", categoriesText[2].getText().toString(), "", categoriesID[2].getText().toString());
//			}
//		});
//		categoriesLayout[3].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[3].getText().toString(), categoriesID[3].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[3].getText().toString();
////				categoryID=categoriesID[3].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[3].getText().toString(),categoriesID[3].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[3].getText().toString(), "", categoriesID[3].getText().toString());
//			}
//		});
//		categoriesLayout[4].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[4].getText().toString(), categoriesID[4].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[4].getText().toString();
////				categoryID=categoriesID[4].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[4].getText().toString(),categoriesID[4].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[4].getText().toString(), "", categoriesID[4].getText().toString());
//			}
//		});
//		categoriesLayout[5].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[5].getText().toString(), categoriesID[5].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[5].getText().toString();
////				categoryID=categoriesID[5].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[5].getText().toString(),categoriesID[5].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[5].getText().toString(), "", categoriesID[5].getText().toString());
//			}
//		});
//		categoriesLayout[6].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[6].getText().toString(), categoriesID[6].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[6].getText().toString();
////				categoryID=categoriesID[6].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[6].getText().toString(),categoriesID[6].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[6].getText().toString(), "", categoriesID[6].getText().toString());
//			}
//		});
//		categoriesLayout[7].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[7].getText().toString(), categoriesID[7].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[7].getText().toString();
////				categoryID=categoriesID[7].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[7].getText().toString(),categoriesID[7].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[7].getText().toString(), "", categoriesID[7].getText().toString());
//			}
//		});
//		categoriesLayout[8].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[8].getText().toString(), categoriesID[8].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[8].getText().toString();
////				categoryID=categoriesID[8].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[8].getText().toString(),categoriesID[8].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[8].getText().toString(), "", categoriesID[8].getText().toString());
//			}
//		});
//		categoriesLayout[9].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				runCategory(categoriesText[9].getText().toString(), categoriesID[9].getText().toString(), getApplicationContext(),Video.this);
//				// TODO Auto-generated method stub
////				categoryName=categoriesText[9].getText().toString();
////				categoryID=categoriesID[9].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[9].getText().toString(),categoriesID[9].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[9].getText().toString(), "", categoriesID[9].getText().toString());
//			}
//		});
//		categoriesLayout[10].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				// TODO Auto-generated method stub
//				runCategory(categoriesText[10].getText().toString(), categoriesID[10].getText().toString(), getApplicationContext(),Video.this);
////				runCategory(categoriesText[10].getText().toString(), categoriesID[10].getText().toString(), getApplicationContext());
////				categoryName=categoriesText[10].getText().toString();
////				categoryID=categoriesID[10].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[10].getText().toString(),categoriesID[10].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[10].getText().toString(), "", categoriesID[10].getText().toString());
//			}
//		});
//		categoriesLayout[11].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				// TODO Auto-generated method stub
//				runCategory(categoriesText[11].getText().toString(), categoriesID[11].getText().toString(), getApplicationContext(),Video.this);
////				runCategory(categoriesText[10].getText().toString(), categoriesID[10].getText().toString(), getApplicationContext());
////				categoryName=categoriesText[10].getText().toString();
////				categoryID=categoriesID[10].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[10].getText().toString(),categoriesID[10].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[10].getText().toString(), "", categoriesID[10].getText().toString());
//			}
//		});
//		categoriesLayout[12].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				// TODO Auto-generated method stub
//				runCategory(categoriesText[12].getText().toString(), categoriesID[12].getText().toString(), getApplicationContext(),Video.this);
////				runCategory(categoriesText[10].getText().toString(), categoriesID[10].getText().toString(), getApplicationContext());
////				categoryName=categoriesText[10].getText().toString();
////				categoryID=categoriesID[10].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[10].getText().toString(),categoriesID[10].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[10].getText().toString(), "", categoriesID[10].getText().toString());
//			}
//		});
//		categoriesLayout[13].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				// TODO Auto-generated method stub
//				runCategory(categoriesText[13].getText().toString(), categoriesID[13].getText().toString(), getApplicationContext(),Video.this);
////				runCategory(categoriesText[10].getText().toString(), categoriesID[10].getText().toString(), getApplicationContext());
////				categoryName=categoriesText[10].getText().toString();
////				categoryID=categoriesID[10].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[10].getText().toString(),categoriesID[10].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[10].getText().toString(), "", categoriesID[10].getText().toString());
//			}
//		});
//		categoriesLayout[14].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				// TODO Auto-generated method stub
//				runCategory(categoriesText[14].getText().toString(), categoriesID[14].getText().toString(), getApplicationContext(),Video.this);
////				runCategory(categoriesText[10].getText().toString(), categoriesID[10].getText().toString(), getApplicationContext());
////				categoryName=categoriesText[10].getText().toString();
////				categoryID=categoriesID[10].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[10].getText().toString(),categoriesID[10].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[10].getText().toString(), "", categoriesID[10].getText().toString());
//			}
//		});
//		categoriesLayout[15].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Video.backgroungLayout.setVisibility(View.GONE);
//				// TODO Auto-generated method stub
//				runCategory(categoriesText[15].getText().toString(), categoriesID[15].getText().toString(), getApplicationContext(),Video.this);
////				runCategory(categoriesText[10].getText().toString(), categoriesID[10].getText().toString(), getApplicationContext());
////				categoryName=categoriesText[10].getText().toString();
////				categoryID=categoriesID[10].getText().toString();
////				runVideoCategory=true;
////				Log.e(categoriesText[10].getText().toString(),categoriesID[10].getText().toString());
////				appmap.RunActivity("VideoCategory", categoriesText[10].getText().toString(), "", categoriesID[10].getText().toString());
//			}
//		});
		
		
		
		if (controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatantv")) {
			VideoAccessData accessData=new VideoAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight,controller);
			try 
			{
				if (accessData.areFilesOK()) 
				{
					accessData.readData();
					Video.backgroungLayout.setVisibility(View.VISIBLE);
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
		}
		else
		{
			GalleryAccessData accessData=new GalleryAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight);
			try 
			{
				if (accessData.areFilesOK()) 
				{
					accessData.readData();
					Video.backgroungLayout.setVisibility(View.VISIBLE);
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
		}
	}


	@Override
	protected void onResume()
	{
		super.onResume();
		
		comScore.onEnterForeground();
		
		if (AutoRefresh)
		{
			if (controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatantv")) {
				VideoAccessData accessData=new VideoAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight,controller);
				try 
				{
					if (accessData.areFilesOK()) 
					{
//						accessData.readData();
//						Video.backgroungLayout.setVisibility(View.VISIBLE);
					}
					else 
					{
						accessData.execute("in");
						ScrollViewHorizontal.scrollTo(0, 0);
						Array_FeaturedVideo_Load.clear();
						videoRowShowCount=0;
						
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
			}
			else
			{
				GalleryAccessData accessData=new GalleryAccessData(getApplicationContext(), Video.this,getSupportFragmentManager(),mPager,mIndicator,FeaturedLayout,CategoriesLayout,LayInfalater,dpHeight);
				try 
				{
					if (accessData.areFilesOK()) 
					{
//						accessData.readData();
//						Video.backgroungLayout.setVisibility(View.VISIBLE);
					}
					else 
					{
						accessData.execute("in");
						ScrollViewHorizontal.scrollTo(0, 0);
						Array_FeaturedVideo_Load.clear();
						videoRowShowCount=0;
						
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
			}
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
	
	
	@Override
	  public void onStart() {
	    super.onStart();
//	    // Send a screen view when the Activity is displayed to the user.  //sayım kodu
	    if (controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatantv"))
		{
	    	mGaTracker.sendView("VatanTV");
	    	mGaTrackerGlobal.sendView("VatanTV");
		}
	    else
	    {
	    	mGaTracker.sendView("Gallery");
	    	mGaTrackerGlobal.sendView("Gallery");
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
			Home.setClickable(true);
			Home.homeMainLayout.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			AppMap.DownloadBannerData.bannerEnabled="false";
		} catch (Exception e) {
			// TODO: handle exception
		}
	   finish();
	}
	
	void layoutApp(boolean menuOut)
	{
		//System.out.println("layout [" + animParams.left + "," + animParams.top+ "," + animParams.right + "," + animParams.bottom + "]");
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
            	if (!Controller.endsWith(activityName()) && IsMenuClicked)
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
			Context context = Video.context;
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
//	private void clickableFalse()
//	{
//		featuredClick0.setClickable(false);
//		featuredClick1.setClickable(false);
//		featuredClick2.setClickable(false);
//		featuredClick3.setClickable(false);
//		featuredClick4.setClickable(false);
//		featuredClick5.setClickable(false);
//		featuredClick6.setClickable(false);
//		featuredClick7.setClickable(false);
//		featuredClick8.setClickable(false);
//		featuredClick9.setClickable(false);
//		featuredClick10.setClickable(false);
//		featuredClick11.setClickable(false);
//		featuredClick12.setClickable(false);
//		featuredClick13.setClickable(false);
//		featuredClick14.setClickable(false);
//	}
//	private void clickableTrue()
//	{
//		featuredClick0.setClickable(true);
//		featuredClick1.setClickable(true);
//		featuredClick2.setClickable(true);
//		featuredClick3.setClickable(true);
//		featuredClick4.setClickable(true);
//		featuredClick5.setClickable(true);
//		featuredClick6.setClickable(true);
//		featuredClick7.setClickable(true);
//		featuredClick8.setClickable(true);
//		featuredClick9.setClickable(true);
//		featuredClick10.setClickable(true);
//		featuredClick11.setClickable(true);
//		featuredClick12.setClickable(true);
//		featuredClick13.setClickable(true);
//		featuredClick14.setClickable(true);
//	}
}
