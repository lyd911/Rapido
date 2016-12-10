package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
/**
 * Created by karti on 29-10-2016.
 */
public class FB_SignupProcess  extends AsyncTask<String,Void,String> {
    private TextView statusField,roleField;
    private Context context;
    public static ProgressDialog dialog ;


    //flag 0 means get and 1 means post.(By default it is get.)
    public FB_SignupProcess(Context context) {
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
            String name=(String)arg0[1];
            String phone=(String)arg0[2];
            String addr=(String)arg0[3];

            String link="http://rapido.counseltech.in/FB_Signup.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("addr", "UTF-8") + "=" + URLEncoder.encode(addr, "UTF-8");

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
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);

            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());

        }
    }

    @Override
    protected void onPostExecute(String result){

        Intent t = new Intent(context,MapsActivity.class);
        context.startActivity(t);

            dialog.dismiss();


    }
}

