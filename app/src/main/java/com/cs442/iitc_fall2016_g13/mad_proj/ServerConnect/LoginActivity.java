package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.cs442.iitc_fall2016_g13.mad_proj.MainActivity;
        import com.cs442.iitc_fall2016_g13.mad_proj.NearbyPlaces;
        import com.cs442.iitc_fall2016_g13.mad_proj.R;
        import com.facebook.AccessToken;
        import com.facebook.AccessTokenTracker;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.Profile;
        import com.facebook.ProfileTracker;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    TextView signup;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FacebookCallback<LoginResult> mCallback=new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken=loginResult.getAccessToken();
            Profile profile= Profile.getCurrentProfile();
            displayWelcomeMessage(profile);
            gotoMainActivity();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AccessTokenTracker tracker =new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(AccessToken.getCurrentAccessToken()==null)
                {
                    Toast.makeText(getApplicationContext(),"Log out",Toast.LENGTH_SHORT).show();
                }
            }
        };

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayWelcomeMessage(newProfile);
            }
        };

        setContentView(R.layout.activity_login);


        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager,mCallback);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");

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

                }

            }
        });
    }

    public void displayWelcomeMessage(Profile profile){
        if(profile!=null){
            Toast.makeText(getApplicationContext(),"Welcome "+profile.getName(),Toast.LENGTH_LONG).show();
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
