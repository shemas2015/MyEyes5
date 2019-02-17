package com.myeyes.myeyes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;

public class MainActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);




        /*



        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Cargó OpenCV",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"No Cargó OpenCV",Toast.LENGTH_LONG).show();
        }


         */

        OpenCvCamera camera = new OpenCvCamera(this);
        camera.iniciar();

        /*
        OpenCvStorage open = new OpenCvStorage(this);
        for(int i =1;i<=4;i++){
            open.setNumDisparity(16*i);
            open.setBlockSize((5-i)*i);
            Mat disparity = open.disparityMap();
            open.SaveImage(disparity,"img_"+i);
        }
         */








    // Example of a call to a native method
    TextView tv = (TextView) findViewById(R.id.sample_text);
    tv.setText(validate(0L,0L));
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String validate(long matAddrGr,long matAddrRgba);

    public native void grises(long imgMat);


    public native void disparityMap(long imgLeft,long imgRight);








    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }
}
