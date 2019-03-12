package com.example.abhi.tick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    TextView first_name, last_name, current_location;
    Button btn_location, btn_checkin, button_logout, btn_view;
    EditText answer;

    double latitude, longitude;
    Location location;

    String area, id;

    Geocoder geocoder;
    List<Address> addresses = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        button_logout = (Button) findViewById(R.id.btn_signout);

        first_name = (TextView) findViewById(R.id.user_first_name);
        last_name = (TextView) findViewById(R.id.user_last_name);
        answer = (EditText) findViewById(R.id.answer_line);


        current_location = (TextView) findViewById(R.id.txt_location);
        btn_location = (Button) findViewById(R.id.btn_location);
        btn_checkin = (Button) findViewById(R.id.btn_check_in);
        btn_view = (Button) findViewById(R.id.detail_employee);

        sharedpreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        id = sharedpreferences.getString("Id", "");
        Log.d("Id of User : ", id);
        first_name.setText(sharedpreferences.getString("FirstName", ""));
        last_name.setText(sharedpreferences.getString("LastName", ""));

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });

        btn_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("location", area);
                intent.putExtra("work", answer.getText().toString());
                startActivity(intent);
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeView.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

    public void buttonClicked() {
        location = getLocation();
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //textView.setText("Latitude : " + latitude + "\nLongitude : " + longitude);

            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                area = addresses.get(0).getSubLocality();
                //String address = addresses.get(0).getAddressLine(0);
                //String city = addresses.get(0).getAdminArea();
                //textView.setText("\n\nLatitude : " + latitude + "\nLongitude : " + longitude + "\nArea : " + area);
                current_location.setText(area);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Location getLocation() {

        Location location = null;
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
        final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                LocationManager locationManager = (LocationManager) getApplicationContext()
                        .getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        //updating when location is changed
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("LatLong", latitude + " : " + longitude);
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

                boolean isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isNetworkEnabled && !isGPSEnabled) {
                    //call getLocation again in onResume method in this case
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                } else {

                    if (isNetworkEnabled) {

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }

                    } else {

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Request for the ACCESS FINE LOCATION
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //call the getLocation funtion again on permission granted callback
        }
        return location;
    }
}
