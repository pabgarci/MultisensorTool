package es.pabgarci.multisensortool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class LinearAcceleration extends Common implements SensorEventListener {


    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    TextView textViewName;
    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(senAccelerometer==null){
            textViewName.setText(R.string.text_accelerometer_unavailable);
        }else {
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_acceleration);
        loadToolbar();
        registerSensor();
        textViewX = (TextView)findViewById(R.id.textView_linear_x);
        textViewY = (TextView)findViewById(R.id.textView_linear_y);
        textViewZ = (TextView)findViewById(R.id.textView_linear_z);
        if(senAccelerometer==null){
            textViewName.setText(R.string.text_accelerometer_unavailable);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

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
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
