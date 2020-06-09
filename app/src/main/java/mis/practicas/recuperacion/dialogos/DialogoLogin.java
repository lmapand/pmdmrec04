package mis.practicas.recuperacion.dialogos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import mis.practicas.recuperacion.MainActivity;
import mis.practicas.recuperacion.R;
import mis.practicas.recuperacion.modelo.GestorUsuarios;
import mis.practicas.recuperacion.modelo.UsuarioAdmin;

/** Dialogo para realizar el Login de Adinistrador
 *
 */
public class DialogoLogin extends DialogFragment implements View.OnClickListener
{
    
    /** Una imlementacion del Intefaz (posiblemente para pasar la propia activity??
     *
     */
    private AlCubrirDatosrListener mAlCubrirDatos;
    private View vistaRaiz;
    
    
    private TextView etqError;
    private EditText txtUsuario;
    private EditText txtPass;
    
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup contenedor, Bundle savedInstanceState)
    {
        this.vistaRaiz=inflater.inflate(R.layout.layout_dialogo_login, contenedor, false);
        this.getDialog().setTitle(this.getTag());
        
        
        
        this.etqError=(TextView)this.vistaRaiz.findViewById(R.id.etqDlgLoginMostrarError);
        this.txtUsuario=(EditText) this.vistaRaiz.findViewById(R.id.txtDlgLoginUsuario);
        this.txtPass=(EditText) this.vistaRaiz.findViewById(R.id.txtDlgLoginPass);
        
        ((Button) this.vistaRaiz.findViewById(R.id.btDlgLoginCancelar)).setOnClickListener(this);
        ((Button) this.vistaRaiz.findViewById(R.id.btDlgLoginLogin)).setOnClickListener(this);
        
        return this.vistaRaiz;
    }
    
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btDlgLoginCancelar:
                this.dismiss();
                break;
                
            case R.id.btDlgLoginLogin:
               
                this.etqError.setText(null);
                
                //Comprobar que los datos estan completos
                if(!this.compruebaCamposCubiertos())
                    break;
               
                //Validar al usuario
                GestorUsuarios gu=new GestorUsuarios(MainActivity.BBDD);
                UsuarioAdmin usuario=gu.logIn(this.txtUsuario.getText().toString(),
                                                this.txtPass.getText().toString());
                if(usuario==null)
                {
                    this.etqError.setText(R.string.txtErrorDlgLoginErrorVerificacion);
                    break;
                }
                
                //Enviar al usuario validado a la Aplicacion Principal
                Bundle datos=new Bundle();
                datos.putSerializable(MainActivity.CAMPO_PASO_USUARIO,usuario);
    
            /* El metodo getActivity referencia la Actividad qeu llamó al dialogo. Capturamos la
            actividad y disparamos el método de interfaz, realizando así la inyeccion.
            */
            this.mAlCubrirDatos= (AlCubrirDatosrListener) getActivity();

            this.mAlCubrirDatos.onDatosCubiertos(datos);

            this.dismiss();
            break;
        }
        
    }
    
    
    /** realiza la verificacion formal de los datos
     *
     * También establece los indicadores de error correpondiente y rellena, en su caso, la
     * etiqueta de texto de error
     *
     * @return boolean si los datos para la verificacion estan presentes
     */
    private boolean compruebaCamposCubiertos()
    {
        boolean camposPresentes=true;
    
        //Verificar que existen los textos y en caso contrario, mostrar un error
        String usuario=this.txtUsuario.getText().toString();
        if(usuario.matches("^\\s*$"))
        {
            camposPresentes=false;
            this.txtUsuario.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
        }
        String contrasena=this.txtPass.getText().toString();
        if(contrasena.matches("^\\s*$"))
        {
            this.txtPass.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
            camposPresentes = false;
        }
    
        if(!camposPresentes)
            this.etqError.setText(R.string.txtErrorDlgLoginCamposRequeridos);

        return camposPresentes;
    }
    
    
    /** Esta interfaz permitirá la comunicacion de los datos.
     *
     * La va a implementar la Activity llamante.
     *
     * */
    public interface AlCubrirDatosrListener
    {
        void onDatosCubiertos(Bundle bundle);
    }
}
