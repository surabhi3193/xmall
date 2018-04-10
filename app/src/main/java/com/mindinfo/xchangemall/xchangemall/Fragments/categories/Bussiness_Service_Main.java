package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MultiPhotoSelectActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.CategoryAdapter;
import com.mindinfo.xchangemall.xchangemall.adapter.ForJobAdapter;
import com.mindinfo.xchangemall.xchangemall.adapter.ForSaleAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.ItemsMain;
import com.mindinfo.xchangemall.xchangemall.beans.categories;
import com.mindinfo.xchangemall.xchangemall.other.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenWarning;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getListData;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getRealPathFromURI;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class Bussiness_Service_Main extends Fragment implements View.OnClickListener {

    private static final int PLACE_PICKER_REQUEST = 23;
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 22;
    private TextView noPostTv;
    private EditText searchbox;

    private LinearLayout postImageLay;
    private FragmentManager fm;
    private Uri imageUri;
    private RelativeLayout category_lay;
    private Typeface face;
    private String search_key = "", cat_id = "", sortby = "newfirst", type = "search", price_min = "", price_max = "",
            country = "", city = "", latitude = "", longitude = "", pcat_id = "101";
    private TextView cancel_btn;
    private TextView cameraIV;
    private TextView galleryIV;

    private Bundle bundle;
    private RecyclerView recyclerViewItem;
    private ForJobAdapter itemlistAdapter;
    private LinearLayout Post_camera_icon;
    private LinearLayout property_rental, property_rentalsale, jobs, for_sale, buisness, personel, community, showcase;

    private TextView currentLocation, priceTextView, cat_TextView, mostRecentTextView;

    private PullRefreshLayout refreshLayout;
    private String user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_main, null);
        bundle = new Bundle();
        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        fm = getActivity().getSupportFragmentManager();

        user_id = getData(getActivity(),"user_id","");

        findbyview(v);
        addClickListner(v);
        postImageLay.setVisibility(View.GONE);
        category_lay.setVisibility(View.GONE);
        Post_camera_icon.setOnClickListener(this);
        for_sale.setBackgroundResource(R.color.trans);
        showcase.setBackgroundResource(R.color.trans);
        jobs.setBackgroundResource(R.color.trans);
        property_rentalsale.setBackgroundResource(R.color.trans);
        property_rental.setBackgroundResource(R.color.trans);
        personel.setBackgroundResource(R.color.trans);
        community.setBackgroundResource(R.color.trans);

        TextView type_businesss=v.findViewById(R.id.type_businesss);
        type_businesss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBusinessTypes();
            }
        });


        bundle = getArguments();
        if (bundle != null) {
            pcat_id = "101";
            cat_id = bundle.getString("subCatID", "");

            if (isNetworkAvailable(getActivity().getApplicationContext()))
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);
            else
                Toast.makeText(getActivity().getApplicationContext(), "No Intenet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (isNetworkAvailable(getActivity().getApplicationContext()))
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);
            else
                Toast.makeText(getActivity().getApplicationContext(), "No Intenet Connection", Toast.LENGTH_LONG).show();
        }



        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        currentLocation.setText("Current location");

                        if (idarray.size()>0)
                            idarray.clear();


                        loadPost(user_id, "", country, city, type, "newFirst", "101", "", "", "", "", "");

                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });




        return v;

    }

    private void openBusinessTypes() {

        final String[] buss_type = {"Best Match", "Highest Rated", "Most Reviews"};
        final boolean[] checkedItems = {false, false, false};


        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(buss_type, 0, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                       String jobtype = buss_type[selectedPosition];

                        System.out.println("selected option *********");
                        System.out.println(jobtype);

                        if (jobtype.equalsIgnoreCase(""))
                            jobtype = "2";
                        else if (jobtype.equalsIgnoreCase(""))
                            jobtype = "1";
                        else
                            jobtype = "";

                        dialog.dismiss();
//                        loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);

                    }
                })
                .show();

    }


    ///find item
    private void findbyview(View v) {
        recyclerViewItem = v.findViewById(R.id.recyclerViewItem);
        noPostTv =  v.findViewById(R.id.noPostTv);
        refreshLayout = (PullRefreshLayout) v.findViewById(R.id.refreshLay);

        //Post_camera_icon find
        RelativeLayout snackbarPosition = v.findViewById(R.id.snackbarPosition);
        Button cancel_button = v.findViewById(R.id.cancel_button);
        Button confirm_btn = v.findViewById(R.id.confirm_btn);

        Post_camera_icon =  v.findViewById(R.id.Post_camera_icon);
        postImageLay =  v.findViewById(R.id.postImageLay);
        property_rental =  v.findViewById(R.id.property_rental_top);
        property_rentalsale =  v.findViewById(R.id.property_rentalsale_top);
        jobs =  v.findViewById(R.id.jobs_top);
        for_sale =  v.findViewById(R.id.forsale_top);
        buisness =  v.findViewById(R.id.buisness_top);
        personel =  v.findViewById(R.id.personal_top);
        community =  v.findViewById(R.id.community_top);
        showcase =  v.findViewById(R.id.showcase_top);
        category_lay =  v.findViewById(R.id.categorylay);
        cancel_btn =  v.findViewById(R.id.cancel_btnIV);
        cameraIV =  v.findViewById(R.id.cameraIV);
        galleryIV =  v.findViewById(R.id.gallerIV);
        TextView addimageHEaderTV = v.findViewById(R.id.addimageheader);
        currentLocation =  v.findViewById(R.id.currentLocation);
        priceTextView =  v.findViewById(R.id.priceTextView);

        cat_TextView =  v.findViewById(R.id.cat_TextView);
        mostRecentTextView =  v.findViewById(R.id.type_businesss);
        searchbox = v.findViewById(R.id.msearch);


        currentLocation.setTypeface(face);
        priceTextView.setTypeface(face);
        cat_TextView.setTypeface(face);
        mostRecentTextView.setTypeface(face);
        searchbox.setTypeface(face);
        cameraIV.setTypeface(face);
        galleryIV.setTypeface(face);
        addimageHEaderTV.setTypeface(face);
        saveData(getActivity().getApplicationContext(), "MainCatType", "101");
    }

    //add Click on Iteh()
    private void addClickListner(View view) {

        priceTextView.setOnClickListener(this);
        cat_TextView.setOnClickListener(this);
        for_sale.setOnClickListener(this);
        jobs.setOnClickListener(this);
        property_rental.setOnClickListener(this);
        property_rentalsale.setOnClickListener(this);
        personel.setOnClickListener(this);
        showcase.setOnClickListener(this);
        community.setOnClickListener(this);

        currentLocation.setOnClickListener(this);

        searchbox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search_key = searchbox.getText().toString();
                    loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);
                    return true;
                }
                return false;
            }

        });
    }

    private void AddJob() {
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

                // Create parameters for Intent with filename

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

//                startCameraActivity();
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity().getBaseContext(), MultiPhotoSelectActivity.class), 0X11);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Post_camera_icon:
                String username = getData(getActivity().getApplicationContext(),
                        "user_name", "");
                if (username.length() > 2) {
                    AddJob();
                } else
                    OpenWarning(getActivity());
                break;
            case R.id.currentLocation:
                ShowSnakforCurrent();
                break;
            case R.id.priceTextView:
                ShowPriceSnack();
                break;
            case R.id.cat_TextView:
                ShowCategeriesSnak();
                break;

            case R.id.mostRecentTextView:
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);
                break;

            case R.id.personal_top:

