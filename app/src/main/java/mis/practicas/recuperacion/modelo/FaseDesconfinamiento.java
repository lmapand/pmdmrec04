package mis.practicas.recuperacion.modelo;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FaseDesconfinamiento implements Serializable,Comparable<FaseDesconfinamiento>
{
    private final int idFase;
    private final List<String>caracteristicas=new ArrayList<>();
    
    public FaseDesconfinamiento(int id)
    {
        this.idFase=id;
    }

    public int getNumeroCaracteristicas()
    {
        return this.caracteristicas.size();
    }
    
    public String getCaracteristica(int indice)
    {
        return this.caracteristicas.get(indice);
    }
    
    public void addCaracterisitica(String carcteristica)
    {
        this.caracteristicas.add(carcteristica);
    }
    
    public String eliminaCaracteristica(int indice)
    {
        if(indice<0 || indice>this.caracteristicas.size()-1)
            return null;
        
        return this.caracteristicas.remove(indice);
    }
    
    public int getIdFase()
    {
        return this.idFase;
    }
    
    @Override
    public boolean equals(@Nullable Object obj)
    {
        if(obj==null || !(obj instanceof FaseDesconfinamiento))
            return false;
        
        return ((FaseDesconfinamiento)obj).idFase==this.idFase;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(idFase);
    }
    
    @Override
    public int compareTo(FaseDesconfinamiento o)
    {
        return this.idFase - o.idFase;
    }
    
    public String toString()
    {
        return "FASE "+String.valueOf(this.idFase);
    }
    
}
