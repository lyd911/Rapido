package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by KiranCD on 9/23/2016.
 */

public class CustomAdapter extends ArrayAdapter {

    private static String TAG = "CustomAdapter";
    private ArrayList<MenuItemObject> objects;
    private Context mContext;
    private MainActivity mActivity;


    public CustomAdapter(Context context, int textViewResourceId, ArrayList<MenuItemObject> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.objects = objects;
        mActivity = (MainActivity) context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            v = View.inflate(mContext, R.layout.rowlayout, null);

        } else {

            v = convertView;
        }



        Button tvAdd = (Button) v.findViewById(R.id.buttonAdd);
        Button tvRemove = (Button) v.findViewById(R.id.buttonRemove);




        final MenuItemObject tempObj = objects.get(position);
        final int pos = position;

        if (tempObj != null) {

            if(tvAdd != null){


                tvAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.v(TAG,"add pressed");

                        if(tempObj.getmOrderCount() >= 99){
                            Toast.makeText(getContext(), "You cannot order more than 99", Toast.LENGTH_SHORT).show();

                        }else{

                           // objects.get(pos).setmOrderCount(objects.get(pos).getmOrderCount()+1);
                            tempObj.setmOrderCount(tempObj.getmOrderCount()+1);
                            notifyDataSetChanged();
                            if(mActivity != null)
                                mActivity.onTitleCountUpdated();

                        }

                    }
                });

                tvRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(tempObj.getmOrderCount() <= 0){
                            Toast.makeText(getContext(), "Order count is 0", Toast.LENGTH_SHORT).show();

                        }else{

                            tempObj.setmOrderCount(tempObj.getmOrderCount()-1);
                            notifyDataSetChanged();
                            if(mActivity != null)
                                mActivity.onTitleCountUpdated();


                        }

                    }
                });


            }

            TextView tv = (TextView) v.findViewById(R.id.txtitem);
            TextView tv_price = (TextView) v.findViewById(R.id.txtprice);
            TextView tv_order = (TextView) v.findViewById(R.id.txtCount);
            TextView tv_totalMain = (TextView) v.findViewById(R.id.textViewTotal);

            String toDisplay = String.valueOf(position+1) +". "+ tempObj.getmItemName();

            tv.setText(toDisplay);

            tv_price.setText(tempObj.getmItemPrice()+"$");
            int countInt = tempObj.getmOrderCount();
            String count = String.valueOf(tempObj.getmOrderCount());

//            Log.v(TAG,"count"+count);
            tv_order.setText(count);
            tv_totalMain.setText((countInt*Integer.parseInt(tempObj.getmItemPrice()))+"$");
        }

        return v;
    }

}
