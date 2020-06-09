package mis.practicas.recuperacion.guiauxiliar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mis.practicas.recuperacion.PantallaUsuario;
import mis.practicas.recuperacion.PantallaVerProvincia;
import mis.practicas.recuperacion.R;
import mis.practicas.recuperacion.modelo.Provincia;

public class AdaptadorProvincia extends RecyclerView.Adapter
{
    
    private final Provincia[] provincias;
    
    
    public AdaptadorProvincia(Provincia[] provincias)
    {
        this.provincias=provincias;
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //Creación de la vista referenciando la 'tarjeta' por 'inflado'
        LayoutInflater inf=(LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //Creación de la tarjeta
        View protoTarjeta = inf.inflate(R.layout.diseno_tarjeta_provincia,parent, false);
        
        //Devolvemos el componente ya creado
        RecyclerView.ViewHolder tarjeta=new AdaptadorProvincia.Visor(protoTarjeta);
        
        return tarjeta;
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((AdaptadorProvincia.Visor)holder).setData(provincias[position]);
    }
    
    @Override
    public int getItemCount()
    {
        return provincias.length;
    }
    
    public static class Visor extends RecyclerView.ViewHolder implements View.OnClickListener
    {
    
        private ImageView imgProvincia;
        private TextView txtNombreProvincia;
        private TextView txtFase;
        private Provincia provincia;
    
        public Visor(@NonNull View itemView)
        {
            super(itemView);
            this.txtNombreProvincia = itemView.findViewById(R.id.etqListadoProvinciaNombre);
            this.txtFase = itemView.findViewById(R.id.etqListadoProvinciaFase);
            this.imgProvincia = itemView.findViewById(R.id.imgListadoProvincia);
            itemView.setOnClickListener(this);
        }
    
        /**
         * Establece la informacion en la vista
         *
         * @param provincia Provincia  a mostrar
         */
        public void setData(Provincia provincia )
        {
            this.provincia=provincia;
            this.txtNombreProvincia.setText(provincia.getNombre());
            this.imgProvincia.setImageResource(provincia.getIdImagen());
            this.txtFase.setText(provincia.getFaseDesconfinamiento().toString());
        }
    
        @Override
        public void onClick(View v)
        {
            Context contexto=v.getContext();
            Intent i=new Intent();
            i.setClassName(v.getContext(), PantallaVerProvincia.class.getName());
            i.putExtra(PantallaUsuario.DATO_TIPO_PROVINCIA, this.provincia);
            //Lanzamos el intent
            contexto.startActivity(i);
        }
    }

}
