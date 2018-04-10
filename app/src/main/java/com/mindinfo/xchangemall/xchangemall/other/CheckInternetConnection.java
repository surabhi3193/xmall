package com.mindinfo.xchangemall.xchangemall.other;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

/**
 * Created by Mind Info- Android on 28-Nov-17.
 */

public class CheckInternetConnection extends Activity implements LocationListener
{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    static boolean grantLOc;
    static String provider;
    private static LocationManager locationManager;

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager

                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static  boolean checkLocationPermission(final Activity act) {
        locationManager = (LocationManager)act.getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ContextCompat.checkSelfPermission(act,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)

        {
            grantLOc =false;
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(act)
                        .setTitle("Location")
                        .setMessage("Please turn on your location ")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(act,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(act,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return grantLOc;
        }

        else {
            return grantLOc;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {

                        grantLOc=true;
                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                    }


                }
                else
                {
                    grantLOc=false;
                }
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates((LocationListener) this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
     double   currentLatitude = location.getLatitude();
    double    currentLongitude = location.getLongitude();

        System.out.println("************ user current location *******");
        System.out.println(currentLatitude);
        System.out.println(currentLongitude);
        saveData(getApplicationContext(), "userLat", String.valueOf(currentLatitude));
        saveData(getApplicationContext(), "userLong", String.valueOf(currentLongitude));
        if (currentLatitude != 0.0 && currentLongitude != 0.0) {
            String address = getAddressFromLatlng(new LatLng(currentLatitude, currentLongitude), getApplicationContext(), 0);
            saveData(getApplicationContext(), "currentLocation", address);


        }
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
