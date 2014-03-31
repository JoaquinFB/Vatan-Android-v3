package com.amvg.vatan.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

public class VideoDetail extends Activity implements OnTouchListener, Handler.Callback{
	static public String[] values;
	static public String dirName;
	static public String fileName;
	static public Global global;
	static public WebView webView;
	private String ID;
	static private String customHtml;
	private String  category;
	private String publishTime;
	private String title;
	private String spot;
	private String imageUrl;
	private String viewCount;
	private String code;
	private String videoCode;
	private String isID;
	private Tracker mGaTracker;
	private Tracker mGaTrackerGlobal;
	private GoogleAnalytics mGaInstance;
	private View BannerDivider;
	
	private final Handler handler = new Handler(this);
	private boolean ShowShareButton=false;
	private static final int CLICK_ON_WEBVIEW = 1;

	private String categoryId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		 
		mGaInstance = GoogleAnalytics.getInstance(this);  //sayım kodu
		mGaTracker = mGaInstance.getTracker("UA-15581378-14");
		mGaTrackerGlobal = mGaInstance.getTracker("UA-15581378-28");
		
		comScore.setAppName("Vatan");
		comScore.setAppContext(this.getApplicationContext());
		
		this.webView=(WebView)findViewById(R.id.webView);
		this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setOnTouchListener(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     ID = extras.getString("id");
		     isID=extras.getString("isID");
		     categoryId = extras.getString("categoryId");
		}
		
		BannerDivider=(View)findViewById(R.id.BannerDivider);
		if (Home.BannerEnabled)
		{
			BannerDivider.setVisibility(View.VISIBLE);	
		}
		
		
		this.dirName="/sdcard/Vatan/videodetail";
		this.fileName=this.dirName+"/"+ID+".txt";
		global=new Global();
		String urlAdress[]={"http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_VatanTVVideoClipByID&VideoClipID="+ID};
		if (isID.equals("0")) {
			urlAdress[0]="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_VatanTVVideoClipByCode&Code="+ID; //Vatan uygulamasında VideoClipByCode yapısı kullanılmıyor.
		}
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
//		
//		webViewBanner=(WebView)findViewById(R.id.webViewBanner);
//		webViewBanner.getSettings().setJavaScriptEnabled(true);
//		webViewBanner.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//		webViewBanner.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
//		{
//			
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
		
