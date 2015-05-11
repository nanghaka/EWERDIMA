package com.ilicit.ewerdima;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.morens.morelo.helper.SQLiteHandler;
import com.morens.morelo.helper.SessionManager;

import java.util.HashMap;


public class MainActivity extends Activity {

    private Button ReportCrime;

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        String address = "";
//        GPSService mGPSService = new GPSService(MainActivity.this);
//        mGPSService.getLocation();
//
//        if (mGPSService.isLocationAvailable == false) {
//
//            // Here you can ask the user to try again, using return; for that
//            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
//            return;
//
//            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
//            // address = "Location not available";
//        } else {
//
//            // Getting location co-ordinates
//            double latitude = mGPSService.getLatitude();
//            double longitude = mGPSService.getLongitude();
//            Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();
//
//            address = mGPSService.getLocationAddress();
//        }
//
//        Toast.makeText(getApplicationContext(), "Your address is: " + address, Toast.LENGTH_SHORT).show();
//
//// make sure you close the gps after using it. Save user's battery power
//        mGPSService.closeGPS();

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        ReportCrime = (Button) findViewById(R.id.Reportbutton);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        //Report Crime Button
        ReportCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reportIntent = new Intent(MainActivity.this, ReportFormActivity.class);
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
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

