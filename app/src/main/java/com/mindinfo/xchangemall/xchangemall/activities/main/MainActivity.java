package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Bussiness_Service_Main;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.ItemMainFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.JobsMainFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Property_Rental_Fragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Property_Sale_Fragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.common.FavoritesActivity;
import com.mindinfo.xchangemall.xchangemall.activities.common.ProfileActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.CustomList;
import com.mindinfo.xchangemall.xchangemall.adapter.ExpandableListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.OpenWarning;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.inFromLeftAnimation;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.inFromRightAnimation;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.outToLeftAnimation;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.outToRightAnimation;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.NullData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int PLACE_PICKER_REQUEST = 011;
    private static final int CAPTURE_IMAGES_FROM_CAMERA = 22;
    //expandable listview
    public static boolean ismovedfromHome;
    public static LinearLayout left_nav_view;
    public static boolean isdogChecked,iscatChecked;
    public String str_image_arr[];
    RelativeLayout right_nav_view;
    TextView category_head_list, user_nameTV, locationTextView, forsale, propertyrenttv, commtv, jobstv, showcasetv, servicetv, socialtv, prop_forsaletv;
    FragmentManager fm;
    String url = "";
    Intent i;
    ListView list;
    LinearLayout postImageLay, vdo_cl_lay, share_btn;
    TextView cancel_btn, cameraIV, galleryIV, addimageHEaderTV;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<Integer> listHeaderImage;
    String user_name, user_id, user_image;
    ImageView profile_image;
    HashMap<String, List<String>> listDataChild;
    Bundle bundle;
    Fragment fragment;
    GoogleApiClient mGoogleApiClient;
    String[] web = {
            "Profile",
            "Home",
            "Location",
            "Saved Search",
            "My Favorites",
            "My Message",
            "My Availability",
            "My Post",
            "Create Post",
            "About Us",
            "Terms and conditions",
            "Privacy Policy",
            "Rate the App",
            "Share the App",
            "Logout"
    };
    Integer[] imageId = {
            R.drawable.profile,
            R.drawable.home,
            R.drawable.location_logo,
            R.drawable.saved_search,
            R.drawable.favv,
            R.drawable.mymassage,
            R.drawable.abliable,
            R.drawable.mypost,
            R.drawable.create_post,
            R.drawable.about,
            R.drawable.treams_condition,
            R.drawable.privacty,
            R.drawable.rating,
            R.drawable.share,
            R.drawable.logout_icon

    };
    private int image_count_before;
    private LinearLayout locationLay, open_left_nav_btn, open_right_nav_btn, close_left_nav,
            forsale_lay, jobs_lay, right_nav_white, log_out, servicesImageView, houseRentalImageView, HouseSaleImageView,
            personalImageView, ShowcaseImageView, CommutnityImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        //I'm also creating a log message, which we'll look at in more detail later//
        FirebaseCrash.log("MainActivity started");
        System.out.println("************* home activity **********");
        fm = getSupportFragmentManager();
        bundle = new Bundle();
        ismovedfromHome = false;
        initid();
        clickListners();
        user_id = getData(getApplicationContext(), "user_id", "");

    }


    ////OpenProfile
    public void OpenProfile(View view) {
        if (user_name.length() < 2)
            OpenWarning(MainActivity.this);
        else {
            ismovedfromHome = true;
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);  //your class
            i.putExtra("username", user_name);
            i.putExtra("user_image", user_image);
            startActivity(i);

            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }

    }

    private void clickListners() {
        open_left_nav_btn.setOnClickListener(this);
        open_right_nav_btn.setOnClickListener(this);
        close_left_nav.setOnClickListener(this);
        right_nav_view.setOnClickListener(this);
        locationLay.setOnClickListener(this);
        share_btn.setOnClickListener(this);

        if (!ismovedfromHome) {
            forsale_lay.setOnClickListener(this);
          jobs_lay.setOnClickListener(this);
            servicesImageView.setOnClickListener(this);
            houseRentalImageView.setOnClickListener(this);
            HouseSaleImageView.setOnClickListener(this);
            personalImageView.setOnClickListener(this);
            ShowcaseImageView.setOnClickListener(this);
            CommutnityImageView.setOnClickListener(this);
        }

        log_out.setOnClickListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        if (user_name.length() > 2)
                            OpenProfile(view);
                        else
                            OpenWarning(MainActivity.this);
                        break;
                    case 1:
                        Homeclick(view);
                        break;

                    case 2:
                        Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


//                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                        try {
//                            startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        break;

                    case 3:
                        Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


//                      startActivity(new Intent(getApplicationContext(),FavoritesActivity.class));

                        break;

                    case 4:
                        Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


                        ismovedfromHome = true;
                        startActivity(new Intent(getApplicationContext(), FavoritesActivity.class)
                                .putExtra("method_name", "my_fav"));

                        break;

                    case 7:
                        Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


                        ismovedfromHome = true;
                        startActivity(new Intent(getApplicationContext(), FavoritesActivity.class)
                                .putExtra("method_name", "my_post"));

                        break;

                    case 8:
                        Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

//                        right_nav_view.setVisibility(View.GONE);
//                        if (user_name.length() > 2)
//                            Addpost(view);
//                        else
//                            OpenWarning();
                        break;


                    case 9:
                        ismovedfromHome = true;
                        i = new Intent(getApplicationContext(), HtmlActivity.class);
                        i.putExtra("header_name", "About us");
                        startActivity(i);
                        break;

                    case 10:
                        ismovedfromHome = true;
                        i = new Intent(getApplicationContext(), HtmlActivity.class);
                        i.putExtra("header_name", "Terms and Condition");
                        startActivity(i);

                        break;

                    case 11:
                        ismovedfromHome = true;
                        i = new Intent(getApplicationContext(), HtmlActivity.class);
                        i.putExtra("header_name", "Privacy Policy");
                        startActivity(i);
                        break;
                    case 13:
                        shareApplication();
                        break;

                    case 14:
                        if (web[14].equals("Login"))
                            OpenWarning(MainActivity.this);

                        else
                            LogoutAlertDialog();
                        break;
                }

            }
        });


        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

    }

    private void Addpost(View v) {

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

//                startCameraActivity();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 01);
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),
                        MultiPhotoSelectActivity.class), 0X11);
            }
        });
    }

    public void startCameraActivity() {

        Cursor cursor = loadCursor();
        image_count_before = cursor.getCount();

        cursor.close();

        Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(cameraIntent, 0);

        if (activities.size() > 0)
            startActivityForResult(cameraIntent, CAPTURE_IMAGES_FROM_CAMERA);
        else
            Toast.makeText(getApplicationContext(), "device doesn't have any app", Toast.LENGTH_SHORT).show();
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

            if (paths.length > 4) {
                Toast.makeText(getApplicationContext(), "Maximum 4 pics allowed", Toast.LENGTH_LONG).show();
            } else {

                List<String> wordList = Arrays.asList(paths);

                for (String e : wordList) {
                }
                // process images
                process(wordList);
            }
        }
        cursor.close();

    }


    private void process(List<String> wordList) {

        ismovedfromHome = true;
        postImageLay.setVisibility(View.GONE);
        List<String> responseArray = new ArrayList<>();
        ArrayList<String> imageArray = new ArrayList<>();
        responseArray = wordList;
        for (int i = 0; i < responseArray.size(); i++) {
            Uri uri = Uri.fromFile(new File(responseArray.get(i)));

//            imageArray.add(tempUri.toString());
//            //Log.e("Path"+i,path);
//            str_image_arr = new String[]{tempUri.toString()};

            saveData(getApplicationContext(), "item_img", uri.getPath());

            str_image_arr = new String[]{uri.getPath()};


        }
        postImageLay.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putStringArray("imagess", str_image_arr);
        bundle.putStringArrayList("imageSet", imageArray);
        bundle.putString("MainCatType", "104");
//                Postyour2Add postyour2Add = new Postyour2Add();
//                postyour2Add.setArguments(bundle);
//                fm = getSupportFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, postyour2Add).commit();
        startActivity(new Intent(getApplicationContext(), Postyour2Add.class).putExtras(bundle));
    }

    public Cursor loadCursor() {

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        return getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
    }


    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ", "").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        right_nav_view.setVisibility(View.GONE);
        left_nav_view.setVisibility(View.GONE);
    }

    private void initid() {
        left_nav_view = (LinearLayout) findViewById(R.id.left_nav_view);
        share_btn = (LinearLayout) findViewById(R.id.share_btn);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        user_nameTV = (TextView) findViewById(R.id.user_nameTV);
        log_out = (LinearLayout) findViewById(R.id.log_out);
        right_nav_white = (LinearLayout) findViewById(R.id.right_nav_white);
        right_nav_view = (RelativeLayout) findViewById(R.id.right_nav);
        locationLay = (LinearLayout) findViewById(R.id.location_lay);
        close_left_nav = (LinearLayout) findViewById(R.id.close_left_nav);
        open_left_nav_btn = (LinearLayout) findViewById(R.id.openLeftNav);
        open_right_nav_btn = (LinearLayout) findViewById(R.id.openRightNav);

        vdo_cl_lay = (LinearLayout) findViewById(R.id.vdo_cl);
        postImageLay = (LinearLayout) findViewById(R.id.postImageLay);
        cancel_btn = (TextView) findViewById(R.id.cancel_btnIV);
        cameraIV = (TextView) findViewById(R.id.cameraIV);
        galleryIV = (TextView) findViewById(R.id.gallerIV);
        addimageHEaderTV = (TextView) findViewById(R.id.addimageheader);
        forsale_lay = (LinearLayout) findViewById(R.id.For_sele_imageView);
        jobs_lay = (LinearLayout) findViewById(R.id.imageViewJobS);
        servicesImageView = (LinearLayout) findViewById(R.id.servicesImageView);
        houseRentalImageView = (LinearLayout) findViewById(R.id.houseRentalImageView);
        HouseSaleImageView = (LinearLayout) findViewById(R.id.HouseSaleImageView);
        personalImageView = (LinearLayout) findViewById(R.id.personalImageView);
        ShowcaseImageView = (LinearLayout) findViewById(R.id.ShowcaseImageView);
        CommutnityImageView = (LinearLayout) findViewById(R.id.community_image_lay);

        locationTextView = (TextView) findViewById(R.id.location_Textview);
        category_head_list = (TextView) findViewById(R.id.category_head_list);
        jobstv = (TextView) findViewById(R.id.jobs_text);
        forsale = (TextView) findViewById(R.id.for_sele_text);
        showcasetv = (TextView) findViewById(R.id.showcaseTextView);
        servicetv = (TextView) findViewById(R.id.services_textView);
        socialtv = (TextView) findViewById(R.id.personalTextView);
        commtv = (TextView) findViewById(R.id.comm_textView);
        prop_forsaletv = (TextView) findViewById(R.id.HouseSaleTextView);
        propertyrenttv = (TextView) findViewById(R.id.houseRentalTextView);
        ;
        expListView = (ExpandableListView) findViewById(R.id.nav_categories_lisview);
        list = (ListView) findViewById(R.id.rightnav_listview);

        postImageLay.setVisibility(View.GONE);
        left_nav_view.setVisibility(View.GONE);
        right_nav_view.setVisibility(View.GONE);

        String city = getData(getApplicationContext(), "currentLocation", "");
        user_name = getData(getApplicationContext(), "user_name", "");
        user_image = getData(getApplicationContext(), "user_profile_pic", "");


        System.out.println("=============== curreny city=============");
        System.out.println(city);

        locationTextView.setText(city);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/estre.ttf");
        locationTextView.setTypeface(face);
        jobstv.setTypeface(face);
        forsale.setTypeface(face);
        showcasetv.setTypeface(face);
        servicetv.setTypeface(face);
        socialtv.setTypeface(face);
        commtv.setTypeface(face);
        prop_forsaletv.setTypeface(face);
        propertyrenttv.setTypeface(face);

        user_nameTV.setTypeface(face);
        cameraIV.setTypeface(face);
        galleryIV.setTypeface(face);
        category_head_list.setTypeface(face);
        addimageHEaderTV.setTypeface(face);
        if (user_name.length() > 2) {
            user_nameTV.setText(user_name);

            System.out.println("************* image at main activity ********");
            System.out.println(user_image);


            if (user_image.equals(DEFAULT_PATH))
                Picasso.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

            else {
                try {
                    Picasso.with(getApplicationContext()).load(user_image).into(profile_image);
                } catch (Exception e) {
                    Picasso.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

                }
            }

            log_out.setVisibility(View.VISIBLE);
        } else {
            log_out.setVisibility(View.GONE);
            web[14] = "Login";
            user_nameTV.setText("Welcome");
        }


        CustomList adapter = new CustomList(MainActivity.this, web, imageId);
        list.setAdapter(adapter);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, listHeaderImage);

        // setting list adapter
        expListView.setAdapter(listAdapter);


    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listHeaderImage = new ArrayList<Integer>();
        listDataChild = new HashMap<String, List<String>>();

        listHeaderImage.add(R.drawable.icon_hcm);
        listHeaderImage.add(R.drawable.humanservicesicon);
        listHeaderImage.add(R.drawable.houseing);
        listHeaderImage.add(R.drawable.job);
        listHeaderImage.add(R.drawable.for_sale);
        listHeaderImage.add(R.drawable.show);
        listHeaderImage.add(R.drawable.community);
        listHeaderImage.add(R.drawable.propertyforsale_bg);


        String responseCate = getData(getApplicationContext(), "categoriesdata", "");
        try {

            System.out.println(responseCate);
            JSONArray jsonArray = new JSONArray(responseCate);


            if (jsonArray.length() > 0) {
                String cat_id = "", cat_name = "";
                JSONArray jsonArray2 = null;
                JSONArray jsonArray3 = null;


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject responseobj2 = jsonArray.getJSONObject(i);

                    Iterator x2 = responseobj2.keys();
                    jsonArray2 = new JSONArray();
                    List<String> childdata = new ArrayList<String>();
                    while (x2.hasNext()) {
                        String key = (String) x2.next();
                        jsonArray2.put(responseobj2.get(key));
                    }
                    if (jsonArray2.length() > 0) {
                        cat_id = jsonArray2.getString(0);
                        cat_name = jsonArray2.getString(1);
                        for (int j = 2; j < jsonArray.length(); j++) {
                            JSONObject responseobj3 = jsonArray.getJSONObject(i);

                            Iterator x3 = responseobj3.keys();
                            jsonArray3 = new JSONArray();
                            while (x3.hasNext()) {
                                String key = (String) x3.next();
                                jsonArray3.put(responseobj3.get(key));
                            }

                            for (int k = 0; k < jsonArray3.length(); k++) {
                            }


                        }
                    }
                    listDataHeader.add(cat_name);
                    for (int l = 2; l < jsonArray3.length(); l++) {
                        childdata.add(jsonArray3.getJSONObject(l).getString("title") + "~" + jsonArray3.getJSONObject(l).getString("id"));
                    }
                    listDataChild.put(cat_name, childdata);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Adding child data


    }


    public void Homeclick(View view) {
        if (ismovedfromHome) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            ismovedfromHome = false;
        } else {
            left_nav_view.setVisibility(View.GONE);
            ismovedfromHome = false;
        }


//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("EXIT", true);
//        startActivity(intent);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.openLeftNav:
                left_nav_view.setVisibility(View.VISIBLE);
                right_nav_view.setVisibility(View.GONE);
                left_nav_view.startAnimation(inFromLeftAnimation());
                break;

            case R.id.log_out:
                LogoutAlertDialog();
                break;

            case R.id.share_btn:
                shareApplication();
                break;

            case R.id.openRightNav:
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                right_nav_view.setBackgroundResource(R.color.dark_trans);
                right_nav_view.setVisibility(View.VISIBLE);
                left_nav_view.setVisibility(View.GONE);
                right_nav_white.startAnimation(inFromRightAnimation());

                break;
            case R.id.right_nav:
                right_nav_view.setBackgroundResource(R.color.trans);
                right_nav_view.startAnimation(outToRightAnimation());

                right_nav_view.setVisibility(View.GONE);
                right_nav_view.setVisibility(View.GONE);
                left_nav_view.setVisibility(View.GONE);
                right_nav_view.setVisibility(View.GONE);
                left_nav_view.setVisibility(View.GONE);
                break;

            case R.id.close_left_nav:
                right_nav_view.setVisibility(View.GONE);
                left_nav_view.setVisibility(View.GONE);
                right_nav_view.setVisibility(View.GONE);
                left_nav_view.setVisibility(View.GONE);

                left_nav_view.startAnimation(outToLeftAnimation());
                break;

            case R.id.location_lay:

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case R.id.For_sele_imageView:

                ismovedfromHome = true;
                bundle.putString("MainCatType", "104");
                fragment = new ItemMainFragment();
                fragment.setArguments(bundle);
                saveData(getApplicationContext(), "fragment_name", "ForSale");
                saveData(getApplicationContext(), "pcat_id", "104");
                replaceFragment(fragment, true);
                break;

            case R.id.imageViewJobS:
                ismovedfromHome = true;
                bundle.putString("MainCatType", "103");
                fragment = new JobsMainFragment();
                fragment.setArguments(bundle);
                saveData(getApplicationContext(), "fragment_name", "Jobs");
                saveData(getApplicationContext(), "pcat_id", "103");

                replaceFragment(fragment, true);

//              Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();
                break;

            case R.id.servicesImageView:
//                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();


                ismovedfromHome = true;
                bundle.putString("MainCatType", "101");
                fragment = new Bussiness_Service_Main();
                fragment.setArguments(bundle);
                saveData(getApplicationContext(), "fragment_name", "Services");
                saveData(getApplicationContext(), "pcat_id", "101");

                replaceFragment(fragment, true);
break;


            case R.id.houseRentalImageView:
//                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

//
                ismovedfromHome = true;
                bundle.putString("MainCatType", "102");
                fragment = new Property_Rental_Fragment();
                fragment.setArguments(bundle);
                saveData(getApplicationContext(), "fragment_name", "rental");
                saveData(getApplicationContext(), "pcat_id", "102");

                replaceFragment(fragment, true);
                break;

            case R.id.HouseSaleImageView:
                ismovedfromHome = true;
                bundle.putString("MainCatType", "272");
                fragment = new Property_Sale_Fragment();
                fragment.setArguments(bundle);
                saveData(getApplicationContext(), "fragment_name", "property sale");
                saveData(getApplicationContext(), "pcat_id", "272");
                replaceFragment(fragment, true);

//                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

                break;

            case R.id.personalImageView:

                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

//                ismovedfromHome = true;
//                bundle.putString("MainCatType", "100");
//                fragment = new SocialFragment();
//                fragment.setArguments(bundle);
//                saveData(getApplicationContext(), "fragment_name", "Social");
//                saveData(getApplicationContext(), "pcat_id", "100");
//                replaceFragment(fragment, true);
                break;
            case R.id.ShowcaseImageView:
                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

//                ismovedfromHome = true;
//                bundle.putString("MainCatType", "105");
//                fragment = new ShowCaseFragment();
//                fragment.setArguments(bundle);
//                saveData(getApplicationContext(), "fragment_name", "ShowCasee");
//                saveData(getApplicationContext(), "pcat_id", "105");
//                replaceFragment(fragment, true);
                break;

            case R.id.community_image_lay:

                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();
//                ismovedfromHome = true;
//                bundle.putString("MainCatType", "106");
//                fragment = new CommunityFragment();
//                fragment.setArguments(bundle);
//                saveData(getApplicationContext(), "fragment_name", "Community");
//                saveData(getApplicationContext(), "pcat_id", "106");
//                replaceFragment(fragment, true);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {

            Place place = PlacePicker.getPlace(data, getApplicationContext());
            LatLng location = place.getLatLng();
            String toastMsg = String.format("Place: %s", place.getName());
            String new_location = getAddressFromLatlng(location, getApplicationContext(), 0);
            locationTextView.setText(new_location);
            getData(getApplicationContext(), "currentLocation", new_location);
        }

        if (requestCode == 17) {
            if (resultCode == 1) {

                ArrayList<String> responseArray = new ArrayList<>();
                ArrayList<String> imageArray = new ArrayList<>();
                responseArray = data.getStringArrayListExtra("MESSAGE");
                for (int i = 0; i < responseArray.size(); i++) {
                    Uri uri = Uri.fromFile(new File(responseArray.get(i)));

                    Log.e("Uri" + i, uri.getPath());
                    saveData(getApplicationContext(), "item_img", uri.getPath());

                    //In case you need image's absolute path
                    // String path= getRealPathFromURI(getApplicationContext(), uri);
                    imageArray.add(uri.getPath());
                    //Log.e("Path"+i,path);
                    str_image_arr = new String[]{uri.getPath()};
                    //AIzaSyDn243JOuaMA4Sx9uMHf1DFXMPSYQECZ0I
                }

                ismovedfromHome = true;
                postImageLay.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putStringArray("imagess", str_image_arr);
                bundle.putStringArrayList("images", imageArray);
                bundle.putString("MainCatType", "104");
//                Postyour2Add postyour2Add = new Postyour2Add();
//                postyour2Add.setArguments(bundle);
//                fm = getSupportFragmentManager();
//                fm.beginTransaction().replace(R.id.allCategeries, postyour2Add).commit();
                startActivity(new Intent(getApplicationContext(), Postyour2Add.class).putExtras(bundle));
            }
        }

        if (requestCode == CAPTURE_IMAGES_FROM_CAMERA) {
            exitingCamera();
        }

