package com.cs442.iitc_fall2016_g13.mad_proj.Map_distance;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.util.ArrayList;

/**
 * Created by KiranCD on 11/11/2016.
 */

public class CustomAdapter extends ArrayAdapter {


    private static final String TAG = "CustomAdapter";
    private Context mContext;
    private ArrayList<Restaurant> objects;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.objects = objects;


        Log.v(TAG,"object "+objects.toString());
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

        TextView tvRestrauntName = (TextView) v.findViewById(R.id.txtRestaurant);
        final Restaurant tempObj = objects.get(position);
        final int pos = position;


        if(tempObj != null){
            if(tvRestrauntName != null){
               // Toast.makeText(getContext(), "restaurnat", Toast.LENGTH_SHORT).show();
                tvRestrauntName.setText(tempObj.getmPlaceName());
            }
        }

        return v;
    }
}
