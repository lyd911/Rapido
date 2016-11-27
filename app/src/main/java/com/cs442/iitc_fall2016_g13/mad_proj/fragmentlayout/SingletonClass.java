package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cs442.iitc_fall2016_g13.mad_proj.Dynamic_menu_update.MenuItems;

import java.util.ArrayList;
import java.util.Set;

import static com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.billingactivity.PREFS_NAME;

/**
 * Created by KiranCD on 10/2/2016.
 */

public class SingletonClass extends Application{


    private static final String TAG = "SingletonClass";
    private static SingletonClass instance;
    private static ArrayList<MenuItemObject>  mArrayList;
    private static ArrayList<MenuItemObject>  mBillingArrayList;
    private static ArrayList<String> mHistoryArray;

    public static Double getmLong() {

        Log.v(TAG,"getmLong"+mLong);
        return mLong;
    }

    public static void setmLong(Double mLong) {
        Log.v(TAG,"setmLong  :"+mLong);
        SingletonClass.mLong = mLong;
    }

    public static Double getmLat() {
        Log.v(TAG,"getmLat  :"+mLat);
        return mLat;
    }

    public static void setmLat(Double mLat) {

        Log.v(TAG,"setmLat  :"+mLat);
        SingletonClass.mLat = mLat;
    }

    private static Double mLong, mLat;
    private static Double mTotalBill;



    private static Context mContext;


    public static SingletonClass initInstance(Context context)
    {
        if (instance == null)
        {
            mContext = context;
            instance = new SingletonClass();
            mTotalBill = 0.0;
        }
        return instance;
    }

    private SingletonClass() {
        mArrayList = new ArrayList<>();

    }


    public static void updateArray(ArrayList<MenuItems> menuItemsarray){

        mArrayList.clear();

        for(MenuItems menutItemsIttr:menuItemsarray)
        mArrayList.add(new MenuItemObject(menutItemsIttr.getmMenuName(),menutItemsIttr.getmPrice(),menutItemsIttr.getmMenuDescription()));


    }

    public static ArrayList<String> getmHistoryArray() {
/*
        if(mHistoryArray == null){
            mHistoryArray = new ArrayList<>();
        }*/


        if(mHistoryArray == null) {

        SharedPreferences pref = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
        Set<String> historyStringSet = pref.getStringSet("HISTORY", null);

            if(historyStringSet == null){
                mHistoryArray = new ArrayList<>();
            }else{
                mHistoryArray = new ArrayList<String>(historyStringSet);
            }
        }

        return mHistoryArray;
    }

    public static void setmHistoryArray(ArrayList<String> historyArray){


        mHistoryArray = historyArray;

    }


    public ArrayList<MenuItemObject> getListArray(){

        return mArrayList;
    }

    public ArrayList<MenuItemObject> getBillingArrayList(){


        if(mBillingArrayList == null){
            mBillingArrayList = new ArrayList<>();
        }
        return mBillingArrayList;
    }

    public void setmTotalBill(Double val){

        mTotalBill = val;

    }

    public Double getmTotalBill(){

        return mTotalBill;
    }

}
