package com.ilicit.ewerdima.Models;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.ilicit.ewerdima.LoginActivity;
import com.ilicit.ewerdima.Utils;
import com.ilicit.ewerdima.app.AppConfig;
import com.ilicit.ewerdima.dialog.ProgressDialogButton;
import com.ilicit.ewerdima.helper.SQLiteHandler;
import com.ilicit.ewerdima.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaffic on 6/4/15.
 */
public class RegisterUser extends AsyncTask<String, String, String> {

    Context context;
    boolean error;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

   public RegisterUser(Context context){
       this.context = context;
       session = new SessionManager(context);

       // SQLite database handler
       db = new SQLiteHandler(context);

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("please wait....");
        pDialog.setCancelable(false);
        pDialog.show();

    }




    @Override
    protected String doInBackground(String... params) {
        com.ilicit.ewerdima.helper.ServiceHandler jsonParser = new com.ilicit.ewerdima.helper.ServiceHandler((Activity)context);
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

                    Utils.save("Token", "token", (Activity)context);
                    String uid = jObj.getString("unique_id");


                    String name = jObj.getString("name");
                    String email = jObj.getString("email");
                    String created_at = jObj.getString("created_at");

                    // Inserting row in users table


                    db.addUser(name, email, uid, created_at);



                    // Launch main activity
                    Intent intent = new Intent(context,
                            LoginActivity.class);
                    context.startActivity(intent);
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
        pDialog.dismiss();

        if(result.equalsIgnoreCase("")){

        }else {

            final ProgressDialogButton dialogButton = new ProgressDialogButton(context, "Error", result);
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
