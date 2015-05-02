package com.ilicit.ewerdima;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Alert extends ActionBarActivity implements OnItemSelectedListener {
	EditText description;
	Spinner alertType;
	String selectEmergency = null;
	Button sendAlertBTN;
	double longitude;
	double latitude;
	String provider;
	Location location;
	String cityName = null;
	LocationManager locationManager;

	ProgressDialog progressDialog;
	ParseObject alertObject;
	private static final String TAG = "AlertFragment";
	String[] emergencyTypes = { "Landslide", "Flood", "Disease", "Tsunami",
			"Earth Quake" };
	ParseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);

		description = (EditText) findViewById(R.id.alert_Description);
		alertType = (Spinner) findViewById(R.id.alert_Type);
		sendAlertBTN = (Button) findViewById(R.id.send_Alert);
		final Boolean status = getGPS();
		sendAlertBTN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(Alert.this, "",
						"Reporting...", true);
				if (status) {
					sendUserAlert(selectEmergency, description.getText()
							.toString(), longitude, latitude);

				} else {
					progressDialog.dismiss();
					showAlert("Error", "GPS required! Turn on GPS");

				}
			}
		});

		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, emergencyTypes);
		adapter_state
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		alertType.setAdapter(adapter_state);
		alertType.setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		alertType.setSelection(position);
		selectEmergency = (String) alertType.getSelectedItem();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private boolean getGPS() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 10, locationListener);

		/*
		 * locationManager = (LocationManager)
		 * getSystemService(Context.LOCATION_SERVICE); Criteria criteria = new
		 * Criteria(); criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		 * criteria.setPowerRequirement(Criteria.POWER_LOW);
		 * criteria.setAltitudeRequired(false);
		 * criteria.setBearingRequired(false); criteria.setSpeedRequired(false);
		 * provider = locationManager.getBestProvider(criteria, true);
		 * Log.i("GPS", "Initializing GPS"); android.location.Location location
		 * = locationManager .getLastKnownLocation(provider);
		 */
		if (location != null) {

			Log.i("GPS", "GPS Locked on!");

			return true;
		} else {

			Log.e("Fail", "GPS broken");
			return false;

		}
	}

	protected void sendUserAlert(final String alertType,
			final String alertDescription, final double longitude,
			final double latitude) {
		// TODO Auto-generated method stub
		String ParseAppID = "kF0sMngGuvbiOLUrDBtXe2NRvDR0ry3T8ru68rya";
		String ParseClientKey = "7YHdqwMXLknaQaJtfmciQkJCTkE2EfW2zi3lgnPw";
		// ParseObject.registerSubclass(Disaster_Alerts.class);
		Parse.initialize(this, ParseAppID, ParseClientKey);
		alertObject = new ParseObject("Disaster_Alerts");
		currentUser = ParseUser.getCurrentUser();
		alertObject.put("username", currentUser.getUsername());
		alertObject.put("alertType", alertType);
		alertObject.put("alertDescription", alertDescription);
		alertObject.put("longitude", longitude);
		alertObject.put("latitude", latitude);
		alertObject.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				progressDialog.dismiss();
				finish();
			}
		});
	}

	public void makeParseQuery() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Disaster_Alerts");
		query.getInBackground(alertObject.getObjectId(),
				new GetCallback<ParseObject>() {

					@Override
					public void done(ParseObject object, ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							Toast.makeText(getApplicationContext(), "Sent!",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Try again", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public String getCity() {
		/*------- To get city name from coordinates -------- */

		Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
			if (addresses.size() > 0)
				System.out.println(addresses.get(0).getLocality());
			cityName = addresses.get(0).getLocality();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
				+ cityName;
		Log.e(TAG, s);
		return cityName;
	}

	private void showAlert(final String status, final String jsonRESPONSE) {
		// TODO Auto-generated method stub

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Alert.this)
						.setTitle(status)
						.setMessage(jsonRESPONSE)
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alert, menu);
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

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			location = loc;
			Toast.makeText(
					getBaseContext(),
					"Location changed: Lat: " + loc.getLatitude() + " Lng: "
							+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
			longitude = loc.getLongitude();
			Log.v(TAG, "" + longitude);
			latitude = loc.getLatitude();
			Log.v(TAG, "" + latitude);
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
