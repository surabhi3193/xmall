package com.mindinfo.xchangemall.xchangemall.activities.news;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindinfo.xchangemall.xchangemall.R;

public class NewsDetails extends AppCompatActivity {
    private  String fragment_name,url;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

      ImageView back_btn =findViewById(R.id.back_arrowImage);
      TextView title_TV =findViewById(R.id.title_TV);
        WebView webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progress_bar);
        Bundle bundle = getIntent().getExtras();

        if (bundle!=null) {
            fragment_name = bundle.getString("fragment_name", "");
            url = bundle.getString("url", "");
        }

        if (fragment_name.equalsIgnoreCase("news"))
        {
            title_TV.setText(R.string.news);
        }

        else if (fragment_name.equalsIgnoreCase("games"))
        {
            title_TV.setText(R.string.games);
        }

        webView.setWebViewClient(new myWebClient());

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);

        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        System.out.println("-------- url to load ----------- " );
        System.out.println(url);
        webView.loadUrl(url);
        back_btn.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }
}
