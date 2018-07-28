package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Bussiness_Service_Main;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.CommunityFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.GamesFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.ItemMainFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.JobsMainFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.NewsFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Property_Rental_Fragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.Property_Sale_Fragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.BaseActivity;
import com.mindinfo.xchangemall.xchangemall.activities.common.FavoritesActivity;
import com.mindinfo.xchangemall.xchangemall.activities.common.ProfileActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.CustomList;
import com.mindinfo.xchangemall.xchangemall.adapter.ExpandableListAdapter;
import com.mindinfo.xchangemall.xchangemall.intefaces.Consts;
import com.mindinfo.xchangemall.xchangemall.intefaces.OnBackPressed;


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
import java.util.HashMap;
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
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout left_nav_view;
    
    public static boolean ismovedfromHome;
    public static boolean isdogChecked, iscatChecked;
    private  final int PLACE_PICKER_REQUEST = 1;
    public String str_image_arr[];
    RelativeLayout right_nav_view;
    TextView category_head_list, user_nameTV, locationTextView;
    FragmentManager fm;
    String url = "";
    Intent i;
    ListView list;
    LinearLayout vdo_cl_lay, share_btn;
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
    private LinearLayout open_left_nav_btn, open_right_nav_btn, close_left_nav,
           news_lay, games_lay, right_nav_white, log_out,jobs_lay;

    private LinearLayout  servicesImageView, houseRentalImageView, HouseSaleImageView,
            personalImageView, ShowcaseImageView, CommutnityImageView, forsale_lay;

    public static void start(Context context, boolean isRunForCall) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        context.startActivity(intent);
    }
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

        share_btn.setOnClickListener(this);

        if (!ismovedfromHome) {
            forsale_lay.setOnClickListener(this);
            jobs_lay.setOnClickListener(this);
            news_lay.setOnClickListener(this);
            games_lay.setOnClickListener(this);
            servicesImageView.setOnClickListener(this);
            houseRentalImageView.setOnClickListener(this);
            HouseSaleImageView.setOnClickListener(this);
            personalImageView.setOnClickListener(this);
            ShowcaseImageView.setOnClickListener(this);
            CommutnityImageView.setOnClickListener(this);
        }

        log_out.setOnClickListener(this);

        list.setOnItemClickListener((parent, view, position, id) -> {
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
        left_nav_view =findViewById(R.id.left_nav_view);
        share_btn =findViewById(R.id.share_btn);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        user_nameTV =findViewById(R.id.user_nameTV);
        log_out =findViewById(R.id.log_out);
        right_nav_white =findViewById(R.id.right_nav_white);
        right_nav_view = (RelativeLayout) findViewById(R.id.right_nav);
        close_left_nav =findViewById(R.id.close_left_nav);
        open_left_nav_btn =findViewById(R.id.openLeftNav);
        open_right_nav_btn =findViewById(R.id.openRightNav);

        vdo_cl_lay =findViewById(R.id.vdo_cl);
        forsale_lay =findViewById(R.id.For_sele_imageView);
        jobs_lay =findViewById(R.id.imageViewJobS);
        news_lay =findViewById(R.id.newsLay);
        games_lay =findViewById(R.id.gamesLay);
        servicesImageView =findViewById(R.id.servicesImageView);
        houseRentalImageView =findViewById(R.id.houseRentalImageView);
        HouseSaleImageView =findViewById(R.id.HouseSaleImageView);
        personalImageView =findViewById(R.id.personalImageView);
        ShowcaseImageView =findViewById(R.id.ShowcaseImageView);
        CommutnityImageView =findViewById(R.id.community_image_lay);

        locationTextView =findViewById(R.id.location_Textview);
        category_head_list =findViewById(R.id.category_head_list);

        expListView =findViewById(R.id.nav_categories_lisview);
        list = (ListView) findViewById(R.id.rightnav_listview);

        left_nav_view.setVisibility(View.GONE);
        right_nav_view.setVisibility(View.GONE);

        String city = getData(getApplicationContext(), "currentLocation", "");
        user_name = getData(getApplicationContext(), "user_name", "");
        user_image = getData(getApplicationContext(), "user_profile_pic", "");


        System.out.println("=============== curreny city=============");
        System.out.println(city);

        locationTextView.setText(city);
        Typeface face = ResourcesCompat.getFont(MainActivity.this, R.font.estre);
        locationTextView.setTypeface(face);

        user_nameTV.setTypeface(face);
        category_head_list.setTypeface(face);

        if (user_name.length() > 2) {
            user_nameTV.setText(user_name);

            System.out.println("************* image at main activity ********");
            System.out.println(user_image);


            if (user_image.equals(DEFAULT_PATH))
               Glide.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

            else {
                try {
                   Glide.with(getApplicationContext()).load(user_image).apply(RequestOptions.placeholderOf(R.drawable.profile)).into(profile_image);
                } catch (Exception e) {
                   Glide.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

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

        expListView.setAdapter(listAdapter);


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listHeaderImage = new ArrayList<Integer>();
        listDataChild = new HashMap<String, List<String>>();

        listHeaderImage.add(R.drawable.community);
        listHeaderImage.add(R.drawable.buddies);
        listHeaderImage.add(R.drawable.event);
        listHeaderImage.add(R.drawable.humanservicesicon);
        listHeaderImage.add(R.drawable.icon_personal);
        listHeaderImage.add(R.drawable.show);
        listHeaderImage.add(R.drawable.news);
        listHeaderImage.add(R.drawable.games);
        listHeaderImage.add(R.drawable.for_sale);
        listHeaderImage.add(R.drawable.job);
        listHeaderImage.add(R.drawable.propertyforsale_bg);
        listHeaderImage.add(R.drawable.houseing);


        String responseCate = getData(getApplicationContext(), "categoriesdata_full", "");

        try {

            JSONArray jsonArray = new JSONArray(responseCate);
            System.out.println("-------- cat response --------");
            System.out.println(jsonArray);


            if (jsonArray.length() > 0) {
                String cat_id = "", cat_name = "";


                for (int i = 0; i < jsonArray.length(); i++)
                {
                    List<String> childdata = new ArrayList<String>();
                    JSONObject responseobj2 = jsonArray.getJSONObject(i);

                    JSONArray childArray = responseobj2.getJSONArray("data");

                    cat_name = responseobj2.getString("title");
                    cat_id = responseobj2.getString("id");

                    for (int l = 0; l < childArray.length(); l++) {
                        childdata.add(childArray.getJSONObject(l).getString("title") + "~" + childArray.getJSONObject(l)
                                .getString("id"));
                    }
                    listDataHeader.add(cat_name);
                    System.out.println(childdata);
                    listDataChild.put(cat_name, childdata);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Homeclick(View view) {
        if (ismovedfromHome) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            ismovedfromHome = false;
        } else {
            left_nav_view.setVisibility(View.GONE);
            ismovedfromHome = false;
        }

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

            case R.id.newsLay:
                ismovedfromHome = true;
                fragment = new NewsFragment();
                saveData(getApplicationContext(), "fragment_name", "News");
                replaceFragment(fragment, true);

                break;

            case R.id.gamesLay:
                ismovedfromHome = true;
                fragment = new GamesFragment();
                saveData(getApplicationContext(), "fragment_name", "Games");
                replaceFragment(fragment, true);
                break;

            case R.id.servicesImageView:
                ismovedfromHome = true;
                bundle.putString("MainCatType", "101");
                fragment = new Bussiness_Service_Main();
                fragment.setArguments(bundle);
                saveData(getApplicationContext(), "fragment_name", "Services");
                saveData(getApplicationContext(), "pcat_id", "101");

                replaceFragment(fragment, true);
                break;


            case R.id.houseRentalImageView:
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
                ismovedfromHome = true;
                bundle.putString("MainCatType", "106");
                fragment = new CommunityFragment();
                fragment.setArguments(bundle);
                saveData(getApplicationContext(), "fragment_name", "Community");
                saveData(getApplicationContext(), "pcat_id", "106");
                replaceFragment(fragment, true);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {

            Place place = PlacePicker.getPlace(data, getApplicationContext());
            LatLng location = place.getLatLng();

            String new_location = getAddressFromLatlng(location, getApplicationContext(), 0);
            locationTextView.setText(new_location);
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
                    imageArray.add(uri.getPath());
                    str_image_arr = new String[]{uri.getPath()};
                }

                ismovedfromHome = true;
                Bundle bundle = new Bundle();
                bundle.putStringArray("imagess", str_image_arr);
                bundle.putStringArrayList("images", imageArray);
                bundle.putString("MainCatType", "104");
                startActivity(new Intent(getApplicationContext(), Postyour2Add.class).putExtras(bundle));
            }
        }
    }

    private void LogoutAlertDialog() {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveData(getApplicationContext(), "loginData", "empty");
                        NullData(getApplicationContext(), "user_profile_pic");
                        NullData(getApplicationContext(), "user_name");
                        NullData(getApplicationContext(), "user_id");

                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                status -> System.out.println("********** logout from g+ "));

                        Intent intent = new Intent(getApplicationContext(), EnterLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        builder.setMessage("Are you sure you want to log out").setPositiveButton("Logout", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
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
        System.out.println("*********** is moved from home -->  " + ismovedfromHome);

        if (left_nav_view.getVisibility() == View.VISIBLE) {
            left_nav_view.setVisibility(View.GONE);
            ismovedfromHome = false;
            return;
        }

        try {
            Fragment currentFragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager()
                    .getBackStackEntryCount() - 1);
            System.out.println("============== current fragment ===");
            System.out.println(currentFragment);
            if (currentFragment != null && currentFragment instanceof OnBackPressed)
            {
                ((OnBackPressed) currentFragment).onBackPressed();
                System.out.println("------- instance of onbackpressed--- > ");
            }
            else
            {
                System.out.println("------- no instance of onbackpressed--- > ");
            }
        } catch (IndexOutOfBoundsException e)
        {
            System.err.println("error iob");
            if (ismovedfromHome) {
                startActivity(new Intent(this, MainActivity.class));
                ismovedfromHome = false;
            } else {
                finish();
            }
        }

        if (getIntent().getBooleanExtra("EXIT", false)) {
            System.out.println("-------finishAffinity onbackpressed--- > ");
            finishAffinity();
            finish();
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
