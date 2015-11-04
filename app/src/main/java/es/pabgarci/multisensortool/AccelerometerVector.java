package es.pabgarci.multisensortool;

import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import es.pabgarci.multisensortool.config.FilterConfigActivity;
import es.pabgarci.multisensortool.view.AccelerationVectorView;

public class AccelerometerVector extends Common implements SensorEventListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_vector);
        loadToolbar();

        textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
        textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
        textViewZAxis = (TextView) findViewById(R.id.value_z_axis);
        textViewHzFrequency = (TextView) findViewById(R.id.value_hz_frequency);

        view = (AccelerationVectorView) findViewById(R.id.vector_acceleration);

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 100);

                updateAccelerationText();
                updateVector();
            }
        };
    }

    private AccelerationVectorView view;


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_accelerometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // Log the data
            case R.id.action_settings_sensor:
                Intent intent = new Intent(this, FilterConfigActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_settings_about:
                showAboutDialog("vector");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateVector(){

            view.updatePoint(acceleration[0], acceleration[1]);

    }

}
