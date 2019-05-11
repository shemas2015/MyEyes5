package com.myeyes.myeyes.entidades;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myeyes.myeyes.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Obstaculo {
    private Database conn;
    private int idObjeto;
    private String longitud;
    private String latitud;
    private MainActivity mainActivity;
    SQLiteDatabase db;

    public Obstaculo(int idObjeto, String longitud, String latitud, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        conn= new Database(mainActivity.getApplicationContext());

        //Consulta los parámetros
        db = conn.getReadableDatabase();


        this.idObjeto = idObjeto;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    /**
     * Almacena un nuevo obstaculo
     */
    public void guardar(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put("id_objeto",this.idObjeto);
        contentValues.put("latitud",this.latitud);
        contentValues.put("fecha", formatter.format(date) );
        contentValues.put("longitud",this.longitud);

        this.db.insert("obstaculo","id",contentValues);
    }
}
