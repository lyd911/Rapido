package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.QRCodeGenerator;
import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.util.ArrayList;

/**
 * Created by KiranCD on 10/7/2016.
 */





public class HistoryAdapter extends ArrayAdapter {


    private static final String TAG = "HistoryAdapter";
    ArrayList<String> mObjects;
    Context mContext;
    HistoryListFragment cont;


    public HistoryAdapter(Context context, int textViewResourceId, ArrayList<String> objects,HistoryListFragment con) {
        super(context, textViewResourceId, objects);



        this.mContext = context;
        cont = con;
        this.mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            v = View.inflate(mContext, R.layout.historyrowlayout, null);

        } else {

            v = convertView;
        }


        Button qr = (Button) v.findViewById(R.id.btnQr);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// QR for bill
                Log.v(TAG,"mAdapter"+position);
                if(mObjects != null){
                    cont.startQrCode(mObjects.get(position));
                }else{
                    Toast.makeText(getContext(), "No message", Toast.LENGTH_SHORT).show();
                }

            }
        });

        TextView  tv = (TextView) v.findViewById(R.id.historyRowText);

        tv.setText(mObjects.get(position));

        return v;


    }




}
