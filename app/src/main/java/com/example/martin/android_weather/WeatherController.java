package com.example.martin.android_weather;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class WeatherController extends AppCompatActivity {
    // Request Code for permission request callback
    final int REQUEST_CODE = 123;

    // Request code for starting new activity for result callback
    final int NEW_CITY_CODE = 456;

    // Base URL for the OpenWeatherMap API, using HTTPS instead of HTTP is a premium
    // feature and will require payment
    final String WEATHER_URL = "";

    // App ID to use OpenWeather data
    final String APP_ID = "";

    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;

    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // Avoids typing 'Clima' in all the logs by putting this in a constant here.
    final String LOGCAT_TAG = "Clima";

    // Setting the LOCATION_PROVIDER here, using this with GPS_Provider is good when testing
    // on an emulator, when using a physical device use LocationManager.NETWORK_PROVIDER
    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    // Member Variables:
    boolean mUseLocation = true;
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;

    // Declaring a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        mCityLabel = findViewById(R.id.locationTV);
        mWeatherImage = findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = findViewById(R.id.tempTV);
        ImageButton changeCityButton = findViewById(R.id.changeCityButton);

        // This adds an OnClickListener to the changeCityButton
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(WeatherController.this, ChangeCityController.class);

                // Using startActivityForResult will just return the city name, The NEW_CITY_CODE
                // constant added will provide an arbitrary request code to check against later
                startActivityForResult(myIntent, NEW_CITY_CODE);
            }
        });
    }

    // onResume() lifecycle callback:
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOGCAT_TAG, "onResume() called");
        if(mUseLocation) getWeatherForCurrentLocation();
    }

    // Location Listener callbacks here, when the location has changed.
    private void getWeatherForCurrentLocation(){
        Log.d(LOGCAT_TAG, "Getting weather for current location");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mLocationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                Log.d(LOGCAT_TAG, "onLocationChanged() callback received");
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                Log.d(LOGCAT_TAG, "longitude is: " + longitude);
                Log.d(LOGCAT_TAG, "latitude is: " + latitude);

                // Using the abbreviated lat and lon and getting parameter values
                // ensure that it is lon and not long that is used
                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Using log statements to help with debugging
                Log.d(LOGCAT_TAG, "onStatusChanged() callback received. Status: " + status);
                Log.d(LOGCAT_TAG, "2 means AVAILABLE, 1: TEMPORARILY_UNAVAILABLE, 0: OUT_OF_SERVICE");
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    // This is the actual networking code. Parameters are already configured.
    private void letsDoSomeNetworking(RequestParams params){
        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // You can make a HTTP GET request by providing a URL and necessary parameters
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler(){

        });
    }

    // Freeing up resources when the app enters the paused state.
    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager != null) mLocationManager.removeUpdates(mLocationListener);
    }
}
