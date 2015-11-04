package es.pabgarci.multisensortool;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import es.pabgarci.multisensortool.plot.Plot;

public class Proximity extends Plot implements SensorEventListener {

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

    float proximity;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senProximity = senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(senProximity==null){
            textViewValue.setText(R.string.text_proximity_unavailable);
        }else{
            senSensorManager.registerListener(this, senProximity , SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        initPlot("proximity");

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 10);
                plot("proximity",proximity);
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximity=event.values[0];
            textViewPower.setText(String.format("Power: %s", mySensor.getPower()));
            textViewMaxRange.setText(String.format("Max range: %s", mySensor.getMaximumRange()));
            textViewMinDelay.setText(String.format("Min delay: %s", mySensor.getMinDelay()));
            textViewResolution.setText(String.format("Resolution: %s", mySensor.getResolution()));
            textViewVendor.setText(String.format("Vendor: %s", mySensor.getVendor()));
            textViewVersion.setText(String.format("Version: %s", mySensor.getVersion()));
            textViewName.setText(String.format("Model: %s", mySensor.getName()));
            
            if(mySensor.getMaximumRange()==proximity) {
                proximity=1;
                textViewValue.setText(R.string.text_proximity_far);
            }else if(proximity<mySensor.getMaximumRange()){
                proximity=0;
                textViewValue.setText(R.string.text_proximity_near);
            }else{
                proximity=event.values[0];
                textViewValue.setText(String.format("Distance: %s cm", Float.toString(proximity)));
            }
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
        senSensorManager.registerListener(this, senProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
