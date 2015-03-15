package com.pfc.pointofinterests;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pfc.pointofinterests.PoiSQLiteHelper;
public class PoiDatabase extends SampleCamCaptureScreenActivity 
{
	// Dabase handler and parameters.
	private 	SQLiteDatabase 			db;						// Database handle.
 	private 	String 					gdbName 		= "dbPoi.30";	// Database Name.
 	private 	PoiSQLiteHelper 		dbhPoi;					// DB Object.
	private		int 					giVersionDB 	= 1;		// Database version.
	private		String					tableName		= "tablePoi";	// Database main table of pois.

	final 	String [][] dbFields = 	{
									{"id", 					"INTEGER", "1"},
									{"name", 				"TEXT", "1"},
									{"lat", 				"TEXT", "1"},
									{"long", 				"TEXT", "1"},
									{"alt", 				"TEXT", "1"},
									{"Country", 			"TEXT", "0"},
									{"City", 				"TEXT", "1"},
									{"description", 		"TEXT", "0"},
									{"ImagesToTrack", 		"TEXT", "1"},
									{"ImagesToDraw", 		"TEXT", "0"},
									{"ImagesButtons", 		"TEXT", "1"},
									{"MainImage", 			"TEXT", "1"},
									{"UrlWikipedia", 		"TEXT", "0"},
									};

	final 	int 	[] dbPrimaryKyes = {0,1};

	final 	String tockenField			= "#field#:";
	final 	String tockenStartfield		= "=!=";
	final 	String tockenEndfield		= "$#";
	// Database fields used for sql sentences.
	final public String sqlCountColums 	= "SELECT COUNT(*) FROM information_schema.columns " +  
												"WHERE table_name = "+ "'" + tableName + "'";
	
	final String sqlExistTable   = "SELECT name FROM sqlite_master WHERE type='table' AND name='tablePoi'";
	final String sqlSelectTables = "SELECT name from sqlite_master where type = 'table'";
	final String sqlDropTable 	 = "DROP TABLE";
	final String CountReg = "SELECT COUNT(*) FROM " + tableName;
	
	
	private int 	iTotalPois 			= 0;
	private	int		iTotalFields		= 0;	


 	String	ImagesToDraw	= "assets/ImagesToDraw/";
 	String	ImagesToTrack	= "assets/ImagesToTrack/";
 	String	ImagesButtons	= "assets/buttons/";
 	
