package com.amvg.vatan.main;

import java.util.Calendar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap; //adapter
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BreakingNewsAdapter extends ArrayAdapter<String> {
	private final Context context;
	private String[][] realValues;
	private TextView ID;
	private ViewHolder holder;
	private int bannerK;
	public boolean haveBanner;
	private Context ContextDialog;
	public int ValuesLength;
	public int year;
	
 
	public BreakingNewsAdapter(Context context, String[] values, String[][] realValues, int breakingNewsCount, Context contextDialog) {
		
		super(context, R.layout.news_items, values);
		this.context = context;
		this.realValues=realValues;
		this.holder=new ViewHolder();
		bannerK=0;
		haveBanner=false;
		this.ContextDialog=contextDialog;
		this.ValuesLength=values.length;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
	}
	
	private class ViewHolder {
        //ImageView imageView;
        TextView saatTitle;
        TextView haberTitle;
        WebView webView;
	}

   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//ViewHolder holder=new ViewHolder();
		
		
		if(position==0)
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
//			convertView.setBackgroundResource(R.color.listview_selector_grey);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.saatTitle.setText("Son Dakika");
		}
		else if ((Home.BannerEnabled || haveBanner) && position==1)
		{
			haveBanner=true;
			convertView=inflater.inflate(R.layout.banner_item, null);
			bannerK=1;
		}
		else if (position==ValuesLength-1)
		{
			convertView=inflater.inflate(R.layout.copyright_item, null);
			((TextView)convertView.findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Vatan Gazetesi");
		}
//		else if ((AppMap.DownloadBannerData.bannerEnabled.equals("true") || haveBanner) && position==1) 
//		{
//			haveBanner=true;
//			convertView=inflater.inflate(R.layout.banner,null);
//			holder.webView=(WebView)convertView.findViewById(R.id.webView);
//			holder.webView.setWebViewClient(new WebViewClient(){
//			    @Override
//			    public boolean shouldOverrideUrlLoading(WebView wView, String url)
//			    {
//			    	com.amvg.vatan.main.Global.parseURLrequest(url, context, ContextDialog,"");
//			        return true;
//			    }
//			});
//			holder.webView.getLayoutParams().height=(int) ((Integer.parseInt(AppMap.DownloadBannerData.bannerHeight)+2)*SplashScreen.scale);
//			holder.webView.getSettings().setJavaScriptEnabled(true);
//			holder.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//			holder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				holder.webView.loadDataWithBaseURL("file:///android_asset/", com.amvg.vatan.main.Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}
//			else
//			{
//				holder.webView.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//			bannerK=1;
//		}
		else
		{
			convertView = inflater.inflate(R.layout.news_items_with_image, null);
			holder.saatTitle = (TextView)convertView.findViewById(R.id.saatText);
			holder.haberTitle = (TextView) convertView.findViewById(R.id.haberText);
			holder.saatTitle.setText(realValues[position-bannerK][2]);
			holder.haberTitle.setText(realValues[position-bannerK][3]);
			ID=(TextView)convertView.findViewById(R.id.ID);
		    ID.setText(realValues[position-bannerK][1]);
		    ImageView jpgView = (ImageView)convertView.findViewById(R.id.newsImage);
		    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Vatan/breakingnews/"+realValues[position-bannerK][4]);
		    jpgView.setImageBitmap(bitmap);
		    ((TextView)convertView.findViewById(R.id.contentType)).setText(realValues[position-bannerK][0]);
		    if (position%2==1) {
	        	convertView.setBackgroundResource(R.color.listview_selector_white);
			}else{
				convertView.setBackgroundResource(R.color.listview_selector_grey);
			}
			
			convertView.setTag(holder);
		}
		
		
		
		
        
	
	return convertView;
	}
}
