package mis.practicas.recuperacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mis.practicas.recuperacion.modelo.Provincia;

import static androidx.core.content.FileProvider.getUriForFile;

public class PantallaVerProvincia extends AppCompatActivity implements View.OnClickListener
{

    /** Codigos de solicitudes de permisos*/
    private static final int AUTORIZACION_USO_CAMARA=1;
    private static final int AUTORIZACION_ECRITURA_ALM_EXTERNO=2;
    
    /** Codigo para indicar etiquetar el dato imagen en la respuesta del  intent*/
     private static final int SOLICITUD_OBTENER_IMAGEN=1;
    
    
    
    private ListView lstCaractConfinamiento;
    private ImageView imgProvincia;
    
    private Provincia provincia;
    private String nombreImgProvincia;
    
    /** Controla si está habilitada la escritura*/
    private boolean permisoEscritura=false;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ver_provincia);

        this.imgProvincia=(ImageView)this.findViewById(R.id.imgListadoProvincia);
        
        //Captura y representacion de los datos de la provincia
         this.provincia=(Provincia)this.getIntent().getSerializableExtra(PantallaUsuario.DATO_TIPO_PROVINCIA);
        
        if(null==this.provincia) //Notificamos y paramos la carga
        {
            Toast.makeText(this, R.string.txtErrorPasoProvincia, Toast.LENGTH_LONG).show();
            this.setTitle("ERROR DE APLICACION");
            return;
        }

        this.nombreImgProvincia=this.provincia.getNombre()+".jpg";
        
        this.setTitle(this.getResources().getString(R.string.provincia, provincia.getNombre()) );
        
        
        
        
        //Establecer el evento de imagen si está disponible la cámara
        if(this.tieneCamara())
        {
            this.imgProvincia.setOnClickListener(this);
            if(Build.VERSION.SDK_INT>=26)
                this.imgProvincia.setTooltipText(getResources().getString(R.string.tttCambiarImagenProvincia));
        }
    
        //Solicitar el permiso de escritura, y en caso de que se conceda y sea necesario, cargar la imagen
        this.obtenPermisoEscritura();
        
        ((TextView)this.findViewById(R.id.etqVPFaseConfinamiento)).setText(provincia.getFaseDesconfinamiento().toString());
        
        //Datos a cargar en la lista
        List<String> listaCaracteristicas=new ArrayList<String>();
        for(int x=0;x<provincia.getFaseDesconfinamiento().getNumeroCaracteristicas();x++)
            listaCaracteristicas.add(provincia.getFaseDesconfinamiento().getCaracteristica(x));
        
        this.lstCaractConfinamiento=(ListView)this.findViewById(R.id.lstCaractFaseConf);
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaCaracteristicas);
        this.lstCaractConfinamiento.setAdapter(adaptador);
    }
    
    
    /** Solicita permiso de escritura
     *
      */
    private void obtenPermisoEscritura()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PantallaVerProvincia.AUTORIZACION_ECRITURA_ALM_EXTERNO);
        else //Suponemos que tenemos permiso de escritura al declararlo en el manifest
        {
            this.permisoEscritura = true;
            this.estableceImagenProcincia();
        }
    }
    
    
    
    
    /** Evalua si el dispositivo tiene una cámara
     *
     * @return true si el dispositivo dispone de al menos una cámara, false en caso contrario
     */
    private boolean  tieneCamara()
    {
         return this.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.imgListadoProvincia)
        {
            //Comprobacion de SDK y autorizacion por código sdk>23
            if (Build.VERSION.SDK_INT >= 23)
            {
                int autoriz = checkSelfPermission(Manifest.permission.CAMERA);
                if (autoriz == PackageManager.PERMISSION_GRANTED)
                    this.sacarFoto();
                else
                    ActivityCompat.requestPermissions(PantallaVerProvincia.this,
                            new String[]{Manifest.permission.CAMERA},
                            PantallaVerProvincia.AUTORIZACION_USO_CAMARA);
        
            }
            else //Para versiones anteriores a sdk 23 es una llamada directa
                this.sacarFoto();
            
        }
    }
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode,data);
        if(resultCode==RESULT_OK && requestCode==SOLICITUD_OBTENER_IMAGEN)
        {
            //Todo: Reaizar el almacenamiento en la tcarpeta de imagenes del almacenamineto externo
            /*File archImagen=E
            Bitmap datosImagen=(Bitmap)data.getExtras().get("data");
            this.imgProvincia.setImageBitmap(datosImagen);
           */
            //Cargar la imagen que corresponda
            estableceImagenProcincia();
        }
    }
    
    
    /** Establece la imagen para el control de la provincia:
     *  - Si existe una imagen almacenada que coincide con el nombre de la provincia, nuestra esa imagen
     *  - En caso contrario, establece la imagen por defecto
     */
    private void estableceImagenProcincia()
    {
        if(!permisoEscritura)
            this.imgProvincia.setImageResource(this.provincia.getIdImagen());
        else
        {
            //Comprobar si existe un archivo con el nombre de la provincia, en cuyo caso, se emplea esa
            File archImagen=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                                                                    this.nombreImgProvincia);
    
            //En caso de error, establecemos la imagen por defecto
            if(!archImagen.exists() || !archImagen.isFile() || archImagen.length()==0)
                this.imgProvincia.setImageResource(this.provincia.getIdImagen());
            else //Cargar la imagen relacionada
                this.imgProvincia.setImageBitmap(BitmapFactory.decodeFile(archImagen.getAbsolutePath()));
            
        }
    }
    
    
    
    
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            switch (requestCode)
            {
                case PantallaVerProvincia.AUTORIZACION_USO_CAMARA:
                    this.sacarFoto();
                    break;
                case PantallaVerProvincia.AUTORIZACION_ECRITURA_ALM_EXTERNO:
                    this.permisoEscritura = true;
                    this.estableceImagenProcincia();
                    break;
            }
        }
    }
    
    /** Inicia la actividad sacar foto.
     *
      */
    private void sacarFoto()
    {
        //Componer la ruta del archivo
        File dirImagenes= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File archivo=new File(dirImagenes, this.nombreImgProvincia);
        Uri uri=null;
        if(Build.VERSION.SDK_INT>=24)
        {
            uri = getUriForFile(getApplicationContext(),
                                getApplicationContext().getPackageName()+".provider",
                                archivo);
        }
        else
            uri=Uri.fromFile(archivo);
        
        //Llamaar al servicio de cámara indicando que almacene
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        this.startActivityForResult(i, SOLICITUD_OBTENER_IMAGEN);
    }
    
   
}
