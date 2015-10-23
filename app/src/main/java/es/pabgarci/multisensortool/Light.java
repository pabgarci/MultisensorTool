package es.pabgarci.multisensortool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Light extends AppCompatActivity  implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senLight;

    TextView textViewValue;
    TextView textViewPower;
    TextView textViewMaxRange;
    TextView textViewMinDelay;
    TextView textViewResolution;
    TextView textViewVendor;
    TextView textViewVersion;
    TextView textViewName;


    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senLight = senSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(senLight==null){
            textViewValue.setText("Light sensor unavailable");
        }else{
            senSensorManager.registerListener(this, senLight , SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    public void loadToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        loadToolbar();
        registerSensor();
        textViewValue = (TextView)findViewById(R.id.textView_light_value);
        textViewPower = (TextView)findViewById(R.id.textView_light_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_light_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_light_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_light_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_light_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_light_version);
        textViewName = (TextView)findViewById(R.id.textView_light_name);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_LIGHT) {
            textViewValue.setText(String.format("Value: %s lux", Float.toString(event.values[0])));
            textViewPower.setText(String.format("Power: %s", mySensor.getPower()));
            textViewMaxRange.setText(String.format("Max range: %s", mySensor.getMaximumRange()));
            textViewMinDelay.setText(String.format("Min delay: %s", mySensor.getMinDelay()));
            textViewResolution.setText(String.format("Resolution: %s", mySensor.getResolution()));
            textViewVendor.setText(String.format("Vendor: %s", mySensor.getVendor()));
            textViewVersion.setText(String.format("Version: %s", mySensor.getVersion()));
            textViewName.setText(String.format("Model: %s", mySensor.getName()));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onStop() {
        super.onStop();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
