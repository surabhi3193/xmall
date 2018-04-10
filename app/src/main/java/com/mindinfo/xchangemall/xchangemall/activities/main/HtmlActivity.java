package com.mindinfo.xchangemall.xchangemall.activities.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;

public class HtmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

      TextView  headerTv = (TextView) findViewById(R.id.headerTv);
        WebView webView = (WebView)findViewById(R.id.webview);
     LinearLayout   back_btn = (LinearLayout) findViewById(R.id.back_arrowImage);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/estre.ttf");
        headerTv.setTypeface(face);

        Bundle bundle =getIntent().getExtras();
        if (bundle !=null)
        {
            String header =bundle.getString("header_name");
            headerTv.setText(header);

            if (header.equals("About us"))
                webView.loadUrl("file:///android_asset/About us.html");
            else if (header.equals("Terms and Condition"))
                webView.loadUrl("file:///android_asset/Terms and conditions.html");


            else
                webView.loadUrl("file:///android_asset/Privacy policy.html");

        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}
