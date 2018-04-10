package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.addJobs;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.addProperty;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.addpost;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postJob.PostyourJob.job_exp;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postJob.PostyourJob.job_responsbitity;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postJob.PostyourJob.job_salary;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postJob.PostyourJob.job_title;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.getData;
import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;


public class Postyour7Add extends Fragment implements View.OnClickListener {

    static ArrayList<String> imageSet = new ArrayList<String>();
    ArrayList<String> categoryids;
    String address;
    String obj;
    JSONObject postOBj = new JSONObject();
    //next_btn
    private TextView next_btn;
    //Fragment Manager
    private FragmentManager fm;
    private String sub_cat_id = "", MainCatType, postTitle = "", postDes = "", postPrice = "",
            postSize = "", postCondition, privacy_str, phone_str = "",
            Existence_str, contName_str = "", language_str = "", lat = "", lng = "", zipcode = "";
    private CheckBox checkboxFree, checkboxPaid, checkboxAutoRenew;
    private ImageView back_arrowImage;

    private String pcat_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.postyour7add, null);

        //Get fm
        fm = getActivity().getSupportFragmentManager();
        MainCatType = getData(getActivity().getApplicationContext(), "MainCatType", "");


        System.out.println("**** **** Main Cat at post time ******* " + MainCatType);

        finditem(v);
        OnClick(v);
        categoryids = new ArrayList<String>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sub_cat_id = bundle.getString("sub_cat_id");
            address = bundle.getString("completeaddress");
            privacy_str = bundle.getString("privacy_str");
            phone_str = bundle.getString("phone_str");
            Existence_str = bundle.getString("Existence_str");
            contName_str = bundle.getString("contName_str");
            language_str = bundle.getString("language_str");
            imageSet = bundle.getStringArrayList("imageSet");
            postTitle = bundle.getString("postTitle");

            if (MainCatType.equals("104")) {

                postDes = bundle.getString("postDes");
                postPrice = bundle.getString("postPrice");
                postSize = bundle.getString("postSize");
                postCondition = bundle.getString("postCondition");
                pcat_name = bundle.getString("pcat_name");
                categoryids = bundle.getStringArrayList("selectedcategories");
            } else if (MainCatType.equals("102") || MainCatType.equals("272")) {

                System.out.println("**** **** MAin Cat in if else  ******* " + MainCatType);

                obj = bundle.getString("prop_obj");
                System.out.println("========= prop obj ==");
                System.out.println(obj);
                try {
                    postOBj = new JSONObject(obj);
                    System.out.println("** prop obj frag 7 ****");
                    System.out.println(postOBj);
                } catch (JSONException e) {

                }
                categoryids = bundle.getStringArrayList("selectedcategories");
                System.out.println("**** **** Cat in if else  ******* " + categoryids);

            } else if (MainCatType.equals("101")) {
                obj = bundle.getString("bussinessobj");

                try {
                    postOBj = new JSONObject(obj);
                    System.out.println("** buss obj frag 7 ****");
                    System.out.println(postOBj);
                } catch (JSONException e) {

                }
                saveData(getActivity().getApplicationContext(), "MainCatType", "101");
            }


            lat = bundle.getString("lat");
            lng = bundle.getString("lng");

            System.err.println("*************post obj at post time ****");
            System.err.println(obj);

            System.out.println("************* latlng at post time ****");
            System.out.println(lat);
            System.out.println(lng);


        }

        return v;
    }

    //find item
    private void finditem(View v) {
        next_btn = (TextView) v.findViewById(R.id.next_btn);
        checkboxFree = (CheckBox) v.findViewById(R.id.checkboxFree);
        checkboxPaid = (CheckBox) v.findViewById(R.id.checkboxPaid);
        checkboxAutoRenew = (CheckBox) v.findViewById(R.id.checkboxAutoRenew);

        back_arrowImage = (ImageView) v.findViewById(R.id.back_arrowImage);
        pageNo_textView.setText("7of7");
    }

    //Set on Click on
    private void OnClick(View view) {
        next_btn.setOnClickListener(this);
        checkboxFree.setOnClickListener(this);
        checkboxPaid.setOnClickListener(this);
        checkboxAutoRenew.setOnClickListener(this);
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);
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

            case R.id.next_btn:

                System.out.println("************* Main Cat at post property " + MainCatType);
                System.out.println("************* Cat at post property " + categoryids);
                PostAddbutonClick(MainCatType);
                break;
            case R.id.checkboxFree:
                Switch();
                break;
            case R.id.checkboxPaid:
                Switch1();
                break;
            case R.id.checkboxAutoRenew:
                Switch1();
                break;
        }
    }

    private void Switch() {
        if (checkboxFree.isChecked()) {
            checkboxPaid.setChecked(false);
            checkboxAutoRenew.setChecked(false);
        }
    }

    private void Switch1() {
        if (checkboxFree.isChecked()) {
            checkboxFree.setChecked(false);
        }
    }

    private void PostAddbutonClick(String mainCatType) {

        if (checkboxFree.isChecked()) {

            System.out.println(categoryids);
            System.out.println(mainCatType);


            String user_id = getData(getActivity().getApplicationContext(), "user_id", "");
            String jobtype = "";

            switch (mainCatType) {
                case "103":

                    jobtype = getData(getActivity().getApplicationContext(), "job_type", "");

                    if (jobtype.equals("Full time"))
                        jobtype = "2";
                    else
                        jobtype = "1";


                    System.out.println("******* edtails for job post ***** ");
                    System.out.println(job_exp);
                    System.out.println(job_responsbitity);
                    System.out.println(job_title);
                    System.out.println(job_salary);
                    addJobs(getActivity(), user_id, contName_str, job_title, postDes, address,
                            phone_str,
                            lat, lng, categoryids, MainCatType,
                            "en", imageSet, job_responsbitity, job_exp, jobtype, job_salary);
                    break;

                case "104":

                    addpost(getActivity(), user_id, contName_str, postTitle, postDes, postPrice, address, phone_str,
                            lat, lng, categoryids, MainCatType,
                            "en", imageSet, postSize, postCondition);
                    break;


                case "102":
                    try {
                        String val = getData(getActivity().getApplicationContext(), "prop_obj", "");
                        postOBj = new JSONObject(val);

                        String roomUnit = postOBj.getString("room_unit");

                        String prop_desc = postOBj.getString("property_desc");
                        String prop_price = postOBj.getString("property_price");
                        String prop_size = postOBj.getString("prop_size");
                        String washroom_unit = postOBj.getString("bathroom_unit");

                        System.out.println("** adding property ********");

                        addProperty(getActivity(), user_id, contName_str, postTitle, prop_desc, address, phone_str, lat, lng,
                                categoryids, MainCatType, "en", imageSet, washroom_unit, roomUnit, prop_price, prop_size);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    break;
                case "272":
                    try {
                        String val = getData(getActivity().getApplicationContext(), "prop_obj", "");
                        postOBj = new JSONObject(val);

                        String roomUnit = postOBj.getString("room_unit");

                        String prop_desc = postOBj.getString("property_desc");
                        String prop_price = postOBj.getString("property_price");
                        String prop_size = postOBj.getString("prop_size");
                        String washroom_unit = postOBj.getString("bathroom_unit");

                        System.out.println("** adding property ********");

                        addProperty(getActivity(), user_id, contName_str, postTitle, prop_desc, address, phone_str, lat, lng,
                                categoryids, MainCatType, "en", imageSet, washroom_unit, roomUnit, prop_price, prop_size);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    break;

                case "101":
                    try {
                        String business_name = postOBj.getString("business_name");
                        String postDes = postOBj.getString("postDes");
                        String about_business = postOBj.getString("about_business");
                        String social_media_link = postOBj.getString("social_media_link");
                        String website_link = postOBj.getString("website_link");
                        String hours_of_operation = postOBj.getString("hours_of_operation");
                        String cat_id = postOBj.getString("category");
//                        String vdo =postOBj.getString("video_file");
//                        File file= new File(vdo);
                       categoryids.add(cat_id);
//
//                        System.out.println("======== vdo file=========");
//                        System.out.println(file);
                        NetworkClass.addBusiness(getActivity(), user_id, business_name,
                                postDes, about_business,categoryids,address,lat,lng,MainCatType,"en",imageSet,social_media_link,website_link,hours_of_operation );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }
    }


    private void OpenMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }

}

