package com.outerspace.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class DisplayTrailerActivity extends AppCompatActivity {
    private WebView webView;
    private String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_trailer);

        urlString = getIntent().getStringExtra(MovieDetailActivity.URL_STRING);
        webView = findViewById(R.id.web);
    }

    @Override
    protected void onStart() {
        super.onStart();
        webView.loadUrl(urlString);
    }
}
