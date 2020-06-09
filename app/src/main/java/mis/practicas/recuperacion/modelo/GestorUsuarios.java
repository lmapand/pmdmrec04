package mis.practicas.recuperacion.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GestorUsuarios
{
    private final SQLiteDatabase bbdd;
    
    public GestorUsuarios(SQLiteDatabase bbdd)
    {
        this.bbdd = bbdd;
    }
    
    /** Comprueba si existe un usario con el login indicado.
     *
     * La comprobacion se realiza 'CASE_INSENSITIVE'
     *
     * @param login String login del cual analizaremos su existencia
     * @return boolean TRUE si ya existe un login on el mismo valor
     */
    public boolean existeUsuario(String login)
    {
        boolean existe=false;
        
        String txtConsulta="SELECT COUNT(*) AS TOTAL FROM USUARIOS WHERE UPPER(LOGIN)=?";
        Cursor cursor=this.bbdd.rawQuery(txtConsulta,new String[]{login.toUpperCase()});
        
        //Analizamos el resultado
        cursor.moveToFirst();
        if( cursor.getInt(cursor.getColumnIndex("TOTAL"))>0)
            existe=true;
        
        cursor.close();
        
        return existe;
    }
    
    
    /** Realiza la operacion de Login, por la cual un Usuario se conexta al Sistema como Administrador.
     *
     * @param usuario login del usuario
     * @param contras contraseña del Usuario
     * @return UsuarioAdmin el Usuario que se ha conectado como Administrador
     */
    
    public UsuarioAdmin logIn(String usuario, String contras)
    {
        UsuarioAdmin admin=null;
        String txtConsulta="SELECT ID_USUARIO, NOMBRE, APELLIDOS, CORREO FROM USUARIOS WHERE LOGIN=? AND PASS=?";
        Cursor cursor=this.bbdd.rawQuery(txtConsulta, new String[]{usuario, contras});
        
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            admin=new UsuarioAdmin(cursor.getString(cursor.getColumnIndex("NOMBRE")),
                                    cursor.getString(cursor.getColumnIndex("APELLIDOS")),
                                    cursor.getInt(cursor.getColumnIndex("ID_USUARIO")),
                                    cursor.getString(cursor.getColumnIndex("CORREO"))
                                );
        }
        cursor.close();

        return admin;
    }
    
    /** Realiza el registro de un Administrador en la tabla USUARIOS
     *
     * @param nombre
     * @param apellidos
     * @param login
     * @param pass
     * @param correo
     * @return
     */
    public boolean registraUsuario(  String nombre, String apellidos, String login, String pass, String correo)
    {
    
        ContentValues valores=new ContentValues();
        valores.put("NOMBRE", nombre);
        valores.put("APELLIDOS", apellidos);
        valores.put("LOGIN", login);
        valores.put("PASS", pass);
        valores.put("CORREO", correo);
        try
        {
            this.bbdd.insert("USUARIOS", null, valores);
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    /** FUncion auxiliar para comprobar las inserciones.
     *
     * Muestra los registros de Usuarios en el log
     *
     */
    public void listaDatosUsuarios()
    {
        String txtConsulta="SELECT * FROM USUARIOS";
        
        Cursor cursor=this.bbdd.rawQuery(txtConsulta, null);
        
        cursor.moveToFirst();
        int x=0;
        while(!cursor.isAfterLast())
        {
            Log .i("Pollo "+ ++x, cursor.getInt(0)
                                            +" "+cursor.getString(1)
                                            +" "+cursor.getString(2)
                                            +" "+cursor.getString(3)
                                            +" "+cursor.getString(4)
                                            +" "+cursor.getString(5));
            
            
            cursor.moveToNext();
        }
        
        cursor.close();
    }
    

    
    
    
}
