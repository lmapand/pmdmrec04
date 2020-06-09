package mis.practicas.recuperacion.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mis.practicas.recuperacion.MainActivity;


public class GestorFasesDesconfinamiento
{
    private final SQLiteDatabase bbdd;
    
    public GestorFasesDesconfinamiento(SQLiteDatabase bbdd)
    {
        this.bbdd = bbdd;
    }
    
    
    public Map<Integer, FaseDesconfinamiento>cargaFases()
    {
        Map<Integer,FaseDesconfinamiento>mapa=new TreeMap<Integer, FaseDesconfinamiento>();
        
        String txtConsulta="SELECT F.ID_FASE AS ID," +
                " CF.TEXTO AS  CARACTERISTICA " +
                "FROM FASES_CONFINAMIENTO AS F LEFT JOIN CARACTERISTICAS_FASE  AS CF" +
                " ON F.ID_FASE=CF.FASE " +
                "ORDER BY F.ID_FASE, CF.ORDEN ";
    
        Cursor cursor=this.bbdd.rawQuery(txtConsulta,null);
        cursor.moveToFirst();
        
        int idActual=-1;
        int idLeido;
        FaseDesconfinamiento faseEnLectura=null;
        while(!cursor.isAfterLast())
        {
            idLeido=cursor.getInt(cursor.getColumnIndex("ID"));

            //Si el valor del ID es nuevo, creamos una nueva fase
            if(idLeido!=idActual)
            {
                idActual=idLeido;
                faseEnLectura=new FaseDesconfinamiento(idActual);
                mapa.put(idActual, faseEnLectura );
            }
            //Si el valor de la caracteristica no es nula, la aadimos como caracteristica de la fase que estamos analizando
            if(!cursor.isNull(cursor.getColumnIndex("CARACTERISTICA")))
            {
                faseEnLectura.addCaracterisitica(cursor.getString(cursor.getColumnIndex("CARACTERISTICA")));
            }
            
            //Avanzamos el cursor
            cursor.moveToNext();
        }
    
        cursor.close();
        
        //Comprobacion
        for(Map.Entry<Integer, FaseDesconfinamiento>entradas: mapa.entrySet())
        {
            Log.i("ID " + entradas.getKey(), entradas.getValue().toString());
            for(int x=0;x<entradas.getValue().getNumeroCaracteristicas();x++)
                Log.i("CARACT "+x,  entradas.getValue().getCaracteristica(x));
        }
        return mapa;
    }
    
    /** realiza el registro de una nueva fase con sus características.
     *
     * @param idFase int ID_FASE a asignar
     * @param caracteristicas String[] Lista de cadenas que conforman las características de la fase
     * @return boolean TRUE si la operacin tuvo éxito, FALSE si no se pudo realizar el registro
     */
    public boolean  registraFase(int idFase, List<String>caracteristicas )
    {
    
        ContentValues valores=new ContentValues();
        valores.put("ID_FASE", idFase);
        try
        {
            this.bbdd.insert("FASES_CONFINAMIENTO", null, valores);
            FaseDesconfinamiento nuevaFase=new FaseDesconfinamiento(idFase);
            
            //Registramos las caracteristicas
            if(!this.registraCaracteristicasFase(nuevaFase, caracteristicas))
                Log.e("ERROR REGISTRANDO FASE", "Fallo añadiendo caracteristica(s)");
                
            //Añadimos el elemento al Arbol Actual
            MainActivity.MAPA_FASES_DESCONFINAMIENTO.put(idFase, nuevaFase);
            return true;
        }
        catch(Exception ex)
        {
            Log.e("ERROR REGISTRANDO FASE", ex.getMessage());
            return false;
        }
    }
    
    /** Elimina las entradas asociadas con la una Fase de Desconfinamiento.
     *
     * @param fd FaseDesconfnamiento
     * @return booean TRUE si ha podido eliminar los registros, false en caso contrario
     */
    private boolean eliminaCaracteristicas(FaseDesconfinamiento fd)
    {
        String parametroEliminacion="FASE=?";
        String[] valoresEliminacion=new String[]{String.valueOf(fd.getIdFase())};
        try
        {
            this.bbdd.delete("CARACTERISTICAS_FASE", parametroEliminacion,valoresEliminacion);
            return true;
        }
        catch(Exception ex)
        {
            Log.e("ERROR ELIM CARACT. FASE", ex.getMessage());
            return false;
        }
    }
    
    
    private boolean registraCaracteristicasFase(FaseDesconfinamiento fase, List<String>caracteristicas)
    {
        ContentValues valores=new ContentValues();
        boolean correcto=true;
        for(int x=0;x<caracteristicas.size();x++)
        {
            valores.clear();
            valores.put("FASE", String.valueOf(fase.getIdFase()));
            valores.put("TEXTO", caracteristicas.get(x));
            valores.put("ORDEN", String.valueOf(x));
            if( this.bbdd.insert("CARACTERISTICAS_FASE", null, valores)==-1)
                correcto=false;
            else //Registramos solo si la inserción ha sido correcta
                fase.addCaracterisitica(caracteristicas.get(x));
        }
        
        return correcto;
    }
}
