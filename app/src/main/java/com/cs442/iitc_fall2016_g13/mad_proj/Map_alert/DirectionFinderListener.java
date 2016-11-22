package com.cs442.iitc_fall2016_g13.mad_proj.Map_alert;

import java.util.List;

/**
 * Created by KiranCD on 11/10/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
