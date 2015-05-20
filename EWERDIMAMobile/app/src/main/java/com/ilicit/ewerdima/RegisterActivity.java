package com.ilicit.ewerdima;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.ilicit.ewerdima.app.AppConfig;
import com.ilicit.ewerdima.app.AppController;
import com.ilicit.ewerdima.helper.*;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dev on 4/27/2015.
 */
public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("please wait....");
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(Utils.isNetworkAvailable(RegisterActivity.this)) {

                        new RegisterUser().execute(email,password,name);
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Please check on your internet connection.Thank you!", Toast.LENGTH_LONG)
                                .show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public class RegisterUser extends AsyncTask<String, String, String> {

        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();

        }




        @Override
        protected String doInBackground(String... params) {
            com.ilicit.ewerdima.helper.ServiceHandler jsonParser = new com.ilicit.ewerdima.helper.ServiceHandler(RegisterActivity.this);
            String message = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair(
                    "email", params[0]));
            nameValuePairs.add(new BasicNameValuePair(
                    "password", params[1]));
            nameValuePairs.add(new BasicNameValuePair(
                    "name", params[2]));

            String json = jsonParser.makeServiceCall(AppConfig.URL_REGISTER, com.ilicit.ewerdima.helper.ServiceHandler.POST,nameValuePairs);

            Log.e("Response sending: ", "> " + json);

            if (json != null && json.length() >0) {
                try {
                    JSONObject jObj = new JSONObject(json);
                    boolean status = true;

                    if(jObj.has("uid")) {



                        session.setLogin(true);


                        String uid = jObj.getString("unique_id");


                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String created_at = jObj.getString("created_at");

                        // Inserting row in users table


                            db.addUser(name, email, uid, created_at);



                        // Launch main activity
                        Intent intent = new Intent(RegisterActivity.this,
                                LoginActivity.class);
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

                Toast.makeText(getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
            }




        }
    }


}
