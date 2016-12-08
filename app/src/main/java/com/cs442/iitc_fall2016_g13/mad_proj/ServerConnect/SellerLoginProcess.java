package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.Seller.SellerMainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;
import static com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.LoginActivity.PREFS_NAME;

/**
 * Created by lyd on 2016/11/9.
 */

public class SellerLoginProcess extends AsyncTask<String,Void,String>{
    private Context context;
    public static ProgressDialog dialog ;
    String password="";

    //flag 0 means get and 1 means post.(By default it is get.)
    public SellerLoginProcess(Context context) {
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
            password = (String)arg0[1];
            username = username.replaceAll("\'","\\'");
            LoginActivity.admin=(String)arg0[0];
            GlobalVariables.SellerUsername=username;

            String link="http://rapido.counseltech.in/sellerlogin.php";
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
            if (result.equals(MD5(password)))
            {
                SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", GlobalVariables.SellerUsername);
                editor.putString("usertype","seller");
                editor.commit();
                Intent intent = new Intent(context, SellerMainActivity.class);
                Toast.makeText(context,"Seller log in",Toast.LENGTH_LONG).show();
                context.startActivity(intent);
                dialog.dismiss();

            }
            else {
                dialog.dismiss();
                Toast toast = Toast.makeText(context, "Invalid Username or Password for seller", Toast.LENGTH_LONG);
                toast.show();
            }
    }
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
