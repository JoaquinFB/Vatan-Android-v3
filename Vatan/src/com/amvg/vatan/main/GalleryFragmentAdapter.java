package com.amvg.vatan.main;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

class GalleryFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private ArrayList<DataModelGalleryHeadlines> results;
    private String Controller;
    
    private static Context Context;
    private static Context ContextDialog;


    public static void setContent(Context context, Context contextDialog)
    {
//    	MilliyetTvFragment.maxIndex=-1;
    	Context=context;
    	ContextDialog=contextDialog;
    }
    public GalleryFragmentAdapter(FragmentManager fm, ArrayList<DataModelGalleryHeadlines> results, String controller) {
        super(fm);
        this.Controller=controller;
        setResults(results);
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryFragment.newInstance(getResults(),position,Context,ContextDialog,Controller);
    }

    @Override
    public int getCount() {
        return getResults().size();
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            notifyDataSetChanged();
        }
    }
	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}
	public ArrayList<DataModelGalleryHeadlines> getResults() {
		return results;
	}
	public void setResults(ArrayList<DataModelGalleryHeadlines> results) {
		this.results = results;
	}
}