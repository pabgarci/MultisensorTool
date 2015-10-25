package es.pabgarci.multisensortool;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Temperature extends Common implements SensorEventListener{

    private SensorManager senSensorManager;
    private Sensor senTemperature;

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
        senTemperature = senSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(senTemperature==null){
            textViewValue.setText("Temperature sensor unavailable");
        }else{
            senSensorManager.registerListener(this, senTemperature , SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        loadToolbar();
        textViewValue = (TextView)findViewById(R.id.textView_temperature_value);
        textViewPower = (TextView)findViewById(R.id.textView_temperature_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_temperature_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_temperature_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_temperature_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_temperature_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_temperature_version);
        textViewName = (TextView)findViewById(R.id.textView_temperature_name);
        registerSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            textViewValue.setText(String.format("Value: %s ºC", Float.toString(event.values[0])));
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

    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onStop() {
        super.onStop();
        senSensorManager.unregisterListener(this);
    }

    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
