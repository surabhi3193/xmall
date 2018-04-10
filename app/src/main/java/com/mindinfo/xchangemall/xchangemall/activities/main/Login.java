package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.plus.Plus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.services.SinchService;
import com.sinch.android.rtc.SinchError;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class Login extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,ServiceConnection, SinchService.StartFailedListener {

    protected static final String TAG = Login.class.getSimpleName();
    public static  String caller="";
String social_login_response="";
    ProgressDialog ringProgressDialog;
    ImageButton login_btn;
    TextView createAccountLink;
    String device_id="",device_token="";
    Typeface face;
    private ProgressDialog mSpinner;
    private LinearLayout fblogin, googlelogin;
    private ProgressBar progress;
    private ProgressBar progress2;
    private LoginButton fb_login_btn;
    private SignInButton signInButton;
    private String username, pass, email;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;
    private int SIGN_IN = 30;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Context context;
    private EditText emailEt, passwordEt;


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();

            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);

        }

        @Override

        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };

    @Override
    protected void onServiceConnected() {

        getSinchServiceInterface().setStartListener(this);
    }
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         face = Typeface.createFromAsset(getAssets(),"fonts/estre.ttf");

        FacebookSdk.setApplicationId("160268414713255");
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login2);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        context = getApplicationContext();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        initui();
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    //Login is Clicked to manually to connect to the Sinch Service
    private void sinchLogin(String user_id,String type) {

        caller=user_id;
        if (user_id.isEmpty()) {
            Toast.makeText(this, "User not registered ", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(user_id);
            showSpinner();
        } else {
            if (type.equals("social"))
            {
//                new NetworkClass().getloginresponse(Login.this, social_login_response);
//                onDestroy();
            }
            else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
//            openPlaceCallActivity();
        }
    }


    //Once the connection is made to the Sinch Service, It takes you to the next activity where you enter the name of the user to whom the call is to be placed
    private void openPlaceCallActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

    private void initui() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        fblogin = (LinearLayout) findViewById(R.id.fblogin);
        googlelogin = (LinearLayout) findViewById(R.id.google_login);
        fb_login_btn = (LoginButton) findViewById(R.id.loginbtn);

        emailEt = (EditText) findViewById(R.id.login_emailid);
        passwordEt = (EditText) findViewById(R.id.login_password);
        login_btn = (ImageButton) findViewById(R.id.login_btn);
        createAccountLink = (TextView) findViewById(R.id.createAccount);

        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        emailEt.setTypeface(face);
        passwordEt.setTypeface(face);
        createAccountLink.setTypeface(face);

        createAccountLink.setOnClickListener(this);
        fblogin.setOnClickListener(this);
        googlelogin.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        device_id= Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);

            device_token= instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + device_token);

        }catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }

        Log.i(TAG, "FCM Registration Token: " + device_token);
        Log.i(TAG, "FCM Registration ID: " + device_id);



