package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
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
import com.mindinfo.xchangemall.xchangemall.activities.main.MultiPhotoSelectActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY;
import com.mindinfo.xchangemall.xchangemall.adapter.gridAdapter;
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

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenMainActivity;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenWarning;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getRealPathFromURI;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.resetData;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.iscatChecked;
import static com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity.isdogChecked;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

/**
 * Created by Mind Info- Android on 17-Nov-17.
 */

public class Property_Rental_Fragment extends Fragment implements View.OnClickListener {

    private static final int PLACE_PICKER_REQUEST = 23;
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 22;
    public String str_image_arr[];
    EditText searchbox;
    ListView cat_sub_list_view;
    LinearLayout postImageLay;
    FragmentManager fm;
    Button cancel_button, confirm_btn;
    RelativeLayout snackbarPosition;
    RelativeLayout catlog;
    Typeface face;
    String search_key = "", cat_id = "", sortby = "", type = "search", price_min = "", price_max = "",
            country = "", city = "", latitude = "", longitude = "", pcat_id = "102", user_id = "",csv="";
    TextView cancel_btn, cameraIV, galleryIV, addimageHEaderTV;
    String lati = "0";
    String longi = "0";
    boolean isChanged = false;
    Button rental_tbn;
    Bundle bundle;
    TextView title_cat;
    private PullRefreshLayout refreshLayout;
    private TextView noPostTv;
    private GridView recyclerViewItem;
    private gridAdapter itemlistAdapter;
    private List<ItemsMain> itemList;
    private LinearLayout Post_camera_icon;
    private LinearLayout property_rental, property_rentalsale, jobs, for_sale, buisness, personel, community, showcase;
    private NestedScrollView nestedrecycler;
    private TextView currentLocation, priceTextView, cat_TextView, mostRecentTextView;
    private int image_count_before;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontal_bar;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_property_rental_main, null);

        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        fm = getActivity().getSupportFragmentManager();
        user_id = getData(getActivity().getApplicationContext(), "user_id", "");

        findbyview(v);
        isdogChecked = false;
        iscatChecked = false;

        postImageLay.setVisibility(View.GONE);

        addClickListner(v);


        bundle = getArguments();
        if (isNetworkAvailable(getActivity().getApplicationContext())) {
            if (bundle != null) {
                if (idarray.size()>0)
                    idarray.clear();

                cat_id = bundle.getString("subCatID", "");
                csv=cat_id;
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv, price_max, price_min, latitude, longitude);
            }
            else {
                if (idarray.size()>0)
                    idarray.clear();

                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv,
                        price_max, price_min, latitude, longitude);

            }
        }
        else
        Toast.makeText(getActivity().getApplicationContext(), "No Intenet Connection", Toast.LENGTH_LONG).show();


        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        search_key = ""; csv = ""; sortby = ""; type = "search"; price_min = ""; price_max = "";
                        latitude = ""; longitude = ""; pcat_id = "102";
                        currentLocation.setText("Current location");

                        if (idarray.size()>0)
                            idarray.clear();

                        loadPost(user_id, search_key, country, city, type, sortby, pcat_id,
                                csv, price_max, price_min, latitude, longitude);

                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


        return v;
    }


    ///find item
    private void findbyview(View v) {
        refreshLayout = (PullRefreshLayout) v.findViewById(R.id.refreshLay);

        horizontalScrollView = (HorizontalScrollView) v.findViewById(R.id.mainHeader);

        recyclerViewItem = (GridView) v.findViewById(R.id.recyclerViewItem);
        noPostTv = (TextView) v.findViewById(R.id.noPostTv);

        title_cat = (TextView) v.findViewById(R.id.title_cat);
        Post_camera_icon = (LinearLayout) v.findViewById(R.id.Post_camera_icon);
        postImageLay = (LinearLayout) v.findViewById(R.id.postImageLay);
        property_rental = (LinearLayout) v.findViewById(R.id.property_rental_top);
        property_rentalsale = (LinearLayout) v.findViewById(R.id.property_rentalsale_top);
        jobs = (LinearLayout) v.findViewById(R.id.jobs_top);
        for_sale = (LinearLayout) v.findViewById(R.id.forsale_top);
        buisness = (LinearLayout) v.findViewById(R.id.buisness_top);
        personel = (LinearLayout) v.findViewById(R.id.personal_top);
        community = (LinearLayout) v.findViewById(R.id.community_top);
        showcase = (LinearLayout) v.findViewById(R.id.showcase_top);
        horizontal_bar = (LinearLayout) v.findViewById(R.id.horizontal_bar);
        catlog = (RelativeLayout) v.findViewById(R.id.catlog);
        cat_sub_list_view = (ListView) v.findViewById(R.id.cat_sub_list_view);
        cancel_button = (Button) v.findViewById(R.id.cancel_button);
        confirm_btn = (Button) v.findViewById(R.id.confirm_btn);
        cancel_btn = (TextView) v.findViewById(R.id.cancel_btnIV);
        cameraIV = (TextView) v.findViewById(R.id.cameraIV);
        galleryIV = (TextView) v.findViewById(R.id.gallerIV);
        addimageHEaderTV = (TextView) v.findViewById(R.id.addimageheader);
        currentLocation = (TextView) v.findViewById(R.id.currentLocation);
        priceTextView = (TextView) v.findViewById(R.id.priceTextView);
        snackbarPosition = (RelativeLayout) v.findViewById(R.id.snackbarPosition);
        cat_TextView = (TextView) v.findViewById(R.id.cat_TextView);
        mostRecentTextView = (TextView) v.findViewById(R.id.mostRecentTextView);
        searchbox = (EditText) v.findViewById(R.id.msearch);
        rental_tbn = (Button) v.findViewById(R.id.propertysaleTV);

        property_rental.getParent().requestChildFocus(property_rental, property_rental);


        catlog.setVisibility(View.GONE);
        currentLocation.setTypeface(face);
        priceTextView.setTypeface(face);
        cat_TextView.setTypeface(face);
        mostRecentTextView.setTypeface(face);
        searchbox.setTypeface(face);
        cameraIV.setTypeface(face);
        galleryIV.setTypeface(face);
        addimageHEaderTV.setTypeface(face);

        postImageLay.setVisibility(View.GONE);

//        property_rental.setBackgroundResource(R.drawable.dark_trans_rectangle);
        rental_tbn.setBackgroundResource(R.color.trans);
        jobs.setBackgroundResource(R.color.trans);
        showcase.setBackgroundResource(R.color.trans);
        buisness.setBackgroundResource(R.color.trans);
        for_sale.setBackgroundResource(R.color.trans);
        personel.setBackgroundResource(R.color.trans);
        community.setBackgroundResource(R.color.trans);
        property_rentalsale.setBackgroundResource(R.color.trans);
        property_rental.setFocusable(true);
        property_rental.setFocusableInTouchMode(true);
        property_rentalsale.setOnClickListener(this);

        saveData(getActivity().getApplicationContext(), "MainCatType", pcat_id);

        horizontalScrollView.getFocusedChild();
        horizontalScrollView.setFocusableInTouchMode(true);
        horizontalScrollView.setFocusable(true);
        horizontalScrollView.requestChildFocus(property_rental, property_rental);


    }

    //add Click on Iteh()
    private void addClickListner(View view) {

        Post_camera_icon.setOnClickListener(this);
        community.setOnClickListener(this);
        personel.setOnClickListener(this);
        showcase.setOnClickListener(this);
        jobs.setOnClickListener(this);
        for_sale.setOnClickListener(this);
        buisness.setOnClickListener(this);
        currentLocation.setOnClickListener(this);
        priceTextView.setOnClickListener(this);
        cat_TextView.setOnClickListener(this);
        mostRecentTextView.setOnClickListener(this);

        searchbox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search_key = searchbox.getText().toString();
                    loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv,
                            price_max, price_min, latitude, longitude);
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
                catlog.setVisibility(View.GONE);
                ShowCategeriesSnak();
                break;


            case R.id.mostRecentTextView:
                catlog.setVisibility(View.GONE);
                sortby="newFirst";
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv,
                        price_max, price_min, latitude, longitude);
                break;

            case R.id.personal_top:
                catlog.setVisibility(View.GONE);

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
            case R.id.jobs_top:

                catlog.setVisibility(View.GONE);
                bundle.putString("MainCatType", "103");
                JobsMainFragment jobsMAin = new JobsMainFragment();
                jobsMAin.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, jobsMAin).addToBackStack(null).commit();

                break;

            case R.id.forsale_top:
                catlog.setVisibility(View.GONE);
                bundle.putString("MainCatType", "104");
                ItemMainFragment forsale_main = new ItemMainFragment();
                forsale_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, forsale_main).addToBackStack(null).commit();
                break;

            case R.id.showcase_top:
                catlog.setVisibility(View.GONE);
