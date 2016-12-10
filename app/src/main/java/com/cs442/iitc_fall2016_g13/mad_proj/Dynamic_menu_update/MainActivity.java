package com.cs442.iitc_fall2016_g13.mad_proj.Dynamic_menu_update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.Seller.Dynamic_menu_update.SingletonClass2;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.cs442.iitc_fall2016_g13.mad_proj.SimpleShowcaseEventListener;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.MenuAndCartActivity;
import com.cs442.iitc_fall2016_g13.mad_proj.fragmentlayout.SingletonClass;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityDynamic";
    String dbKeyRestaurants = "RESTAURANTS";
    String dbKeyRestaurantName = "RESTAURANT_NAME";
    String dbKeyPrice = "PRICE";
    String dbKeyIngredients = "INGREDIENTS";
    String dbKeyRestaurantMenu = "MENU";
    TextView mMenuNameTxt;
    TextView mMenuPrieTxt;
    TextView mMenuIngredientsTxt;
    CustomAdapterMenu mArrayAdapter;
    RestaurantMenu mRestaurantMenu;
    Context mContext;
    boolean mFlag = true;
    Double mLatitude, mLongitude;
    String mRestautantName;
    String mLatLongString;
    ProgressDialog dialog;
    boolean flag;

    ListView mListView;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_updatemenu);
        key= GlobalVariables.SelectedRestaurantName;
        flag = false;

        Intent intent = getIntent();

        mLatitude = intent.getDoubleExtra("LAT",0.0);
        Log.v(TAG,"latitutde"+mLatitude);
        mLongitude = intent.getDoubleExtra("LON",0.0);
        mRestautantName = intent.getStringExtra("restaurantName");

        Log.v(TAG,"mRestautantName"+mRestautantName);
        mLatLongString = String.valueOf(mLatitude)+":"+String.valueOf(mLongitude);
        mLatLongString = mLatLongString.replace('.','_');
        Log.v(TAG,"mLatLongString"+mLatLongString);

        mContext = this;
        TextView restaurantName = (TextView) findViewById(R.id.txtRestaurantName);
        restaurantName.setText(mRestautantName);

        if(flag == false)
        dialog = ProgressDialog.show(this, "",
                "Loading. Please wait...", true);
        LoadMenuListView();

        Button mBtnRrefresh = (Button) findViewById(R.id.btnRefreshMenu);
        mBtnRrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRestaurantMenu == null ||mRestaurantMenu.getmMenuList().size() <= 0 ){

                    Toast.makeText(getApplicationContext(),"There is no menu for this pls add some menu", Toast.LENGTH_SHORT).show();

                    return;
                }
                Log.v(TAG,"mLongitude"+mLongitude+"mLatitude"+mLatitude);
                SingletonClass.initInstance(mContext).updateArray(mRestaurantMenu.getmMenuList());
                SingletonClass.initInstance(mContext).setmLong(mLongitude);
                SingletonClass.initInstance(mContext).setmLat(mLatitude);
                Intent intent = new Intent(v.getContext(), MenuAndCartActivity.class); //Menu and Cart.class was launched here.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_help, menu);
       // TextView drawer_name=(TextView)findViewById(R.id.drawer_name);
        //drawer_name.setText("Kiran - ");
        return true;
    }

    void LoadMenuListView(){


        Log.v(TAG,"LoadMenuListView");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(dbKeyRestaurants);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.v(TAG,"onDataChange");
                if(mFlag == false){
                    Log.v(TAG,"mFlag is false");
                    return;

                }

//               dataSnapshot.child("latlong");

                for (DataSnapshot latlong : dataSnapshot.getChildren())
                {
                    if(key.compareTo(latlong.getKey().toString())== 0){


                        Log.v(TAG,"mLatLongString is equal to data base ");
                        ArrayList<MenuItems> menuItems = new ArrayList<MenuItems>();

                        Log.v(TAG,"Location:"+latlong.getKey());
                        Log.v(TAG,"Restaurant Name:"+latlong.child(dbKeyRestaurantName).getValue());
                        DataSnapshot menu = latlong.child(dbKeyRestaurantMenu);
                        for (DataSnapshot menuIttr : menu.getChildren()) {

                            String menuName = menuIttr.getKey();
                            String price = menuIttr.child(dbKeyPrice).getValue().toString();
                            String description = menuIttr.child(dbKeyIngredients).getValue().toString();
                            Log.v(TAG,"menuName"+menuName+"price"+price+"description"+description);
//                            String description = "junk";
                            menuItems.add(new MenuItems(menuName,price,description));
                        }

                        Log.v(TAG,"Location: mLatitude"+mLatitude+"mLongitude"+mLongitude);
                        if(mRestaurantMenu == null){

                            Log.v(TAG,"mRestaurantMenu created");
                            mRestaurantMenu = new RestaurantMenu(latlong.getKey(),menuItems);
                            mRestaurantMenu.setmLatitude(mLatitude);
                            mRestaurantMenu.setmLongitude(mLongitude);
                        }else{
                            Log.v(TAG,"mRestaurantMenu cleared and updated");
                            mRestaurantMenu.clearRestaurantMenu();
                            mRestaurantMenu.setmRestaurantName(latlong.getKey());
                            mRestaurantMenu.setmMenuList(menuItems);
							mRestaurantMenu.setmLatitude(mLatitude);
                            mRestaurantMenu.setmLongitude(mLongitude);

                        }

                    }
                }

                //refreshMenu();

                if(flag == false && dialog != null){
                    dialog.dismiss();
                    flag = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {


            showcaseShowMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showcaseShowMenu() {
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setContentTitle("Order Food")
                .setContentText("This button will take you to choose menu in Restaurant")
                .setStyle(R.style.CustomShowcaseTheme3)
                .setTarget(new ViewTarget(R.id.btnRefreshMenu, this))
                .setShowcaseEventListener(
                        new SimpleShowcaseEventListener() {
                            @Override
                            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                //currentShowcase++;
                                //  showcase();

                            }
                        }
                )
                .build();
    }


    void refreshMenu(){

        Log.v(TAG,"refresh Menu");
        if(mRestaurantMenu != null){

            if(mArrayAdapter == null){

                Log.v(TAG,"new adapter");
                mArrayAdapter = new CustomAdapterMenu(getApplicationContext(), R.layout.menu_list_view_item, mRestaurantMenu.getmMenuList());
             //   mListView.setAdapter(mArrayAdapter);

            }else{

                Log.v(TAG,"updating  adapter");
                //mArrayAdapter.setmMenuItems(mRestaurantMenu.getmMenuList());
                mArrayAdapter.notifyDataSetChanged();

            }

        }


    }
}
