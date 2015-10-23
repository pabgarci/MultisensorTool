package es.pabgarci.multisensortool;

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

public class MagneticField extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senMagnetic;
    private Sensor senAccelerometer;

    TextView textViewValue;
    TextView textViewValue2;
    TextView textViewPower;
    TextView textViewMaxRange;
    TextView textViewMinDelay;
    TextView textViewResolution;
    TextView textViewVendor;
    TextView textViewVersion;
    TextView textViewName;


    private float gravity[] = new float[3];
    private float magnetic[] = new float[3];

    private final float alpha = (float) 0.8;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senMagnetic = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(senMagnetic==null){
            textViewValue.setText("Magnetic sensor unavailable");
        }else{
            senSensorManager.registerListener(this, senMagnetic , SensorManager.SENSOR_DELAY_NORMAL);
            senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
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
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            textViewValue.setText(String.format("Magnetic field: %s", Math.sqrt(event.values[0]*event.values[0] +
                    event.values[1]*event.values[1] + event.values[2]*event.values[2])));
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
        senSensorManager.registerListener(this, senMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
