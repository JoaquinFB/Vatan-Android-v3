package com.amvg.vatan.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.viewpagerindicator.PageIndicator;

public class VideoAccessData extends AsyncTask<String, String, String>
{
	static public String[][] values;
	private String pathName;
	private String fileName;
	private Context context;
	private Context contextDialog;
	private Global global;
	private String DirName="/sdcard/Vatan/vatantv/";
	private int updateTime;
	private final String advertorialContentName="advertoriallink";
	private ProgressDialog Dialog;
	private float DpHeight;
	private VatanTvFragmentAdapter mAdapter;
	private android.support.v4.app.FragmentManager Fragment_Manager;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private int dataCountHeadlines;
	private View VideoItems_view;
	private LayoutInflater LayInflater;
	private LinearLayout FeaturedLayout;
	private LinearLayout CategoryLayout;
	private DataModelVideoHeadlines dataVideoHeadlines;
	private DataModelVideoCategories dataVideoCategories;
	private ArrayList<DataModelVideoHeadlines> dataArray_VideoHeadlines;
	private ArrayList<DataModelVideoHeadlines> dataArray_VideoHomePage;
	private ArrayList<DataModelVideoCategories> dataArray_VideoCategories;
	private DataModelVideoImageLoad VideoImageLoad;
	private static AppMap appMap;
	private int i;
	private String[] parseData;
	private String Controller;
	private String TVname;
	
	public VideoAccessData(Context context, Context contextDialog,android.support.v4.app.FragmentManager fragmentManager, ViewPager mPager, PageIndicator mIndicator, LinearLayout featuredLayout, LinearLayout categoryLayout, LayoutInflater layInflater, float dpHeight, String controller)
	{
		this.global=new Global();
		dataArray_VideoHeadlines=new ArrayList<DataModelVideoHeadlines>();
		dataArray_VideoHomePage=new ArrayList<DataModelVideoHeadlines>();
		dataArray_VideoCategories=new ArrayList<DataModelVideoCategories>();
		this.LayInflater=layInflater;
		this.FeaturedLayout=featuredLayout;
		this.CategoryLayout=categoryLayout;
		this.context=context;
		this.updateTime=600;
		this.contextDialog=contextDialog;
		this.mPager=mPager;
		this.mIndicator=mIndicator;
		this.dataCountHeadlines=0;
		this.parseData=new String[2];
		this.Fragment_Manager=fragmentManager;
		this.DpHeight=dpHeight;
		appMap=new AppMap(this.context, this.contextDialog);
		this.Controller=controller;
		if (this.Controller.toLowerCase().equals("vatantv") || this.Controller.toLowerCase().equals("dummyvatantv"))
		{
			TVname="MilliyetTV";
			this.DirName="/sdcard/Vatan/vatantv/";
		}
		else
		{
			TVname="SkorerTV";
			this.DirName="/sdcard/Milliyet/skorertv/";
		}
	}
	
	private String selectFileName(int i)
	{
		String fileName="abc";
		if (i==0) 
		{
			fileName="headlines";
		}
		else if (i==1) 
		{
			fileName="featured";
		}
		else if(i==2)
		{
			fileName="categories";
		}
		else if(i==3)
		{
			fileName="weather";
		}
		return fileName;
	}
	
