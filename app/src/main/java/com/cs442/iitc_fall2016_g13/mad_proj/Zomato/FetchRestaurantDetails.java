package com.cs442.iitc_fall2016_g13.mad_proj.Zomato;

/**
 * Created by karti on 10-11-2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.cs442.iitc_fall2016_g13.mad_proj.MainMenuActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by karti on 29-10-2016.
 */
public class FetchRestaurantDetails  extends AsyncTask<String,Void,String> {
    private Context context;
    public static ProgressDialog dialog ;


    //flag 0 means get and 1 means post.(By default it is get.)
    public FetchRestaurantDetails(Context context) {
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
            System.out.println("getting details2");

            String res_ids = (String)arg0[0];
            res_ids = res_ids.replace("\"", "");
            //System.out.println(result);
            String res_id[]= res_ids.split(",");
            String link="http://rapido.counseltech.in/FetchRestaurantDetails.php";
            for (int k =0;k<res_ids.length();k++) {
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
                System.out.println("getting data from server");
                // code for password match and new activity
                String resdata[]=sb.toString().split("`");
                GlobalVariables.res_data[k][0]=resdata[0];
                GlobalVariables.res_data[k][1]=resdata[1];
                GlobalVariables.res_data[k][2]=resdata[2];
                GlobalVariables.res_data[k][3]=resdata[3];
                GlobalVariables.res_data[k][4]=resdata[4];


            }
            for (String[] x : GlobalVariables.res_data)
            {
                System.out.println(x[0]);

            }

            return null;

        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());

        }
    }

    @Override
    protected void onPostExecute(String result){
        if (dialog.isShowing()) {

            dialog.dismiss();
        }
    }


}