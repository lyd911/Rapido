package com.cs442.iitc_fall2016_g13.mad_proj.Seller;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.view.View.INVISIBLE;

/**
 * Created by lyd on 2016/11/10.
 */

public class SellerOrderDetailActivity extends Activity {

    int selected_order_position;
    String order_id_for_process;
    public static String cust_phone;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 0;
    public static ArrayList<String> OneOrderDetail;
    public static ArrayList<OneOrder> tempArr;
    public static ArrayList<String> tempListView;
    String message = "Your order is Ready. From- RAPIDO";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);

        tempArr = SellerMainActivity.pendingOrders;
        tempListView = SellerMainActivity.orderString;
//        tempListView = getIntent().getStringArrayListExtra("orderString");
        selected_order_position = SellerMainActivity.currentOrder;

        TextView detail_textview = (TextView) findViewById(R.id.detail_textview);
        ListView detail_listview = (ListView) findViewById(R.id.detail_listview);
        final Button proceed_button = (Button) findViewById(R.id.proceed_button);
        final Button get_phone_button = (Button) findViewById(R.id.get_phone_button);


        order_id_for_process = tempArr.get(selected_order_position).getOrder_id();

        final String order_id = "Order ID: " + tempArr.get(selected_order_position).getOrder_id();
        final String customer_id = "Customer ID: " + tempArr.get(selected_order_position).getCust_id();
        String menu_list = "Menu list: " + tempArr.get(selected_order_position).getMenu_list();
        String Status;
        if (tempArr.get(selected_order_position).getStatus().equals("0")) {
            Status = "Status: Not Started";
        } else if (tempArr.get(selected_order_position).getStatus().equals("1")) {
            Status = "Status: Cooking";
        } else {
            Status = "Status: Finished Cooking";
        }

        OneOrderDetail = new ArrayList<String>();

        OneOrderDetail.add(order_id);
        OneOrderDetail.add(customer_id);
        OneOrderDetail.add(menu_list);
        OneOrderDetail.add(Status);

        final ArrayAdapter<String> aa;

        aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                OneOrderDetail);

        detail_listview.setAdapter(aa);
        aa.notifyDataSetChanged();


        new SellerGetPhoneProcess(SellerOrderDetailActivity.this).execute(tempArr.get(selected_order_position).getCust_id());

        get_phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + cust_phone));
                startActivity(call);
            }
        });


        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (tempArr.get(selected_order_position).getStatus().equals("4")) {
                    Toast.makeText(getApplicationContext(), "Already finished.", Toast.LENGTH_LONG).show();
                } else if (tempArr.get(selected_order_position).getStatus().equals("0")) {
                    Toast.makeText(getApplicationContext(), "Starting cooking now", Toast.LENGTH_LONG).show();
                    tempArr.get(selected_order_position).setStatus("1");
                    tempListView.set(selected_order_position,
                            "Order ID: " + tempArr.get(selected_order_position).getOrder_id() + "\n" +
                                    "Customer ID: " + tempArr.get(selected_order_position).getCust_id() + "\n" +
                                    "Menu List: " + tempArr.get(selected_order_position).getMenu_list() + "\n" +
                                    "Status: Cooking");
                    OneOrderDetail.set(3, "Status: Cooking");
                    aa.notifyDataSetChanged();
                    new SellerUpdateOrderStatusProcess(SellerOrderDetailActivity.this).execute(order_id_for_process);
                } else if (tempArr.get(selected_order_position).getStatus().equals("1")) {
                    PopupMenu popup = new PopupMenu(SellerOrderDetailActivity.this, proceed_button);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.sms, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId() == R.id.process_sms) {
                                sendSMSMessage();
                                Toast.makeText(getApplicationContext(), "Finished Cooking", Toast.LENGTH_LONG).show();
                                tempArr.get(selected_order_position).setStatus("2");
                                tempListView.set(selected_order_position,
                                        "Order ID: " + tempArr.get(selected_order_position).getOrder_id() + "\n" +
                                                "Customer ID: " + tempArr.get(selected_order_position).getCust_id() + "\n" +
                                                "Menu List: " + tempArr.get(selected_order_position).getMenu_list() + "\n" +
                                                "Status: Finished Cooking");
                                OneOrderDetail.set(3, "Status: Finished Cooking");
                                aa.notifyDataSetChanged();
                                new SellerUpdateOrderStatusProcess(SellerOrderDetailActivity.this).execute(order_id_for_process);
                            } else if (item.getItemId() == R.id.process_without_sms) {
                                Toast.makeText(getApplicationContext(), "Finished Cooking", Toast.LENGTH_LONG).show();
                                tempArr.get(selected_order_position).setStatus("2");
                                tempListView.set(selected_order_position,
                                        "Order ID: " + tempArr.get(selected_order_position).getOrder_id() + "\n" +
                                                "Customer ID: " + tempArr.get(selected_order_position).getCust_id() + "\n" +
                                                "Menu List: " + tempArr.get(selected_order_position).getMenu_list() + "\n" +
                                                "Status: Finished Cooking");
                                OneOrderDetail.set(3, "Status: Finished Cooking");
                                aa.notifyDataSetChanged();
                                new SellerUpdateOrderStatusProcess(SellerOrderDetailActivity.this).execute(order_id_for_process);
                            }
                            return true;
                        }
                    });

                    popup.show();


                } else {
                    Toast.makeText(getApplicationContext(), "Order is Taken away!", Toast.LENGTH_LONG).show();
                    tempArr.get(selected_order_position).setStatus("3");

                    OneOrderDetail.removeAll(OneOrderDetail);
                    aa.notifyDataSetChanged();
                    proceed_button.setVisibility(INVISIBLE);
                    get_phone_button.setVisibility(INVISIBLE);
                    tempListView.remove(selected_order_position);
                    tempArr.remove(selected_order_position);
                    new SellerUpdateOrderStatusProcess(SellerOrderDetailActivity.this).execute(order_id_for_process);
                }
            }
        });
    }

    protected void sendSMSMessage() {

        message="Your order with "+tempArr.get(selected_order_position).getRest_id()+" is Ready. From- RAPIDO";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        REQUEST_CODE_ASK_PERMISSIONS );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(cust_phone, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this,SellerMainActivity.class);
////        intent.putExtra("tempListView",tempListView);
//        startActivity(intent);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(cust_phone, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
