package es.pabgarci.multisensortool;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;


public class GPS extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleApiClient mGoogleApiClient;
    LocationManager locationManager = null;

    TextView textViewAccuracy;
    TextView textViewLatitude;
    TextView textViewLongitude;
    TextView textViewAddress;
    TextView textViewBearing;
    TextView textViewProvider;
    TextView textViewAltitude;
    TextView textViewSpeed;
    TextView textViewSpeedMS;
    TextView textViewTime;
    TextView textViewSat;

    public void loadToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.app_surname);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        loadToolbar();
        textViewAccuracy = (TextView) findViewById(R.id.textView_gps_accuracy);
        textViewLatitude = (TextView) findViewById(R.id.textView_gps_latitude);
        textViewLongitude = (TextView) findViewById(R.id.textView_gps_longitude);
        textViewAddress = (TextView) findViewById(R.id.textView_gps_address);
        textViewBearing = (TextView) findViewById(R.id.textView_gps_bearing);
        textViewProvider = (TextView) findViewById(R.id.textView_gps_provider);
        textViewAltitude = (TextView) findViewById(R.id.textView_gps_altitude);
        textViewSpeed = (TextView) findViewById(R.id.textView_gps_speed);
        textViewSpeedMS = (TextView) findViewById(R.id.textView_gps_speed_ms);
        textViewTime = (TextView) findViewById(R.id.textView_gps_time);
        textViewSat = (TextView) findViewById(R.id.textView_gps_sat);

        textViewAddress.setText("Getting location...");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }



    public String getAddress(double myLat, double myLng){

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String textAddress = "";

        try {
            List<Address> myAddress = geoCoder.getFromLocation(myLat,myLng,1);

            if (myAddress.size() > 0) {
                for (int i = 0; i < myAddress.get(0).getMaxAddressLineIndex(); i++)
                    textAddress = myAddress.get(0).getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return textAddress;
    }


    public String getCityAndCountryCode(double myLat, double myLng){

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String textCity = "";

        try {
            List<Address> myAddress = geoCoder.getFromLocation(myLat,myLng,1);

            if (myAddress.size() > 0) {
                for (int i = 0; i < myAddress.get(0).getMaxAddressLineIndex(); i++)
                    textCity = myAddress.get(0).getLocality()+" ("+myAddress.get(0).getCountryCode()+")";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return textCity;
    }

    @Override
    public void onLocationChanged(Location location) {

        String toShow = getAddress(location.getLatitude(), location.getLongitude())
                + ", " + getCityAndCountryCode(location.getLatitude(), location.getLongitude());

        String accuracy = Float.toString(location.getAccuracy());
        textViewAccuracy.setText(String.format("Accuracy: %s m", accuracy));
        textViewAddress.setText(String.format("Address: %s", toShow));
        String latitude = Double.toString(location.getLatitude());
        textViewLatitude.setText(String.format("Latitude: %s", latitude));
        String longitude = Double.toString(location.getLongitude());
        textViewLongitude.setText(String.format("Longitude: %s", longitude));
        String bearing = Float.toString(location.getBearing());
        textViewBearing.setText(String.format("Bearing: %s", bearing));
        String altitude = Double.toString(location.getAltitude());
        textViewAltitude.setText(String.format("Altitude: %s m", altitude));
        textViewProvider.setText(String.format("Provider: %s", location.getProvider()));
        Float speed = location.getSpeed();
        String speedStringMS = Float.toString(speed);
        String speedStringKMH = Float.toString(speed*(float)3.6)+"kmh";
        String speedStringMPH = Float.toString(speed*(float)2.23694)+"mph";
        textViewSpeedMS.setText(String.format("Speed:\n%s m/s", speed));
        textViewSpeed.setText(String.format("%s\n%s", speedStringKMH, speedStringMPH));
        long time = location.getTime();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss, dd-MM-yyyy");
        String timeString = sdf.format(date);
        textViewTime.setText(String.format("Time UTC: "+timeString));

    }



    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("MyLocation", "Connection to Google Api has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult cResult) {
        Log.i("MyLocation", "Connection to Google Api has failed");
    }


}