//                bundle.putString("MainCatType", "105");
//                ShowCaseFragment showcase_top = new ShowCaseFragment();
//                showcase_top.setArguments(bundle);
//                fm = getFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, showcase_top).addToBackStack(null).commit();
                break;

            case R.id.community_top:
                catlog.setVisibility(View.GONE);

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
        price_min = "";
        price_max = "";
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        LayoutInflater objLayoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View view = objLayoutInflater.inflate(R.layout.snackbar_price_layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        Button cancel_button = (Button) view.findViewById(R.id.cancel_button);
        Button conformButton = (Button) view.findViewById(R.id.conformButton);

        final RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(getActivity().getBaseContext());
        // Set the range
        rangeSeekBar.setRangeValues(1, 400000);
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

                price_min = "";
                price_max = "";

                price_min = String.valueOf(rangeSeekBar.getSelectedMinValue());
                price_max = String.valueOf(rangeSeekBar.getSelectedMaxValue());
                dialog.dismiss();
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv,
                        price_max, price_min, latitude, longitude);


            }
        });
        dialog.show();

    }

    private void ShowCategeriesSnak() {
        if (idarray.size() > 0)
            idarray.clear();


        catlog.setVisibility(View.VISIBLE);
        final ArrayList<categories> arrayList = new ArrayList<>();

        title_cat.setText(getResources().getString(R.string.property_rentals));
        title_cat.setTypeface(face);
        MULTIPLEsELECTIONcATEGORY postadapter = new MULTIPLEsELECTIONcATEGORY(arrayList, getActivity());
        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cat_sub_list_view.setAdapter(postadapter);

        NetworkClass.getListData("102", arrayList, getActivity().getApplicationContext());
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

                String csv = idarray.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, csv,
                        price_max, price_min, latitude, longitude);


            }
        });

    }


    private void loadPost(String user_id, String search_key, String country, String city, String type,
                          String sortby,
                          String pcat_id, String cat_id, String price_max, String price_min, String latitude, String longitude) {
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

                System.out.println(response);
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
                            if (posts.length() > 0) {
                                recyclerViewItem.setVisibility(View.VISIBLE);
                                noPostTv.setVisibility(View.GONE);
                                itemList = new ArrayList<>();
                                JSONObject jsonObject1;

                                for (int i = 0; i < posts.length(); i++)
                                //   for(int i = 0; i<1;i++)
                                {
                                    jsonObject1 = posts.getJSONObject(i);
                                    String id = jsonObject1.getString("id");

                                    String price = jsonObject1.getString("price");
                                    lati = (jsonObject1.getString("latitude"));
                                    longi = (jsonObject1.getString("longitude"));

                                    // Toast.makeText(getActivity(), imagearr.get(0)+"\n"+price+"\n"+etitle, Toast.LENGTH_SHORT).show();
                                    //itemList.add(i,new ItemsMain("https://graph.facebook.com/1441489612612648/picture?height=100&width=100&migration_overrides=%7Boctober_2012%3Atrue%7D","1","2","subtitle","item review"));
                                    itemlistAdapter = new gridAdapter(getActivity(), posts, "rental");
                                    recyclerViewItem.setAdapter(itemlistAdapter);
                                }
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

    public void Addpost(View v) {
        resetData(getActivity().getApplicationContext());
        postImageLay.setVisibility(View.VISIBLE);
        isdogChecked = false;
        iscatChecked = false;

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImageLay.setVisibility(View.GONE);
            }
        });
        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startCameraActivity();

                System.out.println("*** click on property rental ******");



                String fileName = "Camera_Example.jpg";


                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.TITLE, fileName);

                values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");

                // imageUri is the current activity attribute, define and save it for later usage

                imageUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/


                // Standard Intent action that can be sent to have the camera
                // application capture an image and return it.

                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                startActivityForResult( intent, 01);



