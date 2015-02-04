package com.pfc.pointofinterests;

import java.io.File;

import com.pfc.pointofinterests.R;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pfc.pointofinterests.SampleCamCaptureScreenActivity;

public class MainActivityPointOfInterests extends ActionBarActivity {

	public static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING 		= "activityTitle";
	public static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL 	= "activityArchitectWorldUrl";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_point_of_interests);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_point_of_interests, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
		String activityTitle;
		String activityUrl;
		String classNameCamActivity = "com.pfc.pointofinterests.SampleCamActivity";
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        activityUrl 	= "";
        activityTitle 	= "";
        try {
        	final Intent intent;
	    	switch (id)
	    	{
	    		case R.id.ImageRecognitionAndGeo_99_1:
	    			activityUrl 			= "99_Demo_1_Image$Recognition$And$Geo"; //this.getArchitectWorldUrls()[position]
	    			activityTitle 			= (String) getResources().getText(R.string.ImageRecognitionAndGeo);
	    			classNameCamActivity 	= "com.pfc.pointofinterests.SampleCamCaptureScreenActivity";
	    			intent = new Intent( this, SampleCamCaptureScreenActivity.class );
	    			break;
	    		case R.id.POI_NativeDetailScreen_5_5:
	    			activityUrl = "5_Browsing$Pois_5_Native$Detail$Screen";  			
	    			activityTitle = (String) getResources().getText(R.string.POI_NativeDetailScreen);
	    			classNameCamActivity = "com.pfc.pointofinterests.SampleCamCaptureScreenActivity";
	    			intent = new Intent( this, SampleCamCaptureScreenActivity.class );
	    			break;	
	    		case R.id.CaptureScreenBonus_5_6:
	    			activityUrl = "5_Browsing$Pois_6_Capture$Screen$Bonus";
	    			activityTitle = (String) getResources().getText(R.string.POI_CaptureScreenBonus);
	    			classNameCamActivity = "com.pfc.pointofinterests.SampleCamCaptureScreenActivity";
	    			intent = new Intent( this, SampleCamCaptureScreenActivity.class );
	    			break;
	    		default:
	    			intent = new Intent( this, PoiDatabase.class );
	    			break;
	    	}
	
	    	activityUrl += File.separator + "index.html";			
			/* launch activity */
			intent.putExtra(EXTRAS_KEY_ACTIVITY_TITLE_STRING,
							activityTitle);
			intent.putExtra(EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, // "samples" +
							activityUrl);
			/*
			 * intent.putExtra(EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, "samples"
					+ File.separator + this.getArchitectWorldUrls()[position]
					+ File.separator + "index.html");
			 */

			/*  launch activity */
			MainActivityPointOfInterests.this.startActivity(intent);

		} catch (Exception e) {
			/*
			 * may never occur, as long as all SampleActivities exist and are
			 * listed in manifest
			 */
			Toast.makeText(this, classNameCamActivity + "\nnot defined/accessible",
					Toast.LENGTH_SHORT).show();
		}
        	//LaunchCamActivity(classNameCamActivity, activityTitle, activityUrl);
        return true;

        // return super.onOptionsItemSelected(item);
    }
    
    public void LaunchCamActivity (String className, String activityTitle, String activityUrl)
    {
		// com.pfc.pointofinterests.SampleCamHandlePoiDetailActivity
		/* launch activity */
		/* store className of activity to call*/
		// final String className = "com.pfc.pointofinterests.SampleCamHandlePoiDetailActivity";
    	

    }
}