//                bundle.putString("MainCatType", "100");
//                SocialFragment personal_main = new SocialFragment();
//                personal_main.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, personal_main).addToBackStack(null).commit();

                break;

            case R.id.property_rental_top:
                bundle.putString("MainCatType", "102");
                Property_Rental_Fragment business_main = new Property_Rental_Fragment();
                business_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, business_main).addToBackStack(null).commit();

                break;

            case R.id.jobs_top:
                bundle.putString("MainCatType", "103");
                JobsMainFragment jobsMAin = new JobsMainFragment();
                jobsMAin.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobsMAin).addToBackStack(null).commit();
                break;

            case R.id.forsale_top:
                bundle.putString("MainCatType", "104");
                ItemMainFragment forsale_main = new ItemMainFragment();
                forsale_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, forsale_main).addToBackStack(null).commit();

                break;

            case R.id.showcase_top:

//                bundle.putString("MainCatType", "105");
//                ShowCaseFragment showcase_top = new ShowCaseFragment();
//                showcase_top.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, showcase_top).addToBackStack(null).commit();

                break;

            case R.id.community_top:

//                bundle.putString("MainCatType", "106");
//                CommunityFragment communityFragment = new CommunityFragment();
//                communityFragment.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, communityFragment).addToBackStack(null).commit();

                break;


            case R.id.property_rentalsale_top:
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

        Button cancel_button = view.findViewById(R.id.cancel_button);
        Button conformButton = view.findViewById(R.id.conformButton);
