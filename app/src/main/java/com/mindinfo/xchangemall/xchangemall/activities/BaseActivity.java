package com.mindinfo.xchangemall.xchangemall.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.intefaces.Consts;
import com.mindinfo.xchangemall.xchangemall.other.QBResRequestExecutor;
import com.mindinfo.xchangemall.xchangemall.other.XchangemallApplication;
import com.mindinfo.xchangemall.xchangemall.webrtc.gcm.GooglePlayServicesHelper;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.QbUsersDbManager;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.WebRtcSessionManager;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;


public abstract class BaseActivity extends AppCompatActivity {
    public static String BASE_URL_NEW ="http://xchange.world/xchange_mall2/index.php/Webservice/";
    public static String DEFAULT_PATH ="http://xchange.world/xchange_mall2/uploads/users/";
    public  static String user_image, user_name;
    public SharedPrefsHelper sharedPrefsHelper;
    protected GooglePlayServicesHelper googlePlayServicesHelper;
    protected QBResRequestExecutor requestExecutor;
    private QBUser currentUser;
    private boolean isRunForCall;
    private QbUsersDbManager dbManager;
    private WebRtcSessionManager webRtcSessionManager;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestExecutor = XchangemallApplication.getInstance().getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        googlePlayServicesHelper = new GooglePlayServicesHelper();


        initFields();

        String currentRoomName = sharedPrefsHelper.get(Consts.PREF_CURREN_ROOM_NAME, "");

        if (sharedPrefsHelper.getQbUser() != null) {
          String  currentUserFullName = sharedPrefsHelper.getQbUser().getFullName();
            System.out.println("- current qbuser ----- > " + currentUserFullName);
        }

        if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
            MainActivity.start(BaseActivity.this, true);
        }


    }
    private void initFields() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
        }

        currentUser = sharedPrefsHelper.getQbUser();
        dbManager = QbUsersDbManager.getInstance(getApplicationContext());
        webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
    }

}
