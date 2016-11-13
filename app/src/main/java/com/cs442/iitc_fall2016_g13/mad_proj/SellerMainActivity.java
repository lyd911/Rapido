package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.LoginActivity;

import java.util.ArrayList;

/**
 * Created by lyd on 2016/11/9.
 */

public class SellerMainActivity extends Activity{

    public static ArrayList<OneOrder> orders;

    public static ListView orders_listview;
    public static ArrayList<String> orderString;
    public static int currentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_page);
        orders = new ArrayList<OneOrder>();
        orderString = new ArrayList<String>();

        TextView pending_textview=(TextView)findViewById(R.id.detail_textview);
        Button order_history_button=(Button)findViewById(R.id.order_history_button);
        orders_listview =(ListView)findViewById(R.id.orders_listview);

        String admin = LoginActivity.admin;


        order_history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SellerOrderHistoryActivity.class);
                startActivity(intent);
            }
        });


        new SellerGetPendingOrdersProcess(this).execute(LoginActivity.admin);


        orders_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                Intent intent = new Intent(getApplicationContext(),SellerOrderDetailActivity.class);
                startActivity(intent);

            }
        });

    }

    public static void setOrderListView(ArrayAdapter aa){
        orders_listview.setAdapter(aa);
        aa.notifyDataSetChanged();
    }


}
