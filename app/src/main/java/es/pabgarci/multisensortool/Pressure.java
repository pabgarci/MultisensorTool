package es.pabgarci.multisensortool;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Pressure extends AppCompatActivity  implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senPressure;

    TextView textViewValue;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senPressure = senSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(senPressure==null){
            textViewValue.setText("Pressure sensor unavailable");
        }else{
            senSensorManager.registerListener(this, senPressure , SensorManager.SENSOR_DELAY_NORMAL);
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
        setContentView(R.layout.activity_pressure);
        loadToolbar();
        textViewValue = (TextView)findViewById(R.id.textView_pressure_value);
        registerSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_PRESSURE) {
            textViewValue.setText(String.format("Value: %s hPa", Float.toString(event.values[0])));
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
        senSensorManager.registerListener(this, senPressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
