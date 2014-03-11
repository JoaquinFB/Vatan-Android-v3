package com.amvg.vatan.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;
import android.widget.AdapterView.OnItemClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.annotations.SerializedName;



@SuppressLint("NewApi")
public class MainMenu extends Activity {
	static public String[][] values;
	@SuppressLint("NewApi")
	ListView listView;
	private boolean[] itemToggled;
	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		mGaInstance = GoogleAnalytics.getInstance(this);
		mGaTracker = mGaInstance.getTracker("UA-15581378-14");
		
		final AppMap appmap=new AppMap(getApplicationContext(),MainMenu.this);
		ImageView logo=(ImageView)findViewById(R.id.logoImage);
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				appmap.RunActivity("Home", "", "","");
				overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
			}
		});
//		Log.e("Deneme Liest","hata1");
		DownloadData download=new DownloadData();
		String parseParam[]={"http://m2.milliyet.com.tr/AppConfigs/Vatan-Android-v2/MainMenu.json","Controller","Ref","Title","CID"};
		download.execute(parseParam);
		listView=(ListView)findViewById(R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		    {
		    	if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
		    	{
					position=position-1; //banner varsa position'ı 1 kaydırıyoruz.
				}
		    	
		        appmap.RunActivity(((TextView)view.findViewById(R.id.controller)).getText().toString(), ((TextView)view.findViewById(R.id.title)).getText().toString(),"",((TextView)view.findViewById(R.id.CID)).getText().toString());
		        if (((TextView)view.findViewById(R.id.controller)).getText().toString().equals("Home")) 
		        {
		        	overridePendingTransition(R.anim.animated_activity_slide_left_in, R.anim.animated_activity_slide_right_out);
				}
		        
		    }
		});
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    // Send a screen view when the Activity is displayed to the user.
	    mGaTracker.sendView("/MainMenu"); //sayma kodu
	  }
	
	@Override
	protected void onStop()
	{
		super.onStop();
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
	
	public class SearchResponse {
		    public List<Result> root;
	}
	
	public class Result {
		    @SerializedName("Controller")
		    public String Controller;
		
		    @SerializedName("Ref")
		    public String Ref;
		
		    @SerializedName("Title")
		    public String Title;
		
		    @SerializedName("CID")
		    public String CID;
	}

	
	public class DownloadData extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... parseParam) {
			String jString = null;
			URL url;
			//InputStream is = null;
			
			
			try {
				
				url = new URL(parseParam[0]);
				
//				System.setProperty("http.keepAlive", "false");
				
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				
//				urlConnection.setUseCaches(false); 
				//urlConnection.setRequestProperty("User-Agent", useragent);
//				urlConnection.setConnectTimeout(30000);
//				urlConnection.setDoOutput(true); 
//				urlConnection.setDoInput(true); 
				//consumer.sign(conn);
				
//				URLConnection ucon = url.openConnection();
//				ucon.setDoInput(true);
				
				
				InputStream is = urlConnection.getInputStream();
				
				BufferedInputStream bis = new BufferedInputStream(is);
//				is.close();
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {				
					baf.append((byte) current);
				}
				jString = new String(baf.toByteArray());
				
				
//				HttpClient client = new DefaultHttpClient();
//                HttpGet get = new HttpGet(parseParam[0]);
//                HttpResponse responseGet = client.execute(get);  
//                Log.e("DenemeList","HttpResponse responseGet = client.execute(get);");
//                HttpEntity resEntityGet = responseGet.getEntity();  
//                Log.e("DenemeList","HttpEntity resEntityGet = responseGet.getEntity();");
//                jString = EntityUtils.toString(resEntityGet);
//                Log.e("DenemeList","jString = EntityUtils.toString(resEntityGet);");
//                
//                jString = jString.substring(3, jString.length());
//                jString=jString.replace("Ã¼", "ü");
                
				parseParam[0]=jString;			
				Gson gson = new Gson();
				SearchResponse response = gson.fromJson(jString, SearchResponse.class);
				//Toast.makeText(this, response.query, Toast.LENGTH_SHORT).show();
				List<Result> results = response.root;
				values = new String[results.size()][parseParam.length-1];
				int i=0;
				for (Result result : results) {
						values[i][0]=result.Controller;
						values[i][1]=result.Ref;
						values[i][2]=result.Title;
						values[i][3]=result.CID;
						i++;
				}
				return Integer.toString(i);
					
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				return "hata1";
			} catch (IOException e) {
				e.printStackTrace();
				return "hata2";
			}
	}
		protected void onPostExecute(String result) { 
	         /*int size=0;
	         if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
	         {
				size=Integer.parseInt(result)+1;
			 }
	         else
	         {
	        	 size=Integer.parseInt(result);
	         }
	         String[] abstractVals=new String[size];
	         String[] vals = new String[Integer.parseInt(result)];
	         for (int i = 0; i < Integer.parseInt(result); i++) {
				vals[i]=values[i][2];
			}*/
	         int size=0;
	         String[] vals;
	         if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) 
	         {
	        	 size=Integer.parseInt(result)+1;
	        	 vals = new String[size];
	        	 vals[0]="";
	        	 for (int i = 0; i < size-1; i++) 
	        	 {
					vals[i+1]=values[i][2];
				 }
			 }
	         else
	         {
	        	 size=Integer.parseInt(result);
	        	 vals=new String[size];
	        	 for (int i = 0; i < vals.length; i++) 
	        	 {
					vals[i]=values[i][2];
				 }
	         }
	         
	         MainMenuAdapterEski adapter=new MainMenuAdapterEski(getApplicationContext(), vals, values, MainMenu.this);
	         listView.setAdapter(adapter);
	         itemToggled = new boolean[vals.length];
	         Arrays.fill(itemToggled, false);
	     }
	}
}
