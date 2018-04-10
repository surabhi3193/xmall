package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mindinfo.xchangemall.xchangemall.R;

public class EnterLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_login);
    }

    public void openRegistration(View view)
    {
        Intent intent=new Intent(this,SIgnUp.class);
        intent.putExtra("open_logine_registration","reg");
        startActivity(intent);
        finish();
    }

    public void openLogin(View view)
    {
        Intent intent=new Intent(this,Login.class);
        intent.putExtra("open_logine_registration","login");
        startActivity(intent);
        finish();
    }

    public void skip(View view)
    {
        Intent intent=new Intent(this,MainActivity
                .class);
        intent.putExtra("open_logine_registration","login");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//
//        if (getIntent().getBooleanExtra("EXIT", false)) {
//            finishAffinity();
//            finish();
//        }

        finishAffinity();
        finish();
    }
}
