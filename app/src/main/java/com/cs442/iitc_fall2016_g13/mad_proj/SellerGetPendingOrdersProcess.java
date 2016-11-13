package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


/**
 * Created by lyd on 2016/11/9.
 */

public class SellerGetPendingOrdersProcess extends AsyncTask<String,OneOrder,String> {

    private Context context;
    public static ProgressDialog dialog ;
    private String admin="";
    private String rawdata;
    private int numberOfOrders;



    public SellerGetPendingOrdersProcess(Context context) {
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

                SellerMainActivity.orders.add(i,oneOrder);
            }}

            if(SellerMainActivity.orders!=null){
            numberOfOrders = SellerMainActivity.orders.size();
            for(i=0;i<numberOfOrders;i++){
                String Status;
                if(SellerMainActivity.orders.get(i).getStatus().equals("0")){
                    Status = "Status: Not Started";
                }
                else Status = "Status: Cooking";
                String ss= "Order ID: " + SellerMainActivity.orders.get(i).getOrder_id()+"\n"+
                            "Customer ID: " + SellerMainActivity.orders.get(i).getCust_id()+"\n"+
                            "Menu List: " + SellerMainActivity.orders.get(i).getMenu_list()+"\n"+
                            Status;

                SellerMainActivity.orderString.add(ss);
            }}

            final ArrayAdapter<String> aa;

            aa = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1,
                    SellerMainActivity.orderString);

            // Bind the Array Adapter to the List View
            SellerMainActivity.setOrderListView(aa);
            aa.notifyDataSetChanged();

            return null;
        }catch (Exception e){
            return new String("Exception: " + e.getMessage());
        }



    }


    @Override

   protected void onPostExecute(String result) {

                dialog.dismiss();
    }
}
