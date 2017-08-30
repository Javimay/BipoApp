package com.bipo.javier.bipo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.bipo.javier.bipo.R;

import java.io.IOException;
import java.util.List;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class GpsConnection implements LocationListener{

    private Context context;
    private boolean isGpsEnabled;
    private LocationManager locationManager;
    private double latitude = 0, longitude = 0;
    private Activity activity;

    public GpsConnection(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void getGPSPermission() {

        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
        }
    }

    private boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PERMISSION_GRANTED);
    }

    public void turnOnGps() {

        int off = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                off = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            System.out.println("SettingNotFoundException: " + e.getMessage());
        }
        if (off == 0 || off == 2) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("No hay acceso al  GPS.");
            alert.setMessage("bipo no tiene acceso al GPS. " +
                    "\nActiva el GPS para que bipo pueda acceder a tu ubicación o ingresa una direccion manualmente")
                    .setPositiveButton("Activar el acceso a la ubicación", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(onGPS);
                        }
                    })
                    .setNegativeButton("Manualmente", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            alert.show();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        turnOnGps();
    }

    public boolean isGpsEnabled(){

        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGpsEnabled;
    }
}
