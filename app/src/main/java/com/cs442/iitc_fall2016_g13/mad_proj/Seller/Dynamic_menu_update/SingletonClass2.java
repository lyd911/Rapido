package com.cs442.iitc_fall2016_g13.mad_proj.Seller.Dynamic_menu_update;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cs442.iitc_fall2016_g13.mad_proj.Seller.Dynamic_menu_update.MenuItems;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.MenuItemObject;

import java.util.ArrayList;
import java.util.Set;

import static com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.billingactivity.PREFS_NAME;

/**
 * Created by KiranCD on 10/2/2016.
 */

public class SingletonClass2 extends Application{


    private static SingletonClass2 instance;
    private static ArrayList<MenuItemObject>  mArrayList;
    private static ArrayList<MenuItemObject>  mBillingArrayList;
    private static ArrayList<String> mHistoryArray;

    private static Double mTotalBill;



    private static Context mContext;


    public static SingletonClass2 initInstance(Context context)
    {
        if (instance == null)
        {
            mContext = context;
            instance = new SingletonClass2();
            mTotalBill = 0.0;
        }
        return instance;
    }

    private SingletonClass2() {
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
