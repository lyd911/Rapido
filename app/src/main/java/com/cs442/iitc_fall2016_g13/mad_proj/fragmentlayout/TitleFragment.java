package com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.R;

import java.util.ArrayList;

/**
 * Created by KiranCD on 9/30/2016.
 */

public class TitleFragment extends ListFragment {



    final String TAG = "TitleFragment";

    CustomAdapter mAdapter;

    ViewGroup rootView;
    OnListItemClicked mCallback;
    onOrder mOrderCallback;
    onCountUpdated mCountUpdateCallback;
    historyButtonClicked mHistoryBtn;

    TextView totalTextViewDetail = null;

    ArrayList<MenuItemObject> mArrayList ;


    public interface OnListItemClicked {
        public void TitleFragmentListClicked(int position, ArrayList<MenuItemObject> arrayList);
    }

    public interface onOrder{
        public void onOrderClicked();
    }

    public interface onCountUpdated{
        public void onTitleCountUpdated();
    }

    public interface historyButtonClicked{
        public void onHistoryClicked();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mAdapter != null){

            mAdapter.notifyDataSetChanged();
            updateFinalPriceInTitleFragment();
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnListItemClicked) context;
            mOrderCallback = (onOrder) context;
            mCountUpdateCallback = (onCountUpdated) context;
            mHistoryBtn = (historyButtonClicked) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnListItemClicked");
        }
    }

    void getDescriptionForEnteredItem(){

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Enter New Menu");

        alert.setCancelable(false);
//        alert.setMessage("Enter menu name and describe about food");


        final LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);


        final EditText menuName = new EditText(getActivity());
        menuName.setSingleLine(true);
        menuName.setHint("Enter Menu Name");
        final EditText menuDetail = new EditText(getActivity());
        final EditText menuPrice = new EditText(getActivity());
        menuPrice.setHint("Enter Price In $");
        menuPrice.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        menuDetail.setHint("Enter Menu Detail");
        layout.addView(menuName);
        layout.addView(menuPrice);
        layout.addView(menuDetail);

        alert.setView(layout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(menuName.getText().toString().trim().length() > 0 && menuPrice.getText().toString().trim().length() > 0) {

                    Log.v(TAG,menuPrice.getText().toString()+"Kiran");
                    MenuItemObject menuToAdd = new MenuItemObject(menuName.getText().toString(),menuPrice.getText().toString(),menuDetail.getText().toString(),0);
                    mArrayList.add(menuToAdd);
                    mAdapter.notifyDataSetChanged();
                    updateFinalPriceInTitleFragment();
                }
            }
        });


        AlertDialog displayDialog = alert.create();
        displayDialog.show();


    }



    public void clearAllCount(){

        if(mArrayList != null){
            for(int i=0;i<mArrayList.size();i++){

                mArrayList.get(i).setmOrderCount(0);
            }
            mAdapter.notifyDataSetChanged();
        }
        updateFinalPriceInTitleFragment();

    }


    public void notifyAdapterInList(){

        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }

        updateFinalPriceInTitleFragment();

    }


    public void updateFinalPriceInTitleFragment(){

        TextView totalTextViewDetail = (TextView) rootView.findViewById(R.id.textView5total);
        if(totalTextViewDetail != null){

            int count = 0;
            int price= 0;
            int TotalFinalPrice= 0;
            if(mArrayList != null){
                for(int i=0;i<mArrayList.size();i++){

                    count = mArrayList.get(i).getmOrderCount();
                    if(count > 0){
                        price = Integer.parseInt(mArrayList.get(i).getmItemPrice());
                        TotalFinalPrice += (price * count);
                    }
                    count = 0;
                    price = 0;
                }

            }

            String updateVal = "Total ="+Integer.toString(TotalFinalPrice)+"$ ";

            totalTextViewDetail.setText(updateVal);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        setRetainInstance(false);
        mArrayList = SingletonClass.initInstance(getContext()).getListArray();
        rootView = (ViewGroup) inflater.inflate(R.layout.linearlayoutwithlistview, container, false);
        // Create an array of string to be data source of the ListFragment


        // Create ArrayAdapter object to wrap the data source
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.rowlayout,R.id.txtitem,datasource);
        mAdapter = new CustomAdapter(getActivity(),R.layout.rowlayout,mArrayList);

      /*  Button buttonAddNewItem = (Button) rootView.findViewById(R.id.button2);*/

        totalTextViewDetail = (TextView) rootView.findViewById(R.id.textView5total);

//        totalTextView.setText("hahhahaha");

        updateFinalPriceInTitleFragment();
        Button historyButton = (Button) rootView.findViewById(R.id.buttonHistory);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mHistoryBtn.onHistoryClicked();



            }
        });

/*        buttonAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDescriptionForEnteredItem();
            }
        });*/

        Button buttonClear = (Button) rootView.findViewById(R.id.buttonClear);

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clearAllCount();
                mCountUpdateCallback.onTitleCountUpdated();
//                updateFinalPriceInTitleFragment();
            }
        });

        Button buttonOrder = (Button) rootView.findViewById(R.id.buttonOrder);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mOrderCallback.onOrderClicked();

            }
        });

        // Bind adapter to the ListFragment
        setListAdapter(mAdapter);
        //  Retain the ListFragment instance across Activity re-creation
        //setRetainInstance(true);
        return rootView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //return false;
                Log.v("TAG","OnItemLongClick listener Position"+position);
                return true;
            }
        });

        //getListView().setOn

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ViewGroup view = (ViewGroup) v;



        MenuItemObject tempMenuObj = mArrayList.get(position);
        if(tempMenuObj != null){

//            Toast.makeText(getActivity(), tempMenuObj.getmItemDetail(), Toast.LENGTH_LONG).show();

        }else{

            Toast.makeText(getActivity(), "Description Not Available", Toast.LENGTH_LONG).show();

        }

        mCallback.TitleFragmentListClicked(position,mArrayList);

    }



}