//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 01);
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity().getBaseContext(), MultiPhotoSelectActivity.class), 0X11);
            }
        });
    }


    public void startCameraActivity() {
        Cursor cursor = loadCursor();
        image_count_before = cursor.getCount();
        cursor.close();
        Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        List<ResolveInfo> activities = getActivity().getApplicationContext().getPackageManager().queryIntentActivities(cameraIntent, 0);

        if (activities.size() > 0)
            startActivityForResult(cameraIntent, CAPTURE_IMAGES_FROM_CAMERA);
        else
            Toast.makeText(getActivity().getApplicationContext(), "device doesn't have any app", Toast.LENGTH_SHORT).show();
    }

    public String[] getImagePaths(Cursor cursor, int startPosition) {

        int size = cursor.getCount() - startPosition;
        if (size <= 0) return null;

        String[] paths = new String[size];
        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        for (int i = startPosition; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            paths[i - startPosition] = cursor.getString(dataColumnIndex);
        }

        return paths;
    }

    private void exitingCamera() {
        Cursor cursor = loadCursor();

        //get the paths to newly added images
        String[] paths = getImagePaths(cursor, image_count_before);

        if (paths.length > 0) {
            List<String> wordList = Arrays.asList(paths);

            for (String e : wordList) {
            }
            // process images
            process(wordList);
        }
        cursor.close();
    }

    private void process(List<String> wordList) {

        List<String> responseArray = new ArrayList<>();
        ArrayList<String> imageArray = new ArrayList<>();
        responseArray = wordList;
        for (int i = 0; i < responseArray.size(); i++) {
            Uri tempUri = Uri.fromFile(new File(responseArray.get(i)));
            imageArray.add(tempUri.toString());
            //Log.e("Path"+i,path);
            str_image_arr = new String[]{tempUri.toString()};
        }
        Bundle bundle = new Bundle();
        bundle.putStringArray("imagess", str_image_arr);
        bundle.putStringArrayList("images", imageArray);
        bundle.putString("MainCatType", pcat_id);

        startActivity(new Intent(getActivity().getApplicationContext(), Postyour2Add.class).putExtras(bundle));
    }

    public Cursor loadCursor() {

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        return getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        postImageLay.setVisibility(View.GONE);

        if (requestCode == CAPTURE_IMAGES_FROM_CAMERA) {
//            exitingCamera();
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                LatLng location = place.getLatLng();

                latitude = String.valueOf(location.latitude);
                longitude = String.valueOf(location.longitude);

                String toastMsg = String.format("Place: %s", place.getName());
                String new_location = getAddressFromLatlng(location, getActivity().getApplicationContext(), 0);
                currentLocation.setText("  " + new_location);
                loadPost(user_id, search_key, country, city, type, sortby, pcat_id, cat_id,
                        price_max, price_min, latitude, longitude);

            }
        }

        if (requestCode == 01) {

            if (resultCode== -1) {
                postImageLay.setVisibility(View.GONE);

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
                postImageLay.setVisibility(View.GONE);

                saveData(getActivity().getApplicationContext(), "first_entry_contact", "");

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
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("imagess", str_image_arr);
                    bundle.putStringArrayList("imageSet", newimageArray);
                    bundle.putString("MainCatType", "102");

                    startActivity(new Intent(getActivity().getApplicationContext(), Postyour2Add.class).putExtras(bundle));


                }
            }
        }
    }

    public void onBackPressed() {
        OpenMainActivity(getActivity());
    }


    public void nextFragment(ArrayList<String> newimageArray, String[] str_image_arr)
    {
        saveData(getActivity().getApplicationContext(), "language", "select");
        saveData(getActivity().getApplicationContext(), "first_entry", "true");
        saveData(getActivity().getApplicationContext(), "first_entry_contact", "");
        saveData(getActivity().getApplicationContext(), "first_entry_cat", "true");

        Bundle bundle = new Bundle();
        bundle.putStringArray("imagess",str_image_arr);
        bundle.putStringArrayList("imageSet", newimageArray);
        bundle.putString("MainCatType", "102");
        startActivity(new Intent(getActivity().getApplicationContext(), Postyour2Add.class).putExtras(bundle));




    }
}
