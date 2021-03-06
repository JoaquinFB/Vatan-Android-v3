package com.amvg.vatan.main;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter 
{
    private ArrayList<DataModelHomeHeadlines> results;
    private String PahtName;
    private static Context Context;
    private static Context ContextDialog;

    public static void setContent(Context context, Context contextDialog)
    {
    	Context=context;
    	ContextDialog=contextDialog;
    }
    public TestFragmentAdapter(FragmentManager fm, ArrayList<DataModelHomeHeadlines> results, String pathName) 
    {
        super(fm);
        setPathName(pathName);
        setResults(results);
    }

    @Override
    public Fragment getItem(int position) 
    {
        return TestFragment.newInstance(getResults(),position,Context,ContextDialog,getPathName());
    }

    @Override
    public int getCount() 
    {
        return getResults().size();
    }

    public void setCount(int count)
    {
        if (count > 0 && count <= 10) 
        {
            notifyDataSetChanged();
        }
    }
	@Override
	public int getIconResId(int index) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
	public ArrayList<DataModelHomeHeadlines> getResults() 
	{
		return results;
	}
	public void setResults(ArrayList<DataModelHomeHeadlines> results) 
	{
		this.results = results;
	}
	public String getPathName()
	{
		return this.PahtName;
	}
	public void setPathName(String pathName)
	{
		this.PahtName=pathName;
	}
}