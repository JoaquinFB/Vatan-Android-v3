package com.amvg.vatan.main;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class NewsCategoriesAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[][] values;
	private int bannerK;
	private boolean haveBanner;
	private Context ContextDialog;
	public int ValuesLength;
	public int year;
 
	public NewsCategoriesAdapter(Context context, String[][] values, String[] vals, Context contextDialog) {
		super(context, R.layout.activity_main_menu, vals);
		this.context = context;
		this.values = values;
		this.ContextDialog=contextDialog;
		bannerK=0;
		haveBanner=false;
		this.ValuesLength=values.length;
		Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
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
		if (position==0) 
		{
			convertView = inflater.inflate(R.layout.son_dakika_title, null);
			holder.txtTitle = (TextView)convertView.findViewById(R.id.titleSonDakikaText);
			holder.txtTitle.setText("Tüm Kategoriler");
		}
		else if ((Home.BannerEnabled || haveBanner) && position==1)
		{
			haveBanner=true;
			convertView=inflater.inflate(R.layout.banner_item, null);
			bannerK=1;
		}
		else if (position==ValuesLength-1+3)
		{
			convertView=inflater.inflate(R.layout.copyright_item, null);
			((TextView)convertView.findViewById(R.id.Copyrigth)).setText("Copyright \u00A9 "+Integer.toString(year)+" Vatan Gazetesi");
		}
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
//			holder.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//			holder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
//			if (AppMap.DownloadBannerData.bannerURL.equals("")) 
//			{
//				holder.webView.loadDataWithBaseURL("file:///android_asset/", Global.setHtmlText(true, AppMap.DownloadBannerData.bannerHTML), "text/html", "UTF-8", "");
//			}else
//			{
//				holder.webView.loadUrl(AppMap.DownloadBannerData.bannerURL);
//			}
//			bannerK=1;
//			//position
//		}
		else 
		{
		if (position-bannerK-1<7) {
			convertView = inflater.inflate(R.layout.news_categories_items, null);
			
			holder.txtTitle = (TextView) convertView.findViewById(R.id.empty);
	        holder.txtTitle.setText(values[position-bannerK-1][1]);
	        ((TextView) convertView.findViewById(R.id.CID)).setText(values[position-bannerK-1][0]);
	        holder.imageView = (ImageView) convertView.findViewById(R.id.logo);
	        if (values[position-bannerK-1][1].equals("Siyaset")) {
	        	holder.imageView.setImageResource(R.color.politics_selector);
			}else if (values[position-bannerK-1][1].equals("Ekonomi")) {
	        	holder.imageView.setImageResource(R.color.economics_selector);
			}else if (values[position-bannerK-1][1].equals("Dünya")) {
	        	holder.imageView.setImageResource(R.color.world_selector);
			}else if (values[position-bannerK-1][1].equals("Gündem")) {
	        	holder.imageView.setImageResource(R.color.journal_selector);
			}else if (values[position-bannerK-1][1].equals("Şampiy10")) {
	        	holder.imageView.setImageResource(R.color.sports_selector);
			}else if (values[position-bannerK-1][1].equals("Magazin")) { 
	        	holder.imageView.setImageResource(R.color.magazine_selector);
			}else if (values[position-bannerK-1][1].equals("Yaşam")) {
	        	holder.imageView.setImageResource(R.color.life_selector);
			}else if (values[position-bannerK-1][1].equals("Mola")) {
	        	holder.imageView.setImageResource(R.color.mola_selector);
			}
	        convertView.setTag(holder);
		}else{
			convertView = inflater.inflate(R.layout.activity_main_menu_without_icon, null);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.empty);
	        holder.txtTitle.setText(values[position-bannerK-1][1]);
	        ((TextView) convertView.findViewById(R.id.CID)).setText(values[position-bannerK-1][0]);
	        convertView.setTag(holder);
		}
		if (position%2==0) {
        	convertView.setBackgroundResource(R.color.listview_selector_grey);
		}else{
			convertView.setBackgroundResource(R.color.listview_selector_white);
		}
		}
        return convertView;
	}
}