	private boolean haveFile(String fileName) throws IOException
	{
		File file = new File(fileName);
		if (file.exists()) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isUpdateTimeOK(java.sql.Time lastTime, Calendar fileUpdateDate)
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int nowSeconds=0;
		int fileSeconds=0;
		if (fileUpdateDate.get(Calendar.YEAR)==today.year && fileUpdateDate.get(Calendar.MONTH)==today.month && fileUpdateDate.get(Calendar.DAY_OF_MONTH)==today.monthDay) 
		{
			nowSeconds=today.hour*3600+today.minute*60+today.second;
			fileSeconds=lastTime.getHours()*3600+lastTime.getMinutes()*60+lastTime.getSeconds();
			if (nowSeconds-fileSeconds<this.updateTime) 
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public boolean areFilesOK() throws IOException, ParseException
	{
		for (int i = 0; i < 4; i++) 
		{
			if (!haveFile(this.DirName+selectFileName(i)+".txt")) 
			{
				return false;
			}
			else
			{
				File file = new File(this.DirName+selectFileName(i)+".txt");
				java.sql.Time lastTime=new java.sql.Time(file.lastModified());
				Date lastModDate = new Date(file.lastModified());
				final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        final Calendar c = Calendar.getInstance();
		        c.setTime(df.parse(lastModDate.toString()));
		        if (!isUpdateTimeOK(lastTime, c)) 
		        {
					return false;
				}
			}
		}
		return true;
	}
	
	public class SearchResponseHeadlinesFeatured {
	    public List<ResultHeadlinesFeatured> root;
	}

	public class ResultHeadlinesFeatured {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	    
	    @SerializedName("ThumbURL")
	    public String ThumbURL;
	    
	    @SerializedName("Link")
	    public String Link;
	    
	    @SerializedName("ViewCount")
	    public String ViewCount;
	    
	    @SerializedName("CommentCount")
	    public String CommentCount;
	    
	    @SerializedName("PositiveVoteCount")
	    public String PositiveVoteCount;
	    
	    @SerializedName("NegativeVoteCount")
	    public String NegativeVoteCount;
	}
	
	public class SearchResponseCategories {
	    public List<ResultCategories> root;
	}

	public class ResultCategories {
	    @SerializedName("ID")
	    public String ID;
	
	    @SerializedName("Name")
	    public String Name;
	    
	    @SerializedName("SortOrder")
	    public String SortOrder;
	    
	    @SerializedName("Featured")
	    public String Featured;
	    
	    @SerializedName("Website")
	    public String Website;
	}
	
	public class SearchResponseWeather {
	    public List<ResultWeather> root;
	}

	public class ResultWeather {
	    @SerializedName("LocationID")
	    public String LocationID;
	
	    @SerializedName("Location")
	    public String Location;
	    
	    @SerializedName("City")
	    public String City;
	    
	    @SerializedName("Date")
	    public String Date;
	    
	    @SerializedName("Icon")
	    public String Icon;
	    
	    @SerializedName("IconName")
	    public String IconName;
	    
	    @SerializedName("Temperature")
	    public String Temperature;
	    
	    @SerializedName("FeelsLike")
	    public String FeelsLike;
	    
	    @SerializedName("High")
	    public String High;
	    
	    @SerializedName("Low")
	    public String Low;
	    
	    @SerializedName("WeatherText")
	    public String WeatherText;
	    
	    @SerializedName("Humidity")
	    public String Humidity;
	    
	    @SerializedName("WindDirection")
	    public String WindDirection;
	    
	    @SerializedName("WindSpeed")
	    public String WindSpeed;
	    
	    @SerializedName("Visibility")
	    public String Visibility;
	    
	    @SerializedName("Precipitation")
	    public String Precipitation;
	    
	    @SerializedName("Pressure")
	    public String Pressure;
	    
	    @SerializedName("DewPoint")
	    public String DewPoint;
	}
	
	
	public boolean isFileOK() throws IOException, ParseException
	{
		return this.global.isFileOK(this.fileName);
	}
	
	public void readData()
	{
		String aBuffer = "";
		try 
		{ //okumaya yarıyor
			File myFile = new File(this.DirName+"size.txt");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null) 
			{
				aBuffer += aDataRow + "\n";
			}
			myReader.close();
		} 
		catch (Exception e) {
		}
		aBuffer=aBuffer.trim();
		for (int i = 0; i < 4; i++) 
		{
			aBuffer = "";
			try 
			{ //okumaya yarıyor
				File myFile = new File(this.DirName+selectFileName(i)+".txt");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) 
				{
					aBuffer += aDataRow + "\n";
				}
				myReader.close();
			} 
			catch (Exception e) 
			{
			}
			
			if (i==0) 
			{
				Gson gson = new Gson();
				SearchResponseHeadlinesFeatured responseHeadlines = gson.fromJson(aBuffer, SearchResponseHeadlinesFeatured.class);
				List<ResultHeadlinesFeatured> resultsHeadlines = responseHeadlines.root;
				for (ResultHeadlinesFeatured resultHeadlines : resultsHeadlines) 
				{
					dataVideoHeadlines=new DataModelVideoHeadlines();
					
					if (resultHeadlines.ContentType.toLowerCase().equals(advertorialContentName)) 
					{
						dataVideoHeadlines.setID(resultHeadlines.Link);
					}
					else
					{
						dataVideoHeadlines.setID(resultHeadlines.ID);
					}
					dataVideoHeadlines.setContentType(resultHeadlines.ContentType);
					dataVideoHeadlines.setTitle(resultHeadlines.Title);
					dataVideoHeadlines.setImageURL(resultHeadlines.ImageURL);
					dataVideoHeadlines.setThumbURL(resultHeadlines.ThumbURL);
					dataVideoHeadlines.setLink(resultHeadlines.Link);
					dataVideoHeadlines.setViewCount(resultHeadlines.ViewCount);
					dataVideoHeadlines.setCommentCount(resultHeadlines.CommentCount);
					dataVideoHeadlines.setPositiveVoteCount(resultHeadlines.PositiveVoteCount);
					dataVideoHeadlines.setNegativeVoteCount(resultHeadlines.NegativeVoteCount);
					dataArray_VideoHeadlines.add(dataVideoHeadlines);
				}
//				mAdapter=new VatanTvFragmentAdapter(fm, results, controller)
				mAdapter=new VatanTvFragmentAdapter(this.Fragment_Manager, dataArray_VideoHeadlines, Controller);
				VatanTvFragmentAdapter.setContent(context, contextDialog);
		       	mPager.setAdapter(mAdapter);
		       	mIndicator.setViewPager(mPager);
		       	mIndicator.setCurrentItem(0);
			}
			else if (i==1) 
			{
				Gson gson = new Gson();
				SearchResponseHeadlinesFeatured responseFeatured = gson.fromJson(aBuffer, SearchResponseHeadlinesFeatured.class);
				List<ResultHeadlinesFeatured> resultsFeatured = responseFeatured.root;
				for (ResultHeadlinesFeatured resultFeatured : resultsFeatured) 
				{
					dataVideoHeadlines=new DataModelVideoHeadlines();
					if (resultFeatured.ContentType.toLowerCase().equals(advertorialContentName)) 
					{
						dataVideoHeadlines.setID(resultFeatured.Link);
					}
					else
					{
						dataVideoHeadlines.setID(resultFeatured.ID);
					}
					dataVideoHeadlines.setContentType(resultFeatured.ContentType);
					dataVideoHeadlines.setTitle(resultFeatured.Title);
					dataVideoHeadlines.setImageURL(resultFeatured.ImageURL);
					dataVideoHeadlines.setThumbURL(resultFeatured.ThumbURL);
					dataVideoHeadlines.setLink(resultFeatured.Link);
					dataVideoHeadlines.setViewCount(resultFeatured.ViewCount);
					dataVideoHeadlines.setCommentCount(resultFeatured.CommentCount);
					dataVideoHeadlines.setPositiveVoteCount(resultFeatured.PositiveVoteCount);
					dataVideoHeadlines.setNegativeVoteCount(resultFeatured.NegativeVoteCount);
					dataArray_VideoHomePage.add(dataVideoHeadlines);
				}
			}
			else if (i==2) 
			{
				Gson gson = new Gson();
				SearchResponseCategories responseCategories = gson.fromJson(aBuffer, SearchResponseCategories.class);
				List<ResultCategories> resultsCategories = responseCategories.root;
				for (ResultCategories resultCategories : resultsCategories) 
				{
					dataVideoCategories=new DataModelVideoCategories();
					dataVideoCategories.setID(resultCategories.ID);
					dataVideoCategories.setName(resultCategories.Name);
					dataVideoCategories.setSortOrder(resultCategories.SortOrder);
					dataVideoCategories.setFeatured(resultCategories.Featured);
					dataVideoCategories.setWebsite(resultCategories.Website);
					dataArray_VideoCategories.add(dataVideoCategories);
				}
			}
			else if (i==3) 
			{
				Gson gson = new Gson();
				SearchResponseWeather responseWeather = gson.fromJson(aBuffer, SearchResponseWeather.class);
				List<ResultWeather> resultsWeather = responseWeather.root;
				for (ResultWeather resultWeather : resultsWeather) 
				{
					parseData=resultWeather.Date.split(" ");
				    Video.weatherDate.setText(parseData[0]);
				    Video.weatherInfo.setText(resultWeather.City.substring(0, 1).toUpperCase()+resultWeather.City.substring(1, resultWeather.City.length()).toLowerCase()+" "+resultWeather.Temperature.substring(0, resultWeather.Temperature.length()-1)+" / "+resultWeather.FeelsLike.substring(0, resultWeather.FeelsLike.length()-1)+" C\u00B0  ");
				    int resID = this.context.getResources().getIdentifier(resultWeather.IconName , "drawable", this.context.getPackageName());
				    Video.weatherImage.setImageResource(resID);
				}
			}
		}
		String[] urlAddress=null;
		FeaturedLayout.removeAllViews();
		for (i = 0; i < dataArray_VideoHomePage.size(); i++)
		{
			VideoItems_view=LayInflater.inflate(R.layout.video_featured_item,FeaturedLayout,false);
			((TextView)VideoItems_view.findViewById(R.id.featuredText0)).setText(dataArray_VideoHomePage.get(i).getTitle());
			VideoImageLoad=new DataModelVideoImageLoad();
			VideoImageLoad.setItemLayout((LinearLayout)VideoItems_view.findViewById(R.id.featuredLayout0));
			VideoImageLoad.setImageURL(dataArray_VideoHomePage.get(i).getImageURL());
			VideoImageLoad.setNewsImage((ImageView)VideoItems_view.findViewById(R.id.featuredImage0));
			VideoImageLoad.setPlayIcon((ImageView)VideoItems_view.findViewById(R.id.playIcon0)); 
			Video.Array_FeaturedVideo_Load.add(VideoImageLoad);
			((LinearLayout)VideoItems_view.findViewById(R.id.featuredClick0)).setOnClickListener(new ClickListeneNewsItem(dataArray_VideoHomePage.get(i).getContentType(), dataArray_VideoHomePage.get(i).getID(), dataArray_VideoHomePage.get(i).getLink(),TVname));
			urlAddress=dataArray_VideoHomePage.get(i).getImageURL().split("/");
			if (i<Math.ceil(DpHeight/152.0)) 
			{
				if (!(new File(DirName+urlAddress[urlAddress.length-1])).exists())
				{
					(new DownloadImageTaskVideo((ImageView)VideoItems_view.findViewById(R.id.featuredImage0), (ImageView)VideoItems_view.findViewById(R.id.playIcon0), urlAddress[urlAddress.length-1], DirName,Video.Array_FeaturedVideo_Load.get(i).getIsShown(),true)).execute(dataArray_VideoHomePage.get(i).getImageURL());
				}
				else
				{
					((ImageView)VideoItems_view.findViewById(R.id.featuredImage0)).setImageURI(Uri.fromFile(new File(DirName+urlAddress[urlAddress.length-1])));
					((ImageView)VideoItems_view.findViewById(R.id.playIcon0)).setVisibility(View.VISIBLE);
					Video.Array_FeaturedVideo_Load.get(i).setIsShown(true);
				}
			}
			FeaturedLayout.addView(VideoItems_view); 
		}
		CategoryLayout.removeAllViews();
		for (i = 0; i < dataArray_VideoCategories.size(); i++)
		{
			VideoItems_view=LayInflater.inflate(R.layout.video_categories_item,CategoryLayout,false);
			((TextView)VideoItems_view.findViewById(R.id.categoriesText0)).setText(dataArray_VideoCategories.get(i).getName());
			if (i%2==0) 
			{
				((LinearLayout)VideoItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_white);
			}
			else
			{
				((LinearLayout)VideoItems_view.findViewById(R.id.rl)).setBackgroundResource(R.color.listview_selector_grey);
			} 
			    ((LinearLayout)VideoItems_view.findViewById(R.id.categoriesLayout0)).setOnClickListener(new ClickListenerCategory(dataArray_VideoCategories.get(i).getName(), dataArray_VideoCategories.get(i).getID(), TVname));
				CategoryLayout.addView(VideoItems_view); 
		}
	}
	
