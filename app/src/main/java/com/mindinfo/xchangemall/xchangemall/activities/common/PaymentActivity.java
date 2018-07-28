package com.mindinfo.xchangemall.xchangemall.activities.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
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
        ImageView back_arrowImage =findViewById(R.id.back_arrowImage);

        TextView cvvHEaderTV =findViewById(R.id.cvv_header);
        TextView details_headerTV =findViewById(R.id.details_header);
        TextView paypal_headerTV =findViewById(R.id.paypal_header);

        TextView accept_headerTV =findViewById(R.id.accept_header);
        TextView number_headertv =findViewById(R.id.number_header);
        TextView expiryHEaderTV =findViewById(R.id.expiry_header);
        TextView nameHeaderTV =findViewById(R.id.name_header);

        EditText  cvvEt =findViewById(R.id.cvvET);
        EditText  numberET =findViewById(R.id.cardnumberET);
        EditText  expiryET =findViewById(R.id.expiryET);
        EditText  nameET =findViewById(R.id.nameET);


       Button pay_btn =findViewById(R.id.pay_btn);

        Typeface face = ResourcesCompat.getFont(PaymentActivity.this, R.font.estre);
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
        
        pay_btn.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext()," Payment Done ! " ,Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        back_arrowImage.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
    }
}
