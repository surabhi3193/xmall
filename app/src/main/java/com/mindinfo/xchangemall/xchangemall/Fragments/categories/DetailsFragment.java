package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.mindinfo.xchangemall.xchangemall.activities.common.PaymentActivity;
import com.mindinfo.xchangemall.xchangemall.activities.common.PostOwnerProfileActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.SliderAdapter2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import me.relex.circleindicator.CircleIndicator;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.Send_fav;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.openReportWarning;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.DEFAULT_PATH;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.user_image;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;

/**
 * Created by Mind Info- Android on 21-Nov-17.
 */

public class DetailsFragment extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    String user_id = "", cat_id = "";
    ArrayList<Uri> imageSet =new ArrayList<>();

    Marker mapMarker;
    GoogleMap map;
    JSONObject responseOBj, ownerDetailsoBj;
    String response, item_owner_name, item_post_time, item_prize, item_details,
            item_description, post_user_id, fav_status = "", report_status = "", item_cat = "", item_size = "Not disclosed", item_condition = "Not disclosed";
    TextView item_ownerTV, item_postTimeTv, view_profileTV, item_priceTv, item_detailsTV,
            item_descriptionTV, description_headerTv, locationTV, subCatTv, catheadTV, conditionTv, sizeTv;
    double item_lat, item_lng;

    Button buynow_btn;
    LatLng item_location = null;
    ImageView post_user_img, ImageView_fav, report_btn;
    String profile_photo, post_id;
    ViewPager mPager;
    CircleIndicator indicator;
    private ImageView back_arrowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        finditem();
        ownerDetailsoBj = new JSONObject();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            response = bundle.getString("responseObj");
            System.out.println("********* details post response **********");
            System.out.println(response);
            try {
                responseOBj = new JSONObject(response);
                item_lat = Double.parseDouble(responseOBj.getString("latitude"));
                item_lng = Double.parseDouble(responseOBj.getString("longitude"));
                item_location = new LatLng(item_lat, item_lng);
                post_id = responseOBj.getString("id");
                fav_status = responseOBj.getString("favorite_status");
                report_status = responseOBj.getString("report_status");
                item_owner_name = responseOBj.getString("first_name");
                post_user_id = responseOBj.getString("user_id");

                item_prize = responseOBj.getString("price");
                item_cat = responseOBj.getString("category");

                item_size = responseOBj.getString("size");
                item_condition = responseOBj.getString("conditions");

                item_details = responseOBj.getString("title");
                profile_photo = responseOBj.getString("profile_photo");
                user_image = profile_photo;
                item_description = responseOBj.getString("description");
                item_post_time = responseOBj.getString("post_created_datetime");
                String imageArray = responseOBj.getString("featured_img");


                forMapView();

                String[] uris = imageArray.split(",");

                if (imageSet.size() > 0)
                    imageSet.clear();

                for (String uri : uris) {
                    imageSet.add(Uri.parse(uri));
                    System.out.println("----- > image-- > ");
                    System.out.println(Uri.parse(uri));

                }
                mPager.setAdapter(new SliderAdapter2(DetailsFragment.this, imageSet));

                mPager.setBackgroundColor(getResources().getColor(R.color.black));
                indicator.setViewPager(mPager);

                if (fav_status.equals("1"))
                    ImageView_fav.setImageResource(R.drawable.fav);
                else if (fav_status.equals("0"))
                    ImageView_fav.setImageResource(R.drawable.favv);
                if (report_status.equals("1"))
                    report_btn.setImageResource(R.drawable.flag_green);
                else if (report_status.equals("0"))
                    report_btn.setImageResource(R.drawable.flag_red);
                if (user_image.equals(DEFAULT_PATH))
                post_user_img.setImageResource(R.drawable.profile);

                else if (user_image.length() < 1)
                    post_user_img.setBackground(getResources().getDrawable(R.drawable.profile));
                else
                   Glide.with(getApplicationContext()).load(profile_photo).into(post_user_img);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            cat_id = bundle.getString("cat_id");


        }


        user_id = getData(getApplicationContext(), "user_id", "");
        setData();

        clickitem();

    }

    @Override
    protected void onResume() {


        super.onResume();

        PostedUserProfile(post_user_id, post_id);
    }

    private void forMapView() {


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            if (mapMarker != null)
                mapMarker.remove();
            LatLng post_loc;
            if (item_lat != 0 && item_lng != 0) {
                post_loc = new LatLng(item_lat, item_lng);
            } else {
                post_loc = new LatLng(0.0, 0.0);

            }
            mapMarker = map.addMarker(new MarkerOptions().position(post_loc).title("Owner Location"));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(post_loc).tilt(45).bearing(45).zoom((float) 18.5).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            String address = getAddressFromLatlng(post_loc, getApplicationContext(), 1);
            locationTV.setText(address);

        });


    }


    @SuppressLint("SetTextI18n")
    private void setData() {
        item_ownerTV.setText(item_owner_name);
        item_postTimeTv.setText("Ad Posted At :" + item_post_time);
        item_priceTv.setText(item_prize);
        subCatTv.setText(item_cat);
        sizeTv.setText("Size : " + item_size);
        conditionTv.setText("Condition : " + item_condition);

        item_detailsTV.setText(item_details);
        item_descriptionTV.setText(item_description);

        Typeface face = ResourcesCompat.getFont(DetailsFragment.this, R.font.estre);

        item_ownerTV.setTypeface(face);
        item_descriptionTV.setTypeface(face);
        item_detailsTV.setTypeface(face);
        item_priceTv.setTypeface(face);
        item_postTimeTv.setTypeface(face);
        view_profileTV.setTypeface(face);
        description_headerTv.setTypeface(face);
        catheadTV.setTypeface(face);
        buynow_btn.setTypeface(face);
        locationTV.setTypeface(face);
        subCatTv.setTypeface(face);
        conditionTv.setTypeface(face);
        sizeTv.setTypeface(face);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void finditem() {
        mPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);

        ImageView_fav =findViewById(R.id.ImageView_fav);
        report_btn =findViewById(R.id.report_btn);
        post_user_img =findViewById(R.id.post_user_img);
        back_arrowImage =findViewById(R.id.back_arrowImage);
        item_ownerTV =findViewById(R.id.item_post_username);
        item_postTimeTv =findViewById(R.id.posted_timeTV);
        view_profileTV =findViewById(R.id.view_profile);
        item_priceTv =findViewById(R.id.itemPrice);
        item_detailsTV =findViewById(R.id.item_name);
        locationTV =findViewById(R.id.locationName);
        subCatTv =findViewById(R.id.subCatTv);
        conditionTv =findViewById(R.id.conditionTv);
        sizeTv =findViewById(R.id.rommSIzeTv);
        item_descriptionTV =findViewById(R.id.item_description);
        description_headerTv =findViewById(R.id.description_header);
        catheadTV =findViewById(R.id.catheadTV);
        buynow_btn =findViewById(R.id.buy_btn);

    }

    private void clickitem() {
        back_arrowImage.setOnClickListener(this);
        view_profileTV.setOnClickListener(this);
        buynow_btn.setOnClickListener(this);
        ImageView_fav.setOnClickListener(this);
        report_btn.setOnClickListener(this);
        post_user_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrowImage:
                onBackPressed();
                break;

            case R.id.view_profile:

                Intent userDetails = new Intent(getApplicationContext(), PostOwnerProfileActivity.class);
                userDetails.putExtra("ownerDetails", ownerDetailsoBj.toString());
                userDetails.putExtra("latitude", item_lat);
                userDetails.putExtra("longitude", item_lng);
                startActivity(userDetails);

                break;

            case R.id.buy_btn:
                Intent next = new Intent(getApplicationContext(), PaymentActivity.class);
                next.putExtra("ownerDetails", ownerDetailsoBj.toString());
                startActivity(next);

                break;

            case R.id.ImageView_fav:
                user_id = getData(getApplicationContext(), "user_id", "");
                Send_fav(user_id, post_id, ImageView_fav, DetailsFragment.this);
                break;

            case R.id.report_btn:
                user_id = getData(getApplicationContext(), "user_id", "");

                openReportWarning(user_id, post_id, report_btn, DetailsFragment.this);
                break;
            case R.id.post_user_img:

                System.out.println("********* owner image *******");
                System.out.println(profile_photo);
                openProfilePic(profile_photo);
                break;


        }
    }

    private void openProfilePic(String owner_image) {
        final RelativeLayout open =findViewById(R.id.fullsliderlay);
        final ImageView owner_fullIV =findViewById(R.id.owner_fullIV);

        ImageView close =findViewById(R.id.close_slider);
        open.setVisibility(View.VISIBLE);
        owner_fullIV.setVisibility(View.VISIBLE);
        buynow_btn.setVisibility(View.GONE);

        if (owner_image.equals(DEFAULT_PATH))
           Glide.with(getApplicationContext()).load(R.drawable.profile).into(owner_fullIV);

        else if (owner_image.length() < 1)
            owner_fullIV.setBackground(getResources().getDrawable(R.drawable.profile));
        else
           Glide.with(getApplicationContext()).load(owner_image).into(owner_fullIV);

        close.setOnClickListener(view -> {
            open.setVisibility(View.GONE);
            owner_fullIV.setVisibility(View.GONE);
            buynow_btn.setVisibility(View.VISIBLE);
        });

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
                    if (response.getString("status").equals("1"))
                    {
                        DetailsFragment.this.ownerDetailsoBj = response.getJSONObject("result");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
    protected void onPause() {
        super.onPause();
        System.gc();
    }
}
