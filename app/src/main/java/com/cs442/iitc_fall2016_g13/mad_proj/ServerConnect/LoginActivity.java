package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.cs442.iitc_fall2016_g13.mad_proj.MainActivity;
        import com.cs442.iitc_fall2016_g13.mad_proj.R;

public class LoginActivity extends AppCompatActivity {
    public static int err=0;
    EditText username,password;
    Button login;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.input_login_username);
        password = (EditText)findViewById(R.id.input_login_password);



        login = (Button)findViewById(R.id.btn_login);
        signup = (TextView) findViewById(R.id.link_signup);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pwd = password.getText().toString();
                if(uname.equals("")||pwd.equals(""))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {

                    new LoginProcess(v.getContext()).execute(uname, pwd);
                    if (err == 0) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

            }
        });
    }


}
