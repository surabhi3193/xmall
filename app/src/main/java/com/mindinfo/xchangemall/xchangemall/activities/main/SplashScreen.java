package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.services.SinchService;
import com.sinch.android.rtc.SinchError;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class SplashScreen extends BaseActivity implements ServiceConnection, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, SinchService.StartFailedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_PERMISSION = 2;
    public static String caller = "";
    public static String BASE_URL = "http://xchange.world/xchange_mall2/api2/classic.php/";
   public static Typeface face ;
    boolean GpsStatus ;
    LocationManager mLocationManager;
    RelativeLayout loginLay;
    String[] mPermission = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

//    public static String printKeyHash(Activity context) {
//        PackageInfo packageInfo;
//        String key = null;
//        try {
//            //getting application package name, as defined in manifest
//            // String packageName = context.getApplicationContext().getPackageName();
//
//            //Retriving package info
//            packageInfo = context.getPackageManager().getPackageInfo("com.mindinfo.xchangemall.xchangemall",
//                    PackageManager.GET_SIGNATURES);
//
//            Log.e("Package Name=", context.getApplicationContext().getPackageName());
//
//            for (Signature signature : packageInfo.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                key = new String(Base64.encode(md.digest(), 0));
//                Log.e("Key Hash=", key);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("Name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("No such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("Exception", e.toString());
//        }
//
//        return key;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
//        startService(new Intent(getApplicationContext(), MyService.class));
        setfontfamily(getApplicationContext());
        getCategories();
//        getCategoriesFull();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


//     printKeyHash(SplashScreen.this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        loginLay = (RelativeLayout) findViewById(R.id.loginLay);


    }
    private void processRequest() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String login = "log", user_id = "";
                login = getData(getApplicationContext(), "loginData", "");


                if (login.equals("available")) {
                    user_id = getData(getApplicationContext(), "user_id", "");
//                          sinchLogin(user_id);

                    openPlaceCallActivity();

                } else {
                    Intent intent = new Intent(SplashScreen.this, EnterLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);


    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }


    private void openPlaceCallActivity() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    private void getCategories() {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams category = new RequestParams();
        client.post(BASE_URL + "categories", null, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    System.out.println("*********** category data *********");
                    System.out.println(response.getJSONArray("cats"));

                    saveData(getApplicationContext(), "categoriesdata", response.getJSONArray("cats").toString());
                  saveData(getApplicationContext(), "categoriesdata_full",  response.getJSONArray("cats").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println(errorResponse);
            }

        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        Log.e("Req Code length ", "" + grantResults.length);


        if (requestCode == REQUEST_CODE_PERMISSION)
        {
            if (grantResults.length == 7 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == MockPackageManager.PERMISSION_GRANTED)

            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    processRequest();
                }
                else {
                    if (mGoogleApiClient.isConnected())
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    else
                    {
                        if (mGoogleApiClient!=null)
                            mGoogleApiClient.connect();

                    }
                processRequest();}
            }

            else {
              processRequest();
            }
        }

        else if (requestCode == 32)

        {
            if (mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            else
            {
                if (mGoogleApiClient!=null)
                    mGoogleApiClient.connect();

            }

        }

    }


    @Override
    public void onConnected(Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        GpsStatus = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        checkGpsStatus();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

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
    protected void onServiceConnected() {

        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();

    }


    private void otherPermission()
    {
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[5])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[6])
                            != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            }
            else
            {
                processRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();}
    }


    public void checkGpsStatus(){


        GpsStatus = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        System.out.println("===== gps status ==========");
        System.out.println(GpsStatus);
        if(!GpsStatus) {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
        else
        {
            otherPermission();
        }

    }
    private void setfontfamily(Context context) {
        face = Typeface.createFromAsset(context.getAssets(),
                "fonts/estre.ttf");
    }


}
