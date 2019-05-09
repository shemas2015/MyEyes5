package com.myeyes.myeyes.entidades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myeyes.myeyes.MainActivity;

public class Obstaculo {
    private int id;
    private String class_name;
    private Database conn;
    private MainActivity mainActivity;

    public String getNombre() {
        return nombre;
    }

    private String nombre;


    public Obstaculo(String class_name, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        conn= new Database(mainActivity.getApplicationContext());

        //Consulta los parámetros
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {class_name};
        String[] campos = {"nombre"};

        Cursor cursor = db.query("objeto",campos,"class_name =?",parametros,null,null,null);

        cursor.moveToFirst();
        this.nombre = cursor.getString(cursor.getColumnIndex("nombre"));
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }


}