//        if (requestCode == 01) {
//
//
//
//            ArrayList<String> imageArray = new ArrayList<>();
//
//            Bitmap bmp = (Bitmap) data.getExtras().get("data");
//            System.out.println("********* capture image from camera item main **********");
//            System.out.println(bmp);
//            Uri uri = getImageUri(getApplicationContext(),bmp);
//            Bundle bundle = new Bundle();
//            imageArray.add(uri.getPath());
//            saveData(getApplicationContext(),"item_img",getRealPathFromURI(uri));
//            bundle.putStringArray("imagess", str_image_arr);
//            bundle.putStringArrayList("images", imageArray);
//            bundle.putString("MainCatType", "0");
////                Postyour2Add postyour2Add = new Postyour2Add();
////                postyour2Add.setArguments(bundle);
////                fm = getSupportFragmentManager();
////                fm.beginTransaction().replace(R.id.allCategeries, postyour2Add).commit();
//            startActivity(new Intent(getApplicationContext(), Postyour2Add.class).putExtras(bundle));
//
//        }

    }


    private void LogoutAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle1);
        //ab.setTitle("Are you shore you want to log out");
        ab.setMessage("Are you sure you want to log out");
        ab.setNegativeButton("logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(getApplicationContext(), "loginData", "empty");
                NullData(getApplicationContext(), "user_profile_pic");
                NullData(getApplicationContext(), "user_name");
                NullData(getApplicationContext(), "user_id");

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                System.out.println("********** logout from g+ ");
                            }
                        });

                Intent intent = new Intent(getApplicationContext(), EnterLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();
    }


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        System.out.println("*********** is moved from hiome +++ " + ismovedfromHome);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finishAffinity();
            finish();
        }
        if (left_nav_view.getVisibility() == View.VISIBLE) {
            left_nav_view.setVisibility(View.GONE);
            ismovedfromHome = false;
        }
        else {
            if (ismovedfromHome) {
                startActivity(new Intent(this, MainActivity.class));
                ismovedfromHome = false;

            } else {
                finish();
            }
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);

        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        transaction.replace(R.id.allCategeries, fragment);
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        transaction.commit();
        overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_enter);
        getSupportFragmentManager().executePendingTransactions();

    }

}
