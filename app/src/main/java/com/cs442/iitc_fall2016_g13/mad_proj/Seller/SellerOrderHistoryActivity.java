package com.cs442.iitc_fall2016_g13.mad_proj.Seller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.R;
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
 * Created by lyd on 2016/11/9.
 */

public class SellerOrderHistoryActivity extends Activity{
    public static ArrayAdapter<String> aa;

    public static ArrayList<OneOrder> orders_finished;
    public static ListView history_listview;
    public static ArrayList<String> order_finished_String;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);
        orders_finished = new ArrayList<OneOrder>();
        order_finished_String = new ArrayList<String>();

        TextView history_textview = (TextView)findViewById(R.id.detail_textview);
        history_listview = (ListView)findViewById(R.id.history_listview);

        String admin = LoginActivity.admin;

        aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                SellerOrderHistoryActivity.order_finished_String);
        history_listview.setAdapter(aa);

        new SellerGetFinishedOrdersProcess(this).execute(GlobalVariables.SellerUsername);
        aa.notifyDataSetChanged();

    }


    public class SellerGetFinishedOrdersProcess extends AsyncTask<String,OneOrder,String> {

        private Context context;
        private ProgressDialog dialog;
        private String admin="";
        private String rawdata;
        private int numberOfOrders;

        public SellerGetFinishedOrdersProcess(Context context) {
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
                admin = (String)params[0];

                String link="http://rapido.counseltech.in/sellerGetFinished.php";
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

                        orders_finished.add(i,oneOrder);
                    }
                }

                if(orders_finished!=null){
                    numberOfOrders = orders_finished.size();
                    for(i=0;i<numberOfOrders;i++){
                        String Status;
                        if(orders_finished.get(i).getStatus().equals("0")){
                            Status = "Status: Not Started";
                        }
                        else if(orders_finished.get(i).getStatus().equals("1")){Status = "Status: Cooking";}
                        else if(orders_finished.get(i).getStatus().equals("2")){Status = "Status: Finished Cooking";}
                        else {Status = "Status: Taken Away";}
                        String ss= "Order ID: " + orders_finished.get(i).getOrder_id()+"\n"+
                                "Customer ID: " + orders_finished.get(i).getCust_id()+"\n"+
                                "Menu List: " + orders_finished.get(i).getMenu_list()+"\n"+
                                Status;

                        order_finished_String.add(ss);
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
