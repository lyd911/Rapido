package com.cs442.iitc_fall2016_g13.mad_proj.Map_distance;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.cs442.iitc_fall2016_g13.mad_proj.Dynamic_menu_update.MainActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.Seller.SellerMainActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.LoginActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.CustomerOrderHistoryActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.LoginActivity.PREFS_NAME;

public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnJsonParseCompleted {

    public static final String TAG = "MAPSACTIVITY";
    private GoogleMap mMap;
    LocationManager mLocationManager;
    GoogleApiClient mGoogleApiClient;
    double mLatitude;
    double mLongitude;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    Location mLastLocation;
    Context mContext;

    ListView mListView;
    CustomAdapter mArrayAdapter;
    String[] stringArray = {"kiran 1", "kiran 2"};
    private int REQUEST_CODE_ASK_PERMISSIONS_LOC=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mContext = this;


       if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        checkForGPSPermission();

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextView drawer_name=(TextView)findViewById(R.id.drawer_name);
        drawer_name.setText("RAPIDO - "+GlobalVariables.username);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_changeMapView) {
            mMap.setMapType(MapView.getInstance().getNextMapView());
            return true;
        }

        if(id == R.id.action_logout){
            Toast.makeText(MapsActivity.this, "action_logout", Toast.LENGTH_LONG).show();
            SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            // Writing data to SharedPreferences
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);

            return true;

        }

        if(id == R.id.action_sortByDistance){
            Toast.makeText(MapsActivity.this, "action_sortByDistance", Toast.LENGTH_LONG).show();
            updateListViewOrderByDistance();
            return true;

        }


        if(id == R.id.action_sortByRating){
            Toast.makeText(MapsActivity.this, "action_sortByRating", Toast.LENGTH_LONG).show();
            updateListViewOrderByRating();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.history) {
            Intent i = new Intent(this, CustomerOrderHistoryActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.log_out) {
            SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            // Writing data to SharedPreferences
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {

                checkForGPSPermission();


            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        final EditText mETxtDistanceInput = (EditText) findViewById(R.id.edtTxtDistanceInput);

        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            String Restaurant = "restaurant";

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button is Clicked");

                String url;
                if (mETxtDistanceInput != null && mETxtDistanceInput.getText().toString().trim().length() > 0) {


                    int distance = Integer.parseInt(mETxtDistanceInput.getText().toString());
                    if(distance > 15000 || distance <= 0){
                        Toast.makeText(MapsActivity.this, "Distance should be between 0 and 15000", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.v(TAG, "distance +" + distance);
                    url = getUrl(mLatitude, mLongitude, Restaurant, distance);
                    Log.v(TAG, "Url " + url);

                } else {
                    Log.v(TAG, "No text entered");
                    Toast.makeText(MapsActivity.this, "Enter valid Number", Toast.LENGTH_LONG).show();
                    return;

                }
                mMap.clear();
                Location mylocation = new Location("");
                mylocation.setLatitude(mLatitude);
                mylocation.setLongitude(mLongitude);
                Object[] DataTransfer = new Object[4];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                DataTransfer[2] = mContext;
                DataTransfer[3] = mLastLocation;
                Log.d("onClick", url);
                GetPlaceDetail getNearbyPlacesData = new GetPlaceDetail();
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(MapsActivity.this, "Nearby Restaurants", Toast.LENGTH_LONG).show();
            }
        });

        // shifted this to menu
      /*
        Button btnChangeMapView = (Button) findViewById(R.id.btn_changeView);
        btnChangeMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(MapView.getInstance().getNextMapView());
            }
        });
 Button btnSortByDistance = (Button) findViewById(R.id.btn_sortDistance);

        btnSortByDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateListViewOrderByDistance();
            }
        });


        Button btnSortByRating = (Button) findViewById(R.id.btn_sortRating);

        btnSortByRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateListViewOrderByRating();
            }
        });

*/

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    void checkForGPSPermission() {

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertBilderToEnableGPS();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(MapsActivity.this, "No permission ACCESS_FINE_LOCATION", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }

    void showAlertBilderToEnableGPS() {
        Toast.makeText(MapsActivity.this, "showAlertBilderToEnableGPS", Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setMessage("GPS is disabled in your device. Enable it?\nPressing cancel will close the application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }


    private String getUrl(double latitude, double longitude, String nearbyPlace, int distance) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + distance);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&rankBy=RankBy.DISTANCE");
        googlePlacesUrl.append("&key=" + getString(R.string.google_maps_key));
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(MapsActivity.this, "Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", mLatitude, mLongitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }

    void fillListView() {

        int count = MapView.getInstance().getmCount();
        final ArrayList<Restaurant> restList = MapView.getInstance().getmRestaurantList();

        if (count > 0 && restList != null) {

            ListView mListView = (ListView) findViewById(R.id.myListView);
            mArrayAdapter = new CustomAdapter(getApplicationContext(), R.layout.restraunt_list_layout, restList);
            mListView.setAdapter(mArrayAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Value of i"+position);
                    System.out.println("Value of i"+restList.get(position).getmPlaceName());
                    GlobalVariables.SelectedRestaurantName=restList.get(position).getmPlaceName();
                    GlobalVariables.SellerUsername=restList.get(position).getmPlaceName();
                    LatLng currentPlaceLatlng = restList.get(position).getmLatLng();

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra("LAT",currentPlaceLatlng.latitude);
                    intent.putExtra("LON",currentPlaceLatlng.longitude);
                    intent.putExtra("restaurantName",restList.get(position).getmPlaceName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                 //   new GoogleZomatoFetch(mContext).execute(GlobalVariables.SelectedRestaurantName,""+mLatitude,""+mLongitude);

                    //Intent intent = new Intent(view.getContext(), LoadData.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //view.getContext().startActivity(intent);

                }
            });
        }
    }

    void updateListViewOrderByDistance(){


        if(mArrayAdapter != null){
            mArrayAdapter.sortByDistance();
        }


    }



    void updateListViewOrderByRating(){


        if(mArrayAdapter != null){
            mArrayAdapter.sortByRating();
        }


    }

    @Override
    public void onParseCompleted() {
        fillListView();
    }

/*

    public class GetPlaceDetail extends AsyncTask<Object, String, String> {

        String googlePlacesData;
        GoogleMap mMap;
        String url;
        Context mContext;
        public static final String TAG = "GETPLACEDETAIL";

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "doInBackground entered"+url);
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
                mContext = (Context) params[2];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesData = downloadUrl.readUrl(url);
                Log.d(TAG, "doInBackground Exit");
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute Entered");
            List<HashMap<String, String>> nearbyPlacesList = null;
            JsonParser dataParser = new JsonParser();
            nearbyPlacesList = dataParser.parse(result);
            ShowNearbyPlaces(nearbyPlacesList);
            Log.d(TAG, "onPostExecute Exit");
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            int counter = 0;
            ArrayList<Restaurant> restList = new ArrayList<>();

            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                Log.d(TAG,"Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                builder.include(latLng);
                markerOptions.title(placeName + " : " + vicinity);


                restList.add(new Restaurant(placeName,vicinity,latLng));

                counter++;
                mMap.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }



            if(counter > 0){

                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 60));
                */
/*
                MapView.getInstance().setmRestaurantList(restList);

                ListView mListView = (ListView) findViewById(R.id.myListView);
                ArrayList<Restaurant> arrayList =  new ArrayList(Arrays.asList(stringArray));

                mArrayAdapter = new CustomAdapter(getApplicationContext(),R.layout.restraunt_list_layout,arrayList);
                mListView.setAdapter(mArrayAdapter);
*//*



            }
        }
    }
*/

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


}
