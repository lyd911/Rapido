package com.cs442.iitc_fall2016_g13.mad_proj.Map_distance;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KiranCD on 11/11/2016.
 */

public class GetPlaceDetail extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    Location mSearchFromLocation;
    public static final String TAG = "GETPLACEDETAIL";
    private OnJsonParseCompleted listener;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d(TAG, "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            listener = (OnJsonParseCompleted) params[2];
            mSearchFromLocation = (Location) params[3];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d(TAG, "doInBackground Exit"+googlePlacesData);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute Entered "+result);
        List<HashMap<String, String>> nearbyPlacesList = null;
        JsonParser dataParser = new JsonParser();
        nearbyPlacesList =  dataParser.parse(result);
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
            String rating = googlePlace.get("rating");
            Double ratingVal =0.0;
            float distance = 0;

            if(rating.trim().length() > 0){

                 ratingVal = Double.valueOf(rating);
                Log.v(TAG," rating "+ratingVal+"rating String"+rating);


            }
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            builder.include(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            counter++;

            Location destination = new Location("");
            destination.setLongitude(lng);
            destination.setLatitude(lat);

            distance = mSearchFromLocation.distanceTo(destination);
            restList.add(new Restaurant(placeName,vicinity,latLng,ratingVal,distance));  // saving data
            Log.v(TAG," rating "+rating+"distance"+distance);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }




        MapView.getInstance().setmCount(counter);

        if(counter > 0){

            if(mSearchFromLocation != null){
                builder.include(new LatLng(mSearchFromLocation.getLatitude(),mSearchFromLocation.getLongitude()));

            }
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 60));

            MapView.getInstance().setmRestaurantList(restList);
            listener.onParseCompleted();

        }
    }
}
