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
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;
import android.view.Window;
import android.widget.ListView;


public class BreakingNewsAccessData extends AsyncTask<String, String, String>{

	private ListView listview;
	private Context context;
	private Context contextDialog;
	private String[] fileNames;
	private String[] id;
	private String[][] values;
	private String[] outputParameters;
	private String[] category;
	private boolean[] itemToggled;
	private int updateTime;
	private ProgressDialog Dialog;
	private String FilePath;
	
	public BreakingNewsAccessData(Context context, ListView lv, Context contextDialog){
		this.updateTime=600;
		this.context=context;
		this.listview=lv;
		this.outputParameters=new String[5];
		this.outputParameters[0]="ContentType";
		this.outputParameters[1]="ID";
		this.outputParameters[2]="PublishTime";
		this.outputParameters[3]="Title";
		this.outputParameters[4]="ImageURL";
		this.fileNames=new String[7];
		this.fileNames[0]="BreakingNewsTop5";
		this.fileNames[1]="BreakingNewsSiyasetTop5";
		this.fileNames[2]="BreakingNewsEkonomiTop5";
		this.fileNames[3]="BreakingNewsSporTop5";
		this.fileNames[4]="BreakingNewsDunyaTop5";
		this.fileNames[5]="BreakingNewsGundemTop5";
		this.fileNames[6]="BreakingNewsMagazinTop5";
		this.id=new String[7];
		this.id[0]="0";
		this.id[1]="12"; 
		this.id[2]="11";
		this.id[3]="14";
		this.id[4]="19";
		this.id[5]="15";
		this.id[6]="17";
		this.category=new String[6];
		this.category[0]="Siyaset";
		this.category[1]="Ekonomi";
		this.category[2]="Spor";
		this.category[3]="Dünya";
		this.category[4]="Gündem";
		this.category[5]="Magazin";
		this.contextDialog=contextDialog;
		this.FilePath="/sdcard/Vatan/breakingnews/breakingnews.txt";
		
		
	}
	
	private boolean haveFile(String fileName) throws IOException{
		
		File file = new File(fileName);
		if (file.exists()) {
		  return true;
		}else{
			return false;
		}
	}
	
	private boolean isUpdateTimeOK(java.sql.Time lastTime, Calendar fileUpdateDate){
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int nowSeconds=0;
		int fileSeconds=0;
		
		if (fileUpdateDate.get(Calendar.YEAR)==today.year && fileUpdateDate.get(Calendar.MONTH)==today.month && fileUpdateDate.get(Calendar.DAY_OF_MONTH)==today.monthDay) {
			nowSeconds=today.hour*3600+today.minute*60+today.second;
			fileSeconds=lastTime.getHours()*3600+lastTime.getMinutes()*60+lastTime.getSeconds();
			if (nowSeconds-fileSeconds<this.updateTime) {
				return true;
			}
			else{
				return false;
			}
		}else{
			return false;
		}
		
	}
	
	public boolean areFilesOK() throws IOException, ParseException{
		
			if (!haveFile(this.FilePath)) 
			{
				return false;
			}
			else
			{
				File file = new File(this.FilePath);
				java.sql.Time lastTime=new java.sql.Time(file.lastModified());
				Date lastModDate = new Date(file.lastModified());
				final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		         final Calendar c = Calendar.getInstance();
		         c.setTime(df.parse(lastModDate.toString()));
		         if (!isUpdateTimeOK(lastTime, c)) {
					return false;
				}
			}
		
		
		return true;
	}
	
	private void fillListView(int dataSayisi, String[][] values, int breakingNewsCount){
		if (Home.BannerEnabled) {
			dataSayisi=dataSayisi+1;
		} 
		String[] array= new String[dataSayisi+1];
		BreakingNewsAdapter adapter=new BreakingNewsAdapter(this.context, array, values,breakingNewsCount,contextDialog);
		
		this.listview.setAdapter(adapter);
        itemToggled = new boolean[array.length];
        Arrays.fill(itemToggled, false);
	}
	
