package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

/**
 * Created by kartik on 06-11-2016.
 */


        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.preference.PreferenceManager;
        import android.provider.Settings;
        import android.util.Log;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.cs442.iitc_fall2016_g13.mad_proj.Map_distance.MapsActivity;
        import com.cs442.iitc_fall2016_g13.mad_proj.NearbyPlaces;
        import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.CustomerOrderHistoryActivity;

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

        import static android.content.Context.MODE_PRIVATE;
        import static com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.LoginActivity.PREFS_NAME;
        import static com.facebook.GraphRequest.TAG;

/**
 * Created by karti on 29-10-2016.
 */

public class LoginProcess  extends AsyncTask<String,Void,String> {
    private Context context;
    public static ProgressDialog dialog ;
    String password="";

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
           password = (String)arg0[1];

            GlobalVariables.username=username;
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

        if (result.equals(MD5(password)))


        {

            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            // Writing data to SharedPreferences
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", GlobalVariables.username);
            editor.putString("usertype","cust");
            editor.commit();

         ;
            Intent intent = new Intent(context, MapsActivity.class);
            context.startActivity(intent);
           // ((Activity)context).finish();
            dialog.dismiss();

        }
        else {


            dialog.dismiss();
            Toast toast = Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_LONG);
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