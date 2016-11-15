package com.cs442.iitc_fall2016_g13.mad_proj.Map_distance;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by KiranCD on 11/11/2016.
 */
public class MapView {
    private static final String TAG = "MapView";
    private static MapView ourInstance = new MapView();

    public static MapView getInstance() {
        return ourInstance;
    }

    int index = 0;
    int mCount = 0;
    ArrayList<Restaurant> mRestaurantList = null;

    Integer[] mMapType = {GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_NORMAL, GoogleMap.MAP_TYPE_SATELLITE, GoogleMap.MAP_TYPE_TERRAIN};
    private MapView() {

    }

    int getNextMapView(){
        if(index >= mMapType.length) {
            Log.v(TAG,"index = "+index+" array length ="+mMapType.length);
            index = 0;
        }
        Log.v(TAG,"index = "+index+" array length ="+mMapType.length);
        return mMapType[index++];

    }

    public ArrayList<Restaurant> getmRestaurantList() {
        return mRestaurantList;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public void setmRestaurantList(ArrayList<Restaurant> mRestaurantList) {
        this.mRestaurantList = mRestaurantList;
    }
}
