package mis.practicas.recuperacion.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Map;

public class GestorProvincias
{
    
    private final SQLiteDatabase bbdd;
    private final Map<Integer,FaseDesconfinamiento> fasesDesconfinamiento;
    
    public GestorProvincias(SQLiteDatabase bbdd, Map<Integer, FaseDesconfinamiento>fasesDesconfinamineto)
    {
        this.bbdd=bbdd;
        this.fasesDesconfinamiento=fasesDesconfinamineto;
    }
    
    /** Obtiene la lista de provincias
     *
     * @param context COntexto de app, necesario para analizar los recursos gráficos
     * @return Provinicia[] listado de las Provincias utilizadas por la Aplicacion
     */
    public Provincia[] cargaProvincias(Context context)
    {
        String txtConsulta="SELECT NOMBRE, FASE, REF_IMAGEN FROM PROVINCIAS";
        Cursor cursor=this.bbdd.rawQuery(txtConsulta,null);
        Provincia[] provincias=new Provincia[cursor.getCount()];
        int x=0;
        if(cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                //Creamos la Provincia y establecemos la fase
                Provincia unaProvincia=new Provincia(cursor.getString(cursor.getColumnIndex("NOMBRE")),
                        context.getResources().getIdentifier(cursor.getString(cursor.getColumnIndex("REF_IMAGEN")), "drawable", context.getPackageName()));
                unaProvincia.setFaseDesconfinamiento( this.fasesDesconfinamiento.get(cursor.getInt(cursor.getColumnIndex("FASE"))));
                
                provincias[x++] =unaProvincia;
               
                cursor.moveToNext();
            }
            cursor.close();
        }
        
        return provincias;
    }
    
    
    /** Asigna una Fase a una Provincia
     *
     * @param provincia Provincia a la que asignar la fase
     * @param faseAsignar FaseDesconfinamiento a asignar a la Provincia
     */
    public boolean asignaFaseAProvincia(Provincia provincia, FaseDesconfinamiento faseAsignar)
    {
    
        //Realizar la asignacion interna:
        
        ContentValues valores = new ContentValues();
    
        valores.put("FASE",String.valueOf(faseAsignar.getIdFase()));
        String parametrosCondicion="NOMBRE=?";
        String[] valoresCondicion =new String[]{String.valueOf(provincia.getNombre())};
        try
        {
            this.bbdd.update("PROVINCIAS", valores, parametrosCondicion, valoresCondicion);
            //Si no es capaz de actualizar en la base de datos, tampoco la cambviamos internamente, para mantener la integridad de datos
            provincia.setFaseDesconfinamiento(faseAsignar);
            return true;
        }
        catch(Exception ex)
        {
            Log.e("ERROR CAMBIO FASE:", ex.getMessage());
            return false;
        }
    }
    
}
