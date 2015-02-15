package com.pfc.pointofinterests;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.CaptureScreenCallback;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;


public abstract class SampleCamCaptureScreenActivity extends AbstractArchitectCamActivity {

	/**
	 * extras key for activity title, usually static and set in Manifest.xml
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	
	/**
	 * extras key for architect-url to load, usually already known upfront, can be relative folder to assets (myWorld.html --> assets/myWorld.html is loaded) or web-url ("http://myserver.com/myWorld.html"). Note that argument passing is only possible via web-url 
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";

	int iTotalPois 		= 0;
 	int	iTotalPoiFields	= 0;
 	String [] szPoiFieldsName;
	
	
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
				if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && SampleCamCaptureScreenActivity.this != null && !SampleCamCaptureScreenActivity.this.isFinishing() ) {
					Toast.makeText( SampleCamCaptureScreenActivity.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
				}
			}
		};
	}

	@Override
	public ArchitectUrlListener getUrlListener() {
		return new ArchitectUrlListener() {

			@Override
			public boolean urlWasInvoked(final String uriString) {
				
				Uri invokedUri = Uri.parse(uriString);
				String szIdPoiSelected;
				szIdPoiSelected = invokedUri.getQueryParameter("id");
				int i = Integer.parseInt(szIdPoiSelected);
				String pp [] = szPoiInfo (i);
				int iField = 0;
				vReadText (pp[iField]);
				
				vReadText (pp + "¿Tiene alguna pregunta?");
				startVoiceRecognitionActivity();
				
				return true;
				// pressed "More" button on POI-detail panel
				/*
				if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
					final Intent poiDetailIntent = new Intent(SampleCamCaptureScreenActivity.this, SamplePoiDetailActivity.class);
					poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_ID, String.valueOf(invokedUri.getQueryParameter("id")) );
					poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_TITILE, String.valueOf(invokedUri.getQueryParameter("title")) );
					poiDetailIntent.putExtra(SamplePoiDetailActivity.EXTRAS_KEY_POI_DESCR, String.valueOf(invokedUri.getQueryParameter("description")) );
					SampleCamCaptureScreenActivity.this.startActivity(poiDetailIntent);
					return true;
				}
				
				// pressed snapshot button. check if host is button to fetch e.g. 'architectsdk://button?action=captureScreen', you may add more checks if more buttons are used inside AR scene
				else if ("button".equalsIgnoreCase(invokedUri.getHost())) {
					vReadText ("button");
					
					SampleCamCaptureScreenActivity.this.architectView.captureScreen(ArchitectView.CaptureScreenCallback.CAPTURE_MODE_CAM_AND_WEBVIEW, new CaptureScreenCallback() {
						
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
								SampleCamCaptureScreenActivity.this.startActivity(Intent.createChooser(share, chooserTitle));
							
							} catch (final Exception e) {
								// should not occur when all permissions are set
								SampleCamCaptureScreenActivity.this.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// show toast message in case something went wrong
										Toast.makeText(SampleCamCaptureScreenActivity.this, "Unexpected error, " + e, Toast.LENGTH_LONG).show();	
									}
								});
							}
						}
					});
				}
				return true;*/
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
	
	public void onClickTextToSpeech()
	{
		vReadText ("pp");
	}