//       putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
        fb_login_btn.setReadPermissions("public_profile");
        fb_login_btn.registerCallback(callbackManager, callback);
    }


    private void getData()
    {
        username = emailEt.getText().toString();
        pass = passwordEt.getText().toString();
        if (username.length() > 0)
        {
            if (pass.length() > 0) {
                loginbtnclicked(username, pass);
            } else {
                passwordEt.setError("Field Empty");
                Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            }
        } else {
            emailEt.setError("Field Empty");
//            Toast.makeText(getApplicationContext(), "Please enter your Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        if (isNetworkAvailable(context)) {
            switch (view.getId()) {
                case R.id.login_btn:
                    if (isNetworkAvailable(context)) {
                        getData();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet connection unavailable ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.fblogin:
                    fb_login_btn.performClick();
                    break;
                case R.id.google_login:
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    break;

                case R.id.createAccount:
                    Intent createAccount = new Intent(getApplicationContext(), SIgnUp.class);
                    startActivity(createAccount);
                    finish();
                    break;

            }
        } else

        {
            Toast.makeText(getApplicationContext(), "Internet connection unavailable ", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN)
                {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            System.err.println("=== signing result g+ =======");
            System.err.println(result.isSuccess());
            String loginType = "1";
            if (result.isSuccess())
            {
                //Getting google account
                GoogleSignInAccount acct = result.getSignInAccount();
                String name = acct.getDisplayName();
                String email = acct.getEmail();
                String gmailId = acct.getId();
                String picUri="";
                if (acct.getPhotoUrl()!=null) {
                     picUri = acct.getPhotoUrl().toString();
                }
                String social_id = acct.getId();


                Log.i("Login", result.getStatus().toString());
                Log.i("DisplayName", name);
                Log.i("Email", email);

                login_with_social(email, name, picUri, social_id,"3");
                Toast.makeText(Login.this, "Welcome " + name, Toast.LENGTH_LONG).show();
                JSONObject nameobj = new JSONObject();
                nameobj.put("name", name);


            }
            else {
                //If login fails
                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
            Uri picuri = profile.getProfilePictureUri(1000, 1000);
            String social_id = profile.getId();
            Uri emailid = profile.getLinkUri();
            String fullname = profile.getFirstName() + " " + profile.getMiddleName() + " " + profile.getLastName();
            Toast.makeText(Login.this, "Welcome " + fullname, Toast.LENGTH_LONG).show();
            login_with_social(emailid.toString(), fullname, picuri.toString(), social_id,"2");

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("*************** onREsume fb logout *********");
    LoginManager.getInstance().logOut();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Login.this,EnterLogin.class));
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    @SuppressLint("StaticFieldLeak")
    private void loginbtnclicked( final String email, final String password) {
        if (email.length() > 0 && password.length() > 0)
        {
            ringProgressDialog = ProgressDialog.show(Login.this, "Please wait ...", "", true);
            ringProgressDialog.setCancelable(false);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {

                            @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(Login.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                          loginUser(getApplicationContext(),email,password,device_token,"1",deviceId);
                        }
                    });


        }
    }

    public  void loginUser(final Context context, final String user_name, final String password, final String device_token,
                                 final String device_type, final String device_id){

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_email", user_name);
        params.put("user_pwd", password);
        params.put("user_device_token", "234v3s4d3gv");
        params.put("user_device_type", device_type);
        params.put("user_device_id", device_id);
        client.post(BASE_URL_NEW + "login", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();
                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
//                        String photo_str = responseobj.getString("profile_photo");
                        JSONObject responseobj = response.getJSONObject("result");
                        String namefbuser = responseobj.getString("first_name");
                        String user_id = responseobj.getString("id");
                        String username = responseobj.getString("user_name");
                        saveData(context, "user_profile_pic", "");
                        saveData(context, "user_name", namefbuser);
                        saveData(context, "user_id", user_id);
                        saveData(context, "loginData", "available");
                        saveData(context, "login_response", responseobj.toString());

//                        sinchLogin(user_id,"");

openPlaceCallActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                Toast.makeText(context, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void login_with_social(final String email, final String name, final String
            profile_pic, final String id,final  String user_type){

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("first_name", name);
        params.put("last_name", "");
        params.put("user_email", email);
        params.put("user_pic", profile_pic);
        params.put("user_social_id", id);
        params.put("user_device_token", device_token);
        params.put("user_device_id", device_id);
        params.put("user_device_type", "1");
        params.put("user_type",user_type );

        System.out.println("*** requested params for login social *******");
        System.out.println(params);
        client.post(BASE_URL_NEW + "social_login", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                      String  user_id = response.getString("user_id");
                     String profile_pic = response.getString("profile_photo");
                        saveData(getApplicationContext(), "user_id", user_id);
                        saveData(getApplicationContext(), "user_profile_pic", profile_pic);
                        saveData(getApplicationContext(), "user_name",
                                response.getString("first_name")+response.getString("last_name"));
                        saveData(context, "loginData", "available");
                        saveData(context, "login_response", response.toString());
//                        sinchLogin(user_id,"");
openPlaceCallActivity();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String errorResponse) {
                Toast.makeText(context, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
            }
        });
    }
}
