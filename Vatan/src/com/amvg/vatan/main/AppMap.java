package com.amvg.vatan.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@SuppressLint("DefaultLocale")
public final class AppMap extends Activity{
	public static Intent myIntent; //hello
	private Context context;
	private String urlAddress[];
	public static Context appContex;
	public static String controller;
	public static String categoryName;
	public static String ID;
	public static String categoryID;
	public static String valuesAdvert[];
	public Context contextDialog;
	
	public static void setContext(Context context)
	{
		appContex=context;
	}
	
	public AppMap(Context context, Context contextDialog){
		this.context=context;
		myIntent = new Intent(context, Home.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		urlAddress = new String[1];
		this.contextDialog=contextDialog;
	}
	
	
	public void RunActivity(String controller, String categoryName, String ID, String categoryID)
    {
    	RunActivity(controller, categoryName, ID, categoryID, "");
    }
	
    public void RunActivity(String controller, String categoryName, String ID, String categoryID, String theRealCategoryId)
    {
        this.controller=controller;
        this.categoryName=categoryName;
        this.ID=ID;
        this.categoryID=categoryID;
        urlAddress[0]=controller;
        if (controller.equals("NewsGallery")) {
			DownloadDataNewsGallery newsgallery=new DownloadDataNewsGallery();
			newsgallery.execute(urlAddress);
		}
        
        if ((controller.equals("VideoCategory") || controller.equals("videocategory")) && ID.equals("0")) {
			urlAddress[0]="GalleryCategory";
		}
        
        setContext(this.context);
        AppMap.DownloadBannerData.bannerEnabled="false";
		
        
        
        if (controller.equals("Home") || controller.equals("home")) 
		{
			AppMap.myIntent.setClass(appContex, Home.class);
//			if (categoryID.equals("Splash")) {
////				
//				AppMap.myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			}
			appContex.startActivity(AppMap.myIntent);
		} 
        else if (controller.equals("Sampiy10") || controller.equals("sampiy10") || controller.equals("dummyvatanspor")) 
		{
        	AppMap.myIntent.setClass(appContex, Sampiyon.class);
        	appContex.startActivity(AppMap.myIntent);
		} 
		else if (controller.equals("BreakingNews") || controller.equals("breakingnews")) 
		{
			AppMap.myIntent.setClass(appContex, BreakingNews.class);
        	appContex.startActivity(AppMap.myIntent);
		} 
		else if (controller.equals("NewsCategory") || controller.equals("newscategory")) 
		{
			if (categoryID.equals("5"))
			{
				AppMap.myIntent.setClass(appContex, Sampiyon.class);
	        	appContex.startActivity(AppMap.myIntent);
			}
			else
			{
				AppMap.myIntent.setClass(appContex, NewsCategory.class);
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("categoryID", categoryID);
				appContex.startActivity(AppMap.myIntent);
			}
		} 
		else if (controller.equals("NewsCategories") || controller.equals("newscategories")) 
		{
			AppMap.myIntent.setClass(appContex, NewsCategories.class);
			appContex.startActivity(AppMap.myIntent);
		} 
		else if (controller.equals("Columnists") || controller.equals("columnists")) 
		{
			AppMap.myIntent.setClass(appContex, Columnists.class);
			appContex.startActivity(AppMap.myIntent);
		} 
		else if(controller.equals("MainMenu"))
		{
			AppMap.myIntent.setClass(AppMap.appContex, MainMenu.class);
			appContex.startActivity(AppMap.myIntent);
		} 
		else if (controller.equals("NewsArticle") || controller.equals("newsarticle") || controller.toLowerCase().equals("advertorial")) 
		{
			if (controller.toLowerCase().equals("advertorial")) 
			{
				new AdvertorialCount().execute("http://secure.milliyet.com.tr/redirect/Default.aspx?z=95&i=1&l=http%%3A%%2F%%2Fm.gazetevatan.com%%2FNews%%2FArticle%%3FID%%3D"+ID); //reklam sayım kodu için bu servisi çalıştırıyoruz
			}
			AppMap.myIntent.setClass(appContex, NewsArticle.class);
			AppMap.myIntent.putExtra("id", ID);
			appContex.startActivity(AppMap.myIntent);
		} 
		else if(controller.equals("BreakingNewsCategory") || controller.equals("breakingnewscategory"))
		{
//			AppMap.myIntent.setClass(appContex, BreakingNewsCategory.class);
			AppMap.myIntent.putExtra("categoryName", categoryName);
			AppMap.myIntent.putExtra("categoryID", categoryID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if(controller.equals("ColumnistArticle") || controller.equals("columnistarticle"))
		{
			AppMap.myIntent.setClass(appContex, ColumnistArticle.class);
			AppMap.myIntent.putExtra("id", ID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if(controller.equals("ColumnistArticles") || controller.equals("ColumnistArticles"))
		{
			AppMap.myIntent.setClass(appContex, ColumnistArticles.class);
			AppMap.myIntent.putExtra("id", ID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if(controller.equals("columnistarticles"))
		{
			AppMap.myIntent.setClass(appContex, ColumnistArticles.class);
			AppMap.myIntent.putExtra("id", ID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if(controller.equals("VatanTV") || controller.equals("vatantv") || controller.toLowerCase().equals("dummyvatantv") || controller.toLowerCase().equals("dummyvatantv"))
		{
			AppMap.myIntent.setClass(appContex, Video.class);
			AppMap.myIntent.putExtra("controller", controller);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("VideoCategory") || controller.equals("videocategory")) 
		{
			AppMap.myIntent.setClass(appContex, VideoCategory.class); //ID parametresi hangi sayfaya ait kateogrinin açılacağını belirlemek için kullanılıyor. (Video Category=1/Galeri Category=0)
			AppMap.myIntent.putExtra("categoryName", categoryName);
			AppMap.myIntent.putExtra("categoryID", categoryID);
			AppMap.myIntent.putExtra("isVideo", ID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("VideoClip") || controller.equals("videoclip")) // VideoClipByID mi yoksa VideoClipByCode mu olduğunu CategoryName parametresi ile belirliyoruz
		{
			AppMap.myIntent.setClass(appContex, VideoDetail.class);
			AppMap.myIntent.putExtra("id", ID);
			AppMap.myIntent.putExtra("isID", categoryName);
			AppMap.myIntent.putExtra("categoryId", theRealCategoryId);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("Gallery") || controller.equals("gallery") || controller.toLowerCase().equals("dummygallery"))
		{
			AppMap.myIntent.setClass(appContex, Video.class);
			AppMap.myIntent.putExtra("controller", controller);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("PhotoGallery") || controller.equals("photogallery")) //categoryID değişkenini galleryTitle'ı göndermek için kullandık.
		{
			AppMap.myIntent.setClass(appContex, PhotoGallery.class);
			AppMap.myIntent.putExtra("id", ID);
			AppMap.myIntent.putExtra("categoryName", categoryName);
			AppMap.myIntent.putExtra("galleryTitle", categoryID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("VideoPlay")) //categoryName değişkeni url'i göndermek için, ID değişkeni de code'u göndermek için kullanıldı.
		{
			AppMap.myIntent.setClass(appContex, VideoPlay.class);
			AppMap.myIntent.putExtra("url", categoryName);
			AppMap.myIntent.putExtra("code", ID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.toLowerCase().equals("dummyvatanspor")) 
		{
			AppMap.myIntent.setClass(appContex, Sampiyon.class);
        	appContex.startActivity(AppMap.myIntent);
		}
//		else if (controller.toLowerCase().equals("dummyvatanspor")) 
//		{
//			AppMap.myIntent.setClass(appContex, NewsCategory.class);
//			AppMap.myIntent.putExtra("categoryName", "Spor");
//			AppMap.myIntent.putExtra("categoryID", "5");
//			appContex.startActivity(AppMap.myIntent);
//		}
		else if (controller.equals("DummyMilliyetTV")) 
		{
//			AppMap.myIntent.setClass(appContex, Video.class);
			AppMap.myIntent.putExtra("controller", "Video");
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("NewsGallery"))
		{
//			AppMap.myIntent.setClass(appContex, PhotoGallery.class);
			AppMap.myIntent.putExtra("id", ID+"-#");
			AppMap.myIntent.putExtra("categoryName", categoryName);
			AppMap.myIntent.putExtra("galleryTitle", categoryID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("DummyEkonomi"))
		{
			AppMap.myIntent.setClass(appContex, NewsCategory.class);
			AppMap.myIntent.putExtra("categoryName", "Ekonomi");
			AppMap.myIntent.putExtra("categoryID", "2");
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("DummySkorer")) 
		{
			AppMap.myIntent.setClass(appContex, NewsCategory.class);
			AppMap.myIntent.putExtra("categoryName", "Ekonomi");
			AppMap.myIntent.putExtra("categoryID", "11");
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("Weather")) 
		{
			AppMap.myIntent.setClass(appContex, Weather.class);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("Horoscopes")) 
		{
			AppMap.myIntent.setClass(appContex, Horoscopes.class);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("WeatherLocation")) 
		{
			AppMap.myIntent.setClass(appContex, WeatherLocation.class);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.equals("HoroscopesArticle")) 
		{
			AppMap.myIntent.setClass(appContex, HoroscopesArticle.class);
			AppMap.myIntent.putExtra("ID", ID);
			appContex.startActivity(AppMap.myIntent);
		}
		else if (controller.toLowerCase().equals("weblink"))
		{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(categoryID)); //categoryID parametresi olarak yönlendirilecek olan web sayfasının URL'ini aldık.
			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			appContex.startActivity(browserIntent);
		}
		else if (controller.toLowerCase().equals("advertoriallink"))
		{
			
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://secure.milliyet.com.tr/redirect/Default.aspx?z=94&i=1&l="+URLEncoder.encode(ID).toString()));
			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			appContex.startActivity(browserIntent);
			
			
//			Uri uriUrl = Uri.parse(ID);  
//			AppMap.myIntent.setData(uriUrl);
////			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//			appContex.startActivity(AppMap.myIntent); 
//			AppMap.myIntent.setClass(appContex, VideoLive.class);
//			AppMap.myIntent.putExtra("url", categoryName);
//			appContex.startActivity(AppMap.myIntent);
		}
		
		
//		if (valuesAdvert[0].equals("true")) 
//		{
//			AppMap.myIntent.setClass(appContex, InterstitialBanner.class);
//			AppMap.myIntent.putExtra("Width", valuesAdvert[1]);
//			AppMap.myIntent.putExtra("Height", valuesAdvert[2]);
//			AppMap.myIntent.putExtra("ShowCloseButton", valuesAdvert[3]);
//			AppMap.myIntent.putExtra("URL", valuesAdvert[4]);
//			AppMap.myIntent.putExtra("HTML", valuesAdvert[5]);
//			appContex.startActivity(AppMap.myIntent);
//		}
        
//        DownloadData runApp=new DownloadData();
//        runApp.execute(urlAddress);
//		if (this.categoryID.equals("Splash")) 
//		{
//			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//			myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		}
//		else
//		{
//			
//		}
    }
    
    public class SearchResponseNewsGallery {
	    public List<ResultNewsGallery> root;
	}

	public class ResultNewsGallery {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("CategoryID")
	    public String CategoryID;
	    
	    @SerializedName("Category")
	    public String Category;
	    
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
	
	public class DownloadDataNewsGallery extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... urlAddress) {
			String jString = null;
			URL url;
			//InputStream is = null;
			
			//String android_id = Secure.getString(AppMap.appContex.getContentResolver(),Secure.ANDROID_ID); 
			
//			valuesAdvert[0]="false"; //reklam servisi geç yanıt verdiği için manuel olarak değer verdik ve sorguyu kapattık.
//			return "0";
			
			
			try {
				
				url = new URL("http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_NewsArticle&ArticleID="+AppMap.ID);
				URLConnection ucon = url.openConnection();	
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);	
				ByteArrayBuffer baf = new ByteArrayBuffer(50);		
				int current = 0;
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				
				Gson gson = new Gson();
				SearchResponseNewsGallery response = gson.fromJson(jString, SearchResponseNewsGallery.class);
				List<ResultNewsGallery> results = response.root;
				for (ResultNewsGallery result : results) 
				{
					AppMap.categoryName=result.Category;
					AppMap.categoryID=result.Title;
				}
				
				return jString;
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				return "hata1";
			} catch (IOException e) {
				e.printStackTrace();
				return "hata2";
			}
		}
	}
    
    
    public class SearchResponse {
	    public Result root;
	}

	public class Result {
	    @SerializedName("Enabled")
	    public String Enabled;
	
	    @SerializedName("Width")
	    public String Width;
	    
	    @SerializedName("Height")
	    public String Height;
	    
	    @SerializedName("ShowCloseButton")
	    public String ShowCloseButton;
	    
	    @SerializedName("URL")
	    public String URL;
	    
	    @SerializedName("HTML")
	    public String HTML;
	
	}
	
	public static String CategoryAdvertisementBanner(String controller)
	{
		if (controller.equals("BreakingNewsCategory") || controller.equals("breakingnewscategory") || controller.equals("NewsCategory") || controller.equals("newscategory") || controller.equals("VideoCategory") || controller.equals("videocategory")) 
		{
			return "&CategoryID="+categoryID;
		}
		else
		{
			return "";
		}
		
	}
	public static String CategoryAdvertisementInterstitial(String controller)
	{
		if (controller.equals("BreakingNewsCategory") || controller.equals("breakingnewscategory") || controller.equals("NewsCategory") || controller.equals("newscategory") || controller.equals("VideoCategory") || controller.equals("videocategory")) 
		{
			return "&CategoryID="+categoryID;
		}
		else
		{
			return "";
		}
		
	}
    
    public class DownloadData extends AsyncTask<String, String, String>{
    	
    	
    	private Intent myIntent;
    	
    	
    	public DownloadData()
    	{
    		
    		AppMap.valuesAdvert=new String[6];
    		valuesAdvert[0]="false";
    		myIntent = new Intent(context, Home.class);
    		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	}
    	
    	@Override
        protected void onPreExecute() {
//               Dialog = new ProgressDialog(contextDialog);
//               Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//               Dialog.setMessage("Yükleniyor...");
//               Dialog.setCancelable(false);
//               Dialog.show();
       
        }
    	
    	
		@Override
		protected String doInBackground(String... urlAddress) {
			String jString = null;
			URL url;
			//InputStream is = null;
			
		
			
			
			//String android_id = Secure.getString(AppMap.appContex.getContentResolver(),Secure.ANDROID_ID); 
//			
//			valuesAdvert[0]="false"; //reklam servisi geç yanıt verdiği için manuel olarak değer verdik ve sorguyu kapattık.
//			return "0";
			
			
			try {
				//"http://m2.milliyet.com.tr/MobileAdServiceTest/RestAPI/GetBannerV2/?BundleID="+SplashScreen.bundleId+"&Version="+SplashScreen.version+"&UDID="+SplashScreen.android_id+"&Devicea=&Zone="+urlAddress[0]+"_Android_Banner"
				
				url = new URL(SplashScreen.AdServerAPI+"?App=Vatan&AppVersion="+SplashScreen.version+"&Resolution="+SplashScreen.Resolution+"&Device=Android&Screen="+urlAddress[0]+CategoryAdvertisementInterstitial(urlAddress[0])+"&Type=Interstitial");
				
				
				
//				Log.e("Path::","http://m2.milliyet.com.tr/MobileAdServiceTest/Vatan_Android/?Zone="+urlAddress[0]+"_Interstitial&Device=Android&AppVersion="+SplashScreen.version+"&UDID="+SplashScreen.android_id);
				//url = new URL("http://m2.milliyet.com.tr/MobileAdServiceTest/RestAPI/GetBannerV2/?BundleIeD="+SplashScreen.bundleId+"&Version="+SplashScreen.version+"&UDID="+SplashScreen.android_id+"&Devicea=&Zone="+urlAddress[0]+"_Android_Banner");
//				Log.e("path","http://m2.milliyet.com.tr/MobileAdService/RestAPI/GetBanner/?BundleID="+SplashScreen.bundleId+"&Version="+SplashScreen.version+"&UDID="+SplashScreen.android_id+"&Device=Android&ZoneID="+urlAddress[0]+"_Android_Interstitial");
				//HttpsURLConnection ucon = url.openConnection();	
				URLConnection ucon = url.openConnection();	
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);	
				ByteArrayBuffer baf = new ByteArrayBuffer(50);		
				int current = 0;
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				
				Gson gson = new Gson();
				try {
//					SearchResponse response = gson.fromJson(jString, SearchResponse.class);
//					Result results = response.root;
					
					Result results = gson.fromJson(jString, Result.class);
//					Result results = response.root;
					
						valuesAdvert[0]=results.Enabled;
						valuesAdvert[1]=results.Width;
						valuesAdvert[2]=results.Height;
						valuesAdvert[3]=results.ShowCloseButton;
						valuesAdvert[4]=results.URL;
						valuesAdvert[5]=results.HTML;
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				
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
			
			//global.writeFile(result, dirName, fileName);
			//parseAndShowData(global.readData(fileName));
			AppMap.DownloadBannerData.bannerEnabled="false";
			urlAddress[0]=controller;
			
			if ((controller.equals("VideoCategory") || controller.equals("videocategory")) && ID.equals("0")) {
				urlAddress[0]="GalleryCategory";
			}
			
            DownloadBannerData banner =new DownloadBannerData();
            banner.execute(urlAddress);
	     }
    }
    
    public static class DownloadBannerData extends AsyncTask<String, String, String>{
    	
//    	public static String bannerEnabledBreakingNews="";
//    	public static String bannerEnabledColumnistArticle="";
//    	public static String bannerEnabledColumnistArticles="";
//    	public static String bannerEnabledColumnists="";
    	public static String bannerEnabled;
    	public static String bannerWidth;
    	public static String bannerHeight;
    	public static String bannerShowCloseButton;
    	public static String bannerURL;
    	public static String bannerHTML;
    	public static String bannerController;
    	
    	
    	public DownloadBannerData()
    	{
    		
    		//myIntent = new Intent(context, BreakingNews.class);
    		//myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	}
    	
		@Override
		protected String doInBackground(String... urlAddress) {
			String jString = null;
			URL url;
			//InputStream is = null;
			
			//String android_id = Secure.getString(AppMap.appContex.getContentResolver(),Secure.ANDROID_ID); 
			
			
//			bannerEnabled="false";  //reklam servisi geç yanıt verdiği için manuel olarak değer verdik ve sorguyu kapattık.
//			return "a";
			try {
				url = new URL(SplashScreen.AdServerAPI+"?App=Vatan&AppVersion="+SplashScreen.version+"&Resolution="+SplashScreen.Resolution+"&Device=Android&Screen="+urlAddress[0]+CategoryAdvertisementBanner(urlAddress[0])+"&Type=Banner");
//				Log.e("PATH::","http://m2.milliyet.com.tr/MobileAdServiceTest/RestAPI/GetBannerV2/?BundleID="+SplashScreen.bundleId+"&Version="+SplashScreen.version+"&UDID="+SplashScreen.android_id+"&Devicea=&Zone="+urlAddress[0]+"_Android_Banner");
				URLConnection ucon = url.openConnection();			
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);			
				ByteArrayBuffer baf = new ByteArrayBuffer(50);			
				int current = 0;
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				
				Gson gson = new Gson();
				try {
					Result results = gson.fromJson(jString, Result.class);
					
						bannerEnabled=results.Enabled;
						bannerWidth=results.Width;
						bannerHeight=results.Height;
						bannerShowCloseButton=results.ShowCloseButton;
						bannerURL=results.URL;
						bannerHTML=results.HTML;
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				
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
			//AppMap.appCoentex.fini
			if (controller.equals("Home") || controller.equals("home")) 
			{
				AppMap.myIntent.setClass(appContex, Home.class);
//				if (categoryID.equals("Splash")) {
////					
//					AppMap.myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				}
				appContex.startActivity(AppMap.myIntent);
			} 
			else if (controller.equals("BreakingNews") || controller.equals("breakingnews")) 
			{
				AppMap.myIntent.setClass(appContex, BreakingNews.class);
	        	appContex.startActivity(AppMap.myIntent);
			} 
			else if (controller.equals("NewsCategory") || controller.equals("newscategory")) 
			{
				
				AppMap.myIntent.setClass(appContex, NewsCategory.class);
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("categoryID", categoryID);
				appContex.startActivity(AppMap.myIntent);
			} 
			else if (controller.equals("NewsCategories") || controller.equals("newscategories")) 
			{
				AppMap.myIntent.setClass(appContex, NewsCategories.class);
				appContex.startActivity(AppMap.myIntent);
			} 
			else if (controller.equals("Columnists") || controller.equals("columnists")) 
			{
				AppMap.myIntent.setClass(appContex, Columnists.class);
				appContex.startActivity(AppMap.myIntent);
			} 
			else if(controller.equals("MainMenu"))
			{
				AppMap.myIntent.setClass(AppMap.appContex, MainMenu.class);
				appContex.startActivity(AppMap.myIntent);
			} 
			else if (controller.equals("NewsArticle") || controller.equals("newsarticle") || controller.toLowerCase().equals("advertorial")) 
			{
				if (controller.toLowerCase().equals("advertorial")) 
				{
					new AdvertorialCount().execute("http://secure.milliyet.com.tr/redirect/Default.aspx?z=94&i=1&l=http%3A%2F%2Fm2.gazetevatan.com.tr%2FNews%2FNewsArticle%3FID%3D"+ID); //reklam sayım kodu için bu servisi çalıştırıyoruz
				}
				AppMap.myIntent.setClass(appContex, NewsArticle.class);
				AppMap.myIntent.putExtra("id", ID);
				appContex.startActivity(AppMap.myIntent);
			} 
			else if(controller.equals("BreakingNewsCategory") || controller.equals("breakingnewscategory"))
			{
//				AppMap.myIntent.setClass(appContex, BreakingNewsCategory.class);
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("categoryID", categoryID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if(controller.equals("ColumnistArticle") || controller.equals("columnistarticle"))
			{
				AppMap.myIntent.setClass(appContex, ColumnistArticle.class);
				AppMap.myIntent.putExtra("id", ID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if(controller.equals("ColumnistArticles") || controller.equals("ColumnistArticles"))
			{
				AppMap.myIntent.setClass(appContex, ColumnistArticles.class);
				AppMap.myIntent.putExtra("id", ID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if(controller.equals("columnistarticles"))
			{
				AppMap.myIntent.setClass(appContex, ColumnistArticles.class);
				AppMap.myIntent.putExtra("id", ID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if(controller.equals("Video") || controller.equals("video") || controller.toLowerCase().equals("dummyvideo") || controller.toLowerCase().equals("dummyvatantv"))
			{
				AppMap.myIntent.setClass(appContex, Video.class);
				AppMap.myIntent.putExtra("controller", controller);
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("VideoCategory") || controller.equals("videocategory")) 
			{
				AppMap.myIntent.setClass(appContex, VideoCategory.class); //ID parametresi hangi sayfaya ait kateogrinin açılacağını belirlemek için kullanılıyor. (Video Category=1/Galeri Category=0)
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("categoryID", categoryID);
				AppMap.myIntent.putExtra("isVideo", ID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.toLowerCase().equals("vatantvcategory"))
			{
				AppMap.myIntent.setClass(appContex, VideoCategory.class); //ID parametresi hangi sayfaya ait kateogrinin açılacağını belirlemek için kullanılıyor. (Video Category=1/Galeri Category=0)
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("categoryID", categoryID);
				AppMap.myIntent.putExtra("isVideo", "1");
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("VideoClip") || controller.equals("videoclip")) // VideoClipByID mi yoksa VideoClipByCode mu olduğunu CategoryName parametresi ile belirliyoruz
			{
				AppMap.myIntent.setClass(appContex, VideoDetail.class);
				AppMap.myIntent.putExtra("id", ID);
				AppMap.myIntent.putExtra("isID", categoryName);
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("Gallery") || controller.equals("gallery") || controller.toLowerCase().equals("dummygallery"))
			{
				AppMap.myIntent.setClass(appContex, Video.class);
				AppMap.myIntent.putExtra("controller", controller);
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("PhotoGallery") || controller.equals("photogallery")) //categoryID değişkenini galleryTitle'ı göndermek için kullandık.
			{
				AppMap.myIntent.setClass(appContex, PhotoGallery.class);
				AppMap.myIntent.putExtra("id", ID);
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("galleryTitle", categoryID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("VideoPlay")) //categoryName değişkeni url'i göndermek için, ID değişkeni de code'u göndermek için kullanıldı.
			{
				AppMap.myIntent.setClass(appContex, VideoPlay.class);
				AppMap.myIntent.putExtra("url", categoryName);
				AppMap.myIntent.putExtra("code", ID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.toLowerCase().equals("dummyvatanspor")) 
			{
				AppMap.myIntent.setClass(appContex, Sampiyon.class);
	        	appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("DummyMilliyetTV")) 
			{
//				AppMap.myIntent.setClass(appContex, Video.class);
				AppMap.myIntent.putExtra("controller", "Video");
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("NewsGallery"))
			{
//				AppMap.myIntent.setClass(appContex, PhotoGallery.class);
				AppMap.myIntent.putExtra("id", ID+"-#");
				AppMap.myIntent.putExtra("categoryName", categoryName);
				AppMap.myIntent.putExtra("galleryTitle", categoryID);
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("DummyEkonomi"))
			{
				AppMap.myIntent.setClass(appContex, NewsCategory.class);
				AppMap.myIntent.putExtra("categoryName", "Ekonomi");
				AppMap.myIntent.putExtra("categoryID", "2");
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.equals("DummySkorer")) 
			{
				AppMap.myIntent.setClass(appContex, NewsCategory.class);
				AppMap.myIntent.putExtra("categoryName", "Ekonomi");
				AppMap.myIntent.putExtra("categoryID", "11");
				appContex.startActivity(AppMap.myIntent);
			}
			else if (controller.toLowerCase().equals("advertoriallink"))
			{
				
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://secure.milliyet.com.tr/redirect/Default.aspx?z=94&i=1&l="+URLEncoder.encode(ID).toString()));
				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				appContex.startActivity(browserIntent);
				
				
//				Uri uriUrl = Uri.parse(ID);  
//				AppMap.myIntent.setData(uriUrl);
////				Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//				appContex.startActivity(AppMap.myIntent); 
//				AppMap.myIntent.setClass(appContex, VideoLive.class);
//				AppMap.myIntent.putExtra("url", categoryName);
//				appContex.startActivity(AppMap.myIntent);
			}
			
			
			if (valuesAdvert[0].equals("true")) 
			{
				AppMap.myIntent.setClass(appContex, InterstitialBanner.class);
				AppMap.myIntent.putExtra("Width", valuesAdvert[1]);
				AppMap.myIntent.putExtra("Height", valuesAdvert[2]);
				AppMap.myIntent.putExtra("ShowCloseButton", valuesAdvert[3]);
				AppMap.myIntent.putExtra("URL", valuesAdvert[4]);
				AppMap.myIntent.putExtra("HTML", valuesAdvert[5]);
				appContex.startActivity(AppMap.myIntent);
			}
			
			
//			Dialog.dismiss();
//		    Dialog = null;
			
	     }
		
    }
	
}
