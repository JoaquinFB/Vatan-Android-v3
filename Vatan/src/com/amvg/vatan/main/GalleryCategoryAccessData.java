package com.amvg.vatan.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class GalleryCategoryAccessData extends AsyncTask<String, String, String>{
	static public String[][] values;
	private String pathName;
	private String fileName;
	private ListView listview;
	private Context context;
	private boolean[] itemToggled;
	private int dataSayisi;
	private String dirName;
	private String categoryName;
	private Global global;
	private String isVideo;
	private ProgressDialog Dialog;
	private Context ContextDialog;
	
	public GalleryCategoryAccessData(Context context, ListView lv, Context contextDialog, String categoryName, String categoryID, String isVideo){
		this.global=new Global();
		this.context=context;
		this.listview=lv;
		this.dirName="/sdcard/Vatan/gallerycategory";
		this.pathName="http://www13.gazetevatan.com/mobileapi/mobile_PhotoGalleriesByCategory.asp?CategoryID="+categoryID;
		this.fileName=this.dirName+"/"+categoryName+".txt";
		this.categoryName=categoryName;
		this.isVideo=isVideo;
		this.ContextDialog=contextDialog;
		
	}
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	    
	    @SerializedName("CategoryID")
	    public String CategoryID;
	    
	    @SerializedName("Category")
	    public String Category;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	
	}
	public boolean isFileOK() throws IOException, ParseException{
		return this.global.isFileOK(this.fileName);
	}
	
	private String[][] parseData(String JsonString) throws JSONException{
		int k=1;
		String[] urlAdress;
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		dataSayisi=results.size()+1;
		values=new String[dataSayisi][6];
		values[0][0]=this.categoryName;
		for (Result result : results) {
			values[k][0]=result.ContentType;
			values[k][1]=result.ID;
			values[k][2]=result.CategoryID;
			values[k][3]= result.Category;
			values[k][4]=result.Title;
			urlAdress=result.ImageURL.split("/");
			values[k][5]=urlAdress[urlAdress.length-1];
		    
			k++;
		}
		
		return values;
	}
	
	private void fillListView(){
		if (AppMap.DownloadBannerData.bannerEnabled.equals("true")) {
			dataSayisi=dataSayisi+1;
		}
		String[] array= new String[dataSayisi];
		VideoCategoryAdapter adapter=new VideoCategoryAdapter(this.context,array, values, isVideo, ContextDialog);
		this.listview.setAdapter(adapter);
        itemToggled = new boolean[array.length];
        Arrays.fill(itemToggled, false);
	}
	
	public void fillData() throws IOException, ParseException, JSONException{
		parseData(this.global.readData(this.fileName));
		fillListView();
	}
	
	@Override
    protected void onPreExecute() {
		Home.homeMainLayout.setVisibility(View.GONE);
           Dialog = new ProgressDialog(this.ContextDialog);
           Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           Dialog.setMessage("Yükleniyor...");
           Dialog.setCancelable(false);
           Dialog.show();
   
    }
	
	@Override
	protected String doInBackground(String... arg0) {
		
		File dir = new File(this.dirName);
		if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(dir, children[i]).delete();
	        }
	    }
		
		String urlAdress=this.pathName;
		String jString = null;
		try {
			
			File root=new File("/sdcard/Vatan");
			root.mkdir();
			File directory=new File(this.dirName);
			directory.mkdir();
			URL url;
			url = new URL(urlAdress);
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
			SearchResponse response = gson.fromJson(jString, SearchResponse.class);
			List<Result> results = response.root;
			
			String[] urlAdress1=null;
			for (Result result : results) {
				//values[0][0][0]=result.ImageURL;
				if (!result.ImageURL.equals("")) {
					urlAdress1=result.ImageURL.split("/");
					Global.DownloadFile(Global.resizeImageURL(result.ImageURL, "133", "0"), urlAdress1[urlAdress1.length-1],this.dirName);
				}
			}
			
			
		return jString;
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
	
	protected void onPostExecute(String result) { 
       try {
    	   this.global.writeFile(result, this.dirName, this.fileName);
			//writeFile(result);
			fillData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       Dialog.dismiss();
       Dialog = null;
    }
}
