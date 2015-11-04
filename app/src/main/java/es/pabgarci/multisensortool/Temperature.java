package es.pabgarci.multisensortool;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import es.pabgarci.multisensortool.plot.Plot;

public class Temperature extends Plot implements SensorEventListener{

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

    float temperature;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senTemperature = senSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(senTemperature==null){
            textViewValue.setText(R.string.text_temperature_unavailable);
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

        initPlot("temperature");

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 10);
                plot("temperature",temperature);
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperature = event.values[0];
            textViewValue.setText(String.format("Value: %s ºC", Float.toString(temperature)));
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
