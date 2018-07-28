package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.adapter.MessageAdapter;
import com.mindinfo.xchangemall.xchangemall.beans.Message;

import java.util.ArrayList;

public class MessageBoxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box);

        ImageView btn_send = findViewById(R.id.btn_send);
        ImageView iv_sender_pic = findViewById(R.id.iv_sender_pic);
        TextView textview_sender_name = findViewById(R.id.textview_sender_name);
        EditText edittext_message = findViewById(R.id.edittext_message);
        recyclerView = findViewById(R.id.recycler_view_msg);

        String sender_name = "TestUser";
        int sender_pic = R.drawable.car_image;
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            sender_name = bundle.getString("sender_name");
            sender_pic = bundle.getInt("sender_pic");
            textview_sender_name.setText(sender_name);

                    Glide.with(MessageBoxActivity.this).load(sender_pic).apply(RequestOptions
                    .placeholderOf(R.drawable.profile_bg)).into(iv_sender_pic);

        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MessageBoxActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Message> developerdata = new ArrayList<>();
        Message developer = new Message("1", "Hello "+sender_name, R.drawable.profile, "",true);
        Message developer2 = new Message("2", "Hello Surabhi ",sender_pic, "",false);
        Message developer3 = new Message("3", "How are you ? ", R.drawable.profile, "",true);
        Message developer4 = new Message("4", " M good , How are you ?",sender_pic,"",false);
        developerdata.add(developer);
        developerdata.add(developer2);
        developerdata.add(developer3);
        developerdata.add(developer4);


        MessageAdapter mAdapter = new MessageAdapter(developerdata, MessageBoxActivity.this);

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--- cliked send---");
                String str_message = edittext_message.getText().toString();
                if (str_message.length()>0)
                {
                    System.out.println("--- send my message ---- " + str_message);
                    developerdata.add(new Message("1", str_message, R.drawable.profile, "",true));
                    mAdapter.notifyDataSetChanged();
                    edittext_message.setText("");
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);



                }
            }
        });

     edittext_message.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);

         }
         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {

         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });
    }
}
