package com.amvg.vatan.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;






import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public final class TestFragmentPhotoGallery extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    private static final String KEY_INDEX = "TestFragment:Index";
    public static String[][] newsParams =new String[3][4];
    private static Context context;
    private static AppMap appMap;
    public static ImageView image;
//    public static TextView tv;
    public static WebView webView;
    public TextView CopyRight;
    private View view;
	public int year;
    public static boolean k=true;
    private BitmapFactory.Options options=new BitmapFactory.Options();
    
    
    //public static Bitmap[] bitmap;
    

    public static TestFragmentPhotoGallery newInstance(String imageName, String newsPara[][], Context contextParam, int position, Context contextDialog) {
//    	newsParams=newsPara;
//    	context=contextParam;
//    	appMap=new AppMap(context,contextDialog);
//    	
//        TestFragmentPhotoGallery fragment = new TestFragmentPhotoGallery();
//
//        StringBuilder builder = new StringBuilder();
//        
//        builder.append(imageName);//.append("#").append(contentType).append("#").append(ID);
//        
//        builder.deleteCharAt(builder.length() - 1);
//        fragment.mContent = builder.toString();
//        fragment.index=position;
    	
    	newsParams=newsPara;
        TestFragmentPhotoGallery fragment = new TestFragmentPhotoGallery();
        StringBuilder builder = new StringBuilder();
        builder.append(imageName);
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();
        fragment.index=position;
        
        

        return fragment;
    }

    private String mContent = "???";
    private int index=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //k=0;
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT) && savedInstanceState.containsKey(KEY_INDEX)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
        
        Calendar c = Calendar.getInstance(); 
		year = c.get(Calendar.YEAR);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	int kont=0;
    	try {
			do {
				kont++;
				if (kont>1000) {
					Log.e("Bağlantı hatası", "Lütfen tekrar deneyiniz.");
					//getActivity().finish();
					//break;
				}
				Log.e("Dosya bekleniyor","dosya bekleniyor.");
			} while (!Global.haveFile("/sdcard/Vatan/photogallery/"+mContent+"g"));
    		//Global.haveFile("/sdcard/Milliyet/photogallery/"+mContent+"g");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	PhotoGalleryAccessData.textLength[index]=newsParams[index][2].length();
    	PhotoGalleryAccessData.text[index]=newsParams[index][2];
//    	tv = new TextView(getActivity());
    	webView=new WebView(getActivity());
    	image=new ImageView(getActivity());
    	CopyRight=new TextView(getActivity());
        options.inSampleSize = 8;
        File imgFile = new  File("/sdcard/Vatan/photogallery/"+mContent+"g");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile("/sdcard/Vatan/photogallery/"+mContent+"g", options);
    	PhotoGalleryAccessData.bitmapWidth[index]=options.outWidth;;//this.bitmap.getWidth();
    	PhotoGalleryAccessData.bitmapHeight[index]=options.outHeight;;//this.bitmap.getHeight();
    	if (k) 
    	{
    		PhotoGallery.linearLayout.getLayoutParams().height=(int) ((60+Global.calculateTextHeight(newsParams[index][2], PhotoGalleryAccessData.textLength[index], PhotoGallery.width))*getResources().getDisplayMetrics().density+(int) Math.round((int) (320 * getResources().getDisplayMetrics().density + 0.5f)*PhotoGalleryAccessData.bitmapHeight[index]/PhotoGalleryAccessData.bitmapWidth[index]));//(int) (600 * scale + 0.5f);
    		k=false;
		}
    	LinearLayout layout = new LinearLayout(getActivity());
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	params.gravity=Gravity.CENTER;
    	layout.setLayoutParams(params);
//        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        
        layout.setBackgroundColor(0xFFFFFFFF);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.photo_gallery,null);
//        if (newsParams[index][2].equals(""))
//		{
//			((View)view.findViewById(R.id.view)).setVisibility(View.GONE);
//		}
//        Log.e("LENGTH::",Integer.toString(newsParams.length));
        ((TextView)view.findViewById(R.id.ResimIndis)).setText(Integer.toString(index+1));
        ((TextView)view.findViewById(R.id.ResimTop)).setText(Integer.toString(newsParams.length-1));
        image=(ImageView)view.findViewById(R.id.imageView);
        image.setImageURI(Uri.fromFile(imgFile));
