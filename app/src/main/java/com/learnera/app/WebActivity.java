package com.learnera.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learnera.app.data.Constants;

/**
 * Created by praji on 12/31/2017.
 */

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private TextView txtview;
    private ProgressBar pbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_rset_web);

        txtview = (TextView)findViewById(R.id.text_web_load);
        pbar = (ProgressBar) findViewById(R.id.progress_bar_web);;
        webView = (WebView) findViewById(R.id.layout_web_view);

        Intent extras = getIntent();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && pbar.getVisibility() == ProgressBar.GONE){
                    pbar.setVisibility(ProgressBar.VISIBLE);
                    txtview.setVisibility(View.VISIBLE);
                }

                pbar.setProgress(progress);
                if(progress == 100) {
                    pbar.setVisibility(ProgressBar.GONE);
                    txtview.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if(extras.hasExtra("url")) {
            webView.loadUrl(extras.getStringExtra("url"));
        }

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
            return;
        }

        super.onBackPressed();
    }
}
