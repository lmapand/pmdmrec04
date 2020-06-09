package mis.practicas.recuperacion.modelo;


import java.io.Serializable;

/** Clase para la representacion de un usuario Administrador
 *
 */
public class UsuarioAdmin implements Serializable
{
    
    private final String nombre;
    private final String apellidos;
    private final int id;
    private final String correo;
    
    public UsuarioAdmin(String nombre, String apellidos, int id, String correo)
    {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.id = id;
        this.correo = correo;
    }
    
    public String getTextoReferencia()
    {
        return nombre+" "+apellidos;
    }
    
    public String getApellidos()
    {
        return apellidos;
    }
    
    public String getNombre()
    {
        return nombre;
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getCorreo()
    {
        return correo;
    }
}
