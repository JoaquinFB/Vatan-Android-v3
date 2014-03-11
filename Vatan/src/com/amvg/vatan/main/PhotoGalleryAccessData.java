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
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;

import com.amvg.vatan.main.GalleryAccessData.ResultHeadlinesFeatured;
import com.amvg.vatan.main.GalleryAccessData.SearchResponseHeadlinesFeatured;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

public class PhotoGalleryAccessData extends AsyncTask<String, String, String>{
	static public String[][] values;
	private String pathName;
	private String fileName;
	private Context context;
	private int dataSayisi;
	private String dirName;
	private Global global;
	private TestFragmentAdapterPhotoGallery mAdapter;
	private ViewPager mPager;
	private String pathNameCategoryTitle;
	//public static Bitmap[] bitmap;
	public static int[] bitmapWidth;
	public static int[] bitmapHeight; 
	public static int[] textLength;
	public static String[] text;
	private String categoryNameText;
	private String titleText;
	private String jString2;
	private String fileNameTitle;
	private DownloadData downloadImage;
	private Context contextDialog;
	private ProgressDialog Dialog;
	private Boolean isNewsGallery; // hatırla hatırla hatırla
	
	public PhotoGalleryAccessData(Context context, String galleryID, TestFragmentAdapterPhotoGallery mAdapter, ViewPager mPager, Context contextDialog){
		this.global=new Global();
		this.context=context;
//		this.listview=lv;
		this.dirName="/sdcard/Vatan/photogallery";
		
		this.fileName=this.dirName+"/"+galleryID+".txt";
		this.fileNameTitle=this.dirName+"/"+galleryID+"content.txt";
		this.mAdapter=mAdapter;
		this.mPager=mPager;
		
		this.contextDialog=contextDialog;
		String [] idParse;
		if (galleryID.contains("#")) 
		{
			isNewsGallery=true;
			idParse=galleryID.split("-");
			galleryID=idParse[0];
			this.pathName="http://mw.milliyet.com.tr/ashx/Milliyet.ashx?aType=MobileAPI_NewsArticlePhotos&ArticleID="+galleryID;  // vatan uygulamasında NewsArticlePhotos yapısı olmayacak.
		}
		else
		{
			isNewsGallery=false;
			this.pathName="http://www13.gazetevatan.com/mobileapi/mobile_Photos.asp?PhotoGalleryID="+galleryID;
			this.pathNameCategoryTitle="http://www13.gazetevatan.com/mobileapi/mobile_PhotoGallery.asp?PhotoGalleryID="+galleryID;
		}
	}
	
	public class SearchResponse {
	    public List<Result> root;
	}

