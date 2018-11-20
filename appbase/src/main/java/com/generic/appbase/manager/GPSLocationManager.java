package com.generic.appbase.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSLocationManager {


    @SuppressLint("MissingPermission")
    public static void getLocation(final Context context, OnLocationCallback onLocationCallback) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GPSLocationListener locationListener = new GPSLocationListener(locationManager, onLocationCallback);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                locationListener);

    }

    public interface OnLocationCallback {
        void onLocationReceived(Location location);
    }

    static class GPSLocationListener implements LocationListener {
        private LocationManager locationManager;
        private OnLocationCallback onLocationCallback;

        public GPSLocationListener(LocationManager locationManager,
                                   OnLocationCallback onLocationCallback) {
            this.locationManager = locationManager;
            this.onLocationCallback = onLocationCallback;
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(this);
            onLocationCallback.onLocationReceived(location);
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
    }

}

