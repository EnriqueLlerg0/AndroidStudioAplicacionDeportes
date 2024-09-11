package com.example.registromisdeportes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivitylistaractividades extends AppCompatActivity {

    ArrayList<clasemostrardatosactividades> mostrarvalores;
    ListView listView;
    base_de_datos2 base_de_datosdos;
    base_de_datos base_de_datos1;
    Datos2 movil2;
    Datos movil;
    String nombre="";
    String fecha="";
    String hora="";
    String longi="";
    String lati="";
    ArrayList<String>lon;
    ArrayList<String>lat;
    String duracion="";
    int pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitylistaractividades);
        listView=findViewById(R.id.ListViewLista);
        base_de_datosdos=new base_de_datos2(this);
        base_de_datos1=new base_de_datos(this);

        mostrar(base_de_datosdos, base_de_datos1, listView);

    }

    void mostrar(base_de_datos2 base_de_datosdos, base_de_datos base_de_datos1, ListView listView)
    {
        Cursor cursor=base_de_datosdos.listarordenado();
        clasemostrardatosactividades dato1;
        mostrarvalores=new ArrayList<>();
        lon=new ArrayList<>();
        lat=new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movil2 = new Datos2(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                int id = movil2.getId2();
                nombre=mostrarnombre(base_de_datos1,id);
                fecha = movil2.getFecha();
                hora = movil2.getHora();
                longi=movil2.getLongitud();
                lati=movil2.getLatitud();
                lon.add(movil2.getLongitud());
                lat.add(movil2.getLatitud());
                duracion = movil2.getDuracion();

                dato1 = new clasemostrardatosactividades(nombre, fecha, hora);
                mostrarvalores.add(dato1);
            }
            Adaptadorparalistview adaptadorparalistview=new Adaptadorparalistview(this,R.layout.listviewlistaractividades,mostrarvalores);
            listView.setAdapter(adaptadorparalistview);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    pos=i;
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivitylistaractividades.this);
                    builder.setMessage("Nombre: "+nombre+"\nFecha: "+fecha+"\nHora: "+hora+"\nLongitud: "+longi+"\nLatitud: "+lati+"\nDuracion: "+duracion);
                    builder.setPositiveButton("Aceptar",null);
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
            });
        }
        else
        {
            Adaptadorparalistview adaptadorparalistview=new Adaptadorparalistview(this,R.layout.listviewlistaractividades,mostrarvalores);
            listView.setAdapter(adaptadorparalistview);
        }
    }

    String mostrarnombre(base_de_datos base_de_datos1, int num)
    {
        Cursor cursor = base_de_datos1.listarnombredeportes(num);
        String columnadeporte="";
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movil = new Datos(cursor.getString(0));
                columnadeporte = movil.getDeporte();
            }
        }
        return columnadeporte;
    }

    class Adaptadorparalistview extends ArrayAdapter<clasemostrardatosactividades> {

        public Adaptadorparalistview(@NonNull Context context, int resource, @NonNull List<clasemostrardatosactividades> objects) {
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
            View miFila = inflater.inflate(R.layout.listviewlistaractividades, parent, false);

            TextView deporte = miFila.findViewById(R.id.textViewnombre);
            TextView fecha = miFila.findViewById(R.id.textViewfecha);
            TextView duracion = miFila.findViewById(R.id.textViewduracion);
            ImageButton imageButton = miFila.findViewById(R.id.imageButtonmapa);

            String dato1 = mostrarvalores.get(position).getNombre_deporte();
            String dato2 = mostrarvalores.get(position).getFecha();
            String dato3 = mostrarvalores.get(position).getDuracion();

            deporte.setText(dato1);
            fecha.setText(dato2);
            duracion.setText(dato3);
            imageButton.setImageResource(R.drawable.mapa);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent3=new Intent(Intent.ACTION_VIEW);

                    Log.d("mensaje","pos:"+pos);
                    for(int i=0;i<lat.size();i++)
                    {
                        Log.d("posicion",lat.get(i)+" - "+lon.get(i));
                    }
                    intent3.setData(Uri.parse("geo:"+lat.get(position)+","+lon.get(position)));
                    startActivity(intent3);
                }
            });

            return miFila;
        }
    }
}