package com.ilicit.ewerdima;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.parse.Parse;
//import com.parse.ParseException;
//import com.parse.ParseUser;
//import com.parse.SignUpCallback;

public class Register extends ActionBarActivity {
	EditText reg_fullname;
	EditText reg_username;
	EditText reg_email;
	EditText reg_password;
	Button reg_signup;
//	ParseUser user = new ParseUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
		reg_username = (EditText) findViewById(R.id.reg_username);
		reg_fullname = (EditText) findViewById(R.id.reg_fullname);
		reg_email = (EditText) findViewById(R.id.reg_email);
		reg_password = (EditText) findViewById(R.id.reg_password);
		reg_signup = (Button) findViewById(R.id.btnRegister);

		// Listening to SignUp Button
		reg_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				signUp(reg_fullname.getText().toString(), reg_username
						.getText().toString(), reg_email.getText().toString(),
						reg_password.getText().toString());
			}
		});
		// Listening to Login Screen link
		loginScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// Switching to Login Screen/closing register screen
				finish();
			}
		});
//		if(user.isAuthenticated()){
//			Toast.makeText(getApplicationContext(), "User already signed in", Toast.LENGTH_LONG).show();
//			startActivity(new Intent(getApplicationContext(), MainActivity.class));
//		}else{
//			initializeParse(); // initilize all parse components
//		}
//
	}

//	private void initializeParse() {
//		// TODO Auto-generated method stub
//		String ParseAppID = "kF0sMngGuvbiOLUrDBtXe2NRvDR0ry3T8ru68rya";
//		String ParseClientKey = "7YHdqwMXLknaQaJtfmciQkJCTkE2EfW2zi3lgnPw";
//		Toast.makeText(getApplicationContext(), "CLicked Parse",
//				Toast.LENGTH_LONG).show();
//		Parse.initialize(getApplicationContext(), ParseAppID, ParseClientKey);
//
//	}

	private void signUp(String fullname, String username, String email,
			String password) {
		final ProgressDialog progressDialog = ProgressDialog.show(
				this, "", "Logging in...", false);
//		user.setUsername(username);
//		user.setPassword(password);
//		user.setEmail(email);
//		// other fields can be set just like with ParseObject
//		user.put("Fullname", fullname);

//		user.signUpInBackground(new SignUpCallback() {
//			public void done(ParseException e) {
//				progressDialog.dismiss();
//				if (e == null) {
//					Toast.makeText(getApplicationContext(), "User registered",
//							Toast.LENGTH_LONG).show();
//					// Hooray! Let them use the app now.
//					ParseUser.getCurrentUser().saveInBackground();
//					startActivity(new Intent (getApplicationContext(), MainActivity.class));
//
//				} else {
//					// Sign up didn't succeed. Look at the ParseException
//					// to figure out what went wrong
//					Toast.makeText(getApplicationContext(), "Opps Error! " + e,
//							Toast.LENGTH_LONG).show();
//				}
//			}
//		});
	}
	
	// infavour of parse API
	/*
	 * protected void registerUser(final String fullname, final String email,
	 * final String password) { // TODO Auto-generated method stub
	 * 
	 * final UserFunctions userFunctions = new
	 * UserFunctions(getApplicationContext()); new Thread(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * JSONObject json = userFunctions.registerUser(fullname, email, password);
	 * try { if (json.get("status").equals("1")){ String unique_id =
	 * json.get("unique_id").toString(); Intent toMainActivity = new
	 * Intent(getApplicationContext(), MainActivity.class);
	 * userFunctions.saveUserDetails(fullname, unique_id);
	 * startActivity(toMainActivity); } } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } }).start();
	 * 
	 * }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
