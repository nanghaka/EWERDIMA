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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilicit.ewerdima.Models.MyUsers;
import com.ilicit.ewerdima.Models.Results;
import com.ilicit.ewerdima.helper.SQLiteHandler;
import com.ilicit.ewerdima.helper.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shaffic on 5/12/15.
 */
public class FriendsActivity extends Activity {

    Button add;
    EditText email;
    private String URL ="http://ewerdima.cloudapp.net/index.php/api/rest/addContacts";
    private String URL_Circles ="http://ewerdima.cloudapp.net/index.php/api/rest/usercircles/";
    private SQLiteHandler db;
    String uid;
    ListView list;
    private ProgressDialog pDialog;
    ArrayList<MyUsers> friends = new ArrayList<MyUsers>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        email = (EditText) findViewById(R.id.editText);
        add = (Button) findViewById(R.id.button);
        list = (ListView) findViewById(R.id.listView);

        db = new SQLiteHandler(getApplicationContext());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isValidEmail(email.getText().toString())) {
                    if(Utils.isNetworkAvailable(FriendsActivity.this)) {
                        new SendFriends().execute(uid, email.getText().toString());

                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Please check on your internet connection.Thank you!", Toast.LENGTH_LONG)
                                .show();
                    }

                }else{
                    email.setError("Invalid Email");
                }
            }
        });

        HashMap<String, String> user = db.getUserDetails();
        uid = user.get("uid");

        if(Utils.isNetworkAvailable(FriendsActivity.this)) {
            new GetFriends().execute(uid);

        }else{
            Toast.makeText(getApplicationContext(),
                    "Please check on your internet connection.Thank you!", Toast.LENGTH_LONG)
                    .show();
        }




    }


    public class SendFriends extends AsyncTask<String, String, String> {

        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendsActivity.this);
            pDialog.setMessage("please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }




        @Override
        protected String doInBackground(String... params) {
            ServiceHandler jsonParser = new ServiceHandler(FriendsActivity.this);
            String message = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair(
                    "registereduserid1", params[0]));
            nameValuePairs.add(new BasicNameValuePair(
                    "alertedusers", params[1]));


            String json = jsonParser.makeServiceCall(URL, ServiceHandler.POST,nameValuePairs);

            Log.e("Response sending: ", "> " + json);

            if (json != null && json.length() >0) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj.length() != 0) {
                        if(jsonObj.getInt("success") > 0){
                            Log.e("error",""+jsonObj.toString());
                            message = "Success";


                        }else{
                            message = "Failure";
                            int err =jsonObj.getInt("failure");

                        }

                    }

                } catch (JSONException e) {
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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(Utils.isNetworkAvailable(FriendsActivity.this)) {
                new GetFriends().execute(uid);

            }else{
                Toast.makeText(getApplicationContext(),
                        "Please check on your internet connection.Thank you!", Toast.LENGTH_LONG)
                        .show();
            }



        }
    }





    public class GetFriends extends AsyncTask<String, String, String> {

        boolean error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendsActivity.this);
            pDialog.setMessage("please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }




        @Override
        protected String doInBackground(String... params) {
            ServiceHandler jsonParser = new ServiceHandler(FriendsActivity.this);
            String message = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair(
                    "registereduserid1", params[0]));

            String json = jsonParser.makeServiceCall(URL_Circles+params[0], ServiceHandler.GET,nameValuePairs);

            Log.e("Response sending: ", "> " + json);

            if (json != null && json.length() >0) {
                try {
                    Gson gson = new Gson();
                    ArrayList<MyUsers> jsonObj = new Gson().fromJson(json, new TypeToken<List<MyUsers>>(){}.getType());

                    if (jsonObj.size() != 0) {


                        message = "Success";
                        friends.clear();

                        friends.addAll(jsonObj);

                    }

                        else{
                            message = "Failure";
                          //  int err =jsonObj.getInt("failure");

                        }



                } catch (Exception e) {
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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

           list.setAdapter(new FriendsListAdapter(FriendsActivity.this,friends));


        }
    }




}
