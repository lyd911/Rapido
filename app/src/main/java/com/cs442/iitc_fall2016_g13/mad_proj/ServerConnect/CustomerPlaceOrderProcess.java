package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.Map_distance.MapsActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.NearbyPlaces;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.CustomerOrderHistoryActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.billingactivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by lyd on 2016/11/19.
 */

public class CustomerPlaceOrderProcess extends AsyncTask<String,Void,String> {

    private TextView statusField,roleField;
    private Context context;
    public static ProgressDialog dialog ;


    //flag 0 means get and 1 means post.(By default it is get.)
    public CustomerPlaceOrderProcess(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);

    }

    protected void onPreExecute(){
        dialog.setMessage("Please wait");
        dialog.show();

    }


    @Override
    protected String doInBackground(String... arg0) {

        try{
            String rest_id = (String)arg0[0];
            String cust_id=(String)arg0[1];
            String menu_list=(String)arg0[2];


            String link="http://rapido.counseltech.in/CustomerPlaceOrder.php";
            String data  = URLEncoder.encode("rest_id", "UTF-8") + "=" + URLEncoder.encode(rest_id, "UTF-8");
            data += "&" + URLEncoder.encode("cust_id", "UTF-8") + "=" + URLEncoder.encode(cust_id, "UTF-8");
            data += "&" + URLEncoder.encode("menu_list", "UTF-8") + "=" + URLEncoder.encode(menu_list, "UTF-8");


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }


            dialog.dismiss();
            Intent intent = new Intent(context, NotificationService.class);
            context.startService(intent);

            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());

        }
    }

    @Override
    protected void onPostExecute(String result){

        Toast.makeText(getApplicationContext(),"Order placed!",Toast.LENGTH_LONG).show();




    }
}