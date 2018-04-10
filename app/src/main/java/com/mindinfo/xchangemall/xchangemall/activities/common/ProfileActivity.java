package com.mindinfo.xchangemall.xchangemall.activities.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.Fragments.MyFavorites;
import com.mindinfo.xchangemall.xchangemall.Fragments.MyMessage;
import com.mindinfo.xchangemall.xchangemall.Fragments.ProfileFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.SaveSearchFragment;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.EnterLogin;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.SlideImageAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.DEFAULT_PATH;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.user_image;
import static com.mindinfo.xchangemall.xchangemall.activities.main.BaseActivity.user_name;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.NullData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    public static TextView tvUserName;
    public static ScrollView main_scroll;
    public static CircleImageView profile_image;
    private static ViewPager mPager;
    private static int currentPage = 0;
    ProgressDialog ringProgressDialog;
    String id_old, user_type_old, first_name_old, last_name_old, gender_old, profile_photo_old, user_name_old, user_phone_old, user_email_old,
            lng_old, lat_old, verify_code_old, numeric_code_old, login_type_old, education_old, interest_old, work_place_old,
            social_link_old, desc_old, user_imgs_old, user_vdos_old, data_old, apitype_old, con_old;
    ArrayList<String> postarr = new ArrayList<String>();
    FragmentManager fm;
    RelativeLayout ImageProfileFullVIew;
    LinearLayout edit_btn;
    GoogleApiClient mGoogleApiClient;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
//    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager, viewPagerForTab;
    private ImageView back_arrowImage;
    private LinearLayout backBtn, done_btn;
    private ImageView profile_image_full;
    private TextView textViewEditName, image_heade_full, image_header_profile, logout_btn;
    private ArrayList<Uri> XMENArrayUri = new ArrayList<Uri>();
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image_full = (ImageView) findViewById(R.id.profile_pic_IV);
        tvUserName = (TextView) findViewById(R.id.UserProfileName);
        textViewEditName = (TextView) findViewById(R.id.textViewEditName);
        ;
        image_heade_full = (TextView) findViewById(R.id.image_header);
        ;
        image_header_profile = (TextView) findViewById(R.id.textView7);
        logout_btn = (TextView) findViewById(R.id.logout_btn);
        backBtn = (LinearLayout) findViewById(R.id.back_Btn);
        ImageProfileFullVIew = (RelativeLayout) findViewById(R.id.ImageProfileFullVIew);
        edit_btn = (LinearLayout) findViewById(R.id.edit_btn);
        done_btn = (LinearLayout) findViewById(R.id.done_btn);

        main_scroll = (ScrollView) findViewById(R.id.main_scroll);
//        scrollView.fullScroll(View.FOCUS_UP);
        Typeface face = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        textViewEditName.setTypeface(face);
        image_heade_full.setTypeface(face);
        image_header_profile.setTypeface(face);
        logout_btn.setTypeface(face);
        tvUserName.setTypeface(face);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutAlertDialog();
            }
        });


        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        fm = getSupportFragmentManager();
        ImageProfileFullVIew.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            user_image = getData(getApplicationContext(), "user_profile_pic",DEFAULT_PATH);

            user_name = bundle.getString("username");

            tvUserName.setText(user_name);


            if (user_image.length() > 2)
                Picasso.with(getApplicationContext()).load(user_image).into(profile_image);

        }


//      toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        back_arrowImage = (ImageView) findViewById(R.id.back_arrowImage);
        back_arrowImage.setOnClickListener(this);


//        viewPagerForTab = (ViewPager) findViewById(R.id.viewPagerForTab);
//        setupViewPager(viewPagerForTab);
//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(viewPagerForTab);


        viewPagerForTab = (ViewPager)findViewById(R.id.viewPagerForTab);
        viewPagerForTab.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        createViewPager(viewPagerForTab);
        // Connect the tabs with the ViewPager (the setupWithViewPager method does this for us in
        // both directions, i.e. when a new tab is selected, the ViewPager switches to this page,
        // and when the ViewPager switches to a new page, the corresponding tab is selected)
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPagerForTab);
        createTabIcons();


        Intent intent = getIntent();

        boolean openProfile = intent.getBooleanExtra("openProfile", false);

        init();
