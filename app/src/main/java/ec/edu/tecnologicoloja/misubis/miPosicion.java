package ec.edu.tecnologicoloja.misubis;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class miPosicion implements LocationListener {
    public static double latitude,longitude,altitud;
    public static boolean statusGPS;
    public static boolean locationAvailable = false;

    public static Location coordenadas;




    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        coordenadas=location;
        altitud=location.getAltitude();
        locationAvailable = true;

        /*if (MapsActivity.isActive) {
            MapsActivity.getInstance().updateLocationInfo();
        }*/



    }


    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
       statusGPS=true;
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        statusGPS=false;
    }


}
