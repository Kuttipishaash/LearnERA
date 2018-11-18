package com.learnera.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learnera.app.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubjectPDFViewActivity extends AppCompatActivity {

    private WebView webView;
    private TextView txtview;
    private ProgressBar pbar;
    private Intent extras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_pdfview);

        txtview = findViewById(R.id.syllabus_loading_text_view);
        pbar = findViewById(R.id.syllabus_progress_bar);
        webView = findViewById(R.id.syllabus_web_view);

        extras = getIntent();

        showWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && pbar.getVisibility() == ProgressBar.GONE) {
                    pbar.setVisibility(ProgressBar.VISIBLE);
                    txtview.setVisibility(View.VISIBLE);
                }

                pbar.setProgress(progress);
                if (progress == 100) {
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

        if (extras.hasExtra("url")) {
            webView.loadUrl(extras.getStringExtra("url"));
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }

        super.onBackPressed();
    }

}
