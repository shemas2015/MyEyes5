package com.myeyes.myeyes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class Database extends SQLiteOpenHelper {
    public Database(Context contexto, String nombre, CursorFactory factory,int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE audios (clase VARCHAR(50),recurso VARCHAR(50))";
        db.execSQL(sqlCreate);

        sqlCreate = "insert into audios  values ('tvmonitor','monitor')";
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
