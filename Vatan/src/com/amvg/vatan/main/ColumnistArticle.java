package com.amvg.vatan.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;

import com.comscore.analytics.comScore;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class ColumnistArticle extends Activity implements OnTouchListener, Handler.Callback{

	static public String[] values;
	static public String dirName;
	static public String fileName;
	static public Global global;
	static public WebView webView;
	private String ID;
	private String customHtml;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private View BannerDivider;
	
	private final Handler handler = new Handler(this);
	private boolean ShowShareButton=false;
	private static final int CLICK_ON_WEBVIEW = 1;
	
	JavaScriptInterface JSInterface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_columnist_article);
		
		mGaInstance = GoogleAnalytics.getInstance(this);  //sayım kodu
		mGaTracker = mGaInstance.getTracker("UA-15581378-14"); //sayım kodu
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());
		
		this.webView=(WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setOnTouchListener(this);
		//JSInterface = new JavaScriptInterface(this);
		//webView.addJavascriptInterface(JSInterface, "JSInterface"); 
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     ID = extras.getString("id");
		}
		
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.BannerEnabled)
		{
			BannerDivider.setVisibility(View.VISIBLE);
		}
		
		this.dirName="/sdcard/Vatan/columnistsarticles";
		this.fileName=this.dirName+"/"+ID+".txt";
		global=new Global();
		String urlAdress[]={"http://www13.gazetevatan.com/mobileapi/mobile_ColumnistArticleDetails.asp?ArticleID="+ID};
		DownloadData downloadData=new DownloadData();
		try {
			if (global.isFileOK(this.fileName)) {
				this.parseAndShowData(global.readData(this.fileName));
			}else{
				downloadData.execute(urlAdress);
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
		
//		webViewBanner=(WebView)findViewById(R.id.webViewBanner);
//		webViewBanner.getSettings().setJavaScriptEnabled(true);
//		webViewBanner.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//		webViewBanner.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
//		{
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				webViewBanner.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}
//			else
//			{
//				webViewBanner.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//			webViewBanner.getLayoutParams().height=(int) (Integer.parseInt(AppMap.DownloadBannerData.bannerHeight)*SplashScreen.scale);
//		}
//		else
//		{
//			webViewBanner.getLayoutParams().width=0;
//       		webViewBanner.getLayoutParams().height=0;
//		}
		
		final AppMap appmap=new AppMap(getApplicationContext(),ColumnistArticle.this);
		
		webView.setWebViewClient(new WebViewClient(){
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	Global.parseURLrequest(url, getApplicationContext(), ColumnistArticle.this,"");
		        return true;
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
		ImageView menuLogo=(ImageView)findViewById(R.id.backImage);
		menuLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
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
		});
		
		((ImageView)findViewById(R.id.SharButton)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
//				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//				sharingIntent.setType("text/*");
//				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(customHtml));
//				startActivity(Intent.createChooser(sharingIntent,"Share using"));
				
				Intent sharingIntent = new Intent(Intent.ACTION_SEND); 
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://w9.gazetevatan.com/haberdetay.asp?Newsid="+ID); 
				startActivity(Intent.createChooser(sharingIntent,"Share via"));
				
				
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
	    mGaTracker.sendView("ColumnistArticle - "+ID); //sayma kodu
	    mGaTrackerGlobal.sendView("ColumnistArticle - "+ID);
	  }
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
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
			sharingIntent.setType("text/*");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(customHtml));
			startActivity(Intent.createChooser(sharingIntent,"Share using"));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
    
    /*@Override
    public void onAttachedToWindow() {
        openOptionsMenu(); 
    };*/
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("ColumnistID")
	    public String ColumnistID;
	    
	    @SerializedName("Columnist")
	    public String Columnist;
	    
	    @SerializedName("CornerName")
	    public String CornerName;
	    
	    @SerializedName("PublishTime")
	    public String PublishTime;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("Spot")
	    public String Spot;
	    
	    @SerializedName("Description")
	    public String Description;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	}
	
	public class JavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        public void changeActivity()
        {
            //Intent i = new Intent(ColumnistsDetail.this, nextActivity.class);
            //startActivity(i);
            finish();
        }
    }
	
	private String createHTML(String ColumnistID, String Columnist, String CornerName, String PublishTime, String Title, String Spot, String Description, String ImageURL){
		customHtml = "";
		customHtml=customHtml+"<!DOCTYPE HTML>";
		customHtml=customHtml+"<html>";
		customHtml=customHtml+"<head>";
		customHtml=customHtml+"<script type=\"text/javascript\">";
		customHtml=customHtml+"function displaymessage()";
		customHtml=customHtml+"{";
		customHtml=customHtml+"JSInterface.changeActivity();";
		customHtml=customHtml+"}";
		customHtml=customHtml+"</script>";
		customHtml=customHtml+"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\n";
		customHtml=customHtml+"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml=customHtml+"<link rel=\"stylesheet\" href=\"css/ColumnistArticleAndroid.css\" />";
		customHtml=customHtml+"</head>";
		customHtml=customHtml+"<body>";
		customHtml=customHtml+"<div class=\"HeaderBar\">Yazarlar</div>";
		
		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
		{
			customHtml=customHtml+"<div class=\"Banner\" style=\"width:"+(int)(SplashScreen.width/SplashScreen.scale)+"px; height:"+AppMap.DownloadBannerData.bannerHeight+"px; border-bottom:2px solid #005B7F\">"+AppMap.DownloadBannerData.bannerHTML+"</div>";
		}
		
		customHtml=customHtml+"<div class=\"Content\">";
		if (!ImageURL.equals("")) {
			
			customHtml=customHtml+"<div class=\"Image\"><img src=\""+ImageURL+"\" /></div>";
		}
		customHtml=customHtml+"<div class=\"Columnist\">"+Columnist+"</div>";
		//customHtml=customHtml+"<div class=\"CornerName\">"+CornerName+"</div>";
		customHtml=customHtml+"<div class=\"ColumnistArticles\"><a href=\"ColumnistArticles://?P="+URLEncoder.encode("{\"ColumnistID\":\""+ColumnistID+"\"}") +"\">Tüm Yazıları</a></div>";
		String[] time=PublishTime.split(" ");
		customHtml=customHtml+"<div class=\"PublishDate\">"+time[0]+"</div>";
		customHtml=customHtml+"<div class=\"Title\">"+Title+"</div>";
		customHtml=customHtml+"<div class=\"Spot\">"+Spot+"</div>";
		customHtml=customHtml+"<div class=\"Description\">"+Description+"</div>";
		customHtml=customHtml+"</div>";
		customHtml += "<div class=\"Footer\">";
		customHtml += "<p class=\"Copyright\"><b>Copyright &copy; 2014 Vatan Gazetesi</b></p>";
		customHtml += "</div>";
		customHtml=customHtml+"</body>";
		customHtml=customHtml+"</html>";
		return customHtml;
	}
	
	private void parseAndShowData(String JsonString) throws JSONException{
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		values=new String[9];
		for (Result result : results) {
			webView.loadDataWithBaseURL("file:///android_asset/", createHTML(result.ColumnistID, result.Columnist, result.CornerName, result.PublishTime, result.Title, result.Spot, result.Description, result.ImageURL), "text/html", "UTF-8", "");
			
		}
		
	}
	
	public class DownloadData extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... urlAddress) {
			String jString = null;
			URL url;
			try {
				url = new URL(urlAddress[0]);
				URLConnection ucon = url.openConnection();			
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);			
				ByteArrayBuffer baf = new ByteArrayBuffer(50);			
				int current = 0;
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				return jString;
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				return "hata1";
			} catch (IOException e) {
				e.printStackTrace();
				return "hata2";
			}
	}
		protected void onPostExecute(String result) { 
			
			try {
				global.writeFile(result, dirName, fileName);
				parseAndShowData(global.readData(fileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	}
	@Override
	public void onBackPressed() {
		try {
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
	
	@Override
	public boolean handleMessage(Message msg)
	{
		Log.e("handleMessage","HandleMessage");
		// TODO Auto-generated method stub
	    if (msg.what == CLICK_ON_WEBVIEW){
	    	if (ShowShareButton)
			{
	    		((ImageView)findViewById(R.id.SharButton)).setVisibility(View.GONE);
	    		ShowShareButton=false;
			}
	    	else
	    	{
	    		((ImageView)findViewById(R.id.SharButton)).setVisibility(View.VISIBLE);
	    		ShowShareButton=true;
	    	}
	    	
//	    	openOptionsMenu();
//	        Toast.makeText(this, "WebView clicked", Toast.LENGTH_SHORT).show();
	        return true;
	    }
		// TODO Auto-generated method stub
		return false;
	}

	int eventGetActionBeforeKont=-1;
	boolean eventGetActionBeforeBoolean=false;
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		Log.e("Girdi","Girdi");
		// TODO Auto-generated method stub
		if (event.getAction()==0)
		{
//			eventGetActionBeforeKont=0;
			eventGetActionBeforeBoolean=true;
		}
		else if (event.getAction()==2)
		{
			eventGetActionBeforeBoolean=false;
		}
		else if (event.getAction()==1 && eventGetActionBeforeBoolean && v.getId() == R.id.webView)
		{
			Log.e("Girdi","Girdi");
	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500); 
		}
			
		
//		Log.e("eventGetaction:",Integer.toString(event.getAction()));
//	    if (v.getId() == R.id.webView && event.getAction() == MotionEvent.ACTION_MOVE){
//	    	Log.e("Girdi","Girdi");
//	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500); 
//	    }
		return false;
	}

}
