package mis.practicas.recuperacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaAdmin extends AppCompatActivity implements View.OnClickListener
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_admin);
    
        this.setTitle(R.string.txtTituloPantallaAdministrador);
     
        //Asignacion de eventos a botones
        this.findViewById(R.id.btAnadirFase).setOnClickListener(this);
        this.findViewById(R.id.btAsignarFase).setOnClickListener(this);
    }
    
    
    @Override
    public void onClick(View v)
    {
        Intent i=null;
        switch(v.getId())
        {
            case R.id.btAnadirFase:
                i=new Intent();
                i.setClassName(this, PantallaNuevaFase.class.getName());
                break;
                
            case R.id.btAsignarFase:
                i=new Intent();
                i.setClassName(this,PantallaAsignarFase.class.getName());
                break;
        }
        
        //Inicio de Activity si se ha indicado
        if(null!=i)
            this.startActivity(i);
    }
}
