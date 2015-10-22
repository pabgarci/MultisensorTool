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

public class Humidity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senHumidity;

    TextView textViewValue;

    protected void registerSensor(){
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senHumidity = senSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(senHumidity==null){
            textViewValue.setText("Humidity sensor unavailable");
        }else{
            senSensorManager.registerListener(this, senHumidity , SensorManager.SENSOR_DELAY_NORMAL);
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
        setContentView(R.layout.activity_humidity);
        loadToolbar();
        textViewValue = (TextView)findViewById(R.id.textView_humidity_value);
        registerSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            textViewValue.setText(String.format("Value: %s lux", Float.toString(event.values[0])));
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
        senSensorManager.registerListener(this, senHumidity, SensorManager.SENSOR_DELAY_NORMAL);
    }

}


