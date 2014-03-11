package com.amvg.vatan.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.mobilike.garantiad.GarantiAdManager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import de.madvertise.android.sdk.MadvertiseTracker;
import de.madvertise.android.sdk.MadvertiseView;
import de.madvertise.android.sdk.MadvertiseView.MadvertiseViewCallbackListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class Home extends FragmentActivity implements AnimationListener,MadvertiseViewCallbackListener {
	private ListView listView;
	//private ViewFlow viewFlow;
	private File myFile;
	private FileWriter fooWriter;
	private TestFragmentAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private String isID;
	public static int MenuClickCount=0; //Men�� butonuna ka�� defa t��kland������n�� tutan de��i��ken. Men�� butonuna ��ift say��da m�� yoksa tek say��da m�� bas��lma verisine ihtiya�� duyuluyor(Slide men��deki hatalar�� gidermek i��in). 
	public static boolean MenuButtonRunStyle=true; 
	public static TextView weatherDate;
	public static TextView weatherInfo;
	public static ImageView weatherImage;
	public static LinearLayout yellowBandLinearLayout;
	public static RelativeLayout yellowBandRelativeLayout;
	public static TextView yellowBandTitle;
	public static TextView yellowBandID;
	public static TextView yellowBandContentType;
	
	private String Controller;
	private String CategoryName;
	private String CategoryID;
	private boolean IsMenuClicked=false;
	private AppMap appmap;
	
	
	private LinearLayout KategorilerLayout;
	private LinearLayout YazarlarLayout;
	private LinearLayout SampiyonSporLayout;
	private LinearLayout GaleriLayout;
	private LinearLayout VatanTvLayout;
	private LinearLayout HavaDurumuLayout;
	private LinearLayout AstrolojiLayout;
	private LinearLayout SonDakikaLayot;
	//sonradan eklenenler
	private int videoRowShowCount_hp = 0;
	public static View YellowBandDivider;
	public static float dpHeight_hp;
	public static int dikeyVideoSay;
	public static ArrayList<DataModelNewsImageLoad> Array_TopHeadlinesC_Load;
	public static ArrayList<DataModelNewsImageLoad> Array_HomePage_Load;
	private LinearLayout TopHeadlinesC_layout;
	private LinearLayout HomePage_layout;
	private LayoutInflater LayInfalater;
	public float dpHeight;
	public ScrollViewExt sv;
	private int videoRowShowCount = 0; // videolardan kaçıncı satıra kadar gözükmesi gerektiğini tutar.
	private String DirName = "/sdcard/Milliyet/home/";
	private Display display;
	private DisplayMetrics outMetrics;
//	public static LinearLayout headLines5layout0;
//	public static ImageView headLines5image0;
//	public static TextView headLines5title0;
//	public static TextView headLines5contentType0;
//	public static TextView headLines5ID0;
//	public static LinearLayout headLines5layout1;
//	public static ImageView headLines5image1;
//	public static TextView headLines5title1;
//	public static TextView headLines5contentType1;
//	public static TextView headLines5ID1;
//	public static LinearLayout headLines5layout2;
//	public static ImageView headLines5image2;
//	public static TextView headLines5title2;
//	public static TextView headLines5contentType2;
//	public static TextView headLines5ID2;
//	public static LinearLayout headLines5layout3;
//	public static ImageView headLines5image3;
//	public static TextView headLines5title3;
//	public static TextView headLines5contentType3;
//	public static TextView headLines5ID3;
	public static TextView financeImkbPuan;
	public static TextView financeImkbYuzde;
	public static ImageView financeImkbIcon;
	public static TextView financeEuroPuan;
	public static TextView financeEuroYuzde;
	public static ImageView financeEuroIcon;
	public static TextView financeUsdPuan;
	public static TextView financeUsdYuzde;
	public static ImageView financeUsdIcon;
	public static TextView financeAltinPuan;
	public static TextView financeAltinYuzde;
	public static ImageView financeAltinIcon;
//	public static LinearLayout[] headLines5layout;
//	public static ImageView[] headLines5image;
//	public static TextView[] headLines5title;
//	public static TextView[] headLines5contentType;
//	public static TextView[] headLines5ID;
//	public static TextView[] news11title;
//	public static TextView[] news11contentType;
//	public static ImageView[] news11image;
//	public static TextView[] news11ID;
	public static LinearLayout[] news11layout;
	public static LinearLayout homeMainLayout;
//	public static WebView webView;
	public static float scale;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
//	private GoogleAnalyticsTracker mGATracker;
	private MadvertiseTracker mTracker;
    private MadvertiseView mMadView;
    private View BannerDivider;
    public static boolean BannerEnabled=false;
    
    private boolean AutoRefresh;
	
	
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
				Home me = Home.this;
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
				else  // slide menünün kapanışı
				{ 
					anim = new TranslateAnimation(0, -left, 0, 0);
					animParams.init(0, 0, w, h);
				}
				anim.setDuration(500);
				anim.setAnimationListener(me);
				anim.setFillAfter(true);
				app.startAnimation(anim);
			}
		}
	
	
	
	public boolean checkInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
		&& conMgr.getActiveNetworkInfo().isAvailable()
		&& conMgr.getActiveNetworkInfo().isConnected()) {
		return true;
		} else {
		return false;
		}
	}
	public AlertDialog alertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.con_message)
	       .setTitle(R.string.con_title);
		builder.setPositiveButton(R.string.yeniden_dene, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	              if (!checkInternetConnection()) {
					alertDialog().show();
				}else{
					updateApp();
				}
	           }
	       });
		builder.setNegativeButton(R.string.vazgec, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	              finish();
	           }
	       });
		AlertDialog dialog = builder.create();
		return dialog;
	}
	public void updateApp(){
		final JSon json=new JSon();
		String versionName = null;
		String parseParam[]={"http://m2.milliyet.com.tr/AppConfigs/Vatan-Android-v2/AppConfig.json","AppID","BundleID","Version"};
		//String parseParam[]={"http://m2.milliyet.com.tr/AppConfigs/Milliyet-Android-v2/MainMenu.json","AppID","BundleID","Version"};
		try {
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		json.MyPreloader(this,versionName);
		json.execute(parseParam);
	}

	@SuppressLint("NewApi")
	
	private void doFirstRun() {
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        if (settings.getBoolean("isFirstRun", true)) {
        	//showDialog(DIALOG_HELP);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
}
	
	/*public static void saveflagmosque(){
		
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("mosque", false);
        editor.commit();
    }
    public boolean getflagmosque(){
        flagmosque = sharedPref.getBoolean("mosque", true);
        return flagmosque;
    }*/
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("OnCreate","OnCreate");
		
		AutoRefresh=false;
		
		boolean firstboot = getSharedPreferences("BOOT_PREF", MODE_PRIVATE).getBoolean("firstboot", true);

	    if (firstboot){
	        // 1) Launch the authentication activity
	        // 2) Then save the state
	        getSharedPreferences("BOOT_PREF", MODE_PRIVATE)
	            .edit()
	            .putBoolean("firstboot", false)
	            .commit();
	    }
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		updateApp();
		
		MadvertiseView.setAge("20-26");
        MadvertiseView.setGender(MadvertiseView.GENDER_FEMALE);
        
        mMadView = (MadvertiseView) findViewById(R.id.madad);
        mMadView.setMadvertiseViewCallbackListener(this);
        
        mTracker = MadvertiseTracker.getInstance(this);
		
		viewUtils=new ViewUtils();
		MenuClickCount=0;
		
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
		        viewUtils.initListView(this, listView_menu, "Item ", 30, R.id.list_View_Menu, getApplicationContext(), Home.this);
		
		
		// Get the GoogleAnalytics singleton. Note that the SDK uses
	    // the application context to avoid leaking the current context.
	    mGaInstance = GoogleAnalytics.getInstance(this);  //say��m kodu

	    // Use the GoogleAnalytics singleton to get a Tracker.
	    mGaTracker = mGaInstance.getTracker("UA-15581378-14"); // Placeholder tracking ID. say��m kodu
	    mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
	    
	    comScore.setAppName("Vatan");
	    comScore.setAppContext(this.getApplicationContext());
//	    ... // The rest of your onCreate() code.
		
//		GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
//		tracker.start("UA-12345678-1", this);
//		tracker.trackPageView("/HomeScreen");
//		tracker.dispatch();
//		GaInstance = GoogleAnalytics.getInstance(this);
//		GaTracker  = GaInstance.getTracker("UA-15581378-12");
//		GaTracker.sendView("/Home");
		
	    
	    Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		((TextView)findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Vatan Gazetesi");
		
		try
		{
			SplashScreen.splash.finish();
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		
		//this.getResources().getDisplayMetrics().density;
		String ColumnistID="10";
		String dene="\"ColumnistArticles://?P={\"ColumnistID\":\""+ColumnistID+"\"}\"";
		
		Home.scale = this.getResources().getDisplayMetrics().density;
		
//		news11contentType=new TextView[13];
//		news11ID=new TextView[13];
//		news11title=new TextView[13];
//		news11layout=new LinearLayout[13];
//		news11image=new ImageView[13];
//		headLines5layout=new LinearLayout[10];
//		headLines5image=new ImageView[10];
//		headLines5title=new TextView[10];
//		headLines5contentType=new TextView[10];
//		headLines5ID=new TextView[10];
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		
		LayInfalater = getLayoutInflater();
		
		Array_TopHeadlinesC_Load = new ArrayList<DataModelNewsImageLoad>();
		Array_HomePage_Load = new ArrayList<DataModelNewsImageLoad>();
		
		appmap=new AppMap(getApplicationContext(),Home.this);
		
		
//		mAdapter=new TestFragmentAdapter(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
//		final ScrollView sv = (ScrollView) findViewById(R.id.scrollview);
		sv = new ScrollViewExt(getApplicationContext());
		sv = (ScrollViewExt)findViewById(R.id.scrollview);
		sv.setScrollViewListener(new OnScrollViewListener()
		{
			@Override
			public void onScrollChanged(ScrollViewExt scrollView, int x, int y,int oldx, int oldy)
			{
				Log.e("scale",Float.toString(scale));
				Log.e("y=",Integer.toString(y));
				Log.e("videoRowShowCount=",Integer.toString(videoRowShowCount));
				Log.e("dikeyVideoSay",Integer.toString(dikeyVideoSay));
				boolean flag = true;
				flag = true;
				if ((y / scale) / 89 > videoRowShowCount && dikeyVideoSay + videoRowShowCount < Home.Array_TopHeadlinesC_Load.size())
				{
					String[] urlAddress;
					urlAddress = Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getImageURL().split("/");
					try
					// dataVideoLoad dizisi verilerle doldurulmadan önce,
					// kullanıcı scroll yapıp onları da görüntülemek isterse try
					// catch bloğu bunu önler.
					{
						if (!(new File(DirName + urlAddress[urlAddress.length - 1])).exists())
						{
							new DownloadImageTask(Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getNewsImage(),urlAddress[urlAddress.length - 1], DirName, Home.Array_TopHeadlinesC_Load.get( dikeyVideoSay + videoRowShowCount).getIsShown()).execute(Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getImageURL());
						} 
						else
						{
							Array_TopHeadlinesC_Load.get(dikeyVideoSay + videoRowShowCount).getNewsImage().setImageURI(Uri.fromFile(new File(DirName + urlAddress[urlAddress.length - 1])));
							Home.Array_TopHeadlinesC_Load.get( dikeyVideoSay + videoRowShowCount).setIsShown(true);
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
				
				boolean flag2 = true;
				flag2 = true;
				if ((y / scale + dpHeight_hp) / 89 > videoRowShowCount_hp && videoRowShowCount_hp < Home.Array_HomePage_Load.size()&& !Home.Array_HomePage_Load.get(videoRowShowCount_hp).getIsShown())
				{
					String[] urlAddress;
					urlAddress = Array_HomePage_Load.get(videoRowShowCount_hp).getImageURL().split("/");
					try
					// dataVideoLoad dizisi verilerle doldurulmadan önce,
					// kullanıcı scroll yapıp onları da görüntülemek isterse try
					// catch bloğu bunu önler.
					{
						Log.e("girdi", "girdi");
						if (!(new File(DirName+ urlAddress[urlAddress.length - 1])).exists())
						{
							new DownloadImageTask(Array_HomePage_Load.get(videoRowShowCount_hp).getNewsImage(),urlAddress[urlAddress.length - 1], DirName,Home.Array_HomePage_Load.get(videoRowShowCount_hp).getIsShown()).execute(Array_HomePage_Load.get(videoRowShowCount_hp).getImageURL());
						} 
						else
						{
							Array_HomePage_Load.get(videoRowShowCount_hp).getNewsImage().setImageURI(Uri.fromFile(new File(DirName + urlAddress[urlAddress.length - 1])));
							Home.Array_HomePage_Load.get(videoRowShowCount_hp).setIsShown(true);
						}
						
					} 
					catch (Exception e)
					{
						flag2 = false;
					}

					if (flag2)
					{
						videoRowShowCount_hp++;
					}
				}
			}
		});
		
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Log.e("1","1");
				if (MenuClickCount%2==1) {
					Home.MenuButtonRunStyle=false;
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
		
		mPager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				sv.requestDisallowInterceptTouchEvent(true);
				return false;
			}
	    });
		
		
//		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
//			@Override
//			public void onPageSelected(int arg0) {
//				if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
//                    sv.requestDisallowInterceptTouchEvent(true);
//                }
//			}
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//			}
//		});

		
//		webView=(WebView)findViewById(R.id.webView);
		
		//webView.getSettings().setJavaScriptEnabled(true);
		//webView.getLayoutParams().width=(int) (Integer.parseInt("55") * Home.scale + 0.5f);
   		//webView.getLayoutParams().height=(int) (Integer.parseInt("55") * Home.scale + 0.5f);
		/*WebSettings setting = webView.getSettings();
		webView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
		    return true;
		}});*/
			
		
        
		//webView.loadUrl("http://www.google.com");
		TopHeadlinesC_layout = (LinearLayout) findViewById(R.id.TopHeadlinesC_layout);
		HomePage_layout=(LinearLayout)findViewById(R.id.HomePage_layout);
		
		homeMainLayout=(LinearLayout)findViewById(R.id.homeMainLayout);
		homeMainLayout.setVisibility(View.VISIBLE);
		
		weatherDate=(TextView)findViewById(R.id.date);
		weatherInfo=(TextView)findViewById(R.id.weatherData);
		weatherImage=(ImageView)findViewById(R.id.image);
		
		yellowBandLinearLayout=(LinearLayout)findViewById(R.id.titleSonDakikaLayoutLinear);
		yellowBandRelativeLayout=(RelativeLayout)findViewById(R.id.titleSonDakikaLayoutRelative);
		yellowBandTitle=(TextView)findViewById(R.id.title);
		yellowBandID=(TextView)findViewById(R.id.yellowBandID);
		yellowBandContentType=(TextView)findViewById(R.id.yellowBandContentType);
		YellowBandDivider=(View)findViewById(R.id.YellowBandDivider);
		
//		headLines5layout[0]=(LinearLayout)findViewById(R.id.itemLayout0);
//		headLines5layout[1]=(LinearLayout)findViewById(R.id.itemLayout1);
//		headLines5layout[2]=(LinearLayout)findViewById(R.id.itemLayout2);
//		headLines5layout[3]=(LinearLayout)findViewById(R.id.itemLayout3);
//		headLines5layout[4]=(LinearLayout)findViewById(R.id.itemLayout4);
//		headLines5layout[5]=(LinearLayout)findViewById(R.id.itemLayout5);
//		headLines5layout[6]=(LinearLayout)findViewById(R.id.itemLayout6);
//		headLines5layout[7]=(LinearLayout)findViewById(R.id.itemLayout7);
//		headLines5layout[8]=(LinearLayout)findViewById(R.id.itemLayout8);
//		headLines5layout[9]=(LinearLayout)findViewById(R.id.itemLayout9);
//		
//		headLines5contentType[0]=(TextView)findViewById(R.id.contentType0);
//		headLines5contentType[1]=(TextView)findViewById(R.id.contentType1);
//		headLines5contentType[2]=(TextView)findViewById(R.id.contentType2);
//		headLines5contentType[3]=(TextView)findViewById(R.id.contentType3);      
//		headLines5contentType[4]=(TextView)findViewById(R.id.contentType4);
//		headLines5contentType[5]=(TextView)findViewById(R.id.contentType5);
//		headLines5contentType[6]=(TextView)findViewById(R.id.contentType6);
//		headLines5contentType[7]=(TextView)findViewById(R.id.contentType7);
//		headLines5contentType[8]=(TextView)findViewById(R.id.contentType8);
//		headLines5contentType[9]=(TextView)findViewById(R.id.contentType9);
//		
//		headLines5ID[0]=(TextView)findViewById(R.id.ID0);
//		headLines5ID[1]=(TextView)findViewById(R.id.ID1);
//		headLines5ID[2]=(TextView)findViewById(R.id.ID2);
//		headLines5ID[3]=(TextView)findViewById(R.id.ID3);
//		headLines5ID[4]=(TextView)findViewById(R.id.ID4);
//		headLines5ID[5]=(TextView)findViewById(R.id.ID5);
//		headLines5ID[6]=(TextView)findViewById(R.id.ID6);
//		headLines5ID[7]=(TextView)findViewById(R.id.ID7);
//		headLines5ID[8]=(TextView)findViewById(R.id.ID8);
//		headLines5ID[9]=(TextView)findViewById(R.id.ID9);
//		
//		
//		headLines5image[0]=(ImageView)findViewById(R.id.newsImage0);
//		headLines5image[1]=(ImageView)findViewById(R.id.newsImage1);
//		headLines5image[2]=(ImageView)findViewById(R.id.newsImage2);
//		headLines5image[3]=(ImageView)findViewById(R.id.newsImage3);
//		headLines5image[4]=(ImageView)findViewById(R.id.newsImage4);
//		headLines5image[5]=(ImageView)findViewById(R.id.newsImage5);
//		headLines5image[6]=(ImageView)findViewById(R.id.newsImage6);
//		headLines5image[7]=(ImageView)findViewById(R.id.newsImage7);
//		headLines5image[8]=(ImageView)findViewById(R.id.newsImage8);
//		headLines5image[9]=(ImageView)findViewById(R.id.newsImage9);
//		
//		
//		headLines5title[0]=(TextView)findViewById(R.id.haberText0);
//		headLines5title[1]=(TextView)findViewById(R.id.haberText1);
//		headLines5title[2]=(TextView)findViewById(R.id.haberText2);
//		headLines5title[3]=(TextView)findViewById(R.id.haberText3);
//		headLines5title[4]=(TextView)findViewById(R.id.haberText4);
//		headLines5title[5]=(TextView)findViewById(R.id.haberText5);
//		headLines5title[6]=(TextView)findViewById(R.id.haberText7);
//		headLines5title[7]=(TextView)findViewById(R.id.haberText6);
//		headLines5title[8]=(TextView)findViewById(R.id.haberText8);
//		headLines5title[9]=(TextView)findViewById(R.id.haberText9);
		
//		headLines5layout0=(LinearLayout)findViewById(R.id.itemLayout0);
//		headLines5layout1=(LinearLayout)findViewById(R.id.itemLayout1);
//		headLines5layout2=(LinearLayout)findViewById(R.id.itemLayout2);
//		headLines5layout3=(LinearLayout)findViewById(R.id.itemLayout3);
//		
//		headLines5contentType0=(TextView)findViewById(R.id.contentType0);
//		headLines5contentType1=(TextView)findViewById(R.id.contentType1);
//		headLines5contentType2=(TextView)findViewById(R.id.contentType2);
//		headLines5contentType3=(TextView)findViewById(R.id.contentType3);
//		
//		headLines5ID0=(TextView)findViewById(R.id.ID0);
//		headLines5ID1=(TextView)findViewById(R.id.ID1);
//		headLines5ID2=(TextView)findViewById(R.id.ID2);
//		headLines5ID3=(TextView)findViewById(R.id.ID3);
//		
//		headLines5image0=(ImageView)findViewById(R.id.newsImage0);
//		headLines5image1=(ImageView)findViewById(R.id.newsImage1);
//		headLines5image2=(ImageView)findViewById(R.id.newsImage2);
//		headLines5image3=(ImageView)findViewById(R.id.newsImage3);
//		
//		headLines5title0=(TextView)findViewById(R.id.haberText0);
//		headLines5title1=(TextView)findViewById(R.id.haberText1);
//		headLines5title2=(TextView)findViewById(R.id.haberText2);
//		headLines5title3=(TextView)findViewById(R.id.haberText3);
		
		Home.yellowBandLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Home.yellowBandContentType.getText().toString().equals("VideoClipByCode")) {
					Home.yellowBandContentType.setText("VideoClip");
					isID="0";
				}
//				homeMainLayout.setVisibility(View.GONE);
				appmap.RunActivity(Home.yellowBandContentType.getText().toString(), isID, Home.yellowBandID.getText().toString(), "");
				isID="";
			}
		});
		
//		Home.headLines5layout[0].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[0].getText().toString(), "", Home.headLines5ID[0].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[1].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[1].getText().toString(), "", Home.headLines5ID[1].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[2].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[2].getText().toString(), "", Home.headLines5ID[2].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[3].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[3].getText().toString(), "", Home.headLines5ID[3].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[4].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[4].getText().toString(), "", Home.headLines5ID[4].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[5].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[5].getText().toString(), "", Home.headLines5ID[5].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[6].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[6].getText().toString(), "", Home.headLines5ID[6].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[7].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[7].getText().toString(), "", Home.headLines5ID[7].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[8].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[8].getText().toString(), "", Home.headLines5ID[8].getText().toString(), "");
//			}
//		});
//		
//		Home.headLines5layout[9].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				setClickable(false);
//				appmap.RunActivity(Home.headLines5contentType[9].getText().toString(), "", Home.headLines5ID[9].getText().toString(), "");
//			}
//		});
		
		Home.financeAltinIcon=(ImageView)findViewById(R.id.AltinIcon);
		Home.financeAltinPuan=(TextView)findViewById(R.id.AltinPuan);
		Home.financeAltinYuzde=(TextView)findViewById(R.id.AltinYuzde);
		
		Home.financeEuroIcon=(ImageView)findViewById(R.id.EuroIcon);
		Home.financeEuroPuan=(TextView)findViewById(R.id.EuroPuan);
		Home.financeEuroYuzde=(TextView)findViewById(R.id.EuroYuzde);
		
		Home.financeUsdIcon=(ImageView)findViewById(R.id.UsdIcon);
		Home.financeUsdPuan=(TextView)findViewById(R.id.UsdPuan);
		Home.financeUsdYuzde=(TextView)findViewById(R.id.UsdYuzde);
		
		Home.financeImkbIcon=(ImageView)findViewById(R.id.ImkbIcon);
		Home.financeImkbPuan=(TextView)findViewById(R.id.ImkbPuan);
		Home.financeImkbYuzde=(TextView)findViewById(R.id.ImkbYuzde);
		
//		Home.news11contentType[0]=(TextView)findViewById(R.id.featurecontentType0);
//		Home.news11ID[0]=(TextView)findViewById(R.id.featureID0);
//		Home.news11title[0]=(TextView)findViewById(R.id.featurehaberText0);
//		Home.news11layout[0]=(LinearLayout)findViewById(R.id.featureitemLayout0);
//		Home.news11image[0]=(ImageView)findViewById(R.id.featurenewsImage0);
//		
//		Home.news11contentType[1]=(TextView)findViewById(R.id.featurecontentType1);
//		Home.news11ID[1]=(TextView)findViewById(R.id.featureID1);
//		Home.news11title[1]=(TextView)findViewById(R.id.featurehaberText1);
//		Home.news11layout[1]=(LinearLayout)findViewById(R.id.featureitemLayout1);
//		Home.news11image[1]=(ImageView)findViewById(R.id.featurenewsImage1);
//		
//		Home.news11contentType[2]=(TextView)findViewById(R.id.featurecontentType2);
//		Home.news11ID[2]=(TextView)findViewById(R.id.featureID2);
//		Home.news11title[2]=(TextView)findViewById(R.id.featurehaberText2);
//		Home.news11layout[2]=(LinearLayout)findViewById(R.id.featureitemLayout2);
//		Home.news11image[2]=(ImageView)findViewById(R.id.featurenewsImage2);
//		
//		Home.news11contentType[3]=(TextView)findViewById(R.id.featurecontentType3);
//		Home.news11ID[3]=(TextView)findViewById(R.id.featureID3);
//		Home.news11title[3]=(TextView)findViewById(R.id.featurehaberText3);
//		Home.news11layout[3]=(LinearLayout)findViewById(R.id.featureitemLayout3);
//		Home.news11image[3]=(ImageView)findViewById(R.id.featurenewsImage3);
//		
//		Home.news11contentType[4]=(TextView)findViewById(R.id.featurecontentType4);
//		Home.news11ID[4]=(TextView)findViewById(R.id.featureID4);
//		Home.news11title[4]=(TextView)findViewById(R.id.featurehaberText4);
//		Home.news11layout[4]=(LinearLayout)findViewById(R.id.featureitemLayout4);
//		Home.news11image[4]=(ImageView)findViewById(R.id.featurenewsImage4);
//		
//		Home.news11contentType[5]=(TextView)findViewById(R.id.featurecontentType5);
//		Home.news11ID[5]=(TextView)findViewById(R.id.featureID5);
//		Home.news11title[5]=(TextView)findViewById(R.id.featurehaberText5);
//		Home.news11layout[5]=(LinearLayout)findViewById(R.id.featureitemLayout5);
//		Home.news11image[5]=(ImageView)findViewById(R.id.featurenewsImage5);
//		
//		Home.news11contentType[6]=(TextView)findViewById(R.id.featurecontentType6);
//		Home.news11ID[6]=(TextView)findViewById(R.id.featureID6);
//		Home.news11title[6]=(TextView)findViewById(R.id.featurehaberText6);
//		Home.news11layout[6]=(LinearLayout)findViewById(R.id.featureitemLayout6);
//		Home.news11image[6]=(ImageView)findViewById(R.id.featurenewsImage6);
//		
//		Home.news11contentType[7]=(TextView)findViewById(R.id.featurecontentType7);
//		Home.news11ID[7]=(TextView)findViewById(R.id.featureID7);
//		Home.news11title[7]=(TextView)findViewById(R.id.featurehaberText7);
//		Home.news11layout[7]=(LinearLayout)findViewById(R.id.featureitemLayout7);
//		Home.news11image[7]=(ImageView)findViewById(R.id.featurenewsImage7);
//		
//		Home.news11contentType[8]=(TextView)findViewById(R.id.featurecontentType8);
//		Home.news11ID[8]=(TextView)findViewById(R.id.featureID8);
//		Home.news11title[8]=(TextView)findViewById(R.id.featurehaberText8);
//		Home.news11layout[8]=(LinearLayout)findViewById(R.id.featureitemLayout8);
//		Home.news11image[8]=(ImageView)findViewById(R.id.featurenewsImage8);
//		
//		Home.news11contentType[9]=(TextView)findViewById(R.id.featurecontentType9);
//		Home.news11ID[9]=(TextView)findViewById(R.id.featureID9);
//		Home.news11title[9]=(TextView)findViewById(R.id.featurehaberText9);
//		Home.news11layout[9]=(LinearLayout)findViewById(R.id.featureitemLayout9);
//		Home.news11image[9]=(ImageView)findViewById(R.id.featurenewsImage9);
//		
//		Home.news11contentType[10]=(TextView)findViewById(R.id.featurecontentType10);
//		Home.news11ID[10]=(TextView)findViewById(R.id.featureID10);
//		Home.news11title[10]=(TextView)findViewById(R.id.featurehaberText10);
//		Home.news11layout[10]=(LinearLayout)findViewById(R.id.featureitemLayout10);
//		Home.news11image[10]=(ImageView)findViewById(R.id.featurenewsImage10);
//		
//		Home.news11contentType[11]=(TextView)findViewById(R.id.featurecontentType11);
//		Home.news11ID[11]=(TextView)findViewById(R.id.featureID11);
//		Home.news11title[11]=(TextView)findViewById(R.id.featurehaberText11);
//		Home.news11layout[11]=(LinearLayout)findViewById(R.id.featureitemLayout11);
//		Home.news11image[11]=(ImageView)findViewById(R.id.featurenewsImage11);
//		
//		Home.news11contentType[12]=(TextView)findViewById(R.id.featurecontentType12);
//		Home.news11ID[12]=(TextView)findViewById(R.id.featureID12);
//		Home.news11title[12]=(TextView)findViewById(R.id.featurehaberText12);
//		Home.news11layout[12]=(LinearLayout)findViewById(R.id.featureitemLayout12);
//		Home.news11image[12]=(ImageView)findViewById(R.id.featurenewsImage12);
		
//		Home.news11layout[0].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[0].getText().toString(), "", Home.news11ID[0].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[1].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[1].getText().toString(), "", Home.news11ID[1].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[2].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[2].getText().toString(), "", Home.news11ID[2].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[3].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[3].getText().toString(), "", Home.news11ID[3].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[4].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[4].getText().toString(), "", Home.news11ID[4].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[5].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[5].getText().toString(), "", Home.news11ID[5].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[6].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[6].getText().toString(), "", Home.news11ID[6].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[7].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[7].getText().toString(), "", Home.news11ID[7].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[8].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[8].getText().toString(), "", Home.news11ID[8].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[9].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[9].getText().toString(), "", Home.news11ID[9].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[10].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[10].getText().toString(), "", Home.news11ID[10].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[11].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[11].getText().toString(), "", Home.news11ID[11].getText().toString(), "");
//			}
//		});
//		
//		Home.news11layout[12].setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				homeMainLayout.setVisibility(View.GONE);
//				appmap.RunActivity(Home.news11contentType[12].getText().toString(), "", Home.news11ID[12].getText().toString(), "");
//			}
//		});
		
		
//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//		
//		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
//		{
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				webView.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true,AppMap.DownloadBannerData.bannerHTML ), "text/html", "UTF-8", "");
//				
//			}//AppMap.DownloadBannerData.bannerHTML
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
		
		
		KategorilerLayout=(LinearLayout)findViewById(R.id.KategorilerLayout);
		YazarlarLayout=(LinearLayout)findViewById(R.id.YazarlarLayout);
		SampiyonSporLayout=(LinearLayout)findViewById(R.id.Sampiy10Layout);
		GaleriLayout=(LinearLayout)findViewById(R.id.GaleriLayout);
		VatanTvLayout=(LinearLayout)findViewById(R.id.VatanTVLayout);
		HavaDurumuLayout=(LinearLayout)findViewById(R.id.HavaDurumuLayout);
		AstrolojiLayout=(LinearLayout)findViewById(R.id.AstrolojiLayout);
		SonDakikaLayot=(LinearLayout)findViewById(R.id.SonDakikaLayout);
		
		SonDakikaLayot.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("BreakingNews", "", "", "");
			}
		});
		
		KategorilerLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("NewsCategories", "", "", "");
			}
		});
		
		YazarlarLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Columnists", "", "", "");
			}
		});
		
		SampiyonSporLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Sampiy10", "", "", "");
			}
		});
		
		GaleriLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Gallery", "", "", "");
			}
		});
		
		VatanTvLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("VatanTV", "", "", "");
			}
		});
		
		HavaDurumuLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Weather", "", "", "");
			}
		});
		
		AstrolojiLayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				appmap.RunActivity("Horoscopes", "", "", "");
			}
		});
		
		
		display = getWindowManager().getDefaultDisplay(); 
		outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		dpHeight = outMetrics.heightPixels / (getResources().getDisplayMetrics().density);
		dpHeight -= 265;
		
		//weatherImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//HomeAccessData accessData=new HomeAccessData(getApplicationContext(), listView, this, getSupportFragmentManager(),mAdapter,mPager,mIndicator);
		HomeAccessData accessData = new HomeAccessData(getApplicationContext(),listView, this, getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout, HomePage_layout,LayInfalater, dpHeight);
		//HomeAccessBannerData accessBannerData=new HomeAccessBannerData();
        //advertData.execute(advertParam);
		
		
		/*ActionBar actionBar = getActionBar();
		actionBar.hide();*/
		
		final String[] in={"in"};
	    //this.listView=(ListView)findViewById(R.id.list);
	    //accessBannerData.execute(in);
		try {
			if (accessData.areFilesOK()) {
				accessData.readData();
			}else {
				accessData.execute(in);
				
			}
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Ba��lant�� Hatas��", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			Toast.makeText(getApplicationContext(), "Ba��lant�� Hatas��", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	   
		
	
		File file = new File("/sdcard/Vatan/homefirst/firstrun.txt" );
		if (!file.exists()){
			File root=new File("/sdcard/Vatan");
			root.mkdir();
			File directory=new File("/sdcard/Vatan/homefirst");
			directory.mkdir();
			
			this.myFile = new File("/sdcard/Vatan/homefirst/firstrun.txt");
			
			
			try {
				this.myFile.createNewFile();
				FileOutputStream fOut = new FileOutputStream(this.myFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.append("0");
				myOutWriter.close();
				fOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
			String aBuffer = "";
			File myFile = new File("/sdcard/Vatan/homefirst/firstrun.txt");
			FileInputStream fIn;
			try {
				fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow;
				}
				myReader.close();
				//File myFoo = new File("foo.log");
				if (aBuffer.equals("0")) {
					File dir = new File("/sdcard/Vatan/newsarticles");
					if (dir.isDirectory()) {
				        String[] children = dir.list();
				        for (int i = 0; i < children.length; i++) {
				            new File(dir, children[i]).delete();
				        }
				    }
					File dir2 = new File("/sdcard/Vatan/columnistsarticles");
					if (dir2.isDirectory()) {
				        String[] children = dir2.list();
				        for (int i = 0; i < children.length; i++) {
				            new File(dir2, children[i]).delete();
				        }
				    }
					this.fooWriter = new FileWriter(myFile, false); // true to append
                    // false to overwrite.
					fooWriter.write("1");
					fooWriter.close();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		
		    //doFirstRun();
		
		boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
	    if (firstrun){
	    	
	    	File dir = new File("/sdcard/Vatan/newsarticles");
			if (dir.isDirectory()) {
		        String[] children = dir.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(dir, children[i]).delete();
		        }
		    }
			File dir2 = new File("/sdcard/Vatan/columnistsarticles");
			if (dir2.isDirectory()) {
		        String[] children = dir2.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(dir2, children[i]).delete();
		        }
		    }
	    // Save the state
	    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
	        .edit()
	        .putBoolean("firstrun", false)
	        .commit();
	    }
		
		
		
		
		
	 
	    ImageView menuLogo=(ImageView)findViewById(R.id.menuImage);
	    menuLogo.setOnClickListener(new ClickListener());
//	    menuLogo.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				appmap.RunActivity("MainMenu", "", "","");
//			}
//		});
//	    {
//			
//			@Override
//			public void onClick(View v) {
//				appmap.RunActivity("MainMenu", "", "","");
//			}
//		});
	    final ImageView refreshLogo=(ImageView)findViewById(R.id.refreshImage);
	    refreshLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
//				Array_TopHeadlinesC_Load.clear();
//				videoRowShowCount = 0;
				
				Array_TopHeadlinesC_Load.clear();
				Array_HomePage_Load.clear();
				videoRowShowCount = 0;
				videoRowShowCount_hp = 0;
				
				HomeAccessData access = new HomeAccessData(getApplicationContext(),listView, Home.this, getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout, HomePage_layout,LayInfalater, dpHeight);
				//HomeAccessData access=new HomeAccessData(getApplicationContext(), listView, Home.this, getSupportFragmentManager(),mAdapter,mPager,mIndicator);
				//HomeAccessBannerData accessBanner=new HomeAccessBannerData();
				access.execute(in);
				//accessBanner.execute(in);
				sv.scrollTo(0, 0);
				
			}
		});
	    final ImageView mainLogo=(ImageView)findViewById(R.id.logoImage);
	    mainLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    
	    /*listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (((TextView)view.findViewById(R.id.type)).getText().toString().equals("1")) {
					appmap.RunActivity(((TextView)view.findViewById(R.id.contentType)).getText().toString(), "", ((TextView)view.findViewById(R.id.ID)).getText().toString(),"");
				}
		    }
		});*/
	    
	    
	    
	    /*viewFlow = (ViewFlow)findViewById(R.id.viewflow);
		viewFlow.setAdapter(new ImageAdapter(getApplicationContext()), 5);
		CircleFlowIndicator indic = (CircleFlowIndicator)findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);*/
	    
	    /*HomeFeatureLayout hfl=new HomeFeatureLayout(getApplicationContext());
	    ArrayList<String> stringList = new ArrayList<String>();
	    stringList.add("a");
	    stringList.add("b");
	    stringList.add("c");
	    
	    hfl.setFeatureItems(stringList);*/
	    
	    
	}

	