	public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	
	    @SerializedName("Description")
	    public String Description;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	
	}
	
	public class SearchResponseCategoryTitle {
	    public List<ResultCategoryTitle> root;
	}

	public class ResultCategoryTitle {
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
		int k=0;
		String[] urlAdress;
		Gson gson = new Gson();
		SearchResponse response = gson.fromJson(JsonString, SearchResponse.class);
		List<Result> results = response.root;
		dataSayisi=results.size()+1;
		values=new String[dataSayisi][4];
		//values[0][0]=this.categoryName;
		for (Result result : results) {
			values[k][0]=result.ContentType;
			values[k][1]=result.ID;
			values[k][2]=Global.Replace(result.Description);
			urlAdress=result.ImageURL.split("/");
		    values[k][3]=urlAdress[urlAdress.length-1];
//			values[k][3]=result.ImageURL;
		    k++;	
		}
		//bitmap = new Bitmap[k];
		bitmapWidth=new int[k];
		bitmapHeight=new int[k];
		textLength=new int[k];
		text=new String[k];
		if (!this.isNewsGallery) {
			PhotoGallery.categoryName.setText("Galeri - "+categoryNameText);
			PhotoGallery.galleryTitle.setText(titleText);
		}
		
		
		TestFragmentAdapterPhotoGallery.setContent(values, this.context, k, contextDialog);
		mPager.setAdapter(mAdapter);
		return values;
		
	}
	private String[][] parseDataTitle(String JsonString) throws JSONException{
		Gson gson2 = new Gson();
		SearchResponseCategoryTitle response2 = gson2.fromJson(JsonString, SearchResponseCategoryTitle.class);
		List<ResultCategoryTitle> results2 = response2.root;
		for (ResultCategoryTitle result2 : results2) 
		{
			
			try {
				categoryNameText=Global.Replace(result2.Category);
				titleText=Global.Replace(result2.Title);
//				PhotoGallery.categoryName.setText(result2.Category);
//				PhotoGallery.galleryTitle.setText(result2.Title);
			} catch (Exception e) {
				// TODO: handle exception
			}
				
//				urlAdress1=result.ImageURL.split("/");
//				Global.DownloadFile(result.ImageURL, urlAdress1[urlAdress1.length-1],this.dirName);
//				Log.e("DOWNLOADED",this.dirName);
//				Log.e("DOWNLOADED",urlAdress1[urlAdress1.length-1]);
		}
		
		PhotoGallery.categoryName.setText("Galeri - "+categoryNameText);
		PhotoGallery.galleryTitle.setText(titleText);
		
		return values;
		
	}
	
	@Override
    protected void onPreExecute() {
           Dialog = new ProgressDialog(this.contextDialog);
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
			File noMediaFile = new File("/sdcard/Vatan/photogallery/.nomedia");  //resim dosyalarının galeride gözükmemesi için, ilgili dizinin içine bu dosyayı ekliyoruz.
			noMediaFile.createNewFile();
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
			SearchResponseHeadlinesFeatured response = gson.fromJson(jString, SearchResponseHeadlinesFeatured.class);
			List<ResultHeadlinesFeatured> results = response.root;
			String[] urlAdress1=null;
			int dataCount=0;
			for (ResultHeadlinesFeatured result : results) 
			{
				if (!result.ImageURL.equals("")) 
				{
					if (dataCount==2) {
						break;
					}
					urlAdress1=result.ImageURL.split("/");
					Global.DownloadFile(result.ImageURL, urlAdress1[urlAdress1.length-1],this.dirName);
				}
				dataCount++;
			}
			
			jString2="";
			URL url2;
			if (!this.isNewsGallery) {
				url2 = new URL(pathNameCategoryTitle);
				URLConnection ucon2 = url2.openConnection();			
				InputStream is2 = ucon2.getInputStream();			
				BufferedInputStream bis2 = new BufferedInputStream(is2);			
				ByteArrayBuffer baf2 = new ByteArrayBuffer(50);			
				int current2 = 0;
				while ((current2 = bis2.read()) != -1) {				
					baf2.append((byte) current2);
				}			
				jString2 = new String(baf2.toByteArray());
			}
			
			
//			Gson gson2 = new Gson();
//			SearchResponseCategoryTitle response2 = gson2.fromJson(jString2, SearchResponseCategoryTitle.class);
//			List<ResultCategoryTitle> results2 = response2.root;
//			for (ResultCategoryTitle result2 : results2) 
//			{
//				
//				try {
//					categoryNameText=result2.Category;
//					titleText=result2.Title;
////					PhotoGallery.categoryName.setText(result2.Category);
////					PhotoGallery.galleryTitle.setText(result2.Title);
//				} catch (Exception e) {
//					// TODO: handle exception
//					Log.e("Hata nedir?",e.toString());
//				}
//					
////					urlAdress1=result.ImageURL.split("/");
////					Global.DownloadFile(result.ImageURL, urlAdress1[urlAdress1.length-1],this.dirName);
////					Log.e("DOWNLOADED",this.dirName);
////					Log.e("DOWNLOADED",urlAdress1[urlAdress1.length-1]);
//			}
			
		return jString;
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "hata1";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("hata1",e.toString());
			return "hata1";
		}
		
	}
	
	protected void onPostExecute(String result) { 
       Dialog.dismiss();
       Dialog = null;
       try {
    	   String[] in={result,this.dirName};
    	   PhotoGallery.stopThread=false;
    	   downloadImage=new DownloadData();
	    	downloadImage.execute(in);
	    	//downloadImage.cancel(true);
	    	
    	   this.global.writeFile(result, this.dirName, this.fileName); //farklı bir dosya adına kaydet. daha sonra parseData2 metodunda pars et ve gerekli değerleri ata.
	    	 parseData(this.global.readData(this.fileName));
	    	 
	    	 if (!this.isNewsGallery) {
	    		 this.global.writeFile(jString2, this.dirName, this.fileNameTitle);
		    	 parseDataTitle(this.global.readData(this.fileNameTitle));
			}
	    	 
	    	 
	    	 
			//writeFile(result);
			//fillData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	public class DownloadData extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... urlAddress) {
			
			Gson gson = new Gson();
			SearchResponseHeadlinesFeatured response = gson.fromJson(urlAddress[0], SearchResponseHeadlinesFeatured.class);
			List<ResultHeadlinesFeatured> results = response.root;
			String[] urlAdress1=null;
			int dataCount=0;
			for (ResultHeadlinesFeatured result : results) 
			{
				if (PhotoGallery.stopThread) {
					break;
				}
				if (!result.ImageURL.equals("")) 
				{
					urlAdress1=result.ImageURL.split("/");
					try {
						if (dataCount>1) {
							Global.DownloadFile(result.ImageURL, urlAdress1[urlAdress1.length-1],urlAddress[1]);
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//Log.e("DOWNLOADED",urlAdress[1]);
				}
				dataCount++;
			}
	    	return "hop";
	}
		protected void onPostExecute(String result) { 
//			Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/photogallery/"+urlAdress[urlAdress.length-1]);
//	    	Log.e("bitmapName","/sdcard/Milliyet/photogallery/"+urlAdress[urlAdress.length-1]);
//	    	
//	    	image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//	    	image.setImageBitmap(bitmap);
//	    	final float scale = TestFragmentPhotoGallery.context.getResources().getDisplayMetrics().density;
//	    	image.getLayoutParams().width=(int) (320 * scale + 0.5f);
//	    	image.setScaleType(ImageView.ScaleType.FIT_XY);
//			try {
//				global.writeFile(result, dirName, fileNa<zzzzzme);
//				parseAndShowData(global.readData(fileName));
//				Log.e("Dosya","yazma islemi basarili");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				Log.e("Dosya","yazma işlemi hatalı");
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	     }
	}
}
