package com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postBussiness.PostyouBusiness;
import com.mindinfo.xchangemall.xchangemall.Fragments.categories.postJob.PostyourJob;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.BaseActivity;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;
import com.mindinfo.xchangemall.xchangemall.adapter.SliderAdapter2;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

import static com.mindinfo.xchangemall.xchangemall.storage.MySharedPref.saveData;

public class Postyour2Add extends BaseActivity implements View.OnClickListener{


    public static ImageView cross_imageView;
    public static TextView pageNo_textView;
    String id;
    ArrayList<String> postarr;
    ArrayList<Uri> imageSet;
    //next_btn
    private Button next_btn;
    //Fragment Manager
    private FragmentManager fm;
    private ImageButton back_arrowImage;
    private LinearLayout imageViewJobS, For_sele_imageView, servicesImageView,
            ShowcaseImageView, personalImageView, CommImageView, houseRentalImageView, propertySaleImageView;
    private Fragment fragment;


    private ViewPager mPager;
    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postyour2add);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Get fm
        fm = getSupportFragmentManager();
        findItem();

        OnClick();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imageSet = new ArrayList<Uri>();
            imageSet = bundle.getParcelableArrayList("imageSet");
            id = bundle.getString("MainCatType");

            System.out.println(" ************* Image Set ***********");
            System.out.println(" *************selected cat id ***********");
            System.out.println(imageSet);
            System.out.println(id);

            setBackgroundId(id);
        }


        ArrayList<Uri> imageArray = new ArrayList<>();
        for (int i = 0; i < imageSet.size(); i++)
        {
//            url_maps.put("image" + i, new File(imageSet.get(i)));
            imageArray.add(imageSet.get(i));
        }
        mPager.setAdapter(new SliderAdapter2(Postyour2Add.this, imageArray));

        mPager.setBackgroundColor(getResources().getColor(R.color.black));
        indicator.setViewPager(mPager);

//        for (String name : url_maps.keySet()) {
//            TextSliderView textSliderView = new TextSliderView(this);
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
//                    .setOnSliderClickListener(this);
//
//
//
////            //add your extra information
////            textSliderView.bundle(new Bundle());
////            textSliderView.getBundle()
////                    .putString("extra", name);
////            imageSlider.stopAutoCycle();
////            imageSlider.clearAnimation();
////            imageSlider.addSlider(textSliderView);
//        }


    }
//
//    @Override
//    protected View getSnackbarAnchorView() {
//        return null;
//    }
    private void setBackgroundId(String id) {
        switch (id) {
            case "101":
                servicesImageView.setBackground(getResources().getDrawable(R.drawable.selected_cat_bg));
                break;

            case "102":
                houseRentalImageView.setBackground(getResources().getDrawable(R.drawable.selected_cat_bg));
                break;
            case "103":
                imageViewJobS.setBackground(getResources().getDrawable(R.drawable.selected_cat_bg));
                break;
            case "104":
                For_sele_imageView.setBackground(getResources().getDrawable(R.drawable.selected_cat_bg));
                break;
            case "272":
                propertySaleImageView.setBackground(getResources().getDrawable(R.drawable.selected_cat_bg));
                break;
        }

    }


    // find item
    private void findItem() {
//        imageSlider = (SliderLayout) findViewById(R.id.slider);
        next_btn = (Button) findViewById(R.id.next_btn);
        //  Home_ImageView = (ImageView) v.findViewById(R.id.Home_ImageView);
        cross_imageView = (ImageView) findViewById(R.id.cross_imageView);
        pageNo_textView = (TextView) findViewById(R.id.pageNo_textView);
        back_arrowImage = (ImageButton) findViewById(R.id.back_arrowImage);
//        postCatMRecyclerView = (RecyclerView) v.findViewById(R.id.postCatMRecyclerView);
        pageNo_textView.setText("2of7");


        mPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        imageViewJobS = findViewById(R.id.imageViewJobS);
        For_sele_imageView = findViewById(R.id.For_sele_imageView);
        servicesImageView = findViewById(R.id.servicesImageView);
        ShowcaseImageView = findViewById(R.id.ShowcaseImageView);
        personalImageView = findViewById(R.id.personalImageView);
        CommImageView = findViewById(R.id.CommImageView);
        houseRentalImageView = findViewById(R.id.houseRentalImageView);
        propertySaleImageView = findViewById(R.id.propertySaleImageView);

    }

    //OnClick
    private void OnClick() {
//        Home_ImageView.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        back_arrowImage.setOnClickListener(this);
        cross_imageView.setOnClickListener(this);

        imageViewJobS.setOnClickListener(this);
        For_sele_imageView.setOnClickListener(this);
        servicesImageView.setOnClickListener(this);
        ShowcaseImageView.setOnClickListener(this);
        personalImageView.setOnClickListener(this);
        CommImageView.setOnClickListener(this);
        houseRentalImageView.setOnClickListener(this);
        propertySaleImageView.setOnClickListener(this);
    }


    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:

                opnenext(id);
                break;

            case R.id.imageViewJobS:
//                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

                opnenext("103");
                break;
            case R.id.For_sele_imageView:
                opnenext("104");
                break;

            case R.id.servicesImageView:
//                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_SHORT).show();
                opnenext("101");
                break;

            case R.id.ShowcaseImageView:
                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

                break;

            case R.id.personalImageView:
                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

                break;

            case R.id.CommImageView:
                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT).show();

                break;

            case R.id.houseRentalImageView:
                opnenext("102");
                break;

            case R.id.propertySaleImageView:
//                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_SHORT).show();

//                ImageType_str = "7";
////                fragment = new PostyourHouse();
                opnenext("272");
                break;

            case R.id.back_arrowImage:
                OpenMainActivity();
                break;
            case R.id.cross_imageView:
                OpenMainActivity();
                break;

        }
    }

    private void OpenMainActivity() {
        Intent i = new Intent(Postyour2Add.this, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    @SuppressLint("ResourceType")
    private void opnenext(String str) {

        saveData(getApplicationContext(), "MainCatType", str);

        if (str.equals("0")) {
            Toast.makeText(getApplicationContext(), "Please select category first", Toast.LENGTH_LONG).show();
        } else {
            switch (str) {
                case "101":
                    fragment = new PostyouBusiness();
                    break;

                case "102":
                    fragment = new Postyour3Add();
                    break;

                case "104":
                    System.out.println("********** click on next with forsale**********");
                    fragment = new Postyour3Add();
                    break;

                case "103":
                    fragment = new PostyourJob();
                    break;

                case "272":
                    fragment = new Postyour3Add();
                    break;
            }
            saveData(getApplicationContext(), "pcat_id", str);

            if (fragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString("MainCatType", str);
                bundle.putParcelableArrayList("imageSet", imageSet);
                fragment.setArguments(bundle);

                fm.beginTransaction().replace(R.id.allCategeriesIN, fragment).addToBackStack(null)
                        .setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit)
                        .commit();
            }
        }
    }


}
