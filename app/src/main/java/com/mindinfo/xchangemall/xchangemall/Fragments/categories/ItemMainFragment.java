package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.activities.main.MultiPhotoSelectActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.ForSaleAdapter;
import com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY;
import com.mindinfo.xchangemall.xchangemall.beans.categories;
import com.mindinfo.xchangemall.xchangemall.intefaces.OnBackPressed;
import com.mindinfo.xchangemall.xchangemall.other.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenWarning;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getRealPathFromURI;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.hideKeyboard;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.isAlpha;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.resetData;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class ItemMainFragment extends Fragment implements View.OnClickListener, OnBackPressed {

    final ArrayList<categories> arrayList = new ArrayList<>();
    private  final int PLACE_PICKER_REQUEST = 23;
    TextView noPostTv;
    EditText searchbox;
    RelativeLayout snackbarPosition;
    private String str_image_arr[];
    private String csv = "";
    private LinearLayout postImageLay;
    private FragmentManager fm;
    private int slider_max_price = 10000;
    private int slider_min_price = 0;
    private Typeface face;
    private String search_key = "", cat_id = "", sortby = "", type = "search", price_min = "", price_max = "",
            country = "", city = "", latitude = "", longitude = "", pcat_id = "104", user_id = "";
    private TextView cancel_btn;
    private TextView cameraIV;
    private TextView galleryIV;
    private Bundle bundle;
    private ProgressBar progressbar;
    private RelativeLayout catlog;
    private Button cancel_button;
    private Button confirm_btn;
    private ListView cat_sub_list_view;
    private TextView title_cat;
    private Uri imageUri;
    private RecyclerView recyclerViewItem;
    private ForSaleAdapter itemlistAdapter;
    private LinearLayout Post_camera_icon;
    private LinearLayout property_rental;
    private LinearLayout news_top;
    private LinearLayout games_top;
    private LinearLayout property_rentalsale;
    private LinearLayout jobs;
    private LinearLayout buisness;
    private LinearLayout personel;
    private LinearLayout community;
    private LinearLayout showcase;
    private TextView currentLocation, priceTextView, cat_TextView, mostRecentTextView;
    private PullRefreshLayout refreshLayout;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_itemmain_l1, null);
        face = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()), R.font.estre);
        fm = getActivity().getSupportFragmentManager();

        user_id = getData(getActivity(), "user_id", "");
        findbyview(v);

        addClickListner(v);

        bundle = getArguments();
        if (bundle != null) {
            pcat_id = "104";
            cat_id = bundle.getString("subCatID", "");
            csv = cat_id;
            if (isNetworkAvailable(getActivity())) {
                if (idarray.size() > 0)
                    idarray.clear();
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            } else
                Toast.makeText(getActivity(), "No Intenet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (isNetworkAvailable(getActivity())) {
                if (idarray.size() > 0)
                    idarray.clear();
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            } else
                Toast.makeText(getActivity(), "No Intenet Connection", Toast.LENGTH_LONG).show();
        }

        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        refreshLayout.setOnRefreshListener(() -> refreshLayout.postDelayed(() -> {
            search_key = "";
            csv = "";
            sortby = "";
            type = "search";
            price_min = "";
            price_max = "";
            latitude = "";
            longitude = "";
            pcat_id = "104";
            currentLocation.setText("Current location");

            if (idarray.size() > 0)
                idarray.clear();

            loadPost(user_id, "", country, city, type, "", pcat_id, "", "", "", "", "");

            refreshLayout.setRefreshing(false);
        }, 2000));


        return v;
    }

    private void findbyview(View v) {
        refreshLayout =v.findViewById(R.id.refreshLay);
        recyclerViewItem =v.findViewById(R.id.recyclerViewItem);
        noPostTv = v.findViewById(R.id.noPostTv);
        progressbar =v.findViewById(R.id.progressbar);
        catlog =v.findViewById(R.id.catlog);
        cancel_button =v.findViewById(R.id.cancel_button);
        confirm_btn =v.findViewById(R.id.confirm_btn);
        title_cat = v.findViewById(R.id.title_cat);
        Post_camera_icon =v.findViewById(R.id.Post_camera_icon);
        postImageLay =v.findViewById(R.id.postImageLay);
        property_rental =v.findViewById(R.id.property_rental_top);
        property_rentalsale =v.findViewById(R.id.property_rentalsale_top);
        news_top =v.findViewById(R.id.news_top);
        games_top =v.findViewById(R.id.games_top);
        jobs =v.findViewById(R.id.jobs_top);
        LinearLayout for_sale =v.findViewById(R.id.forsale_top);
        buisness =v.findViewById(R.id.buisness_top);
        personel =v.findViewById(R.id.personal_top);
        community =v.findViewById(R.id.community_top);
        showcase =v.findViewById(R.id.showcase_top);

        cat_sub_list_view =v.findViewById(R.id.cat_sub_list_view);
        cancel_button =v.findViewById(R.id.cancel_button);
        confirm_btn =v.findViewById(R.id.confirm_btn);
        cancel_btn = v.findViewById(R.id.cancel_btnIV);
        cameraIV = v.findViewById(R.id.cameraIV);
        galleryIV = v.findViewById(R.id.gallerIV);
        TextView addimageHEaderTV = v.findViewById(R.id.addimageheader);
        currentLocation = v.findViewById(R.id.currentLocation);
        priceTextView = v.findViewById(R.id.priceTextView);
        snackbarPosition =v.findViewById(R.id.snackbarPosition);
        cat_TextView = v.findViewById(R.id.cat_TextView);
        mostRecentTextView = v.findViewById(R.id.mostRecentTextView);
        Button forsaleTV =v.findViewById(R.id.forsaleTV);
        searchbox =v.findViewById(R.id.msearch);

        catlog.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        postImageLay.setVisibility(View.GONE);

        for_sale.getParent().requestChildFocus(for_sale, for_sale);

        progressbar.setVisibility(View.GONE);
        currentLocation.setTypeface(face);
        priceTextView.setTypeface(face);
        cat_TextView.setTypeface(face);
        mostRecentTextView.setTypeface(face);
        searchbox.setTypeface(face);
        cameraIV.setTypeface(face);
        galleryIV.setTypeface(face);
        addimageHEaderTV.setTypeface(face);

        forsaleTV.setBackgroundResource(R.drawable.blue_btn);

        saveData(getActivity(), "MainCatType", "104");

        recyclerViewItem.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewItem.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerViewItem.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerViewItem.setItemAnimator(new DefaultItemAnimator());
    }
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void addClickListner(View view) {
        jobs.setOnClickListener(this);
        buisness.setOnClickListener(this);
        property_rental.setOnClickListener(this);
        property_rentalsale.setOnClickListener(this);
        news_top.setOnClickListener(this);
        games_top.setOnClickListener(this);
        showcase.setOnClickListener(this);
        personel.setOnClickListener(this);
        community.setOnClickListener(this);
        Post_camera_icon.setOnClickListener(this);
        currentLocation.setOnClickListener(this);
        priceTextView.setOnClickListener(this);
        cat_TextView.setOnClickListener(this);
        mostRecentTextView.setOnClickListener(this);
        searchbox.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                search_key = searchbox.getText().toString();
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
                hideKeyboard(getActivity());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Post_camera_icon:
                catlog.setVisibility(View.GONE);
                String username = getData(getActivity(), "user_name", "");
                if (username.length() > 2)
                    Addpost(v);
                else
                    OpenWarning(getActivity());
                break;
            case R.id.currentLocation:
                catlog.setVisibility(View.GONE);

                ShowSnakforCurrent();
                break;
            case R.id.priceTextView:
                catlog.setVisibility(View.GONE);
                ShowPriceSnack();
                break;
            case R.id.cat_TextView:
                ShowCategeriesSnak();
                break;


            case R.id.mostRecentTextView:
                catlog.setVisibility(View.GONE);
                sortby = "newfirst";
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
                break;

            case R.id.personal_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "100");
