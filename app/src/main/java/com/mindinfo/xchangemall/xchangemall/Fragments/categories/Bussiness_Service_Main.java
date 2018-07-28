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

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.activities.main.MultiPhotoSelectActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.ForJobAdapter;
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
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class Bussiness_Service_Main extends Fragment implements View.OnClickListener,OnBackPressed {

    final ArrayList<categories> arrayList = new ArrayList<>();
    private  final int PLACE_PICKER_REQUEST = 23;
    private TextView noPostTv;
    private EditText searchbox;

    private LinearLayout postImageLay;
    private FragmentManager fm;
    private Uri imageUri;
    private Typeface face;
    private String search_key = "", cat_id = "", sortby = "newfirst", type = "search", price_min = "", price_max = "",
            country = "", city = "", latitude = "", longitude = "", pcat_id = "101";
    private TextView cancel_btn;
    private TextView cameraIV;
    private TextView galleryIV;
    private  final int CAPTURE_IMAGES_FROM_CAMERA = 22;
    private RelativeLayout catlog;
    private Button cancel_button;
    private Bundle bundle;
    private RecyclerView recyclerViewItem;
    private ForJobAdapter itemlistAdapter;
    private LinearLayout Post_camera_icon;
    private Button confirm_btn;

    private TextView currentLocation, priceTextView, cat_TextView, mostRecentTextView;
    private LinearLayout property_rental, property_rentalsale, jobs, for_sale, personel, community, showcase,news_top,games_top;
    private ListView cat_sub_list_view;
    private PullRefreshLayout refreshLayout;
    private String user_id;
    private TextView title_cat;
    private String csv = "";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_main, null);
        bundle = new Bundle();
        face = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()), R.font.estre);
        fm = getActivity().getSupportFragmentManager();

        user_id = getData(getActivity(),"user_id","");

        findbyview(v);
        addClickListner(v);
        postImageLay.setVisibility(View.GONE);
        catlog.setVisibility(View.GONE);
        Post_camera_icon.setOnClickListener(this);
        for_sale.setBackgroundResource(R.color.trans);
        showcase.setBackgroundResource(R.color.trans);
        jobs.setBackgroundResource(R.color.trans);
        property_rentalsale.setBackgroundResource(R.color.trans);
        games_top.setBackgroundResource(R.color.trans);
        news_top.setBackgroundResource(R.color.trans);
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
            csv = cat_id;
            if (isNetworkAvailable(getActivity()))
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            else
                Toast.makeText(getActivity(), "No Intenet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (isNetworkAvailable(getActivity()))
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            else
                Toast.makeText(getActivity(), "No Intenet Connection", Toast.LENGTH_LONG).show();
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
                        pcat_id = "101";

                        currentLocation.setText("Current location");
                        searchbox.setText("");

                        if (idarray.size()>0)
                            idarray.clear();

                        loadPost(user_id, "", country, city, type, "newFirst",
                                "101", csv, "", "", "", "");

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
        title_cat = (TextView) v.findViewById(R.id.title_cat);
        cat_sub_list_view = (ListView) v.findViewById(R.id.cat_sub_list_view);
         catlog = v.findViewById(R.id.catlog);
         cancel_button = v.findViewById(R.id.cancel_button);
         confirm_btn = v.findViewById(R.id.confirm_btn);

        Post_camera_icon =  v.findViewById(R.id.Post_camera_icon);
        postImageLay =  v.findViewById(R.id.postImageLay);
        property_rental =  v.findViewById(R.id.property_rental_top);
        property_rentalsale =  v.findViewById(R.id.property_rentalsale_top);
        games_top =  v.findViewById(R.id.games_top);
        news_top =  v.findViewById(R.id.news_top);
        jobs =  v.findViewById(R.id.jobs_top);
        for_sale =  v.findViewById(R.id.forsale_top);

        personel =  v.findViewById(R.id.personal_top);
        community =  v.findViewById(R.id.community_top);
        showcase =  v.findViewById(R.id.showcase_top);
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
        saveData(getActivity(), "MainCatType", "101");
    }

    //add Click on Iteh()
    private void addClickListner(View view) {

        priceTextView.setOnClickListener(this);
        cat_TextView.setOnClickListener(this);
        for_sale.setOnClickListener(this);
        jobs.setOnClickListener(this);
        property_rental.setOnClickListener(this);
        property_rentalsale.setOnClickListener(this);
        games_top.setOnClickListener(this);
        news_top.setOnClickListener(this);
        personel.setOnClickListener(this);
        showcase.setOnClickListener(this);
        community.setOnClickListener(this);

        currentLocation.setOnClickListener(this);

        searchbox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search_key = searchbox.getText().toString();
                    loadPost(user_id,search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
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
                String username = getData(getActivity(),
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
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        LayoutInflater objLayoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View view = objLayoutInflater.inflate(R.layout.snackbar_price_layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        Button cancel_button = view.findViewById(R.id.cancel_button);
        Button conformButton = view.findViewById(R.id.conformButton);
        EditText maxTv =view.findViewById(R.id.maxTV);
        EditText minTv =view.findViewById(R.id.minTv);



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
                if (Integer.parseInt(price_min.trim())>Integer.parseInt(price_max.trim()))
                {
                    Toast.makeText(getActivity(),"Max price should be greater then Min price ",Toast.LENGTH_LONG).show();
                    return;
                }

                dialog.dismiss();
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);

                price_min = "";
                price_max = "";
            }
        });
        dialog.show();

    }

    private void ShowCategeriesSnak() {
        catlog.setVisibility(View.VISIBLE);
        title_cat.setText(getResources().getString(R.string.services));
        title_cat.setTypeface(face);
        MULTIPLEsELECTIONcATEGORY postadapter = new MULTIPLEsELECTIONcATEGORY(arrayList, getActivity());
        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cat_sub_list_view.setAdapter(postadapter);

        NetworkClass.getListData("101", arrayList, getActivity());
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
                            recyclerViewItem.setVisibility(View.GONE);
                            noPostTv.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();
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

                                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
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
                LatLng latLng = place.getLatLng();
                latitude= String.valueOf(latLng.latitude);
                longitude= String.valueOf(latLng.longitude);
                String new_location= place.getName().toString();
                currentLocation.setText("  " + new_location);
                loadPost(user_id,search_key, country, city, type, sortby, pcat_id, cat_id, price_max, price_min, latitude, longitude);
                latitude = "";
                longitude = "";
            }
        }


        if (requestCode == 01) {
            if (resultCode == -1) {
                ArrayList<Uri> imageArray = new ArrayList<>();
                System.out.println("********** image uri ****");
                System.out.println(imageUri);

//                imageArray.add(getRealPathFromURI(imageUri, getActivity()));
                imageArray.add(imageUri);
                saveData(getActivity(), "item_img0", getRealPathFromURI(imageUri, getActivity()));

                nextFragment(imageArray);

            }
        }

        if (requestCode == 17) {
            if (resultCode == 1) {
                String str_image_arr[] = null;
                saveData(getActivity(), "language", "select");
                saveData(getActivity(), "first_entry", "true");
                saveData(getActivity(), "first_entry_contact", "");
                saveData(getActivity(), "first_entry_cat", "true");
                ArrayList<String> responseArray = new ArrayList<>();
                ArrayList<String> newimageArray = new ArrayList<>();
                ArrayList<Uri> newImageURi = new ArrayList<>();
                responseArray = data.getStringArrayListExtra("MESSAGE");
                if (responseArray.size()==0)
                {
                    Toast.makeText(getActivity(), "Select Min 1 image to proceed",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (responseArray.size() > 4) {
                    Toast.makeText(getActivity(), "Maximum 4 images allowed",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                    for (int i = 0; i < responseArray.size(); i++) {
                        Uri uri = Uri.fromFile(new File(responseArray.get(i)));

                        Log.e("Uri" + i, uri.toString());
                        saveData(getActivity(), "item_img" + i, uri.getPath());

                        newimageArray.add(uri.getPath());
                        newImageURi.add(uri);
                        //Log.e("Path"+i,path);
                        str_image_arr = new String[]{uri.toString()};
                        //AIzaSyDn243JOuaMA4Sx9uMHf1DFXMPSYQECZ0I
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("imagess", str_image_arr);
                    bundle.putParcelableArrayList("imageSet", newImageURi);
                    bundle.putString("MainCatType", "101");

                    startActivity(new Intent(getActivity(), Postyour2Add.class).putExtras(bundle));
            }
        }
    }

    public void nextFragment(ArrayList<Uri> newimageArray) {
        saveData(getActivity(), "language", "select");
        saveData(getActivity(), "first_entry", "true");
        saveData(getActivity(), "first_entry_contact", "");
        saveData(getActivity(), "first_entry_cat", "true");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("imageSet", newimageArray);
        bundle.putString("MainCatType", "101");
        startActivity(new Intent(getActivity(), Postyour2Add.class).putExtras(bundle));
    }

    @Override
    public void onBackPressed() {
            startActivity(new Intent(getActivity(),
                    MainActivity.class).putExtra("EXIT", true));
    }
}