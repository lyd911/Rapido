package com.cs442.iitc_fall2016_g13.mad_proj;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView text1, text2, text3, text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.font_test_1);
        text2 = (TextView) findViewById(R.id.font_test_2);
        text3 = (TextView) findViewById(R.id.font_test_3);
        text4 = (TextView) findViewById(R.id.font_test_4);

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
