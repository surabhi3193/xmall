package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.isMatch;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class SIgnUp extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = SIgnUp.class.getSimpleName();
    String email_id;
    ProgressDialog ringProgressDialog;
    String namefbuser = "", facebook_id;
    String photo_str = "";
    String str_terms_conditions = "";
    String device_id="", device_token="";
    Typeface face;
    // fb & gmail data
    private LinearLayout fblogin, googlelogin;
    private LoginButton loginButton;
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int SIGN_IN = 30;
// form data
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Context context;
    private EditText fullName, userEmailId, password, confirmPassword, usernameET;
    private Button signUpBtn, google_sing_btn;
    private CheckBox Check_terms_conditions;
    private TextView already_userTextView;
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

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN =  "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.&*!]).{8,50})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("160268414713255");
        FacebookSdk.sdkInitialize(this);
        face = Typeface.createFromAsset(getAssets(),"fonts/estre.ttf");

        setContentView(R.layout.activity_sign_up);

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
        initui();

    }

    private void initui() {

        // google & fb dedinations
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        fblogin = (LinearLayout) findViewById(R.id.fblogin);
        googlelogin = (LinearLayout) findViewById(R.id.google_login);
        loginButton = (LoginButton) findViewById(R.id.loginbtn);

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
                startActivityForResult(signInIntent, SIGN_IN);
            }
        });

        fblogin.setOnClickListener(this);
        googlelogin.setOnClickListener(this);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);


        // signup forn definations


        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        fullName = (EditText) findViewById(R.id.fullName);
        usernameET = (EditText) findViewById(R.id.username);
        userEmailId = (EditText) findViewById(R.id.userEmailId);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        Check_terms_conditions = (CheckBox) findViewById(R.id.terms_conditions);
        already_userTextView = (TextView) findViewById(R.id.already_user);



        fullName.setTypeface(face);
        usernameET.setTypeface(face);
        userEmailId.setTypeface(face);
        password.setTypeface(face);
        confirmPassword.setTypeface(face);
        Check_terms_conditions.setTypeface(face);
        already_userTextView.setTypeface(face);

        //Click listerner of buttons
        already_userTextView.setOnClickListener(this);
        Check_terms_conditions.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);


    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            String unm = fullName.getText().toString();
//            String nmpatt="^[a-zA-Z ]+$";

            if (unm.length() < 2) {
                fullName.setError("Field Empty");
                return;
            }
            String usernaem = usernameET.getText().toString();
//            String nmpatt="^[a-zA-Z ]+$";

            if (usernaem.length() < 1) {
                userEmailId.setError("Field Empty");
                return;
            }


            String em = userEmailId.getText().toString();
            String empatt = "^[a-zA-Z0-9_.]+@[a-zA-Z]+\\.[a-zA-Z]+$";
            boolean b4 = isMatch(em, empatt);
            if (!b4) {
                userEmailId.setError("Invalid Email ID");
                return;
            }


            String pass = password.getText().toString();
            if (isValidPassword(pass)) {
                password.setError("Password should be of min 8 charecter long with atleast 1 number , 1 lower case, 1 uppercase & 1 special charecter (@#$%.&*!)");
                return;
            }

            String con_pass = confirmPassword.getText().toString();
