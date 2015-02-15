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
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.pfc.pointofinterests.PoiSQLiteHelper;
public class PoiDatabase extends SampleCamCaptureScreenActivity 
{
	// Dabase handler and parameters.
	private 	SQLiteDatabase 			db;						// Database handle.
 	private 	String 					gdbName 	= "dbPoi29";	// Database Name.
 	private 	PoiSQLiteHelper 		dbhPoi;					// DB Object.
	private		int 					giVersion 	= 1;		// Database version.
	private		String					tableName	= "tablePoi";	// Database main table of pois.
	
	// File & JSON Fields for wikitude engine.
	final 	String fieldId 				= "#field#:id";
	final 	String fieldName 			= "#field#:name";
	final 	String fieldLat 			= "#field#:lat";
	final 	String fieldLong 			= "#field#:long";
	final 	String fieldAlt 			= "#field#:alt";
	final 	String fieldCountry 		= "#field#:Country";
	final 	String fieldCity 			= "#field#:City";
	final 	String fieldDescription 	= "#field#:description";
	final 	String tockenStartfield		= "=!=";
	final 	String tockenEndfield		= "$#";

	
	private 	int 					iTotalPois 	= 0;
	// Database fields used for sql sentences.
	final 	String dbFieldId 			= "id";
	final 	String dbFieldName 			= "name";
	final 	String dbFieldLat 			= "latitude";
	final 	String dbFieldLong 			= "longtude";
	final 	String dbFieldAlt 			= "altitude";
	final 	String dbFieldCountry 		= "Country";
	final 	String dbFieldCity 			= "City";
	final 	String dbFieldDescription 	= "description";
	
	final public String sqlCountColums 	= "SELECT COUNT(*) FROM information_schema.columns " +  
												"WHERE table_name = "+ "'" + tableName + "'";
	
	private	int		iTotalFields		= 0; 
	final	String	[] databaseCols		= {	dbFieldId,  dbFieldLat, dbFieldLong, dbFieldAlt, 
											dbFieldName, dbFieldCountry, dbFieldCity, dbFieldDescription}; 

	final	String szFieldsDB = 	dbFieldId + 	", " + dbFieldLat + 		", " + 
									dbFieldLong +	", " + dbFieldAlt + 		", " + 
									dbFieldName + 	", " + dbFieldCountry + 	", " + 
									dbFieldCity + ", " + dbFieldDescription;
	private String szFieldsDB2 = "";
	final	String	[] databaseCols2	= {	 "id", "name", "latitude", "longtude", 
											"altitude", "Country", "City", "description"};
	 
