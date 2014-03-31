package com.amvg.vatan.main;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.liverail.library.AdView;
import com.liverail.library.events.VPAIDEvent;
import com.liverail.library.events.VPAIDEventListener;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LiverailPlayer extends DialogFragment 
{	
	private String 					  token = null;
	private OnCloseListener onCloseListener = null;
	
	private WeakReference<AdView> adView 	= null;
	
	
	public LiverailPlayer(String token)
	{
		this.token = token;
		if(token == null)
		{
			this.token = "1331"; // TestToken
		}
		setCancelable(false);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.fullScreenDialog);
	}
	
	private final VPAIDEventListener onAdLoaded = new VPAIDEventListener()
	{
		@Override
		public void onEvent(VPAIDEvent event) 
		{			
			AdView adView = getAdView();
			if(adView != null)
			{
				adView.startAd();
			}
		}		
	};
	
	private final VPAIDEventListener onAdStopped = new VPAIDEventListener()
	{
		@Override
		public void onEvent(VPAIDEvent event) 
		{			
			dismiss();
		}		
	};
	
	private final VPAIDEventListener onAdError = new VPAIDEventListener()
	{
		@Override
		public void onEvent(VPAIDEvent event) 
		{			
			dismiss();
		}		
	};
	
	private final VPAIDEventListener onAdClick = new VPAIDEventListener()
	{
		@Override
		public void onEvent(VPAIDEvent event) 
		{		
			dismiss();
		}		
	};

	@Override
	public void onStart() 
	{
		super.onStart();				
		AdView adView = getAdView();
		if(adView != null)
		{			
			adView.addEventListener(VPAIDEvent.AdLoaded, onAdLoaded);
			adView.addEventListener(VPAIDEvent.AdStopped, onAdStopped);
			adView.addEventListener(VPAIDEvent.AdError, onAdError);
			adView.addEventListener(VPAIDEvent.AdClickThru, onAdClick);
			
			Map<String, String> paramaters = new HashMap<String, String>();
			
			paramaters.put("LR_PUBLISHER_ID", token);
			paramaters.put("LR_SKIP_COUNTDOWN", "Reklam› Geç {COUNTDOWN}");
			paramaters.put("LR_LAYOUT_SKIN_MESSAGE", "Reklam | {COUNTDOWN} sn. kald›");
			paramaters.put("LR_SKIP_MESSAGE", "Reklam› Geç");
			adView.initAd(paramaters);			
		}
	}
	
	@Override
	public void dismiss()
	{
		if(onCloseListener != null)
		{
			onCloseListener.onClose();
		}
		super.dismiss();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{		
		View rootView = inflater.inflate(R.layout.fragment_liverail, null, false);
		AdView adView = (AdView)rootView.findViewById(R.id.adView);
		
		setAdView(adView);
		return rootView;
	}	
	
	public void setAdView(AdView view)
	{
		if(view == null)
			adView = null;
		else
			adView = new WeakReference<AdView>(view);
	}
	
	public AdView getAdView()
	{
		return adView == null ? null :adView.get();
	}
	
	public void setOnCloseListener(OnCloseListener onCloseListener)
	{
		this.onCloseListener = onCloseListener;
	}
		
	public interface OnCloseListener
	{
		public void onClose();
	}
}
