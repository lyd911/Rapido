package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs442.iitc_fall2016_g13.mad_proj.R;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.FB_SignupProcess;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;

public class FB_Signup extends AppCompatActivity {
    public static int err=0;
    EditText username,name,address,phone;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb__signup);
        username = (EditText)findViewById(R.id.input_email);
        username.setText(GlobalVariables.username);

        name = (EditText)findViewById(R.id.input_name);
        name.setText(GlobalVariables.FB_Name);

        address = (EditText)findViewById(R.id.input_address);
        phone = (EditText)findViewById(R.id.input_phone);

        signup = (Button)findViewById(R.id.btn_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();

                String nm = name.getText().toString();
                String addr = address.getText().toString();
                String ph = phone.getText().toString();

                new FB_SignupProcess(v.getContext()).execute(uname, nm, ph, addr);



            }
        });
    }


}
