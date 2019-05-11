package com.myeyes.myeyes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Localizacion {
    private double latitud;
    private double longitud;
    private MainActivity mainActivity;
    private String provider;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public Localizacion(MainActivity mainActivity) {
        if (mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            mainActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            //Inicia la actividad nuevamente
            mainActivity.finish();
            mainActivity.startActivity(mainActivity.getIntent());
        }




        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
            System.out.println("Lat gps activo");
            provider = LocationManager.GPS_PROVIDER;
        }else{
            System.out.println("Lat gps  no está activo");
            Audio audio= new Audio(mainActivity);
            provider = LocationManager.NETWORK_PROVIDER;
        }

        if(locationManager.getLastKnownLocation(provider) != null){
            latitud = locationManager.getLastKnownLocation(provider).getLatitude();
            longitud = locationManager.getLastKnownLocation(provider).getLatitude();
        }

        LocationListener locationListener = new LocationListener()  {

            @Override
            public void onLocationChanged(Location location) {

                longitud =  location.getLongitude() ;
                latitud =  location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //solicita permisos
        if (mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            mainActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            //Inicia la actividad nuevamente
            mainActivity.finish();
            mainActivity.startActivity(mainActivity.getIntent());
        }



        locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
    }



    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }


    @Override
    public String toString() {
        return "Lat: "+latitud+" ; Lon "+longitud;
    }
}
