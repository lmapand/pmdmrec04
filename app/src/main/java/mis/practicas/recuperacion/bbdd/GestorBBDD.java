package mis.practicas.recuperacion.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestorBBDD extends SQLiteOpenHelper
{
   
    public final static String NOMBRE_BBDD="CONFINAMIENTO";
    public final static int VERSION_BBDD=2;
    
    public final static String TABLA_FASES="CREATE TABLE FASES_CONFINAMIENTO" +
            " (ID_FASE INTEGER PRIMARY KEY)";
    
    public final static String TABLA_CARACTERISTICAS="CREATE TABLE CARACTERISTICAS_FASE " +
            "( ID_CARACT INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            "FASE INTEGER NOT NULL REFERENCES FASE_CONFINAMIENTO (ID_FASE) ON DELETE CASCADE ON UPDATE CASCADE,"+
            "TEXTO TEXT,"+
            "ORDEN INTEGER)";
    
    
    
    public final static String TABLA_PROVINCIAS="CREATE TABLE PROVINCIAS " +
            "( ID_PROVINCIA INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "NOMBRE VARCHAR (25) NOT NULL UNIQUE,"+
            "FASE INTEGER REFERENCES FASE_CONFINAMIENTO (ID_FASE) ON DELETE SET NULL ON UPDATE SET NULL,"+
            "REF_IMAGEN varchar(25))";
    
    
    public final static String TABLA_USUARIOS="CREATE TABLE USUARIOS " +
            "(ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NOMBRE VARCHAR (25)," +
            " APELLIDOS VARCHAR (50), " +
            "LOGIN VARCHAR (12)  NOT NULL," +
            "PASS VARCHAR (40)  NOT NULL," +
            " CORREO VARCHAR (120))";
    
    
    public static final String INSERCION_FASES="INSERT INTO FASES_CONFINAMIENTO(ID_FASE) VALUES (0)";
    
    public static final String INSERCION_CARACTERISTICAS="INSERT INTO CARACTERISTICAS_FASE(FASE, TEXTO, ORDEN) VALUES" +
            " (0, 'Bares y restaurantes: Solo se permite que abran para vender comidas para llevar.',0),"+
            " (0, 'Instalaciones científico-técnicas. Se permite la reapertura de las instalaciones científicas que quedaron afectadas por el decreto de estado de alarma',1),"+
            " (0, 'Los mayores de 14 años podrán hacer deporte o pasear en las franjas de 6.00 a 10.00 y de 20.00 a 23.00',2),"+
            " (0, 'Los menores de 14 años podrán salir acompañados de un adulto entre las 12.00 y las 19.00',3),"+
            " (0, 'Los mayores de 70 años podrán hacerlo entre las 10.00 y las 12.00 y entre las 19.00 y las 20.00',4)";
    
    public static final String INSERCION_PROVINCIAS="INSERT INTO PROVINCIAS(NOMBRE, FASE, REF_IMAGEN) VALUES " +
            "('A Coruña',0,'p_a_coruna')," +
            "('Lugo',0,'p_lugo')," +
            "('Ourense',0,'p_ourense')," +
            "('Pontevedra',0,'p_pontevedra')";
    
    
    private SQLiteDatabase bbdd=null;
    
    
    public  GestorBBDD(Context context)
    {
        super(context, GestorBBDD.NOMBRE_BBDD,null,GestorBBDD.VERSION_BBDD );
        
        this.bbdd=this.getWritableDatabase();
        //Borramos la base de datos antes de volver a empezar (esto puede ser un problema)
        //context.deleteDatabase(NOMBRE_BBDD);
    }
    
    
    public SQLiteDatabase getBbdd()
    {
        return this.bbdd;
    }
    
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Creacion de la tabla de Fases
        db.execSQL(GestorBBDD.TABLA_FASES);
        Log.i("CREACION BBDD", "Creando tabla de FASES ");
        //Creacion de la tabla de Provincias
        db.execSQL(GestorBBDD.TABLA_PROVINCIAS);
        Log.i("CREACION BBDD", "Creando tabla de PROVINCIAS ");
        
        //Creacion de la tabla de Caracteristicas de las fases
        db.execSQL(GestorBBDD.TABLA_CARACTERISTICAS);
        Log.i("CREACION BBDD", "Creando tabla de CARACTERISTICAS ");
        
        //Creacion de la tabla de Usuarios
        db.execSQL(GestorBBDD.TABLA_USUARIOS);
        Log.i("CREACION BBDD", "Creando tabla de USUARIOS ");
        
        
        //Insercion de los datos de fases y provincias
        db.execSQL(GestorBBDD.INSERCION_FASES);
        db.execSQL(GestorBBDD.INSERCION_CARACTERISTICAS);
        db.execSQL(GestorBBDD.INSERCION_PROVINCIAS);
       
        Log.i("CREACION BBDD", "INSERTADAS FASES Y PROVINCIAS");
        
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    
    }
}
