package com.mindinfo.xchangemall.xchangemall.activities.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.Fragments.MyFavorites;
import com.mindinfo.xchangemall.xchangemall.Fragments.MyMessage;
import com.mindinfo.xchangemall.xchangemall.Fragments.ProfileFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.SaveSearchFragment;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.DetailsFragment;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.EnterLogin;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.SlideImageAdapter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.DEFAULT_PATH;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.user_image;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.user_name;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.NullData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    @SuppressLint("StaticFieldLeak")
    public static TextView tvUserName;
    @SuppressLint("StaticFieldLeak")
    public static ScrollView main_scroll;
    public static CircleImageView profile_image;
    @SuppressLint("StaticFieldLeak")
    private  ViewPager mPager;
    private  int currentPage = 0;
    ProgressDialog ringProgressDialog;
    FragmentManager fm;
    RelativeLayout ImageProfileFullVIew;
    LinearLayout edit_btn;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private TabLayout tabLayout;
    private LinearLayout backBtn, done_btn;
    private ImageView profile_image_full;
    private TextView textViewEditName;
    private ArrayList<Uri> XMENArrayUri = new ArrayList<Uri>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_image =findViewById(R.id.profile_image);
        profile_image_full =findViewById(R.id.profile_pic_IV);
        tvUserName =findViewById(R.id.UserProfileName);
        textViewEditName =findViewById(R.id.textViewEditName);
        ;
        TextView image_heade_full = findViewById(R.id.image_header);
        ;
        TextView image_header_profile = findViewById(R.id.textView7);
        TextView logout_btn = findViewById(R.id.logout_btn);
        backBtn =findViewById(R.id.back_Btn);
        ImageProfileFullVIew =findViewById(R.id.ImageProfileFullVIew);
        edit_btn =findViewById(R.id.edit_btn);
        done_btn =findViewById(R.id.done_btn);

        main_scroll =findViewById(R.id.main_scroll);
        Typeface face =     ResourcesCompat.getFont(ProfileActivity.this, R.font.estre);



        textViewEditName.setTypeface(face);
        image_heade_full.setTypeface(face);
        image_header_profile.setTypeface(face);
        logout_btn.setTypeface(face);
        tvUserName.setTypeface(face);
        logout_btn.setOnClickListener(view -> LogoutAlertDialog());


        edit_btn.setOnClickListener(view -> selectImage());
        fm = getSupportFragmentManager();
        ImageProfileFullVIew.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            user_image = getData(getApplicationContext(), "user_profile_pic", DEFAULT_PATH);

            user_name = bundle.getString("username");

            tvUserName.setText(user_name);

               Glide.with(getApplicationContext()).load(user_image).apply(RequestOptions.placeholderOf(R.drawable.profile)).into(profile_image);

        }

        ImageView back_arrowImage = findViewById(R.id.back_arrowImage);
        back_arrowImage.setOnClickListener(this);

        ViewPager viewPagerForTab = findViewById(R.id.viewPagerForTab);
        viewPagerForTab.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        createViewPager(viewPagerForTab);
        tabLayout =findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPagerForTab);
        createTabIcons();
        init();
        ClickItem();

    }


    public void PostUserUpdatedDetailToServer(final String user_id, final String data, final String type) {

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
                        saveData(getApplicationContext(), "user_profile_pic", profile_photo);
                        saveData(getApplicationContext(), "user_name", firstname);
                        if (profile_photo.equals(DEFAULT_PATH))
                           Glide.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

                        else

                           Glide.with(getApplicationContext()).load(profile_photo).apply(RequestOptions.placeholderOf(R.drawable.profile)).into(profile_image);


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


    public void UpdatePIc(final String user_id, final File data, final String type) {

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
                        saveData(getApplicationContext(), "user_profile_pic", profile_photo);
                        saveData(getApplicationContext(), "user_name", firstname);


                        if (profile_photo.equals(DEFAULT_PATH))
                           Glide.with(getApplicationContext()).load(R.drawable.profile).into(profile_image);

                        else {

                           Glide.with(getApplicationContext()).load(profile_photo).apply(RequestOptions.placeholderOf(R.drawable.profile)).into(profile_image_full);
                           Glide.with(getApplicationContext()).load(profile_photo).apply(RequestOptions.placeholderOf(R.drawable.profile)).into(profile_image);
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
    }

    private void ClickItem() {
        textViewEditName.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        done_btn.setOnClickListener(this);
    }


    private void LogoutAlertDialog() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveData(getApplicationContext(), "loginData", "empty");
                        NullData(getApplicationContext(), "user_profile_pic");
                        NullData(getApplicationContext(), "user_name");

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

                user_image = getData(getApplicationContext(), "user_profile_pic", DEFAULT_PATH);
               Glide.with(getApplicationContext()).load(user_image).into(profile_image_full);
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {

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
        Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        assert thumbnail != null;
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
            Uri uri = getImageUri(getApplicationContext(), thumbnail);
            getRealPathFromURI(uri);
            String userid = getData(getApplicationContext(), "user_id", "");


            UpdatePIc(userid, new File(getRealPathFromURI(uri)), "profile_photo");
   
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (bm != null) {
            Uri uri = getImageUri(getApplicationContext(), bm);
            getRealPathFromURI(uri);
            String userid = getData(getApplicationContext(), "user_id", "");


            UpdatePIc(userid, new File(getRealPathFromURI(uri)), "profile_photo");
        }

    }

    private void ShowDialog_chage_name() {
        AlertDialog.Builder ab = new AlertDialog.Builder(ProfileActivity.this);
        final AlertDialog alert;
        alert = ab.create();
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.alertdialogprofileupdatename, null);

        final EditText editTextName =v.findViewById(R.id.editTextName);


        editTextName.setText(getData(getApplicationContext(), "user_name", ""));

        Button cancel_button =v.findViewById(R.id.cancel_button);
        Button update_button =v.findViewById(R.id.update_button);

        cancel_button.setOnClickListener(v1 -> {
            alert.dismiss();
            alert.cancel();
        });
        update_button.setOnClickListener(v12 -> {

            if (isNetworkAvailable(getApplicationContext())) {
                if (editTextName.getText().length() == 0) {
                    Toast.makeText(ProfileActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String changed_name = "";
                changed_name = editTextName.getText().toString();

                String userid = getData(getApplicationContext(), "user_id", "");
                PostUserUpdatedDetailToServer(userid, changed_name, "name");
                alert.dismiss();
                alert.cancel();
            } else {
                ShowAlertDialog("Internet not connected", "");
            }
        });

        alert.setView(v);
        alert.show();

    }


    private void ShowAlertDialog(String str_title, String str_mess) {
        AlertDialog.Builder ab = new AlertDialog.Builder(ProfileActivity.this, R.style.MyAlertDialogStyle1);
        ab.setTitle(str_title);
        ab.setMessage(str_mess);
        ab.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
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
        assert cursor != null;
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void init() {
        mPager =findViewById(R.id.viewPager);
        mPager.setAdapter(new SlideImageAdapter(XMENArrayUri, ProfileActivity.this));
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


        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {

        Typeface face =      ResourcesCompat.getFont(ProfileActivity.this, R.font.estre);


        TextView tabThree = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("PROFILE");
        tabThree.setTextColor(getResources().getColor(R.color.business_port_text));
        tabThree.setTypeface(face);
        tabLayout.getTabAt(0).setCustomView(tabThree);

        TextView tabTwo = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("SAVED SEARCH");
        tabTwo.setTextColor(getResources().getColor(R.color.logo_color));
        tabTwo.setTypeface(face);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setCustomView(tabTwo);

        TextView tabFour = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText("MY FAVORITE");
        tabFour.setTextColor(getResources().getColor(R.color.LimeGreen));
        tabFour.setTypeface(face);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setCustomView(tabFour);

        TextView tabfive = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
        tabfive.setText("MY MESSAGE");
        tabfive.setTextColor(getResources().getColor(R.color.business_port_text));
        tabfive.setTypeface(face);
        Objects.requireNonNull(tabLayout.getTabAt(3)).setCustomView(tabfive);


    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private MyPagerAdapter(FragmentManager manager) {
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

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
