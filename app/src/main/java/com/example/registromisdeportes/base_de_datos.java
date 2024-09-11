package com.example.registromisdeportes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class base_de_datos extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "misdeportes.db";
    private static final String TABLE_NAME = "DEPORTES";
    private static final String COL_ID = "ID";
    private static final String COL_DEPORTE = "DEPORTE";
    private static final String COL_DESCRIPCION = "DESCRIPCION";
    private static final String COL_IMAGEN = "IMAGEN";
    private static final String TABLE_NAME2 = "ACTIVIDADES";
    private static final String COL_ID2 = "ID2";
    private static final String COL_ID_DEPORTE = "ID_DEPORTE";
    private static final String COL_FECHA = "FECHA";
    private static final String COL_HORA = "HORA";
    private static final String COL_LATITUD = "LATITUD";
    private static final String COL_LONGITUD = "LONGITUD";
    private static final String COL_DURACION = "DURACION";

    public base_de_datos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public base_de_datos(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_DEPORTE + " TEXT,"
                + COL_DESCRIPCION + " TEXT,"
                + COL_IMAGEN + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME2 + " (" + COL_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_ID_DEPORTE + " INTEGER,"
                + COL_FECHA + " DATE,"
                + COL_HORA + " DATE,"
                + COL_LATITUD + " TEXT,"
                + COL_LONGITUD + " TEXT,"
                + COL_DURACION + " TEXT"
                + ")");

    }

    public boolean insertar(String deport, String descrip, String imag) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DEPORTE, deport);
        contentValues.put(COL_DESCRIPCION, descrip);
        contentValues.put(COL_IMAGEN, imag);

        long resultado = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        sqLiteDatabase.close(); //Cierro la BD

        return (resultado != -1); //en resultado está el número de filas afectadas
    }

    public Cursor listar() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    public Cursor listarnombredeportes(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DEPORTE FROM " + TABLE_NAME +" WHERE ID="+id, null);
        return cursor;
    }

    public Cursor listarnombredeporteseid() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT ID,DEPORTE FROM " + TABLE_NAME, null);
        return cursor;
    }

    public boolean actualizar(String id,String deporte, String descripcion) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DEPORTE, deporte);
        contentValues.put(COL_DESCRIPCION, descripcion);

        long resultado = sqLiteDatabase.update(TABLE_NAME, contentValues, COL_ID + "=?", new String[]{id});
        sqLiteDatabase.close();

        return (resultado > 0);

    }

    public boolean actualizarsoloimagen(String id, String imagen) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGEN, imagen);

        long resultado = sqLiteDatabase.update(TABLE_NAME, contentValues, COL_ID + "=?", new String[]{id});
        sqLiteDatabase.close();

        return (resultado > 0);

    }

    public boolean borrar(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int borradas = sqLiteDatabase.delete( TABLE_NAME,COL_ID+"=?",new String[]{id});
        sqLiteDatabase.close();
        return borradas>0;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
