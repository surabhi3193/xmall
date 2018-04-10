package com.mindinfo.xchangemall.xchangemall.activities.communityActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.mindinfo.xchangemall.xchangemall.R;

public class GroupChatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_screen);

        LinearLayout toolbar = (LinearLayout)findViewById(R.id.toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),GroupDetailsActivity.class));
            }
        });
    }
}
