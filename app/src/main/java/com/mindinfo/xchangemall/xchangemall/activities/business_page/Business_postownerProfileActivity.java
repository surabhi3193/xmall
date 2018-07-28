package com.mindinfo.xchangemall.xchangemall.activities.business_page;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.BaseActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.HLVAdapter;
import com.mindinfo.xchangemall.xchangemall.adapter.HoursAdapter;
import com.mindinfo.xchangemall.xchangemall.adapter.SliderAdapter2;
import com.mindinfo.xchangemall.xchangemall.services.CallService;
import com.mindinfo.xchangemall.xchangemall.webrtc.activities.CallActivity;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.Toaster;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.WebRtcSessionManager;
import com.mindinfo.xchangemall.xchangemall.webrtc.classes.utils.CollectionsUtils;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

import static com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

public class Business_postownerProfileActivity extends BaseActivity {
    private static final String TAG = "BUSINESS PROFILE";
    RecyclerView mRecyclerView;
    private ArrayList<Uri> imageSet = new ArrayList<Uri>();
    private GoogleMap map;
    private Marker marker;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_postowner_profile);

        this.getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RecyclerView list = findViewById(R.id.hourList);
        ViewPager mPager = findViewById(R.id.pager);
        CircleIndicator indicator = findViewById(R.id.indicator);

        TextView title_TV = findViewById(R.id.title_TV);
        TextView owner_nameTV = findViewById(R.id.owner_nameTV);
        TextView post_timeTV = findViewById(R.id.post_timeTV);
        TextView businessnameTV = findViewById(R.id.businessnameTV);
        TextView websiteTV = findViewById(R.id.websiteTV);
        TextView categoryTV = findViewById(R.id.categoryTV);
        TextView aboutTV = findViewById(R.id.aboutTV);
        TextView descTV = findViewById(R.id.descTV);
        TextView socialTV = findViewById(R.id.socialTV);
        TextView locationNameTV = findViewById(R.id.locationName);
        CircleImageView profile_image = findViewById(R.id.profile_image);
        mRecyclerView = findViewById(R.id.recycler_view_photo);
        LinearLayout video_call = findViewById(R.id.video_call);
        RecyclerView mRecyclerView_vdo = findViewById(R.id.recycler_view_vdo);

        ImageView back_arrowImage = findViewById(R.id.back_arrowImage);
        list.setFocusable(false);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Business_postownerProfileActivity.this);
        list.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(Business_postownerProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.LayoutManager mLayoutManager_vdo = new LinearLayoutManager(Business_postownerProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_vdo.setLayoutManager(mLayoutManager_vdo);

        ArrayList<String> alName = new ArrayList<>(Arrays.asList("", "", "", "", ""));
        ArrayList<Integer> alImgVdo = new ArrayList<>(Arrays.asList(R.drawable.youtube_dummy2, R.drawable.youtube_dummy1, R.drawable.youtube_dummy2));

        RecyclerView.Adapter mAdapter_vdo = new HLVAdapter(Business_postownerProfileActivity.this, alName, alImgVdo);
        mRecyclerView_vdo.setAdapter(mAdapter_vdo);


        back_arrowImage.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String response = bundle.getString("productDetails");
            try {
                JSONObject responseObj = new JSONObject(response);

                System.err.println("==== response for business services=========");
                System.err.println(response);
                String cat = responseObj.getString("category");
                final String profile = responseObj.getString("profile_photo");
                String ownername = responseObj.getString("first_name");
                String posttime = responseObj.getString("post_created_datetime");
                String title = responseObj.getString("title");
                String website_link = responseObj.getString("website_link");
                String about = responseObj.getString("about_business");
                String description = responseObj.getString("description");
                String social_media_link = responseObj.getString("social_media_link");
                String locationName = responseObj.getString("address");
                String imageArray = responseObj.getString("featured_img");
                String video_url = responseObj.getString("video_url");
                double post_lat = Double.parseDouble(responseObj.getString("latitude"));
                double post_lng = Double.parseDouble(responseObj.getString("longitude"));
                JSONArray operationArray = responseObj.getJSONArray("hours_of_operation");

//                videoView.setVideoPath(video_url);
////                Bitmap bm = setVideoThumbnail(video_url);
////                System.out.println("=======thumbnail ======= " + bm);
////                BitmapDrawable bitmapDrawable = new BitmapDrawable(bm);
////                videoView.setBackgroundDrawable(bitmapDrawable);
//                Uri video = Uri.parse(video_url);
//                videoView.setVideoURI(video);
//            videoView.seekTo(100);
//
//                videoView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (!isplaying) {
//                            videoView.start();
//                            isplaying = true;
//                        } else {
//                            videoView.pause();
//                            isplaying = false;
//                        }
//                        return false;
//                    }
//                });


                HoursAdapter adapter2 = new HoursAdapter(Business_postownerProfileActivity.this, operationArray);
                list.setAdapter(adapter2);

                if (profile.length() > 0 && profile != null) {
                    Glide.with(Business_postownerProfileActivity.this)
                            .load(profile)
                            .into(profile_image);
                }
                title_TV.setText(cat);
                owner_nameTV.setText(ownername);
                aboutTV.setText(about);
                locationNameTV.setText(locationName);
                socialTV.setText(social_media_link);
                descTV.setText(description);
                businessnameTV.setText(title);
                categoryTV.setText(cat);
                websiteTV.setText(website_link);
                post_timeTV.setText("Ad Posted At : " + posttime);
                forMapView(post_lat, post_lng);
                String[] uris = imageArray.split(",");

                if (imageSet.size() > 0)
                    imageSet.clear();

                for (String uri : uris) {
                    imageSet.add(Uri.parse(uri));

                }
                mPager.setAdapter(new SliderAdapter2(Business_postownerProfileActivity.this, imageSet));

                mPager.setBackgroundColor(getResources().getColor(R.color.black));
                indicator.setViewPager(mPager);

                profile_image.setOnClickListener(v -> openProfilePic(profile));
                HLVAdapter mAdapter = new HLVAdapter(Business_postownerProfileActivity.this, imageSet, "business");
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QBUser qbUser = new QBUser("testuser4","x6Bt0VDy5");
                Log.d(TAG, " in user ------ " + qbUser.getLogin());
                Log.d(TAG, " in user ------ " + qbUser.getPassword());
                Log.d(TAG, " in user ------ " + qbUser.getId());

                qbUser.setId(54633368);


                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {

                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        System.out.println("---------- login success to chat ");

                    }

                    @Override
                    public void onError(QBResponseException errors) {
                        System.out.println("---------- login fail to chat ");                    }
                });

//                if (isLoggedInChat()) {
//                     qbUser =  QBChatService.getInstance().getUser();
//                    Log.d(TAG, "Logged in user ------ " + qbUser.getLogin());
//                    Log.d(TAG, "Logged in user ------ " + qbUser.getPassword());
////                    startCall(true);
//
//                    QBRTCClient.getInstance(Business_postownerProfileActivity.this).addSessionCallbacksListener(new CallActivity());
//                    QBRTCTypes.QBConferenceType qbConferenceType = QB_CONFERENCE_TYPE_VIDEO;
//                    List<Integer> opponents = new ArrayList<Integer>();
//                    opponents.add(54638796); //12345 - QBUser ID
//                    //Set user information
//                    Map<String, String> userInfo = new HashMap<>();
//                    userInfo.put("key", "value");
//                    //Init session
//                    QBRTCSession session = QBRTCClient.getInstance(Business_postownerProfileActivity.this).createNewSessionWithOpponents(opponents, qbConferenceType);
//                    //Start call
//                    session.startCall(userInfo);
//                }

            }
        });

    }




    private void startCall(boolean isVideoCall) {

        System.out.println("---------- startcall---------");
        Log.d(TAG, "startCall()");
//        ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());

        ArrayList<Integer> opponentsList = new ArrayList<>();
        opponentsList.add(54638796);

        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        System.out.println("---------- opponentsList---------");


        Log.d(TAG, "opponentsList()" + opponentsList.toString());
        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

        CallActivity.start(this, false);
        Log.d(TAG, "conferenceType = " + conferenceType);
    }


    private boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
