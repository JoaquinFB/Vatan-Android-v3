package com.amvg.vatan.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class MainMenuAccessData extends AsyncTask<String, String, String> {
	
	private Context context;
	private String className;
	private MainMenuDataModel model;
	private ListView listView_menu;
	public static ArrayList<MainMenuDataModel> array_list;
	
	MainMenuAccessData(Context context, String className, ListView listView_menu)
	{
		this.listView_menu=listView_menu;
		this.context=context;
		this.className=className;
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Log.e("protected String doInBackground(String... params) {","MainMenuAccessData");
		String jString = null;
		URL url;
		
		
		try {
			array_list=new ArrayList<MainMenuDataModel>();
			url = new URL("http://m2.milliyet.com.tr/AppConfigs/Vatan-Android-v3.0/MainMenu.json");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			InputStream is = urlConnection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {				
				baf.append((byte) current);
			}
			jString = new String(baf.toByteArray());
			
//			jString= "{\"root\":["+
//					 "{\"Controller\":\"Home\",\"Ref\":\"Home\",\"Title\":\"Ana Sayfa\",\"CID\":null},"+
//					 "{\"Controller\":\"BreakingNews\",\"Ref\":\"BreakingNews\",\"Title\":\"Son Dakika\",\"CID\":null},"+
//					 "{\"Controller\":\"Columnists\",\"Ref\":\"Columnists\",\"Title\":\"Yazarlar\",\"CID\":null},"+
//					 "{\"Controller\":\"NewsCategory\",\"Ref\":\"Journal\",\"Title\":\"Gündem\",\"CID\":1},"+
//					 "{\"Controller\":\"NewsCategory\",\"Ref\":\"Politics\",\"Title\":\"Siyaset\",\"CID\":9},"+
//					 "{\"Controller\":\"NewsCategory\",\"Ref\":\"World\",\"Title\":\"Dünya\",\"CID\":30},"+
//					 "{\"Controller\":\"Sampiyon\",\"Ref\":\"Sports\",\"Title\":\"\",\"CID\":null},"+
//					 "{\"Controller\":\"NewsCategory\",\"Ref\":\"Economics\",\"Title\":\"Ekonomi\",\"CID\":2},"+
//					 "{\"Controller\":\"NewsCategory\",\"Ref\":\"Life\",\"Title\":\"Yaşam\",\"CID\":7},"+
//					 "{\"Controller\":\"NewsCategory\",\"Ref\":\"Magazine\",\"Title\":\"Magazin\",\"CID\":8},"+
//					 "{\"Controller\":\"NewsCategories\",\"Ref\":\"NewsCategories\",\"Title\":\"Tüm Kategoriler\",\"CID\":null},"+
//					 "{\"Controller\":\"Gallery\",\"Ref\":\"Gallery\",\"Title\":\"Galeri\",\"CID\":null},"+
//					 "{\"Controller\":\"VatanTV\",\"Ref\":\"TV\",\"Title\":\"\",\"CID\":null},"+
//					 "{\"Controller\":\"Weather\",\"Ref\":\"Weather\",\"Title\":\"Hava Durumu\",\"CID\":null},"+
//					 "{\"Controller\":\"Horoscopes\",\"Ref\":\"Horoscopes\",\"Title\":\"Astroloji\",\"CID\":null}"+
//					 "]}";
			
			params[0]=jString;			
			Gson gson = new Gson();
			SearchResponse response = gson.fromJson(jString, SearchResponse.class);
			List<Result> results = response.root;
			for (Result result : results) {
				
				model = new MainMenuDataModel();//burada model yap�s�na gelen datay� set yap�yoruz
				
				model.setController(result.Controller);  
				model.setRef(result.Ref);
				model.setTitle(result.Title);
				model.setCID(result.CID);
				model.setIcon(result.Icon);
				array_list.add(model);
			}
			model = null;
			return "a";
				
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "hata1";
		} catch (IOException e) {
			e.printStackTrace();
			return "hata2";
		}
	}
	protected void onPostExecute(String result)
	{
		this.listView_menu.setAdapter(new MainMenuAdapter(context, array_list));
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
	    
	    @SerializedName("Icon")
	    public String Icon;
	}
	

}
