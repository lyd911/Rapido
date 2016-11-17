package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.MenuAndCartActivity;

public class MenuWeb extends AppCompatActivity {
    private Button menuandcart,ZomatoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_web);

        menuandcart = (Button)findViewById(R.id.menu_rapido);
        ZomatoButton = (Button)findViewById(R.id.ZomatoButton);
        ZomatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(GlobalVariables.url));
                startActivity(intent);
            }
        });
        menuandcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuAndCartActivity.class); //Menu and Cart.class was launched here.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
