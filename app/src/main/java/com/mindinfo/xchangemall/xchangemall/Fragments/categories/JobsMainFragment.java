package com.mindinfo.xchangemall.xchangemall.Fragments.categories;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.mindinfo.xchangemall.xchangemall.adapter.ForJobAdapter;
import com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY;
import com.mindinfo.xchangemall.xchangemall.beans.ItemsMain;
import com.mindinfo.xchangemall.xchangemall.beans.categories;
import com.mindinfo.xchangemall.xchangemall.intefaces.OnBackPressed;
import com.mindinfo.xchangemall.xchangemall.other.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenWarning;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getRealPathFromURI;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.resetData;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.adapter.MULTIPLEsELECTIONcATEGORY.idarray;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

/**
 * Created by Mind Info- Android on 17-Nov-17.
 */

public class JobsMainFragment extends Fragment implements View.OnClickListener,OnBackPressed{

    private  final int PLACE_PICKER_REQUEST = 23;
    public String str_image_arr[];
    Typeface face;
    String csv ="";
    String search_key = "", cat_id = "", sortby = "newfirst", type = "search", latitude = "", longitude = "", pcat_id = "103", jobtype = "";
    String lati = "0";
    String longi = "0";
    Bundle bundle;
    JSONArray posts;
    Uri imageUri;
    private ProgressBar progressbar;
    private TextView noPostTv;
    private EditText searchbox;
    private ListView cat_sub_list_view;
    private LinearLayout postImageLay;
    private FragmentManager fm;
    private Button cancel_button, confirm_btn;
    private RelativeLayout snackbarPosition;
    private TextView cancel_btn, cameraIV, galleryIV, addimageHEaderTV, title_cat;
    private RecyclerView recyclerViewItem;
    private ForJobAdapter itemlistAdapter;
    private List<ItemsMain> itemList;
    private LinearLayout Post_camera_icon;
    private LinearLayout property_rental;
    private LinearLayout property_rentalsale;
    private LinearLayout news_top;
    private LinearLayout for_sale;
    private LinearLayout games_top;
    private LinearLayout buisness;
    private LinearLayout personel;
    private LinearLayout community;
    private LinearLayout showcase;
    private RelativeLayout catlog;
    private TextView currentLocation, jobtypeTV, industryTV;
    private int image_count_before;
    private PullRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job_main, null);

        face = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()), R.font.estre);
        fm = getActivity().getSupportFragmentManager();
        bundle = new Bundle();
        findbyview(v);
        addClickListner(v);

        postImageLay.setVisibility(View.GONE);
        Post_camera_icon.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {


            for_sale.setOnClickListener(this);
            games_top.setOnClickListener(this);
            buisness.setOnClickListener(this);
            property_rental.setOnClickListener(this);
            property_rentalsale.setOnClickListener(this);
            news_top.setOnClickListener(this);
            buisness.setOnClickListener(this);
            personel.setOnClickListener(this);
            showcase.setOnClickListener(this);
            community.setOnClickListener(this);
            cat_id = bundle.getString("subCatID", "");
            csv=cat_id;
            if (idarray.size() > 0)
                idarray.clear();

                loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);
        }
        else
        {
            if (idarray.size() > 0)
                idarray.clear();

            loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);

        }
        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        search_key = ""; csv = ""; sortby = ""; type = "search"; jobtype = ""; latitude="";
                        longitude="";pcat_id = "103";
                        if (idarray.size()>0)
                            idarray.clear();

                        currentLocation.setText("Current location");

                        loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);

                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        return v;

    }


    private void findbyview(View v) {
        refreshLayout =v.findViewById(R.id.refreshLay);

        recyclerViewItem =v.findViewById(R.id.recyclerViewItem);
        noPostTv =v.findViewById(R.id.noPostTv);
        catlog = v.findViewById(R.id.catlog);
        //Post_camera_icon find
        Post_camera_icon =v.findViewById(R.id.Post_camera_icon);
        postImageLay =v.findViewById(R.id.postImageLay);
        property_rental =v.findViewById(R.id.property_rental_top);
        property_rentalsale =v.findViewById(R.id.property_rentalsale_top);
        news_top =v.findViewById(R.id.news_top);
        LinearLayout jobs = v.findViewById(R.id.jobs_top);
        for_sale =v.findViewById(R.id.forsale_top);
        buisness =v.findViewById(R.id.buisness_top);
        games_top =v.findViewById(R.id.games_top);
        personel =v.findViewById(R.id.personal_top);
        community =v.findViewById(R.id.community_top);
        showcase =v.findViewById(R.id.showcase_top);
        progressbar = v.findViewById(R.id.progressbar);

        cat_sub_list_view = v.findViewById(R.id.cat_sub_list_view);
        cancel_button =v.findViewById(R.id.cancel_button);
        confirm_btn =v.findViewById(R.id.confirm_btn);
        cancel_btn =v.findViewById(R.id.cancel_btnIV);
        cameraIV =v.findViewById(R.id.cameraIV);
        galleryIV =v.findViewById(R.id.gallerIV);
        addimageHEaderTV =v.findViewById(R.id.addimageheader);
        currentLocation =v.findViewById(R.id.currentLocation);
        title_cat =v.findViewById(R.id.title_cat);

        snackbarPosition = v.findViewById(R.id.snackbarPosition);
        jobtypeTV =v.findViewById(R.id.jobtypeTV);
        industryTV =v.findViewById(R.id.industryTV);
        searchbox = (EditText) v.findViewById(R.id.msearch);

        jobs.getParent().requestChildFocus(jobs, jobs);

        catlog.setVisibility(View.GONE);

        currentLocation.setTypeface(face);

        jobtypeTV.setTypeface(face);
        industryTV.setTypeface(face);
        searchbox.setTypeface(face);
        cameraIV.setTypeface(face);
        galleryIV.setTypeface(face);
        addimageHEaderTV.setTypeface(face);

        saveData(getActivity(), "MainCatType", "103");



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

    private void addClickListner(View view) {
        currentLocation.setOnClickListener(this);
        industryTV.setOnClickListener(this);
        jobtypeTV.setOnClickListener(this);


        searchbox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search_key = searchbox.getText().toString();
                    loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);
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
                String username = getData(getActivity(), "user_name", "");
                if (username.length() > 2) {
                    AddJob();
                } else
                    OpenWarning(getActivity());
                break;
            case R.id.currentLocation:
                ShowSnakforCurrent();
                break;

            case R.id.jobtypeTV:
                openJobTypes();
                break;

            case R.id.industryTV:
                ShowCategeriesSnak();
                break;

            case R.id.personal_top:
                break;

            case R.id.buisness_top:

                bundle.putString("MainCatType", "101");
                Bussiness_Service_Main business_main = new Bussiness_Service_Main();
                business_main.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.allCategeries, business_main).addToBackStack(null).commit();

                break;

            case R.id.property_rental_top:

                bundle.putString("MainCatType", "102");
                Property_Rental_Fragment jobsMAin = new Property_Rental_Fragment();
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

    private void AddJob() {
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

                // Create parameters for Intent with filename

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
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity().getBaseContext(), MultiPhotoSelectActivity.class), 0X11);
            }
        });
    }


    private void ShowCategeriesSnak() {

        catlog.setVisibility(View.VISIBLE);
        final ArrayList<categories> arrayList = new ArrayList<>();

        title_cat.setText(getResources().getString(R.string.jobs));
        title_cat.setTypeface(face);

        MULTIPLEsELECTIONcATEGORY postadapter = new MULTIPLEsELECTIONcATEGORY(arrayList, getActivity());
        cat_sub_list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        cat_sub_list_view.setAdapter(postadapter);

        NetworkClass.getListData("103", arrayList, getActivity());
        postadapter.notifyDataSetChanged();

        cat_sub_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                System.out.println(idarray);
                categories cat = arrayList.get(i);

            }
        });


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
                loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);

            }
        });

    }


    private void loadPost(String search_key, String job_type, String sortby,
                          String pcat_id,
                          String cat_id, String latitude, String longitude) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;

        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", false);
        ringProgressDialog.setCancelable(false);
        System.out.println("************* requested params ****************");
        System.err.println(search_key);
        System.err.println(sortby);
        System.err.println(pcat_id);
        System.err.println("cat_id" + cat_id);
        System.err.println(latitude);
        System.err.println(longitude);
        System.err.println(job_type);

        String user_id = getData(getActivity(), "user_id", "");

        params.put("user_id", user_id);
        params.put("val", search_key);
        params.put("type", "search");
        params.put("sortby", sortby);
        params.put("pcat_id", pcat_id);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("cat_id", cat_id);
        params.put("job_type", job_type);
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

                            break;
                        case "1":
                           JSONArray  posts = response.getJSONArray("result");

                            System.out.println("****** post response *********");
                            System.out.println(posts);
                            if (posts.length() > 0)
                            {
                                recyclerViewItem.setVisibility(View.VISIBLE);
                                noPostTv.setVisibility(View.GONE);
                                    itemlistAdapter = new ForJobAdapter(getActivity(), posts, "job");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        postImageLay.setVisibility(View.GONE);

        int CAPTURE_IMAGES_FROM_CAMERA = 22;
        if (requestCode == CAPTURE_IMAGES_FROM_CAMERA) {
//          exitingCamera();
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                progressbar.setVisibility(View.GONE);

                Place place = PlacePicker.getPlace(data, getActivity());
                LatLng latLng = place.getLatLng();
                latitude= String.valueOf(latLng.latitude);
                longitude= String.valueOf(latLng.longitude);
                String new_location = place.getName().toString();
                currentLocation.setText("  " + new_location);
                loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);

            }
        }

        if (requestCode == 01) {
            if (resultCode== -1) {
                ArrayList<String> imageArray = new ArrayList<>();
                ArrayList<Uri> uriimageArray = new ArrayList<>();
                System.out.println("********** image uri ****");
                System.out.println(imageUri);

                uriimageArray.add(imageUri);
                imageArray.add(getRealPathFromURI(imageUri, getActivity()));
                saveData(getActivity(), "item_img0", getRealPathFromURI(imageUri, getActivity()));
                nextFragment(uriimageArray, str_image_arr);
            }
        }

        if (requestCode == 17)
        {
            if (resultCode == 1) {

                saveData(getActivity(), "language", "select");
                saveData(getActivity(), "first_entry", "true");
                saveData(getActivity(), "first_entry_contact", "");
                saveData(getActivity(), "first_entry_cat", "true");
                ArrayList<String> responseArray = new ArrayList<>();
                ArrayList<String> newimageArray = new ArrayList<>();
                ArrayList<Uri> uriimageArray = new ArrayList<>();
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
                        saveData(getActivity(), "item_img" + i, uri.getPath());

                        newimageArray.add(uri.getPath());
                        uriimageArray.add(uri);
                        //Log.e("Path"+i,path);
                        str_image_arr = new String[]{uri.toString()};
                        //AIzaSyDn243JOuaMA4Sx9uMHf1DFXMPSYQECZ0I
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("imagess", str_image_arr);
                    bundle.putParcelableArrayList("imageSet", uriimageArray);
                    bundle.putString("MainCatType", "103");

                    startActivity(new Intent(getActivity(), Postyour2Add.class).putExtras(bundle));

            }
        }
    }

    public void nextFragment(ArrayList<Uri> newimageArray, String[] str_image_arr)
    {
        saveData(getActivity(), "language", "select");
        saveData(getActivity(), "first_entry", "true");
        saveData(getActivity(), "first_entry_contact", "");
        saveData(getActivity(), "first_entry_cat", "true");

        Bundle bundle = new Bundle();
        bundle.putStringArray("imagess",str_image_arr);
        bundle.putParcelableArrayList("imageSet", newimageArray);
        bundle.putString("MainCatType", "103");
        startActivity(new Intent(getActivity(), Postyour2Add.class).putExtras(bundle));
    }

    private void openJobTypes() {

        final String[] jobtypes = {"Full Time", "Part Time", "Both"};
        final boolean[] checkedItems = {false, false, false};


        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(jobtypes, 2, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        jobtype = jobtypes[selectedPosition];

                        System.out.println("selected option *********");
                        System.out.println(jobtype);

                        if (jobtype.equalsIgnoreCase("Full Time"))
                            jobtype = "2";
                        else if (jobtype.equalsIgnoreCase("Part Time"))
                            jobtype = "1";
                        else
                            jobtype = "";

                        dialog.dismiss();
                        loadPost(search_key, jobtype, sortby, pcat_id, csv, latitude, longitude);

                    }
                })
                .show();

    }
    public void onBackPressed() {
        startActivity(new Intent(getActivity(),
                MainActivity.class).putExtra("EXIT", true));

    }
}