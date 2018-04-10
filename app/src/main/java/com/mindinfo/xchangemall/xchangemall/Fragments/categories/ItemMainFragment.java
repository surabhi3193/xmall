package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
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
import com.mindinfo.xchangemall.xchangemall.activities.main.MultiPhotoSelectActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.ForSaleAdapter;
import com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY;
import com.mindinfo.xchangemall.xchangemall.beans.ItemsMain;
import com.mindinfo.xchangemall.xchangemall.beans.categories;
import com.mindinfo.xchangemall.xchangemall.other.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenWarning;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getRealPathFromURI;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.hideKeyboard;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.isAlpha;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.resetData;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class ItemMainFragment extends Fragment implements View.OnClickListener {

    private static final int PLACE_PICKER_REQUEST = 23;
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 22;
    private String str_image_arr[];
    private String csv = "";
    TextView noPostTv;
    EditText searchbox;
    private LinearLayout postImageLay;
    private FragmentManager fm;
    RelativeLayout snackbarPosition;
    private int slider_max_price = 10000;
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
    private ListView recyclerViewItem;
    private ForSaleAdapter itemlistAdapter;
    private List<ItemsMain> itemList;
    private LinearLayout Post_camera_icon;
    private LinearLayout property_rental;
    private LinearLayout property_rentalsale;
    private LinearLayout jobs;
    private LinearLayout buisness;
    private LinearLayout personel;
    private LinearLayout community;
    private LinearLayout showcase;
    private TextView currentLocation, priceTextView, cat_TextView, mostRecentTextView;
    private int image_count_before;
    private boolean fromLocation;
    private PullRefreshLayout refreshLayout;
    final ArrayList<categories> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_itemmain_l1, null);
        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        fm = getActivity().getSupportFragmentManager();

        user_id = getData(getActivity().getApplicationContext(), "user_id", "");
        findbyview(v);

        addClickListner(v);
        fromLocation = false;


        bundle = getArguments();
        if (bundle != null) {
            pcat_id = "104";
            cat_id = bundle.getString("subCatID", "");
            csv = cat_id;
            if (isNetworkAvailable(getActivity().getApplicationContext())) {
                if (idarray.size() > 0)
                    idarray.clear();
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            } else
                Toast.makeText(getActivity().getApplicationContext(), "No Intenet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (isNetworkAvailable(getActivity().getApplicationContext())) {
                if (idarray.size() > 0)
                    idarray.clear();
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            } else
                Toast.makeText(getActivity().getApplicationContext(), "No Intenet Connection", Toast.LENGTH_LONG).show();
        }

        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                }, 2000);
            }
        });


        return v;
    }

    private void findbyview(View v) {


        refreshLayout = (PullRefreshLayout) v.findViewById(R.id.refreshLay);

        recyclerViewItem = (ListView) v.findViewById(R.id.recyclerViewItem);
        noPostTv = (TextView) v.findViewById(R.id.noPostTv);
        progressbar = (ProgressBar) v.findViewById(R.id.progressbar);
        catlog = (RelativeLayout) v.findViewById(R.id.catlog);
        cancel_button = (Button) v.findViewById(R.id.cancel_button);
        confirm_btn = (Button) v.findViewById(R.id.confirm_btn);
        title_cat = (TextView) v.findViewById(R.id.title_cat);
        catlog.setVisibility(View.GONE);
        //Post_camera_icon find
        Post_camera_icon = (LinearLayout) v.findViewById(R.id.Post_camera_icon);
        postImageLay = (LinearLayout) v.findViewById(R.id.postImageLay);
        property_rental = (LinearLayout) v.findViewById(R.id.property_rental_top);
        property_rentalsale = (LinearLayout) v.findViewById(R.id.property_rentalsale_top);
        jobs = (LinearLayout) v.findViewById(R.id.jobs_top);
        LinearLayout for_sale = (LinearLayout) v.findViewById(R.id.forsale_top);
        buisness = (LinearLayout) v.findViewById(R.id.buisness_top);
        personel = (LinearLayout) v.findViewById(R.id.personal_top);
        community = (LinearLayout) v.findViewById(R.id.community_top);
        showcase = (LinearLayout) v.findViewById(R.id.showcase_top);

        cat_sub_list_view = (ListView) v.findViewById(R.id.cat_sub_list_view);
        cancel_button = (Button) v.findViewById(R.id.cancel_button);
        confirm_btn = (Button) v.findViewById(R.id.confirm_btn);
        cancel_btn = (TextView) v.findViewById(R.id.cancel_btnIV);
        cameraIV = (TextView) v.findViewById(R.id.cameraIV);
        galleryIV = (TextView) v.findViewById(R.id.gallerIV);
        TextView addimageHEaderTV = (TextView) v.findViewById(R.id.addimageheader);
        currentLocation = (TextView) v.findViewById(R.id.currentLocation);
        priceTextView = (TextView) v.findViewById(R.id.priceTextView);
        snackbarPosition = (RelativeLayout) v.findViewById(R.id.snackbarPosition);
        cat_TextView = (TextView) v.findViewById(R.id.cat_TextView);
        mostRecentTextView = (TextView) v.findViewById(R.id.mostRecentTextView);
        Button forsaleTV = (Button) v.findViewById(R.id.forsaleTV);
        searchbox = (EditText) v.findViewById(R.id.msearch);


        progressbar.setVisibility(View.GONE);
        postImageLay.setVisibility(View.GONE);

        for_sale.getParent().requestChildFocus(for_sale, for_sale);
        recyclerViewItem.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

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
        jobs.setBackgroundResource(R.color.trans);
        showcase.setBackgroundResource(R.color.trans);
        buisness.setBackgroundResource(R.color.trans);
        property_rentalsale.setBackgroundResource(R.color.trans);
        property_rental.setBackgroundResource(R.color.trans);
        personel.setBackgroundResource(R.color.trans);
        community.setBackgroundResource(R.color.trans);
        saveData(getActivity().getApplicationContext(), "MainCatType", "104");
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void addClickListner(View view) {

        jobs.setOnClickListener(this);
        buisness.setOnClickListener(this);
        property_rental.setOnClickListener(this);
        property_rentalsale.setOnClickListener(this);
        showcase.setOnClickListener(this);
        personel.setOnClickListener(this);
        community.setOnClickListener(this);
        Post_camera_icon.setOnClickListener(this);

        currentLocation.setOnClickListener(this);
        priceTextView.setOnClickListener(this);
        cat_TextView.setOnClickListener(this);
        mostRecentTextView.setOnClickListener(this);


        searchbox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search_key = searchbox.getText().toString();
                    loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
                    hideKeyboard(getActivity());
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Post_camera_icon:
                catlog.setVisibility(View.GONE);
                String username = getData(getActivity().getApplicationContext(), "user_name", "");
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
                Toast.makeText(getActivity().getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


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
//                Toast.makeText(getActivity().getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


                bundle.putString("MainCatType", "102");
                Property_Rental_Fragment jobsMAin = new Property_Rental_Fragment();
                jobsMAin.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobsMAin).addToBackStack(null).commit();
                break;


            case R.id.jobs_top:
                catlog.setVisibility(View.GONE);
//                Toast.makeText(getActivity().getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

                bundle.putString("MainCatType", "103");
                JobsMainFragment forsale_main = new JobsMainFragment();
                forsale_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, forsale_main).addToBackStack(null).commit();

                break;


            case R.id.showcase_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "105");
