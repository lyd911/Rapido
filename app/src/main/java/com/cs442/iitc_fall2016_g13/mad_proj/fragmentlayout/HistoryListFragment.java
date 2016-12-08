package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cs442.iitc_fall2016_g13.mad_proj.QRCodeGenerator;
import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import static com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.billingactivity.PREFS_NAME;

/**
 * Created by KiranCD on 10/6/2016.
 */

public class HistoryListFragment extends Fragment {

    ArrayList<String> mArrayList;
    HistoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
      /*  ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.historylayout, container, false);
        // Create an array of string to be data source of the ListFragment

        SharedPreferences pref = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);


        Set<String> historyStringSet = pref.getStringSet("HISTORY",null);
        if(historyStringSet == null){
            mArrayList = new ArrayList<>();
            mArrayList.add("No transaction found");
        }else{

            mArrayList = new ArrayList<String>(historyStringSet);

            Collections.sort(mArrayList, new Comparator<String>() {
                public int compare(String a, String b) {
                    return Integer.signum(fixString(a) - fixString(b));
                }
                private int fixString(String in) {
                    return Integer.parseInt(in.substring(0, in.indexOf(')')));
                }
            });
        }

*//*
        mArrayList.add("Kiran");
        mArrayList.add("Arun");
        mArrayList.add("Ravi");
        mArrayList.add("Ranbo");*//*


        // Create ArrayAdapter object to wrap the data source
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.rowlayout,R.id.txtitem,datasource);
        mAdapter = new HistoryAdapter(getActivity(),R.layout.historylayout,mArrayList,this);

        ListView lv = (ListView) rootView.findViewById(R.id.listViewHistory);
        lv.setAdapter(mAdapter);

        return rootView;
*/
    }

// getting bill  QR kiran
    public void startQrCode(String billNumber){

        Intent intent = new Intent(getContext(),QRCodeGenerator.class);
        intent.putExtra("BILL#",billNumber);
        startActivity(intent);

    }
}