	@Override
    protected void onPreExecute() 
	{
           Dialog = new ProgressDialog(this.contextDialog);
           Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           Dialog.setMessage("Yükleniyor...");
           Dialog.setCancelable(false);
           Dialog.show();
    }
	
	@Override
	protected String doInBackground(String... arg0) 
	{
		File dir = new File(this.DirName);
		if (dir.isDirectory()) 
		{
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) 
	        {
	            new File(dir, children[i]).delete();
	        }
	    }
		String urlAdress=this.pathName;
		String jString = null;
		for (int i = 0; i < 4; i++) 
		{
			if (i==0) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_VatanTVHeadlines";
				this.fileName=selectFileName(i);
			}
			else if (i==1) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_VatanTVHomePage";
				this.fileName=selectFileName(i);
			}
			else if (i==2) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_VatanTVCategories";
				this.fileName=selectFileName(i);
			}
			else if (i==3) 
			{
				urlAdress="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_WeatherCurrent&LocationID=290";
				this.fileName=selectFileName(i);
			}
			URL url;
			try 
			{
				File root=new File("/sdcard/Vatan");
				root.mkdir();
				File directory=new File(this.DirName);
				directory.mkdir();
				url = new URL(urlAdress);
				URLConnection ucon = url.openConnection();			
				InputStream is = ucon.getInputStream();			
				BufferedInputStream bis = new BufferedInputStream(is);			
				ByteArrayBuffer baf = new ByteArrayBuffer(50);			
				int current = 0;
				while ((current = bis.read()) != -1) 
				{				
					baf.append((byte) current);
				}			
				jString = new String(baf.toByteArray());
				try 
				{  //yazmaya yarıyor
					File myFile = new File(this.DirName+this.fileName+".txt");
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					myOutWriter.append(jString);
					myOutWriter.close();
					fOut.close();
				} 
				catch (Exception e) {
				}
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "hata1";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "hata1";
			}
		}
		File noMediaFile = new File(this.DirName+".nomedia");  //resim dosyalarının galeride gözükmemesi için, ilgili dizinin içine bu dosyayı ekliyoruz.
		File sizeFile= new File(this.DirName+"size.txt");
		try 
		{
			noMediaFile.createNewFile();
			sizeFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(sizeFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(Integer.toString(dataCountHeadlines));
			myOutWriter.close();
			fOut.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jString;
	}
	
	protected void onPostExecute(String result) 
	{ 
	   readData();
	   Video.backgroungLayout.setVisibility(View.VISIBLE);
	   Dialog.dismiss();
       Dialog = null;
    }
	
	class ClickListeneNewsItem implements OnClickListener
	{
		private String ContentType;
		private String Id;
		private String Link;
		private String TVname;
		
		public ClickListeneNewsItem(String contentType, String id, String link, String tvName) 
		{
			// TODO Auto-generated constructor stubi
			this.ContentType=contentType;
			this.Id=id;
			this.Link=link;
			this.TVname=tvName;
		}

		@Override
		public void onClick(View v) 
		{
			Log.e("click","click");
			Log.e("contentType=",this.ContentType);
			Log.e("id=",this.Id);
			appMap.RunActivity(this.ContentType, this.TVname, this.Id, this.Link);
		}
	}
	
	class ClickListenerCategory implements OnClickListener
	{
		private String CategoryName;
		private String CategoryID;
		private String ActivityName;
		
		public ClickListenerCategory(String categoryName, String categoryID, String activityName) 
		{
			// TODO Auto-generated constructor stubi
			this.CategoryName=categoryName;
			this.CategoryID=categoryID;
			this.ActivityName=activityName;
		}

		@Override
		public void onClick(View v) 
		{
			appMap.RunActivity("VideoCategory", this.CategoryName, "1", this.CategoryID);
		}
	}
}
