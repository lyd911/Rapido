package com.cs442.iitc_fall2016_g13.mad_proj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

public class MenuWeb extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_web);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(GlobalVariables.url);
    }
}