	public static void DownloadFile(String imageURL, String fileName) throws IOException {
		URL url = new URL(imageURL);
		File director=new File("/sdcard/Vatan/breakingnews");
		director.mkdir();
		
		File file = new File("/sdcard/Vatan/breakingnews/"+fileName);
		URLConnection ucon = url.openConnection();
		InputStream is = ucon.getInputStream();
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1)
				baf.append((byte) current);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	
	
	public String readData() throws JSONException, IOException{
		String aBuffer = "";
		String[] urlAdress=null;
		int breakingNewsCount=0;
		int k=0;
		values = new String[26][5];
		values[0][0]="Son Dakika";
		k++;
		
			
			aBuffer = "";
			try { //okumaya yar�yor
				File myFile = new File("/sdcard/Vatan/breakingnews/breakingnews.txt");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
				String aDataRow = "";
				
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}
				myReader.close();
				
				
			} catch (Exception e) {
			}
			if (!aBuffer.equals("{\"root\" : null}\n"))
			{
				Gson gson = new Gson();
				SearchResponse response = gson.fromJson(aBuffer, SearchResponse.class);
				List<Result> results = response.root;
				
				
				for (Result result : results) {
					//values[0][0][0]=result.ImageURL;
					
						values[k][0]=result.ContentType;
						values[k][1]=result.ID;
						values[k][2]=result.PublishTime;
						values[k][3]=result.Title;
							urlAdress=result.ImageURL.split("/");
							values[k][4]=urlAdress[urlAdress.length-1];
					k++;
					
				}
				
			}
		
		fillListView(k, values,breakingNewsCount);
		return aBuffer;
	}
	
	
public class SearchResponse {
	    public List<Result> root;
}

public class Result {
	    @SerializedName("ContentType")
	    public String ContentType;
	
	    @SerializedName("ID")
	    public String ID;
	
	    @SerializedName("PublishTime")
	    public String PublishTime;
	    
	    @SerializedName("Title")
	    public String Title;
	    
	    @SerializedName("ImageURL")
	    public String ImageURL;
	
}
	
	
	@Override
    protected void onPreExecute() {
           Dialog = new ProgressDialog(this.contextDialog);
           Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           Dialog.setMessage("Yükleniyor...Lütfen Bekleyiniz...");
           Dialog.setCancelable(false);
           Dialog.show();
   
    }
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		File dir = new File("/sdcard/Vatan/breakingnews");
		if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(dir, children[i]).delete();
	        }
	    }
	    
	   
	    
		
		String urlAdress = null;
		URL url;
		
			urlAdress="http://www13.gazetevatan.com/mobileapi/mobile_breakingnews.asp";
			String jString = null;
			try {
				
				File root=new File("/sdcard/Vatan");
				root.mkdir();
				File directory=new File("/sdcard/Vatan/breakingnews");
				directory.mkdir();
				
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
				
//				InputStream is = new URL(urlAdress).openStream();
//				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("ISO-8859-1"))); //ISO-8859-1
//				jString = Global.readAll(rd);
				
				Gson gson = new Gson();
				SearchResponse response = gson.fromJson(jString, SearchResponse.class);
				List<Result> results = response.root;
				
					String[] urlAdress1=null;
					for (Result result : results) {
						//values[0][0][0]=result.ImageURL;
						if (!result.ImageURL.equals("")) {
							urlAdress1=result.ImageURL.split("/");
							DownloadFile(Global.resizeImageURL(result.ImageURL, "133", "0"), urlAdress1[urlAdress1.length-1]);
						}
						
					}
				
				
				
				//if (i==0) {
					//parseForImageName(jString);
				//}
				try {  //yazmaya yar�yor
					File noMediaFile = new File("/sdcard/Vatan/breakingnews/.nomedia");  //resim dosyalarının galeride gözükmemesi için, ilgili dizinin içine bu dosyayı ekliyoruz.
					noMediaFile.createNewFile();
					File myFile = new File("/sdcard/Vatan/breakingnews/breakingnews.txt");
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					myOutWriter.append(jString);
					myOutWriter.close();
					fOut.close();
					
				} catch (Exception e) {
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
		
		
		
		return null;
	}
	protected void onPostExecute(String result) { 
     
       try {
		readData();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       Dialog.dismiss();
       Dialog = null;
    }


}
