package com.amvg.vatan.main;



import com.amvg.vatan.main.LiverailPlayer.OnCloseListener;
import com.mobilike.preroll.PreRollManager;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
//import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlay extends FragmentActivity {
	
	VideoView videoView;
	private String url;
	
	private void Video_Play(final String url){
		
		
        String httpLiveUrl = url;   
        videoView = (VideoView) findViewById(R.id.VideoView);
        videoView.setVideoURI(Uri.parse(httpLiveUrl));  
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
    }
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_play);
//		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
//            return;
		String  preRollToken = null;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     this.url=extras.getString("url");
		     String categoryId = extras.getString("categoryId");
		     preRollToken = getPreRollUrl(categoryId);
		}
//		Video_Play(url);
		
		LiverailPlayer liverailPlayer = new LiverailPlayer(preRollToken);
		liverailPlayer.setOnCloseListener(new OnCloseListener() 
		{			
			@Override
			public void onClose() 
			{
				Video_Play(VideoPlay.this.url);
			}
		});
		
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		liverailPlayer.show(trans, "liverailPlayer");
		
		
		//PreRollManager.sharedInstance().showPreRoll(this, preRollUrl);
	}
	
	private String getPreRollUrl(String categoryId)
	{
		String url = null;
		
		//Spor
		if("3".equalsIgnoreCase(categoryId))
		{
			url = "29330";
		}
		//Fragman
		else if("13".equalsIgnoreCase(categoryId))
		{
			url = "29333";
		}
		//Magazin
		else if("38".equalsIgnoreCase(categoryId))
		{
			url = "29329";
		}
		else if("2".equalsIgnoreCase(categoryId))
		{
			url = "29328";
		}
		else if("44".equalsIgnoreCase(categoryId))
		{
			url = "29332";
		}
		else if("4".equalsIgnoreCase(categoryId))
		{
			url = "29331";
		}
		else
		{
			url = "29327";
		}
		return url;
		/*
		// Amatör
		if("13".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_amator_android.xml";
		}
		// Animasyon
		else if("4".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_animasyon_android.xml";
		}
		// Eğlence
		else if("7".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_eglence_android.xml";
		}
		// Haber
		else if("9".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_haber_android.xml";
		}
		// Hayvalar
		else if("15".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_hayvan_android.xml";
		}
		// Kazalar
		else if("12".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_kazalar_android.xml";
		}
		// Komedi
		else if("2".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_komedi_android.xml";
		}
		// Magazin
		else if("19".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_magazin_android.xml";
		}
		// Müzik
		else if("3".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_muzik_android.xml";
		}
		// Politika
		else if("6".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_politika_android.xml";
		}
		// Reklamlar
		else if("11".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_reklamlar_android.xml";
		}
		// Şakalar
		else if("17".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_sakalar_android.xml";
		}
		// Fragman
		else if("5".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_fragman_android.xml";
		}
		// Spor
		else if("1".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_spor_android.xml";
		}
		// Teknoloji
		else if("8".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_teknoloji_android.xml";
		}
		// TV
		else if("14".equals(categoryId))
		{
			url = "http://mobworkz.com/video/vatan_tv_android.xml";
		}
		else
		{
			url = "http://mobworkz.com/video/vatan_manset_android.xml";
		}
		
		return url;*/
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   onPause();
	   finish();
	}

	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data)
	{
		switch (requestCode)
		{
			case PreRollManager.REQUESTCODE_PREROLLACTIVITY:
			{
				Video_Play(this.url);
				
				break;
			}
			default:
				break;
		}
	}
}