//            Toaster.shortToast(R.string.dlg_signal_error);
            tryReLoginToChat();
            return false;
        }
        return true;
    }


    private void tryReLoginToChat() {
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            Log.d(TAG, "hasqbuser in user ------ " + qbUser.getLogin());
            Log.d(TAG, "hasqbuser in user ------ " + qbUser.getPassword());

            CallService.start(this, qbUser);
        }
        else
        {
            QBUser qbUser = new QBUser("testuser4","x6Bt0VDy5");
            Log.d(TAG, "reLogged in user ------ " + qbUser.getLogin());
            Log.d(TAG, "reLogged in user ------ " + qbUser.getPassword());
            Log.d(TAG, "reLogged in user ------ " + qbUser.getId());

            qbUser.setId(54633368);
            CallService.start(this, qbUser);
        }
    }

    private Bitmap setVideoThumbnail(String video_url) throws Throwable {

        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(video_url, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(video_url);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
    private void forMapView(final double lat, final double lng) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            if (marker != null)
                marker.remove();
            LatLng post_loc;
            if (lat != 0 && lng != 0) {
                post_loc = new LatLng(lat, lng);
            } else {
                post_loc = new LatLng(0.0, 0.0);

            }
            marker = map.addMarker(new MarkerOptions().position(post_loc).title("Owner Location"));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(post_loc).tilt(45).bearing(45).zoom((float) 18.5).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        });
    }
    private void openProfilePic(String owner_image) {
        final RelativeLayout open = findViewById(R.id.fullsliderlay);
        final ImageView owner_fullIV = findViewById(R.id.owner_fullIV);

        ImageView close = findViewById(R.id.close_slider);
        open.setVisibility(View.VISIBLE);
        owner_fullIV.setVisibility(View.VISIBLE);

        if (owner_image.equals(DEFAULT_PATH))
            Glide.with(getApplicationContext()).load(R.drawable.profile).into(owner_fullIV);

        else if (owner_image.length() < 1)
            owner_fullIV.setBackground(getResources().getDrawable(R.drawable.profile));
        else
            Glide.with(getApplicationContext()).load(owner_image).into(owner_fullIV);

        close.setOnClickListener(view -> {
            open.setVisibility(View.GONE);
            owner_fullIV.setVisibility(View.GONE);
        });

    }


}
 