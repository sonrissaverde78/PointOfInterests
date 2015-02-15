package com.pfc.pointofinterests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class PoiSQLiteHelper extends SQLiteOpenHelper 
{
	final 	String dbFieldId 			= "id";
	final 	String dbFieldName 			= "name";
	final 	String dbFieldLat 			= "latitude";
	final 	String dbFieldLong 			= "longtude";
	final 	String dbFieldAlt 			= "altitude";
	final 	String dbFieldCountry 		= "Country";
	final 	String dbFieldCity 			= "City";
	final 	String dbFieldDescription 	= "description";
	
	final	private		String					tableName	= "tablePoi";
	
    //Sentencia SQL para crear la tabla de Usuarios
	final public String sqlCreate2 = "CREATE TABLE " + tableName  + " ( " +
			 dbFieldId + 			" INTEGER, " 	+
			 dbFieldLat + 			" INTEGER, " 	+ 
			 dbFieldLong + 			" INTEGER, " 	+ 
			 dbFieldAlt + 			" INTEGER, " 	+ 
			 dbFieldName + 			" TEXT, " 		+
			 dbFieldCountry + 		" TEXT, " 		+ 
			 dbFieldCity + 			" TEXT, " 		+
			 dbFieldDescription + 	" TEXT" 		+//, " 		+
			 // "PRIMARY KEY " + 		" ( " + dbFieldId + " ) "+
			 ")";	
    //Sentencia SQL para crear la tabla de Usuarios
	final public String sqlCreate = "CREATE TABLE " + tableName  + " ( " +
			 dbFieldId + 			" INTEGER, " 	+
			 dbFieldLat + 			" INTEGER, " 	+ 
			 dbFieldLong + 			" INTEGER, " 	+ 
			 dbFieldAlt + 			" INTEGER, " 	+ 
			 dbFieldName + 			" TEXT NOT NULL, " 		+
			 dbFieldCountry + 		" TEXT, " 		+ 
			 dbFieldCity + 			" TEXT, " 		+
			 dbFieldDescription + 	" TEXT, " 		+//, " 		+
			 "PRIMARY KEY " + 		" ( " + dbFieldId + ", " + dbFieldName + " ) "+
			 ")";	
	
	//Sentencia SQL para crear la tabla de Usuarios
	
	String sqlCreate_ejemplo = "CREATE TABLE Usuarios (codigo INTEGER, nombre TEXT)";
    
    public PoiSQLiteHelper(	Context context, String dbName,
    						CursorFactory factory, int iVersion) 
    {
    	super(context, dbName, factory, iVersion);
    	//sqlCreate = sqlCreateTable;
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql sentence for table cration.
    	try
    	{
    		db.execSQL(sqlCreate);
    	}
    	catch (Exception e)
    	{
    		String s = e.toString();
    	}
        
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
 
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS INTEGER");
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}