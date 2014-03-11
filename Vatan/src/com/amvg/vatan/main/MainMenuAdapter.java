package com.amvg.vatan.main;

import java.util.ArrayList;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private static ArrayList<MainMenuDataModel> searchArrayList;
	private Context Context;
	
	
	
	//CONSTRUCTOR
	 public MainMenuAdapter(Context context, ArrayList<MainMenuDataModel> results) {
		  searchArrayList = results;
		  mInflater = LayoutInflater.from(context);
		  this.Context=context;
		 }

	@Override
	public int getCount() {
		return searchArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		 ViewHolder holder;
			
//		 if (convertView == null) {
			   convertView = mInflater.inflate(R.layout.activity_main_menu, null);
			   holder = new ViewHolder();
			   holder.imageView=(ImageView)convertView.findViewById(R.id.logo);
			   holder.titleImage=(ImageView)convertView.findViewById(R.id.titleImage);
			   holder.tv_Controller = (TextView) convertView.findViewById(R.id.controller);
			   holder.tv_Title = (TextView) convertView.findViewById(R.id.empty);
			   holder.tv_CID = (TextView) convertView.findViewById(R.id.CID);
			   
			   convertView.setTag(holder);
//			  } else {
//			   holder = (ViewHolder) convertView.getTag();
//			  }
		 
			   holder = (ViewHolder) convertView.getTag();
			   
		 	holder.tv_Controller.setText(searchArrayList.get(position).getController());
			
			holder.tv_CID.setText(searchArrayList.get(position).getCID());
			  
//			Log.e("CONTROLLER::",holder.tv_Controller.getText().toString());
		 
		 	if (holder.tv_Controller.getText().toString().equals("Sampiy10"))
			{
		 		Log.e("GİRDİ","GİRDİ");
				holder.titleImage.setImageResource(R.drawable.sampiy10_h);
			}
		 	else if (holder.tv_Controller.getText().toString().equals("VatanTV"))
			{
		 		Log.e("GİRDİ","GİRDİ");
				holder.titleImage.setImageResource(R.drawable.vatantv_h);
			}
		 	else
		 	{
		 		holder.tv_Title.setText(searchArrayList.get(position).getTitle());
		 	}
			  //holder.tv_content_type.setText(searchArrayList.get(position).getContentType());
			 
		 	
			
	        convertView.setBackgroundResource(R.color.listview_selector_slide_menu);
	        
	        int resID = this.Context.getResources().getIdentifier(searchArrayList.get(position).getIcon().toLowerCase().toString()+"_h" , "drawable", this.Context.getPackageName());
		    holder.imageView.setImageResource(resID);
			
			
//			if (holder.tv_Title.getText().equals("Ana Sayfa")) {
//	        	holder.imageView.setImageResource(R.drawable.home_h);
//			}else if (holder.tv_Title.getText().equals("Son Dakika")) {
//	        	holder.imageView.setImageResource(R.drawable.breaking_news_h);
//			}else if (holder.tv_Title.getText().equals("Yazarlar")) {
//	        	holder.imageView.setImageResource(R.drawable.columnists_h);
//			}else if (holder.tv_Title.getText().equals("Siyaset")) {
//	        	holder.imageView.setImageResource(R.drawable.politics_h);
//			}else if (holder.tv_Title.getText().equals("Ekonomi")) {
//	        	holder.imageView.setImageResource(R.drawable.economics_h);
//			}else if (holder.tv_Title.getText().equals("Dünya")) {
//	        	holder.imageView.setImageResource(R.drawable.world_h);
//			}else if (holder.tv_Title.getText().equals("Gündem")) {
//	        	holder.imageView.setImageResource(R.drawable.journal_h);
//			}else if (holder.tv_Title.getText().equals("Spor")) {
//	        	holder.imageView.setImageResource(R.drawable.sports_h);
//			}else if (holder.tv_Title.getText().equals("Magazin")) {
//	        	holder.imageView.setImageResource(R.drawable.magazine_h);
//			}else if (holder.tv_Title.getText().equals("Tüm Kategoriler")) {
//	        	holder.imageView.setImageResource(R.drawable.news_categories_h);
//			}else if (holder.tv_Title.getText().equals("Galeri")) {
//	        	holder.imageView.setImageResource(R.drawable.gallery_h);
//			}else if (holder.tv_Title.getText().equals("Video")) {
//	        	holder.imageView.setImageResource(R.drawable.video_h);
//			}else if (holder.tv_Title.getText().equals("Hava Durumu")) {
//	        	holder.imageView.setImageResource(R.drawable.weather_h);
//			}else if (holder.tv_Title.getText().equals("Astroloji")) {
//	        	holder.imageView.setImageResource(R.drawable.horoscopes_h);
//			}else if (holder.tv_Title.getText().equals("Yaşam")) {
//	        	holder.imageView.setImageResource(R.drawable.life_h);
//			}else if (holder.tv_Title.getText().equals("Mola")) {
//	        	holder.imageView.setImageResource(R.drawable.mola_h);
//			}else if (holder.tv_Controller.getText().equals("Sampiy10")) {
//	        	holder.imageView.setImageResource(R.drawable.sports_h);
//			}else if (holder.tv_Controller.getText().equals("VatanTV")) {
//	        	holder.imageView.setImageResource(R.drawable.tv_h);
//			}else
//				holder.imageView.setImageResource(R.drawable.ic_launcher);

			  return convertView;
	}
	

	static class ViewHolder {
		  TextView tv_Controller;
		  TextView tv_Title;
		  TextView tv_CID;
		  ImageView imageView;
		  ImageView titleImage;
		}
}
