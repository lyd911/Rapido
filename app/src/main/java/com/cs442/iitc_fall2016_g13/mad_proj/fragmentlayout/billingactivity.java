package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.Map_distance.MapsActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.Payment.Paypal;
import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.CustomerPlaceOrderProcess;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.cs442.iitc_fall2016_g13.mad_proj.Payment.Paypal.PAYPAL_REQUEST_CODE;


public class billingactivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int PAYMENT_RESULT = 999;
    private static final String TAG = "billingactivity";
    ArrayList<MenuItemObject> mArrayList ;
    ArrayList<MenuItemObject> mBillingArrayList;
    ArrayAdapter<String> mArrayAdapter;
    String currentBilledItemNumbers;

    Double currentItemTotal = 0.0, finalBill = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billingactivity);
        currentItemTotal = 0.0;
        finalBill = 0.0;

        final Context mContext = getApplicationContext();
        ListView mListView = (ListView) findViewById(R.id.myListView);
        TextView billDescriptionTV = (TextView) findViewById(R.id.textView_bill);

        Button btBillingConfirmation = (Button) findViewById(R.id.ButtonConfirmation);

        btBillingConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(finalBill > 0){

//kiran
                    Intent data = new Intent(getApplicationContext(),Paypal.class);
                    data.putExtra("paymentAmount",finalBill);
                    startActivityForResult(data, PAYMENT_RESULT);

//                    SingletonClass.initInstance().getmHistoryArray().add(currentDateTimeString+ "Total amount "+ finalBill.toString() +"$");
                }else{

                    new CustomerPlaceOrderProcess(getApplicationContext()).execute(GlobalVariables.SellerUsername,GlobalVariables.username,GlobalVariables.OrderDetails);//billingConfirmation = "No item selected by you, try again";
                }

                if(mArrayList != null){
                    for(int i=0;i<mArrayList.size();i++){
                        mArrayList.get(i).setmOrderCount(0);
                    }
                }



               /* data.putExtra("RESULT",billingConfirmation);
                setResult(1,data);
                finish();
*/

            }
        });

        mArrayList = SingletonClass.initInstance(getApplicationContext()).getListArray();
        mBillingArrayList = SingletonClass.initInstance(getApplicationContext()).getBillingArrayList();

        fillBillingArrayList();

        new DownloadFilesTask().execute(null,null,null);


        mArrayAdapter = new CustomAdapterBilling(getApplicationContext(),R.layout.billingsimpleview,mBillingArrayList);
        mListView.setAdapter(mArrayAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(this, "onActivityResult billingactivity PAYMENT_RESULT0", Toast.LENGTH_LONG).show();
        if (requestCode == PAYMENT_RESULT) {

            Toast.makeText(this, "onActivityResult billingactivity PAYMENT_RESULT1", Toast.LENGTH_LONG).show();
            if (resultCode == 1) {

                String paymentDeatil,paymentAmount;

                paymentDeatil = data.getStringExtra("paymentDeatil");
                paymentAmount = data.getStringExtra("paymentAmount");


                try {
                    JSONObject jsonDetails = new JSONObject(paymentDeatil);
                    JSONObject jsonResponse = jsonDetails.getJSONObject("response");
                    String paymentState = jsonResponse.getString("state");
                    String paymentId = jsonResponse.getString("id");

                    if(paymentState.compareTo("approved") == 0){

                        String billingDetail = "Payment Successful paymentId"
                                +paymentId+"paymentState"+paymentState;
                        Toast.makeText(this, "Payment Successful paymentId"
                                +paymentId+"paymentState"+paymentState, Toast.LENGTH_LONG).show();
                        updateHistory();
                        sendResultBack(billingDetail);
                        new CustomerPlaceOrderProcess(this).execute(GlobalVariables.SellerUsername,GlobalVariables.username,GlobalVariables.OrderDetails);
                        // upload the order to database

                    }else{

                        Toast.makeText(this, "paymentId"
                                +paymentId+"paymentState"+paymentState, Toast.LENGTH_LONG).show();
                        String billingConfirmation = "Billing not done try again";

/*
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        Set<String> set = editor.putStringSet("HISTORY")
        settings.getStringSet("HISTORY",)*/

                        data.putExtra("RESULT",billingConfirmation);
                        setResult(1,data);
                        finish();


                    }
                } catch (JSONException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                Log.v(TAG,"paymentDetail"+paymentDeatil+"paymentAmount"+paymentAmount);


            }
        }
    }

    public void updateHistory(){


        ArrayList<String> historyArrayList;

        String billingConfirmation;

        historyArrayList = SingletonClass.initInstance(getApplicationContext()).getmHistoryArray();

        billingConfirmation = " Thanks for purchasing your total billed amout is "+ finalBill.toString() +"$"+" billed items are"+currentBilledItemNumbers;
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        if(historyArrayList != null){
            historyArrayList.add((historyArrayList.size()+1)+") "+currentDateTimeString+ "Total amount "+ finalBill.toString() +"$"+"  billed items are"+currentBilledItemNumbers);
            SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            if(historyArrayList != null){
                Set<String> set = new HashSet<String>(historyArrayList);
                editor.putStringSet("HISTORY", set);
                editor.commit();
            }
        }




    }

    public void sendResultBack(String billingDeatil){


        Intent data = new Intent();

        String billingConfirmation = billingDeatil;
/*
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        Set<String> set = editor.putStringSet("HISTORY")
        settings.getStringSet("HISTORY",)*/

        data.putExtra("RESULT",billingConfirmation);
        setResult(1,data);
        finish();
    }

    private class DownloadFilesTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String s) {
            TextView billDescriptionTV = (TextView) findViewById(R.id.textView_bill);
            if(billDescriptionTV != null)
                billDescriptionTV.setText("Total Bill ="+finalBill.toString()+"$");
//            String.format("%.2",finalBill)
        }

        @Override
        protected String doInBackground(String... params) {
            calculateTotalBill();
            return null;
        }
    }


    private void calculateTotalBill() {

        ArrayList<MenuItemObject> mArrayList = SingletonClass.initInstance(getApplicationContext()).getBillingArrayList();

        if(mArrayList != null){

            for (int i = 0; i < mArrayList.size(); i++) {

                MenuItemObject obj = mArrayList.get(i);
                if (obj != null) {
                    String count = String.valueOf(obj.getmOrderCount());
                    currentItemTotal = obj.getmOrderCount() * Double.parseDouble(obj.getmItemPrice());
                    finalBill += currentItemTotal;
                }
            }
        }

        Log.v("final ans",finalBill.toString());
//        return String.format("%.2",finalBill);
    }


    private void fillBillingArrayList(){

        MenuItemObject tempObj;

        mBillingArrayList.clear(); // remove previous data

        if(mArrayList != null){
            for(int i=0;i<mArrayList.size();i++){
                tempObj = mArrayList.get(i);
                if(tempObj.getmOrderCount() > 0){
                    MenuItemObject objToAdd = new MenuItemObject(tempObj.getmItemName(), tempObj.getmItemPrice(),tempObj.getmItemDetail(),tempObj.getmOrderCount());
                    mBillingArrayList.add(objToAdd);
                    if(currentBilledItemNumbers == null){
                        currentBilledItemNumbers = " #"+(i+1);
                    }else{
                        currentBilledItemNumbers += " #"+(i+1);
                    }
                }
            }
        }

    }


    @Override
    public void onBackPressed() {
        Intent data = new Intent();

        String billingConfirmation = "Billing not done try again";

/*
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        Set<String> set = editor.putStringSet("HISTORY")
        settings.getStringSet("HISTORY",)*/

        data.putExtra("RESULT",billingConfirmation);
        setResult(1,data);
        finish();

    }

}