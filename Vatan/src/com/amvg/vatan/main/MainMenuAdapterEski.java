package com.amvg.vatan.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainMenuAdapterEski extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private final String[][] realValues;
	private int bannerK;
	private boolean haveBanner;
	private Context ContextDialog;
 
	private View bannerView = null;
	
	public MainMenuAdapterEski(Context context, String[] values, String[][] realValues, Context contextDialog) {
		super(context, R.layout.activity_main_menu, values);
		this.context = context;
		this.values = values;
		this.ContextDialog=contextDialog;
		bannerK=0;
		haveBanner=false;
		this.realValues=realValues;
	}
	
	private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        WebView webView;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder = new ViewHolder();
		
		
//		if ((AppMap.DownloadBannerData.bannerEnabled.equals("true") || haveBanner) && position==0) 
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
////			holder.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
////			holder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				//webView.loadUrl(HTML);
//				holder.webView.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}else
//			{
//				holder.webView.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//			bannerK=1;
//			//position
//		}
//		if ((Home.BannerEnabled || haveBanner) && position==0)
//		{
//			haveBanner=true;
//			
//			if(bannerView == null)
//			{
//				bannerView = inflater.inflate(R.layout.banner_item, null);
//			}
//			
//			convertView = bannerView;
//			
//			bannerK=1;
//		}
//		else
//		{
		
		
		convertView = inflater.inflate(R.layout.activity_main_menu, null);
		
		holder.txtTitle = (TextView) convertView.findViewById(R.id.empty);
        holder.imageView = (ImageView) convertView.findViewById(R.id.logo);
        ((TextView)convertView.findViewById(R.id.controller)).setText(realValues[position-bannerK][0]);
        ((TextView)convertView.findViewById(R.id.title)).setText(realValues[position-bannerK][2]);
        ((TextView)convertView.findViewById(R.id.CID)).setText(realValues[position-bannerK][3]);
        //holder.txtTitle.set
        //holder.txtTitle.setX(13);
        //holder.txtTitle.setY(13);
        if ((position)%2==0) {
        	convertView.setBackgroundResource(R.color.listview_selector_grey);
		}else{
			convertView.setBackgroundResource(R.color.listview_selector_white);
		}
        
        
        holder.txtTitle.setText(values[position]);
        if (values[position].equals("Ana Sayfa")) {
        	holder.imageView.setImageResource(R.color.home_selector);
		}else if (values[position].equals("Son Dakika")) {
        	holder.imageView.setImageResource(R.color.breaking_news_selector);
		}else if (values[position].equals("Yazarlar")) {
        	holder.imageView.setImageResource(R.color.columnists_selector);
		}else if (values[position].equals("Siyaset")) {
        	holder.imageView.setImageResource(R.color.politics_selector);
		}else if (values[position].equals("Ekonomi")) {
        	holder.imageView.setImageResource(R.color.economics_selector);
		}else if (values[position].equals("Dünya")) {
        	holder.imageView.setImageResource(R.color.world_selector);
		}else if (values[position].equals("Gündem")) {
        	holder.imageView.setImageResource(R.color.journal_selector);
		}else if (values[position].equals("Spor")) {
        	holder.imageView.setImageResource(R.color.sports_selector);
		}else if (values[position].equals("Magazin")) {
        	holder.imageView.setImageResource(R.color.magazine_selector);
		}else if (values[position].equals("Tüm Kategoriler")) {
        	holder.imageView.setImageResource(R.color.news_categories_selector);
		}else if (values[position].equals("Galeri")) {
        	holder.imageView.setImageResource(R.color.gallery_selector);
		}else if (values[position].equals("Video")) {
        	holder.imageView.setImageResource(R.color.video_selector);
		}else if (values[position].equals("Hava Durumu")) {
        	holder.imageView.setImageResource(R.color.weather_selector);
		}else if (values[position].equals("Astroloji")) {
        	holder.imageView.setImageResource(R.color.horoscopes_selector);
		}else if (values[position].equals("Yaşam")) {
        	holder.imageView.setImageResource(R.color.life_selector);
		}else 
			holder.imageView.setImageResource(R.drawable.ic_launcher);
//	}
		convertView.setTag(holder);
		return convertView;
	}
	
}
