package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mQuestrialText,
             mQuicksandBText,
             mQuicksandLText,
             mQuicksandRText,
             mGetPlace;

    Button mBtnQRGenerate;
    Button mBtnQRReader;
    Button mBtnOpenMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestrialText = (TextView) findViewById(R.id.font_test_1);
        mQuicksandBText = (TextView) findViewById(R.id.font_test_2);
        mQuicksandLText = (TextView) findViewById(R.id.font_test_3);
        mQuicksandRText = (TextView) findViewById(R.id.font_test_4);


        mBtnQRGenerate = (Button) findViewById(R.id.btn_QRCodeGenerator);
        mBtnQRReader = (Button) findViewById(R.id.btn_QRCodeReader);
        mBtnQRGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QRCodeGenerator.class));
            }
        });

        mBtnQRReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QRCodeReader.class));
            }
        });

        mBtnOpenMaps = (Button) findViewById(R.id.btn_OpenMaps);
        mBtnOpenMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        Typeface questrial_regular = Typeface.createFromAsset(getAssets(), "fonts/questrial_regular.ttf");
        Typeface quicksand_bold = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.ttf");
        Typeface quicksand_light = Typeface.createFromAsset(getAssets(), "fonts/quicksand_light.ttf");
        Typeface quicksand_regular = Typeface.createFromAsset(getAssets(), "fonts/quicksand_regular.ttf");

        mQuestrialText.setTypeface(questrial_regular);
        mQuicksandBText.setTypeface(quicksand_bold);
        mQuicksandLText.setTypeface(quicksand_light);
        mQuicksandRText.setTypeface(quicksand_regular);

        mGetPlace = (TextView) findViewById(R.id.tv_ResturantInfo);
        mGetPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
