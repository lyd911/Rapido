package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.Zomato.FetchNearbyPlaces;

import org.w3c.dom.Text;

public class NearbyPlaces extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext(),NearbyPlaces.this)) {
            fetchLocationData();
        }
        else
        {
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,PERMISSION_REQUEST_CODE_LOCATION,getApplicationContext(),NearbyPlaces.this);
        }

        
    }

    public static void requestPermission(String strPermission,int perCode,Context _c,Activity _a){

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
            //Toast.makeText(,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
        }
    }

    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }

    private void fetchLocationData()
    {
        GPSTracker gps = new GPSTracker(NearbyPlaces.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
           // test.setText("Lat - "+latitude+"\n long :"+longitude);
            new FetchNearbyPlaces(this).execute(latitude+"",""+longitude);

            // \n is for new line
           // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }//code to use the granted permission (location)
    }

}
