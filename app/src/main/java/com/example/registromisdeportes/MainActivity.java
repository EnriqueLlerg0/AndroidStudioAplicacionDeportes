package com.example.registromisdeportes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int VENGO_DE_LA_CAMARA_CON_CALIDAD = 2;
    private static final int PIDO_PEROMISO_ESCRITURA = 1;
    private static final int VENGO_DE_LA_CAMARA = 1;
    private static final int VENGO_DE_LA_GALERIA = 3;
    private static final String NOMBRE_FICHERO = "A";
    private static final String CONTRASEÑA = "contraseña";
    private static final String IMAGEN = "imagen";
    private static final String COMP = "B";
    private static final String COMPSON = "E";
    private static final String MIS_PREFERENCIAS = "Z";
    private static final String VOLUMEN = "AA";
    ImageButton imageButtonopciones;
    File fichero;
    Button buttoncontinuar;
    EditText editTextpassword;
    int contintentos=0, bandera1=0;
    boolean estadoimagen=false;
    MediaPlayer mediaPlayer;
    SensorManager sensorManager;
    SharedPreferences misDatos;
    SharedPreferences comprobar;
    SharedPreferences preferences;
    base_de_datos base_de_datos1;
    String saludo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButtonopciones=findViewById(R.id.imageButtonopcion);
        buttoncontinuar=findViewById(R.id.buttoncontinuar);
        editTextpassword=findViewById(R.id.editTextTextPassword);
        mediaPlayer=MediaPlayer.create(this,R.raw.error);
        base_de_datos1=new base_de_datos(this);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_NORMAL);
        misDatos= PreferenceManager.getDefaultSharedPreferences(this);

        comprobar=PreferenceManager.getDefaultSharedPreferences(this);
        /*SharedPreferences.Editor editor=comprobar.edit();
        editor.putString(COMP,"");
        editor.apply();*/
        if(comprobar.getString(COMP,"").isEmpty())
        {
            base_de_datos1.insertar("futbol","deporte de 11vs11, se juega con los pies","vacio");
            base_de_datos1.insertar("baloncesto","deporte de 5vs5, se juega con las manos","vacio");
            base_de_datos1.insertar("voleibol","deporte de 6vs6, se juega con los pies y las manos","vacio");
            ((ComprobarSonido) this.getApplication()).setEstado("con");
            SharedPreferences.Editor editor=comprobar.edit();
            editor.putString(COMP,"SI");
            editor.apply();
        }

        preferences=getSharedPreferences(VOLUMEN,MODE_PRIVATE);
        saludo=preferences.getString(MIS_PREFERENCIAS,"");
        Toast.makeText(this, ""+saludo, Toast.LENGTH_SHORT).show();

        if(misDatos.getString(IMAGEN,"").isEmpty()==false)
        {
            Log.d("mensaje","si");
            imageButtonopciones.setImageURI(Uri.parse(misDatos.getString(IMAGEN,"")));
            estadoimagen=true;
        }

        buttoncontinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //misDatos=getSharedPreferences(NOMBRE_FICHERO, MODE_PRIVATE);
                if(misDatos.getString(CONTRASEÑA,"").isEmpty())
                {
                    if(editTextpassword.getText().toString().isEmpty())
                    {
                        Toast.makeText(MainActivity.this, "No se puede asignar una contraseña sin caracteres", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SharedPreferences.Editor editor=misDatos.edit();
                        editor.putString(CONTRASEÑA, editTextpassword.getText().toString());
                        editor.apply();
                        Toast.makeText(MainActivity.this, ""+misDatos.getString(CONTRASEÑA,""), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(editTextpassword.getText().toString().equals(misDatos.getString(CONTRASEÑA,"")))
                    {
                        if(estadoimagen==true)
                        {
                            Intent intent=new Intent(MainActivity.this,MainActivitymenu.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "ERROR, tienes que introducir una foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //Toast.makeText(MainActivity.this, "incorrecto", Toast.LENGTH_SHORT).show();
                        Animation animacion= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.deslizar);
                        buttoncontinuar.startAnimation(animacion);
                        contintentos++;
                        if (contintentos==3)
                        {
                            if(saludo.equals("con")||saludo==null)
                            {
                                mediaPlayer.setLooping(true);
                                mediaPlayer.start();
                            }

                            buttoncontinuar.setEnabled(false);
                        }
                    }
                }
            }
        });

        imageButtonopciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertdedialogos();
            }
        });
    }



    public AlertDialog alertdedialogos()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Selecciona de imagen:");
        builder.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent galeria=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galeria,VENGO_DE_LA_GALERIA);
                Toast.makeText(MainActivity.this, "galeria", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Camara", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "camara", Toast.LENGTH_SHORT).show();
                try
                {
                    PedirPermisoParaFoto();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        return builder.create();
    }

    private void PedirPermisoParaFoto() throws IOException {
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PIDO_PEROMISO_ESCRITURA);
            }
        }
        else
        {
            hacerfotocalidad();
        }
    }

    private void hacerfotocalidad() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fichero=crearficherofoto();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,"com.example.RegistroMisDeportes.fileprovider",fichero));
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,VENGO_DE_LA_CAMARA_CON_CALIDAD);
        }
        else
        {
            Toast.makeText(this,"Necesitas camara para poder hacer fotos!!",Toast.LENGTH_SHORT).show();
        }
    }

    private File crearficherofoto() throws IOException {
        String fechaYhora=new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date());
        String nombreFichero="Misfotos"+fechaYhora;
        File carpetaFotos = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        carpetaFotos.mkdirs();
        File imagenGrandResolucion=File.createTempFile(nombreFichero,".jpg",carpetaFotos);
        return imagenGrandResolucion;
    }

    void actualizargaleria(String path)
    {
        MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String s, Uri uri) {
                Log.d("ACTUALIZAR","Se ha actualizado");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==VENGO_DE_LA_CAMARA && resultCode == RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageButtonopciones.setImageBitmap(bitmap);
        }
        else if(requestCode==VENGO_DE_LA_CAMARA_CON_CALIDAD )
        {
            if(resultCode == RESULT_OK)
            {
                Uri imag= Uri.parse(fichero.getAbsolutePath());
                imageButtonopciones.setImageURI(imag);
                //imageButtonopciones.setImageBitmap(BitmapFactory.decodeFile(fichero.getAbsolutePath()));
                Log.d("mensaje",""+fichero.getAbsolutePath());
                misDatos= PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit=misDatos.edit();
                edit.putString(IMAGEN, fichero.getAbsolutePath().toString());
                edit.commit();
                estadoimagen=true;
                actualizargaleria(fichero.getAbsolutePath());
            }
            else
            {
                fichero.delete();
            }
        }
        else if(requestCode==VENGO_DE_LA_GALERIA)
        {
            Uri imagenUri=data.getData();
            Log.d("mensaje",""+imagenUri);
            misDatos= PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit=misDatos.edit();
            edit.putString(IMAGEN, imagenUri.toString());
            edit.commit();
            estadoimagen=true;
            imageButtonopciones.setImageURI(imagenUri);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY)
        {
            if(sensorEvent.values[0]==0&&(saludo==null||saludo.equals("con")))
            {
                mediaPlayer.stop();
                buttoncontinuar.setEnabled(true);
                contintentos=0;
            }
            else
            {
                buttoncontinuar.setEnabled(true);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}