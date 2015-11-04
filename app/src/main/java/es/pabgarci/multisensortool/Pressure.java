package es.pabgarci.multisensortool;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import es.pabgarci.multisensortool.plot.Plot;

public class Pressure extends Plot implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senPressure;

    TextView textViewValue;
    TextView textViewPower;
    TextView textViewMaxRange;
    TextView textViewMinDelay;
    TextView textViewResolution;
    TextView textViewVendor;
    TextView textViewVersion;
    TextView textViewName;

    float pressure;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senPressure = senSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(senPressure==null){
            textViewValue.setText(R.string.text_pressure_unavailable);
        }else{
            senSensorManager.registerListener(this, senPressure , SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure);
        loadToolbar();
        textViewValue = (TextView)findViewById(R.id.textView_pressure_value);
        textViewValue = (TextView)findViewById(R.id.textView_pressure_value);
        textViewPower = (TextView)findViewById(R.id.textView_pressure_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_pressure_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_pressure_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_pressure_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_pressure_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_pressure_version);
        textViewName = (TextView)findViewById(R.id.textView_pressure_name);
        registerSensor();

        initPlot("pressure");

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 10);
                plot("pressure",pressure);
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_PRESSURE) {
            pressure = event.values[0];
            textViewValue.setText(String.format("Value: %s hPa", Float.toString(pressure)));
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
        senSensorManager.registerListener(this, senPressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
