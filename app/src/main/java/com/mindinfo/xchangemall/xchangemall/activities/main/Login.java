package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.plus.Plus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.DetailsFragment;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.BaseActivity;
import com.mindinfo.xchangemall.xchangemall.intefaces.Consts;
import com.mindinfo.xchangemall.xchangemall.services.CallService;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.SharedPrefsHelper;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.Toaster;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class Login extends BaseActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = Login.class.getSimpleName();
    ProgressDialog ringProgressDialog;
    ImageButton login_btn;
    TextView createAccountLink;
    String device_id = "", device_token = "";
    Typeface face;
    private LoginButton fb_login_btn;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Context context;
    private EditText emailEt, passwordEt;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
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

    public static void start(Context context) {
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        face =  ResourcesCompat.getFont(Login.this, R.font.estre);

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
        super.onPause();
    }


    private void openPlaceCallActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        LoginManager.getInstance().logOut();

        if (mGoogleApiClient.isConnected())
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        System.out.println("********** logout from g+ ");
                    }
                });
        finish();

    }

    @SuppressLint("HardwareIds")
    private void initui() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        SignInButton signInButton = findViewById(R.id.sign_in_button);

        LinearLayout fblogin = findViewById(R.id.fblogin);
        LinearLayout googlelogin = findViewById(R.id.google_login);
        fb_login_btn = findViewById(R.id.loginbtn);

        emailEt = findViewById(R.id.login_emailid);
        passwordEt = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        createAccountLink = findViewById(R.id.createAccount);
        TextView forgot_btn = findViewById(R.id.forgot_btn);

        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        signInButton.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        emailEt.setTypeface(face);
        passwordEt.setTypeface(face);
        createAccountLink.setTypeface(face);

        createAccountLink.setOnClickListener(this);
        fblogin.setOnClickListener(this);
        googlelogin.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        device_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        forgot_btn.setOnClickListener(v -> forgotDialog());

        try {
            InstanceID instanceID = InstanceID.getInstance(this);

            device_token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + device_token);

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }

        Log.i(TAG, "FCM Registration Token: " + device_token);
        Log.i(TAG, "FCM Registration ID: " + device_id);
        fb_login_btn.setReadPermissions("public_profile");
        fb_login_btn.registerCallback(callbackManager, callback);
    }

    private void forgotDialog() {

        final Dialog dialog = new Dialog(Login.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.alertdialog);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        EditText login_emailid = dialog.findViewById(R.id.login_emailid);
        TextView ok_btn = dialog.findViewById(R.id.gallery_btn);
        TextView cancel_btn = dialog.findViewById(R.id.camera_btn);

        ok_btn.setOnClickListener(view -> {

            String email = login_emailid.getText().toString();
            if (email.length() == 0) {
                Toast.makeText(Login.this, "Enter Username or Email", Toast.LENGTH_SHORT).show();
                return;
            }

            forgotPassword(email);
            dialog.dismiss();
        });


        cancel_btn.setOnClickListener(view -> dialog.dismiss());
    }

    private void forgotPassword(String email) {
        ringProgressDialog = ProgressDialog.show(Login.this, "Please wait ...", "", true);
        ringProgressDialog.setCancelable(false);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_name", email);
        client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "forget_password", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    if (response.getString("status").equals("1")) {
                        Toast.makeText(Login.this, response.getString("message"), Toast.LENGTH_LONG).show();

                    } else
                        Toast.makeText(Login.this, response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });

    }


    private void getData() {
        String username = emailEt.getText().toString();
        String pass = passwordEt.getText().toString();
        if (username.length() > 0) {
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
                    LoginManager.getInstance().logOut();
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
            if (requestCode == RC_SIGN_IN) {

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                int statusCode = result.getStatus().getStatusCode();

//                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                System.err.println("=== signing resultCode g+ =======");
                System.err.println(resultCode);
                handleSignInResult(result, statusCode);
            }
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSignInResult(GoogleSignInResult result, int resultCode) {
        try {
            System.err.println("=== signing result g+ =======");
            System.err.println(result.isSuccess());
            System.err.println("=== signing statuscode g+ =======");
            System.err.println(resultCode);

            if (result.isSuccess()) {
                //Getting google account
                GoogleSignInAccount acct = result.getSignInAccount();
                assert acct != null;
                String name = acct.getDisplayName();
                String email = acct.getEmail();

                String picUri = "";
                if (acct.getPhotoUrl() != null) {
                    picUri = acct.getPhotoUrl().toString();
                }
                String social_id = acct.getId();


                Log.i("Login", result.getStatus().toString());
                Log.i("DisplayName", name);
                Log.i("Email", email);

                login_with_social(email, name, picUri, social_id, "3");
                Toast.makeText(Login.this, "Welcome " + name, Toast.LENGTH_LONG).show();
                JSONObject nameobj = new JSONObject();
                nameobj.put("name", name);

            } else {
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
            login_with_social(emailid.toString(), fullname, picuri.toString(), social_id, "2");
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
        System.out.println("*************** onResume fb logout *********");
        LoginManager.getInstance().logOut();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            System.out.println("********** logout from g+ ");
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Login.this, EnterLogin.class));
        LoginManager.getInstance().logOut();
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @SuppressLint("StaticFieldLeak")
    private void loginbtnclicked(final String email, final String password) {
        if (email.length() > 0 && password.length() > 0) {
            ringProgressDialog = ProgressDialog.show(Login.this, "Please wait ...", "", true);
            ringProgressDialog.setCancelable(false);

            runOnUiThread(() -> {

                @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(Login.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                loginUser(getApplicationContext(), email, password, device_token, "1", deviceId);
            });


        }
    }

    public void loginUser(final Context context, final String user_name, final String password, final String device_token,
                          final String device_type, final String device_id) {

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
                        JSONObject responseobj = response.getJSONObject("result");
                        String namefbuser = responseobj.getString("first_name");
                        String user_id = responseobj.getString("id");

                        saveData(context, "user_profile_pic", "");
                        saveData(context, "user_name", namefbuser);
                        saveData(context, "user_id", user_id);
                        saveData(context, "loginData", "available");
                        saveData(context, "login_response", responseobj.toString());

                        QBUser qbuser =createQBUserWithCurrentData(user_name,"Xchange",device_id);
                        signupqbUser(qbuser);
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

    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName, String device_id) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
            StringifyArrayList<String> userTags = new StringifyArrayList<>();
            userTags.add(chatRoomName);

            qbUser = new QBUser();
            qbUser.setFullName(userName);
            qbUser.setLogin(userName);
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
            qbUser.setTags(userTags);
        }

        return qbUser;
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);

//        userForSave = qbUser;
        Intent tempIntent = new Intent(this, CallService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallService.start(this, qbUser, pendingIntent);
    }

    private void signupqbUser(final QBUser newUser) {
            requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser result, Bundle params) {
                            loginToChat(result);
                            SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
                            sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, result.getTags().get(0));
                            sharedPrefsHelper.saveQbUser(result);
                            openPlaceCallActivity();
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                                signInCreatedUser(newUser, true);
                                openPlaceCallActivity();

                            } else {
                                e.printStackTrace();
//                                hideProgressDialog();
                                Toaster.longToast(R.string.sign_up_error);
                            }
                        }
                    }
            );
    }

    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                if (deleteCurrentUser) {
//                    removeAllUserData(result);
                } else {
                    System.out.println("--------- success signup quickblox-----");
//                    startOpponentsActivity();
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
//                hideProgressDialog();
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }

    public void login_with_social(final String email, final String name, final String
            profile_pic, final String id, final String user_type) {

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
        params.put("user_type", user_type);

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
                        String user_id = response.getString("user_id");
                        String profile_pic = response.getString("profile_photo");
                        saveData(getApplicationContext(), "user_id", user_id);
                        saveData(getApplicationContext(), "user_profile_pic", profile_pic);
                        saveData(getApplicationContext(), "user_name",
                                response.getString("first_name") + response.getString("last_name"));
                        saveData(context, "loginData", "available");
                        saveData(context, "login_response", response.toString());
                        openPlaceCallActivity();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "Server Error,Try Again ", Toast.LENGTH_LONG).show();
            }

        });
    }

}
