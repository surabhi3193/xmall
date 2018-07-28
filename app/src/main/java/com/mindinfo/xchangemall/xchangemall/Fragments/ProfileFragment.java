package com.mindinfo.xchangemall.xchangemall.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.HLVAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.mindinfo.xchangemall.xchangemall.activities.common.ProfileActivity.profile_image;
import static com.mindinfo.xchangemall.xchangemall.activities.common.ProfileActivity.tvUserName;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.BASE_URL_NEW;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.DEFAULT_PATH;
import static com.mindinfo.xchangemall.xchangemall.activities.BaseActivity.user_image;
import static com.mindinfo.xchangemall.xchangemall.other.CheckInternetConnection.isNetworkAvailable;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class ProfileFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 3;
    Marker mapMarker;
    ProgressDialog ringProgressDialog;

    RecyclerView mRecyclerView;
    RecyclerView mRecyclerView_vdo;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.LayoutManager mLayoutManager_vdo;
    RecyclerView.Adapter mAdapter;
    RecyclerView.Adapter mAdapter_vdo;
    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    ArrayList<Integer> alImgVdo;
    String user_name;

    String dob_staus = "1", email_status = "1", mobile_staus = "1";
    private View view;
    private TextView TextEdit, editAboutMe, TextViewLocationEdit;
    private String user_id;
    private GoogleMap mMap;
    private TextView GenderText, DateText, OccupationText, WorkPlaceText, InterestText, EducationText, phoneNumberText,
            EmailText, AboutMeText, locationTV, socialTV, dob_statusTV, email_statustV, mobile_statusTV;
    private String gender;
    private String occupation;
    private String interrest;
    private String workplace;
    private String education;
    private String dob;
    private String phone;
    private String social_link;
    private String about_me;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.profileposted, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        user_id = getData(Objects.requireNonNull(getActivity()), "user_id", "");

        alName = new ArrayList<>(Arrays.asList("", "", "", "", ""));
        alImage = new ArrayList<>(Arrays.asList(R.drawable.image1, R.drawable.img2, R.drawable.img3, R.drawable.car_image, R.drawable.image1));
        alImgVdo = new ArrayList<>(Arrays.asList(R.drawable.youtube_dummy2, R.drawable.youtube_dummy1, R.drawable.youtube_dummy2));

        user_name = getData(getActivity(), "user_name", "");

        findItem(view);
        setOnClick();
        forMapView();

        getUserProfile(user_id);
        return view;
    }

    private void getUserProfile(String user_id) {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Updating", true);
        ringProgressDialog.setCancelable(false);


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "get_profile", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ringProgressDialog.dismiss();
                    System.out.println("response**********");
                    System.out.println(response);
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        saveData(Objects.requireNonNull(getActivity()), "userData", response.getJSONObject("result").toString());
                        String userDetail = getData(getActivity(), "userData", "");
                        try {

                            System.out.println(userDetail);
                            JSONObject joboj = new JSONObject(userDetail);
                            getUserData(joboj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();

                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                ringProgressDialog.dismiss();

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void addValues(String fname, String lname, String email, String gender,
                           String occupation, String interest, String workplace,
                           String education, String dob, String phone, String social_link,
                           String profile_photo, String aboutme,
                           String dob_staus, String email_status,
                           String mobile_staus) throws JSONException {

        GenderText.setText(gender);
        WorkPlaceText.setText(workplace);
        InterestText.setText(interest);
        EducationText.setText(education);
        phoneNumberText.setText(phone);
        EmailText.setText(email);
        DateText.setText(dob);
        OccupationText.setText(occupation);
        socialTV.setText(social_link);
        tvUserName.setText(fname + " " + lname);

        validate(dob_staus, dob_statusTV);
        validate(email_status, email_statustV);
        validate(mobile_staus, mobile_statusTV);


        AboutMeText.setText(aboutme);

        JSONObject userData = new JSONObject();
        userData.put("first_name", fname);
        userData.put("last_name", lname);
        userData.put("gender", gender);
        userData.put("work_place", workplace);
        userData.put("interest", interest);
        userData.put("education", education);
        userData.put("user_phone", phone);
        userData.put("user_email", email);
        userData.put("dob", dob);
        userData.put("occupation", occupation);
        userData.put("social_link", social_link);
        userData.put("about_me", aboutme);
        userData.put("profile_photo", profile_photo);
        userData.put("email_hide_status", email_status);
        userData.put("phone_hide_status", mobile_staus);
        userData.put("dob_hide_status", dob_staus);

        System.out.println("********** user pic in profile fragment **************");
        System.out.println(profile_photo);

        saveData(Objects.requireNonNull(getActivity()), "user_profile_pic", profile_photo);
        saveData(getActivity(), "user_name", fname);
        user_image = profile_photo;
        user_name = fname;

        System.out.println("********** user pic in profile fragment before if  **************");
        System.out.println(profile_photo);
        if (profile_photo.equals(DEFAULT_PATH))
            Glide.with(getActivity()).load(R.drawable.profile).into(profile_image);

        else

            Glide.with(getActivity()).load(profile_photo).into(profile_image);

        saveData(getActivity(), "userData", userData.toString());

    }

    @SuppressLint("SetTextI18n")
    private void validate(String dob_staus, TextView dob_statusTV) {
        if (dob_staus.equals("1")) {
            dob_statusTV.setText("Hide");
            dob_statusTV.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.light_red));
        } else if (dob_staus.equals("2")) {
            dob_statusTV.setText("Show");
            dob_statusTV.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.green));

        }
    }


    private void forMapView() {


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            if (mapMarker != null)
                mapMarker.remove();


            double lat = Double.parseDouble(getData(Objects.requireNonNull(getActivity()).getBaseContext(), "userLat", "0"));
            double lng = Double.parseDouble(getData(getActivity().getBaseContext(), "userLong", "0"));
            LatLng sydney = new LatLng(lat, lng);

            System.out.println("************ user current location  in profile frag *******");
            System.out.println(lat);
            System.out.println(lng);

            mapMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("You're Location"));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney).tilt(45).bearing(45).zoom((float) 18.5).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            String address = getAddressFromLatlng(sydney, getActivity().getBaseContext(), 1);
            System.out.println("-------- your location for tv ----- " + address);
            locationTV.setText(address);

        });
    }

    //findItems
    @SuppressLint("ClickableViewAccessibility")
    private void findItem(View v) {

        dob_statusTV = v.findViewById(R.id.dob_statusTV);
        mobile_statusTV = v.findViewById(R.id.mobile_statusTV);
        email_statustV = v.findViewById(R.id.email_statusTV);

        TextEdit = v.findViewById(R.id.TextEdit);
        editAboutMe = v.findViewById(R.id.editAboutMe);
        TextViewLocationEdit = v.findViewById(R.id.TextViewLocationEdit);

        GenderText = v.findViewById(R.id.GenderText);
        DateText = v.findViewById(R.id.DateText);
        OccupationText = v.findViewById(R.id.OccupationText);
        WorkPlaceText = v.findViewById(R.id.WorkPlaceText);
        InterestText = v.findViewById(R.id.InterestText);
        EducationText = v.findViewById(R.id.EducationText);
        phoneNumberText = v.findViewById(R.id.phoneNumberText);
        EmailText = v.findViewById(R.id.EmailText);
        AboutMeText = v.findViewById(R.id.AboutMeText);
        locationTV = v.findViewById(R.id.locationName);
        socialTV = v.findViewById(R.id.socialTV);
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView_vdo = v.findViewById(R.id.recycler_view_vdo);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView_vdo.setHasFixedSize(true);


        Typeface face = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()), R.font.estre);
        GenderText.setTypeface(face);
        DateText.setTypeface(face);
        OccupationText.setTypeface(face);
        WorkPlaceText.setTypeface(face);
        socialTV.setTypeface(face);
        AboutMeText.setTypeface(face);
        locationTV.setTypeface(face);
        EmailText.setTypeface(face);
        phoneNumberText.setTypeface(face);
        EducationText.setTypeface(face);
        InterestText.setTypeface(face);


        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.my_custom_divider)));
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView_vdo.addItemDecoration(divider);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager_vdo = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_vdo.setLayoutManager(mLayoutManager_vdo);

        mAdapter = new HLVAdapter(getActivity(), alName, alImage);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter_vdo = new HLVAdapter(getActivity(), alName, alImgVdo);
        mRecyclerView_vdo.setAdapter(mAdapter_vdo);
    }

    private void setOnClick() {
        TextEdit.setOnClickListener(v -> ShowDialog());

        editAboutMe.setOnClickListener(v -> ShowDialogAbout());

        dob_statusTV.setOnClickListener(view -> {

            String status = checkstatus(dob_staus);
            PostUserUpdatedDetailToServer(user_id, "", "", "",
                    "", "", "", "", "", "", "", "", status, email_status, mobile_staus);
        });

        email_statustV.setOnClickListener(view -> {
            String status = checkstatus(email_status);

            PostUserUpdatedDetailToServer(user_id, "", "", "",
                    "", "", "", "", "", "", "", "", dob_staus, status, mobile_staus);
        });


        mobile_statusTV.setOnClickListener(view -> {
            String status = checkstatus(mobile_staus);

            PostUserUpdatedDetailToServer(user_id, "", "", "",
                    "", "", "", "", "", "", "", "", dob_staus, email_status, status);
        });


        TextViewLocationEdit.setOnClickListener(view -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(Objects.requireNonNull(getActivity())), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private String checkstatus(String staus) {
        if (staus.equals("1"))
            staus = "2";

        else if (staus.equals("2"))
            staus = "1";

        return staus;
    }

    private void ShowDialogAbout() {
        AlertDialog.Builder ab = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        final AlertDialog alert;
        alert = ab.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.alertdialogprofileupdateaboutme, null);

        final EditText EditTextAboutMe = v.findViewById(R.id.EditTextAboutMe);
        Button cancel_button = v.findViewById(R.id.cancel_button);
        Button update_button = v.findViewById(R.id.update_button);

        EditTextAboutMe.setText(about_me);
        cancel_button.setOnClickListener(v1 -> {
            alert.dismiss();
            alert.cancel();
        });

        update_button.setOnClickListener(v12 -> {
            String data = EditTextAboutMe.getText().toString();

            PostUserUpdatedDetailToServer(user_id, "", "", "",
                    "", "", "", "", "", "", "", data, dob_staus, email_status, mobile_staus);
            alert.dismiss();
        });

        alert.setView(v);
        alert.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, Objects.requireNonNull(getActivity()));
                LatLng location = place.getLatLng();

                if (mapMarker != null)
                    mapMarker.remove();
//                String toastMsg = String.format("Place: %s", place.getName());

                String new_location = getAddressFromLatlng(location, getActivity(), 1);
                locationTV.setText("  " + new_location);
                System.out.println("-------- your location for tv ----- " + new_location);
                mapMarker = mMap.addMarker(new MarkerOptions().position
                        (new LatLng(location.latitude, location.longitude)));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.latitude, location.longitude)).tilt(45).bearing(45).zoom((float) 18.5).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        }
    }


    private void ShowDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        final AlertDialog alert;
        alert = ab.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.alertdialogprofileupdategender, null);

        final AppCompatSpinner genderSpinner = v.findViewById(R.id.genderSpinner);
        final EditText dobEditText = v.findViewById(R.id.dobEditText);
        final EditText occEditText = v.findViewById(R.id.occEditText);
        final EditText WorkPlaceEditText = v.findViewById(R.id.WorkPlaceEditText);
        final EditText InterestEditText = v.findViewById(R.id.InterestEditText);
        final EditText EducationEditText = v.findViewById(R.id.EducationEditText);
        final EditText phoneNumberEditText = v.findViewById(R.id.phoneNumberEditText);
        final EditText SocialLinkEditText = v.findViewById(R.id.SocialLinkEditText);

        switch (gender) {
            case "Female":
                genderSpinner.setSelection(2);
                break;
            case "Male":
                genderSpinner.setSelection(1);
                break;
            default:
                genderSpinner.setSelection(0);
                break;
        }
        dobEditText.setText(dob);
        occEditText.setText(occupation);
        WorkPlaceEditText.setText(workplace);
        InterestEditText.setText(interrest);
        EducationEditText.setText(education);
        phoneNumberEditText.setText(phone);
        SocialLinkEditText.setText(social_link);

        Button cancel_button = v.findViewById(R.id.cancel_button);
        Button update_button = v.findViewById(R.id.update_button);
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(dobEditText, myCalendar);
        };

        dobEditText.setOnClickListener(v1 -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        cancel_button.setOnClickListener(v12 -> {
            alert.dismiss();
            alert.cancel();
        });
        update_button.setOnClickListener(v13 -> {

            String dob = dobEditText.getText().toString();
            String mobileno = phoneNumberEditText.getText().toString();
            String occ = occEditText.getText().toString();
            String educ = EducationEditText.getText().toString();
            String work = WorkPlaceEditText.getText().toString();
            String interest = InterestEditText.getText().toString();

            String sociallink = SocialLinkEditText.getText().toString();

            if (isNetworkAvailable(getActivity())) {
                String mob = phoneNumberEditText.getText().toString();
                String mpatt = "[0-9]{10,10}";

                if (mob.length() > 0) {
                    boolean b3 = isMatch(mob, mpatt);
                    if (!b3) {
                        Toast.makeText(getActivity(), "please enter valid mobile no", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                PostUserUpdatedDetailToServer(user_id, "", "", genderSpinner.getSelectedItem().toString(),
                        dob, occ, work, interest, educ, mobileno, sociallink, "", dob_staus, email_status, mobile_staus);

                alert.dismiss();
                alert.cancel();
            } else {
                AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle1);
                ab2.setTitle("Internet connected");
                ab2.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                ab2.show();
            }

        });

        alert.setView(v);
        alert.show();

    }


    public void PostUserUpdatedDetailToServer(final String user_id, final String name, final String profile_pic, final String gender, final String
            dob, final String occupation, final String workplace, final String interest, final
                                              String edu, final String phone, final String social_link, final String about_me, String dob_staus,
                                              String email_status, String mobile_staus) {

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Updating", true);
        ringProgressDialog.setCancelable(false);
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("user_id", user_id);
        params.put("name", name);
        params.put("gender", gender);
        params.put("dob", dob);
        params.put("occupation", occupation);
        params.put("work_place", workplace);
        params.put("interest", interest);
        params.put("user_phone", phone);
        params.put("social_link", social_link);
        params.put("about_me", about_me);
        params.put("education", edu);
        params.put("profile_photo", profile_pic);
        params.put("dob_hide_status", dob_staus);
        params.put("email_hide_status", email_status);
        params.put("phone_hide_status", mobile_staus);
        System.out.println(params);
        client.post(BASE_URL_NEW + "update_profile", params, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            ringProgressDialog.dismiss();
                            System.out.println("response**********");
                            System.out.println(response);
                            if (response.getString("status").equals("0")) {
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                JSONObject jsonObject = response.getJSONObject("result");
                                getUserData(jsonObject);

                            }
                        } catch (Exception e) {
                            ringProgressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getActivity(), "Server Error,Try Again ", Toast.LENGTH_LONG).show();
                        ringProgressDialog.dismiss();

                    }

                }
        );
    }
    private void getUserData(JSONObject jsonObject) {
        try {
            String fname = jsonObject.getString("first_name");
            String lname = "";
            String email = jsonObject.getString("user_email");
            gender = jsonObject.getString("gender");
            occupation = jsonObject.getString("occupation");
            interrest = jsonObject.getString("interest");
            workplace = jsonObject.getString("work_place");
            education = jsonObject.getString("education");
            phone = jsonObject.getString("user_phone");
            social_link = jsonObject.getString("social_link");
            dob_staus = jsonObject.getString("dob_hide_status");
            email_status = jsonObject.getString("email_hide_status");
            mobile_staus = jsonObject.getString("phone_hide_status");
            String profile_photo = jsonObject.getString("profile_photo");
            about_me = jsonObject.getString("desc");
            dob = jsonObject.getString("dob");

            addValues(fname, lname, email, gender, occupation, interrest, workplace, education, dob, phone, social_link, profile_photo, about_me, dob_staus, email_status, mobile_staus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void updateLabel(EditText textEdit, Calendar myCalendar) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textEdit.setText(sdf.format(myCalendar.getTime()));
    }

    boolean isMatch(String s, String patt) {
        Pattern pat = Pattern.compile(patt);
        Matcher m = pat.matcher(s);
        return m.matches();
    }
}
