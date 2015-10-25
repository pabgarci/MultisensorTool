package es.pabgarci.multisensortool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Gravity extends Common implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senGyroscope;

    TextView textViewName;
    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;
    TextView textViewAbs;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(senGyroscope==null){
            textViewName.setText("Gyroscope unavailable");
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

        if(senGyroscope==null){
            textViewName.setText("Accelerometer unavailable");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        double gravity_abs;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, acceleration, 0,
                    event.values.length);

            gravity_abs=Math.sqrt(acceleration[0]*acceleration[0]+acceleration[1]*acceleration[1]+acceleration[2]*acceleration[2]);

            textViewX.setText(String.format("X-Axis: %.4f  m/s^2", acceleration[0]));
            textViewY.setText(String.format("Y-Axis: %.4f  m/s^2", acceleration[1]));
            textViewZ.setText(String.format("Z-Axis: %.4f  m/s^2", acceleration[2]));
            textViewAbs.setText(String.format("Absolute: %.2f  m/s^2", gravity_abs));
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
