package com.cs442.iitc_fall2016_g13.mad_proj.Map_distance;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.LoadData;
import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.Zomato.GoogleZomatoFetch;
import com.google.android.gms.common.ConnectionResult;
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

public class MapsActivity extends FragmentActivity implements
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext = this;

        checkForGPSPermission();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       // finish();

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
                if (mETxtDistanceInput != null && mETxtDistanceInput.getText().toString().trim() != null) {

                    int distance = Integer.parseInt(mETxtDistanceInput.getText().toString());
                    Log.v(TAG, "distance +" + distance);
                    url = getUrl(mLatitude, mLongitude, Restaurant, distance);
                    Log.v(TAG, "Url " + url);

                } else {
                    Log.v(TAG, "No text entered");
                    Toast.makeText(MapsActivity.this, "Enter valid Number", Toast.LENGTH_LONG).show();
                    return;

                }
                mMap.clear();
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

        Button btnChangeMapView = (Button) findViewById(R.id.btn_changeView);
        btnChangeMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(MapView.getInstance().getNextMapView());
            }
        });


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

            final ListView mListView = (ListView) findViewById(R.id.myListView);
            ArrayList<Restaurant> arrayList = new ArrayList(Arrays.asList(stringArray));
            mArrayAdapter = new CustomAdapter(getApplicationContext(), R.layout.restraunt_list_layout, restList);
            mListView.setAdapter(mArrayAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Value of i"+position);
                    System.out.println("Value of i"+restList.get(position).getmPlaceName());
                    GlobalVariables.SelectedRestaurantName=restList.get(position).getmPlaceName();
                    Intent intent = new Intent(view.getContext(), LoadData.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });
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

}
