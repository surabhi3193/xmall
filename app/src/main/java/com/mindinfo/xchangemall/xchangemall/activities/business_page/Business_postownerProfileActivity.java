package com.mindinfo.xchangemall.xchangemall.activities.business_page;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.HoursAdapter;
import com.mindinfo.xchangemall.xchangemall.other.StretchVideoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.DEFAULT_PATH;

public class Business_postownerProfileActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    ArrayList<String> imageSet = new ArrayList<String>();
    GoogleMap map;
    boolean isplaying = false;
    private Marker marker;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_postowner_profile);

        this.getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ListView list = findViewById(R.id.hourList);

        SliderLayout imageSlider = findViewById(R.id.slider);
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
//        ScrollView scrollview = findViewById(R.id.scrollview);
//        final StretchVideoView videoView = findViewById(R.id.videoView);
        CircleImageView profile_image = findViewById(R.id.profile_image);

        ImageView back_arrowImage = findViewById(R.id.back_arrowImage);
        back_arrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

//        scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                videoView.pause();
//                isplaying=false;
//            }
//        });
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
                JSONArray operationArray = responseObj.getJSONArray("hours_of_operation");
                double post_lat = Double.parseDouble(responseObj.getString("latitude"));
                double post_lng = Double.parseDouble(responseObj.getString("longitude"));

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


                String[] uris = imageArray.split(",");

                if (imageSet.size() > 0)
                    imageSet.clear();

                imageSet = new ArrayList<String>(Arrays.asList(uris));


                if (profile.length() > 0 && profile != null) {
                    Picasso.with(Business_postownerProfileActivity.this)
                            .load(profile)
                            .placeholder(R.drawable.profile)
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

                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openProfilePic(profile);
                    }
                });

                if (operationArray.length() > 0) {
                    ArrayList<String> daylist = new ArrayList<String>();
                    ArrayList<String> startlist = new ArrayList<String>();
                    ArrayList<String> endlist = new ArrayList<String>();

                    for (int i = 0; i < operationArray.length(); i++) {
                        System.err.println("==== obj ==== " + i);
                        JSONObject json_data = operationArray.getJSONObject(i);

                        daylist.add(json_data.getString("days")); //add to arraylist
                        startlist.add(json_data.getString("open_time")); //add to arraylist
                        endlist.add(json_data.getString("close_time")); //add to arraylist
                    }

                    String[] dayArray = daylist.toArray(new String[daylist.size()]);
                    String[] startArray = startlist.toArray(new String[startlist.size()]);
                    String[] endArray = endlist.toArray(new String[endlist.size()]);

                    HoursAdapter adapter = new HoursAdapter(Business_postownerProfileActivity.this,
                            dayArray, startArray, endArray);
                    setListViewHeightBasedOnChildren(list);
                    list.setAdapter(adapter);
                }

//
//                list.setOnTouchListener(new ListView.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int action = event.getAction();
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                // Disallow ScrollView to intercept touch events.
//                                v.getParent().requestDisallowInterceptTouchEvent(true);
//                                break;
//
//                            case MotionEvent.ACTION_UP:
//                                // Allow ScrollView to intercept touch events.
//                                v.getParent().requestDisallowInterceptTouchEvent(false);
//                                break;
//                        }
//
//                        // Handle ListView touch events.
//                        v.onTouchEvent(event);
//                        return true;
//                    }
//                });


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {


            }
        }

    }

    private Bitmap setVideoThumbnail(String video_url) throws Throwable{

            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try
            {
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

    @Override
    public void onSliderClick(BaseSliderView slider) {

        System.out.println("************ slider clicked ************");
        final RelativeLayout open = (RelativeLayout) findViewById(R.id.fullsliderlay);
        final SliderLayout imageSlider2 = (SliderLayout) findViewById(R.id.slidefullr);

        ImageView close = (ImageView) findViewById(R.id.close_slider);
        open.setVisibility(View.VISIBLE);
        imageSlider2.setVisibility(View.VISIBLE);

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
            imageSlider2.addSlider(textSliderView);
            imageSlider2.stopAutoCycle();
            imageSlider2.clearAnimation();
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open.setVisibility(View.GONE);
                imageSlider2.setVisibility(View.GONE);
                imageSet.clear();
            }
        });

    }

    private void forMapView(final double lat, final double lng) {


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (marker != null)
                    marker.remove();
                LatLng post_loc;
                if (lat != 0 && lng != 0) {
                    post_loc = new LatLng(lat, lng);
                } else {
                    post_loc = new LatLng(22.78965, 75.3652);

                }
                marker = map.addMarker(new MarkerOptions().position(post_loc).title("Owner Location"));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(post_loc).tilt(45).bearing(45).zoom((float) 18.5).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });


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

}
 