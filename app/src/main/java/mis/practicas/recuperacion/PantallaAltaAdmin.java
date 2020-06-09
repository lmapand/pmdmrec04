package mis.practicas.recuperacion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import mis.practicas.recuperacion.modelo.GestorUsuarios;

public class PantallaAltaAdmin extends AppCompatActivity implements View.OnClickListener
{
    
    
    private final String FORMATO_LOGIN="^[A-Za-z0-9\\.\\-\\_\\#]{6,12}$";
    private final String FORMATO_CONTRAS=FORMATO_LOGIN;
    private final String FORMATO_CORREO="^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    
    private  EditText txtNombre;
    private  EditText txtApellidos;
    private  EditText txtLogin;
    private  EditText txtPass1;
    private  EditText txtPass2;
    private  EditText txtCorreo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_alta_admin);
    
        //Asociacion de ariables a widgets, para facilitar su manejo
        this.txtNombre =(EditText)this.findViewById(R.id.txtAltaAdminNombre);
        this.txtApellidos=(EditText)this.findViewById(R.id.txtAltaAdminApellidos);
        this.txtLogin=(EditText)this.findViewById(R.id.txtAltaAdminUsuario);
        this.txtPass1=(EditText)this.findViewById(R.id.txtAltaAdminPass1);
        this.txtPass2=(EditText)this.findViewById(R.id.txtAltaAdminPass2);
        this.txtCorreo=(EditText)this.findViewById(R.id.txtAltaAdminCorreo);
        
        //Establecer evento de alta
        this.findViewById(R.id.btAlataAdminRegistrar).setOnClickListener(this);
    
        //Establecer titulo
        this.setTitle(R.string.txtTituloPantallaAltaAdmin);
    }
    
    
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btAlataAdminRegistrar:
                StringBuilder sb=new StringBuilder();
                if(!this.compruebaDatos(sb))
                {
                    //Mostrar el dialogo con los errores
                    muestraDialogoError(sb);
                    break;
                }
                //comprobar la validez de los datos
                
                if(MainActivity.BBDD==null)
                {
                    Log.e("ERROR BBDD", "No hay base de datos a la que conectar");
                    break;
                }
                else if(!MainActivity.BBDD.isOpen())
                {
                    Log.e("ERROR BBDD", "La base de datos está cerrada");
                    break;
                }
                
                GestorUsuarios gu=new GestorUsuarios(MainActivity.BBDD);
                if(gu.existeUsuario( this.txtLogin.getText().toString()))
                {
                    this.txtLogin.setError(this.getResources().getString(R.string.txtErrorLoginEnUso));
                    sb.append("\n\t-El login indicado ya está en uso");
                    muestraDialogoError(sb);
                    break;
                }
                
                //Llegados aqui sin errores, procedemos al registro del usuario
                if(gu.registraUsuario(  this.txtNombre.getText().toString(),
                                        this.txtApellidos.getText().toString(),
                                        this.txtLogin.getText().toString(),
                                        this.txtPass1.getText().toString(),
                                        this.txtCorreo.getText().toString()))
                {
                    
                    //Test:
                    gu.listaDatosUsuarios();
                    //Mostrar el toast (o un Dialog) indicando 'usuario registrado'
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
                    //Volver a la actividad anterior
                    this.finish();
                }
                else //Todo: pasar a una constante de texto
                    Toast.makeText(this, "SE HA PRODUCIDO UN ERROR AL INTENTAR REALIZAR EL REGISTRO", Toast.LENGTH_LONG).show();
            break;
        }
    }
    
    
    
    
    
    
    
    /** Realiza la comporbacion de contenido y formato de datos del Formulario.
     *
     * Adicionalmente, el método actualiza los cuadros de texto de la interfaz para indicar los errores cometidos.
     *
     * @param sb StringBuilder (byRef) donde se intriducen las cadenas describiendo los errores
     * @return boolean TRUE si los contenidos son formalmente correctos, FALSE en caso contrario
     */
    private boolean compruebaDatos(StringBuilder sb)
    {
        
        //Boramos los indicadores de posibles errores previos
        this.txtNombre.setError(null);
        this.txtApellidos.setError(null);
        this.txtLogin.setError(null);
        this.txtPass1.setError(null);
        this.txtPass2.setError(null);
        this.txtCorreo.setError(null);
        
        
        //Analisis de los diferentes elementos:
        boolean hayError=false;
        
        //Comprobacion del nombre (existencia)
        String nombre=this.txtNombre.getText().toString();
        if(nombre.matches("^\\s*$"))
        {
            hayError=true;
            this.txtNombre.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
            sb.append("\n\t - Falta el nombre");
        }
    
        //Comprobacion del apellido(existencia)
        String apellidos=this.txtApellidos.getText().toString();
        if(apellidos.matches("^\\s*$"))
        {
            hayError=true;
            this.txtApellidos.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
            sb.append("\n\t - Faltan los apellidos");
        }
        
        //Comprobacion del login (existencia y formato: 6 a 12 caracteres alfabeticos, numericos, y _.,-)
        String login=this.txtLogin.getText().toString();
        if(login.matches("^\\s*$"))
        {
            hayError=true;
            this.txtLogin.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
            sb.append("\n\t - Falta el login");
        }
        else if(!login.matches(FORMATO_LOGIN))
        {
            hayError=true;
            this.txtLogin.setError(this.getResources().getString(R.string.txtErrorFormatoIncorrecto));
            sb.append("\n\t - El formato para login no es correcto: deben ser entre 6 y 12 caracteres alfabéticos, numéricos y/o los símbolos # . -  _");
        }
        
        //Comprobacion de las contrsaeñas
        boolean passCorrecta=true;
        boolean passCompCorrecta=true;
        String pass1=this.txtPass1.getText().toString();
        if(pass1.matches("^\\s*$"))
        {
            hayError=true;
            passCorrecta=false;
            this.txtPass1.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
            sb.append("\n\t - Falta la contraseña");
        }
        else if(!pass1.matches(FORMATO_CONTRAS))
        {
            hayError=true;
            passCorrecta=false;
            this.txtPass1.setError(this.getResources().getString(R.string.txtErrorFormatoIncorrecto));
            sb.append("\n\t - El formato para la Contraseña no es correcto: deben ser entre 6 y 12 caracteres alfabéticos, numéricos y/o los símbolos # . -  _");
        }
        
        //Comprobar la Pass2 sólo en caso de que la pass1 sea correcta
        String pass2=this.txtPass2.getText().toString();
        if(passCorrecta)
        {
            if(pass2.matches("^\\s*$"))
            {
                hayError=true;
                passCompCorrecta=false;
                this.txtPass2.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
                sb.append("\n\t - Falta la comprobación de la contraseña");
            }
            else if(!pass2.matches(FORMATO_CONTRAS))
            {
                hayError=true;
                passCompCorrecta=false;
                this.txtPass2.setError(this.getResources().getString(R.string.txtErrorFormatoIncorrecto));
                sb.append("\n\t - El formato para la comprobacion de Contraseña no es correcto: deben ser entre 6 y 12 caracteres alfabéticos, numéricos y/o los símbolos # . -  _");
            }
        }
        
        //Comprobacion de coincidencia de Contraseñas (solo si las dos son formalmente correctas)
        if(passCorrecta && passCompCorrecta && pass1.compareTo(pass2)!=0)
        {
            hayError=true;
            this.txtPass1.setError(this.getResources().getString(R.string.txtErrorContrasNoCoinciden));
            this.txtPass2.setError(this.getResources().getString(R.string.txtErrorContrasNoCoinciden));
            sb.append("\n\t - La contraseña y su verificación tienen valores distintos");
        }
        
        //Comprobacion del e-mail
        String correo=this.txtCorreo.getText().toString();
        if(correo.matches("^\\s*$"))
        {
            hayError=true;
            this.txtCorreo.setError(this.getResources().getString(R.string.txtErrorCampoVacio));
            sb.append("\n\t - Falta la direccion de correo electrónico");
        }
        else if(!correo.matches(FORMATO_CORREO))
        {
            hayError=true;
            this.txtCorreo.setError(this.getResources().getString(R.string.txtErrorFormatoIncorrecto));
            sb.append("\n\t - El formato de la direccion de correo electrónico no es correcto");
        }
    
        return !hayError;
    }
    
    private void muestraDialogoError(StringBuilder sb)
    {
        AlertDialog.Builder bldDlg=new AlertDialog.Builder(this);
        bldDlg.setTitle(R.string.txtTituloDlgErrorAltaAdmin);
        bldDlg.setMessage(sb.toString());
        bldDlg.setIcon(android.R.drawable.ic_dialog_alert);
        bldDlg.setPositiveButton(R.string.txtDlgErrorAltaAdminBtAceptar, new DialogInterface.OnClickListener()
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