//            boolean b3=isMatch(con_pass, pass);

            if (con_pass.length() == 0) {
                confirmPassword.setError("Field Empty");
            }
            if (!pass.equals(con_pass)) {
                Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
                return;
            }

            if (str_terms_conditions.equals("")) {
                Toast.makeText(getApplicationContext(), "Please accept terms & conditions", Toast.LENGTH_SHORT).show();
                return;
            }

            if (str_terms_conditions.equals("check")) {
                ClickedOnRegerter();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please turn on internet", Toast.LENGTH_SHORT).show();

        }

    }

    @SuppressLint("StaticFieldLeak")
    private void ClickedOnRegerter() {

        final String name = fullName.getText().toString();
        final String email = userEmailId.getText().toString();
        final String pass = password.getText().toString();
        final String username = usernameET.getText().toString();

        ringProgressDialog = ProgressDialog.show(SIgnUp.this, "Please wait ...", "", true);
        ringProgressDialog.setCancelable(false);
        registerUser(username, name, email, pass, device_token, "1", device_id);

    }

    public void registerUser(final String user_name, final String name, final String user_email, final String password, final String device_token,
                             final String device_type, final String device_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();


        params.put("user_email", user_email);
        params.put("user_pwd", password);
        params.put("user_device_token", "35436tytyfi");
        params.put("user_device_type", "1");
        params.put("user_device_id", device_id);
        params.put("name", name);
        params.put("user_type", "1");
        params.put("user_name", user_name);
        client.post(BASE_URL_NEW + "register", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* signup response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();

                    } else {

                        JSONObject responseobj = response.getJSONObject("result");
//                        String photo_str = responseobj.getString("profile_photo");
                        String namefbuser = responseobj.getString("first_name");
                        String user_id = responseobj.getString("id");
                        final String username = responseobj.getString("user_name");
                        saveData(context, "user_profile_pic", "");
                        saveData(context, "user_name", namefbuser);
                        saveData(context, "user_id", user_id);
                        saveData(context, "loginData", "available");
                        saveData(context, "login_response", responseobj.toString());

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

        });
    }

    @Override
    public void onClick(View view) {

        if (isNetworkAvailable(context)) {
            switch (view.getId()) {
                case R.id.login_btn:
                    if (isNetworkAvailable(context)) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet connection unavailable ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.fblogin:
                    LoginManager.getInstance().logOut();
                    loginButton.performClick();
                    break;

                case R.id.google_login:
                    signIn();
                    break;

                case R.id.signUpBtn:
                    checkConnection();
                    break;
                case R.id.terms_conditions:
                    if (Check_terms_conditions.isChecked()) {
                        str_terms_conditions = "check";
                    } else {
                        str_terms_conditions = "";
                    }
                    break;
                case R.id.already_user:

                    startActivity(new Intent(getApplicationContext(), Login.class));
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
            if (requestCode == SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                handleSignInResult(result);
            }
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signIn() {
        try {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            String loginType = "1";
            if (result.isSuccess()) {
                //Getting google account
                GoogleSignInAccount acct = result.getSignInAccount();
                String name = acct.getDisplayName();
                String email = acct.getEmail();
                String gmailId = acct.getId();
                Uri picUri = acct.getPhotoUrl();
                String social_id = acct.getId();


                Log.i("Login", result.getStatus().toString());
                Log.i("DisplayName", name);
                Log.i("Email", email);

                login_with_social(email, name, picUri.toString(), social_id,"3");
                Toast.makeText(SIgnUp.this, "Welcome " + name, Toast.LENGTH_LONG).show();
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

            Toast.makeText(SIgnUp.this, "Welcome " + fullname, Toast.LENGTH_LONG).show();

            login_with_social(emailid.toString(), fullname, picuri.toString(), social_id,"2");

        }
    }


    public  void login_with_social(final String email, final String name, final String
            profile_pic, final String id,final  String user_type){

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("first_name", name.split(" ")[0]);
        params.put("last_name",  name.split(" ")[1]);
        params.put("user_email", email);
        params.put("user_pic", profile_pic);
        params.put("user_social_id", id);
        params.put("user_device_token", device_token);
        params.put("user_device_id", device_id);
        params.put("user_device_type", "1");
        params.put("user_type",user_type );
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
                        saveData(getApplicationContext(), "user_name", response.getString("first_name"));
                        saveData(context, "loginData", "available");
                        saveData(context, "login_response", response.toString());
//                        sinchLogin(user_id,"");
                        Intent i = new Intent(context, MainActivity.class);
                        // Closing all the Activities
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                        finish();

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
    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
//        Profile profile = Profile.getCurrentProfile();
//        displayMessage(profile);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SIgnUp.this,EnterLogin.class));
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void checkConnection() {
        boolean isConnected = isNetworkAvailable(getApplicationContext());
        showSnack(isConnected);
    }
}
