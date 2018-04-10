package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postBussiness;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour5Add;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.beans.categories;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.mindinfo.xchangemall.xchangemall.Constants.NetworkClass.getListData;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.cross_imageView;
import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;


public class PostyouBusiness extends Fragment implements View.OnClickListener {
    //Shard preferences

    ArrayList<String> postarr = new ArrayList<String>();
    TextView business_header, about_header, descripption_header, social_header, add_day_btn, website_header, hours_header;
    int count = 1;
    private Button next_btn;
    private ImageButton back_arrowImage;
    private FragmentManager fm;
    private AppCompatSpinner SpinnerCat, SpinnerWeekName1, SpinnerWeekName2, SpinnerWeekName3, SpinnerWeekName4, SpinnerWeekName5, SpinnerWeekName6, SpinnerWeekName7;
    private AppCompatSpinner SpinnerAM1, SpinnerAM2, SpinnerAM3, SpinnerAM4, SpinnerAM5, SpinnerAM6, SpinnerAM7;
    private AppCompatSpinner SpinnerPM1, SpinnerPM2, SpinnerPM3, SpinnerPM4, SpinnerPM5, SpinnerPM6, SpinnerPM7;
    private RelativeLayout add1, add2, add3, add4, add5, add6, add7;
    private String MainCatType;
    private ArrayList<categories> arrayList = new ArrayList<>();
    private EditText EditTextBusinessName, EditTextAbout, EditTextDescription, EditTextSocialMedia, EditTextWebsite;
    private ImageView remove_btn1, remove_btn2, remove_btn3, remove_btn4, remove_btn5, remove_btn6;
//    private TextView addVideoTV,videoname;
    private int VIDEO_CAPTURE = 1;
    private Uri videoUri ;
    File mediaFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.postyour4addforbus, null);

        //Get fm
        fm = getActivity().getSupportFragmentManager();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MainCatType = bundle.getString("MainCatType");
            postarr = bundle.getStringArrayList("imageSet");


        }
        findItem(v);
        setOnClick(v);
        loadSpinner();
        return v;
    }

    //finditem
    private void findItem(View v) {
        SpinnerCat =v.findViewById(R.id.SpinnerCat);
        next_btn = (Button) v.findViewById(R.id.next_btn);

        hours_header = v.findViewById(R.id.TextViewHouseOfOpertion);
//        addVideoTV = v.findViewById(R.id.addVideoTV);
//        videoname = v.findViewById(R.id.videoname);
        business_header = v.findViewById(R.id.business_name_header);
        descripption_header = v.findViewById(R.id.description_header_service);
        social_header = v.findViewById(R.id.social_header);
        about_header = v.findViewById(R.id.abount_header);
        website_header = v.findViewById(R.id.website_header);
        add_day_btn = v.findViewById(R.id.add_day_btn);

        back_arrowImage =v.findViewById(R.id.back_arrowImage);
        EditTextBusinessName =v.findViewById(R.id.EditTextBusinessName);
        EditTextAbout =v.findViewById(R.id.EditTextAbout);
        EditTextDescription =v.findViewById(R.id.EditTextDescription);
        EditTextSocialMedia =v.findViewById(R.id.EditTextSocialMedia);
        EditTextWebsite =v.findViewById(R.id.EditTextWebsite);

        SpinnerWeekName1 =v.findViewById(R.id.SpinnerWeekName);
        SpinnerWeekName2 =v.findViewById(R.id.SpinnerWeekName1);
        SpinnerWeekName3 =v.findViewById(R.id.SpinnerWeekName2);
        SpinnerWeekName4 =v.findViewById(R.id.SpinnerWeekName3);
        SpinnerWeekName5 =v.findViewById(R.id.SpinnerWeekName4);
        SpinnerWeekName6 =v.findViewById(R.id.SpinnerWeekName5);
        SpinnerWeekName7 =v.findViewById(R.id.SpinnerWeekName6);

        SpinnerAM1 =v.findViewById(R.id.SpinnerAM);
        SpinnerAM2 =v.findViewById(R.id.SpinnerAM1);
        SpinnerAM3 =v.findViewById(R.id.SpinnerAM2);
        SpinnerAM4 =v.findViewById(R.id.SpinnerAM3);
        SpinnerAM5 =v.findViewById(R.id.SpinnerAM4);
        SpinnerAM6 =v.findViewById(R.id.SpinnerAM5);
        SpinnerAM7 =v.findViewById(R.id.SpinnerAM6);

        SpinnerPM1 =v.findViewById(R.id.SpinnerPM);
        SpinnerPM2 =v.findViewById(R.id.SpinnerPM1);
        SpinnerPM3 =v.findViewById(R.id.SpinnerPM2);
        SpinnerPM4 =v.findViewById(R.id.SpinnerPM3);
        SpinnerPM5 =v.findViewById(R.id.SpinnerPM4);
        SpinnerPM6 =v.findViewById(R.id.SpinnerPM5);
        SpinnerPM6 =v.findViewById(R.id.SpinnerPM6);


        add1 =v.findViewById(R.id.add1);
        add2 =v.findViewById(R.id.add2);
        add3 =v.findViewById(R.id.add3);
        add4 =v.findViewById(R.id.add4);
        add5 =v.findViewById(R.id.add5);
        add6 =v.findViewById(R.id.add6);
        add7 =v.findViewById(R.id.add7);


        remove_btn1 = v.findViewById(R.id.remove_btn1);
        remove_btn2 = v.findViewById(R.id.remove_btn2);
        remove_btn3 = v.findViewById(R.id.remove_btn3);
        remove_btn4 = v.findViewById(R.id.remove_btn4);
        remove_btn5 = v.findViewById(R.id.remove_btn5);
        remove_btn6 = v.findViewById(R.id.remove_btn6);


        Typeface face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        pageNo_textView.setText("3of7");

        pageNo_textView.setTypeface(face);
//        addVideoTV.setTypeface(face);
//        videoname.setTypeface(face);
        hours_header.setTypeface(face);
        business_header.setTypeface(face);
        descripption_header.setTypeface(face);
        about_header.setTypeface(face);
        social_header.setTypeface(face);
        website_header.setTypeface(face);
        hours_header.setTypeface(face);
        EditTextWebsite.setTypeface(face);
        EditTextSocialMedia.setTypeface(face);
        EditTextDescription.setTypeface(face);
        EditTextAbout.setTypeface(face);
        EditTextBusinessName.setTypeface(face);
        add_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                addDay(count);
            }
        });
    }
    private void addDay(int count) {

        switch (count) {
            case 2:
                add2.setVisibility(View.VISIBLE);
                break;
            case 3:
                add3.setVisibility(View.VISIBLE);
                break;
            case 4:
                add4.setVisibility(View.VISIBLE);
                break;
            case 5:
                add5.setVisibility(View.VISIBLE);
                break;
            case 6:
                add6.setVisibility(View.VISIBLE);
                break;
            case 7:
                add7.setVisibility(View.VISIBLE);
                break;
        }
    }

    //set on Click Listener
    private void setOnClick(View v) {
        next_btn.setOnClickListener(this);
//        addVideoTV.setOnClickListener(this);
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);
        remove_btn1.setOnClickListener(this);
        remove_btn2.setOnClickListener(this);
        remove_btn3.setOnClickListener(this);
        remove_btn4.setOnClickListener(this);
        remove_btn5.setOnClickListener(this);
        remove_btn6.setOnClickListener(this);
    }

    //load spinner cat
    private void loadSpinner() {
        int catm = Integer.parseInt(MainCatType);
        // Toast.makeText(getActivity(), ""+catm, Toast.LENGTH_SHORT).show();
        getListData("101", arrayList, getActivity().getApplicationContext());
        // postAdapter.notifyDataSetChanged();
        ArrayAdapter<categories> dataAdapter = new ArrayAdapter<categories>(getActivity(),
                android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerCat.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                openNext();
                break;

                case R.id.addVideoTV:
//               openVideoRecorder();
                break;

            case R.id.remove_btn1:
                count=1;
                removeHours(add2);
                break;
            case R.id.remove_btn2:
                count=2;
                removeHours(add3);
                break;
            case R.id.remove_btn3:
                count=3;
                removeHours(add4);
                break;
            case R.id.remove_btn4:
                count=4;
                removeHours(add5);
                break;
            case R.id.remove_btn5:
                count=5;
                removeHours(add6);
                break;
            case R.id.remove_btn6:
                count=6;
                removeHours(add7);
                break;

            case R.id.back_arrowImage:
                getActivity().onBackPressed();
                break;
            case R.id.cross_imageView:
                OpenMainActivity();
                break;
        }
    }