//        findItem();
        ClickItem();

    }



    public  void PostUserUpdatedDetailToServer(final  String user_id,final String data,final String type){

        ringProgressDialog = ProgressDialog.show(ProfileActivity.this, "Please wait ...", "Updating", true);
        ringProgressDialog.setCancelable(false);


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);
        params.put(type, data);


        client.post(BASE_URL_NEW + "update_profile", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();

                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = response.getJSONObject("result");
                        String firstname = jsonObject.getString("first_name");
                        String profile_photo = jsonObject.getString("profile_photo");
                       tvUserName.setText(firstname);
                       saveData(getApplicationContext(),"user_profile_pic",profile_photo);
                       saveData(getApplicationContext(),"user_name",firstname);
                        if(profile_photo.equals(DEFAULT_PATH))
                            Picasso.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

                        else

                        Picasso.with(getApplicationContext()).load(profile_photo).into(profile_image);


                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }

        });
    }


    public  void UpdatePIc(final  String user_id,final File data,final String type){

        ringProgressDialog = ProgressDialog.show(ProfileActivity.this, "Please wait ...", "Updating", true);
        ringProgressDialog.setCancelable(false);


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);
        try {
            params.put(type, data);
        } catch (FileNotFoundException e) {

        }
        client.post(BASE_URL_NEW + "update_profile", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();

                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = response.getJSONObject("result");
                        String firstname = jsonObject.getString("first_name");
                        String profile_photo = jsonObject.getString("profile_photo");
                        tvUserName.setText(firstname);
                        saveData(getApplicationContext(),"user_profile_pic",profile_photo);
                        saveData(getApplicationContext(),"user_name",firstname);


                        if(profile_photo.equals(DEFAULT_PATH))
                            Picasso.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

                        else {

                            Picasso.with(getApplicationContext()).load(profile_photo).into(profile_image_full);
                            Picasso.with(getApplicationContext()).load(profile_photo).into(profile_image);
                        }

                        done_btn.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(errorResponse);

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
//        session.removeImage();
    }

    private void ClickItem() {
        textViewEditName.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        done_btn.setOnClickListener(this);
    }


    private void LogoutAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(ProfileActivity.this, R.style.MyAlertDialogStyle1);
        //ab.setTitle("Are you shore you want to log out");
        ab.setMessage("Are you sure you want to log out");
        ab.setNegativeButton("logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(getApplicationContext(), "loginData", "empty");
                NullData(getApplicationContext(), "user_profile_pic");
                NullData(getApplicationContext(), "user_name");

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


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new SaveSearchFragment(), "Saved Search");
        adapter.addFragment(new MyFavorites(), "My Favorites");
        adapter.addFragment(new MyMessage(), "My Message");
