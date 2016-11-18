package com.cs442.iitc_fall2016_g13.mad_proj.Seller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by lyd on 2016/11/12.
 */

public class SellerUpdateOrderStatusProcess extends AsyncTask<String,OneOrder,String> {

    private Context context;
    private ProgressDialog dialog;
    private String order_id="";

    public SellerUpdateOrderStatusProcess(Context context) {
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
            order_id = (String)params[0];

            String link="http://rapido.counseltech.in/sellerUpdateOrders.php";
            String data  = URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(order_id, "UTF-8");

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
