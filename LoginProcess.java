package com.example.sandratobias.mad_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class LoginProcess  extends AsyncTask<String,Void,String> {
    private Context context;
    public static ProgressDialog dialog ;


    //flag 0 means get and 1 means post.(By default it is get.)
    public LoginProcess(Context context) {
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
            String password = (String)arg0[1];


            String link="http://rapido.counseltech.in/login.php";
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

            // code for password match and new activity
            return sb.toString();
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