//    private void openVideoRecorder() {
//         mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//                        + "/myvideo.mp4");
//
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//         videoUri = Uri.fromFile(mediaFile);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
//        startActivityForResult(intent, VIDEO_CAPTURE);
//    }

    private void removeHours(RelativeLayout layout) {
        layout.setVisibility(View.GONE);
    }

    private void openNext() {
        //EditTextBusinessName,EditTextAbout,EditTextDescription,EditTextSocialMedia,EditTextWebsite;
        if (EditTextBusinessName.getText().length() == 0) {
            ShowToast_msg("Enter Business Name");
            return;
        }
        if (EditTextAbout.getText().length() == 0) {
            ShowToast_msg("Enter About");
            return;
        }
        if (EditTextDescription.getText().length() == 0) {
            ShowToast_msg("Enter Description");
            return;
        }
        if (EditTextSocialMedia.getText().length() == 0) {
            ShowToast_msg("Enter social media link");
            return;
        }
//        if (videoUri==null)
//        {
//            ShowToast_msg("Upload VIdeo");
//            return;
//        }
//        if (videoUri!=null)
//        {
//            if (videoUri.toString().length()==0)
//            {
//                ShowToast_msg("Upload VIdeo");
//                return;
//            }
//        }
        ArrayList<String> categoryids = new ArrayList<String>();
        categories cat = arrayList.get(SpinnerCat.getSelectedItemPosition());
        categoryids.add(cat.getId());
        cat.getTitle();
        cat.getId();

        System.err.println("========= selected cat for buss =======" + cat.getId() + cat.getTitle());

        JSONArray operationArray = new JSONArray();
        addspinners(SpinnerWeekName1, SpinnerAM1, SpinnerPM1, operationArray);

        if (add2.getVisibility() == View.VISIBLE)
            addspinners(SpinnerWeekName2, SpinnerAM2, SpinnerPM2, operationArray);

        if (add3.getVisibility() == View.VISIBLE)
            addspinners(SpinnerWeekName3, SpinnerAM3, SpinnerPM3, operationArray);

        if (add4.getVisibility() == View.VISIBLE)
            addspinners(SpinnerWeekName4, SpinnerAM4, SpinnerPM4, operationArray);

        if (add5.getVisibility() == View.VISIBLE)
            addspinners(SpinnerWeekName5, SpinnerAM5, SpinnerPM5, operationArray);

        if (add6.getVisibility() == View.VISIBLE)
            addspinners(SpinnerWeekName6, SpinnerAM6, SpinnerPM6, operationArray);

        if (add7.getVisibility() == View.VISIBLE)
            addspinners(SpinnerWeekName7, SpinnerAM7, SpinnerPM7, operationArray);

        Bundle bundle = new Bundle();
        JSONObject bussinessData = new JSONObject();
        try {
            bussinessData.put("business_name", EditTextBusinessName.getText().toString());
            bussinessData.put("postDes", EditTextDescription.getText().toString());
            bussinessData.put("about_business", EditTextAbout.getText().toString());
            bussinessData.put("social_media_link", EditTextSocialMedia.getText().toString());
            bussinessData.put("website_link", EditTextWebsite.getText().toString());
            bussinessData.put("hours_of_operation", operationArray.toString());
//            bussinessData.put("video_file",videoUri.getPath());
            bussinessData.put("category", cat.getId());

            System.err.println("==== operation array =======");
            System.err.println(operationArray.toString());
            bundle.putString("bussinessobj", bussinessData.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        //bundle.putStringArray("imagess",str_image_arr);
        bundle.putStringArrayList("imageSet", postarr);
        bundle.putString("MainCatType", "101");
        bundle.putString("sub_cat_id", cat.getId());
        bundle.putStringArrayList("selectedcategories", categoryids);


        Postyour5Add postyour5Add = new Postyour5Add();
        postyour5Add.setArguments(bundle);
        fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.allCategeriesIN, postyour5Add).addToBackStack(null).commit();
    }

    private void addspinners(AppCompatSpinner WeekName1, AppCompatSpinner spinnerAM1, AppCompatSpinner spinnerPM1, JSONArray operationArray) {
        try {

            JSONObject obj1 = new JSONObject();
            obj1.put("days", WeekName1.getSelectedItem().toString());
            obj1.put("open_time", spinnerAM1.getSelectedItem().toString());
            obj1.put("close_time", spinnerPM1.getSelectedItem().toString());
            operationArray.put(obj1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenMainActivity() {
        Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(i);
    }


    private void ShowToast_msg(String str_msg) {
        Toast.makeText(getActivity(), str_msg, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == VIDEO_CAPTURE) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(getActivity(), "Video saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
//                videoname.setVisibility(View.VISIBLE);
//                videoname.setText(videoUri.toString());
//            }
//            else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(getActivity(), "Video recording cancelled.",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getActivity(), "Failed to record video",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}


