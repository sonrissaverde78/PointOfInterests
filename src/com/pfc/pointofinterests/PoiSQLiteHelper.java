package com.pfc.pointofinterests;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class PoiSQLiteHelper extends SQLiteOpenHelper 
{
	protected 	String 	tableName = "POI";
	private		String 	gdbName;
	private		int 	giVersion;

 	private String 					dbPoi = "dbPoi";
 	private SQLiteDatabase 			db;
 	private PoiSQLiteHelper 		usdbh;
 	
    //Sentencia SQL para crear la tabla de Usuarios
	private String sqlCreate = "CREATE TABLE " + tableName  + " (	iId INTEGER, " 			+
						    										"name TEXT, " 			+
						    										"description TEXT, " 	+ 
						    										"lat INTEGER, " 		+ 
						    										"long INTEGER, " 		+
						    										"alt INTEGER)";
    
    
    public PoiSQLiteHelper(	Context context, String dbName,
    						CursorFactory factory, int iVersion) 
    {
    	super(context, dbName, factory, iVersion);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
        
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
    public void vInitDataBase ()
    {
        // db Opening. Name 'dbPoi' with RW permissions.
 		// db location:
 		// 		/data/data/paquete.java.de.la.aplicacion/databases/nombre_base_datos
 
    	
        db = this.getWritableDatabase();
 
        //If db was properly openned 5 samples are inserted
        if(db != null)
        {
            // 
            for(int i=1; i<=5; i++)
            {
                //Generamos los datos                
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
 
            //Cerramos la base de datos
            db.close();
        }
    	
    }
    public void AddPois ()
    {
    	
    	
    }
}