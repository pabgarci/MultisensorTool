package es.pabgarci.multisensortool;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.widget.TextView;

import es.pabgarci.multisensortool.plot.Plot;

public class MagneticField extends Plot implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senMagnetic;

    TextView textViewValue;
    TextView textViewValue2;
    TextView textViewPower;
    TextView textViewMaxRange;
    TextView textViewMinDelay;
    TextView textViewResolution;
    TextView textViewVendor;
    TextView textViewVersion;
    TextView textViewName;

    double magnetic_field;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senMagnetic = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(senMagnetic==null){
            textViewValue.setText(R.string.text_magnetic_unavailable);
        }else{
            senSensorManager.registerListener(this, senMagnetic , SensorManager.SENSOR_DELAY_NORMAL);
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetic_field);
        loadToolbar();
        textViewValue = (TextView)findViewById(R.id.textView_magnetic_value);
        textViewValue2 = (TextView)findViewById(R.id.textView_magnetic_value2);
        textViewPower = (TextView)findViewById(R.id.textView_magnetic_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_magnetic_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_magnetic_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_magnetic_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_magnetic_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_magnetic_version);
        textViewName = (TextView)findViewById(R.id.textView_magnetic_name);
        registerSensor();
        initPlot("magnetic");

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 10);
                plot("magnetic",(float)magnetic_field);
            }
        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetic_field = Math.sqrt(event.values[0]*event.values[0] +
                    event.values[1]*event.values[1] + event.values[2]*event.values[2]);
            textViewValue.setText(String.format("Magnetic field: %s", magnetic_field ));
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
        senSensorManager.registerListener(this, senMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
