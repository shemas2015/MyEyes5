package com.myeyes.myeyes;

import android.app.Activity;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;


public class Sonido{
    MediaPlayer sonido;
    public Sonido(MainActivity activity){
        //busca el nombre del sonido que le corresponde a la clase

        //Carga el objeto media desde la carpeta de recursos
        //sonido =  MediaPlayer.create(activity,R.raw.sound);


        Resources res = activity.getResources(); //resource handle
        String url = "sound";
        InputStream is = getClass().getClassLoader().getResourceAsStream("raw/");
        Integer resIdSound = res.getIdentifier (url,  "raw", activity.getPackageName());
        this.sonido = MediaPlayer.create(activity, resIdSound);



    }

    /**
     * Reproduce el sonido
     */
    public void start(){
        this.sonido.start();
    }




}