//        adapter.addFragment(new MyShowcase(), "My Showcase");
//        adapter.addFragment(new PaymentInfo(), "Payment info");
        // adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrowImage:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;

            case R.id.textViewEditName:
                ShowDialog_chage_name();
                break;

            case R.id.back_Btn:
                ImageProfileFullVIew.setVisibility(View.GONE);
                break;

                case R.id.done_btn:
                ImageProfileFullVIew.setVisibility(View.GONE);
                break;

            case R.id.profile_image:
                ImageProfileFullVIew.setVisibility(View.VISIBLE);

                user_image = getData(getApplicationContext(),"user_profile_pic",DEFAULT_PATH);
                Picasso.with(getApplicationContext()).load(user_image).placeholder(R.drawable.profile).into(profile_image_full);
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {


                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (thumbnail!=null)
        {
            Uri uri = getImageUri(getApplicationContext(),thumbnail);
            getRealPathFromURI(uri);
            String userid = getData(getApplicationContext(),"user_id","");


            UpdatePIc(userid,new File(getRealPathFromURI(uri)),"profile_photo");
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (bm!=null)
        {
            Uri uri = getImageUri(getApplicationContext(),bm);
           getRealPathFromURI(uri);
            String userid = getData(getApplicationContext(),"user_id","");


            UpdatePIc(userid,new File(getRealPathFromURI(uri)),"profile_photo");
        }

    }

    private void ShowDialog_chage_name() {
        AlertDialog.Builder ab = new AlertDialog.Builder(ProfileActivity.this);
        final AlertDialog alert;
        alert = ab.create();
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alertdialogprofileupdatename, null);

        final EditText editTextName = (EditText) v.findViewById(R.id.editTextName);


        editTextName.setText(getData(getApplicationContext(),"user_name",""));

        Button cancel_button = (Button) v.findViewById(R.id.cancel_button);
        Button update_button = (Button) v.findViewById(R.id.update_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                alert.cancel();
            }
        });
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(getApplicationContext())) {
                    HashMap<String, String> user;
//                    user = session.getUserDetails();
//
//                    ParseLoginResponse(user.get(Constants.LOGIN_SUCCESS));

                    if (editTextName.getText().length() == 0) {
                        Toast.makeText(ProfileActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String changed_name ="" ;
                    changed_name=editTextName.getText().toString();

                    String userid = getData(getApplicationContext(),"user_id","");
                    PostUserUpdatedDetailToServer(userid,changed_name,"name");
                    alert.dismiss();
                    alert.cancel();
                } else {
                    ShowAlertDialog("Internet not connected", "");
                }
            }
        });

        alert.setView(v);
        alert.show();

    }


    private void ShowAlertDialog(String str_title, String str_mess) {
        AlertDialog.Builder ab = new AlertDialog.Builder(ProfileActivity.this, R.style.MyAlertDialogStyle1);
        ab.setTitle(str_title);
        ab.setMessage(str_mess);
        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void init() {
        //  for(int i=0;i<XMEN.length;i++)
        //      XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(new SlideImageAdapter(XMENArrayUri, ProfileActivity.this));
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMENArrayUri.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
    }

    private void createViewPager(ViewPager viewPager) {
       MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());



        adapter.addFrag(new ProfileFragment(), "Profile");
        adapter.addFrag(new SaveSearchFragment(), "Saved Search");
        adapter.addFrag(new MyFavorites(), "My Favorites");
        adapter.addFrag(new MyMessage(), "My Message");
//        adapter.addFrag(new MyShowcase(), "My Showcase");
//        adapter.addFrag(new PaymentInfo(), "Payment info");



        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {

        Typeface face = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/estre.ttf");

        TextView tabThree = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("PROFILE");
        tabThree.setTextColor(getResources().getColor(R.color.business_port_text));
        tabThree.setTypeface(face);
        tabLayout.getTabAt(0).setCustomView(tabThree);

        TextView tabTwo = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("SAVED SEARCH");
        tabTwo.setTextColor(getResources().getColor(R.color.logo_color));
        tabTwo.setTypeface(face);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabFour = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText("MY FAVORITE");
        tabFour.setTextColor(getResources().getColor(R.color.LimeGreen));
        tabFour.setTypeface(face);
        tabLayout.getTabAt(2).setCustomView(tabFour);

        TextView tabfive = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabfive.setText("MY MESSAGE");
        tabfive.setTextColor(getResources().getColor(R.color.business_port_text));
        tabfive.setTypeface(face);
        tabLayout.getTabAt(3).setCustomView(tabfive);

//
//        TextView tabsix = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
//        tabsix.setText("MY SHOWCASE");
//        tabsix.setTextColor(getResources().getColor(R.color.logo_color));
//        tabsix.setTypeface(face);
//        tabLayout.getTabAt(4).setCustomView(tabsix);
//
//        TextView tabseven = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
//        tabseven.setText("PAYMENT PROFILE");
//        tabseven.setTextColor(getResources().getColor(R.color.green));
//        tabseven.setTypeface(face);
//        tabLayout.getTabAt(5).setCustomView(tabseven);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




}
