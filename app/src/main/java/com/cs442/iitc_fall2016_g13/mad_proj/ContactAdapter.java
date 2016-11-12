package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sandra Tobias on 11/11/2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<RestaurantInfo> restaurantList;

    public ContactAdapter(List<RestaurantInfo> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        RestaurantInfo ci = restaurantList.get(i);
        contactViewHolder.vName.setText(ci.mName);
        contactViewHolder.vCuisine.setText(ci.mCuisine);
        contactViewHolder.vBusyness.setText(ci.mBusyness);
        contactViewHolder.vDistance.setText(ci.mPriceRange);
        contactViewHolder.vMinSpend.setText(ci.mMinSpend);
        contactViewHolder.vPriceRange.setText(ci.mPriceRange);
        contactViewHolder.vRating.setText(ci.mRating);
        contactViewHolder.vReview.setText(ci.mReview);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vCuisine;
        protected TextView vBusyness;
        protected TextView vMinSpend;
        protected TextView vDistance;
        protected TextView vRating;
        protected TextView vReview;
        protected TextView vPriceRange;


        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.restaurant_title);
            vCuisine = (TextView)  v.findViewById(R.id.cuisine);
            vBusyness = (TextView)  v.findViewById(R.id.busyness);
            vMinSpend = (TextView) v.findViewById(R.id.min_spend);
            vDistance =  (TextView) v.findViewById(R.id.distance);
            vRating = (TextView)  v.findViewById(R.id.rating);
            vReview = (TextView)  v.findViewById(R.id.reviews);
            vPriceRange = (TextView) v.findViewById(R.id.price_range);
            v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                }
            });

        }
    }
}
