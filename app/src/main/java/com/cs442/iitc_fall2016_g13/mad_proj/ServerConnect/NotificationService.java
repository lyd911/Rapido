package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.HandlerThread;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.Seller.OneOrder;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.CustomerOrderHistoryActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by karti on 22-10-2016.
 */
public class NotificationService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    public static int x =0;
    Thread thread;
    String Status="";
    String check="";
    NotificationManager mNotifyMgr2 ;
    String username;
    private static final String BUTTON_CLICK_ACTION = "com.paad.notifications.action.BUTTON_CLICK";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        Log.v("", "Counter Service started");
       username =GlobalVariables.username;
        startBackgroundTask(intent, startId);
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        Log.v("", "Counter Service Destroyed");

        // The service is no longer used and is being destroyed
    }
    private void  startBackgroundTask(Intent intent, int startId) {
        // Start a background thread and begin the processing.
        backgroundExecution();
    }

    /**
     * Listing 9-14: Moving processing to a background Thread
     */
    //This method is called on the main GUI thread.
    private void backgroundExecution() {
        // This moves the time consuming operation to a child thread.
        thread = new Thread(null, doBackgroundThreadProcessing,
                "Background");
        thread.start();
    }

    //Runnable that executes the background processing method.
    private Runnable doBackgroundThreadProcessing = new Runnable() {
        public void run() {
            backgroundThreadProcessing();
        }
    };

    //Method which does some processing in the background.
    private void backgroundThreadProcessing() {

        try {
            System.out.println(x);
            while(isMyServiceRunning(NotificationService.class))
            {
                String username = GlobalVariables.username;


                String link="http://rapido.counseltech.in/NotificationService.php";
                String data  = URLEncoder.encode("cust_id", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");


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
                Status=sb.toString();
                String s1[] = Status.split("\"Status\":\"");
                if(s1!=null){
                    for(int i=0;i<s1.length-1;i++){
                        String s2[]=s1[1].split("\"");

                        Status=(s2[0]);


                    }
                }
                System.out.println("Status"+Status);
                if (!Status.equals(check)) {

                    // System.out.println(x);
                    System.out.println("Status"+Status);
                    System.out.println("Check"+check);

                    int mNotificationId = 002;
                    mNotifyMgr2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                    mNotifyMgr2.notify(mNotificationId, customLayoutNotification().build());
                    check = Status;
                    if( Status.equals("3"))
                    {
                        stopSelf();
                    }
                }
                wr.close();
                reader.close();

                Thread.sleep(10000);

            }
        }
        catch (Exception e)
        {
            System.out.println(e);

        }
        // [ ... Time consuming operations ... ]
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private Notification.Builder customLayoutNotification() {



        Notification.Builder builder =
                new Notification.Builder(NotificationService.this);
        GlobalVariables.username=username;
        Intent resultIntent = new Intent(this, CustomerOrderHistoryActivity.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        resultIntent.putExtra("CustUsername",username);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Bitmap myIconBitmap = null; // TODO Obtain Bitmap

        /**
         * Listing 10-36: Applying a custom layout to the Notification status window
         */
        String stat="Status is: ";
        if (Status.equals("0")) stat="Not Started";
        if (Status.equals("1")) stat="Cooking";
        if (Status.equals("2")) stat="Finished Cooking";
        if (Status.equals("3")) stat="Taken Away";

        builder.setSmallIcon(R.drawable.restaurant_menu_icon)
                .setTicker("Notification From Rapido")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Order Status")
                .setContentText(""+stat)
                .setLargeIcon(myIconBitmap)
                .setContentIntent(resultPendingIntent);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        //LED
        // builder.setLights(Color.RED, 3000, 3000);

        //Ton
        // builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
        //


        return builder;
    }
}
