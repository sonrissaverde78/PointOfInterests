package com.pfc.pointofinterests;

import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONArray;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.CaptureScreenCallback;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;
import com.pfc.pointofinterests.R;

public class SampleCamActivity extends AbstractArchitectCamActivity {

	public static final String EXTRAS_KEY_ACTIVITIES_TILES_ARRAY 		= "activitiesTitles";
	/**
	 * extras key for activity title, usually static and set in Manifest.xml
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	
	/**
	 * extras key for architect-url to load, usually already known upfront, can be relative folder to assets (myWorld.html --> assets/myWorld.html is loaded) or web-url ("http://myserver.com/myWorld.html"). Note that argument passing is only possible via web-url 
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
	

	/**
	 * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
	 */
	private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

	@Override
	public String getARchitectWorldPath() {
		return getIntent().getExtras().getString(
				EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL);
	}

	@Override
	public String getActivityTitle() {
		return (getIntent().getExtras() != null && getIntent().getExtras().get(
				EXTRAS_KEY_ACTIVITY_TITLE_STRING) != null) ? getIntent()
				.getExtras().getString(EXTRAS_KEY_ACTIVITY_TITLE_STRING)
				: "Test-World";
	}

	@Override
	public int getContentViewId() {
		//return R.layout.sample_cam;
		return R.layout.prueba;
	}

	@Override
	public int getArchitectViewId() {
		return R.id.architectView;
	}
	
	@Override
	public String getWikitudeSDKLicenseKey() {
		return WikitudeSDKConstants.WIKITUDE_SDK_KEY;
	}
	
	@Override
	public SensorAccuracyChangeListener getSensorAccuracyListener() {
		return new SensorAccuracyChangeListener() {
			@Override
			public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
				if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && SampleCamActivity.this != null && !SampleCamActivity.this.isFinishing() && System.currentTimeMillis() - SampleCamActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
					Toast.makeText( SampleCamActivity.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
					SampleCamActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
				}
			}
		};
	}

	//@Override
	public ArchitectUrlListener getUrlListener2() {
		return new ArchitectUrlListener() {

			@Override
			public boolean urlWasInvoked(String uriString) {
				// by default: no action applied when url was invoked
				return false;
			}
		};
	}
	public ArchitectUrlListener getUrlListener() {
		return new ArchitectUrlListener() {

			@Override
			public boolean urlWasInvoked(final String uriString) {
				
				Uri invokedUri = Uri.parse(uriString);
				
				// pressed "More" button on POI-detail panel
				if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
					final Intent poiDetailIntent = new Intent(SampleCamActivity.this, SampleCamActivity.class);
					poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_ID, String.valueOf(invokedUri.getQueryParameter("id")) );
					poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_TITILE, String.valueOf(invokedUri.getQueryParameter("title")) );
					poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_DESCR, String.valueOf(invokedUri.getQueryParameter("description")) );
					SampleCamActivity.this.startActivity(poiDetailIntent);
					return true;
				}
				
				// pressed snapshot button. check if host is button to fetch e.g. 'architectsdk://button?action=captureScreen', you may add more checks if more buttons are used inside AR scene
				else if ("button".equalsIgnoreCase(invokedUri.getHost())) {
					SampleCamActivity.this.architectView.captureScreen(ArchitectView.CaptureScreenCallback.CAPTURE_MODE_CAM_AND_WEBVIEW, new CaptureScreenCallback() {
						
						@Override
						public void onScreenCaptured(final Bitmap screenCapture) {
							// store screenCapture into external cache directory
							final File screenCaptureFile = new File(Environment.getExternalStorageDirectory().toString(), "screenCapture_" + System.currentTimeMillis() + ".jpg");
							
							// 1. Save bitmap to file & compress to jpeg. You may use PNG too
							try {
								final FileOutputStream out = new FileOutputStream(screenCaptureFile);
								screenCapture.compress(Bitmap.CompressFormat.JPEG, 90, out);
								out.flush();
								out.close();
							
								// 2. create send intent
								final Intent share = new Intent(Intent.ACTION_SEND);
								share.setType("image/jpg");
								share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(screenCaptureFile));
		
								// 3. launch intent-chooser
								final String chooserTitle = "Share Snaphot";
								SampleCamActivity.this.startActivity(Intent.createChooser(share, chooserTitle));
							
							} catch (final Exception e) {
								// should not occur when all permissions are set
								SampleCamActivity.this.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// show toast message in case something went wrong
										Toast.makeText(SampleCamActivity.this, "Unexpected error, " + e, Toast.LENGTH_LONG).show();	
									}
								});
							}
						}
					});
				}
				return true;
			}
				
		};
		
	}
	@Override
	public ILocationProvider getLocationProvider(final LocationListener locationListener) {
		return new LocationProvider(this, locationListener);
	}
	
	@Override
	public float getInitialCullingDistanceMeters() {
		// you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
		return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
	}

	@Override
	public void onInit (int status)
	{
		
	}



	@Override
	public float vSetSpeechRate()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float vSetPitch()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String vSetLocalA()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String vSetLocalB()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String JSonFuntion()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray getPoiInformation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray getPoiInformation(android.location.Location userLocation,
			int numberOfPlaces)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
