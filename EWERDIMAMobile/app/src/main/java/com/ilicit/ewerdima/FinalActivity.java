package com.ilicit.ewerdima;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Dev on 4/27/2015.
 */
public class FinalActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_layout);


        String address = "";
        GPSService mGPSService = new GPSService(FinalActivity.this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();
        }

        Toast.makeText(getApplicationContext(), "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
    }
}
