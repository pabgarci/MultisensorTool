package es.pabgarci.multisensortool;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Proximity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senProximity;

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
        senProximity = senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(senProximity==null){
            textViewValue.setText("Proximity sensor unavailable");
        }else{
            senSensorManager.registerListener(this, senProximity , SensorManager.SENSOR_DELAY_NORMAL);
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
        setContentView(R.layout.activity_proximity);
        loadToolbar();
        textViewValue = (TextView)findViewById(R.id.textView_proximity_value);
        textViewPower = (TextView)findViewById(R.id.textView_proximity_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_proximity_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_proximity_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_proximity_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_proximity_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_proximity_version);
        textViewName = (TextView)findViewById(R.id.textView_proximity_name);
        registerSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
            float value=event.values[0];

            textViewPower.setText(String.format("Power: %s", mySensor.getPower()));
            textViewMaxRange.setText(String.format("Max range: %s", mySensor.getMaximumRange()));
            textViewMinDelay.setText(String.format("Min delay: %s", mySensor.getMinDelay()));
            textViewResolution.setText(String.format("Resolution: %s", mySensor.getResolution()));
            textViewVendor.setText(String.format("Vendor: %s", mySensor.getVendor()));
            textViewVersion.setText(String.format("Version: %s", mySensor.getVersion()));
            textViewName.setText(String.format("Model: %s", mySensor.getName()));
            
            if(mySensor.getMaximumRange()==value) {
                textViewValue.setText("'Far' state");
            }else if(value<mySensor.getMaximumRange()){
                textViewValue.setText("'Near' state");
            }else{
                textViewValue.setText(String.format("Value: %s cm", Float.toString(value)));
            }
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
        senSensorManager.registerListener(this, senProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
