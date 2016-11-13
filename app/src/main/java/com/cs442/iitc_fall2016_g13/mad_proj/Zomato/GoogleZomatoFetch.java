package com.cs442.iitc_fall2016_g13.mad_proj.Zomato;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.cs442.iitc_fall2016_g13.mad_proj.FetchMenu;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by karti on 29-10-2016.
 */
public class GoogleZomatoFetch  extends AsyncTask<String,Void,String> {
    private Context context;
    public static ProgressDialog dialog ;


    //flag 0 means get and 1 means post.(By default it is get.)
    public GoogleZomatoFetch(Context context) {
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
            String search=(String)arg0[0];

            String link="http://rapido.counseltech.in/GoogleZomatoFetch.php";
            String data  = URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8");

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
            System.out.println("Zomata and Google Check");
            System.out.println(sb);

            Log.d("Fetch Menu","Loaded Menu Url");

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
        System.out.println("getting Menu Items");
        try{
            Looper.prepare();
            new FetchMenu(context).execute(y);
            // Check menu here
            // code for password match and new activity
        }
        catch(Exception e){
            System.out.println(e);

        }
    }

}