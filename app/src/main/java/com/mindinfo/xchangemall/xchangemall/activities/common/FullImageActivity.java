package com.mindinfo.xchangemall.xchangemall.activities.common;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.SliderAdapter2;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class FullImageActivity extends AppCompatActivity {
    private ViewPager mPager2;
    private CircleIndicator indicator2;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        mPager2 = findViewById(R.id.pager2);
        indicator2 = findViewById(R.id.indicator2);
        back_btn = findViewById(R.id.back_btn);


        back_btn.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<Uri> imageList = new ArrayList<>();
            imageList = bundle.getParcelableArrayList("imageArray");
            System.err.println("=== full image=======");
            System.err.println(imageList.get(0));
            init(imageList);
        }
    }

    private void init(final ArrayList<Uri> imageArray) {
        mPager2.setAdapter(new SliderAdapter2(FullImageActivity.this, imageArray, imageArray.size()));
        mPager2.setBackgroundColor(getResources().getColor(R.color.black));
        indicator2.setViewPager(mPager2);
    }
}
