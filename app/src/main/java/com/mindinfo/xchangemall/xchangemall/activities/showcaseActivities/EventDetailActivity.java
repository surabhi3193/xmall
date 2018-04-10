package com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mindinfo.xchangemall.xchangemall.R;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        ImageView item_image = (ImageView)findViewById(R.id.item_image);

        int itemimage = getIntent().getExtras().getInt("item_image",R.drawable.live1);

        item_image.setBackgroundResource(itemimage);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
