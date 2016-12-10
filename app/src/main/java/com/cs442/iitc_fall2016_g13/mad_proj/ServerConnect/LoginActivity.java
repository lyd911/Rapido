package com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect;

        import android.Manifest;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.os.Handler;
        import android.preference.PreferenceManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.cs442.iitc_fall2016_g13.mad_proj.Map_distance.MapsActivity;
        import com.cs442.iitc_fall2016_g13.mad_proj.R;
        import com.cs442.iitc_fall2016_g13.mad_proj.Seller.SellerMainActivity;
        import com.cs442.iitc_fall2016_g13.mad_proj.Seller.SellerSignUp;
        import com.facebook.AccessToken;
        import com.facebook.AccessTokenTracker;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.HttpMethod;
        import com.facebook.Profile;
        import com.facebook.ProfileTracker;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    TextView signup,SellerSignup;
    public static String admin="";
    public static String UserName="";
    public static final String PREFS_NAME = "MyApp_Settings";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private int REQUEST_CODE_ASK_PERMISSIONS_LOC=0;
    private RadioButton SellerRadio,CustomerRadio;

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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS_LOC );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Reading from SharedPreferences
        String username1 = settings.getString("username", "");
        String usertpye=settings.getString("usertype","");
        if (!username1.equals(null) && username1.length() > 0) {
            //System.out.println("reading username: "+username1);
            Toast.makeText(getApplicationContext(), "Welcome Back: " + username1, Toast.LENGTH_SHORT).show();
            if(usertpye.equals("cust")) {
                GlobalVariables.username=username1;
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            }
            else if(usertpye.equals("seller"))
            {
                GlobalVariables.SellerUsername=username1;
                Intent intent = new Intent(this, SellerMainActivity.class);
                startActivity(intent);
            }
            finish();

        } else {
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            AccessTokenTracker tracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    if (AccessToken.getCurrentAccessToken() == null) {
                        Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_SHORT).show();
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

            loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions("user_friends");
            loginButton.registerCallback(callbackManager, mCallback);
            loginButton.setReadPermissions("public_profile", "email", "user_friends");

            //loginButton.callOnClick();

            username = (EditText) findViewById(R.id.input_login_username);
            password = (EditText) findViewById(R.id.input_login_password);

            SellerRadio = (RadioButton) findViewById(R.id.SellerRadio);
            CustomerRadio = (RadioButton) findViewById(R.id.CustomerRadio);
            SellerSignup = (TextView) findViewById(R.id.Sellerlink_signup);

            login = (Button) findViewById(R.id.btn_login);
            signup = (TextView) findViewById(R.id.link_signup);
            SellerSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), SellerSignUp.class);
                    startActivity(i);
                }
            });


            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(i);
                }
            });

            final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.LoginRadioGroup);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uname = username.getText().toString();
                    String pwd = password.getText().toString();
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    if (uname.equals("") || pwd.equals("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (checkedRadioButtonId == R.id.SellerRadio) {
                        new SellerLoginProcess(v.getContext()).execute(uname, pwd);
                    } else {
                        new LoginProcess(v.getContext()).execute(uname, pwd);
                    }

                /*else if(SellerRadio.getText().toString().equals("Restaurant Owner")){
                    new SellerLoginProcess(v.getContext()).execute(uname, pwd);
                }
                else {
                    new LoginProcess(v.getContext()).execute(uname, pwd);
                }*/

                }
            });

        }
    }

    public void displayWelcomeMessage(Profile profile){
        if(profile!=null){
            Toast.makeText(getApplicationContext(),"Welcome "+profile.getName(),Toast.LENGTH_LONG).show();
            GlobalVariables.username=profile.getId();
            GlobalVariables.FB_Name=profile.getName();

        }
    }

    private void gotoMainActivity() {
//        System.out.println("Checking FB");
        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", GlobalVariables.username);
        editor.putString("usertype","cust");
        editor.commit();
        new FB_Check(this).execute(GlobalVariables.username);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public static void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }
    public void onResume()
    {
        super.onResume();

    }
}
