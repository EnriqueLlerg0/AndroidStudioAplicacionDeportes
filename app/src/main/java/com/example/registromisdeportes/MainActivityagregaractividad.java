package com.example.registromisdeportes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivityagregaractividad extends AppCompatActivity {

    Button buttonagregar;
    Spinner spinnerdeportes;
    EditText editTexttiempo;
    TextView textViewcronometro, textViewactividad;
    base_de_datos2 base_de_datosdos;
    ArrayList<String> datosdeporte;
    ArrayList<Integer> datosid;
    ArrayList<String> lat;
    ArrayList<String> lon;
    Datos movil;
    Datos2 movil2;
    String recogerdeporte = "";
    int recogerid = -1;
    private static final long TIEMPO_REFRESCO = 500;
    private static final int PERMISO_GPS = 123;
    private static final String ID_CANAL = "Nombre del canal";
    LocationManager locationManager;
    LocationListener locationListener;
    double latitud=0, longitud=0;
    MediaPlayer mediaPlayer;
    String resul = "";
    double total=0.0;
    String totaltotal="";
    SharedPreferences preferences;
    private static final String VOLUMEN = "AA";
    private static final String MIS_PREFERENCIAS = "Z";
    String saludo;
    int banderainsertar=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityagregaractividad);
        buttonagregar = findViewById(R.id.buttonagregar);
        spinnerdeportes = findViewById(R.id.spinnerdeportes);
        editTexttiempo = findViewById(R.id.editTextNumbertiempo);
        textViewcronometro = findViewById(R.id.textViewcronometro);
        textViewactividad = findViewById(R.id.textViewactividad);
        base_de_datosdos = new base_de_datos2(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitud=location.getLatitude();
                longitud=location.getLongitude();
                Log.d("entro","si");
                //Log.d("datos","tiempo: "+timeStamp+" Hora: "+horaactual+" Latitud: "+latitud+" Longitud"+longitud);

            }
        };

        if (ActivityCompat.checkSelfPermission(MainActivityagregaractividad.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivityagregaractividad.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISO_GPS);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIEMPO_REFRESCO, 0, locationListener);

        mediaPlayer=MediaPlayer.create(this,R.raw.inicioactividad);

        getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                String tiempocronometroahora = textViewcronometro.getText().toString();
                /*if(latitud==0.0&&longitud==0.0)
                {
                    Toast.makeText(MainActivityagregaractividad.this, "La latitud y loongitud no puede ser 0", Toast.LENGTH_SHORT).show();
                }*/
                /*else*/ if(tiempocronometroahora.equals("00:00"))
                {
                    Toast.makeText(MainActivityagregaractividad.this, "ERROR, el cronometro ya esta a 0", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String horaactual = dateFormat.format(date);
                    resul="";

                    int mi=0;
                    int se=0;

                    String pala2=editTexttiempo.getText().toString();
                    String[]pala=tiempocronometroahora.split(":");

                    int num1=Integer.parseInt(pala[0]);
                    int num2=Integer.parseInt(pala[1]);

                    int nume1=Integer.parseInt(pala2.substring(0,2));
                    int nume2=Integer.parseInt(pala2.substring(2,4));
                    Log.d("datos",nume1+" - "+nume2);

                    if(nume2==0)
                    {
                        nume2=60;
                    }

                    if(nume2<num2)
                    {
                        num2=60-nume2;
                        se=nume2+num2;
                        num1++;
                        mi=nume1-num1;
                        String min=Integer.toString(mi);
                        String seg;
                        if(se>=0&&se<=9)
                        {
                            seg="0"+se;
                        }
                        else
                        {
                            seg=Integer.toString(se);
                        }
                        Toast.makeText(MainActivityagregaractividad.this, min+":"+seg, Toast.LENGTH_SHORT).show();
                        resul=min+":"+seg;
                    }
                    else
                    {
                        se=nume2-num2;
                        mi=nume1-num1;

                        String min=Integer.toString(mi);
                        String seg;
                        if(se>=0&&se<=9)
                        {
                            seg="0"+se;
                        }
                        else
                        {
                            seg=Integer.toString(se);
                        }

                        Toast.makeText(MainActivityagregaractividad.this, min+":"+seg, Toast.LENGTH_SHORT).show();
                        resul=min+":"+seg;
                    }

                    //Log.d("mensaje",num1+":"+num2+" - "+nume1+":"+nume2);

                    banderainsertar=1;
                    //Toast.makeText(MainActivityagregaractividad.this, tiempocronometroahora, Toast.LENGTH_SHORT).show();
                    base_de_datosdos.insertar(datosid.get(recogerid).toString(),timeStamp,horaactual,Double.toString(latitud),Double.toString(longitud),resul);

                }
            }
        });

        mostrar(base_de_datosdos);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, datosdeporte);
        spinnerdeportes.setAdapter(adapter);
        spinnerdeportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recogerdeporte = adapterView.getSelectedItem().toString();
                recogerid = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String horaactual = dateFormat.format(date);
                resul="";
                String tiempo = editTexttiempo.getText().toString();


                if(tiempo.length()==3)
                {
                    for(int i=0;i<tiempo.length();i++)
                    {
                        String caracter= String.valueOf(tiempo.charAt(i));

                        if(i==0)
                        {
                            resul+="0";
                        }
                        if(i==1)
                        {
                            resul+=":"+caracter;
                        }
                        else
                        {
                            resul+=caracter;
                        }
                    }
                    textViewactividad.setText(recogerdeporte);
                    textViewcronometro.setText(resul);
                    if(latitud==0.0&&longitud==0.0)
                    {
                        Toast.makeText(MainActivityagregaractividad.this, "La latitud y loongitud no puede ser 0", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //base_de_datosdos.insertar(datosid.get(recogerid).toString(),timeStamp,horaactual,Double.toString(latitud),Double.toString(longitud),resul);
                    }
                    preferences=getSharedPreferences(VOLUMEN,MODE_PRIVATE);
                    saludo=preferences.getString(MIS_PREFERENCIAS,"");
                    if(saludo.equals("con")||saludo==null)
                    {
                        mediaPlayer.start();
                    }


                    editTexttiempo.setEnabled(false);
                    spinnerdeportes.setEnabled(false);
                    buttonagregar.setEnabled(false);

                    CuentraAtrasConometro cuentraAtrasConometro=new CuentraAtrasConometro(textViewcronometro,resul,editTexttiempo,spinnerdeportes,buttonagregar);
                    cuentraAtrasConometro.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else if(tiempo.length()==4)
                {
                    for(int i=0;i<tiempo.length();i++)
                    {
                        String caracter= String.valueOf(tiempo.charAt(i));
                        if(i==2)
                        {
                            resul+=":"+caracter;
                        }
                        else
                        {
                            resul+=caracter;
                        }
                    }
                    textViewactividad.setText(recogerdeporte);
                    textViewcronometro.setText(resul);
                    if(latitud==0.0&&longitud==0.0)
                    {
                        Toast.makeText(MainActivityagregaractividad.this, "La latitud y loongitud no puede ser 0", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //base_de_datosdos.insertar(datosid.get(recogerid).toString(),timeStamp,horaactual,Double.toString(latitud),Double.toString(longitud),resul);
                    }

                    preferences=getSharedPreferences(VOLUMEN,MODE_PRIVATE);
                    saludo=preferences.getString(MIS_PREFERENCIAS,"");
                    if(saludo.equals("con")||saludo==null)
                    {
                        mediaPlayer.start();
                    }

                    editTexttiempo.setEnabled(false);
                    spinnerdeportes.setEnabled(false);
                    buttonagregar.setEnabled(false);
                    CuentraAtrasConometro cuentraAtrasConometro=new CuentraAtrasConometro(textViewcronometro,resul,editTexttiempo,spinnerdeportes,buttonagregar);
                    cuentraAtrasConometro.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else
                {
                    Toast.makeText(MainActivityagregaractividad.this, "El formato para el cronometro es: mm:ss", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISO_GPS){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIEMPO_REFRESCO, 0, locationListener);
            }else{
                Toast.makeText(this, "Esta aplicación necesita este permiso para funcionar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class SettingsContentObserver extends ContentObserver {
        private AudioManager audioManager;

        public SettingsContentObserver(Context context, Handler handler) {
            super(handler);
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            Toast.makeText(MainActivityagregaractividad.this, "Volumen: "+currentVolume, Toast.LENGTH_SHORT).show();
        }
    }

    void mostrar(base_de_datos2 base_de_datosdos)
    {
        Cursor cursor = base_de_datosdos.listardeportes();
        datosdeporte=new ArrayList<>();
        datosid=new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movil = new Datos(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3));
                String columnadeporte = movil.toString();
                int columnaid = movil.getId();
                datosdeporte.add(columnadeporte);
                datosid.add(columnaid);
            }
        }
    }

    void mostrar2(base_de_datos2 base_de_datosdos)
    {
        Cursor cursor = base_de_datosdos.listar();
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
        int segbuenos=0;
        int num1=0;
        int num2=0;
        int band=0;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movil2 = new Datos2(cursor.getInt(0),cursor.getInt(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                String duracion = movil2.getDuracion();
                String fecha = movil2.getFecha();
                if(timeStamp.equals(fecha))
                {
                    String[] division=duracion.split(":");
                    Log.d("longi",""+division.length);
                    num1+=Integer.parseInt(division[0]);
                    num2=Integer.parseInt(division[1]);
                    segbuenos+=num2;
                    if(segbuenos>=60)
                    {
                        segbuenos=60-segbuenos;
                        num1++;
                    }
                    if(segbuenos>=0&&segbuenos<10)
                    {
                        band=1;
                    }
                    else
                    {
                        band=0;
                    }
                }
            }

            if(band==0)
            {
                totaltotal=num1+":"+segbuenos;
            }
            else
            {
                totaltotal=num1+":0"+segbuenos;
            }
        }
    }

    void localizacion()
    {
        lat=new ArrayList<>();
        lon=new ArrayList<>();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitud=location.getLatitude();
                longitud=location.getLongitude();

                //Log.d("datos","tiempo: "+timeStamp+" Hora: "+horaactual+" Latitud: "+latitud+" Longitud"+longitud);

            }
        };

        if (ActivityCompat.checkSelfPermission(MainActivityagregaractividad.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivityagregaractividad.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISO_GPS);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIEMPO_REFRESCO, 0, locationListener);
    }
    //"Enhorabuena, has terminado tu actividad de "+00+"\nCon esta actividad llevas "+0+" minutos de actividad en el dia de hoy"
    private void lanzarNotificacionTextoLargo()
    {
        mostrar2(base_de_datosdos);
        String idChannel="Canal 2";
        String nombrecanal="Canal texto largo";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CANAL);
        builder.setSmallIcon(R.drawable.imagennotificacion).setContentTitle("Deporte").setAutoCancel(false).setContentText("");
        NotificationCompat.BigTextStyle bigTextStyle=new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Cantidad de deporte");
        bigTextStyle.bigText("Enhorabuena, has terminado tu actividad de "+resul+" Con esta actividad llevas "+totaltotal+" minutos de actividad en el dia de hoy ");
        bigTextStyle.setSummaryText("Sigue asi");
        builder.setStyle(bigTextStyle);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {

            NotificationChannel notificationChannel=new NotificationChannel(idChannel,nombrecanal, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);

            builder.setChannelId(idChannel);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        else
        {
            //Esto se utiliza para versiones anteriores a oreo, este modo ya esta obsoleto
            builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS);
        }

        notificationManager.notify(2, builder.build());
    }

    class CuentraAtrasConometro extends AsyncTask<String, String, String>
    {
        TextView textView;
        EditText textView2;
        String numactual;
        String palabra="";
        Spinner spinner;
        Button button;

        public CuentraAtrasConometro(TextView textView, String numactual, EditText textView2, Spinner spinner, Button button)
        {
            this.textView=textView;
            this.numactual=numactual;
            this.textView2=textView2;
            this.spinner=spinner;
            this.button=button;
        }

        @Override
        protected String doInBackground(String... strings) {
            String[] num=numactual.split(":");
            int num1=Integer.parseInt(num[0]);
            Log.d("num1",""+num1);
            int num2=Integer.parseInt(num[1]);
            Log.d("num2",""+num2);

            while(num1>=0)
            {
                num2--;
                if(num2<=0)
                {
                    num1--;
                    num2=59;
                }
                palabra=num1+":"+num2;
                if(num1<0)
                {
                    palabra="00:00";
                }
                if(banderainsertar==1)
                {
                    break;
                }
                publishProgress(palabra);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return null;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.d("tiempo",""+palabra);
            textView.setText(palabra);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("entro","si");
            textView2.setEnabled(true);
            spinner.setEnabled(true);
            button.setEnabled(true);

            String palab=editTexttiempo.getText().toString();

            resul=palab.substring(0,2)+":"+palab.substring(2,4);

            textViewcronometro.setText("00:00");

            String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String horaactual = dateFormat.format(date);

            String tiempo = editTexttiempo.getText().toString();

            if(banderainsertar!=1)
            {
                base_de_datosdos.insertar(datosid.get(recogerid).toString(), timeStamp, horaactual, Double.toString(latitud), Double.toString(longitud), resul);
            }
            banderainsertar=0;
            lanzarNotificacionTextoLargo();
        }
    }
}