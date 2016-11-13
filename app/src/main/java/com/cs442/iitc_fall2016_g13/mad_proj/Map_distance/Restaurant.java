package com.cs442.iitc_fall2016_g13.mad_proj.Map_distance;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by KiranCD on 11/11/2016.
 */

public class Restaurant {

    private String mPlaceName;
    private String mVicinity;
    private LatLng mLatLng;


    public Restaurant(String mPlaceName, String mVicinity, LatLng mLatLng) {
        this.mPlaceName = mPlaceName;
        this.mVicinity = mVicinity;
        this.mLatLng = mLatLng;
    }


    public String getmPlaceName() {
        return mPlaceName;
    }

    public void setmPlaceName(String mPlaceName) {
        this.mPlaceName = mPlaceName;
    }

    public String getmVicinity() {
        return mVicinity;
    }

    public void setmVicinity(String mVicinity) {
        this.mVicinity = mVicinity;
    }

    public LatLng getmLatLng() {
        return mLatLng;
    }

    public void setmLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }
}
