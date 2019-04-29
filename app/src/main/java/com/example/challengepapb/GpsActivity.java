package com.example.challengepapb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtLatitude, txtLongitude, txtGeocode, txtAddress;
    private Button btnGet, btnCuaca;
    private Geocoder geocoder;
    private GpsTracker gpsTracker;
    private List<Address> addresses;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        txtLatitude = findViewById(R.id.edit_latitude);
        txtLongitude = findViewById(R.id.edit_longitude);
        txtGeocode = findViewById(R.id.edit_geocode);
        txtAddress = findViewById(R.id.edit_address);
        btnGet = findViewById(R.id.btn_get);
        btnCuaca = findViewById(R.id.btn_cuaca);

        btnGet.setOnClickListener(this);
        btnCuaca.setOnClickListener(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                gpsTracker = new GpsTracker(GpsActivity.this);
                if (!isConnected()){
                    gpsTracker.showSettingsAlert("connection");
                }else{
                    if(gpsTracker.canGetLocation()==true){
                        latitude = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();
                        txtLatitude.setText(String.valueOf(latitude));
                        txtLongitude.setText(String.valueOf(longitude));
                        txtGeocode.setText(String.valueOf(latitude+", "+longitude));

                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
//                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                        txtAddress.setText(address);
                    }else if (gpsTracker.canGetLocation()==false){
                        gpsTracker.showSettingsAlert("gps");
                    }
                }


                break;

            case R.id.btn_cuaca:
                Intent intent = new Intent(GpsActivity.this, WeatherActivity.class);
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("longitude", String.valueOf(longitude));
                startActivity(intent);
                break;

        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;

    }

}