		webView.setWebViewClient(new WebViewClient(){
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	Global.parseURLrequest(url, getApplicationContext(), VideoDetail.this,code, VideoDetail.this.categoryId);
		    	return true;
		    }
		});
		
		//downloadData.execute(urlAdress);
		final AppMap appmap=new AppMap(getApplicationContext(),VideoDetail.this);
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
				AppMap.DownloadBannerData.bannerEnabled="false";
				   try {
					   TestFragmentVideo.image.setClickable(true);
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
		
		/*webView.setWebViewClient(new WebViewClient(){
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	url=URLDecoder.decode(url);
		    	webView.loadUrl(url);*/
		    	//Global.parseURLrequest(url, getApplicationContext());
		    	/*Log.e("URL",url);
		    	url=url.toLowerCase();
		    	Log.e("soru isareti","://?P=");
		    	String[] controller=new String[2];
		    	String[] ID=new String[2];
		    	controller=url.split("://");
		    	ID=controller[1].split("p=");
		    	appmap.RunActivity(controller[0], "", ID[1],"");*/
		        //return true;
		    //}
		//});
		
		((ImageView)findViewById(R.id.SharButton)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent sharingIntent = new Intent(Intent.ACTION_SEND); 
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://videogaleri.gazetevatan.com/video-izle/m-"+videoCode+".html"); 
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
	    // Send a screen view when the Activity is displayed to the user.
	    
	    	mGaTracker.sendView("VideoClip - "+ID); // sayım kodu
	    	mGaTrackerGlobal.sendView("VideoClip - "+ID);
	    	FlurryAgent.onStartSession(this, "J7LIJ4D8612IYIAF6REK");
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
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://videogaleri.gazetevatan.com/"+this.ID+"_9_m.html");
			startActivity(Intent.createChooser(sharingIntent,"Share via"));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
	
	@Override
	public void onBackPressed() {
		AppMap.DownloadBannerData.bannerEnabled="false";
		   try {
			   TestFragmentVideo.image.setClickable(true);
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
		} catch (Exception e) {
			// TODO: handle exception
		}
	   finish();
	}
	
	public static String DownloadText(String url){
	    StringBuffer result = new StringBuffer();
	    try{
	        URL jsonUrl = new URL(url);

	        InputStreamReader isr  = new InputStreamReader(jsonUrl.openStream());

	        BufferedReader in = new BufferedReader(isr);

	        String inputLine;

	        while ((inputLine = in.readLine()) != null){
	            result.append(inputLine);
	        }
	        in.close();
	        isr.close();
	    }catch(Exception ex){
	        result = new StringBuffer("TIMEOUT");
	        //Log.e(Util.AppName, ex.toString());
	    }
	        
	    return result.toString();
	}
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Code")
	    public String Code;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("Spot")
	    public String Spot;
	    
	    @SerializedName("PublishTime")
	    public String PublishTime;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	    
	    @SerializedName("ViewCount")
	    public String ViewCount;
	    
	    @SerializedName("VideoURL")
	    public String VideoURL;
	
	}
	
	private String createHTML(String Category, String PublishTime, String Title, String Spot, String ImageURL, String videoURL, String ViewCount){
		customHtml="";
		customHtml=customHtml+"<!DOCTYPE HTML>";
		customHtml=customHtml+"<html>";
		customHtml=customHtml+"<head>";
		customHtml=customHtml+"<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">\n";
		customHtml=customHtml+"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml=customHtml+"<link rel=\"stylesheet\" href=\"css/VideoClipAndroid.css\" />";
		customHtml=customHtml+"</head>";
		customHtml=customHtml+"<body>";
		customHtml=customHtml+"<div class=\"HeaderBar\">Video</div>";
		
		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
		{
			customHtml=customHtml+"<div class=\"Banner\" style=\"width:"+(int)(SplashScreen.width/SplashScreen.scale)+"px; height:"+AppMap.DownloadBannerData.bannerHeight+"px; border-bottom:2px solid #005B7F\">"+AppMap.DownloadBannerData.bannerHTML+"</div>";
		}
		
		if (!ImageURL.equals("")) {
			String ImageResizeURL="http://im.milliyet.com.tr/320/0/"+ImageURL.substring(7, ImageURL.length()).replaceAll("/", "-.-")+".jpg";//http://video.milliyet.com.tr/d/h/IosMobile.ashx?VideoCode="+Code
			customHtml=customHtml+"<div class=\"ContentImage\"><a href=\""+videoURL+"\"><img src=\""+ImageResizeURL+"\" /><span class=\"VideoPlayIcon\"></span></a></div>";
		}
		Log.e("videoURL:",videoURL);
		customHtml=customHtml+"<div class=\"Content\">";
		customHtml=customHtml+"<div class=\"Title\">"+Title+"</div>";
		customHtml=customHtml+"<div class=\"Spot\">"+Spot+"</div>";
		String[] time=PublishTime.split(" ");
		customHtml=customHtml+"<div class=\"PublishDate\"><b>Yay�nlanma Tarihi:</b> "+time[0]+"</div>";
//		customHtml=customHtml+"<div class=\"ViewCount\"><b>İzlenme Sayısı:</b>"+ViewCount+"</div>";
//		customHtml=customHtml+"<div class=\"Category\"><b>Kategorisi:</b>"+Category+"</div>";
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
		values=new String[8];
		for (Result result : results) {
			Log.e("result.videoURL",result.VideoURL);
			publishTime=result.PublishTime;
			title=result.Title;
			spot=result.Spot;
			imageUrl=result.ImageURL;
			viewCount=result.ViewCount;
			code=result.ID;
			videoCode=result.Code;
			webView.loadDataWithBaseURL("file:///android_asset/", createHTML(category, publishTime, title, spot, imageUrl, result.VideoURL, viewCount), "text/html", "UTF-8", "");
			//webView.loadDataWithBaseURL("file:///android_asset/", createHTML(result.Category, result.PublishTime, result.Title, result.Spot, result.ImageURL, result.Code, result.ViewCount), "text/html", "UTF-8", "");
			
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
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