	@Override
	public void onInit(int status)
	{
		// TODO Auto-generated method stub
        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED )
        {
                Toast.makeText( this, "ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED", Toast.LENGTH_SHORT ).show();
        }
	}
	
	@Override 
	public float vSetSpeechRate () 
	{	
		return 0.0f;
	}
	
	@Override 
	public float vSetPitch () 
	{
		return 0.0f;
	}


	@Override
	public String vSetLocalA()
	{
		// TODO Auto-generated method stub
		return "spa";
	}

	@Override
	public String vSetLocalB()
	{
		// TODO Auto-generated method stub
		return "ESP";
	}
	
	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
  	private void startVoiceRecognitionActivity() 
  	{
	  // Definici�n del intent para realizar en an�lisis del mensaje
	  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	  // Indicamos el modelo de lenguaje para el intent
	  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	   RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	  // Definimos el mensaje que aparecer� 
	  intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Diga, Llamar a ...");
	  // Lanzamos la actividad esperando resultados
	  startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
  	}

  	

 	@Override
 	public String JSonGetPoiInformationFuntion()
 	{
 		return "IrAndGeo.loadPoisFromJSon";
 	}
 	@Override
 	public String JSonUploadMoreInfoFuntion()
 	{
 		return "IrAndGeo.GetExecuteOperationFromJSon";
 	}
 	
 	@Override 
 	public JSONArray 	UploadMoreInfoFuntionToJavaScript()
 	{
 		String [][] pruebadeEnvio = new String [1][2];
 		pruebadeEnvio [0] [0] = "envio";
 		pruebadeEnvio [0] [1] = "Estos son los datos";
 		
		return ExecuteJavaScript(0, pruebadeEnvio, 1);

 		
 	}
 	public JSONArray ExecuteJavaScript (String szFunction,  int iOperation, String [] [] info, int iTotalInfo)
 	{		
 		final HashMap<String, String> HashMapOperationToExecute = new HashMap<String, String>();	
 		final JSONArray JSONArrayOperation = new JSONArray();
 		
 		////////////////////////////////////////////////////////////////////////////////
 		// Operation To Execute in Java Script
 		////////////////////////////////////////////////////////////////////////////////
 		String [] szOperation = new String [2];
 		szOperation[0] = "Operation";
 		szOperation[1] = "" + iOperation;
 		HashMapOperationToExecute.put (szOperation[0], szOperation[1]);
 		JSONArrayOperation.put(new JSONObject(HashMapOperationToExecute));
 		////////////////////////////////////////////////////////////////////////////////
 		final HashMap<String, String> HashMapOperationInformation = new HashMap<String, String>();
		
		for (int j = 0; j < iTotalInfo; j++)
		{ 
			HashMapOperationInformation.put (info[j][0], info[j][1]);
		}
		JSONArrayOperation.put(new JSONObject(HashMapOperationInformation));
		SampleCamCaptureScreenActivity.this.callJavaScript(szFunction, new String[] { JSONArrayOperation.toString() });

 		return JSONArrayOperation;
 	}
 	public JSONArray ExecuteJavaScript (int iOperation, String [] [] info, int iTotalInfo)
 	{		
 		final HashMap<String, String> HashMapOperationToExecute = new HashMap<String, String>();	
 		final JSONArray JSONArrayOperation = new JSONArray();
 		
 		////////////////////////////////////////////////////////////////////////////////
 		// Operation To Execute in Java Script
 		////////////////////////////////////////////////////////////////////////////////
 		String [] szOperation = new String [2];
 		szOperation[0] = "Operation";
 		szOperation[1] = "" + iOperation;
 		HashMapOperationToExecute.put (szOperation[0], szOperation[1]);
 		JSONArrayOperation.put(new JSONObject(HashMapOperationToExecute));
 		////////////////////////////////////////////////////////////////////////////////
 		final HashMap<String, String> HashMapOperationInformation = new HashMap<String, String>();
		
		for (int j = 0; j < iTotalInfo; j++)
		{ 
			HashMapOperationInformation.put (info[j][0], info[j][1]);
		}
		JSONArrayOperation.put(new JSONObject(HashMapOperationInformation));
 		return JSONArrayOperation;
 	}
 	/**
 	 * loads poiInformation and returns them as JSONArray. Ensure attributeNames of JSON POIs are well known in JavaScript, so you can parse them easily
 	 * @param userLocation the location of the user
 	 * @param numberOfPlaces number of places to load (at max)
 	 * @return POI information in JSONArray
 	 */
 	@Override
 	public JSONArray getPoiInformation()
 	{
 		final HashMap<String, String> poiInformation = new HashMap<String, String>();
 		iTotalPois 		= iInitPlaces	(); 
 	 	iTotalPoiFields	= iSetTotalPoiFields ();
 	 	szPoiFieldsName = szPoiNameFields();
 		final JSONArray pois = new JSONArray();
 		String [] PoiInformation = new String [iTotalPoiFields];
 		for (int i=1;i <= iTotalPois; i++) 
 		{
 			PoiInformation = szPoiInfo(i);
 			for (int j = 0; j < iTotalPoiFields;j++)
			{
 				poiInformation.put (szPoiFieldsName[j], PoiInformation[j]);
			}
 			
 			pois.put(new JSONObject(poiInformation));
 		}
 		return pois;
 	}

  	@Override
  	public JSONArray getPoiInformation(final Location userLocation, final int numberOfPlaces) 
  	{
 		if (userLocation==null) {
 			return null;
 		}

 		final JSONArray pois = new JSONArray();

 		// ensure these attributes are also used in JavaScript when extracting POI data
 		final String ATTR_ID 			= "id";
 		final String ATTR_NAME 			= "name";
 		final String ATTR_DESCRIPTION 	= "description";
 		final String ATTR_LATITUDE 		= "latitude";
 		final String ATTR_LONGITUDE 	= "longitude";
 		final String ATTR_ALTITUDE 		= "altitude";
 		
 		for (int i=1;i <= numberOfPlaces; i++) 
 		{
 			final HashMap<String, String> poiInformation = new HashMap<String, String>();
 			poiInformation.put(ATTR_ID, String.valueOf(i));
 			poiInformation.put(ATTR_NAME, "POI#" + i);
 			poiInformation.put(ATTR_DESCRIPTION, "This is the description of POI#" + i);
 			double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
 			poiInformation.put(ATTR_LATITUDE, String.valueOf(poiLocationLatLon[0]));
 			poiInformation.put(ATTR_LONGITUDE, String.valueOf(poiLocationLatLon[1]));
 			final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
 			// Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
 			poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
 			pois.put(new JSONObject(poiInformation));
 		}
 		
 		return pois;
 	}
 	public HashMap <String, String> DataPoints ()
 	{
		return null;
 	}
 	/**
 	 * helper for creation of dummy places.
 	 * @param lat center latitude
 	 * @param lon center longitude
 	 * @return lat/lon values in given position's vicinity
 	 */
	private static double[] getRandomLatLonNearby(final double lat, final double lon) {
		return new double[] { lat + Math.random()/5-0.1 , lon + Math.random()/5-0.1};
	}
 	/**
 	 * vInit must return the number of items to track
 	 * Inside you can do anything for example create a database and
 	 * insert poi.
 	 * @param 
 	 * @param lon center longitude
 	 * @return lat/lon values in given position's vicinity
 	 */
 	public abstract int 		iInitPlaces 			();
 	public abstract String[] 	szPoiInfo				(int IdPoi);
 	public abstract int			iSetTotalPoiFields		();
 	public abstract String[]	szPoiNameFields		();
 	
}
