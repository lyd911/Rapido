package com.cs442.iitc_fall2016_g13.mad_proj;

/**
 * Created by KiranCD on 10/1/2016.
 */

public class MenuItemObject {

    private String mItemName;
    private String mItemPrice;
    private String mItemDetail;
    private int mOrderCount;
    private Double mTotalBill;

    public MenuItemObject(String mItemName, String mItemPrice, String mItemDetail ) {
        this.mItemName = mItemName;
        this.mItemPrice = mItemPrice;
        this.mItemDetail = mItemDetail;
        this.mOrderCount = 0;

    }

    public MenuItemObject(String mItemName, String mItemPrice, String mItemDetail, int mOrderCount ) {
        this.mItemName = mItemName;
        this.mItemPrice = mItemPrice;
        this.mItemDetail = mItemDetail;
        this.mOrderCount = mOrderCount;

    }

    public String getmItemName() {
        return mItemName;
    }

    public void setmItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getmItemPrice() {
        return mItemPrice;
    }

    public void setmItemPrice(String mItemPrice) {
        this.mItemPrice = mItemPrice;
    }

    public String getmItemDetail() {
        return mItemDetail;
    }

    public void setmItemDetail(String mItemDetail) {
        this.mItemDetail = mItemDetail;
    }

    public int getmOrderCount(){return mOrderCount;}

    public void setmOrderCount(int mOrderCount) { this.mOrderCount = mOrderCount; }
}
