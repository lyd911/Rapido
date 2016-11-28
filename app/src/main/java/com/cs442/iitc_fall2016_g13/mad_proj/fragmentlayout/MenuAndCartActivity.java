package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import java.util.ArrayList;

public class MenuAndCartActivity extends AppCompatActivity implements TitleFragment.historyButtonClicked, TitleFragment.onCountUpdated, TitleFragment.OnListItemClicked, TitleFragmentDetail.onBackKeyPressed, TitleFragment.onOrder, TitleFragmentDetail.onCountChanged {


    final static String TAG = "MenuAndCartActivity";
    final static String ARG_POSITION = "savedBundle";

    final static int RESULT_CODE = 3;
    ArrayList<MenuItemObject> mArrayList;
    int mPositionForDfragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_and_menu);

        if (savedInstanceState != null) {
            mPositionForDfragment = savedInstanceState.getInt(ARG_POSITION);
            Log.v(TAG, "savedPosition" + mPositionForDfragment);

        }


        TitleFragment tfragment = (TitleFragment) getSupportFragmentManager().findFragmentByTag("tfragment");
        TitleFragmentDetail tdfragment = (TitleFragmentDetail) getSupportFragmentManager().findFragmentByTag("tdfragment");

        if(findViewById(R.id.activity_main).getTag().equals("big_screen")){

            android.support.v4.app.FragmentTransaction transact = getSupportFragmentManager().beginTransaction();
            if(tfragment != null){
                getSupportFragmentManager().beginTransaction().remove(tfragment).commit();
                tfragment = null;
            }
            if(tdfragment != null){
                getSupportFragmentManager().beginTransaction().remove(tdfragment).commit();
                tdfragment = null;
            }
            getSupportFragmentManager().executePendingTransactions();
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

//
//        tfragment = (TitleFragment) getSupportFragmentManager().findFragmentByTag("tfragment");
//        tdfragment = (TitleFragmentDetail) getSupportFragmentManager().findFragmentByTag("tdfragment");
        android.support.v4.app.FragmentTransaction transact = getSupportFragmentManager().beginTransaction();

        if (tfragment == null) {
            tfragment = new TitleFragment();
            transact.replace(R.id.your_placeholder_list, tfragment, "tfragment");
        }

        if (findViewById(R.id.activity_main).getTag().equals("big_screen")) {

            if (tdfragment == null) {
                tdfragment = new TitleFragmentDetail();
                ArrayList<MenuItemObject> mArray = SingletonClass.initInstance(getApplicationContext()).getListArray();
                if (mArray != null) {
                    Bundle args = new Bundle();
                    String toSend = null;
                    if(mPositionForDfragment >= 0)
                    toSend = (mPositionForDfragment + 1) + ". " + mArray.get(mPositionForDfragment).getmItemName().toUpperCase() + "\n" + mArray.get(mPositionForDfragment).getmItemDetail();
                    args.putInt("POSITION",mPositionForDfragment);
                    Log.v(TAG, "toSend" + toSend);
                    args.putString("Description", toSend);
                    tdfragment.setArguments(args);
                }
                transact.replace(R.id.your_placeholder_detail, tdfragment, "tdfragment");
            }else{
                //transact.remove(tdfragment);

           //     transact.replace(R.id.your_placeholder_list,tfragment,"tfragment");

                transact.replace(R.id.your_placeholder_detail, tdfragment, "tdfragment");
            }
        }

        transact.commit();
    }

    @Override
    public void TitleFragmentListClicked(int position, ArrayList<MenuItemObject> arrayList) {

        ArrayList<MenuItemObject> mArray = SingletonClass.initInstance(getApplicationContext()).getListArray();
        mArrayList = arrayList;
        mPositionForDfragment = position;

        if (findViewById(R.id.activity_main).getTag().equals("small_screen")){
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            TitleFragmentDetail tdfragment = (TitleFragmentDetail) getSupportFragmentManager().findFragmentByTag("tdfragment");
            if(tdfragment != null){
                transaction.remove(tdfragment);
                transaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }

        }


        TitleFragmentDetail tdfragment = (TitleFragmentDetail) getSupportFragmentManager().findFragmentByTag("tdfragment");
        Log.v(TAG, "TitleFragmentListClicked");
        if (tdfragment == null) {
            tdfragment = new TitleFragmentDetail();
            if (mArray != null) {
                Bundle args = new Bundle();
//                args.putInt("position", position);
                args.putInt("POSITION",position);
                String toSend = "" + (position + 1) + ". " + mArray.get(position).getmItemName().toUpperCase() + "\n" + mArray.get(position).getmItemDetail();
                Log.v(TAG, "before else" + toSend);
                args.putString("Description", toSend);
                tdfragment.setArguments(args);

            }

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            TitleFragment tfragment = (TitleFragment) getSupportFragmentManager().findFragmentByTag("tfragment");

            if (tfragment != null) {
                transaction.replace(tfragment.getId(), tdfragment, "tdfragment");
//                transaction.replace(R.id.your_placeholder_list, tdfragment, "tdfragment");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        } else {
                tdfragment.updateView("" + (position + 1) + ". " + mArrayList.get(position).getmItemName().toUpperCase() + "\n" + mArrayList.get(position).getmItemDetail());

            tdfragment.updateCount(position);
        }
    }



    @Override
    public void TitleFragmentDetailCountChanged() {
        TitleFragment tfragment = (TitleFragment) getSupportFragmentManager().findFragmentByTag("tfragment");
        if(tfragment != null){
            tfragment.notifyAdapterInList();
        }
    }


    @Override
    public void onTitleCountUpdated() {

        TitleFragmentDetail tdfragment = (TitleFragmentDetail) getSupportFragmentManager().findFragmentByTag("tdfragment");
        if(tdfragment != null){
            tdfragment.updateCount(mPositionForDfragment);
        }

        TitleFragment tfragment = (TitleFragment) getSupportFragmentManager().findFragmentByTag("tfragment");
        if(tfragment != null){
            tfragment.notifyAdapterInList();
        }
    }


    @Override
    public void TitleFragmentDetailBackButtonPressed() {
        Log.v(TAG, "TitleFragmentDetailBackButtonPressed");
        android.support.v4.app.FragmentManager fm = this
                .getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }



    @Override
    public void onHistoryClicked() {

        Intent i = new Intent(this,CustomerOrderHistoryActivity.class);
        startActivity(i);

    }

    @Override
    public void onOrderClicked() {

        ArrayList<MenuItemObject> arrayList = SingletonClass.initInstance(getApplicationContext()).getListArray();
        for (int i = 0; i < arrayList.size(); i++) {
            Log.v(TAG, "Count = " + arrayList.get(i).getmOrderCount() + "Index =" + i);
            if (arrayList.get(i).getmOrderCount()>0) {
                GlobalVariables.OrderDetails += arrayList.get(i).getmItemName() + "*" + arrayList.get(i).getmOrderCount() + ",";
                GlobalVariables.mLon = SingletonClass.initInstance(getApplicationContext()).getmLong();
                GlobalVariables.mLat = SingletonClass.initInstance(getApplicationContext()).getmLat();
                Log.v(TAG,"Global lat lon"+GlobalVariables.mLon+" "+GlobalVariables.mLat);
            }
            }
        System.out.println(GlobalVariables.OrderDetails);
        Intent i = new Intent(this, billingactivity.class);
        startActivityForResult(i, RESULT_CODE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CODE) {
            if (resultCode == 1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Billing Confirmation");
                builder.setMessage(data.getStringExtra("RESULT"));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
//                builder.setView(tv);
                builder.show();

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mPositionForDfragment);
        Log.v(TAG, "Saving Position" + mPositionForDfragment);
    }
}
