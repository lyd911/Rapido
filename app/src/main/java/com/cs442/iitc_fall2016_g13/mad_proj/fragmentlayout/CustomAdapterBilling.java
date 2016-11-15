package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.util.ArrayList;

/**
 * Created by KiranCD on 10/2/2016.
 */

public class CustomAdapterBilling extends ArrayAdapter {


    Context mContext;
    ArrayList<MenuItemObject> mArrayList;
    private static String TAG = "CustomAdapterBilling";
    int cursor;
    Double TotalBillAmount = 0.0;
/*
    public CustomAdapterBilling(Context context, int resource,ArrayList<MenuItemObject> array ) {
        super(context, resource,array);
        this.mContext = context;
        mArrayList = SingletonClass.initInstance().getListArray();
        TotalBillAmount = 0.0;
        cursor = 0;
    }
*/

    public CustomAdapterBilling(Context context, int textViewResourceId, ArrayList<MenuItemObject> mArrayList) {
        super(context, textViewResourceId, mArrayList);
        this.mContext = mContext;
        this.mArrayList = mArrayList;

        TotalBillAmount = 0.0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MenuItemObject tempObj;
        View v = convertView;
        if (v == null) {

            v = View.inflate(getContext(), R.layout.rowlayoutbilling, null);

        } else {

            v = convertView;
        }

        TextView tvItem = (TextView) v.findViewById(R.id.txtItemBill);
        TextView tvPrice = (TextView) v.findViewById(R.id.txtPriceBill);

        TextView tvCount = (TextView) v.findViewById(R.id.txtTotalCountBill);
        TextView tvTotalForEach = (TextView) v.findViewById(R.id.txtTotalPrice);
        TextView tvFinalStatement = (TextView) v.findViewById(R.id.textView_bill);

        MenuItemObject obj = mArrayList.get(position);
        if (obj != null) {
            tvItem.setText(obj.getmItemName());
            tvPrice.setText(obj.getmItemPrice()+"$");
            String count = String.valueOf(obj.getmOrderCount());
            Log.v(TAG, "count   " + count );
            tvCount.setText(count);



            Double currentItemTotal = obj.getmOrderCount()* Double.parseDouble(obj.getmItemPrice());
            Log.v(TAG,"obj"+obj.getmOrderCount()+obj.getmItemPrice()+"burrentTotal  "+currentItemTotal.toString());

            TotalBillAmount += currentItemTotal;

            tvTotalForEach.setText(String.format("%.2f",currentItemTotal)+"$");

            SingletonClass.initInstance(getContext()).setmTotalBill(TotalBillAmount);

                String finalStatemnt = "Total bill amount is = "+String.valueOf(TotalBillAmount);
            if(tvFinalStatement != null)
                tvFinalStatement.setText(finalStatemnt);
            return v;

        }

        return v;
    }
}