//        image.getLayoutParams().width=PhotoGallery.width;//(int) (420 * scale + 0.5f);
        image.getLayoutParams().width=(int) (320 * getResources().getDisplayMetrics().density + 0.5f);
    	try 
    	{
    		Log.e("HATASIZ","BOYUT");
    		image.getLayoutParams().height=(int) Math.round(image.getLayoutParams().width*PhotoGalleryAccessData.bitmapHeight[index]/PhotoGalleryAccessData.bitmapWidth[index]);//(int) (320 * scale + 0.5f);
//    		image.getLayoutParams().height=(int) (320 * getResources().getDisplayMetrics().density + 0.5f);
    	} 
    	catch (Exception e) {
			// TODO: handle exception
    		Log.e("HATAYA","GŞİRDİ");
			image.getLayoutParams().height=850;
		}
    	image.setScaleType(ImageView.ScaleType.FIT_XY);
//        tv=(TextView)view.findViewById(R.id.text);
//        tv.setText(newsParams[index][2]);
    	
    	webView=(WebView)view.findViewById(R.id.webView);
    	//webView.loadData(newsParams[index][2], "text/html", "UTF-8");
    	webView.loadDataWithBaseURL("http://www.google.com", createHTML(newsParams[index][2]), "text/html", "UTF-8", "");
        CopyRight=(TextView)view.findViewById(R.id.Copyrigth);
        CopyRight.setText("Copyright \u00A9 "+Integer.toString(year)+" Vatan Gazetesi");
        layout.addView(view);
    	
