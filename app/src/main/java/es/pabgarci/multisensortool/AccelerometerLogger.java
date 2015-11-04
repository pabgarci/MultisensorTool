package es.pabgarci.multisensortool;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import es.pabgarci.multisensortool.config.FilterConfigActivity;
import es.pabgarci.multisensortool.plot.Plot;

public class AccelerometerLogger extends Plot implements Runnable{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accelerometer_logger);

        loadToolbar();

        textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
        textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
        textViewZAxis = (TextView) findViewById(R.id.value_z_axis);
        textViewHzFrequency = (TextView) findViewById(R.id.value_hz_frequency);

        updateAccelerationText();
        initPlot("accelerometer");

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 10);

                updateAccelerationText();
                plot("accelerometer",0);
            }
        };
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings_sensor:
                Intent intent = new Intent(getApplicationContext(), FilterConfigActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_help:
                showHelpDialog("main");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Output and logs are run on their own thread to keep the UI from hanging
     * and the output smooth.
     */
    @Override
    public void run(){

        Thread.currentThread().interrupt();
    }



}