//        final AppCompatSeekBar SeekBarPrice = (AppCompatSeekBar) view.findViewById(R.id.SeekBarPrice);
//        final TextView textStartProgress =  view.findViewById(R.id.textStartProgress);
//        TextView TextReset =  view.findViewById(R.id.TextReset);

        // Setup the new range seek bar
        final RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(getActivity().getBaseContext());
        // Set the range
        rangeSeekBar.setRangeValues(0, 4000);
        rangeSeekBar.setSelectedMinValue(rangeSeekBar.getSelectedMinValue());
        rangeSeekBar.setSelectedMaxValue(rangeSeekBar.getSelectedMaxValue());


        // Add to layout
        LinearLayout layout =  view.findViewById(R.id.seekbar_placeholder);
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
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);

                price_min = "";
                price_max = "";
            }
        });
        dialog.show();

    }

    private void ShowCategeriesSnak() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        LayoutInflater objLayoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View view = objLayoutInflater.inflate(R.layout.snackbar_cat_layout, null);
        dialog.setContentView(view);
        ArrayList<categories> arrayList = new ArrayList<>();


        final ListView cat_sub_list_view = view.findViewById(R.id.cat_sub_list_view);
        TextView title_cat =  view.findViewById(R.id.title_cat);
        title_cat.setText(getResources().getString(R.string.services));

        title_cat.setTypeface(face);

        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        CategoryAdapter postAdapter = new CategoryAdapter(arrayList, getActivity());

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(XchangemallApplication.getInstance().getApplicationContext());
//        cat_sub_list_view.setLayoutManager(mLayoutManager);
//        cat_sub_list_view.setItemAnimator(new DefaultItemAnimator());

        cat_sub_list_view.setAdapter(postAdapter);
        getListData("101", arrayList, getActivity().getApplicationContext());
        postAdapter.notifyDataSetChanged();

        cat_sub_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cat_sub_list_view.setItemChecked(i, true);
            }
        });

        Button cancel_button = view.findViewById(R.id.cancel_button);
        Button confirm_btn = view.findViewById(R.id.confirm_btn);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categories = getData(getActivity().getApplicationContext(), "cat_id", "");

                dialog.dismiss();
                cat_id = categories;
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);
                cat_id = "";

            }
        });

    }



    private void loadPost(String user_id,String search_key, String country, String city, String type,
                          String sortby, String pcat_id, String cat_id, String price_max,
                          String price_min, String latitude, String longitude) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", false);
        ringProgressDialog.setCancelable(false);
        System.out.println("************* requested params ****************");
        System.err.println(country);
        System.err.println(search_key);
        System.err.println(city);
        System.err.println(type);
        System.err.println(sortby);
        System.err.println(pcat_id);
        System.err.println("cat_id" + cat_id);
        System.err.println(price_max);
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

                ringProgressDialog.dismiss();
                try {
                    String status = response.getString("status");
                    switch (status) {
                        case "0":
                            Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();
                            break;
                        case "1":
                            JSONArray posts = response.getJSONArray("result");

                            System.out.println(posts);
                            if (posts.length() > 0)
                            {
                                recyclerViewItem.setVisibility(View.VISIBLE);
                                noPostTv.setVisibility(View.GONE);

                                itemlistAdapter = new ForJobAdapter(getActivity(), posts,
                                        "job");

                                recyclerViewItem.setAdapter(itemlistAdapter);



                                LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerViewItem.setLayoutManager(llm);
                                itemlistAdapter = new ForJobAdapter(getActivity(), posts, "business");
                                recyclerViewItem.setAdapter(itemlistAdapter);
                                System.gc();
                                itemlistAdapter.notifyDataSetChanged();
                            } else {
                                recyclerViewItem.setVisibility(View.GONE);
                                noPostTv.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ringProgressDialog.dismiss();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });

    }


    private void ShowSnakforCurrent() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        postImageLay.setVisibility(View.GONE);

        if (requestCode == CAPTURE_IMAGES_FROM_CAMERA) {
//            exitingCamera();
        }

        if (requestCode == PLACE_PICKER_REQUEST)
        {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, getActivity());
                LatLng location = place.getLatLng();
                latitude = String.valueOf(location.latitude);
                longitude = String.valueOf(location.longitude);
                String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                currentLocation.setText("  " + new_location);
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);
                latitude = "";
                longitude = "";
            }
        }


        if (requestCode == 01) {
            if (resultCode == -1) {
                ArrayList<String> imageArray = new ArrayList<>();
                System.out.println("********** image uri ****");
                System.out.println(imageUri);

                imageArray.add(getRealPathFromURI(imageUri, getActivity().getApplicationContext()));
                saveData(getActivity().getApplicationContext(), "item_img0", getRealPathFromURI(imageUri, getActivity().getApplicationContext()));

                nextFragment(imageArray);

            }
        }

        if (requestCode == 17) {
            if (resultCode == 1) {
                String str_image_arr[] = null;
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
                        //Log.e("Path"+i,path);
                        str_image_arr = new String[]{uri.toString()};
                        //AIzaSyDn243JOuaMA4Sx9uMHf1DFXMPSYQECZ0I
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("imagess", str_image_arr);
                    bundle.putStringArrayList("imageSet", newimageArray);
                    bundle.putString("MainCatType", "101");

                    startActivity(new Intent(getActivity().getApplicationContext(), Postyour2Add.class).putExtras(bundle));


                }
            }
        }
    }

    public void nextFragment(ArrayList<String> newimageArray) {
        saveData(getActivity().getApplicationContext(), "language", "select");
        saveData(getActivity().getApplicationContext(), "first_entry", "true");
        saveData(getActivity().getApplicationContext(), "first_entry_contact", "");
        saveData(getActivity().getApplicationContext(), "first_entry_cat", "true");

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imageSet", newimageArray);
        bundle.putString("MainCatType", "101");
        startActivity(new Intent(getActivity().getApplicationContext(), Postyour2Add.class).putExtras(bundle));
    }
}