//                SocialFragment personal_main = new SocialFragment();
//                personal_main.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, personal_main).addToBackStack(null).commit();

                break;

            case R.id.buisness_top:
                catlog.setVisibility(View.GONE);
                bundle.putString("MainCatType", "101");
                Bussiness_Service_Main business_main = new Bussiness_Service_Main();
                business_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, business_main).addToBackStack(null).commit();

                break;

            case R.id.property_rental_top:
                property_rental.requestFocus();
                catlog.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


                bundle.putString("MainCatType", "102");
                Property_Rental_Fragment jobsMAin = new Property_Rental_Fragment();
                jobsMAin.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobsMAin).addToBackStack(null).commit();
                break;


            case R.id.jobs_top:
                catlog.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();

                bundle.putString("MainCatType", "103");
                JobsMainFragment forsale_main = new JobsMainFragment();
                forsale_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, forsale_main).addToBackStack(null).commit();

                break;


            case R.id.showcase_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "105");
//                ShowCaseFragment showcase_top = new ShowCaseFragment();
//                showcase_top.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, showcase_top).addToBackStack(null).commit();

                break;

            case R.id.community_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();


                bundle.putString("MainCatType", "106");
                CommunityFragment communityFragment = new CommunityFragment();
                communityFragment.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, communityFragment).addToBackStack(null).commit();

                break;

            case R.id.property_rentalsale_top:
                catlog.setVisibility(View.GONE);

                bundle.putString("MainCatType", "272");
                Property_Sale_Fragment property_sale = new Property_Sale_Fragment();
                property_sale.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, property_sale).addToBackStack(null).commit();

                break;

            case R.id.news_top:
                bundle.putString("MainCatType", "309");
                NewsFragment news_top = new NewsFragment();
                news_top.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, news_top).addToBackStack(null).commit();
                break;
            case R.id.games_top:
                bundle.putString("MainCatType", "373");
                GamesFragment games = new GamesFragment();
                games.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, games).addToBackStack(null).commit();
                break;

        }
    }

    private void ShowPriceSnack() {

        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(getActivity()));
        LayoutInflater objLayoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert objLayoutInflater != null;
        @SuppressLint("InflateParams") View view = objLayoutInflater.inflate(R.layout.snackbar_price_layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        EditText maxTv =view.findViewById(R.id.maxTV);
        EditText minTv =view.findViewById(R.id.minTv);
        Button cancel_button =view.findViewById(R.id.cancel_button);
        Button conformButton =view.findViewById(R.id.conformButton);

        System.out.println("-------- min / max at rangeBar------ " + slider_min_price + " / " + slider_max_price);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        conformButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                price_min = minTv.getText().toString();
                price_max = maxTv.getText().toString();

                if (price_min.length()==0) {
                    minTv.setError("Mandatory Field");
                    return;
                }

                if (price_max.length()==0) {
                    maxTv.setError("Mandatory Field");
                    return;
                }
                if (Double.parseDouble(price_min.trim())>Double.parseDouble(price_max.trim()))
                {
                    Toast.makeText(getActivity(),"Max price should be greater then Min price ",Toast.LENGTH_LONG).show();
                    return;
                }

                    dialog.dismiss();
                    loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);


            }
        });
        dialog.show();

    }

    public void ShowCategeriesSnak() {
        catlog.setVisibility(View.VISIBLE);
        title_cat.setText(getResources().getString(R.string.for_sale));
        title_cat.setTypeface(face);
        MULTIPLEsELECTIONcATEGORY postadapter = new MULTIPLEsELECTIONcATEGORY(arrayList, getActivity());
        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cat_sub_list_view.setAdapter(postadapter);

        NetworkClass.getListData("104", arrayList, getActivity());
        postadapter.notifyDataSetChanged();


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catlog.setVisibility(View.GONE);
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                catlog.setVisibility(View.GONE);

                csv = idarray.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
                cat_id = "";

            }
        });

    }

    private void loadPost(String user_id, String search_key, String country, String city, String type, String sortby,
                          String pcat_id, String cat_id, String price_max, String price_min,
                          String latitude, String longitude) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", false);
        ringProgressDialog.setCancelable(false);
        params.put("user_id", user_id);
        params.put("val", search_key);
        params.put("type", type);
        params.put("sortby", sortby);
        params.put("price_min", price_min);
        params.put("price_max", price_max);
        params.put("country", country);
        params.put("city", city);
        params.put("pcat_id", pcat_id);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("cat_id", cat_id);
        System.out.println("************* requested params ****************");
        System.err.println(params);
        client.post(BASE_URL_NEW + "search_post", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
ringProgressDialog.dismiss();
                System.out.println(response);
                try {
                    String status = response.getString("status");
                    switch (status) {
                        case "0":

                            recyclerViewItem.setVisibility(View.GONE);
                            noPostTv.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.GONE);

                            break;
                        case "1":
                            // this is the array of response i am getting for listview
                            JSONArray posts = response.getJSONArray("result");

                            String range_max = response.getString("max_price");
                            String min_price = response.getString("min_price");

                            double abc = 0.00;
                            double def = 0.00;
                            if (range_max.length()>0 && !isAlpha(range_max))
                                abc = Double.parseDouble(range_max);

                            if (min_price.length()>0 && !isAlpha(min_price))
                                def = Double.parseDouble(min_price);

                            System.out.println("--------- max price from api ------ " + abc);
                            System.out.println("--------- min price from api ------ " + def);
                            slider_max_price = (int) abc;
                            slider_min_price = (int) def;

                            System.out.println(posts);

                            if (posts.length() > 0)
                            {
                                recyclerViewItem.setVisibility(View.VISIBLE);
                                noPostTv.setVisibility(View.GONE);
                                itemlistAdapter = new ForSaleAdapter(getActivity(), posts, "sale");
                                recyclerViewItem.setAdapter(itemlistAdapter);
                                itemlistAdapter.notifyDataSetChanged();
                            }
                            else {
                                recyclerViewItem.setVisibility(View.GONE);
                                noPostTv.setVisibility(View.VISIBLE);
                                progressbar.setVisibility(View.GONE);

                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
                ringProgressDialog.dismiss();
            }
        });

    }


    private void ShowSnakforCurrent() {
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.isGpsConnected()) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void Addpost(View v) {

        resetData(getActivity());
        catlog.setVisibility(View.GONE);
        postImageLay.setVisibility(View.VISIBLE);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImageLay.setVisibility(View.GONE);
            }
        });

        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String fileName = "Camera_Example.jpg";


                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.TITLE, fileName);

                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

                // imageUri is the current activity attribute, define and save it for later usage

                imageUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/


                // Standard Intent action that can be sent to have the camera
                // application capture an image and return it.

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                startActivityForResult(intent, 01);
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity().getBaseContext(), MultiPhotoSelectActivity.class), 0X11);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        postImageLay.setVisibility(View.GONE);


        System.out.println("******** result from camera **********");
        System.out.println("result " + resultCode);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                progressbar.setVisibility(View.GONE);

                Place place = PlacePicker.getPlace(data, getActivity());
             LatLng latLng = place.getLatLng();
             latitude= String.valueOf(latLng.latitude);
             longitude= String.valueOf(latLng.longitude);

                System.out.println(place.getName());
                String new_location = place.getName().toString();
                currentLocation.setText("  " + new_location);
