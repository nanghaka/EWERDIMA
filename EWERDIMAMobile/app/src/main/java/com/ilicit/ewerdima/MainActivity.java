package com.ilicit.ewerdima;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ilicit.ewerdima.dialog.ProgressDialogButton;
import com.ilicit.ewerdima.helper.GPSService;
import com.ilicit.ewerdima.helper.SQLiteHandler;
import com.ilicit.ewerdima.helper.ServiceHandler;
import com.ilicit.ewerdima.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MainActivity extends Activity {
    public static final String GCM_SENDER_ID = "1074367254642";

    public static final String PREFS_NAME = "Ewerdima_GCM";
    public static final String PREFS_PROPERTY_REG_ID = "registration_id";
    GoogleCloudMessaging gcm;
    String regid;
    SharedPreferences prefs;

    private String REG_URL = "http://ewerdima.cloudapp.net/index.php/api/rest/register";
    double latitude;
    double longitude;

    private Button ReportCrime;

    private TextView txtName;
  //  private TextView txtEmail;
    private Button btnLogout, addFriends;

    private SQLiteHandler db;
    private SessionManager session;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
     //   txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        ReportCrime = (Button) findViewById(R.id.Reportbutton);
        addFriends = (Button) findViewById(R.id.btnfriends);

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gcm = GoogleCloudMessaging.getInstance(this);

        if(getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("gcm")) {

                String mes = getIntent().getExtras().getString("title");
                final ProgressDialogButton dialogButton = new ProgressDialogButton(MainActivity.this, "Ewerdima Alert", mes);
                dialogButton.setCancelable(false);

                dialogButton.show();
                dialogButton.setOkClickedAction("OK",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogButton.dismiss();
                                getIntent().getExtras().clear();

                            }
                        });


            }
        }




        GPSService mGPSService = new GPSService(MainActivity.this);
        mGPSService.getLocation();






        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            latitude = mGPSService.getLatitude();
            longitude = mGPSService.getLongitude();
            // Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            // address = mGPSService.getLocationAddress();
        }


// make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        final HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
      //  String email = user.get("email");
        uid = user.get("uid");


        // Displaying the user details on the screen
        txtName.setText(name);
        //txtEmail.setText(email);

        //Report Crime Button
        ReportCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reportIntent = new Intent(MainActivity.this, ReportFormActivity.class);

                reportIntent.putExtra("uid", user.get("uid"));

                startActivity(reportIntent);
            }
        });

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        if (getRegistrationId().equalsIgnoreCase("")) {
            registerInBackground();
        } else {
            if (isOnline()) {

                sendInBackground(getRegistrationId());
            }
        }


        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(intent);

            }
        });

    }


    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                Log.e("EWERDIMA", "Registration back");
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    regid = gcm.register(GCM_SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    if (isOnline()) {
                        sendInBackground(regid);
                    }
                    storeRegistrationId(regid);


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.e("EWERDIMA", msg);
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }


    private void storeRegistrationId(String regId) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_PROPERTY_REG_ID, regId);
        Log.i("Ewerdima", "REGIDXXXXXXXX" + regId);
        editor.commit();
    }

    private String getRegistrationId() {
        String reg = "";
        reg = prefs.getString(PREFS_PROPERTY_REG_ID, "");
        return reg;
    }


    private void sendInBackground(String reg) {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        new SendDetails().execute(reg, String.valueOf(latitude), String.valueOf(longitude), uid);

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public class SendDetails extends AsyncTask<String, String, String> {

        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        @Override
        protected String doInBackground(String... params) {
            ServiceHandler jsonParser = new ServiceHandler(MainActivity.this);
            String message = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair(
                    "gcm_regid", params[0]));
            nameValuePairs.add(new BasicNameValuePair(
                    "devicelat", params[1]));
            nameValuePairs.add(new BasicNameValuePair(
                    "devicelong", params[2]));
            nameValuePairs.add(new BasicNameValuePair(
                    "useruniqueid", params[3]));


            String json = jsonParser.makeServiceCall(REG_URL, ServiceHandler.POST, nameValuePairs);

            Log.e("Response sending: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj.length() != 0) {
                        if (jsonObj.getInt("success") > 0) {
                            Log.e("error", "" + jsonObj.toString());
                            message = "Sucess";


                        } else {
                            message = "Failure";
                            int err = jsonObj.getInt("failure");

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data error", "Didn't receive any data from server!");
            }

            return message;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}


