package com.cs442.iitc_fall2016_g13.mad_proj;

/**
 * Created by KiranCD on 12/10/2016.
 */


import android.view.MotionEvent;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;

/**
 * Basic implementation of {@link OnShowcaseEventListener} which does nothing
 * for each event, but can be override for each one.
 */
public class SimpleShowcaseEventListener implements OnShowcaseEventListener {
    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        // Override to do stuff
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
        // Override to do stuff
    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {
        // Override to do stuff
    }


    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {
        // Override to do stuff
    }
}
