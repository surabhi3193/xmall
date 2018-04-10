package com.mindinfo.xchangemall.xchangemall.SinchActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import static com.mindinfo.xchangemall.xchangemall.services.SinchService.APP_KEY;
import static com.mindinfo.xchangemall.xchangemall.services.SinchService.APP_SECRET;

public class CallActivity extends AppCompatActivity {


    private Call call;
    private TextView callState;
    private SinchClient sinchClient;
    private Button button;
    private String callerId;
    private String recipientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        Intent intent = getIntent();
        callerId = intent.getStringExtra("callerId");
        recipientId = intent.getStringExtra("recipientId");


        button = (Button) findViewById(R.id.button);
        callState = (TextView) findViewById(R.id.callState);

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(callerId)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost("sandbox.sinch.com")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        System.out.println("Sinch Caller Id" + callerId);
        System.out.println("Sinch Receipnt Id" + recipientId);

/*        call = sinchClient.getCallClient().callUser(recipientId);
        call.addCallListener(new SinchCallListener());
        button.setText("Hang Up");  */

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (call == null) {
                    call = sinchClient.getCallClient().callUser(recipientId);
                    call.addCallListener(new SinchCallListener());
                    button.setText("Hang Up");
                } else {
                    call.hangup();
                }
            }
        });


    }

    public class SinchCallListener implements CallListener {


        @Override
        public void onCallEnded(Call endedCall) {


            call = null;
            button.setText("Call");
            callState.setText("");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        }

        @Override
        public void onCallEstablished(Call establishedCall) {


            callState.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        }

        @Override
        public void onCallProgressing(Call progressingCall) {


            callState.setText("ringing");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {


        }
    }


    private class SinchCallClientListener implements CallClientListener {


        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {

            call = incomingCall;
            call.answer();
            call.addCallListener(new SinchCallListener());
            button.setText("Hang Up");
        }
    }
}
