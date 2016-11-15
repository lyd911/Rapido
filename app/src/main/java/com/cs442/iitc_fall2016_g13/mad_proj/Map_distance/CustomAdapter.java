package com.cs442.iitc_fall2016_g13.mad_proj.Map_distance;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.FetchMenu;
import com.cs442.iitc_fall2016_g13.mad_proj.LoadData;
import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.Zomato.GoogleZomatoFetch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by KiranCD on 11/11/2016.
 */

public class CustomAdapter extends ArrayAdapter {


    private static final String TAG = "CustomAdapter";
    private Context mContext;
    private ArrayList<Restaurant> objects;

    TextView tvRestrauntName;
    TextView tvRating;
    TextView tvDistanct;


    public CustomAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.objects = objects;


        Log.v(TAG,"object "+objects.toString());
    }

    public void sortByRating(){

        Log.v(TAG,"sortByRating");

        Log.v(TAG,"sortByRating notifyDataset"+objects);

        if ( objects != null) {

            Collections.sort(objects, new Comparator<Restaurant>() {
                @Override
                public int compare(Restaurant o1, Restaurant o2) {
                    return  Double.compare(o1.getmRating(),o2.getmRating());
                }
            });

            Collections.reverse(objects);



            Log.v(TAG,"sortByRating notifyDataset"+objects);
            this.notifyDataSetInvalidated();
        }
    }


    public void sortByDistance(){

        Log.v(TAG,"sortByDistance");

        Log.v(TAG,"sortByDistance notifyDataset"+objects);

        if ( objects != null) {

            Collections.sort(objects, new Comparator<Restaurant>() {
                @Override
                public int compare(Restaurant o1, Restaurant o2) {
                    return  Double.compare(o1.getmDistance(),o2.getmDistance());
                }
            });



            Log.v(TAG,"sortByDistance notifyDataset"+objects);
            this.notifyDataSetInvalidated();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        Log.v(TAG,"position "+position);

        if (v == null) {

            v = View.inflate(mContext, R.layout.restraunt_list_layout, null);

        } else {

            v = convertView;
        }

        tvRating = (TextView) v.findViewById(R.id.txtRating);
        tvDistanct = (TextView) v.findViewById(R.id.txtDistance);
        tvRestrauntName = (TextView) v.findViewById(R.id.txtRestaurant);
        final Restaurant tempObj = objects.get(position);
        final int pos = position;


        if(tempObj != null){
            if(tvRestrauntName != null){
               // Toast.makeText(getContext(), "restaurnat", Toast.LENGTH_SHORT).show();
                tvRestrauntName.setText(tempObj.getmPlaceName());

                String distance = String.format("%.2f",tempObj.getmDistance());
                tvDistanct.setText(distance);
                String rating = Double.toString(tempObj.getmRating());
                tvRating.setText(rating);
            }
        }

        return v;
    }

}
