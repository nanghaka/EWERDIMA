package com.ilicit.ewerdima;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ilicit.ewerdima.dialog.ProgressDialogButton;
import com.ilicit.ewerdima.helper.GPSService;
import com.ilicit.ewerdima.helper.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dev on 4/27/2015.
 */


public class ReportFormActivity extends Activity implements AdapterView.OnItemSelectedListener {

  //  private Button btnAddNewCategory;
  //  private TextView txtCategory;
    private Spinner spinnerFood;
    // array list for spinner adapter
    private ArrayList<Category> categoriesList;
    ProgressDialog pDialog;
     Button ReportButton;
    double latitude;
    double longitude;
    String address = "";
    // API urls

    // Url to create new category
    private String URL_NEW_CATEGORY = "http://planetweneed.org/morelo/mobile/new_category.php";

    // Url to get all categories
    private String URL_CATEGORIES = "http://planetweneed.org/morelo/mobile/get_categories.php";

    private String SEND_URL ="http://planetweneed.org/morelo/mobile/new_report.php";

    EditText txtDesc,txtDescOffender,txtPhone;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportform_layout);


        //This is for the GPS Location


        GPSService mGPSService = new GPSService(ReportFormActivity.this);
        mGPSService.getLocation();

        uid = getIntent().getExtras().getString("uid");

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
            Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();
        }

        Toast.makeText(getApplicationContext(), "Your address is: " + address, Toast.LENGTH_SHORT).show();

// make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();


//        btnAddNewCategory = (Button) findViewById(R.id.btnAddNewCategory);
        spinnerFood = (Spinner) findViewById(R.id.spinCrimes);
        txtDesc = (EditText) findViewById(R.id.alert_Description);
        txtDescOffender = (EditText) findViewById(R.id.alert_description_offender);
        txtPhone = (EditText) findViewById(R.id.alert_cell);

        categoriesList = new ArrayList<Category>();

        // spinner item select listener
        spinnerFood.setOnItemSelectedListener(this);

        ReportButton = (Button) findViewById(R.id.send_report);
        ReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendDetails().execute();

            }
        });

        // Add new category click event
//        btnAddNewCategory.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (txtCategory.getText().toString().trim().length() > 0) {
//
//                    // new category name
//                    String newCategory = txtCategory.getText().toString();
//
//                    // Call Async task to create new category
//                    new AddNewCategory().execute(newCategory);
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter category name", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        });

        new GetCategories().execute();

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        //txtCategory.setText("");

        for (int i = 0; i < categoriesList.size(); i++) {
            lables.add(categoriesList.get(i).getName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerFood.setAdapter(spinnerAdapter);
    }

    /**
     * Async task to get all food categories
     * */
    private class GetCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportFormActivity.this);
            pDialog.setMessage("Fetching Criminal categories...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_CATEGORIES, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("categories");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            Category cat = new Category(catObj.getInt("id"),
                                    catObj.getString("name"));
                            categoriesList.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();
        }

    }

    /**
     * Async task to create a new food category
     * */
//    private class AddNewCategory extends AsyncTask<String, Void, Void> {
//
//        boolean isNewCategoryCreated = false;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(ReportFormActivity.this);
//            pDialog.setMessage("Creating new category..");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(String... arg) {
//
//            String newCategory = arg[0];
//
//            // Preparing post params
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("name", newCategory));
//
//            ServiceHandler serviceClient = new ServiceHandler();
//
//            String json = serviceClient.makeServiceCall(URL_NEW_CATEGORY,
//                    ServiceHandler.POST, params);
//
//            Log.d("Create Response: ", "> " + json);
//
//            if (json != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(json);
//                    boolean error = jsonObj.getBoolean("error");
//                    // checking for error node in json
//                    if (!error) {
//                        // new category created successfully
//                        isNewCategoryCreated = true;
//                    } else {
//                        Log.e("Create Category Error: ", "> " + jsonObj.getString("message"));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("JSON Data", "Didn't receive any data from server!");
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            if (isNewCategoryCreated) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // fetching all categories
//                        new GetCategories().execute();
//                    }
//                });
//            }
//        }
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        Toast.makeText(
                getApplicationContext(),
                parent.getItemAtPosition(position).toString() + " Selected" ,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public class SendDetails extends AsyncTask<String, String, String>{

        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportFormActivity.this);
            pDialog.setMessage("Please wait Sending Report...");
            pDialog.setCancelable(false);
            pDialog.show();

        }




        @Override
        protected String doInBackground(String... params) {
            ServiceHandler jsonParser = new ServiceHandler();
            String message = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair(
                    "reporttype", spinnerFood.getSelectedItem().toString()));
            nameValuePairs.add(new BasicNameValuePair(
                    "reporterid", uid));
            nameValuePairs.add(new BasicNameValuePair(
                    "locationstreet", address));
            nameValuePairs.add(new BasicNameValuePair(
                    "locationlat", String.valueOf(latitude)));
            nameValuePairs.add(new BasicNameValuePair(
                    "locationlong", String.valueOf(longitude)));
            nameValuePairs.add(new BasicNameValuePair(
                    "descriptionoffender", txtDescOffender.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair(
                    "descriptionreport", txtDesc.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair(
                    "contactnumber", txtPhone.getText().toString()));



            String json = jsonParser.makeServiceCall(SEND_URL, ServiceHandler.POST,nameValuePairs);

            Log.e("Response sending: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj.length() != 0) {
                        if(jsonObj.getBoolean("error")){
                            Log.e("error",""+jsonObj.toString());
                            message = jsonObj.getString("message");
                            error =jsonObj.getBoolean("error");

                        }else{
                            message = jsonObj.getString("message");
                            error =jsonObj.getBoolean("error");
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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

               final ProgressDialogButton dialogButton = new ProgressDialogButton(ReportFormActivity.this, "Report", result);

                dialogButton.show();
                dialogButton.setOkClickedAction("OK",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogButton.dismiss();
                                Intent finalintent = new Intent(ReportFormActivity.this, MainActivity.class);
                                startActivity(finalintent);
                                finish();

                            }
                        });



        }
    }





}


