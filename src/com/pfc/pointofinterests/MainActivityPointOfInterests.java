package com.pfc.pointofinterests;

import java.io.File;

import com.pfc.pointofinterests.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivityPointOfInterests extends ActionBarActivity {

	public static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	public static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
	
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	LaunchCamActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void LaunchCamActivity ()
    {
		// com.pfc.pointofinterests.SampleCamHandlePoiDetailActivity
		/* launch activity */
		/* store className of activity to call*/
		// final String className = "com.pfc.pointofinterests.SampleCamHandlePoiDetailActivity";
		final String className = "com.pfc.pointofinterests.SampleCamActivity";
		try {

			final Intent intent = new Intent(this, Class.forName(className));
			intent.putExtra(EXTRAS_KEY_ACTIVITY_TITLE_STRING,
					"POI");
			intent.putExtra(EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, // "samples" +
					"99_Demo_1_Image$Recognition$And$Geo" //this.getArchitectWorldUrls()[position]
					+ File.separator + "index.html");
			/*
			 * intent.putExtra(EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, "samples"
					+ File.separator + this.getArchitectWorldUrls()[position]
					+ File.separator + "index.html");
			 */

			/* launch activity */
			this.startActivity(intent);

		} catch (Exception e) {
			/*
			 * may never occur, as long as all SampleActivities exist and are
			 * listed in manifest
			 */
			Toast.makeText(this, className + "\nnot defined/accessible",
					Toast.LENGTH_SHORT).show();
		}
    }
}
