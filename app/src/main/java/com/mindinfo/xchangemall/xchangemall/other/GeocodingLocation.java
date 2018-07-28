package com.mindinfo.xchangemall.xchangemall.other;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getDefault;

public class GeocodingLocation {

    private   String TAG = "GeocodingLocation";


    public static String getAddressFromLatlng(LatLng latLng, Context context, int select) {
        Geocoder geocoder;
        List<Address> addresses;
        String city = "";
        String country = "";
        String colony = "";
        String fulladdress = "";
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            System.out.println("********** user location on geocoading location **********");
            System.out.println(latLng.latitude);
            System.out.println(latLng.longitude);

            double lat = latLng.latitude;
            double lng = latLng.longitude;

            if (lat != 0 && lng != 0) {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                if (addresses.size() > 0) {
                    city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                     country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                   String  knownName = addresses.get(0).getFeatureName();
                    fulladdress = knownName + ", " + postalCode + ", " + city + ", " + state + ", " + country;
                    System.out.println(addresses.get(0).getAddressLine(0));
                    System.out.println(addresses.get(0).getAddressLine(0).split(",")[1]);
                    colony = addresses.get(0).getAddressLine(0).split(",")[1];
                } else
                    fulladdress = "N/A";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (select == 0) {
            return city;
        } else if (select==2)
        {
            return country;
        }
        else if (select==3)
        {
            return colony;
        }
            else {
            return fulladdress;
        }
    }
}