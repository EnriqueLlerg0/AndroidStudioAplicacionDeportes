package com.example.registromisdeportes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivityconfiguracion extends AppCompatActivity
{
    private static final String ID_CANAL = "Nombre del canal";
    private static final String VOLUMEN = "AA";
    private static final String MIS_PREFERENCIAS = "Z";
    ImageButton imageButtonvolumen;
    ListView listViewlogros;
    int bandera=0;
    base_de_datos base_de_datos1;
    base_de_datos2 base_de_datos2;
    Datos datos1;
    Datos2 datos2;
    ArrayList<Integer>id;
    ArrayList<String>deporte;
    ArrayList<String>duracion;
    ArrayList<String>duraciontotal;
    ArrayList<mostrarvaloreslogros>mostrarvaloresdefinitiva;
    ArrayList<String>nombresdeportes;
    SharedPreferences preferences;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    int dosposiciones=0;
    Datos movil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityconfiguracion);
        imageButtonvolumen=findViewById(R.id.imageButtonvolumen);
        listViewlogros=findViewById(R.id.ListViewlogros);
        base_de_datos1=new base_de_datos(this);
        base_de_datos2=new base_de_datos2(this);

        rellenararraylist(base_de_datos2);

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor==null)
        {
            Toast.makeText(this, "ERROR, no tienes el sensor requerido", Toast.LENGTH_SHORT).show();
        }
        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent)
            {
                double y=sensorEvent.values[1];
                if(y<-5&&dosposiciones==0)
                {
                    dosposiciones++;
                }
                else if(y>5&&dosposiciones==1)
                {
                    dosposiciones++;
                }
                if(dosposiciones==2)
                {
                    dosposiciones=0;
                    generarnotificacion(nombresdeportes);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        start();

        recogerdeporteeid(base_de_datos1);
        recogerduracion(base_de_datos2);
        mostrar(listViewlogros);

        preferences=this.getSharedPreferences(VOLUMEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=preferences.edit();

        preferences=getSharedPreferences(VOLUMEN,MODE_PRIVATE);
        String saludo=preferences.getString(MIS_PREFERENCIAS,"");
        if(saludo==null)
        {
            imageButtonvolumen.setImageResource(R.drawable.convolumen);
            edit.putString(MIS_PREFERENCIAS,"con");
            edit.apply();
        }
        else
        {
            if(saludo.equals("con"))
            {
                imageButtonvolumen.setImageResource(R.drawable.convolumen);
            }
            else
            {
                imageButtonvolumen.setImageResource(R.drawable.sinvolumen);
            }
        }

        imageButtonvolumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                preferences=getSharedPreferences(VOLUMEN,MODE_PRIVATE);
                String saludo=preferences.getString(MIS_PREFERENCIAS,"");
                if(saludo.equals("sin"))
                {
                    imageButtonvolumen.setImageResource(R.drawable.convolumen);
                    edit.putString(MIS_PREFERENCIAS,"con");
                    edit.apply();
                    Log.d("estado",saludo);
                }
                else
                {
                    imageButtonvolumen.setImageResource(R.drawable.sinvolumen);
                    edit.putString(MIS_PREFERENCIAS,"sin");
                    edit.apply();
                    Log.d("estado",saludo);
                }
            }

        });
    }

    void generarnotificacion(ArrayList<String>nombresdeportes)
    {
        String palabra=aleatorio(nombresdeportes);
        String idChannel="Canal 2";
        String nombrecanal="Canal texto largo";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CANAL);
        builder.setSmallIcon(R.drawable.imagennotificacion).setContentTitle("Deporte").setAutoCancel(false).setContentText("");
        NotificationCompat.BigTextStyle bigTextStyle=new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("DEPORTE");
        bigTextStyle.bigText("Es buen momento para hacer "+palabra);
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

    String aleatorio(ArrayList<String>nombresdeportes)
    {
        int num=(int)(Math.floor(Math.random()*((nombresdeportes.size()-1)-0+1)+0));
        String palabra=nombresdeportes.get(num);
        return palabra;
    }
    void start()
    {
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    void stop()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause() {
        stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        start();
        super.onResume();
    }

    void rellenararraylist(base_de_datos2 base_de_datosdos)
    {
        Cursor cursor = base_de_datosdos.listardeportesnombre();
        nombresdeportes=new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movil = new Datos(cursor.getString(0));
                String columnadeporte = movil.toString();
                nombresdeportes.add(columnadeporte);
            }
        }
    }

    void mostrar(ListView listView)
    {
        mostrarvaloreslogros mostrarvaloreslogros1;
        mostrarvaloresdefinitiva=new ArrayList<>();
        for(int i=0;i<deporte.size();i++)
        {
            //Log.d("datos"," - "+duracion.get(i));
            mostrarvaloreslogros1=new mostrarvaloreslogros(deporte.get(i),duraciontotal.get(i));
            mostrarvaloresdefinitiva.add(mostrarvaloreslogros1);
        }

        Adaptadorparalistview adaptadorparalistview=new Adaptadorparalistview(this,R.layout.mostrarlogros,mostrarvaloresdefinitiva);
        listView.setAdapter(adaptadorparalistview);
    }

    void recogerdeporteeid(base_de_datos base_de_datos1)
    {
        Cursor cursor=base_de_datos1.listarnombredeporteseid();
        id=new ArrayList<>();
        deporte=new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                datos1 = new Datos(cursor.getInt(0), cursor.getString(1));
                Log.d("mensaje",datos1.getId()+" - "+datos1.getDeporte());
                id.add(datos1.getId());
                deporte.add(datos1.getDeporte());
            }
        }
    }

    void recogerduracion(base_de_datos2 base_de_datos2)
    {
        duraciontotal=new ArrayList<>();
        int bandera=0;
        for(int i=0;i<id.size();i++)
        {
            int seg=0;
            int min=0;
            Cursor cursor=base_de_datos2.listarduracion(id.get(i));
            duracion=new ArrayList<>();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    datos2 = new Datos2(cursor.getString(0));
                    String dato=datos2.getDuracion();
                    String[]tiempo=dato.split(":");
                    int num1=Integer.parseInt(tiempo[0]);
                    int num2=Integer.parseInt(tiempo[1]);
                    min+=num1;
                    seg+=num2;
                    if(seg>=60)
                    {
                        min++;
                        seg=seg-60;
                    }
                    if(seg>=0&&seg<10)
                    {
                        bandera=1;
                    }
                    else
                    {
                        bandera=0;
                    }
                }
                Log.d("mensaje",min+":"+seg);
                if(bandera==0)
                {
                    duraciontotal.add(min+":"+seg);
                }
                else
                {
                    duraciontotal.add(min+":0"+seg);
                }
            }
            else
            {
                duraciontotal.add("00:00");
            }
        }
    }

    class Adaptadorparalistview extends ArrayAdapter<mostrarvaloreslogros> {


        public Adaptadorparalistview(@NonNull Context context, int resource, @NonNull List<mostrarvaloreslogros> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return super.getDropDownView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View miFila = inflater.inflate(R.layout.mostrarlogros, parent, false);

            TextView deport=miFila.findViewById(R.id.textViewdeporte);
            TextView dur=miFila.findViewById(R.id.textViewduracion2);
            ImageButton imageButton=miFila.findViewById(R.id.imageButtoncompartir);

            deport.setText(deporte.get(position));
            dur.setText(duraciontotal.get(position));
            imageButton.setImageResource(R.drawable.compartir);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, deporte.get(position)+" => "+duraciontotal.get(position));
                    intent.setPackage("com.whatsapp");
                    try {
                        startActivity(intent);
                    }catch (ActivityNotFoundException e){
                        Toast.makeText(MainActivityconfiguracion.this, "Debes de instalar whatsapp", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return miFila;
        }
    }
}