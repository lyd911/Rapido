package com.cs442.iitc_fall2016_g13.mad_proj;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.SignUpActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.Zomato.FetchNearbyPlaces;
import com.google.android.gms.location.places.ui.PlacePicker;


import java.util.ArrayList;
import java.util.List;

public class NearbyPlaces extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Retrieve the PlaceAutocompleteFragment.
        //PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
        //        getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        //Set the fragment initially
        //MenuFragment fragment = new MenuFragment();
        //android.support.v4.app.FragmentTransaction fragmentTransaction =
        //        getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.fragment_container, fragment);
        //fragmentTransaction.commit();

        System.out.println("Started Nearby Places");
        setTitle("Nearby Places");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        RestaurantAdapter ca = new RestaurantAdapter(createList(GlobalVariables.res_data.length));
        recList.setAdapter(ca);

    }


    //I would need to populate this.
    private List<RestaurantInfo> createList(int size) {

        List<RestaurantInfo> result = new ArrayList<RestaurantInfo>();
        for (int i=1; i < size; i++) {
            RestaurantInfo ri = new RestaurantInfo();
            ri.mName = GlobalVariables.res_data[i][0];
            ri.mCuisine = GlobalVariables.res_data[i][1];
            ri.mBusyness = RestaurantInfo.BUSYNESS_PREFIX + i;
            ri.mDistance = RestaurantInfo.DISTANCE_PREFIX + i + " miles";
            ri.mMinSpend = RestaurantInfo.MINSPEND_PREFIX + i;
            ri.mRating = GlobalVariables.res_data[i][2];
            ri.mReview = RestaurantInfo.REVIEW_PREFIX + i;
            ri.mPriceRange = GlobalVariables.res_data[i][3];

            result.add(ri);

        }

        return result;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent i = new Intent(getApplicationContext(), OrderHistory.class);
            startActivity(i);
            // Handle the camera action

            //MenuFragment fragment = new MenuFragment();
            //android.support.v4.app.FragmentTransaction fragmentTransaction =
            //        getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {
            //CartFragment fragment = new CartFragment();
            //android.support.v4.app.FragmentTransaction fragmentTransaction =
            //        getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
