package com.fm.johan.flashmeet;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * Created by johan on 25.01.2016.
 */
public class LocationHelper {

    public static void getLocation(Context context) {

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.v("LocationListener", "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.v("LocationListener", "onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.v("LocationListener", "onProviderDisabled");
            }

            @Override
            public void onLocationChanged(Location location) {

                Log.v("LocationListener", "onLocationChanged++++++++");
            }
        };

        LocationManager locationManager = (LocationManager)context.getSystemService(Service.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);

        Log.v("Provider", provider);

        locationManager.requestSingleUpdate(provider, locationListener, Looper.myLooper());
    }
}
