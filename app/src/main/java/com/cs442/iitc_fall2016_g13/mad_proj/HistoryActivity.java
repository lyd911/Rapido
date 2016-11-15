package com.cs442.iitc_fall2016_g13.mad_proj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);


        HistoryListFragment hfragment = (HistoryListFragment) getSupportFragmentManager().findFragmentByTag("historyFragment");

        android.support.v4.app.FragmentTransaction transact = getSupportFragmentManager().beginTransaction();

        if (hfragment == null) {
            hfragment = new HistoryListFragment();
            transact.replace(R.id.historyPlaceholder, hfragment, "historyFragment").commit();
        }else{

            transact.replace(R.id.historyPlaceholder, hfragment, "historyFragment").commit();
        }


    }
}
