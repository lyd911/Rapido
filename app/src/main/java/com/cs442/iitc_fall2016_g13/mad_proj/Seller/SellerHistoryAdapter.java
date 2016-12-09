package com.cs442.iitc_fall2016_g13.mad_proj.Seller;

/**
 * Created by karti on 09-12-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.QRCodeGenerator;
import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.CustomerOrderHistoryActivity;

import java.util.ArrayList;



/**
 * Created by KiranCD on 10/7/2016.
 */





public class SellerHistoryAdapter extends ArrayAdapter {


    private static final String TAG = "HistoryAdapter";
    ArrayList<String> mObjects;
    Context mContext;
    SellerOrderHistoryActivity sel;
    ArrayList<OneOrder> od;

    public SellerHistoryAdapter(Context context, int textViewResourceId, ArrayList<String> objects, ArrayList<OneOrder> od, SellerOrderHistoryActivity sel) {
        super(context, textViewResourceId, objects);



        this.mContext = context;
        this.sel = sel;
        this.mObjects = objects;
        this.od=od;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            v = View.inflate(mContext, R.layout.sellerhistoryrow, null);

        } else {

            v = convertView;
        }



        TextView tv1 = (TextView) v.findViewById(R.id.historyRoworder);
        TextView tv5 = (TextView) v.findViewById(R.id.historyRowcustid);
        TextView  tv3 = (TextView) v.findViewById(R.id.historyRowmenu);
        TextView  tv4 = (TextView) v.findViewById(R.id.historyRowstatus);

        tv1.setText(od.get(position).getOrder_id().toString());
        tv5.setText(od.get(position).getCust_id().toString());
        tv3.setText(od.get(position).getMenu_list().toString());
        String Status;
        if(od.get(position).getStatus().equals("0")){
            Status = "Status: Not Started";
        } else if((od.get(position).getStatus().equals("1"))){Status = "Status: Cooking";}
        else if((od.get(position).getStatus().equals("2"))){Status = "Status: Finished Cooking";}
        else {Status = "Status: Taken Away";}
        tv4.setText(Status);


        return v;


    }




}
