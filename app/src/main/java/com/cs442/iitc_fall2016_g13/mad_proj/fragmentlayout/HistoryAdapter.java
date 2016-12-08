package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.app.Activity;
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
import com.cs442.iitc_fall2016_g13.mad_proj.Seller.OneOrder;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import java.util.ArrayList;

import static com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.CustomerOrderHistoryActivity.customer_order_history;

/**
 * Created by KiranCD on 10/7/2016.
 */





public class HistoryAdapter extends ArrayAdapter {


    private static final String TAG = "HistoryAdapter";
    ArrayList<String> mObjects;
    Context mContext;
    CustomerOrderHistoryActivity cont;
    ArrayList<OneOrder> od;

    public HistoryAdapter(Context context, int textViewResourceId, ArrayList<String> objects, ArrayList<OneOrder> od, CustomerOrderHistoryActivity con) {
        super(context, textViewResourceId, objects);



        this.mContext = context;
        cont = con;
        this.mObjects = objects;
        this.od=od;
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
v.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        GlobalVariables.orderID_QR = od.get(position).getOrder_id();
        GlobalVariables.lati=Double.parseDouble(od.get(position).getlat());
        GlobalVariables.longi=Double.parseDouble(od.get(position).getlon());
        System.out.println("Lati is:"+GlobalVariables.lati);
        System.out.println("Longi is:"+GlobalVariables.longi);

        Intent intent = new Intent(mContext, QRCodeGenerator.class);
        mContext.startActivity(intent);
    }
});

       /* Button qr = (Button) v.findViewById(R.id.btnQr);

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
        });*/

        TextView  tv1 = (TextView) v.findViewById(R.id.historyRoworder);
        TextView  tv3 = (TextView) v.findViewById(R.id.historyRowmenu);
        TextView  tv4 = (TextView) v.findViewById(R.id.historyRowstatus);

        tv1.setText(od.get(position).getOrder_id().toString());
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
