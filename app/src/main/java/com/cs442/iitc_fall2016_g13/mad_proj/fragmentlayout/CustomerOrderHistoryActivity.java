package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.QRActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.QRCodeGenerator;
import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.Seller.OneOrder;
import com.cs442.iitc_fall2016_g13.mad_proj.Seller.SellerOrderHistoryActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.LoginActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by lyd on 2016/11/20.
 */

public class CustomerOrderHistoryActivity extends Activity {
    public static ArrayAdapter<HistoryAdapter> aa;

    public static ArrayList<OneOrder> customer_order_history;
    public static ListView customer_history_listview;
    public static ArrayList<String> customer_order_string;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutomer_order_history);
        customer_order_history = new ArrayList<OneOrder>();
        customer_order_string = new ArrayList<String>();

        TextView customer_history_textview = (TextView)findViewById(R.id.customer_detail_textview);
        customer_history_listview = (ListView)findViewById(R.id.customer_history_listview);

        String username = GlobalVariables.username;




        new CustomerGetOrderHistoryProcess(this).execute(username);
        aa = new HistoryAdapter(this,
                R.layout.historylayout,
                customer_order_string,customer_order_history,this);
        customer_history_listview.setAdapter(aa);



        aa.notifyDataSetChanged();

    }


    public class CustomerGetOrderHistoryProcess extends AsyncTask<String,OneOrder,String> {

        private Context context;
        private ProgressDialog dialog;
        private String admin="";
        private String rawdata;
        private int numberOfOrders;

        public CustomerGetOrderHistoryProcess(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
        }

        protected void onPreExecute(){
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                String cust_id = (String)params[0];

                String link="http://rapido.counseltech.in/UserGetHistory.php";
                String data  = URLEncoder.encode("cust_id", "UTF-8") + "=" + URLEncoder.encode(cust_id, "UTF-8");

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
                rawdata=sb.toString();

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
                        oneOrder.setlat(s2[23]);
                        oneOrder.setlon(s2[27]);

                        customer_order_history.add(i,oneOrder);
                    }
                }

                if(customer_order_history!=null){
                    numberOfOrders = customer_order_history.size();
                    for(i=0;i<numberOfOrders;i++){
                        String Status;
                        if(customer_order_history.get(i).getStatus().equals("0")){
                            Status = "Status: Not Started";
                        }
                        else if(customer_order_history.get(i).getStatus().equals("1")){Status = "Status: Cooking";}
                        else if(customer_order_history.get(i).getStatus().equals("2")){Status = "Status: Finished Cooking";}
                        else {Status = "Status: Taken Away";}
                        String ss= "Order ID: " + customer_order_history.get(i).getOrder_id()+"\n"+
                                "Customer ID: " + customer_order_history.get(i).getCust_id()+"\n"+
                                "Menu List: " + customer_order_history.get(i).getMenu_list()+"\n"+
                                Status;

                        customer_order_string.add(ss);
                    }
                }

                aa.notifyDataSetChanged();

                return null;
            }catch (Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            aa.notifyDataSetChanged();

            dialog.dismiss();
        }
    }
}
