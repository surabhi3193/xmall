package com.mindinfo.xchangemall.xchangemall.activities.showcaseActivities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mindinfo.xchangemall.xchangemall.R;

import static com.mindinfo.xchangemall.xchangemall.Fragments.categories.postADD.Postyour2Add.pageNo_textView;

public class CreateShowCase extends AppCompatActivity {

//    ArrayList<Integer> alImage;
//    ArrayList<String> alName;

//    ShowPostAdapter mAdapter;
   int btn_click=0;
int REQUEST_VIDEO_CAPTURE =01;
    LinearLayout first_page,second_page,third_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_show_case);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        TextView headtv = (TextView) findViewById(R.id.headtv);
        TextView titleTV = (TextView) findViewById(R.id.titleTV);
        ImageView back_btn = (ImageView) findViewById(R.id.back_btn);
        ImageView cross_btn = (ImageView) findViewById(R.id.cancel_btn);

        TextView livestreamvdo_headTV = (TextView) findViewById(R.id.livestreamvdo_headTV);
        TextView livestreamaudio_headTV = (TextView) findViewById(R.id.livestreamaudio_headTV);
        TextView recordVdo_headTV = (TextView) findViewById(R.id.recordVdo_headTV);
        TextView recordAudio_headTV = (TextView) findViewById(R.id.recordAudio_headTV);
        TextView upcomingTV = (TextView) findViewById(R.id.upcomingTV);

        TextView postTitle_headTV = (TextView) findViewById(R.id.postTitle_headTV);
        TextView cat_headTV = (TextView) findViewById(R.id.cat_headTV);
        TextView cat_TV = (TextView) findViewById(R.id.catTV);
        TextView startDate_headTV = (TextView) findViewById(R.id.startDate_headTV);
        TextView startTime_headTV = (TextView) findViewById(R.id.startTime_headTV);
        TextView endDate_headTV = (TextView) findViewById(R.id.endDate_headTV);
        TextView endTime_headTV = (TextView) findViewById(R.id.endTime_headTV);
        TextView postDesc_headTV = (TextView) findViewById(R.id.postDesc_headTV);

        EditText postTitleET = (EditText) findViewById(R.id.postTitleET);
        EditText startDateET = (EditText) findViewById(R.id.startDateET);
        EditText startTimeET = (EditText) findViewById(R.id.startTimeET);
        EditText endDateET = (EditText) findViewById(R.id.endDateET);
        EditText endTimeET = (EditText) findViewById(R.id.endTimeET);
        EditText postDescET = (EditText) findViewById(R.id.postDescET);

        TextView item_headTV = (TextView) findViewById(R.id.item_headTV);
        TextView privacy_headTV = (TextView) findViewById(R.id.privacy_headTV);
        TextView privacyTV = (TextView) findViewById(R.id.privacyTV);
        TextView phoneNumber_headTV = (TextView) findViewById(R.id.phoneNumber_headTV);
        EditText phoneEt = (EditText) findViewById(R.id.phoneET);
        TextView ext_headTV = (TextView) findViewById(R.id.ext_headTV);
        EditText extET = (EditText) findViewById(R.id.extET);
        TextView lang_headTV = (TextView) findViewById(R.id.lang_headTV);
        TextView langTV = (TextView) findViewById(R.id.langTV);
        TextView name_headTV = (TextView) findViewById(R.id.name_headTV);
        EditText nameET = (EditText) findViewById(R.id.nameET);

        Button next_btn = (Button)findViewById(R.id.next_btn);


         first_page = (LinearLayout) findViewById(R.id.first_page);
         second_page = (LinearLayout) findViewById(R.id.second_page);
         third_page = (LinearLayout) findViewById(R.id.third_page);

         if (first_page.getVisibility()==View.VISIBLE)
        pageNo_textView.setText("1 of 2 ");

         else     pageNo_textView.setText("2 of 2 ");

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/estre.ttf");

        pageNo_textView.setTypeface(face);
        headtv.setTypeface(face);
        titleTV.setTypeface(face);

        lang_headTV.setTypeface(face);
        ext_headTV.setTypeface(face);
        extET.setTypeface(face);
        item_headTV.setTypeface(face);
        langTV.setTypeface(face);

        name_headTV.setTypeface(face);
        nameET.setTypeface(face);
        next_btn.setTypeface(face);
        phoneNumber_headTV.setTypeface(face);
        phoneEt.setTypeface(face);
        privacy_headTV.setTypeface(face);
        privacyTV.setTypeface(face);
        recordAudio_headTV.setTypeface(face);
        recordVdo_headTV.setTypeface(face);
        upcomingTV.setTypeface(face);
        recordAudio_headTV.setTypeface(face);
        livestreamaudio_headTV.setTypeface(face);
        livestreamvdo_headTV.setTypeface(face);

        cat_headTV.setTypeface(face);
        endTime_headTV.setTypeface(face);
        endTimeET.setTypeface(face);
        startTimeET.setTypeface(face);
        startDateET.setTypeface(face);
        endDate_headTV.setTypeface(face);
        endDateET.setTypeface(face);
        startDate_headTV.setTypeface(face);
        startTime_headTV.setTypeface(face);

        postTitle_headTV.setTypeface(face);
        postDesc_headTV.setTypeface(face);
        postTitleET.setTypeface(face);
        postDescET.setTypeface(face);
        cat_TV.setTypeface(face);


//        alImage = new ArrayList<>(Arrays.asList(R.drawable.live_vdo, R.drawable.record_vdo, R.drawable.live_audio, R.drawable.upcoming_event, R.drawable.record_audio));
//        alName = new ArrayList<>(Arrays.asList("Live Streamed Video", "Recorded Video", "Live Streamed Audio", "Upcoming", "Recorded Audio"));

//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        mLayoutManager.setAutoMeasureEnabled(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);

//        mAdapter = new ShowPostAdapter(getApplicationContext(), alName, alImage);


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_click==0)
                {
                    btn_click=1;
                    pageNo_textView.setText("2 of 2 ");
                    second_page.setVisibility(View.VISIBLE);
                    first_page.setVisibility(View.GONE);
                }
                else
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                    startActivityForResult(cameraIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_click==0)
                {
                    onBackPressed();
                    overridePendingTransition(R.anim.push_down_out,R.anim.push_down_out);
                }
                else
                {
                    pageNo_textView.setText("1 of 2 ");
                    second_page.setVisibility(View.GONE);
                    first_page.setVisibility(View.VISIBLE);
                    btn_click=0;
                }
            }
        });

        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.push_down_out,R.anim.push_down_out);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                third_page.setVisibility(View.VISIBLE);
                second_page.setVisibility(View.GONE);
                first_page.setVisibility(View.GONE);

                System.out.println(videoUri);
                VideoView mVideoView = (VideoView) findViewById(R.id.vdo_view);
                mVideoView.setVideoURI(videoUri);

        }
    }
}
