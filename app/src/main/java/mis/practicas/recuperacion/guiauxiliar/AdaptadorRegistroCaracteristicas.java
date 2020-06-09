package mis.practicas.recuperacion.guiauxiliar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mis.practicas.recuperacion.R;

public class AdaptadorRegistroCaracteristicas extends RecyclerView.Adapter
{
    private final List<String>caracteristicas;
    
    
    public AdaptadorRegistroCaracteristicas(List<String> caracteristicas)
    {
        this.caracteristicas=caracteristicas;
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //Creación de la vista referenciando la 'tarjeta' por 'inflado'
        LayoutInflater inf=(LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        //Creación de la tarjeta
        View protoTarjeta = inf.inflate(R.layout.diseno_tarjeta_alta_caracteristica,parent, false);
    
        //Devolvemos el componente ya creado
        RecyclerView.ViewHolder tarjeta=new Visor(protoTarjeta);
        
        return tarjeta;
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((Visor)holder).setData(caracteristicas.get(position));
    }
    
    @Override
    public int getItemCount()
    {
        return caracteristicas.size();
    }
    
    public static class Visor extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        
        public ImageButton btArriba;
        public ImageButton btAbajo;
        public ImageButton btQuitar;
        public TextView txtCaracteristica;
        
        public Visor(@NonNull View itemView)
        {
            super(itemView);
            this.btArriba=itemView.findViewById(R.id.btSubirNuevaCaract);
            this.btAbajo=itemView.findViewById(R.id.btBajarNuevaCaract);
            this.btQuitar=itemView.findViewById(R.id.btEliminarNuevaCaract);
            this.txtCaracteristica=itemView.findViewById(R.id.etqTxtNuevaCaracteristica);
            itemView.setOnClickListener(this);
        }
        
        /** Establece la informacion en la vista
         *
         * @param txtCaract texto de la Carcaterística
         */
        public void setData(String txtCaract)
        {
            this.txtCaracteristica.setText(txtCaract);
        }
        
        @Override
        public void onClick(View v)
        {
            Toast.makeText(v.getContext(),  this.txtCaracteristica.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    
    
    
    
}