package com.mindinfo.xchangemall.xchangemall.activities.job_Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.SinchActivity.CallScreenActivity;
import com.mindinfo.xchangemall.xchangemall.activities.common.PostOwnerProfileActivity;
import com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity;
import com.mindinfo.xchangemall.xchangemall.activities.property.ApplyForPropertySale;
import com.mindinfo.xchangemall.xchangemall.activities.property.ApplyForRental;
import com.mindinfo.xchangemall.xchangemall.services.SinchService;
import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenWarning;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;

public class JobsCatDetailsActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback, BaseSliderView.OnSliderClickListener {

    Typeface face;
    String response, fragment_name;

    ArrayList<String> imageSet = new ArrayList<String>();
    GoogleMap map;
    Marker mapMarker;
    LatLng item_location = null;
    String post_owner_name, post_time, job_type, job_salary, post_id, job_desc,job_duties = "Not disclosed",experience="Not disclosed",owner_id, job_name, owner_image;
    double post_lat, post_lng;
    private TextView detail_headTV, owner_nameTV, post_timeTV, view_profile_btn, job_nameTV, job_typeTV, salaryTv, address_headTV,
            job_descheadTv, job_descTv, user_reviewTv,job_dutiestv,exptv,sizeTV,dogTv,catTv;
    private ImageView call_btn, vdo_btn, chat_btn, post_owner_image, share_btn, fav_btn, back_btn,report_btn;
    private Button apply_job_btn;

