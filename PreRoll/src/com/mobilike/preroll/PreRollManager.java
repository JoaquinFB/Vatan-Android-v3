package com.mobilike.preroll;

import android.app.Activity;
import android.content.Intent;

public class PreRollManager extends BaseObject
{
	public static final int REQUESTCODE_PREROLLACTIVITY = 907;

	
	/***************************************
	 * Singleton reference
	 */
	private static PreRollManager sharedInstance = null;
	
	/***************************************
	 * Constructors & instance providers
	 */
	private PreRollManager(){/*No public constructor*/}

	public static PreRollManager sharedInstance()
	{
		if(PreRollManager.sharedInstance == null)
		{
			PreRollManager.sharedInstance = new PreRollManager();
		}
		
		return PreRollManager.sharedInstance;
	}
	
	/***************************************
	 * -
	 */
	public void showPreRoll(final Activity activity, String url)
	{
		if(ApplicationUtilities.sharedInstance().isActivityAlive(activity))
		{
			Intent preRollIntent = new Intent(activity, PreRollActivity.class);
			
			preRollIntent.putExtra(PreRollActivity.PREROLLACTIVITY_XMLDOCUMENTURL_KEY, url);
			
			activity.startActivityForResult(preRollIntent, REQUESTCODE_PREROLLACTIVITY);
		}
		else
		{
			log("Provided activity is rotten!");
		}
	}

	
	/***************************************
	 * Log
	 */
	
	@Override
	protected boolean isLogEnabled()
	{
		return true;
	}

	@Override
	protected String getLogTag()
	{
		return "PreRollManager";
	}
}
