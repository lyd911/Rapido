package com.cs442.iitc_fall2016_g13.mad_proj.Seller.Dynamic_menu_update;

/**
 * Created by KiranCD on 11/17/2016.
 */

public class MenuItems {

    String mMenuName;
    String mPrice;
    String mMenuDescription;

    public MenuItems(String mMenuName, String mPrice, String mMenuDescription) {
        this.mMenuName = mMenuName;
        this.mPrice = mPrice;
        this.mMenuDescription = mMenuDescription;
    }

    public String getmMenuName() {
        return mMenuName;
    }

    public void setmMenuName(String mMenuName) {
        this.mMenuName = mMenuName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmMenuDescription() {
        return mMenuDescription;
    }

    public void setmMenuDescription(String mMenuDescription) {
        this.mMenuDescription = mMenuDescription;
    }
}
