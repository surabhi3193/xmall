package com.mindinfo.xchangemall.xchangemall.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.SinchActivity.IncomingCallScreenActivity;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;

public class SinchService extends Service {


    public static final String CALL_ID = "CALL_ID";
    static final String TAG = SinchService.class.getSimpleName();
    public static String APP_KEY = "ee0bc80f-cec7-48ab-b8e0-0c203976ee86";
    public static String APP_SECRET = "0/6uOx1yA0yb3wNc56W3ug==";
    private static String ENVIRONMENT = "sandbox.sinch.com";
    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private SinchClient mSinchClient;
    private String mUserId;
    private String username="Unknown";

    private StartFailedListener mListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void start(String userName) {
        if (mSinchClient == null) {
            mUserId = userName;
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection();

            mSinchClient.addSinchClientListener(new MySinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.start();
        }
    }
    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }

    public void getUserProfile(final String user_id,final Call call) {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);

        client.post(BASE_URL_NEW+"get_profile?", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject responseobj) {
                System.out.println("*********** json of get profile ********");
                System.out.println(responseobj);
                try {

                    if (responseobj.getString("status").equals("0")) {
//                        Toast.makeText(context, responseobj.getString("message"), Toast.LENGTH_LONG).show();

                    } else {

                        username = responseobj.getJSONObject("result").getString("first_name");
                        Intent intent = new Intent(SinchService.this, IncomingCallScreenActivity.class);
                        intent.putExtra(CALL_ID, call.getCallId());
                        intent.putExtra("CALLER_NAME", username);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        SinchService.this.startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });
    }
    public interface StartFailedListener {

        void onStartFailed(SinchError error);

        void onStarted();
    }

    public class SinchServiceInterface extends Binder {

        public Call callUserVideo(String userId) {
            return mSinchClient.getCallClient().callUserVideo(userId);
        }

        public Call callUserAudio(String userId) {

            return mSinchClient.getCallClient().callUser(userId);
        }

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }
        public void startClient(String userName) {
            start(userName);
        }


        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getVideoController();
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getAudioController();
        }
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Log.d(TAG, "SinchClient stopped");
        }
        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {

            if (call.getDetails().isVideoOffered()) {

                Log.d(TAG, "Incoming call");
                getUserProfile(call.getRemoteUserId(),call);

            }

        }
    }



}