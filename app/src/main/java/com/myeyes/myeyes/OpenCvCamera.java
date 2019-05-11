package com.myeyes.myeyes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.SurfaceView;
import android.widget.Toast;

import com.myeyes.myeyes.entidades.Database;
import com.myeyes.myeyes.entidades.Objeto;
import com.myeyes.myeyes.entidades.Obstaculo;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpenCvCamera extends Imagen  implements CameraBridgeViewBase.CvCameraViewListener2  {

    CameraBridgeViewBase cameraBridgeViewBase;
    Scalar scalarLow, scalarHight;
    MainActivity mainActivity = null;
    boolean tmp = true;
    SQLiteDatabase db;

    BaseLoaderCallback baseLoaderCallback;
    private static final int MY_CAMERA_REQUEST_CODE = 100;



    private static final String TAG = "OpenCV/Sample/MobileNet";
    private Date espera =null;

    private static final String[] classNames = {"background",
            "aeroplane", "bicycle", "bird", "boat","bottle", "bus",
            "car", "cat", "chair","cow", "diningtable","dog", "horse",
            "motorbike", "person", "pottedplant","sheep", "sofa", "train", "tvmonitor"};



    private Map<String, Objeto> obstaculos = new HashMap<String, Objeto>();



    private Net net;
    private CameraBridgeViewBase mOpenCvCameraView;



    Audio audio = null;
    Localizacion localizacion = null;

    public OpenCvCamera(MainActivity mainActivity) {
        super(mainActivity);
        this.mainActivity = mainActivity;
        espera = Calendar.getInstance().getTime();

        //DB
        this.db = new Database(mainActivity.getApplicationContext()).getWritableDatabase() ;

        //Crea lista de objetos
        obstaculos.put("car",new Objeto("car",mainActivity));
        obstaculos.put("aeroplane",new Objeto("aeroplane",mainActivity));
        obstaculos.put("bicycle",new Objeto("bicycle",mainActivity));
        obstaculos.put("boat",new Objeto("boat",mainActivity));
        obstaculos.put("bottle",new Objeto("bottle",mainActivity));
        obstaculos.put("chair",new Objeto("chair",mainActivity));
        obstaculos.put("diningtable",new Objeto("diningtable",mainActivity));
        obstaculos.put("dog",new Objeto("dog",mainActivity));
        obstaculos.put("motorbike",new Objeto("motorbike",mainActivity));
        obstaculos.put("person",new Objeto("person",mainActivity));
        obstaculos.put("train",new Objeto("train",mainActivity));



        //Permisos de la cámara
        if (this.mainActivity.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            this.mainActivity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);

            //Inicia la actividad nuevamente
            this.mainActivity.finish();
            this.mainActivity.startActivity(this.mainActivity.getIntent());
        }

        localizacion = new Localizacion(this.mainActivity);



        audio = new Audio(mainActivity);

    }


    public void iniciar(){


        cameraBridgeViewBase = (JavaCameraView) this.mainActivity.findViewById(R.id.myCameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);

        scalarLow = new Scalar(45, 20, 10);
        scalarHight = new Scalar(75, 255, 255);
        cameraBridgeViewBase.setCvCameraViewListener(this);


        baseLoaderCallback = new BaseLoaderCallback(this.mainActivity) {
            @Override
            public void onManagerConnected(int status) {

                switch (status){
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }

            }

        };

        if(!OpenCVLoader.initDebug()){
            Toast.makeText(this.mainActivity.getApplicationContext(),"Hay un problema con OpenCV",Toast.LENGTH_LONG).show();

        }else{
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {


        String proto = getPath("MobileNetSSD_deploy.prototxt");
        String weights = getPath("MobileNetSSD_deploy.caffemodel");




        net = Dnn.readNetFromCaffe(proto, weights);




    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final int IN_WIDTH = 300;
        final int IN_HEIGHT = 300;
        final float WH_RATIO = (float)IN_WIDTH / IN_HEIGHT;
        final double IN_SCALE_FACTOR = 0.007843;
        final double MEAN_VAL = 127.5;
        final double THRESHOLD = 0.95;
        // Get a new frame
        Mat frame = inputFrame.rgba();


        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);



        
        // Forward image through network.
        Mat blob = Dnn.blobFromImage(frame, IN_SCALE_FACTOR,
                new Size(IN_WIDTH, IN_HEIGHT),
                new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), false);





        net.setInput(blob);
        Mat detections = net.forward();

        int cols = frame.cols();
        int rows = frame.rows();
        Size cropSize;
        if ((float)cols / rows > WH_RATIO) {
            cropSize = new Size(rows * WH_RATIO, rows);
        } else {
            cropSize = new Size(cols, cols / WH_RATIO);
        }
        int y1 = (int)(rows - cropSize.height) / 2;
        int y2 = (int)(y1 + cropSize.height);
        int x1 = (int)(cols - cropSize.width) / 2;
        int x2 = (int)(x1 + cropSize.width);
        Mat subFrame = frame.submat(y1, y2, x1, x2);
        cols = subFrame.cols();
        rows = subFrame.rows();
        detections = detections.reshape(1, (int)detections.total() / 7);



        for (int i = 0; i < detections.rows(); ++i) {
            double confidence = detections.get(i, 2)[0];


            int classId = (int)detections.get(i, 1)[0];
            Objeto objetoTmp = null ;

            try{
                objetoTmp = obstaculos.get(classNames[classId]);
            }catch (Exception e){}




            if (confidence > THRESHOLD &&  objetoTmp != null
                    && this.verifica(classNames[classId] )) {
                int xLeftBottom = (int)(detections.get(i, 3)[0] * cols);
                int yLeftBottom = (int)(detections.get(i, 4)[0] * rows);
                int xRightTop   = (int)(detections.get(i, 5)[0] * cols);
                int yRightTop   = (int)(detections.get(i, 6)[0] * rows);
                // Draw rectangle around detected object.
                Imgproc.rectangle(subFrame, new Point(xLeftBottom, yLeftBottom),
                        new Point(xRightTop, yRightTop),
                        new Scalar(0, 255, 0));



                String label = classNames[classId] + ": " + confidence;

                audio.leer(objetoTmp.getNombre()+(int)(confidence*100)+"%" );


                //Obtiene coordenadas


                //almacena en la db
                System.out.println(localizacion);

                Obstaculo obstaculo = new Obstaculo(objetoTmp.getId(),""+localizacion.getLongitud(),""+localizacion.getLatitud(),mainActivity);
                obstaculo.guardar();

                int[] baseLine = new int[1];
                Size labelSize = Imgproc.getTextSize(label, Core.FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);
                // Draw background for label.
                Imgproc.rectangle(subFrame, new Point(xLeftBottom, yLeftBottom - labelSize.height),
                        new Point(xLeftBottom + labelSize.width, yLeftBottom + baseLine[0]),
                        new Scalar(255, 255, 255), Core.FILLED);
                // Write class name and confidence.
                Imgproc.putText(subFrame, label, new Point(xLeftBottom, yLeftBottom),
                        Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0));
            }

        }

        return frame;

    }


    /**
     * Verifica si ha pasando al menos un segundo desde la última vez que detectó un obstáculo
     *  para no saturar de detecciones la escena
     * @return
     */
    private boolean verifica(String clase){



        Date ahora = Calendar.getInstance().getTime();
        float tiempo = (ahora.getTime() -espera.getTime()) / 1000;
        if(tiempo > 1){
            this.espera = ahora;
            return true;
            //Verifica si el objeto de la clase existe
            /*
            for(int i =0 ; i < obstaculos.size() ; i++ ){
                if(obstaculos.get(i).getClass_name().equals(clase)){
                    return  obstaculos.get(i);
                }
            }
            */

        }


        return false;



    }





}
