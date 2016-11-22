package com.cs442.iitc_fall2016_g13.mad_proj.Seller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs442.iitc_fall2016_g13.mad_proj.R;

/**
 * Created by lyd on 2016/11/17.
 */

public class SellerGetMenuListActivity extends Activity {

    public static String orderid;
    public static TextView menu_list_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_get_menu_list);

        TextView title_textview = (TextView)findViewById(R.id.title_textview);
        menu_list_textview = (TextView)findViewById(R.id.menu_list_textview);
        final EditText order_id_edittext = (EditText)findViewById(R.id.order_id_editText);
        Button get_menu_list_button = (Button)findViewById(R.id.QR_reader_button);

        get_menu_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderid = order_id_edittext.getText().toString();
                order_id_edittext.setText("");

                new SellerGetMenuListProcess(SellerGetMenuListActivity.this).execute(orderid);
            }
        });
    }

    public static void setMenuList(String menu){
        menu_list_textview.setText(menu);
    }

}
