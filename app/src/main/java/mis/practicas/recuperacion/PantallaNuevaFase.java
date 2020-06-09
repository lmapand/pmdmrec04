package mis.practicas.recuperacion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mis.practicas.recuperacion.guiauxiliar.AdaptadorRegistroCaracteristicas;
import mis.practicas.recuperacion.modelo.FaseDesconfinamiento;
import mis.practicas.recuperacion.modelo.GestorFasesDesconfinamiento;

public class PantallaNuevaFase extends AppCompatActivity implements View.OnClickListener
{
    
    private RecyclerView listadoCaract;
    private AdaptadorRegistroCaracteristicas adaptador;
    private ArrayList<String>caracteristicas=new ArrayList<String>();
    private EditText txtTextoNuevaCaract;
    private Spinner spNuevaFase;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nueva_fase);
        
        this.setTitle( R.string.txtTituloPantallaCrearFase);
        
        
        /* Cargar los datos en el Spinner:
        Se permite generar hasta 10 niveles de  fase, siempre que no exista ya la fase.
        */
        List<Integer> valoresIdFase=new ArrayList<Integer>();
        //Aañdir el máximo de fases
        for(int i=0;i<MainActivity.MAX_FASES;i++)
            valoresIdFase.add(i);
        
        //Captura del Recicler view:
        this.listadoCaract=(RecyclerView ) this.findViewById(R.id.rvRegistroListaCaracteristicas);
        
        //Asignacion de los datos a enlazar
        this.adaptador=new AdaptadorRegistroCaracteristicas((this.caracteristicas));
        
        //Establcemos el gestor de distribucion
        RecyclerView.LayoutManager gestorDiseno=new LinearLayoutManager(this);
        this.listadoCaract.setLayoutManager(gestorDiseno);
        this.listadoCaract.setAdapter(this.adaptador);
        
        /* Quitar las ya asignadas:
        Convertimos a Integer, porque lo que queremos quitar es EL VALOR, no el indice
        */
        for(FaseDesconfinamiento fd:MainActivity.MAPA_FASES_DESCONFINAMIENTO.values())
            valoresIdFase.remove( (Integer)fd.getIdFase());

        ArrayAdapter<Integer>adaptadorIDFase=new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, valoresIdFase);
        adaptadorIDFase.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spNuevaFase=(Spinner)this.findViewById(R.id.spNFFaseID);
        this.spNuevaFase.setAdapter(adaptadorIDFase);
    
       /** Evento de borrado de las características si se cambia de selector de Fase
         *
         */
       this.spNuevaFase.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
       {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
           {
               //Limpiamos la lista de características actuales
                vaciaCadenas();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent)
           {

           }
       });
        
        
        /** Asignar otros elementos de interfaz*/
        this.txtTextoNuevaCaract=(EditText)this.findViewById(R.id.txtEntradaCaracteristicaFase);
        
        /* Asignar acciones a los botones*/
        ((ImageButton)this.findViewById(R.id.btNFaddCaracteristica)).setOnClickListener(this);
        ((Button)this.findViewById(R.id.btNFRegistarFase)).setOnClickListener(this);

    }
    
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btNFaddCaracteristica:
                //Capturamos el texto escrito y borramos el contenido del cuadro
                String txtCaract=this.txtTextoNuevaCaract.getText().toString();
                this.txtTextoNuevaCaract.setText(null);
                //Si el texto era nulo, notificamos
                if(txtCaract.matches("^\\s*$"))
                {
                    this.txtTextoNuevaCaract.setText(null);
                    Toast.makeText(this, "Nada que añadir", Toast.LENGTH_LONG).show();
                    break;
                }
                this.caracteristicas.add(txtCaract);
                this.adaptador.notifyItemInserted(this.caracteristicas.size());
                break;
                
            case R.id.btNFRegistarFase:
                //Si no se han seleccionado Caracteristicas, no permitirá añadirla
                if(this.caracteristicas.isEmpty())
                {
                    this.muestraMensajeErrorCaracteristicas();
                    break;
                }
                //Registrar la fase y sus caracteristicas
                GestorFasesDesconfinamiento gfd=new GestorFasesDesconfinamiento(MainActivity.BBDD);
                int idNuevaFase=(Integer)this.spNuevaFase.getSelectedItem();
                String mensajeOperacion;
                if(gfd.registraFase(idNuevaFase, this.caracteristicas ))
                {
                    Toast.makeText(this, getResources().getString(R.string.txtFaseCreadaCorrectamente, idNuevaFase), Toast.LENGTH_LONG).show();
                    this.finish();
                }
                else
                    Toast.makeText(this, getResources().getString(R.string.txtFaseErrorCreacion, idNuevaFase), Toast.LENGTH_LONG).show();
                break;
        }
    }
    
    /** Realiza el vaciado del Array de Caracteristicas y lo notifica al RecyclerView
     *
     */
    private void vaciaCadenas()
    {
        int numDatosActuales=this.caracteristicas.size();
        this.caracteristicas.clear();
        this.adaptador.notifyItemRangeRemoved(0,numDatosActuales);
    }
    
    /** Muestra un dialogo de error indicando que no hay características que mostrar
     *
     */
    private void muestraMensajeErrorCaracteristicas()
    {
        AlertDialog.Builder bldDlg=new AlertDialog.Builder(this);
        bldDlg.setTitle(R.string.txtDlgNITitulo);
        bldDlg.setMessage(R.string.txtDlgNITexto);
        bldDlg.setIcon(android.R.drawable.ic_dialog_alert);
        bldDlg.setPositiveButton(R.string.txtDlgNIBtAceptar, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        bldDlg.create();
        bldDlg.show();
    }
    
    
}
