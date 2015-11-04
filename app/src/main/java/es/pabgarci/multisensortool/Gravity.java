package es.pabgarci.multisensortool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import es.pabgarci.multisensortool.plot.Plot;

public class Gravity extends Plot implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senGyroscope;

    double gravity_abs;

    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;
    TextView textViewAbs;
    TextView textViewPower;
    TextView textViewMaxRange;
    TextView textViewMinDelay;
    TextView textViewResolution;
    TextView textViewVendor;
    TextView textViewVersion;
    TextView textViewName;
    TextView textViewHz;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(senGyroscope==null){
            textViewName.setText(R.string.text_accelerometer_unavailable);
        }else {
            senSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        loadToolbar();
        registerSensor();
        textViewX = (TextView)findViewById(R.id.textView_gravity_x);
        textViewY = (TextView)findViewById(R.id.textView_gravity_y);
        textViewZ = (TextView)findViewById(R.id.textView_gravity_z);
        textViewAbs = (TextView)findViewById(R.id.textView_gravity_abs);
        textViewPower = (TextView)findViewById(R.id.textView_gravity_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_gravity_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_gravity_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_gravity_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_gravity_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_gravity_version);
        textViewName = (TextView)findViewById(R.id.textView_gravity_name);
        textViewHz = (TextView)findViewById(R.id.textView_gravity_hz);

        initPlot("gravity");

        if(senGyroscope==null){
            textViewName.setText(R.string.text_accelerometer_unavailable);
        }

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 10);
                plot("gravity", (float)gravity_abs);
            }
        };

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        textViewPower.setText(String.format("Power: %s", mySensor.getPower()));
        textViewMaxRange.setText(String.format("Max range: %s", mySensor.getMaximumRange()));
        textViewMinDelay.setText(String.format("Min delay: %s", mySensor.getMinDelay()));
        textViewResolution.setText(String.format("Resolution: %s", mySensor.getResolution()));
        textViewVendor.setText(String.format("Vendor: %s", mySensor.getVendor()));
        textViewVersion.setText(String.format("Version: %s", mySensor.getVersion()));
        textViewName.setText(String.format("Model: %s", mySensor.getName()));

        calculateSensorFrequency();

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
           System.arraycopy(event.values, 0, acceleration, 0, event.values.length);

            gravity_abs=Math.sqrt(acceleration[0]*acceleration[0]+acceleration[1]*acceleration[1]+acceleration[2]*acceleration[2]);

            textViewX.setText(String.format("X-Axis: %.4f  m/s^2", acceleration[0]));
            textViewY.setText(String.format("Y-Axis: %.4f  m/s^2", acceleration[1]));
            textViewZ.setText(String.format("Z-Axis: %.4f  m/s^2", acceleration[2]));
            textViewAbs.setText(String.format("Absolute: %.2f  m/s^2", gravity_abs));
            textViewHz.setText(String.format("Frequency: %.2f  Hz", hz));
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
        senSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
