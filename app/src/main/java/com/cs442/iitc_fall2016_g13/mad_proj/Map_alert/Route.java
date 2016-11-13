package com.cs442.iitc_fall2016_g13.mad_proj.Map_alert;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by KiranCD on 11/10/2016.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}