//	@Override
//	  public void onStart() {
//	    super.onStart();
////	    ... // The rest of your onStart() code.
//	    EasyTracker.getInstance().activityStart(this); // Add this method.
//	  }
//
//	  @Override
//	  public void onStop() {
//	    super.onStop();
////	    ... // The rest of your onStop() code.
//	    EasyTracker.getInstance().activityStop(this); // Add this method.
//	  }
	
	@Override
	  public void onStart() {
	    super.onStart();
	    FlurryAgent.onStartSession(this, "J7LIJ4D8612IYIAF6REK");
	    
	    // Send a screen view when the Activity is displayed to the user.
	    mGaTracker.sendView("Home"); //sayma kodu
	    mGaTrackerGlobal.sendView("Home");
	    mTracker.reportActive();
	  }
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		comScore.onEnterForeground();
		
		
		Log.e("Resume","Resume");
		
		
		
		HomeAccessData accessData = new HomeAccessData(getApplicationContext(),listView, this, getSupportFragmentManager(), mAdapter, mPager,mIndicator, TopHeadlinesC_layout, HomePage_layout,LayInfalater, dpHeight);
		
		
		try {
			if (AutoRefresh)
			{
				if (accessData.areFilesOK()) {
//					accessData.readData();
				}else {
					Array_TopHeadlinesC_Load.clear();
					Array_HomePage_Load.clear();
					videoRowShowCount = 0;
					videoRowShowCount_hp = 0;
					accessData.execute("in");
					sv.scrollTo(0, 0);
				}
			}
			
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Ba��lant�� Hatas��", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			Toast.makeText(getApplicationContext(), "Ba��lant�� Hatas��", Toast.LENGTH_LONG).show();
			finish();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		GarantiAdManager.loadAd(this, "http://adserv.nmdapps.com/vatan_android.mobilike", null);
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
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
		mTracker.reportInactive();
	}
	
	@Override
	public void onBackPressed() {
	   AppMap.DownloadBannerData.bannerEnabled="false";
	   finish();
	}
	
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
        	getSharedPreferences("PREFERENCE", MODE_PRIVATE)
	        .edit()
	        .putBoolean("firstrun", true)
	        .commit();
        	//GAServiceManager.getInstance().dispatch();
        	finish();
        	try {
        		File myFileClose = new File("/sdcard/Vatan/homefirst/firstrun.txt");
        		FileWriter fooWriterClose = new FileWriter(myFileClose, false);
        		fooWriterClose.write("0");
        		fooWriterClose.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
	
	public static void setClickable(boolean value)
	{
//		Home.headLines5layout[0].setClickable(value);
//		Home.headLines5layout[1].setClickable(value);
//		Home.headLines5layout[2].setClickable(value);
//		Home.headLines5layout[3].setClickable(value);
//		Home.headLines5layout[4].setClickable(value);
//		Home.headLines5layout[5].setClickable(value);
//		Home.headLines5layout[6].setClickable(value);
//		Home.headLines5layout[7].setClickable(value);
//		Home.headLines5layout[8].setClickable(value);
//		Home.headLines5layout[9].setClickable(value);
//		Home.news11layout[0].setClickable(value);
//		Home.news11layout[1].setClickable(value);
//		Home.news11layout[2].setClickable(value);
//		Home.news11layout[3].setClickable(value);
//		Home.news11layout[4].setClickable(value);
//		Home.news11layout[5].setClickable(value);
//		Home.news11layout[6].setClickable(value);
//		Home.news11layout[7].setClickable(value);
//		Home.news11layout[8].setClickable(value);
//		Home.news11layout[9].setClickable(value);
//		Home.news11layout[10].setClickable(value);
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
			Context context = Home.context;
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

	void layoutApp(boolean menuOut)
	{
		System.out.println("layout [" + animParams.left + "," + animParams.top+ "," + animParams.right + "," + animParams.bottom + "]");
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)app.getLayoutParams();
		params.gravity=Gravity.TOP;
		if (menuOut)
		{
			
			params.setMargins(animParams.left, 0, -animParams.left, 0); 
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
				if (!Controller.endsWith("Home") && IsMenuClicked)
				{
					Log.e("Controller",Controller);
					Log.e("CategoryName",CategoryName);
					Log.e("CategoryID",CategoryID);
					appmap.RunActivity(Controller,CategoryName, "" ,CategoryID);
					IsMenuClicked=false;
				}
			}
		}
		layoutApp(menuOut);
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{
		// TODO Auto-generated method stub
		Log.e("onAnimationRepeat","onAnimationRepeat");
	}

	@Override
	public void onAnimationStart(Animation animation)
	{
		Log.e("onAnimationStart","onAnimationStart");
		// TODO Auto-generated method stub
	}

	static class AnimParams
	{
		int left, right, top, bottom;
		void init(int left, int top, int right, int bottom)
		{
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}



@Override
public void onLoaded(boolean succeed, MadvertiseView madView)
{
	// TODO Auto-generated method stub
	if (succeed) {
        // ad loaded
		BannerEnabled=true;
		BannerDivider.setVisibility(View.VISIBLE);
        Log.e("YOUR_LOG_TAG", "Ad successfully loaded");
    } else {
        // ad could not be loaded
        Log.e("YOUR_LOG_TAG", "Ad could not be loaded");
    }
}
@Override
public void onError(Exception exception)
{
	// TODO Auto-generated method stub
	
}
@Override
public void onIllegalHttpStatusCode(int statusCode, String message)
{
	// TODO Auto-generated method stub
	
}
@Override
public void onAdClicked()
{
	// TODO Auto-generated method stub
	
}
@Override
public void onApplicationPause()
{
	// TODO Auto-generated method stub
	this.onPause();
}
@Override
public void onApplicationResume()
{
	// TODO Auto-generated method stub
	this.onResume();
}
@Override
protected void onDestroy() {
    super.onDestroy();

    // Report that the application is being ended
    mTracker.reportStop();
}
}
