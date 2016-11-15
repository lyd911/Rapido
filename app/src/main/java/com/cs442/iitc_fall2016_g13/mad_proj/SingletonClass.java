package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

import static com.cs442.kdakshinamurthy.fragmentlayout.billingactivity.PREFS_NAME;

/**
 * Created by KiranCD on 10/2/2016.
 */

public class SingletonClass extends Application{


    private static SingletonClass instance;
    private static ArrayList<MenuItemObject>  mArrayList;
    private static ArrayList<MenuItemObject>  mBillingArrayList;
    private static ArrayList<String> mHistoryArray;

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
        mArrayList.add(new MenuItemObject("Foiled Fish  ","10","The filet is herb-crusted, and then cooked in foil with lemon and butter. Served with campfire roasted local squash and corn"));
        mArrayList.add(new MenuItemObject("Veggie Medley","20","Campfire grilled Portobello mushrooms, locally grown squash, peppers, onions, and cherry tomatoes, served over seasoned rice."));
        mArrayList.add(new MenuItemObject(" Red Rice","30","Corn, black beans, and garbanzos in a blend of rice and salsa, accented by cilantro. Partnered with Tex-Mex mashers."));
        mArrayList.add(new MenuItemObject("Stir-Fry","40","Carrots, broccoli, cauliflower, peppers, onion,and snap peas sautéed in sesame oil over Teriyaki rice."));
        mArrayList.add(new MenuItemObject("Pot Roast","50","Baked with carrots and potatoes. Topped with gravy made from the drippings, accompanied by a side salad."));
        mArrayList.add(new MenuItemObject("Beef Stew","60"," Chunks of seasoned beef, browned, and then cooked with potatoes, carrots, corn, onion, and peas in a full-flavored brown sauce."));

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
