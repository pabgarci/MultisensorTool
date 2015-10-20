package es.pabgarci.multisensortool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.hardware.SensorEventListener;
import android.widget.TextView;


public class Accelerometer extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    TextView textViewName;
    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;
    TextView textViewPower;
    TextView textViewMaxRange;
    TextView textViewMinDelay;
    TextView textViewResolution;
    TextView textViewVendor;
    TextView textViewVersion;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

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
        setContentView(R.layout.activity_accelerometer);
        loadToolbar();
        registerSensor();
        textViewName = (TextView)findViewById(R.id.textView_accelerometer_name);
        textViewPower = (TextView)findViewById(R.id.textView_accelerometer_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_accelerometer_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_accelerometer_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_accelerometer_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_accelerometer_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_accelerometer_version);
        textViewX = (TextView)findViewById(R.id.textView_accelerometer_x);
        textViewY = (TextView)findViewById(R.id.textView_accelerometer_y);
        textViewZ = (TextView)findViewById(R.id.textView_accelerometer_z);
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

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = (float)0.8;
            float gravity[] = new float[3];
            float linear_acceleration[] = new float[3];

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            textViewX.setText(String.format("X-Axis = %s  m/s^2", Float.toString(linear_acceleration[0])));
            textViewY.setText(String.format("Y-Axis = %s  m/s^2", Float.toString(linear_acceleration[1])));
            textViewZ.setText(String.format("Z-Axis = %s  m/s^2", Float.toString(linear_acceleration[2])));
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
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
