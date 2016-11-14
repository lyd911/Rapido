package com.cs442.iitc_fall2016_g13.mad_proj;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sandra Tobias on 11/11/2016.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ContactViewHolder> {

    private List<MenuItemInfo> menuItemList;

    public MenuItemAdapter(List<MenuItemInfo> menuItemList) {
        this.menuItemList = menuItemList;
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        MenuItemInfo mii = menuItemList.get(i);
        contactViewHolder.vFoodName.setText(mii.mFoodName);
        contactViewHolder.vPrice.setText(mii.mPrice);
        contactViewHolder.vDescription.setText(mii.mDescription);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_menu_item, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vFoodName;
        protected TextView vPrice;
        protected TextView vDescription;

        public ContactViewHolder(View v) {
            super(v);
            vFoodName = (TextView) v.findViewById(R.id.menu_item_title);
            vPrice = (TextView) v.findViewById(R.id.menu_item_price);
            vDescription = (TextView) v.findViewById(R.id.menu_item_description);
        }
    }
}
