package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.Map_alert.MapsActivity_direction;
import com.cs442.iitc_fall2016_g13.mad_proj.Map_distance.MapsActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

// demo
public class TypographyActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;

    TextView mQuestrialText,
             mQuicksandBText,
             mQuicksandLText,
             mQuicksandRText,
             mGetPlace;

    Button mBtnQRGenerate;
    Button mBtnQRReader;
    Button mBtnOpenMaps;
    Button mBtnOpenMapsDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typography);

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

        mBtnOpenMapsDirection = (Button) findViewById(R.id.btn_map_direction);


        mBtnOpenMapsDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity_direction.class));
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

                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(TypographyActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                final Place place = PlacePicker.getPlace(data,this);
                String address = String.format("Place: %s", place.getAddress());
                mGetPlace.setText(address);
            }
        }
    }
}
