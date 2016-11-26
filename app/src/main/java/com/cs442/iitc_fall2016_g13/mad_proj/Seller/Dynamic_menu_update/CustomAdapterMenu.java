package com.cs442.iitc_fall2016_g13.mad_proj.Seller.Dynamic_menu_update;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.util.ArrayList;

/**
 * Created by KiranCD on 11/17/2016.
 */

public class CustomAdapterMenu extends ArrayAdapter {


    private static final String TAG = "CustomAdapterMenu";
    private Context mContext;

    TextView tvMenuName;
    TextView tvPrice;
    TextView tvIngredients;
    ArrayList<MenuItems> mMenuItems;


    public CustomAdapterMenu(Context context, int textViewResourceId, ArrayList<MenuItems> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mMenuItems = objects;

    }

    public void setmMenuItems(ArrayList<MenuItems> mMenuItems) {
        this.mMenuItems = mMenuItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        Log.v(TAG,"position "+position);

        if (v == null) {

            v = View.inflate(mContext, R.layout.menu_list_view_item, null);

        } else {

            v = convertView;
        }

        tvMenuName = (TextView) v.findViewById(R.id.menuNameTextView);
        tvPrice = (TextView) v.findViewById(R.id.priceTextView);
        tvIngredients = (TextView) v.findViewById(R.id.descriptionTextView);
        final MenuItems tempObj = mMenuItems.get(position);
        final int pos = position;


        if(tempObj != null){
            if(tvMenuName != null){
                // Toast.makeText(getContext(), "restaurnat", Toast.LENGTH_SHORT).show();
                tvMenuName.setText(tempObj.getmMenuName());
                tvPrice.setText(tempObj.getmPrice()+"$");
                tvIngredients.setText(tempObj.getmMenuDescription());
            }
        }

        return v;
    }
}
