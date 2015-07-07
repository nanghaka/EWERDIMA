package com.ilicit.ewerdima;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.ilicit.ewerdima.app.AppConfig;
import com.ilicit.ewerdima.dialog.ProgressDialogButton;
import com.ilicit.ewerdima.helper.SQLiteHandler;
import com.ilicit.ewerdima.helper.ServiceHandler;
import com.ilicit.ewerdima.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dev on 4/27/2015.
 */
public class LoginActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,View.OnClickListener,ResultCallback<People.LoadPeopleResult> {
    // LogCat tag
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;

    private SQLiteHandler db;
    String email,password,name;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;

    private boolean mSignInClicked;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("please wait....");
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    if(Utils.isNetworkAvailable(LoginActivity.this)) {
                       new  CheckLogin().execute(email,password);

                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Please check on your internet connection.Thank you!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);


    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mSignInClicked = false;
        Toast.makeText(this, "Please wait..", Toast.LENGTH_LONG).show();

        email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        if(Utils.isNetworkAvailable(LoginActivity.this)) {
            if(Utils.getSaved("Token",this).equalsIgnoreCase("")){



                Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);


                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    name = currentPerson.getDisplayName();
                    Log.e("name sent ",">> "+name);
                    if(Utils.isNetworkAvailable(LoginActivity.this)) {

                        new  CheckLogin().execute(email,"google+user");

                      //  new RegisterUser(LoginActivity.this).execute(email, "", name);
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Please check on your internet connection.Thank you!", Toast.LENGTH_LONG)
                                .show();
                    }

                }


            }else{
                new CheckLogin().execute(email, "");
            }


        }else{
            Toast.makeText(getApplicationContext(),
                    "Please check on your internet connection.Thank you!", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (!mIntentInProgress) {
            if (mSignInClicked && connectionResult.hasResolution()) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIntentInProgress = true;
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        }

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.sign_in_button && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }



    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {



    }




    public class CheckLogin extends AsyncTask<String, String, String> {

        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();

        }




        @Override
        protected String doInBackground(String... params) {
            ServiceHandler jsonParser = new ServiceHandler(LoginActivity.this);
            String message = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair(
                    "email", params[0]));
            nameValuePairs.add(new BasicNameValuePair(
                    "password", params[1]));

            String json = jsonParser.makeServiceCall(AppConfig.URL_LOGIN, ServiceHandler.POST,nameValuePairs);

            Log.e("Response sending: ", "> " + json);

            if (json != null && json.length() >0) {
                try {
                    JSONObject jObj = new JSONObject(json);
                    boolean status = true;

                    if(jObj.has("uid")) {



                        session.setLogin(true);

                        Utils.save("email",email,LoginActivity.this);
                        Utils.save("password", password, LoginActivity.this);


                        String uid = jObj.getString("unique_id");


                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String created_at = jObj.getString("created_at");

                        // Inserting row in users table
                        if(db.getRowCount() <= 0){
                            Log.e("added","dcsdf");

                            db.addUser(name, email, uid, created_at);

                        }


                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                       message = jObj.getString("Error");

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data error", "Didn't receive any data from server!");
                message="";
            }

            return message;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideDialog();
            if(result.equalsIgnoreCase("")){

            }else {

                final ProgressDialogButton dialogButton = new ProgressDialogButton(LoginActivity.this, "Error", result);
                dialogButton.setCancelable(false);

                dialogButton.show();
                dialogButton.setOkClickedAction("OK",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogButton.dismiss();


                            }
                        });
            }




        }
    }




    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        }
    }



}
