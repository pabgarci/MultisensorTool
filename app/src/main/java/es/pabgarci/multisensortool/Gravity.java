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
import java.lang.Math;

public class Gravity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senGyroscope;
    static final double EPSILON = 0.10;

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
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);

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
        setContentView(R.layout.activity_gravity);
        loadToolbar();
        registerSensor();
        textViewName = (TextView)findViewById(R.id.textView_gyroscope_name);
        textViewPower = (TextView)findViewById(R.id.textView_gyroscope_power);
        textViewMaxRange = (TextView)findViewById(R.id.textView_gyroscope_max_range);
        textViewMinDelay = (TextView)findViewById(R.id.textView_gyroscope_min_delay);
        textViewResolution = (TextView)findViewById(R.id.textView_gyroscope_resolution);
        textViewVendor = (TextView)findViewById(R.id.textView_gyroscope_vendor);
        textViewVersion = (TextView)findViewById(R.id.textView_gyroscope_version);
        textViewX = (TextView)findViewById(R.id.textView_gyroscope_x);
        textViewY = (TextView)findViewById(R.id.textView_gyroscope_y);
        textViewZ = (TextView)findViewById(R.id.textView_gyroscope_z);
        if(senGyroscope==null){
            textViewName.setText("Gyroscope unavailable");
        }
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

            textViewX.setText(String.format("X-Axis = %s  m/s^2", Float.toString(gravity[0])));
            textViewY.setText(String.format("Y-Axis = %s  m/s^2", Float.toString(gravity[1])));
            textViewZ.setText(String.format("Z-Axis = %s  m/s^2", Float.toString(gravity[2])));
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
        senSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
