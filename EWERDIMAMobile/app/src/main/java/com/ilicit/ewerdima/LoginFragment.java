package com.ilicit.ewerdima;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

//import com.parse.LogInCallback;
//import com.parse.Parse;
//import com.parse.ParseException;
//import com.parse.ParseFacebookUtils;
//import com.parse.ParseUser;

public class LoginFragment extends Fragment {
	private static final String TAG = "LoginFragment";
	private ProgressDialog progressDialog;
	protected static final int HOME = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
//		String ParseAppID = "kF0sMngGuvbiOLUrDBtXe2NRvDR0ry3T8ru68rya";
//		String ParseClientKey = "7YHdqwMXLknaQaJtfmciQkJCTkE2EfW2zi3lgnPw";
//		Parse.initialize(getActivity(), ParseAppID, ParseClientKey);
		ImageButton loginBTN = (ImageButton) view
				.findViewById(R.id.normal_loginBTN);
		Button fbLogin = (Button) view.findViewById(R.id.fb_button);
		
		fbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onFbButtonClicked();
			}
		});
		Button signUP = (Button) view.findViewById(R.id.signup_button);
		final EditText username = (EditText) view.findViewById(R.id.username);
		final EditText password = (EditText) view.findViewById(R.id.password);
		// aq.id(R.id.normal_loginBTN).clicked(this,
		// "new AsyncCaller().execute(email.getText().toString(), password.getText().toString())");

		loginBTN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				normalLogin(username.getText().toString(), password.getText()
						.toString());
			}
		});

		signUP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toSignUP = new Intent(getActivity(), Register.class);
				startActivity(toSignUP);
			}
		});

		return view;
	}

	private void onFbButtonClicked() {
		progressDialog = ProgressDialog.show(getActivity(), "",
				"Logging in...", true);
		
//		ParseFacebookUtils.initialize(getString(R.string.app_id));
//		ParseFacebookUtils.logIn(Arrays.asList("email"), getActivity(),
//				new LogInCallback() {
//					@Override
//					public void done(ParseUser user, ParseException err) {
//						progressDialog.dismiss();
//						if (user == null) {
//							Log.d("FBlogin",
//									"Uh oh. The user cancelled the Facebook login.");
//						} else if (user.isNew()) {
//							Log.d("FBlogin",
//									"User signed up and logged in through Facebook!");
//							Toast.makeText(getActivity(), "New User!", 1000)
//									.show();
//
//							showUserDetailsActivity();
//						} else {
//							Log.d("FBlogin", "User logged in through Facebook!");
//
//							showUserDetailsActivity();
//						}
//					}
//				});
	}

	protected void normalLogin(final String uname, final String psword) {
		// TODO Auto-generated method stub
		
		final ProgressDialog progressDialog = ProgressDialog.show(
				getActivity(), "", "Logging in...", true);

//		ParseUser.logInInBackground(uname, psword, new LogInCallback() {
//
//			@Override
//			public void done(ParseUser user, ParseException err) {
//				progressDialog.dismiss();
//				if (user == null) {
//					Toast.makeText(getActivity(), "Opps! Wrong combination",
//							Toast.LENGTH_LONG).show();
//					Log.d("Parse Nomal Login",
//							"Uh oh. The user cancelled the Facebook login.");
//				} else if (user.isNew()) {
//					Log.d("Parse Nomal Login", "User signed up and logged in!");
//					Toast.makeText(getActivity(), "New User!", 1000).show();
//
//					showUserDetailsActivity();
//				} else {
//					Log.d("Parse Nomal Login", "User logged in!");
//					Toast.makeText(getActivity(), "Welcome back!", 1000).show();
//					showUserDetailsActivity();
//				}
//			}
//		});

	}

	private void showUserDetailsActivity() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		Fragment frag = new Fragment();
		frag = fm.findFragmentById(R.id.homeFragment);
		if (frag.isAdded()) {
			transaction.show(frag);
			transaction.commit();
		}
	}
}