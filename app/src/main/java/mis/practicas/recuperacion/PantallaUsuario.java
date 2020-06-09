package mis.practicas.recuperacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mis.practicas.recuperacion.guiauxiliar.AdaptadorProvincia;

public class PantallaUsuario extends AppCompatActivity implements  View.OnClickListener
{
    
    public static final String DATO_TIPO_PROVINCIA="Provincia";
    
    private RecyclerView rvProvincias;
    private AdaptadorProvincia adaptador;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_usuario);
  
        this.setTitle(R.string.txtTituloPantallaUsuario);
        
        //Establecer el RecyclerView
        this.rvProvincias=(RecyclerView ) this.findViewById(R.id.rvListaProvincias);
    
        this.adaptador=new AdaptadorProvincia(MainActivity.PROVINCIAS);
    
        //Establcemos el gestor de distribucion
        RecyclerView.LayoutManager gestorDiseno=new LinearLayoutManager(this);
 
        this.rvProvincias.setLayoutManager(gestorDiseno);
        this.rvProvincias.setAdapter(this.adaptador);
        
        //establecer eventos de raton
        this.findViewById(R.id.btSalir).setOnClickListener(this);
    }
    
    
    @Override
    public void onClick(View v)
    {
        Intent i=null;
        switch(v.getId())
        {
            //No se ha definido la accion a realizar: interpreto cerrar activity para volver a la anterior
            case R.id.btSalir:
                this.finish();
                break;
        }
        
    }
}
