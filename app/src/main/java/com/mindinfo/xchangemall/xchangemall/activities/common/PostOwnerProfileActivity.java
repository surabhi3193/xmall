package com.mindinfo.xchangemall.xchangemall.activities.common;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.HLVAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.DEFAULT_PATH;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;

public class PostOwnerProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    String response;
    JSONObject responseObj;
    RecyclerView mRecyclerView;
    RecyclerView mRecyclerView_vdo;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.LayoutManager mLayoutManager_vdo;
    RecyclerView.Adapter mAdapter;
    RecyclerView.Adapter mAdapter_vdo;

    ImageView profile_image;
    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    ArrayList<Integer> alImgVdo;
    ArrayList<Uri> alvideo;
    ImageView back_btn;
    String user_name,profile_photo="",user_address;
    double user_lat,user_lng;
    GoogleMap map;
    LatLng item_location = null;
    LinearLayout dob_lay,email_lay,mobile_lay;
    String dob_status ="1",email_status="1",mobile_status="1;",aboutme="";
    // Map
    double item_lat, item_lng;
    Marker mapMarker;
    private TextView GenderText,DateText,OccupationText,WorkPlaceText,InterestText,EducationText,phoneNumberText,
            EmailText,AboutMeText,locationTV,socialTV,owner_nameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_owner_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            response = bundle.getString("ownerDetails");
            try {

                System.out.println("************** owner details ***********");

                responseObj=new JSONObject(response);
                System.out.println(response);
                user_name = responseObj.getString("first_name");

                dob_status =responseObj.getString("dob_hide_status");
                email_status =responseObj.getString("email_hide_status");
                mobile_status =responseObj.getString("phone_hide_status");
                aboutme =responseObj.getString("desc");
                item_lat =bundle.getDouble("latitude");
                item_lng =bundle.getDouble("longitude");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        alName = new ArrayList<>(Arrays.asList("", "", "", "", ""));
        alImage = new ArrayList<>(Arrays.asList(R.drawable.image1, R.drawable.img2, R.drawable.img3, R.drawable.car_image, R.drawable.image1));
        alImgVdo = new ArrayList<>(Arrays.asList(R.drawable.youtube_dummy2, R.drawable.youtube_dummy1, R.drawable.youtube_dummy2));

        initui();
        forMapView();
        addValues(responseObj);






    }
    private void forMapView() {


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (mapMarker != null)
                    mapMarker.remove();
                LatLng post_loc;
                if (item_lat!=0 && item_lng!=0)
                {
                    post_loc = new LatLng(item_lat, item_lng);
                }
                else
                {
                    post_loc= new LatLng(22.78965,75.3652);

                }
                mapMarker = map.addMarker(new MarkerOptions().position(post_loc).title("Owner Location"));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(post_loc).tilt(45).bearing(45).zoom((float) 18.5).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                String address = getAddressFromLatlng(post_loc, getApplicationContext(), 1);
                locationTV.setText(address);

            }
        });


    }



    private void validate(String value, LinearLayout layout)
    {
        if (value.equals("2"))
        {
            layout.setVisibility(View.GONE);
        }
        else if (value.equals("1"))
        {
          layout.setVisibility(View.VISIBLE);

        }
    }
    private void initui() {


        dob_lay = (LinearLayout) findViewById(R.id.dobLay);
        mobile_lay = (LinearLayout)findViewById(R.id.contactLay);
        email_lay = (LinearLayout)findViewById(R.id.emailLay);


        back_btn = (ImageView)findViewById(R.id.back_btn);
        profile_image = (ImageView)findViewById(R.id.profile_image);
        GenderText = (TextView)findViewById(R.id.GenderText);
        owner_nameTV = (TextView)findViewById(R.id.owner_name);
        DateText = (TextView) findViewById(R.id.DateText);
        OccupationText = (TextView)findViewById(R.id.OccupationText);
        WorkPlaceText = (TextView)findViewById(R.id.WorkPlaceText);
        InterestText = (TextView)findViewById(R.id.InterestText);
        EducationText = (TextView)findViewById(R.id.EducationText);
        phoneNumberText = (TextView)findViewById(R.id.phoneNumberText);
        EmailText = (TextView)findViewById(R.id.EmailText);
        AboutMeText = (TextView)findViewById(R.id.AboutMeText);
        locationTV = (TextView)findViewById(R.id.locationName);
        socialTV = (TextView)findViewById(R.id.socialTV);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView_vdo = (RecyclerView)findViewById(R.id.recycler_view_vdo);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView_vdo.setHasFixedSize(true);


        Typeface face  = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        GenderText.setTypeface(face);
        DateText.setTypeface(face);
        OccupationText.setTypeface(face);
        WorkPlaceText.setTypeface(face);
        socialTV.setTypeface(face);
        AboutMeText.setTypeface(face);
        locationTV.setTypeface(face);
        EmailText.setTypeface(face);
        phoneNumberText.setTypeface(face);
        EducationText.setTypeface(face);
        InterestText.setTypeface(face);
        owner_nameTV.setTypeface(face);

validate(dob_status,dob_lay);
validate(email_status,email_lay);
validate(mobile_status,mobile_lay);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_custom_divider));
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView_vdo.addItemDecoration(divider);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager_vdo = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_vdo.setLayoutManager(mLayoutManager_vdo);

        mAdapter = new HLVAdapter(getApplicationContext(), alName, alImage);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter_vdo = new HLVAdapter(getApplicationContext(), alName, alImgVdo);
        mRecyclerView_vdo.setAdapter(mAdapter_vdo);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
    private void addValues(JSONObject jsonObject){
        try {
        String fname = null;

            fname = jsonObject.getString("user_name");


        String email = jsonObject.getString("user_email");
        String gender = jsonObject.getString("gender");
        String occupation = jsonObject.getString("occupation");
        String interrest = jsonObject.getString("interest");
        String dob = jsonObject.getString("dob");
        String workplace = jsonObject.getString("work_place");
        String education = jsonObject.getString("education");
        String phone = jsonObject.getString("user_phone");
        String social_link = null;
        social_link = jsonObject.getString("social_link");
       String lat= responseObj.getString("latitude");
       String lng = responseObj.getString("longitude");
            profile_photo = responseObj.getString("profile_photo");

if(profile_photo.equals(DEFAULT_PATH))
    Picasso.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);
else if (profile_photo.length()<1)
    profile_image.setBackground(getResources().getDrawable(R.drawable.profile));

else
            Picasso.with(getApplicationContext()).load(profile_photo).into(profile_image);

       user_lat= Double.parseDouble(lat);
       user_lng= Double.parseDouble(lng);
       user_address = responseObj.getString("address");
        user_name =fname;

        GenderText.setText(gender);
        WorkPlaceText.setText(workplace);
        InterestText.setText(interrest);
        EducationText.setText(education);
        phoneNumberText.setText(phone);
        EmailText.setText(email);
        DateText.setText(dob);
        OccupationText.setText(occupation);
        socialTV.setText(social_link);
        owner_nameTV.setText(user_name);
        locationTV.setText(user_address);
        AboutMeText.setText(aboutme);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (item_location == null) {
            item_location = new LatLng(43.1, -87.9);
        }
        System.out.println("On map ready ");
        System.out.println(item_location);
        System.out.println(item_lat);
        System.out.println(item_lng);

        map.addMarker(new MarkerOptions().position(item_location).title("You're Location"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(item_location).tilt(45).bearing(45).zoom((float) 18.5).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
