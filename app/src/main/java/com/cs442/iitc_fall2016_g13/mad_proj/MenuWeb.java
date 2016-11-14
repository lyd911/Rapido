package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.cs442.iitc_fall2016_g13.mad_proj.Map_distance.MapsActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

public class MenuWeb extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_web);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.com_facebook_close));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent); //What to do on back clicked*/
                finish();
            }
        });

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(GlobalVariables.url);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent); //What to do on back clicked
        */
        finish();
    }
}
