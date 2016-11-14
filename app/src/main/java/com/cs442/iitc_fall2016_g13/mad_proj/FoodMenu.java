package com.cs442.iitc_fall2016_g13.mad_proj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

public class FoodMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardFoodItemList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        MenuItemAdapter ca = new MenuItemAdapter(createList(GlobalVariables.res_data.length));
        recList.setAdapter(ca);
    }

    private List<MenuItemInfo> createList(int size) {

        List<MenuItemInfo> result = new ArrayList<>();
        for (int i=1; i < size; i++) {
            MenuItemInfo ci = new MenuItemInfo();
            ci.mFoodName = GlobalVariables.res_data[i][0];
            ci.mPrice = GlobalVariables.res_data[i][1];
            ci.mDescription = MenuItemInfo.DESCRIPTION_PREFIX + i;
            result.add(ci);
        }

        return result;

    }
}
