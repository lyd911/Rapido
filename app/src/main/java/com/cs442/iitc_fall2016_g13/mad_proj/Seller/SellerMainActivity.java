package com.cs442.iitc_fall2016_g13.mad_proj.Seller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.LoginActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.os.Handler;


import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by lyd on 2016/11/9.
 */

public class SellerMainActivity extends Activity{

    public static ArrayList<OneOrder> pendingOrders;
    public static ArrayAdapter<String> aa;
    public static Dialog dialog;

    public static ListView orders_listview;
    public static ArrayList<String> orderString;
    public static int currentOrder;
   private int numberOfOrders;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main_page);
        pendingOrders = new ArrayList<OneOrder>();
        orderString = new ArrayList<String>();


        TextView pending_textview=(TextView)findViewById(R.id.detail_textview);
        Button order_history_button=(Button)findViewById(R.id.order_history_button);
        Button get_menu_list_button=(Button)findViewById(R.id.get_menu_list_button);
        orders_listview =(ListView)findViewById(R.id.orders_listview);

        String admin = LoginActivity.admin;

        order_history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SellerOrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        get_menu_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SellerGetMenuListActivity.class);
                startActivity(intent);
            }
        });

        orders_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentOrder = position;

                Intent intent = new Intent(getApplicationContext(),SellerOrderDetailActivity.class);
                startActivity(intent);
            }
        });

        aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                SellerMainActivity.orderString);
        orders_listview.setAdapter(aa);

        callAsynchronousTask();

    }


    @Override
    protected void onResume() {
        super.onResume();
        aa.notifyDataSetChanged();
    }

    public static void setOrderListView(ArrayAdapter aa){
        orders_listview.setAdapter(aa);
        aa.notifyDataSetChanged();
    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            doMysql();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 1, 10000); //execute in every 10000 ms
    }


    public void doMysql(){

        AsyncTask<String,OneOrder,String> task = new AsyncTask<String, OneOrder, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                dialog.setMessage("Please wait");
//                dialog.show();
            }


            @Override
            protected String doInBackground(String... params) {
                try{
                    orderString.removeAll(orderString);
                    pendingOrders.removeAll(pendingOrders);
                    String admin = LoginActivity.admin;

                    String link="http://rapido.counseltech.in/sellerRefresh.php";
                    String data  = URLEncoder.encode("admin", "UTF-8") + "=" + URLEncoder.encode(admin, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    String rawdata=sb.toString();

                    int i=0;
                    String s1[] = rawdata.split("\\}");
                    if(s1!=null){
                        for(i=0;i<s1.length-1;i++){
                            String s2[]=s1[i].split("\"");
                            OneOrder oneOrder = new OneOrder();
                            oneOrder.setOrder_id(s2[3]);
                            oneOrder.setRest_id(s2[7]);
                            oneOrder.setCust_id(s2[11]);
                            oneOrder.setMenu_list(s2[15]);
                            oneOrder.setStatus(s2[19]);

                            SellerMainActivity.pendingOrders.add(i,oneOrder);
                        }}

                    if(pendingOrders !=null){
                        numberOfOrders = pendingOrders.size();
                        for(i=0;i<numberOfOrders;i++){
                            String Status;
                            if(pendingOrders.get(i).getStatus().equals("0")){
                                Status = "Status: Not Started";
                            }
                            else if(pendingOrders.get(i).getStatus().equals("1")){
                                Status = "Status: Cooking";
                            }
                            else Status = "Status: Finished Cooking";
                            String ss= "Order ID: " + pendingOrders.get(i).getOrder_id()+"\n"+
                                    "Customer ID: " + pendingOrders.get(i).getCust_id()+"\n"+
                                    "Menu List: " + pendingOrders.get(i).getMenu_list()+"\n"+
                                    Status;

                            orderString.add(ss);
                        }}


                    aa.notifyDataSetChanged();

                    return null;
                }catch (Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }



        };
        if(Build.VERSION.SDK_INT >= 11)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }
}
