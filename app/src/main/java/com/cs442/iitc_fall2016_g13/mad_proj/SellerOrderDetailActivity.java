package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lyd on 2016/11/10.
 */

public class SellerOrderDetailActivity extends Activity{

    int order_position;
    String order_id_for_process;

    public static ArrayList<String> OneOrderDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);

        TextView detail_textview = (TextView) findViewById(R.id.detail_textview);
        ListView detail_listview = (ListView) findViewById(R.id.detail_listview);
        Button proceed_button = (Button)findViewById(R.id.proceed_button);
        if(SellerMainActivity.orders.get(order_position).getMenu_list().equals("2")){
            proceed_button.setVisibility(View.INVISIBLE);
        }

        order_position = SellerMainActivity.currentPosition;
        order_id_for_process = SellerMainActivity.orders.get(order_position).getOrder_id();

        final String order_id = "Order ID: " + SellerMainActivity.orders.get(order_position).getOrder_id();
        String customer_id ="Customer ID: " + SellerMainActivity.orders.get(order_position).getCust_id();
        String menu_list ="Menu list: " + SellerMainActivity.orders.get(order_position).getMenu_list();
        String Status;
        if(SellerMainActivity.orders.get(order_position).getStatus().equals("0")){
            Status = "Status: Not Started";
        }
        else Status = "Status: Cooking";

        OneOrderDetail = new ArrayList<String>();

        OneOrderDetail.add(order_id);
        OneOrderDetail.add(customer_id);
        OneOrderDetail.add(menu_list);
        OneOrderDetail.add(Status);


        final ArrayAdapter<String> aa;

        aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                OneOrderDetail);

        // Bind the Array Adapter to the List View
        detail_listview.setAdapter(aa);
        aa.notifyDataSetChanged();


        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SellerUpdateOrderStatusProcess(SellerOrderDetailActivity.this).execute(order_id_for_process);
            }
        });

    }


}
