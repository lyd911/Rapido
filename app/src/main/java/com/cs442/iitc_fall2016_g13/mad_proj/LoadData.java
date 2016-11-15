package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.Zomato.FetchNearbyPlaces;
import com.cs442.iitc_fall2016_g13.mad_proj.Zomato.GoogleZomatoFetch;

public class LoadData extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        System.out.println("Load Data started");
       new GoogleZomatoFetch(this).execute(GlobalVariables.SelectedRestaurantName);
    }


}