 	private void vCreateDatabase ()
	{
		String TableStructure = SQLMakeCreateTable (tableName, dbFields, dbPrimaryKyes, iTotalFields);
		//public String sqlCreate2 = "CREATE TABLE " + tableName  + " " + TableStructure;
		dbhPoi = 
 	            new PoiSQLiteHelper(this, gdbName, null, giVersionDB, TableStructure); // RW Opened. 
	}
	private void vInitDataBase ()
	{
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
				    	 if (szTableName.equals("android_metadata"))
				    	 {
				    	 	SQLexecute (sqlDropTable + " " + szTableName);
				    	 }
				     } while(c.moveToNext() != false);
				}
				SQLexecute (dbhPoi.sqlCreate);
			}
			db.close();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		szExc = e.toString();
    	} 
	}
	// iSetColsDatabase ().
	private int iGetColsDatabase ()
	{		
		int iFields;
		iFields = dbFields.length;
		return iFields;
	}
	private int iGetRegDatabase ()
	{
		int count = 0;
		String s;
		try
		{
			db = dbhPoi.getReadableDatabase();
			if (db != null)
			{
			    
			    SQLiteStatement statement = db.compileStatement(CountReg);
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

	private String [][]  vSelecAllSamplesDB ()
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
	
	private int iGetTotalFieldsDB ()
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
		String szSQLInsertFields;
		String szSQLInsertValues = SQLMakeString(szPoiInfo, iTotalFields, true, true);
		String cols[] = null;
		cols = getCols();
		szSQLInsertFields = SQLMakeString(cols, iTotalFields, false, true);
		String szSQLInsertIntoPOItable = "INSERT INTO " + tableName + " " + szSQLInsertFields +  
										" VALUES " + szSQLInsertValues; 

		SQLexecute(szSQLInsertIntoPOItable);
	}
	private String [] getCols ()
	{
		String cols[] = new String [iTotalFields];
		for (int i = 0; i < iTotalFields; i++)
		{
			cols[i] = dbFields[i][0];
		}
		return cols;
	}
	private Exception SQLexecute (String szSQL)
	{
		Exception exc = null;
		try
		{
			db.execSQL(szSQL);
		}catch(Exception e)
		{
			exc = e;
		}
		return (exc);
	}
	private String SQLMakeString (String [] szPoiInfo,  int iTotalCols, boolean values, boolean parentesis)
	{
		String szSQLMakeString = " ";
		if (iTotalCols <= 0)
		{
			szSQLMakeString = "";
		}
		else 
		{
			String szOpenParentesis = 	" ( ";
			String szCloseParentesis = 	" ) ";
			if (parentesis == false)
			{
				szOpenParentesis 	= 	"";
				szCloseParentesis 	= 	"";	
			}
			
			if (iTotalCols < 2)
			{
				if (values == false)
					szSQLMakeString = szSQLMakeString +  szOpenParentesis + szPoiInfo[0] + szCloseParentesis;
				else
					szSQLMakeString = szSQLMakeString +  szOpenParentesis + "'" + "'"+ szPoiInfo[0] + "'" +  szCloseParentesis;
			}
			else 
			{
				int i = 0;
				szSQLMakeString = szOpenParentesis;
				for ( i = 0; i < iTotalFields - 1; i++)
				{
					if (values == false)
						szSQLMakeString = szSQLMakeString + (szPoiInfo[i] + ", ");
					else
						szSQLMakeString = szSQLMakeString + ("'" + szPoiInfo[i] + "'" + ", ");
				}
				if (values == false)
					szSQLMakeString = szSQLMakeString + (szPoiInfo[i] + szCloseParentesis);
				else
					szSQLMakeString = szSQLMakeString + ("'" + szPoiInfo[i] + "'" +  szCloseParentesis);
			}
		}
		return szSQLMakeString;

	}
	private String SQLMakeCreateTable (String table, String [][] databaseFieldsType,  int [] iPrimarieKeys,int iTotalCols)
	{
		String szSQLMakeString = " ";
		if (iTotalCols <= 0)
		{
			szSQLMakeString = "";
		}
		else 
		{
			szSQLMakeString = "CREATE TABLE " + tableName;
			if (iTotalCols < 2)
			{
				szSQLMakeString = szSQLMakeString + (" ( " + databaseFieldsType[0][0] + " " + databaseFieldsType[0][1] + " ) ");	
			}
			else 
			{
				int i = 0;
				szSQLMakeString = szSQLMakeString + (" ( ");
				for ( i = 0; i < iTotalFields - 1; i++)
				{
					szSQLMakeString = szSQLMakeString + (databaseFieldsType[i][0] + " " + databaseFieldsType[i][1] + ", ");
				}
				int iTotalFieldsPrimeryKeys = iPrimarieKeys.length;
				if (iTotalFieldsPrimeryKeys <= 0)
					szSQLMakeString = szSQLMakeString + (databaseFieldsType[i][0] + " " + databaseFieldsType[i][1] +  " ) ");
				else
				{
					szSQLMakeString = szSQLMakeString + (databaseFieldsType[i][0] + " " + databaseFieldsType[i][1] +  ", ");
					if (iTotalFieldsPrimeryKeys == 1)
					{
						szSQLMakeString = szSQLMakeString + (" PRIMARY KEY ( " + databaseFieldsType[iPrimarieKeys[i]][0] + " ) )");	
						
					}
					else
					{
						szSQLMakeString = szSQLMakeString + ( " PRIMARY KEY (");
						for ( i = 0; i < iTotalFieldsPrimeryKeys - 1; i++)
						{
							szSQLMakeString = szSQLMakeString + ( databaseFieldsType[iPrimarieKeys[i]][0] + ", ");
						}
						szSQLMakeString = szSQLMakeString + (databaseFieldsType[iPrimarieKeys[i]][0] +  " ))");
					}
					
				}
					
			}
			
		}
		return szSQLMakeString;

	}
	private String SelectImagesFromPoi(int PoiId)
	{
		
		String Select = "SELECT " + "ImagesToDraw" + " FROM " +  tableName + " WHERE " + dbFields[0][0] + " = " + PoiId;
		
		String PoiInfImagesToDraw;
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
		PoiInfImagesToDraw = "";
		if (c != null)
		{
			int iField = 0;
			if (c.moveToFirst()) 
			{
			     //Until the end of samples
				try
				{
					PoiInfImagesToDraw 	= c.getString(0);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					s = e.toString();	
				}
			}
		}
		
		db.close();
		return PoiInfImagesToDraw;			
	}
	private String[][] readPoiInformation (int id)
	{
		
		String[] cols = getCols();
		String szFieldsDB = SQLMakeString(cols, iTotalFields, false, false);
		String Select = "SELECT " + szFieldsDB + " FROM " +  tableName + " WHERE " + dbFields[0][0] + " = " + id;
		
		String[][] PoiInf = new String[iTotalFields][2];
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
			     //Until the end of samples
				try
				{
				     do 
				     {
				    	 PoiInf[iField][0] 	= cols[iField];
				    	 PoiInf[iField][1] 	= c.getString(iField);
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
		String 	szFile 		= new String( buffer); // , Charset.forName("UTF-8") 
		int 	i 			= 0; 
		int 	iRes 		= 0;
		// Sample are extracted from database.
		do
		{
			
			iRes = AddSampleToDBFromFile (szFile, i + 1);
			i++;
		}while (iRes > -1);
		return i - 1;
	}

	private int AddSampleToDBFromFile (String szFile, int iRegister)
	{
		int iSamplePos 			= szFile.indexOf("#Sample#" + iRegister );
		if (iSamplePos >= 0)
		{
			// There are data in the file...
			String [] szPoiInfo = new String [iTotalFields]; 
			for (int i = 0; i < iTotalFields; i++)
			{
				szPoiInfo[i] 		= readFieldFromFileCampo (szFile, iSamplePos, dbFields[i][0]); // fieldId);
			}
			SQLInsertPoi ( szPoiInfo);
		}		
		return iSamplePos;

	}
	private String readFieldFromFileCampo (String szSample, int iSample, String tockenIdfield)
	{
		int iFieldPos 				= szSample.indexOf (tockenField + 		tockenIdfield, iSample);
		int iStartValueOfFieldPos 	= szSample.indexOf (tockenStartfield, 	iFieldPos);
		int iEndValueOfFieldPos 	= szSample.indexOf (tockenEndfield, 	iFieldPos);
		iStartValueOfFieldPos	 = iStartValueOfFieldPos + 4;
		
		String  szStringValue = szSample.substring(iStartValueOfFieldPos, iEndValueOfFieldPos);
		return szStringValue;
	}
	///////////////////////////////////////////////////////////////////////////
	// SampleCamCaptureScreenActivity.java abstract functions.
	///////////////////////////////////////////////////////////////////////////
	@Override
	// vInitPlaces () create and open a database in write mode to 
	// allow the insertion of registers.
 	public int iInitPlaces ()
	{
		iTotalFields = iGetColsDatabase ();
 		// iTotalFields = iGetTotalFieldsDB()
 		vCreateDatabase ();
 		iTotalPois = iGetRegDatabase();
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
	public String [] szPoiInfo		(int IdPoi)
 	{
		String [] PoiInfo = new String [iTotalFields];
		String [] [] TotalPoiInfo = new String [iTotalFields][2];
		TotalPoiInfo = readPoiInformation (IdPoi);
		for (int i=0; i< iTotalFields;i++)
		{
			PoiInfo[i] = TotalPoiInfo[i][1];
		}
		return PoiInfo;
 	}
	public String [][] szPoiInfoInGeoAR		(int IdPoi)
 	{
		
		String [][] POI_Info = readPoiInformation(IdPoi);
		int iTotalFieldsInGeoAR = 0;
		for (int i=0; i < iTotalFields;i++)
		{
			if (dbFields[i][2] == "1")
			{
				iTotalFieldsInGeoAR++;
			}
		}
		String [][] POI_GeoAr = new String [iTotalFieldsInGeoAR][2];
		int j=0;
		for (int i=0; i< iTotalFields;i++)
		{
			if (dbFields[i][2] == "1")
			{
				POI_GeoAr [j] = POI_Info[i];
				j++;
			}
		}
		return POI_GeoAr;
 	}
	
	public int iSetTotalPoiFields()
	{
		return iTotalFields;
	}
	public String[] szPoiNameFields()
	{
		// TODO Auto-generated method stub
		String[] cols = getCols();
		return cols;
	}

 	public String	szPathForButtons		()
 	{
 		return ImagesButtons;
 	}

 	public String	szPathForImagesToDraw	()
 	{
 		return ImagesToDraw;
 	}
 	public String	szPathForImagesToTrack	()
 	{
 		return ImagesToTrack;
 	}
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////	
 	///////////////////////////////////////////////////////////////////////////


    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
	     Bitmap bm = null;
	     
	     // First decode with inJustDecodeBounds=true to check dimensions
	     final BitmapFactory.Options options = new BitmapFactory.Options();
	     options.inJustDecodeBounds = true;
	     
	     ////////////////////////////////////////////////////////////////////////
	     ////////////////////////////////////////////////////////////////////////
	     Rect outPadding = null;
	     // outPadding
	     //  BitmapFactory.decodeFile(path, options);
	     
	     // Calculate inSampleSize
	    AssetManager assetManager = getAssets();
	    
	    InputStream istr = null;
	    Bitmap bitmap = null;
	    try {
	        istr = assetManager.open(path);
	        //bitmap = BitmapFactory.decodeStream(istr);
	        bitmap = BitmapFactory.decodeStream(istr, outPadding, options);
	    } catch (IOException e) {
	        // handle exception
	    }
	    ////////////////////////////////////////////////////////////////////////
	    ////////////////////////////////////////////////////////////////////////
	     options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	     
	     // Decode bitmap with inSampleSize set
	     options.inJustDecodeBounds = false;
		    try {
		    	istr.close();
		        istr = assetManager.open(path);
		        //bitmap = BitmapFactory.decodeStream(istr);
		        bitmap = BitmapFactory.decodeStream(istr, outPadding, options);
		    } catch (IOException e) {
		        // handle exception
		    }
	     //bm = BitmapFactory.decodeFile(path, options); 
	     bm = bitmap;
	     return bm;  
	
  }
  public static Bitmap getBitmapFromAsset(Context context, String filePath) {
    AssetManager assetManager = context.getAssets();

    InputStream istr;
    Bitmap bitmap = null;
    try {
        istr = assetManager.open(filePath);
        bitmap = BitmapFactory.decodeStream(istr);
    } catch (IOException e) 
    {
        // handle exception
    }

    return bitmap;
}
  String [] list;
  
public View insertPhoto(int iPoi)
{
	String path = "ImagesOfPois/";
	path = path + list[iPoi];
	
	Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
	  
	LinearLayout layout = new LinearLayout(getApplicationContext());
	layout.setLayoutParams(new LayoutParams(250, 250));
	layout.setGravity(Gravity.CENTER);
	  
	ImageView imageView = new ImageView(getApplicationContext());
	imageView.setLayoutParams(new LayoutParams(220, 220));
	imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	imageView.setImageBitmap(bm);
	  
	layout.addView(imageView);
	return layout;
}
@Override
public View insertPhoto2(String szPoi)
{
	String path = "ImagesOfPois/";
	path = path + szPoi;
	
	Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
	  
	LinearLayout layout = new LinearLayout(getApplicationContext());
	layout.setLayoutParams(new LayoutParams(250, 250));
	layout.setGravity(Gravity.CENTER);
	  
	ImageView imageView = new ImageView(getApplicationContext());
	imageView.setLayoutParams(new LayoutParams(220, 220));
	imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	imageView.setImageBitmap(bm);
	  
	layout.addView(imageView);
	return layout;
}

@Override
public String [] getListOfImages(int iPoi)
{
	String szImagesNameOfPoi 	= SelectImagesFromPoi(iPoi);
	String arrayImagesNameOfPoi[] = szImagesNameOfPoi.split(",");
	return arrayImagesNameOfPoi;
}
@Override
public View[] vLoadImages (int PoiId)
{
	String szImagesNameOfPoi 	= SelectImagesFromPoi(PoiId);
	View viewImagesToDraw[] 	= new View [iTotalImages];
	String arrayImagesNameOfPoi[] = szImagesNameOfPoi.split(",");
	for (int i = 0; i < arrayImagesNameOfPoi.length; i++)
	{
		viewImagesToDraw [i] = insertPhoto(arrayImagesNameOfPoi[i]);
	}
	return viewImagesToDraw;
}
public View insertPhoto(String szPoi)
{
	
	String path = "ImagesOfPois/";
	path = path + szPoi;

	Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);
	  
	LinearLayout layout = new LinearLayout(getApplicationContext());
	layout.setLayoutParams(new LayoutParams(250, 250));
	layout.setGravity(Gravity.CENTER);
	  
	ImageView imageView = new ImageView(getApplicationContext());
	imageView.setLayoutParams(new LayoutParams(220, 220));
	imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	imageView.setImageBitmap(bm);
	  
	layout.addView(imageView);
	return layout;
}
 
   public int calculateInSampleSize(
   	      
   	     BitmapFactory.Options options, int reqWidth, int reqHeight) {
   	     // Raw height and width of image
   	     final int height = options.outHeight;
   	     final int width = options.outWidth;
   	     int inSampleSize = 1;
   	        
   	     if (height > reqHeight || width > reqWidth) {
   	      if (width > height) {
   	       inSampleSize = Math.round((float)height / (float)reqHeight);   
   	      } else {
   	       inSampleSize = Math.round((float)width / (float)reqWidth);   
   	      }   
   	     }
   	     
   	     return inSampleSize;   
   	    }
   
   
   public int TotalInitialImages()
   {
	   String path = "ImagesOfPois"; 

	    try {
	        list = getAssets().list(path);
	       return list.length;
	    } catch (IOException e) {
	        return 0;
	    }
   }



   
   //////////////////////////////////////////////////////////////////////////
   //////////////////////////////////////////////////////////////////////////
}
