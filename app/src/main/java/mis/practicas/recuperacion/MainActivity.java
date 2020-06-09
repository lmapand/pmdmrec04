package mis.practicas.recuperacion;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Map;

import mis.practicas.recuperacion.bbdd.GestorBBDD;
import mis.practicas.recuperacion.dialogos.DialogoLogin;
import mis.practicas.recuperacion.modelo.FaseDesconfinamiento;
import mis.practicas.recuperacion.modelo.GestorFasesDesconfinamiento;
import mis.practicas.recuperacion.modelo.GestorProvincias;
import mis.practicas.recuperacion.modelo.GestorUsuarios;
import mis.practicas.recuperacion.modelo.Provincia;
import mis.practicas.recuperacion.modelo.UsuarioAdmin;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener,  DialogoLogin.AlCubrirDatosrListener
{
    /** numero máximo de Fases, para este práctica*/
    public static final int MAX_FASES=10;
    
    /** Referencia al nombre de capo de pasos de datos para el usuario que realiza login*/
    public static final String CAMPO_PASO_USUARIO="USUARIO";
    
    
    
    public static SQLiteDatabase BBDD=null;
    
    /** Declaracion de las Provincias, que no van a variar durante la ejecucion de esta aplicacion*/
    public static Provincia[] PROVINCIAS;
    
    /** Usuario que está logurado como Administrador.*/
    public static UsuarioAdmin USUARIO_ACTUAL=null;
    
    /** Mapa para el almacenemiento de las Fases de Desconfinamiento.
     * Utilizamos el campo ID_FASE de la tabla FASES_DESCONFINAMIENTO como indice de Mapa.
     */
    public static Map<Integer, FaseDesconfinamiento> MAPA_FASES_DESCONFINAMIENTO;
    
    
    //Referencias a elementos
    private TextView etqUsuario;
    private Button btLogin;
    private Button btAdmin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        //Inicializacion del SQLiteHelper, para creacion y actualizacion
        this.iniciaBaseDatos();

        //Carga de los datos de arranque de la aplicacion:
        //Carga de Fases de Confinamiento:
        GestorFasesDesconfinamiento gfd=new GestorFasesDesconfinamiento(BBDD);
        MainActivity.MAPA_FASES_DESCONFINAMIENTO=gfd.cargaFases();
        
        //Carga de Provicias:
        GestorProvincias gp=new GestorProvincias(BBDD, MainActivity.MAPA_FASES_DESCONFINAMIENTO);
        MainActivity.PROVINCIAS=gp.cargaProvincias(this.getApplicationContext());
        
        //Comprobacion de los usuarios:
        GestorUsuarios gu=new GestorUsuarios(BBDD);
        gu.listaDatosUsuarios();
        
        //Asociacion de vriables a controles
        this.etqUsuario=this.findViewById(R.id.etqUsuarioVerificadio);
        this.btLogin=((Button)this.findViewById(R.id.btLogin));
        this.btAdmin =((Button)this.findViewById(R.id.btAdmin));;
        
        //Establecer los eventos de click
        this.findViewById(R.id.btUsuario).setOnClickListener(this);
        this.btLogin.setOnClickListener(this);
        this.btAdmin.setOnClickListener(this);
        this.findViewById(R.id.btNuevoAdmin).setOnClickListener(this);
        
        this.actualizaPresentacionUsuario();
        
        this.setTitle(R.string.txtTituloPantallaPrincipal);
    
    
        //Borrado de la Base de Datos para pruebas
        //this.deleteDatabase(GestorBBDD.NOMBRE_BBDD);
        
    }
    
   
    
    
    
    @Override
    public void onClick(View v)
    {
        Intent i=null;
        
        switch(v.getId())
        {
            //Gention del boton de Usuario
            case R.id.btUsuario:
                i=new Intent();
                i.setClassName(this, PantallaUsuario.class.getName());
                break;
                
            case R.id.btAdmin:
                i=new Intent();
                i.setClassName(this, PantallaAdmin.class.getName());
                break;
                
            //Gestion desesion  Sesion: muestra el dialogo de Login o realzia el LogOut
            case R.id.btLogin:
                //Aqui hay dos casos: que esté en modo LogIn o en modo LogOut
                if(USUARIO_ACTUAL==null) //Caso LogIn
                {
                    DialogoLogin dlg = new DialogoLogin();
                    FragmentManager fm2 = this.getSupportFragmentManager();
                    dlg.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogoPersonalizado);
                    dlg.show(fm2, "LOGIN");
                    break;
                }
                else  //caso LogOut
                {
                    //Poner e Usuario como NULL
                    MainActivity.USUARIO_ACTUAL=null;

                    //Actualizar los controles necesarios
                    this.actualizaPresentacionUsuario();

                    //Sacar un 'toast' de aviso
                    Toast.makeText(this, R.string.txtLogOutCorrecto,Toast.LENGTH_LONG).show();
                }
                break;
                
                
            //añadir Administrador
            case R.id.btNuevoAdmin:
                i=new Intent();
                i.setClassName(this, PantallaAltaAdmin.class.getName());
                break;
                
        }

        //Inicio de Actividad si ha lugar
        if(null!=i)
            this.startActivity(i);
    }
    
    /** Lanza el SQLiteHelper y establece la Base de Datos.
     *
      */
    private void iniciaBaseDatos()
    {
        Log.i("BBDD","Reiniciando Base de Datos");
        //Comprobar si la base de datos esta cerrada, e cuyo caso habrá que volver a abrirla
        if(BBDD==null || !BBDD.isOpen())
        {
            GestorBBDD gbd=new GestorBBDD(this);
            BBDD=gbd.getBbdd();
        }
    }
    
    /** Liberar recursos de la Base de Datos.
     *
     */
    private void cierraBaseDatos()
    {
        Log.i("BBDD","Reiniciando Base de Datos");
        if(BBDD!=null && BBDD.isOpen())
            BBDD.close();
    
        BBDD=null;
    }
    
    
    /** implementacion de la interfaz DialogoLogin.AlCubrirDatosrListener
     *
     * Realiza la verificacion de Usuario y Contrtaseña
     * @param bundle BUndle datos recibidos del Dialogo de Login
     */
    @Override
    public void onDatosCubiertos(Bundle bundle)
    {
    
        /* No comprobamos el origen , ya que solo vamos a recibir datos de validacion de usuario.
        Además la llamada a este méeitido solo se produce como respuesta a una verificacion correcta,
        con lo que se trata de un usurio verificado*/
        
        MainActivity.USUARIO_ACTUAL=(UsuarioAdmin)bundle.getSerializable(MainActivity.CAMPO_PASO_USUARIO);
        
        //Actualizamos el estado de la Aplicacion
        this.actualizaPresentacionUsuario();
    }
    
    
    private void actualizaPresentacionUsuario()
    {
        if(MainActivity.USUARIO_ACTUAL!=null)
        {
            //establecer la etiqueta de nombre de usuario correspondiente
    
            this.etqUsuario.setText(getResources().getString(R.string.fmtConectadoComo,
                    MainActivity.USUARIO_ACTUAL.getNombre(),
                    MainActivity.USUARIO_ACTUAL.getApellidos()));
    
            this.etqUsuario.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
    
            //Habilitar el botón del área de Administrador
            this.btAdmin.setEnabled(true);
            
            //Cambiar el texto del botón de login
            this.btLogin.setText(R.string.txtBtLogOut);
        }
        else
        {
            this.etqUsuario.setText(R.string.txtEtqUsuarioNoVerificado);
            this.etqUsuario.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
    
            this.btAdmin.setEnabled(false);
    
            this.btLogin.setText(R.string.txtBtLogIn);
        }
        
    }
    
    
    
    /** liberacion de la bases de datos si seguia activa.
     *
     */
    public void onDestroy()
    {
        super.onDestroy();
        this.cierraBaseDatos();
    }
    
    public void onStop()
    {
        super.onStop();
        //this.cierraBaseDatos();
    }
    
    public void onResume()
    {
        super.onResume();
    }
    
    
}
