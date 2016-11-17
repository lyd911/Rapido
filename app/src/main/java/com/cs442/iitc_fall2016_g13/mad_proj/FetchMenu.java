package com.cs442.iitc_fall2016_g13.mad_proj;

/**
 * Created by kartik on 06-11-2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.NearbyPlaces;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.MenuAndCartActivity;

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
public class FetchMenu  extends AsyncTask<String,Void,String> {
    private Context context;
    public static ProgressDialog dialog ;


    //flag 0 means get and 1 means post.(By default it is get.)
    public FetchMenu(Context context) {
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

            String res_id = (String)arg0[0];

            System.out.println(res_id);
            String link="http://rapido.counseltech.in/FetchMenuUrl.php";
            String data  = URLEncoder.encode("res_id", "UTF-8") + "=" + URLEncoder.encode(res_id, "UTF-8");

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
            System.out.println("Loaded Menu URL");
            Log.d("Fetch Menu","Loaded Menu Url");


            //code to go to menu list
            //check data exists or not in global variables res_menu and check global variables menu_check value
            //if there a menu then go to the card list actvity
            //if no menu is there then it will go to another activity that saya that there is no menu
            return sb.toString();

        }
        catch(Exception e){
            return("exception"+e);

        }

    }

    @Override
    protected void onPostExecute(String result){
        load_data(result);
        if (GlobalVariables.menu_check==1)
        {
            Intent intent = new Intent(context, MenuAndCartActivity.class); //Menu and Cart.class was launched here.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
        else
        {
            Intent intent = new Intent(context, MenuWeb.class); //MenuWeb.class was launched here.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
        if (dialog.isShowing()) {

            //System.out.println(res_ids[0]);


            dialog.dismiss();
        }
    }
    public  void load_data(String y)
    {
        System.out.println("getting Menu Items");
        System.out.println(y);
        try{

            String url = y;
            System.out.println(url);
            url=url.replaceAll("\"","");
            GlobalVariables.url=url;
           // url="https://www.zomato.com/chicago/nana-bridgeport/menu?utm_source=api_basic_user&utm_medium=api&utm_campaign=v2.1&openSwipeBox=menu&showMinimal=1#tabtop";
            Document doc = Jsoup.connect(url).timeout(10*1000).get();
            Elements newsHeadlines = doc.select(".tmi");
            if (newsHeadlines.size()<3)
            {
                GlobalVariables.menu_check=0;

            }
            else {
                GlobalVariables.menu_check=1;
                GlobalVariables.res_menu = new String[3][3];
                System.out.println(newsHeadlines.size());
                for (int i = 0; i < 3; i++) {
                    String item = ((newsHeadlines.get(i).toString().replaceAll("\\s+", "")).split("<divclass=\"tmi-nameft16mb5\">")[1]).split("<divclass=\"tmi-desc-textpt5pb5\">")[0];
                    String item_price = (((newsHeadlines.get(i).toString().replaceAll("\\s+", "")).split("<divclass=\"tmi-nameft16mb5\">")[1]).split("<divclass=\"tmi-price-txtfontsize4ta-rightbold600\">")[1]).split("<")[0];
                    String item_description = ((((newsHeadlines.get(i).toString().replaceAll("\\s+", "")).split("<divclass=\"tmi-nameft16mb5\">")[1]).split("<divclass=\"tmi-price-txtfontsize4ta-rightbold600\">")[0]).split("<divclass=\"tmi-desc-textpt5pb5\"><div>")[1]).split("<")[0];
                    System.out.println(item);
                    GlobalVariables.res_menu[i][0] = item;
                    GlobalVariables.res_menu[i][1] = item_price;
                    GlobalVariables.res_menu[i][2] = item_description;
                }
                for (String[] x : GlobalVariables.res_menu) {
                    System.out.println(x[0]);
                }
            }
            doc.remove();

            // JSoup Code here
            // code for password match and new activity
        }
        catch(Exception e){
            GlobalVariables.menu_check=0;
            System.out.println(e);

        }
    }

}