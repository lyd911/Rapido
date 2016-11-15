package com.example.sandratobias.mad_project;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

   EditText username,password,conf_password,name,address,phone;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText)findViewById(R.id.userName_editText);
        password = (EditText)findViewById(R.id.password_editText);

        conf_password = (EditText)findViewById(R.id.confirm_editText);

        name = (EditText)findViewById(R.id.name_editText);

        address = (EditText)findViewById(R.id.address_editText);

        phone = (EditText)findViewById(R.id.phone_editText);

        signup = (Button)findViewById(R.id.sign_up_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pwd = password.getText().toString();
                String cnf_pwd = conf_password.getText().toString();
                String nm = name.getText().toString();
                String addr = address.getText().toString();
                String ph = phone.getText().toString();


                new SignUpProcess(v.getContext()).execute(uname,pwd,nm,addr,ph);
            }
        });
    }


}
