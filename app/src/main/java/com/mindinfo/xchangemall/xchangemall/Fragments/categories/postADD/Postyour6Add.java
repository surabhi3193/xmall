package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.other.GPSTracker;

import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour4Add.face;
import static com.mindinfo.xchangemall.xchangemall.other.GeocodingLocation.getAddressFromLatlng;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;

public class Postyour6Add extends Fragment implements View.OnClickListener {
    private static final int PLACE_PICKER_REQUEST = 12;
    ArrayList<String> imageSet = new ArrayList<String>();
    ArrayList<String> categoryids = new ArrayList<String>();
    Marker location_marker = null;
    String lat, lng;
    String obj;
    SupportMapFragment mapFragment;
    private View view;
    //next_btn
    private Button next_btn, saveLocation;
    private TextView inputLocationEditText;
    private String sub_cat_id = "", MainCatType, postTitle, postDes, postPrice, postSize, postCondition, privacy_str, phone_str, Existence_str, contName_str, language_str;
    //Fragment Manager
    private FragmentManager fm;
    private GoogleMap mMap;
    private TextView headercategory;
    private ImageButton back_arrowImage;
    private String pcat_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.postyour6add, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        fm = getActivity().getSupportFragmentManager();
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        GPSTracker gpsTracker = new GPSTracker(getActivity());
        gpsTracker.isGpsConnected();
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            if (location_marker != null) {
                location_marker.remove();
            }

            double latitude =gpsTracker.getLatitude();
            double longitide = gpsTracker.getLongitude();

            lat= String.valueOf(latitude);
            lng= String.valueOf(longitide);
            LatLng latLng = new LatLng(latitude, longitide);

            String full_address = getAddressFromLatlng(latLng, getActivity(), 1);

            inputLocationEditText.setText(full_address);

            location_marker = mMap.addMarker(new MarkerOptions().position(latLng));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).tilt(45).bearing(45).zoom((float) 18.5).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        });
        findItems(view);

        onClickonItem(view);

        MainCatType = getData(getActivity(), "pcat_id", "");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //bussinessobj
            sub_cat_id = bundle.getString("sub_cat_id");
            pcat_name = bundle.getString("pcat_name");
            MainCatType = bundle.getString("MainCatType");
            imageSet = bundle.getStringArrayList("imageSet");
            privacy_str = bundle.getString("privacy_str");
            phone_str = bundle.getString("phone_str");
            Existence_str = bundle.getString("Existence_str");
            contName_str = bundle.getString("contName_str");
            language_str = bundle.getString("language_str");

            if (MainCatType.equals("104")) {
                postTitle = bundle.getString("postTitle");
                postDes = bundle.getString("postDes");
                postPrice = bundle.getString("postPrice");
                postSize = bundle.getString("postSize");
                postCondition = bundle.getString("postCondition");
                categoryids = bundle.getStringArrayList("selectedcategories");
                System.out.println("============ post details at frfag 6 ********");
                System.out.println(postTitle + " title");
                System.out.println(postDes + " desc");
                System.out.println(postPrice + " price");
                System.out.println(postSize + " size");
                System.out.println(postCondition + " condi");
            } else if (MainCatType.equals("102") || MainCatType.equals("272")) {
                obj = bundle.getString("prop_obj");
                categoryids = bundle.getStringArrayList("selectedcategories");
            } else {
                obj = bundle.getString("bussinessobj");
            }
        }
        return view;
    }

    //findItem
    private void findItems(View v) {
        next_btn =v.findViewById(R.id.next_btn);
        saveLocation =v.findViewById(R.id.saveLocation);
        inputLocationEditText =v.findViewById(R.id.inputLocationEditText);

        back_arrowImage =v.findViewById(R.id.back_arrowImage);
        headercategory =v.findViewById(R.id.headercategory);

        pageNo_textView.setText("6of7");
        pageNo_textView.setTypeface(face);
        headercategory.setTypeface(face);
        inputLocationEditText.setTypeface(face);
        saveLocation.setTypeface(face);
        headercategory.setText(pcat_name);

    }


    private void onClickonItem(View v) {
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        saveLocation.setOnClickListener(this);
        inputLocationEditText.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrowImage:
                getActivity().onBackPressed();
                break;
            case R.id.cross_imageView:
                OpenMainActivity();
                break;

            case R.id.inputLocationEditText:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.next_btn:
                if (lat == null || lng == null || lat.equalsIgnoreCase("0.0") || lng.equalsIgnoreCase("0.0")) {
                    Toast.makeText(getActivity(), "please get location", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                if (MainCatType.equals("101")) {

                    bundle.putString("bussinessobj", obj);
                    bundle.putString("sub_cat_id", bundle.getString(sub_cat_id));


                } else if (MainCatType.equals("102") || MainCatType.equals("272")) {

                    System.out.println("========= prop obj ==");
                    System.out.println(obj);
                    bundle.putString("prop_obj", obj);
                    bundle.putString("sub_cat_id", bundle.getString(sub_cat_id));
                } else {
                    bundle.putString("pcat_name", pcat_name);
                    bundle.putString("postTitle", postTitle);
                    bundle.putString("postDes", postDes);
                    bundle.putString("postPrice", postPrice);
                    bundle.putString("postSize", postSize);
                    bundle.putString("postCondition", postCondition);
                    bundle.putStringArrayList("selectedcategories", categoryids);
                }

                bundle.putString("privacy_str", privacy_str);
                bundle.putString("phone_str", phone_str);
                bundle.putString("Existence_str", Existence_str);
                bundle.putString("contName_str", contName_str);
                bundle.putString("language_str", language_str);
                bundle.putString("MainCatType", MainCatType);
                bundle.putStringArrayList("imageSet", imageSet);
                bundle.putString("lat", lat);
                bundle.putString("lng", lng);
                bundle.putString("completeaddress", inputLocationEditText.getText().toString());
                Postyour7Add postyour7add = new Postyour7Add();
                postyour7add.setArguments(bundle);
                fm.beginTransaction().replace(R.id.allCategeriesIN, postyour7add).addToBackStack(null).commit();

                break;

            case R.id.saveLocation:
                if (inputLocationEditText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter your zip code", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ClickonSavelocation(inputLocationEditText.getText().toString());
                }
                break;
        }
    }

    private void ClickonSavelocation(String zipStr) {
        mMap.clear();
    }

    private void OpenMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, getActivity());
                LatLng location = place.getLatLng();

                lat = String.valueOf(location.latitude);
                lng = String.valueOf(location.longitude);

//                String toastMsg = String.format("Place: %s", place.getName());

                String new_location = getAddressFromLatlng(location, getActivity(), 1);
                inputLocationEditText.setText("  " + new_location);
                location_marker = mMap.addMarker(new MarkerOptions().position
                        (new LatLng(location.latitude, location.longitude)));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.latitude, location.longitude)).tilt(45).bearing(45).zoom((float) 18.5).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        }
    }

}

