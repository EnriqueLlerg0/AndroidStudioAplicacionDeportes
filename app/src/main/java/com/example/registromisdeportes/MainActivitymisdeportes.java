package com.example.registromisdeportes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivitymisdeportes extends AppCompatActivity {

    private static final int VENGO_DE_LA_GALERIA = 1;
    private static final String PRIMERA_VEZ = "A";
    private static final String COMP = "B";
    ListView listViewlista;
    ArrayList <Datos> mostrardatos;
    ArrayList <String> mostrardescripcion;
    ArrayList <Integer> mostrarid;
    ArrayList <String> mostrarimagen;
    ArrayList <claseimagendeportes> recogervalores;
    Button buttoninsertar;
    EditText textViewdeporte, textViewdescripcion;
    base_de_datos base_de_datos1;
    Datos movil;
    int pos=0;
    List<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitymisdeportes);
        listViewlista=findViewById(R.id.listViewLista);
        buttoninsertar=findViewById(R.id.buttonInsertar);
        textViewdeporte=findViewById(R.id.editTextTextDeporte);
        textViewdescripcion=findViewById(R.id.editTextTextDescripcion);
        base_de_datos1=new base_de_datos(this);

        mostrar(base_de_datos1,listViewlista,textViewdeporte,textViewdescripcion);
        buttoninsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean resultado = base_de_datos1.insertar(textViewdeporte.getText().toString(), textViewdescripcion.getText().toString(),"vacio");

                if (resultado) {
                    Toast.makeText(MainActivitymisdeportes.this, "Se ha insertado correctamente", Toast.LENGTH_SHORT).show();
                    mostrar(base_de_datos1,listViewlista,textViewdeporte,textViewdescripcion);
                }
                else {
                    Toast.makeText(MainActivitymisdeportes.this, "Error en la inserción", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerForContextMenu(listViewlista);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Opciones: ");
        menu.add(0,v.getId(),0,"Eliminar");
        menu.add(0,v.getId(),0,"Modificar");
        menu.add(0,v.getId(),0,"Mostrar descripcion");
        menu.add(0,v.getId(),0,"Cambiar imagen");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posicionLista=info.position;
        pos=posicionLista;
        if(item.getTitle()=="Mostrar descripcion")
        {
            //Toast.makeText(this,"Compramos: "+graficas[posicionLista],Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Descripcion de "+mostrardatos.get(posicionLista));
            builder.setMessage(mostrardescripcion.get(posicionLista));
            builder.setPositiveButton("Aceptar",null);
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        if(item.getTitle()=="Modificar")
        {
            boolean resultado = base_de_datos1.actualizar(Integer.toString(mostrarid.get(posicionLista)), textViewdeporte.getText().toString(), textViewdescripcion.getText().toString());
            Toast.makeText(MainActivitymisdeportes.this, resultado?"Modificado correctamente":"No se ha modificado nada", Toast.LENGTH_SHORT).show();
            mostrar(base_de_datos1,listViewlista,textViewdeporte,textViewdescripcion);
        }
        if(item.getTitle()=="Eliminar")
        {
            boolean borrado = base_de_datos1.borrar(Integer.toString(mostrarid.get(posicionLista)));
            Toast.makeText(MainActivitymisdeportes.this, borrado?"Borrado correctamente":"Nada a borrar", Toast.LENGTH_SHORT).show();
            mostrar(base_de_datos1,listViewlista,textViewdeporte,textViewdescripcion);
        }
        if(item.getTitle()=="Cambiar imagen")
        {
            Intent galeria=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galeria,VENGO_DE_LA_GALERIA);
        }
        return true;
    }

    void mostrar(base_de_datos base_de_datos1, ListView listViewLista, TextView editTextdeportes, TextView editTextdescripcion)
    {
        Cursor cursor = base_de_datos1.listar();
        mostrardatos = new ArrayList<>();
        mostrardescripcion=new ArrayList<>();
        mostrarid=new ArrayList<>();
        recogervalores=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;
        claseimagendeportes dato1;
        lista = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movil = new Datos(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3));
                String fila = movil.toString();
                String fila2="Deporte: "+fila;
                lista.add(fila2);
                mostrardatos.add(movil);
                mostrardescripcion.add(movil.getDescripcion());
                mostrarid.add(movil.getId());

                    dato1=new claseimagendeportes(fila2,movil.getImagen());
                    recogervalores.add(dato1);

            }
            //arrayAdapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lista);
            AdaptadorParaListview adaptadorParaListview=new AdaptadorParaListview(this,R.layout.listviewdiseno,recogervalores);
            listViewLista.setAdapter(adaptadorParaListview);
            listViewLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    editTextdeportes.setText(mostrardatos.get(i).getDeporte());
                    editTextdescripcion.setText(mostrardatos.get(i).getDescripcion());

                }
            });
        }else{
            //arrayAdapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lista);
            AdaptadorParaListview adaptadorParaListview=new AdaptadorParaListview(this,R.layout.listviewdiseno,recogervalores);
            listViewLista.setAdapter(adaptadorParaListview);

        }
    }

    private class AdaptadorParaListview extends ArrayAdapter<claseimagendeportes>
    {


        public AdaptadorParaListview(@NonNull Context context, int resource, @NonNull List<claseimagendeportes> objects) {
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
            View miFila = inflater.inflate(R.layout.listviewdiseno,parent, false);

            TextView deporte = miFila.findViewById(R.id.textViewlista);
            ImageView imageView = miFila.findViewById(R.id.imageViewimagenlista);

            String dato1=recogervalores.get(position).getDeportes();
            Log.d("dato1",""+dato1);
            String dato2=recogervalores.get(position).getImagen();
            Log.d("dato2",""+dato2);
            if(dato2.equals("vacio"))
            {
                Log.d("entro","si");
                imageView.setImageResource(R.drawable.galeriaimagen);
            }
            else
            {
                Log.d("entro","no");
                imageView.setImageURI(Uri.parse(dato2));
            }
            deporte.setText(dato1);
            return miFila;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==VENGO_DE_LA_GALERIA)
        {
            Uri imagenUri=data.getData();
            Log.d("mensaje",""+imagenUri);
            boolean resultado = base_de_datos1.actualizarsoloimagen(Integer.toString(mostrarid.get(pos)), ""+imagenUri);
            Toast.makeText(MainActivitymisdeportes.this, resultado?"Modificado imagen correctamente":"No se ha modificado nada", Toast.LENGTH_SHORT).show();
            mostrar(base_de_datos1,listViewlista,textViewdeporte,textViewdescripcion);
            //imageButtonopciones.setImageURI(imagenUri);
        }
    }
}