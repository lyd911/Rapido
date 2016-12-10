package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

/**
 * Created by kartik on 06-11-2016.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.FB_Signup;
import com.cs442.iitc_fall2016_g13.mad_proj.Map_distance.MapsActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.NearbyPlaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by karti on 29-10-2016.
 */
public class FB_Check  extends AsyncTask<String,Void,String> {
    private Context context;
    public static ProgressDialog dialog ;
    String password="";

    //flag 0 means get and 1 means post.(By default it is get.)
    public FB_Check(Context context) {
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
            String username = (String)arg0[0];
            username=username.replaceAll(" ","" );
            String link="http://rapido.counseltech.in/FB_Check.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");


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
            if (sb.toString().equals("0"))
            {
                Intent intent = new Intent(context, FB_Signup.class);
                context.startActivity(intent);
                //redirect to signup page

            }
            else if (sb.toString().equals("1"))
            {

                Intent intent = new Intent(context, MapsActivity.class);
                context.startActivity(intent);
                // redirect to main activity
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());

        }
    }

    @Override
    protected void onPostExecute(String result){

       dialog.dismiss();

    }

}