////    	final float scale = this.context.getResources().getDisplayMetrics().density;
////    	getResources().getDisplayMetrics().density
//    	PhotoGalleryAccessData.textLength[index]=newsParams[index][2].length();
//    	PhotoGalleryAccessData.text[index]=newsParams[index][2];
////    	Log.e("scale=",Float.toString(scale));
////    	Log.e("textSize",Integer.toString((int) (16*scale+0.5f)));
//    	TextView text = new TextView(getActivity());
//        text.setGravity(Gravity.CENTER);
//        text.setText(newsParams[index][2]);
//        text.setTextSize(16); 
//        //text.setTextSize((int) (16 * scale + 0.5f));
//        text.setTypeface(Typeface.DEFAULT_BOLD);
//        text.setTextColor(0xFF000000);
//        text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//        //text.setPadding(20, 20, 20, 20);
//        //text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
////        Log.e("newsParams[index][2]",newsParams[index][2]);
////        Log.e("mContent",mContent);
//        //Log.e("textview width=",Integer.toStrqwering(text.getLayoutParams().width));
//        //Log.e("textview height=",Integer.toString(text.getLayoutParams().height));
//        options.inSampleSize = 8;
////        //bitmap.recycle();
////        try {
////        	this.bitmap=BitmapFactory.decodeFile("/sdcard/Milliyet/photogallery/"+mContent+"g");
////		} catch (Exception e) {
////			// TODO: handle exception
////			Log.e("HATA","HATA");
////			this.bitmap=BitmapFactory.decodeFile("/sdcard/Milliyet/photogallery/"+mContent+"g",options);
////		}
//        
//        File imgFile = new  File("/sdcard/Vatan/photogallery/"+mContent+"g");
//        
////        try {
////			inputStream = new FileInputStream("/sdcard/Milliyet/photogallery/"+mContent+"g");
////			bitmap=BitmapFactory.decodeStream(inputStream);
////		} catch (FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			Log.e("hata","FileNotFoundException");
////			e.printStackTrace();
////		} finally {
////			if (inputStream != null) { try {
////				inputStream.close();
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} }
////		}
//        
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//
//        //Returns null, sizes are in the options variable
//        BitmapFactory.decodeFile("/sdcard/Vatan/photogallery/"+mContent+"g", options);
//        
//        
////    	this.bitmap = BitmapFactory.decodeFile("/sdcard/Milliyet/photogallery/"+mContent+"g");
//    	PhotoGalleryAccessData.bitmapWidth[index]=options.outWidth;;//this.bitmap.getWidth();
//    	PhotoGalleryAccessData.bitmapHeight[index]=options.outHeight;;//this.bitmap.getHeight();
//    	Log.e("bitmapName","/sdcard/Vatan/photogallery/"+mContent);
//    	image=new ImageView(getActivity());
//    	Log.e("screen width:",Integer.toString(PhotoGallery.width));
////    	Log.e("bitmap width:",Integer.toString(bitmap.getWidth()));
//    	Log.e("screen height:",Integer.toString(PhotoGallery.height));
////    	Log.e("bitmap height:",Integer.toString(bitmap.getHeight()));
//    	
//    	
//    	
//    	image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
////    	image.setImageBitmap(bitmap);//Bitmap.createScaledBitmap(bitmap, PhotoGallery.width, 896, false)
//    	//bitmap.recycle();
//    	image.setImageURI(Uri.fromFile(imgFile));
//    	
//    	
//    	
//    	image.getLayoutParams().width=PhotoGallery.width;//(int) (420 * scale + 0.5f);
//    	try {
//    		image.getLayoutParams().height=(int) Math.round(PhotoGallery.width*PhotoGalleryAccessData.bitmapHeight[index]/PhotoGalleryAccessData.bitmapWidth[index]);//(int) (320 * scale + 0.5f);
//    		Log.e("image.getLayoutParams",Integer.toString(image.getLayoutParams().height) );
//		} catch (Exception e) {
//			// TODO: handle exception
//			image.getLayoutParams().height=850;
//			Log.e("HATA",e.toString());
//		}
//    	
//    	image.setScaleType(ImageView.ScaleType.FIT_XY);
//    	image.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.e("click",mContent);
//				String compare=mContent+"g";
//				if (compare.equals(newsParams[0][3])) {
//					appMap.RunActivity(newsParams[0][0], "", newsParams[0][1], "");
//				}else if (compare.equals(newsParams[1][3])) {
//					appMap.RunActivity(newsParams[1][0], "", newsParams[1][1], "");
//					Log.e("id=",newsParams[1][1]);
//					Log.e("content Type=",newsParams[1][0]);
//				}else if (compare.equals(newsParams[2][3])) {
//					appMap.RunActivity(newsParams[2][0], "", newsParams[2][1], "");
//					Log.e("id=",newsParams[2][1]);
//					Log.e("content Type=",newsParams[2][0]);
//				}
//				
//				
//			}
//		});
//    	text.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//    	
//    	//image.setImageResource(R.drawable.ic_launcher);
//    	//image.setImageBitmap(bitmap);
//    	//image.setima
//    	
//    	if (k) 
//    	{
//    		PhotoGallery.linearLayout.getLayoutParams().height=(int) (60+Global.calculateTextHeight(newsParams[index][2],PhotoGalleryAccessData.textLength[index], PhotoGallery.width)*getResources().getDisplayMetrics().density+(int) Math.round(PhotoGallery.width*PhotoGalleryAccessData.bitmapHeight[index]/PhotoGalleryAccessData.bitmapWidth[index]));//(int) (600 * scale + 0.5f);
//    		//PhotoGallery.linearLayout.getLayoutParams().height=320;
//    		k=false;
//		}
//    	
//    	//PhotoGallery.linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        
//
//        LinearLayout layout = new LinearLayout(getActivity());
//        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        layout.setBackgroundColor(0xFFFFFFFF);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.getLayoutParams().height=(int) Math.round(PhotoGallery.width*PhotoGalleryAccessData.bitmapHeight[index]/PhotoGalleryAccessData.bitmapWidth[index]);
//        layout.setGravity(Gravity.CENTER_HORIZONTAL);
//        //layout.addView(image);
////    	RelativeLayout.LayoutParams lptvinvisible2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,(int) (29 * scale + 0.5f));
////    	//lptvinvisible.addRule(RelativeLayout.CENTER_HORIZONTAL);
////    	lptvinvisible2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//    	
//    	
//    	
//    	
////    	RelativeLayout layout = new RelativeLayout(getActivity());
////    	layout.setBackgroundColor(0xFFFFFFFF);
////        //layout.setOrientation(LinearLayout.VERTICAL);
////        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        
//    	layout.addView(image);
//        layout.addView(text);

        return layout;
    }
   

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
        outState.putInt(KEY_INDEX, index);
    }
    
    private String createHTML(String Description)
	{ 
		String customHtml;
		customHtml="";
		customHtml+="<!DOCTYPE HTML>";
		customHtml+="<html>";
		customHtml+="<head>";
		customHtml+="<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">";
		customHtml+="<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0\" />";
		customHtml+="<link rel=\"stylesheet\" href=\"file:///android_asset/css/PhotoGalleryAndroid.css\" />";
		customHtml+="</head>";
		customHtml+="<body data-role=\"page\">";
		customHtml+="<div class=\"Description\">"+Description+"</div>";
		customHtml+="</body>";
		customHtml+="</html>";
		return customHtml;
	}
    
}
