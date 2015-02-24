package com.pfc.pointofinterests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class PoiSQLiteHelper extends SQLiteOpenHelper 
{
    //Sentencia SQL para crear la tabla de Usuarios
	public String sqlCreate;
    
    public PoiSQLiteHelper(	Context context, String dbName,
    						CursorFactory factory, int iVersion) 
    {
    	super(context, dbName, factory, iVersion);
    }
    public PoiSQLiteHelper(	Context context, String dbName,
			CursorFactory factory, int iVersion, String szSQLCreate)
	{
    	super(context, dbName, factory, iVersion);
    	sqlCreate = szSQLCreate;
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