//                saveData(getApplicationContext(), "currentLocation", new_location);
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            }
        }

        if (requestCode == 01) {
            if (resultCode == -1) {

                ArrayList<String> imageArray = new ArrayList<>();
                ArrayList<Uri> UriimageArray = new ArrayList<>();
                System.out.println("********** image uri ****");
                System.out.println(imageUri);

                UriimageArray.add(imageUri);
                imageArray.add(getRealPathFromURI(imageUri, getActivity()));
                saveData(getActivity(), "item_img0", getRealPathFromURI(imageUri, getActivity()));

                nextFragment(UriimageArray, str_image_arr);
            }
        }

        if (requestCode == 17) {
            if (resultCode == 1) {
                saveData(getActivity(), "language", "select");
                saveData(getActivity(), "first_entry", "true");
                saveData(getActivity(), "first_entry_contact", "");
                saveData(getActivity(), "first_entry_cat", "true");

                ArrayList<String> responseArray = new ArrayList<>();
                ArrayList<String> newimageArray = new ArrayList<>();
                ArrayList<Uri> UrimageArray = new ArrayList<>();
                responseArray = data.getStringArrayListExtra("MESSAGE");

                if (responseArray.size()==0)
                {
                    Toast.makeText(getActivity(), "Select Min 1 image to proceed",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (responseArray.size() > 4) {
                    Toast.makeText(getActivity(), "Maximum 4 pics allowed", Toast.LENGTH_LONG).show();

               return;
                }
                    for (int i = 0; i < responseArray.size(); i++) {
                        Uri uri = Uri.fromFile(new File(responseArray.get(i)));

                        Log.e("Uri" + i, uri.toString());
                        UrimageArray.add(uri);
                        saveData(getActivity(), "item_img" + i, uri.getPath());
                        newimageArray.add(uri.getPath());
                        str_image_arr = new String[]{uri.toString()};
                    }
                    nextFragment(UrimageArray, str_image_arr);


            }
        }
    }

    public void nextFragment(ArrayList<Uri> newimageArray, String[] str_image_arr) {
        saveData(getActivity(), "language", "select");
        saveData(getActivity(), "first_entry", "true");
        saveData(getActivity(), "first_entry_contact", "");
        saveData(getActivity(), "first_entry_cat", "true");

        Bundle bundle = new Bundle();
        bundle.putStringArray("imagess", str_image_arr);
        bundle.putParcelableArrayList("imageSet", newimageArray);
        bundle.putString("MainCatType", "104");
        startActivity(new Intent(getActivity(), Postyour2Add.class).putExtras(bundle));
    }

    @Override
    public void onResume() {
        super.onResume();
        progressbar.setVisibility(View.GONE);
        if (isNetworkAvailable(getActivity()))

            loadPost(user_id, search_key, "", "", type, sortby, "104", csv, price_max, price_min, latitude, longitude);
        else
            Toast.makeText(getActivity(), "No Intenet Connection", Toast.LENGTH_LONG).show();
    }

    public void onBackPressed() {
        startActivity(new Intent(getActivity(),
                MainActivity.class).putExtra("EXIT", true));

    }

    @Override
    public void onPause() {
        super.onPause();
        System.gc();
    }


}
