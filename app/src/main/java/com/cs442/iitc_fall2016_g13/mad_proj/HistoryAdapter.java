package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KiranCD on 10/7/2016.
 */





public class HistoryAdapter extends ArrayAdapter {



    ArrayList<String> mObjects;
    Context mContext;


    public HistoryAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);



        this.mContext = context;
        this.mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            v = View.inflate(mContext, R.layout.historyrowlayout, null);

        } else {

            v = convertView;
        }

        TextView  tv = (TextView) v.findViewById(R.id.historyRowText);

        tv.setText(mObjects.get(position));

        return v;


    }
}