    private JSONObject responseObj, OwnerDetailsObj = null;
    private SliderLayout imageSlider;
    private TextView locationTV;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_cat_details);

        face = Typeface.createFromAsset(getAssets(),
                "fonts/estre.ttf");

         user_id = getData(getApplicationContext(),"user_id","");

        init();
        chagnefont(face);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            response = bundle.getString("productDetails");
            fragment_name = bundle.getString("fragment_name");
            System.out.println(fragment_name);
            System.out.println("*********** job details ****** ");
            System.out.println(response);
            try {
                responseObj = new JSONObject(response);

                if (fragment_name.equals("job"))

                {
                    job_descheadTv.setText(R.string.job_description);
                    String apply_status = responseObj.getString("apply_status");
                     post_id = responseObj.getString("id");
                    System.out.println(post_id + "////" +user_id+ "****** is user's post ******");
                    if (post_id.equals(user_id))
                        apply_job_btn.setVisibility(View.GONE);

                    if (apply_status.equals("1")) {
                        apply_job_btn.setBackgroundColor(getResources().getColor(R.color.green));
                        apply_job_btn.setText(R.string.already_apply);

                    } else {
                        apply_job_btn.setText(R.string.apply_for_job);
                        apply_job_btn.setOnClickListener(this);

                    }
                }

                else if (fragment_name.equals("rental"))
                {
                    job_descheadTv.setText(R.string.property_desc);

                    String apply_status = responseObj.getString("rental_request");
                    post_id = responseObj.getString("id");

                    System.out.println(post_id + "////" +user_id+ "****** is user's post ******");

                    if (post_id.equals(user_id))
                        apply_job_btn.setVisibility(View.GONE);

                    if (apply_status.equals("1")) {
                        apply_job_btn.setBackgroundColor(getResources().getColor(R.color.green));
                        apply_job_btn.setText(R.string.already_apply);

                    } else {
                        apply_job_btn.setText(R.string.send_app);
                        apply_job_btn.setOnClickListener(this);
                    }
                }

                else if (fragment_name.equals("property_sale"))
                {
                    job_descheadTv.setText(R.string.property_desc);

                    String apply_status = responseObj.getString("property_request");
                    post_id = responseObj.getString("id");

                    System.out.println(post_id + "////" +user_id+ "****** is user's post ******");

                    if (post_id.equals(user_id))
                        apply_job_btn.setVisibility(View.GONE);

                    if (apply_status.equals("1")) {
                        apply_job_btn.setBackgroundColor(getResources().getColor(R.color.green));
                        apply_job_btn.setText(R.string.already_apply);

                    } else {
                        System.out.println("***** sale send *******");

                        apply_job_btn.setText(R.string.send_app);
                        apply_job_btn.setOnClickListener(this);
                    }
                }
                setData(responseObj);

                HashMap<String, String> url_maps = new HashMap<String, String>();

                for (int i = 0; i < imageSet.size(); i++) {
                    url_maps.put("image" + i, imageSet.get(i));

                }
                for (String name : url_maps.keySet()) {
                    TextSliderView textSliderView = new TextSliderView(this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.CenterInside)
                            .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", name);

                    imageSlider.stopAutoCycle();
                    imageSlider.clearAnimation();
                    imageSlider.addSlider(textSliderView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        forMapView();
        clickliostners();

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
                if (post_lat != 0 && post_lng != 0) {
                    post_loc = new LatLng(post_lat, post_lng);
                }
                else {
                    post_loc = new LatLng(22.78965, 75.3652);

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


    private void callButtonClicked(String user_id) {

        user_id = "182";
        System.out.println("userid ****** for calling " + user_id);
        if (user_id.isEmpty()) {
            Toast.makeText(this, "User Not Exist", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(user_id);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    @SuppressLint("SetTextI18n")
    private void setData(JSONObject responseObj) {


        try {
            post_lat = Double.parseDouble(responseObj.getString("latitude"));
            post_lng = Double.parseDouble(responseObj.getString("longitude"));
            post_owner_name = responseObj.getString("contact_person");
            owner_id = responseObj.getString("user_id");
            owner_image = responseObj.getString("profile_photo");
            post_id = responseObj.getString("id");
            post_time = responseObj.getString("post_created_datetime");
            job_type = responseObj.getString("job_type");
            String fav_status = responseObj.getString("favorite_status");
            String report_status = responseObj.getString("report_status");
            job_desc = responseObj.getString("description");
            job_name = responseObj.getString("title");

            job_descTv.setText(job_desc);
            detail_headTV.setText(job_name);
            owner_nameTV.setText(post_owner_name);
            post_timeTV.setText("Ad Posted At :" + post_time);

            if (fragment_name.equals("job"))
            {
                job_salary = responseObj.getString("salary_as_per");
                job_duties = responseObj.getString("job_responsibilities");
                experience = responseObj.getString("experience_skills");

                job_dutiestv.setText("Job Duties : "+job_duties);
                exptv.setText("Experience : "+experience);
                sizeTV.setVisibility(View.GONE);
                dogTv.setVisibility(View.GONE);
                catTv.setVisibility(View.GONE);
            }

            else {
                if (fragment_name.equals("property_sale"))
                {
                    job_salary = responseObj.getString("price").split("/")[0];
                }
                else
                {
                    job_salary = responseObj.getString("price");

                }

                experience = responseObj.getString("bathrooms");
                job_duties = responseObj.getString("rooms_bedrooms");

              String size  = responseObj.getString("size");
                String dog = responseObj.getString("dog_friendly");
                String cat = responseObj.getString("cat_friendly");

                sizeTV.setVisibility(View.VISIBLE);
                dogTv.setVisibility(View.VISIBLE);
                catTv.setVisibility(View.VISIBLE);

                job_dutiestv.setText("Rooms/Bedrooms : " + job_duties);
                exptv.setText("Bathrooms : " + experience);
                sizeTV.setText("Room size : "+size);
                dogTv.setText("Cat Friendly : "+dog);
                catTv.setText("Dog friendly : "+cat);
            }

            salaryTv.setText(job_salary);
            job_typeTV.setText(job_type);
            job_nameTV.setText(job_name);

            if (fav_status.equals("1"))
                Picasso.with(getApplicationContext()).load(R.drawable.fav).into(fav_btn);
            else if (fav_status.equals("0"))
                Picasso.with(getApplicationContext()).load(R.drawable.favv).into(fav_btn);

            if (report_status.equals("1"))
                Picasso.with(getApplicationContext()).load(R.drawable.flag_green).into(report_btn);
            else if (report_status.equals("0"))
                Picasso.with(getApplicationContext()).load(R.drawable.flag_red).into(report_btn);


            if (owner_image.equals(DEFAULT_PATH))
                Picasso.with(getApplicationContext()).load(R.drawable.profile).into(post_owner_image);

            else if (owner_image.length() < 1)
                post_owner_image.setBackground(getResources().getDrawable(R.drawable.profile));
            else
                Picasso.with(getApplicationContext()).load(owner_image).placeholder(R.drawable.profile).into(post_owner_image);

            PostedUserProfile(owner_id, post_id);
            String imageArray = responseObj.getString("featured_img");
            String[] uris = imageArray.split(",");

            imageSet = new ArrayList<String>(Arrays.asList(uris));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void PostedUserProfile(String post_user_id, String post_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams category = new RequestParams();

        category.put("user_id", post_user_id);
        category.put("post_id", post_id);
        client.post(BASE_URL_NEW + "get_profile", category, new JsonHttpResponseHandler() {


            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Bad Server Connection", Toast.LENGTH_SHORT).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  JSONObject response) {
                try {
                    if (response.getString("status").equals("1")) ;
                    {
                        OwnerDetailsObj = response.getJSONObject("result");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clickliostners() {

        back_btn.setOnClickListener(this);
        share_btn.setOnClickListener(this);
        chat_btn.setOnClickListener(this);
        call_btn.setOnClickListener(this);
        fav_btn.setOnClickListener(this);
        report_btn.setOnClickListener(this);
        share_btn.setOnClickListener(this);
        view_profile_btn.setOnClickListener(this);
        vdo_btn.setOnClickListener(this);
        post_owner_image.setOnClickListener(this);
    }
    private void init() {

        locationTV = (TextView) findViewById(R.id.address_tv);
        imageSlider = (SliderLayout) findViewById(R.id.slider);

        detail_headTV = (TextView) findViewById(R.id.title_TV);
        owner_nameTV = (TextView) findViewById(R.id.post_owner_name);
        post_timeTV = (TextView) findViewById(R.id.post_time);
        view_profile_btn = (TextView) findViewById(R.id.view_profile_btn);
        user_reviewTv = (TextView) findViewById(R.id.user_reviewTV);
        job_descTv = (TextView) findViewById(R.id.job_descTV);
        sizeTV = (TextView) findViewById(R.id.sizeTV);
        dogTv = (TextView) findViewById(R.id.dogTv);
        catTv = (TextView) findViewById(R.id.catTv);
        job_dutiestv = (TextView) findViewById(R.id.job_dutiestv);
        exptv = (TextView) findViewById(R.id.exptv);

        job_descheadTv = (TextView) findViewById(R.id.job_desc_head);
        address_headTV = (TextView) findViewById(R.id.address_head_tv);
        salaryTv = (TextView) findViewById(R.id.salaryTv);
        job_typeTV = (TextView) findViewById(R.id.job_typeTV);
        job_nameTV = (TextView) findViewById(R.id.job_name);
        post_owner_image = (ImageView) findViewById(R.id.profile_image);
        share_btn = (ImageView) findViewById(R.id.share_btn);
        fav_btn = (ImageView) findViewById(R.id.fav_btn);
        report_btn = (ImageView) findViewById(R.id.report_btn);
        call_btn = (ImageView) findViewById(R.id.call_btn);
        vdo_btn = (ImageView) findViewById(R.id.vdo_btn);
        chat_btn = (ImageView) findViewById(R.id.chat_btn);
        back_btn = (ImageView) findViewById(R.id.back_arrowImage);
        apply_job_btn = (Button) findViewById(R.id.apply_job_btn);


    }


    private void chagnefont(Typeface face) {
        detail_headTV.setTypeface(face);
        owner_nameTV.setTypeface(face);
        post_timeTV.setTypeface(face);
        view_profile_btn.setTypeface(face);
        user_reviewTv.setTypeface(face);
        job_descTv.setTypeface(face);
        sizeTV.setTypeface(face);
        catTv.setTypeface(face);
        dogTv.setTypeface(face);
        job_dutiestv.setTypeface(face);
        exptv.setTypeface(face);
        job_descheadTv.setTypeface(face);
        address_headTV.setTypeface(face);
        salaryTv.setTypeface(face);
        job_typeTV.setTypeface(face);
        job_nameTV.setTypeface(face);
        apply_job_btn.setTypeface(face);
        locationTV.setTypeface(face);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrowImage:
                onBackPressed();

                finish();
                break;

            case R.id.apply_job_btn:
                if (user_id.length()<1)
                    OpenWarning(JobsCatDetailsActivity.this);
                else {

                    if (fragment_name.equals("rental")) {
                        Intent applyjob = new Intent(getApplicationContext(), ApplyForRental.class);
                        applyjob.putExtra("post_id", post_id);
//                    userDetails.putExtra("ownerDetails", OwnerDetailsObj.toString());
                        startActivity(applyjob);
                    }
                    else if (fragment_name.equals("job"))
                    {
                        Intent applyjob = new Intent(getApplicationContext(), ApplyJobActivity.class);
                        applyjob.putExtra("post_id", post_id);
//                    userDetails.putExtra("ownerDetails", OwnerDetailsObj.toString());
                        startActivity(applyjob);
                    }
                    else if ( fragment_name.equals("property_sale")) {
                        Intent applyjob = new Intent(getApplicationContext(), ApplyForPropertySale.class);
                        applyjob.putExtra("post_id", post_id);
//                    userDetails.putExtra("ownerDetails", OwnerDetailsObj.toString());
                        startActivity(applyjob);
                    }
                }
                break;

            case R.id.vdo_btn:

//                callButtonClicked(owner_id);
                break;
            case R.id.call_btn:
//                AudiocallButtonClicked(owner_id);
                break;

            case R.id.fav_btn:
               user_id = getData(getApplicationContext(),"user_id","");
                Send_fav(user_id, post_id,fav_btn,JobsCatDetailsActivity.this);
                break;

                case R.id.report_btn:
                    openReportWarning(user_id, post_id,report_btn,JobsCatDetailsActivity.this);
                break;

            case R.id.profile_image:

                System.out.println("********* owner image *******");
                System.out.println(owner_image);
                openProfilePic(owner_image);
                break;

            case R.id.view_profile_btn:
                if (OwnerDetailsObj != null) {
                    Intent userDetails = new Intent(getApplicationContext(), PostOwnerProfileActivity.class);
                    userDetails.putExtra("ownerDetails", OwnerDetailsObj.toString());
                    userDetails.putExtra("latitude", post_lat);
                    userDetails.putExtra("longitude", post_lng);
                    startActivity(userDetails);


                } else {
                    Toast.makeText(getApplicationContext(), "Internal Server Error. Try Again ", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }


    private void openProfilePic(String owner_image) {
        final RelativeLayout open = (RelativeLayout) findViewById(R.id.fullsliderlay);
        final ImageView owner_fullIV = (ImageView) findViewById(R.id.owner_fullIV);

        ImageView close = (ImageView) findViewById(R.id.close_slider);
        open.setVisibility(View.VISIBLE);
        owner_fullIV.setVisibility(View.VISIBLE);

        if (owner_image.equals(DEFAULT_PATH))
            Picasso.with(getApplicationContext()).load(R.drawable.profile).into(owner_fullIV);

        else if (owner_image.length() < 1)
            owner_fullIV.setBackground(getResources().getDrawable(R.drawable.profile));
        else
            Picasso.with(getApplicationContext()).load(owner_image).placeholder(R.drawable.profile).into(owner_fullIV);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open.setVisibility(View.GONE);
                owner_fullIV.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        try {
            double item_lat = Double.parseDouble(responseObj.getString("latitude"));
            double item_lng = Double.parseDouble(responseObj.getString("longitude"));
            item_location = new LatLng(item_lat, item_lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (item_location == null) {
            item_location = new LatLng(43.1, -87.9);
        }

        map.addMarker(new MarkerOptions().position(item_location).title("You're Location"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(item_location).tilt(45).bearing(45).zoom((float) 12.5).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        System.out.println("************ slider clicked ************");
        final RelativeLayout open = (RelativeLayout) findViewById(R.id.fullsliderlay);
        final SliderLayout imageSlider = (SliderLayout) findViewById(R.id.slidefullr);

        ImageView close = (ImageView) findViewById(R.id.close_slider);
        open.setVisibility(View.VISIBLE);
        imageSlider.setVisibility(View.VISIBLE);

        HashMap<String, String> url_maps = new HashMap<String, String>();

        for (int i = 0; i < imageSet.size(); i++) {
            url_maps.put("image" + i, imageSet.get(i));

        }
        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            imageSlider.addSlider(textSliderView);
            imageSlider.stopAutoCycle();
            imageSlider.clearAnimation();
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open.setVisibility(View.GONE);
                imageSlider.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        PostedUserProfile(owner_id, post_id);

    }
}
