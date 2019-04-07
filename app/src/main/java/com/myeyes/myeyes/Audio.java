package com.myeyes.myeyes;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Audio implements TextToSpeech.OnInitListener {
    MainActivity activity= null;
    private TextToSpeech tts;
    private Date espera =null;

    public Audio(MainActivity activity){
        espera = Calendar.getInstance().getTime();
        this.activity = activity;
        tts = new TextToSpeech(activity,this);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.getDefault());
            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS","Este lenguaje no es soportado!");
                System.out.println("Error: Este lenguaje no es soportado!");
            } else {
                leer("Aplicación iniciada");
            }
        } else {
            Log.e("TTS","Inicializacion del lenguaje ha fallado!");
            System.out.println("Error: Este lenguaje no es soportado!");
        }
    }

    public void leer(String txt){
        Date ahora = Calendar.getInstance().getTime();
        float tiempo = (ahora.getTime() -espera.getTime()) / 1000;
        String text = txt;
        if(tiempo > 2){
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
            this.espera = ahora;
        }

    }

}
