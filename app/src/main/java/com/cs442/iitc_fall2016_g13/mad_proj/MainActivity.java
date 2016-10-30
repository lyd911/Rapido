package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView text1, text2, text3, text4;
    Button mBtnQRGenerate;
    Button mBtnQRReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.font_test_1);
        text2 = (TextView) findViewById(R.id.font_test_2);
        text3 = (TextView) findViewById(R.id.font_test_3);
        text4 = (TextView) findViewById(R.id.font_test_4);


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


        Typeface questrial_regular = Typeface.createFromAsset(getAssets(), "fonts/questrial_regular.ttf");
        Typeface quicksand_bold = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.ttf");
        Typeface quicksand_light = Typeface.createFromAsset(getAssets(), "fonts/quicksand_light.ttf");
        Typeface quicksand_regular = Typeface.createFromAsset(getAssets(), "fonts/quicksand_regular.ttf");

        text1.setTypeface(questrial_regular);
        text2.setTypeface(quicksand_bold);
        text3.setTypeface(quicksand_light);
        text4.setTypeface(quicksand_regular);
    }
}