//                ShowCaseFragment showcase_top = new ShowCaseFragment();
//                showcase_top.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, showcase_top).addToBackStack(null).commit();

                break;

            case R.id.community_top:
                catlog.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


//                bundle.putString("MainCatType", "106");
//                CommunityFragment communityFragment = new CommunityFragment();
//                communityFragment.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, communityFragment).addToBackStack(null).commit();

                break;

            case R.id.property_rentalsale_top:
                catlog.setVisibility(View.GONE);

                bundle.putString("MainCatType", "272");
                Property_Sale_Fragment property_sale = new Property_Sale_Fragment();
                property_sale.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, property_sale).addToBackStack(null).commit();

                break;


        }
    }

    private void ShowPriceSnack() {

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        LayoutInflater objLayoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View view = objLayoutInflater.inflate(R.layout.snackbar_price_layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        Button cancel_button = (Button) view.findViewById(R.id.cancel_button);
        Button conformButton = (Button) view.findViewById(R.id.conformButton);

        // Setup the new range seek bar
        final RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(getActivity().getBaseContext());
        // Set the range
        rangeSeekBar.setRangeValues(1, slider_max_price);
        rangeSeekBar.setSelectedMinValue(rangeSeekBar.getSelectedMinValue());
        rangeSeekBar.setSelectedMaxValue(rangeSeekBar.getSelectedMaxValue());


        // Add to layout
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        conformButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price_min = String.valueOf(rangeSeekBar.getSelectedMinValue());
                price_max = String.valueOf(rangeSeekBar.getSelectedMaxValue());
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

        NetworkClass.getListData("104", arrayList, getActivity().getApplicationContext());
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
//        final ProgressDialog ringProgressDialog;

//        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", false);
//        ringProgressDialog.setCancelable(false);
        System.out.println("************* requested params ****************");
        System.err.println(country);
        System.err.println(search_key);
        System.err.println(city);
        System.err.println(type);
        System.err.println(sortby);
        System.err.println(pcat_id);
        System.err.println("cat_id" + cat_id);
        System.err.println(price_min);
        System.err.println(price_max);
        System.err.println(latitude);
        System.err.println(longitude);

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
                            JSONArray posts = response.getJSONArray("result");

                            String range_max = response.getString("max_price");

                            System.out.println(range_max);
                            double abc =0.00;
                            if (!isAlpha(range_max))
                                 abc = Double.parseDouble(range_max);

                            slider_max_price = (int) abc;

                            System.out.println(posts);
                            if (posts.length() > 0) {
                                recyclerViewItem.setVisibility(View.VISIBLE);
                                noPostTv.setVisibility(View.GONE);
                                itemlistAdapter = new ForSaleAdapter(getActivity(), posts, "sale");
                                recyclerViewItem.setAdapter(itemlistAdapter);
                                System.gc();
                                itemlistAdapter.notifyDataSetChanged();
                            } else {
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
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
            }
        });

    }

    private void ShowSnakforCurrent() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            progressbar.setVisibility(View.VISIBLE);
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Addpost(View v) {

        resetData(getActivity().getApplicationContext());
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
                LatLng location = place.getLatLng();

                latitude = String.valueOf(location.latitude);
                longitude = String.valueOf(location.longitude);
                fromLocation = true;

                String toastMsg = String.format("Place: %s", place.getName());
                String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                currentLocation.setText("  " + new_location);
//                saveData(getApplicationContext(), "currentLocation", new_location);
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
                fromLocation = false;
            }
        }

        if (requestCode == 01) {
            if (resultCode == -1) {

                ArrayList<String> imageArray = new ArrayList<>();
                System.out.println("********** image uri ****");
                System.out.println(imageUri);
                imageArray.add(getRealPathFromURI(imageUri, getActivity().getApplicationContext()));
                saveData(getActivity().getApplicationContext(), "item_img0", getRealPathFromURI(imageUri, getActivity().getApplicationContext()));

                nextFragment(imageArray, str_image_arr);
            }
        }

        if (requestCode == 17) {
            if (resultCode == 1) {
                saveData(getActivity().getApplicationContext(), "language", "select");
                saveData(getActivity().getApplicationContext(), "first_entry", "true");
                saveData(getActivity().getApplicationContext(), "first_entry_contact", "");
                saveData(getActivity().getApplicationContext(), "first_entry_cat", "true");

                ArrayList<String> responseArray = new ArrayList<>();
                ArrayList<String> newimageArray = new ArrayList<>();
                responseArray = data.getStringArrayListExtra("MESSAGE");
                if (responseArray.size() > 4) {
                    Toast.makeText(getActivity().getApplicationContext(), "Maximum 4 pics allowed", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < responseArray.size(); i++) {
                        Uri uri = Uri.fromFile(new File(responseArray.get(i)));

                        Log.e("Uri" + i, uri.toString());
                        saveData(getActivity().getApplicationContext(), "item_img" + i, uri.getPath());
                        newimageArray.add(uri.getPath());
                        str_image_arr = new String[]{uri.toString()};
                    }
                    nextFragment(newimageArray, str_image_arr);

                }
            }
        }
    }

    public void nextFragment(ArrayList<String> newimageArray, String[] str_image_arr) {
        saveData(getActivity().getApplicationContext(), "language", "select");
        saveData(getActivity().getApplicationContext(), "first_entry", "true");
        saveData(getActivity().getApplicationContext(), "first_entry_contact", "");
        saveData(getActivity().getApplicationContext(), "first_entry_cat", "true");

        Bundle bundle = new Bundle();
        bundle.putStringArray("imagess", str_image_arr);
        bundle.putStringArrayList("imageSet", newimageArray);
        bundle.putString("MainCatType", "104");
        startActivity(new Intent(getActivity().getApplicationContext(), Postyour2Add.class).putExtras(bundle));
    }

    @Override
    public void onResume() {
        super.onResume();
        progressbar.setVisibility(View.GONE);
        if (isNetworkAvailable(getActivity().getApplicationContext()))

            loadPost(user_id, search_key, "", "", type, sortby, "104", csv, price_max, price_min, latitude, longitude);
        else
            Toast.makeText(getActivity().getApplicationContext(), "No Intenet Connection", Toast.LENGTH_LONG).show();
    }
}
