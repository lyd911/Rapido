package com.cs442.iitc_fall2016_g13.mad_proj.Zomato;

/**
 * Created by kartik on 06-11-2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.NearbyPlaces;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by karti on 29-10-2016.
 */
public class FetchNearbyPlaces  extends AsyncTask<String,Void,String> {
    private Context context;
    public static ProgressDialog dialog ;


    //flag 0 means get and 1 means post.(By default it is get.)
    public FetchNearbyPlaces(Context context) {
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
            String lat = (String)arg0[0];
            String lon = (String)arg0[1];


            String link="http://rapido.counseltech.in/FetchNearby.php";
            String data  = URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
            data  += URLEncoder.encode("long", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");

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

            }
            System.out.println("getting data from server");
            load_data(sb.toString());
            return sb.toString();

        }
        catch(Exception e){
            return("exception"+e);

        }

    }

    @Override
    protected void onPostExecute(String result){
        if (dialog.isShowing()) {

            //System.out.println(res_ids[0]);


                dialog.dismiss();
        }
    }
public  void load_data(String y)
{
    System.out.println("getting details");
    try{
    String res_ids = (y.replace("\"", "")).replaceAll("\\s+","");
        System.out.println(res_ids);

        //System.out.println(result);
    String res_id[]= res_ids.split(",");
        GlobalVariables.res_ids=res_id;
    String link="http://rapido.counseltech.in/FetchRestaurantDetails.php";
        GlobalVariables.res_data=new String[res_id.length][5];
        System.out.println("lenght of the array is"+res_id.length+"-----");
    for (int k =0;k<res_id.length;k++) {
       // System.out.println("counter - "+k);
       // System.out.println("res_id - "+res_id[k]);

        String data = URLEncoder.encode("res_id", "UTF-8") + "=" + URLEncoder.encode(res_id[k], "UTF-8");


        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(data);
        wr.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;

        // Read Server Response
        while ((line = reader.readLine()) != null) {

            sb.append(line);

        }
       // System.out.println("getting data from server");
      //  System.out.println(sb.toString());
        // code for password match and new activity
        String resdata[]=sb.toString().split("`");
        GlobalVariables.res_data[k][0]=resdata[0];
      //  System.out.println(resdata[1]);

      //  System.out.println(GlobalVariables.res_data[0][0]);
        GlobalVariables.res_data[k][1]=resdata[1];
        GlobalVariables.res_data[k][2]=resdata[2];
        GlobalVariables.res_data[k][3]=resdata[3];
        GlobalVariables.res_data[k][4]=resdata[4];


    }
      for (String[] x: GlobalVariables.res_data)
      {
          System.out.println(x[0]);

      }
        System.out.println("Starting Nearby Places");
        Intent intent = new Intent(context, NearbyPlaces.class);
        context.startActivity(intent);
        ((Activity)context).finish();
        // code for password match and new activity
    }
    catch(Exception e){
        System.out.println(e);

    }
}

}