package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.util.ArrayList;

/**
 * Created by KiranCD on 10/1/2016.
 */

public class TitleFragmentDetail extends Fragment {

    onBackKeyPressed mCallback;
    onCountChanged mCountCallback;
    final static String TAG = "TitleFragmentDetail";
    int mPosition = -1;
    ArrayList<MenuItemObject> mArray;

    TextView tv_count;

    TextView tv_current_total ;

    public interface onBackKeyPressed {
        public void TitleFragmentDetailBackButtonPressed();
    }

    public interface onCountChanged {
        public void TitleFragmentDetailCountChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (onBackKeyPressed) context;
            mCountCallback = (onCountChanged) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnListItemClicked");
        }
    }

    public void updateCount(int position) {


//        final TextView tv_count = (TextView) getActivity().findViewById(R.id.detailCountText);

        mArray = SingletonClass.initInstance(getContext()).getListArray();
        mPosition = position;


        if (mPosition >= 0 && mArray != null) {
/*
            String DisPlay = Integer.toString(mArray.get(mPosition).getmOrderCount());
            tv_count.setText(DisPlay);
*/

            int itemCount = mArray.get(mPosition).getmOrderCount();
            int itemPrice = Integer.parseInt(mArray.get(mPosition).getmItemPrice());
            int currentItemTotal =itemPrice * itemCount;
            if(tv_count != null)
                tv_count.setText(Integer.toString(mArray.get(mPosition).getmOrderCount()));

            if(tv_current_total != null)
                tv_current_total.setText("Total (Qty) "+itemCount+"* (Item Price)  "+itemPrice+"$ = "+currentItemTotal+"$");




        }



    }

    public void updateView(String s) {

        TextView tv = (TextView) getActivity().findViewById(R.id.detailtextview);

        Log.v(TAG, "updateView " + s);
        if (tv != null)
            tv.setText(s);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        setRetainInstance(false);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.detaillinearlayout, container, false);

        Button bt_plus = (Button) rootView.findViewById(R.id.detailAddButton);
        Button bt_reduce = (Button) rootView.findViewById(R.id.detailReduceButton);
        tv_count = (TextView) rootView.findViewById(R.id.detailCountText);
        tv_current_total = (TextView) rootView.findViewById(R.id.textViewTotalPerItem);


        mArray = SingletonClass.initInstance(getContext()).getListArray();

        if (mPosition >= 0 && mArray != null){

            int itemCount = mArray.get(mPosition).getmOrderCount();
            int itemPrice = Integer.parseInt(mArray.get(mPosition).getmItemPrice());
            int currentItemTotal =itemPrice * itemCount;
            if(tv_count != null)
                tv_count.setText(Integer.toString(mArray.get(mPosition).getmOrderCount()));

            if(tv_current_total != null){
                tv_current_total.setText("Total (Qty) "+itemCount+"* (Item Price)  "+itemPrice+"$ = "+currentItemTotal+"$");

            }
        }


        if (bt_plus != null)
            bt_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = 0;
                    if (mPosition >= 0 && mArray != null) {

                        count = mArray.get(mPosition).getmOrderCount() + 1;
                        if (count < 0 || count > 99) {
                            Toast.makeText(getActivity(), "Already reached extreme count values 0to99", Toast.LENGTH_SHORT).show();
                        } else {
                            mArray.get(mPosition).setmOrderCount(count);
                            if (tv_count != null)
                                tv_count.setText(Integer.toString(count));

                            if (mCountCallback != null)
                                mCountCallback.TitleFragmentDetailCountChanged();

                            // calculate current total

                            int itemCount = mArray.get(mPosition).getmOrderCount();
                            int itemPrice = Integer.parseInt(mArray.get(mPosition).getmItemPrice());
                            int currentItemTotal =itemPrice * itemCount;

                            if(tv_current_total != null){
                                tv_current_total.setText("Total (Qty) "+itemCount+"* (Item Price)  "+itemPrice+"$ = "+currentItemTotal+"$");

                            }


                        }
                    }

                }
            });

        if (bt_reduce != null)
            bt_reduce.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    int count = 0;
                    if (mPosition >= 0 && mArray != null) {
                        count = mArray.get(mPosition).getmOrderCount() - 1;
                        if (count < 0 || count > 99) {
                            Toast.makeText(getActivity(), "Already reached extreme count values 0to99", Toast.LENGTH_SHORT).show();
                        } else {
                            mArray.get(mPosition).setmOrderCount(count);
                            if (tv_count != null)
                                tv_count.setText(Integer.toString(count));

                            if (mCountCallback != null)
                                mCountCallback.TitleFragmentDetailCountChanged();



                            int itemCount = mArray.get(mPosition).getmOrderCount();
                            int itemPrice = Integer.parseInt(mArray.get(mPosition).getmItemPrice());
                            int currentItemTotal =itemPrice * itemCount;

                            if(tv_current_total != null){
                                tv_current_total.setText("Total (Qty) "+itemCount+"* (Item Price)  "+itemPrice+"$ = "+currentItemTotal+"$");

                            }
                        }


                    }

                }
            });

        Bundle b = getArguments();
        String s;


/*
        if(rootView.findViewById(android.R.id.content).getTag().equals("big_screen")){

            rootView.findViewById(R.id.back_to_list_button).setVisibility(View.INVISIBLE);
        }*/
        if (b != null) {

            s = b.getString("Description");
            mPosition = b.getInt("POSITION");
            if (mPosition == -1) {
                s = "Click on items to display description";
            } else {
                updateCount(mPosition);
            }
            Log.v(TAG, "b is not null" + s);

        } else {

            s = "Click on items to display description";

        }

        TextView tv = (TextView) rootView.findViewById(R.id.detailtextview);
        tv.setText(s);
/*
        Button button = (Button) rootView.findViewById(R.id.back_to_list_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.TitleFragmentDetailBackButtonPressed();

            }
        });*/
        //setRetainInstance(true);
        return rootView;

    }

    void CalculateTotal(){

    }


}
