package es.pabgarci.multisensortool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.hardware.SensorEventListener;
import android.widget.ImageView;
import android.widget.TextView;


public class Accelerometer extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float lastX, lastY, lastZ;
    private final float NOISE = 2;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

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

    ImageView imageViewArrow;

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
        imageViewArrow = (ImageView) findViewById(R.id.imageViewArrow);
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
            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = Math.abs(lastY - event.values[1]);
            deltaZ = Math.abs(lastZ - event.values[2]);

            if (deltaX < NOISE)
            deltaX = 0;
            if (deltaY < NOISE)
            deltaY = 0;
            if (deltaZ < NOISE)
                deltaZ = 0;

            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];

        }


        textViewX.setText(String.format("X-Axis = %s  m/s^2", Float.toString(deltaX)));
        textViewY.setText(String.format("Y-Axis = %s  m/s^2", Float.toString(deltaY)));
        textViewZ.setText(String.format("Z-Axis = %s  m/s^2", Float.toString(deltaZ)));

        if (deltaX > deltaY) {
            imageViewArrow.setImageResource(R.drawable.arrow_h);
        } else if (deltaY > deltaX) {
            imageViewArrow.setImageResource(R.drawable.arrow_v);
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
