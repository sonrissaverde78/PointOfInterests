package com.pfc.pointofinterests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONArray;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.pfc.pointofinterests.PoiSQLiteHelper;
public class PoiDatabase extends SampleCamCaptureScreenActivity 
{
	private SQLiteDatabase 			db;
 	private String 					dbName = "dbPoi";
 	private PoiSQLiteHelper 		usdbh;
 	
	public PoiDatabase()
	{
		
	}
	public void vCreateDatabase ()
	{
 		usdbh = 
 	            new PoiSQLiteHelper(this, dbName, null, 1); // RW Opened. 
	}
	
	@Override
	// vInitPlaces () create and open a database in write mode to 
	// allow the insertion of registers.
 	public int iInitPlaces ()
	{
        // db Opening. Name 'dbPoi' with RW permissions.
 		// db location:
 		// 		/data/data/paquete.java.de.la.aplicacion/databases/nombre_base_datos

 		vCreateDatabase ();
 		// Database  is opened with W mode.
        db = usdbh.getWritableDatabase();
 
        int i = 0;
        
        //If db was properly openned 5 samples are inserted
        if(db != null)
        {
            // 
            for( i=1; i<=5; i++)
            {
                String szPoi = 
                i + ", "				+ 
				"' " + i + "'" + ", "	+
				"' " + i + "'" + ", "	+ 
				i + ", "				+ 
				i + ", "				+
				i;
                //Insertamos los datos en la tabla Usuarios
                String szInsertIntoPOItable = "INSERT INTO POI (iId, name, description, lat, long, alt) " +
                "VALUES (" + szPoi +")";
                db.execSQL(szInsertIntoPOItable);
            }
 
            // Database is closed.
            db.close();
        }
        return i;
	}
	@Override
	public String 	szGet_db_PoiInfo		(int IdPoi)
 	{
		return readPoiInformation(IdPoi);
 	}

	
	private String readPoiInformation (int id)
	{
		String cols = (	"iId, " 			+
					"name, " 			+
					"description, " 	+ 
					"lat, " 			+ 
					"long, " 			+
					"alt");
		String Select = "SELECT " + cols + " FROM " +  usdbh.tableName + " WHERE " + " iId= " + id;
		
		db = usdbh.getReadableDatabase();
		
		// String[] args = new String[] {"usu1"};
		// sql select is sent.
		Cursor c = db.rawQuery( Select, null);
		// End of pointer is checked and then we take the result.
		String iId = null;
		if (c.moveToFirst()) 
		{
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		          // String codigo= c.getString(0);
		          iId = c.getString(1);
		     } while(c.moveToNext());
		}
		
		db.close();
		return iId;	
	}

	public HashMap<String, String> JSonPoiInformation (int i)
	{
 		final String ATTR_ID 			= "id";
 		final String ATTR_NAME 			= "name";
 		final String ATTR_DESCRIPTION 	= "description";
 		final String ATTR_LATITUDE 		= "latitude";
 		final String ATTR_LONGITUDE 	= "longitude";
 		final String ATTR_ALTITUDE 		= "altitude";
 		
 		final HashMap<String, String> poiInformation = new HashMap<String, String>();
			
		//= getInfoPlace (i);
		String pp = readPoiInformation (i);
		poiInformation.put(ATTR_ID, pp);
		poiInformation.put(ATTR_NAME, "POI#" + pp);
		poiInformation.put(ATTR_DESCRIPTION, "This is the description of POI#" + pp);
		//double[] poiLocationLatLon = getRandomLatLonNearby(lastKnownLocaton.getLatitude(), lastKnownLocaton.getLongitude());
		// poiInformation.put(ATTR_LATITUDE, String.valueOf(poiLocationLatLon[0]));
		poiInformation.put(ATTR_LATITUDE, "ATTR_LATITUDE" + pp);
		// poiInformation.put(ATTR_LONGITUDE, String.valueOf(poiLocationLatLon[1]));
		poiInformation.put(ATTR_LONGITUDE, "ATTR_LONGITUDE"  + pp);
		final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
		// Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
		poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE) + pp);
 		
		return poiInformation;
	}
	
	void vInsertPoiFromFile ()
	{
		// Opening the file from assets.
		AssetManager assetManager = getAssets();
		InputStream input;
		
		try
		{
			input = assetManager.open("poisFile");
			int size = input.available();
			byte[] buffer = new byte [size];	
			input.read(buffer);
			parsePoiFile(buffer, size);
		}
		catch (IOException e)
		{
			//e.printStackTrace();
			return;
		}
	}
	void parsePoiFile(byte [] buffer, int size)
	{
		// byte [] bytes = buffer.getBytes( Charset.forName("UTF-8" ));
		String szFile = new String( buffer, Charset.forName("UTF-8") );
		

	}
	public int leeRegistro (String szFile, int iRegister)
	{
		int a = szFile.indexOf("#Sample#" + iRegister );
		szFile.indexOf(szFile, a);
		int NexRegister	= szFile.indexOf("#Sample#" + iRegister, a);
		leeCampo (szFile, NexRegister, "#field#:id");
		return NexRegister;
	}
	public int leeCampo (String szRegister, int NexRegister, String field)
	{
		
		String fieldid = "#field#:id";	
		String fieldName = "#field#:name";
		String fieldlat = "#field#:lat";
		String fieldLong = "#field#:long";
		String fieldAlt = "#field#:alt";
		String fieldCountry = "#field#:Country";
		String fieldCity = "#field#:City";
		String fieldDescription = "#field#:description";
		szRegister.indexOf(field, NexRegister);
		return NexRegister;
		
	}
}
