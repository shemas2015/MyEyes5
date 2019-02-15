package com.myeyes.myeyes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

abstract class Imagen {

    protected MainActivity mainActivity;
    public Imagen(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    private int numDisparity =80;
    private int blockSize =23;

    public Mat getCircles(Mat mat1, Mat gray){


        /*

        // accumulator value
        double dp = 1.2d;
        // minimum distance between the center coordinates of detected circles in pixels
        double minDist = 20;

        // min and max radii (set these values as you desire)
        int minRadius = 0, maxRadius = 0;
        double param1 = 70, param2 = 72;




        Bitmap bitmap = null;
        Mat tmp = new Mat(mat1.rows(), mat1.cols(), CvType.CV_8U, new Scalar(4));
        try {
            //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
            bitmap = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bitmap);
        }
        catch (CvException ex){ System.out.println("Errors: "+ ex.getMessage());}


        Mat circles = new Mat(bitmap.getWidth(),
                bitmap.getHeight(), CvType.CV_8UC1);

        // find the circle in the image
        Imgproc.HoughCircles(gray, circles,
                Imgproc.CV_HOUGH_GRADIENT, dp, minDist, param1,
                param2, minRadius, maxRadius);

        // get the number of circles detected
        int numberOfCircles = (circles.rows() == 0) ? 0 : circles.cols();

        // draw the circles found on the image
        for (int i=0; i<numberOfCircles; i++) {
            System.out.println("Errors: we");

            // get the circle details, circleCoordinates[0, 1, 2] = (x,y,r)
            //(x,y) are the coordinates of the circle's center

            double[] circleCoordinates = circles.get(0, i);


            int x = (int) circleCoordinates[0], y = (int) circleCoordinates[1];

            Point center = new Point(x, y);

            int radius = (int) circleCoordinates[2];

            // circle's outline
            Core.circle(mat1, center, radius, new Scalar(0,
                    255, 0), 4);

            // circle's center outline
            Core.rectangle(mat1, new Point(x - 5, y - 5),
                    new Point(x + 5, y + 5),
                    new Scalar(0, 128, 255), -1);
        }
        */
        return mat1;

    }

    protected Mat   disparityMap(Mat imgLeft,Mat imgRight){
        // Converts the images to a proper type for stereoMatching
        Mat left = new Mat();
        Mat right = new Mat();

        Imgproc.cvtColor(imgLeft, left, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(imgRight, right, Imgproc.COLOR_BGR2GRAY);

        // Create a new image using the size and type of the left image
        Mat disparity = new Mat(left.size(), left.type());

        int numDisparity = (int)(left.size().width/8);
        numDisparity = 32;




        StereoSGBM stereoAlgo = StereoSGBM.create(
                0,    // min DIsparities
                this.numDisparity, // numDisparities
                this.blockSize,   // SADWindowSize
                2*this.blockSize*this.blockSize,   // 8*number_of_image_channels*SADWindowSize*SADWindowSize   // p1
                5*this.blockSize*this.blockSize,  // 8*number_of_image_channels*SADWindowSize*SADWindowSize  // p2

                -1,   // disp12MaxDiff
                63,   // prefilterCap
                10,   // uniqueness ratio
                0, // sreckleWindowSize
                32, // spreckle Range
                0); // full DP
        // create the DisparityMap - SLOW: O(Width*height*numDisparity)

        /*

        //Funciona mejor
        StereoSGBM stereoAlgo = StereoSGBM.create(
                0,    // min DIsparities
                80, // numDisparities
                23,   // SADWindowSize
                2*23*23,   // 8*number_of_image_channels*SADWindowSize*SADWindowSize   // p1
                5*23*23,  // 8*number_of_image_channels*SADWindowSize*SADWindowSize  // p2

                -1,   // disp12MaxDiff
                63,   // prefilterCap
                0,   // uniqueness ratio
                0, // sreckleWindowSize
                0, // spreckle Range
                0); // full DP
        */

        stereoAlgo.compute(left, right, disparity);
        Core.normalize(disparity, disparity, 0, 256, Core.NORM_MINMAX);

        return disparity;
    }



    public void setPermisos(){
        if (mainActivity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mainActivity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
            this.mainActivity.finish();
            this.mainActivity.startActivity(this.mainActivity.getIntent());
        }
    }

    public void SaveImage(Mat mat, String nombre) {
        this.setPermisos();


        /**


        Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
         */

        Bitmap bmp = null;
        Mat tmp = new Mat (mat.cols(), mat.rows(), CvType.CV_8UC1, new Scalar(4));
        try {
            //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
            //Imgproc.cvtColor(mat, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
            bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, bmp);
        }
        catch (CvException e){
            System.out.println("Errorw: "+e.toString());
        }


        String filename = nombre+".png";
        File dest = new File("/sdcard/", filename);


        try {
            FileOutputStream out = new FileOutputStream(dest);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected Mat imgToMat(String url){
        if (mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED) {
            mainActivity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Mat left =null;
        Bitmap bmp = BitmapFactory.decodeFile(url);
        left = new Mat (bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC1 , new Scalar(4));
        Utils.bitmapToMat(bmp, left);
        return left;
    }

    protected Mat imgGray(Mat img){
        Mat imgGray = new Mat();
        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
        return imgGray;
    }


    public int getNumDisparity() {
        return numDisparity;
    }

    public void setNumDisparity(int numDisparity) {
        this.numDisparity = numDisparity;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}
