package com.myeyes.myeyes.entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;



public class Database extends SQLiteOpenHelper {


    public Database(Context contexto) {

        super(contexto, "my_eyes.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE objeto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "class_name  TEXT UNIQUE," +
                "nombre TEXT)");

        db.execSQL("CREATE TABLE obstaculo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_objeto  INTEGER," +
                "longitud TEXT," +
                "latitud TEXT)"
                );

        ContentValues values = new ContentValues();

        values.put("class_name","car");
        values.put("nombre","vehículo");
        db.insert("objeto","id",values);

        values.put("class_name","aeroplane");
        values.put("nombre","Avión");
        db.insert("objeto","id",values);

        values.put("class_name","bicycle");
        values.put("nombre","Bicicleta");
        db.insert("objeto","id",values);

        values.put("class_name","boat");
        values.put("nombre","Bote");
        db.insert("objeto","id",values);

        values.put("class_name","bottle");
        values.put("nombre","Botella");
        db.insert("objeto","id",values);

        values.put("class_name","chair");
        values.put("nombre","Silla");
        db.insert("objeto","id",values);


        values.put("class_name","diningtable");
        values.put("nombre","Comedor");
        db.insert("objeto","id",values);

        values.put("class_name","dog");
        values.put("nombre","Perro");
        db.insert("objeto","id",values);


        values.put("class_name","motorbike");
        values.put("nombre","Motocicleta");
        db.insert("objeto","id",values);

        values.put("class_name","person");
        values.put("nombre","Persona");
        db.insert("objeto","id",values);


        values.put("class_name","train");
        values.put("nombre","Tren");
        db.insert("objeto","id",values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion != newVersion){
            System.out.println("Atenc: Cambio de versión");
            db.execSQL("DROP TABLE IF EXISTS objeto");
            onCreate(db);
        }
    }
}
