package com.ilicit.ewerdima;

import android.os.Bundle;   
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ewerdimamobile.utils.UserFunctions;
import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity {

	private static final int LOGIN = 0;
	private static final int HOME = 1;
	private static final int SETTINGS = 2;
	private static final int FRAGMENT_COUNT = SETTINGS + 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	ParseUser currentUser;
	private MenuItem settings;
	public UserFunctions userFunctions;
	TextView name, gender, location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
		fragments[HOME] = fm.findFragmentById(R.id.homeFragment);
		fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
		String ParseAppID = "kF0sMngGuvbiOLUrDBtXe2NRvDR0ry3T8ru68rya";
		String ParseClientKey = "7YHdqwMXLknaQaJtfmciQkJCTkE2EfW2zi3lgnPw";
		Parse.initialize(this, ParseAppID, ParseClientKey);
		currentUser = ParseUser.getCurrentUser();
	}

	public void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	/*
	 * private void onSessionStateChange(Session session, SessionState state,
	 * Exception exception) { // Only make changes if the activity is visible if
	 * (isResumed) { FragmentManager manager = getSupportFragmentManager(); //
	 * Get the number of entries in the back stack int backStackSize =
	 * manager.getBackStackEntryCount(); // Clear the back stack for (int i = 0;
	 * i < backStackSize; i++) { manager.popBackStack(); } } if
	 * (state.isOpened() || userFunctions.isUserLoggedIn()) { // || newUser //
	 * make request to the /me API to get Graph user
	 * 
	 * Session.openActiveSession(this, true, Arrays.asList("public_profile",
	 * "email"), new Session.StatusCallback() { // callback when session changes
	 * state
	 * 
	 * @Override public void call(final Session session, SessionState state,
	 * Exception exception) { if (session.isOpened()) { // make request to the
	 * /me API
	 * 
	 * Request.newMeRequest(session, new Request.GraphUserCallback() { //
	 * callback after Graph API response // with user object
	 * 
	 * @Override public void onCompleted( GraphUser user, Response response) {
	 * if (user != null) {
	 * 
	 * // Set User name Toast.makeText( getApplicationContext(), user.getName(),
	 * Toast.LENGTH_LONG); name.setText(user.getName()); //
	 * Toast.makeText(getApplicationContext(), //
	 * user.getProperty("gender").toString(), // Toast.LENGTH_LONG);
	 * location.setText(user .getLocation() .getProperty("name") .toString());
	 * // .toString(), // Toast.LENGTH_LONG); showFragment(HOME, false); try {
	 * gender.setText(user .getInnerJSONObject() .getString( "email")); } catch
	 * (JSONException e) { // TODO Auto-generated // catch block
	 * e.printStackTrace(); } } } }).executeAsync(); } } });
	 * 
	 * // If the session state is open: // Show the authenticated fragment
	 * 
	 * } else if (state.isClosed()) { // If the session state is closed: // Show
	 * the login fragment showFragment(LOGIN, false); } }
	 */

	@Override
	protected void onStart() {
		super.onStart();
		
		if (currentUser != null) {
			Toast.makeText(getApplicationContext(),
					"Welcome back " + currentUser.getUsername(),
					Toast.LENGTH_LONG).show();
			showFragment(HOME, true);
		} else {
			showFragment(LOGIN, true);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// only add the menu when the selection fragment is showing
		if (fragments[HOME].isVisible()) {
			if (menu.size() == 0) {
				settings = menu.add(R.string.action_settings);
			}
			return true;
		} else {
			menu.clear();
			settings = null;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			showFragment(SETTINGS, true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); if (currentUser.isAuthenticated()) { showFragment(HOME,
	 * true); } isResumed = true;
	 * 
	 * }
	 */

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();

		if (currentUser != null) { // || newUser

			showFragment(HOME, false);
			// showFragment(LOGIN, false);
		} else { // otherwise present the splash screen

			showFragment(LOGIN, false);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

	}
}
