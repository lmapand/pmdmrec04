package mis.practicas.recuperacion.modelo;

import java.io.Serializable;

public class Provincia implements Serializable
{
    //private static final long serialVersionUID = 12345678L;
    
    private final String nombre;
    private final int idImagen;
    
    private FaseDesconfinamiento faseDesconfinamiento;
    
    public Provincia(String nombre, int idImagen)
    {
        this.nombre=nombre;
        this.idImagen= idImagen;
    }
    
    public String getNombre()
    {
        return this.nombre;
    }
    
    public int  getIdImagen()
    {
        return this.idImagen;
    }
    
    public String toString()
    {
        return this.nombre;
    }
    
    /** Obtiene la fase de confinamiento.
     *
     * Sería incorrecto dar acceso de este modo a la Fase de Confinamiento, pero a efectos
     * de esta práctica lo dejaremos asi.
     *
     * @return FaseDesconfinamiento la Fase de COnfinamiento en la que se encuentra esta provincia.
     */
    public FaseDesconfinamiento getFaseDesconfinamiento()
    {
        return this.faseDesconfinamiento;
    }
    
    public void setFaseDesconfinamiento(FaseDesconfinamiento nuevaFase)
    {
        this.faseDesconfinamiento =nuevaFase;
    }
}
