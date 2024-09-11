package com.example.registromisdeportes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivitymenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitymenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menuactionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                Intent intent=new Intent(MainActivitymenu.this,MainActivitymisdeportes.class);
                startActivity(intent);
                break;
            case R.id.item2:
                Intent intent2=new Intent(MainActivitymenu.this,MainActivityagregaractividad.class);
                startActivity(intent2);
                break;
            case R.id.item3:
                Intent intent3=new Intent(MainActivitymenu.this,MainActivitylistaractividades.class);
                startActivity(intent3);
                break;
            case R.id.item4:
                Intent intent4=new Intent(MainActivitymenu.this,MainActivityconfiguracion.class);
                startActivity(intent4);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}