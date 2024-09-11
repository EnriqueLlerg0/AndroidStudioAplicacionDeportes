package com.example.registromisdeportes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class base_de_datos2 extends SQLiteOpenHelper
{

    private static final String TABLE_NAME = "ACTIVIDADES";
    private static final String COL_ID2 = "ID2";
    private static final String COL_ID_DEPORTE = "ID_DEPORTE";
    private static final String COL_FECHA = "FECHA";
    private static final String COL_HORA = "HORA";
    private static final String COL_LATITUD = "LATITUD";
    private static final String COL_LONGITUD = "LONGITUD";
    private static final String COL_DURACION = "DURACION";
    private static final String TABLE_NAME1 = "DEPORTES";
    private static final String DATABASE_NAME = "misdeportes.db";

    public base_de_datos2(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public base_de_datos2(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_ID_DEPORTE + " INTEGER,"
                + COL_FECHA + " DATE,"
                + COL_HORA + " DATE,"
                + COL_LATITUD + " TEXT,"
                + COL_LONGITUD + " TEXT,"
                + COL_DURACION + " TEXT"
                + ")");
    }

    public boolean insertar(String id_deport, String fech, String hora, String lat, String lon, String dur) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_DEPORTE, id_deport);
        contentValues.put(COL_FECHA, fech);
        contentValues.put(COL_HORA, hora);
        contentValues.put(COL_LATITUD, lat);
        contentValues.put(COL_LONGITUD, lon);
        contentValues.put(COL_DURACION, dur);

        long resultado = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        sqLiteDatabase.close(); //Cierro la BD

        return (resultado != -1); //en resultado está el número de filas afectadas
    }

    public Cursor listar() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    public Cursor listardeportes() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME1, null);
        return cursor;
    }

    public Cursor listardeportesnombre() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DEPORTE FROM " + TABLE_NAME1, null);
        return cursor;
    }

    public Cursor listarduracion(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DURACION FROM " + TABLE_NAME +" WHERE ID_DEPORTE="+id, null);
        return cursor;
    }

    public Cursor listarordenado() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME+ " ORDER BY " + COL_FECHA + ", "+ COL_HORA, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