	// SQL sentence table delete.
	final String sqlExistTable   = "SELECT name FROM sqlite_master WHERE type='table' AND name='tablePoi'";
	final String sqlSelectTables = "SELECT name from sqlite_master where type = 'table'";
	final String sqlDropTable 	 = "DROP TABLE";
	// vCreateDatabase ().
	public int iSetColsDatabase ()
	{
		
		int iFields;
		iFields = databaseCols.length;
		
		for (int i= 0; i < iTotalFields; i++)
		{
			if (i == iTotalFields -1)
				szFieldsDB2 = databaseCols[i];
			else 
				szFieldsDB2 = databaseCols[i] + ", ";
		}
		return iFields; 
	}
	public int iGetRegDatabase ()
	{
		int count = 0;
		String s;
		try
		{
			db = dbhPoi.getReadableDatabase();
			if (db != null)
			{
			    String sql = "SELECT COUNT(*) FROM " + tableName;
			    SQLiteStatement statement = db.compileStatement(sql);
			    count = (int) statement.simpleQueryForLong();
			    db.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			s = e.toString();
		}
	    return count;

	}
	public void vCreateDatabase ()
	{
		dbhPoi = 
 	            new PoiSQLiteHelper(this, gdbName, null, giVersion); // RW Opened. 
	}
	public void vInitDataBase ()
	{
		//ejmplo_db();	
    	String szTableName = "";
    	String szExc = "";
    	try
    	{
    		db = dbhPoi.getWritableDatabase();
    		Cursor c = null;
			c = db.rawQuery( sqlSelectTables, null);
			if (c != null)
			{
				// End of pointer cursor checked.
				if (c.moveToFirst()) 
				{
				     //Recorremos el cursor hasta que no haya más registros
				     do 
				     {
				    	 szTableName = c.getString(0);
				    	 if (szTableName.equals("android_metadata"));
				    	 	db.execSQL(sqlDropTable + " " + szTableName);
				     } while(c.moveToNext() != false);
				}
		    	try
		    	{
		    		db.execSQL(dbhPoi.sqlCreate);
		    	}
		    	catch (Exception e)
		    	{
		    		String s = e.toString();
		    	}
			}
			db.close();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		szExc = e.toString();
    	} 
	}
	public void ejmplo_db ()
	{
        //Abrimos la base de datos 'DBUsuarios' en modo escritura
		PoiSQLiteHelper usdbh =
            new PoiSQLiteHelper(this, "DBUsuarios", null, 1);
 
        SQLiteDatabase db = usdbh.getWritableDatabase();
 
        //Si hemos abierto correctamente la base de datos
        if(db != null)
        {
            //Insertamos 5 usuarios de ejemplo
            for(int i=1; i<=5; i++)
            {
                //Generamos los datos
                int codigo = i;
                String nombre = "Usuario" + i;
 
                //Insertamos los datos en la tabla Usuarios
                db.execSQL("INSERT INTO Usuarios (codigo, nombre) " +
                           "VALUES (" + codigo + ", '" + nombre +"')");
            }
 
            //Cerramos la base de datos
            db.close();
        }		
	}
	@Override
	// vInitPlaces () create and open a database in write mode to 
	// allow the insertion of registers.
 	public int iInitPlaces ()
	{
 		vCreateDatabase ();
 		iTotalFields = iGetTotalFields();
 		iTotalPois = iGetRegDatabase();
 		vSelecAllSamplesDB ();

 		// iTotalFields = iGetTotalFields (); // Database select for count fields on table does not work
 		iTotalFields = iSetColsDatabase ();
        // db Opening. Name 'dbPoi' with RW permissions.
 		// db location:
 		// 		/data/data/paquete.java.de.la.aplicacion/databases/nombre_base_datos
		// Database  is opened with W mode. 		
        db = dbhPoi.getWritableDatabase();    
        //If db was properly opened
        if(db != null)
        {
        	iTotalPois = iInsertPoisFromFile();
            // Database is closed.
            db.close();
        }
        iGetRegDatabase();
        vSelecAllSamplesDB ();
        return iTotalPois;
	}
	
	public String [][]  vGetAllDataFromDB ()
	{
		String [][] szPoisArray = new String [iTotalPois][iTotalFields];
        for (int i = 0; i < iTotalFields; i++)
        {
        	szPoisArray [i] = readPoiInformation (i + 1);
        }
        return szPoisArray;
	}

	public String [][]  vSelecAllSamplesDB ()
	{
		String Select = "SELECT *" + " FROM " +  tableName;
		
		String[][] PoiInf = new String[iTotalPois + 10] [iTotalFields];
		Cursor c = null;
		String s = "";
		try
		{
			db = dbhPoi.getReadableDatabase();
			// sql select is sent
			c = db.rawQuery( Select, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			s = e.toString();
		}
		if (c != null)
		{

			if (c.moveToFirst()) 
			{
				int iIndex = 0;
				do
				{
					int iField = 0;
				     //Read the samples until c.moveNext is false
					try
					{
					     do 
					     {
					          // String codigo= c.getString(0);
					    	 PoiInf[iIndex][iField] = c.getString(iField);
					    	 iField++;
					     } while(iField < iTotalFields);
					}
					
					catch (Exception e)
					{
						e.printStackTrace();
						s = e.toString();	
					}
					iIndex++;
				}while (c.moveToNext() == true);
			}
		}
		if (db != null)
			db.close();
		return PoiInf;	
	}
	
	public String [] szPoiInfo		(int IdPoi)
 	{
		return readPoiInformation(IdPoi);
 	}
	
	@Override
	public int iSetTotalPoiFields()
	{
		return iTotalFields;
	}
	private int iGetTotalFields ()
	{
		Cursor c = null;
		String s = "";
		int iTotalCols = 0;	
		String [] szCols  = new String [8];
		try
		{
			db = dbhPoi.getReadableDatabase();
			c = db.rawQuery("PRAGMA table_info( "+ tableName + " )", null);
			if (c != null)
			{
				if ( c.moveToFirst() ) 
				{
				    do {
				        // System.out.println("col: " + c.getString(1));
				    	szCols[iTotalCols] = c.getString(1);
				        iTotalCols++;
				    } while (c.moveToNext());
				}
			}
			/*
			db = dbhPoi.getReadableDatabase();
			db.execSQL(sqlCountColums);
			DatabaseUtils.queryNumEntries(db, tableName);
			DatabaseUtils.qu
			c = db.rawQuery( sqlCountColums, null);
			*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
			s = e.toString();
		}
		if (db != null)
			db.close();
		return (iTotalCols);
	}
	private void SQLInsertPoi (String [] szPoiInfo)
	{
		String szSQLInsertIntoPOItable = "INSERT INTO " + tableName + " " + 
				"( " + 	dbFieldId + ", " + dbFieldLat + ", " + dbFieldLong + ", " + dbFieldAlt + ", " +
				dbFieldName + ", " + dbFieldCountry + ", " + dbFieldCity + ", " + dbFieldDescription + 
				" ) " +
				" VALUES " +
				"( " +
				szPoiInfo[0] + 			", " +  
				szPoiInfo[1] + 			", " + 
				szPoiInfo[2] + 			", " + 
				szPoiInfo[3] + 			", " +
				"'" + szPoiInfo[4] + 		"'" + ", " +
				"'" + szPoiInfo[5] + 		"'" + ", " +
				"'" + szPoiInfo[6] + 		"'" + ", " +
				"'" + szPoiInfo[7] + 		"'" +
				")";
		String szSQLInsertIntoPOItable2 = "INSERT INTO " + tableName + " " + 
				"( " + 	
				// dbFieldId + ", " + 
				dbFieldLat + ", " + 
				dbFieldLong + ", " + 
				dbFieldAlt + ", " +
				dbFieldName + ", " + 
				dbFieldCountry + ", " + 
				dbFieldCity + ", " + 
				dbFieldDescription + 
				" ) " +
				" VALUES " +
				"( " +
				// szPoiInfo[0] + 			", " +  
				szPoiInfo[1] + 			", " + 
				szPoiInfo[2] + 			", " + 
				szPoiInfo[3] + 			", " +
				"'" + szPoiInfo[4] + 		"'" + ", " +
				"'" + szPoiInfo[5] + 		"'" + ", " +
				"'" + szPoiInfo[6] + 		"'" + ", " +
				"'" + szPoiInfo[7] + 		"'" +
				")";
		try
		{
			db.execSQL(szSQLInsertIntoPOItable);
		}catch(Exception e)
		{
			String Exc = e.toString();
			
		}		
	}
	private String[] readPoiInformation (int id)
	{

		String Select = "SELECT " + szFieldsDB + " FROM " +  tableName + " WHERE " + dbFieldId + " = " + id;
		
		String[] PoiInf = new String[iTotalFields];
		Cursor c = null;
		String s = "";
		try
		{
			db = dbhPoi.getReadableDatabase();
			// sql select is sent
			c = db.rawQuery( Select, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			s = e.toString();
		}
		if (c != null)
		{
			int iField = 0;
			if (c.moveToFirst()) 
			{
			     //Recorremos el cursor hasta que no haya más registros
				try
				{
				     do 
				     {
				          // String codigo= c.getString(0);
				    	 PoiInf[iField] = c.getString(iField);
				    	 iField++;
				     } while(iField < iTotalFields);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					s = e.toString();	
				}
			}
		}
		
		db.close();
		return PoiInf;	
	}

	public HashMap<String, String> JSonPoiInformation (int i)
	{ 		
 		final HashMap<String, String> poiInformation = new HashMap<String, String>();
			
		//= getInfoPlace (i);
		String pp[] = readPoiInformation (i);
		int iField = 4;
		poiInformation.put(fieldId, pp[iField]);
		poiInformation.put(fieldName, "POI#" + pp[iField]);
		poiInformation.put(fieldDescription, "This is the description of POI#" + pp[iField]);
		//double[] poiLocationLatLon = getRandomLatLonNearby(lastKnownLocaton.getLatitude(), lastKnownLocaton.getLongitude());
		// poiInformation.put(fieldLat, String.valueOf(poiLocationLatLon[0]));
		poiInformation.put(fieldLat, "fieldLat" + pp[iField]);
		// poiInformation.put(fieldLong, String.valueOf(poiLocationLatLon[1]));
		poiInformation.put(fieldLong, "fieldLong"  + pp[iField]);
		final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
		// Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
		poiInformation.put(fieldAlt, String.valueOf(UNKNOWN_ALTITUDE) + pp[iField]);
 		
		return poiInformation;
	}

	
	private int iInsertPoisFromFile ()
	{
		// Opening the file from assets.
		AssetManager assetManager = getAssets();
		InputStream input;
		int iTotalPois = 0;
		try
		{
			input = assetManager.open("poisFile");
			int size = input.available();
			byte[] buffer = new byte [size];	
			input.read(buffer);
			iTotalPois = iParsePoiFile(buffer, size);
		}
		catch (IOException e)
		{
			//e.printStackTrace();
			return 0;
		}
		return iTotalPois;
	}
	
	private int iParsePoiFile(byte [] buffer, int size)
	{
		// byte [] bytes = buffer.getBytes( Charset.forName("UTF-8" ));
		String szFile = new String( buffer); // , Charset.forName("UTF-8") 
		int i;
		for ( i = 0; i < 4; i++ )
		{
			AddSampleToDBFromFile (szFile, i + 1);
		}
		return i;
	}
	enum fields
	{
		eId,
		eLat,
		eLong,
		eAlt,
		eName,
		eCity,
		eCountry,
		eDescription,
		maxFields
	}

	public void AddSampleToDBFromFile (String szFile, int iRegister)
	{
		int iSamplePos 			= szFile.indexOf("#Sample#" + iRegister );
		String [] szPoiInfo = new String [iTotalFields]; 
		szPoiInfo[0] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldId);
		szPoiInfo[1] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldLat);
		szPoiInfo[2] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldLong);
		szPoiInfo[3] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldAlt);
		szPoiInfo[4] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldName);
		szPoiInfo[5] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldCountry);
		szPoiInfo[6] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldCity);
		szPoiInfo[7] 		= readFieldFromFileCampo (szFile, iSamplePos, fieldDescription);
		SQLInsertPoi ( szPoiInfo);

	}

	
	public String readFieldFromFileCampo (String szSample, int iSample, String tockenfield)
	{

		int iFieldPos = szSample.indexOf (tockenfield, iSample);
		int iStartValueOfFieldPos = szSample.indexOf(tockenStartfield, iFieldPos);
		int iEndValueOfFieldPos = szSample.indexOf(tockenEndfield, iFieldPos);
		iStartValueOfFieldPos = iStartValueOfFieldPos + 4;
		
		int iLengthField = iEndValueOfFieldPos - iStartValueOfFieldPos - 1;
		char tcValue[] = new char [iLengthField];
		
		String  szStringValue = szSample.substring(iStartValueOfFieldPos, iEndValueOfFieldPos);
		// szSample.getChars(iStartValueOfFieldPos, iEndValueOfFieldPos, tcValue, 0);
		//String szStringValue = tcValue.toString();
		return szStringValue;
	}
	@Override
	public String[] szPoiNameFields()
	{
		// TODO Auto-generated method stub
		return databaseCols;
	}
}
