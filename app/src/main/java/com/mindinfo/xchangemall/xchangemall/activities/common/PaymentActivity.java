package com.mindinfo.xchangemall.xchangemall.activities.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindinfo.xchangemall.xchangemall.R;
import com.mindinfo.xchangemall.xchangemall.activities.main.MainActivity;

public class PaymentActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ImageView back_arrowImage =(ImageView) findViewById(R.id.back_arrowImage);

        TextView cvvHEaderTV = (TextView) findViewById(R.id.cvv_header);
        TextView details_headerTV = (TextView) findViewById(R.id.details_header);
        TextView paypal_headerTV = (TextView) findViewById(R.id.paypal_header);

        TextView accept_headerTV = (TextView) findViewById(R.id.accept_header);
        TextView number_headertv = (TextView) findViewById(R.id.number_header);
        TextView expiryHEaderTV = (TextView) findViewById(R.id.expiry_header);
        TextView nameHeaderTV = (TextView) findViewById(R.id.name_header);

        EditText  cvvEt = (EditText) findViewById(R.id.cvvET);
        EditText  numberET = (EditText) findViewById(R.id.cardnumberET);
        EditText  expiryET = (EditText) findViewById(R.id.expiryET);
        EditText  nameET = (EditText) findViewById(R.id.nameET);


       Button pay_btn = (Button) findViewById(R.id.pay_btn);

        Typeface face = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/estre.ttf");
        details_headerTV.setTypeface(face);
        paypal_headerTV.setTypeface(face);
        accept_headerTV.setTypeface(face);
        number_headertv.setTypeface(face);
        cvvHEaderTV.setTypeface(face);
        expiryHEaderTV.setTypeface(face);
        nameHeaderTV.setTypeface(face);
        pay_btn.setTypeface(face);

        cvvEt.setTypeface(face);
        numberET.setTypeface(face);
        expiryET.setTypeface(face);
        nameET.setTypeface(face);


        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext()," Payment Done ! " ,Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        back_arrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}
