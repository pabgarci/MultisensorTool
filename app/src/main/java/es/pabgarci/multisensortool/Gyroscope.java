package es.pabgarci.multisensortool;

import es.pabgarci.multisensortool.config.FilterConfigActivity;
import es.pabgarci.multisensortool.gauge.GaugeAcceleration;
import es.pabgarci.multisensortool.gauge.GaugeRotation;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;

public class Gyroscope extends Common {

	private GaugeAcceleration gaugeAcceleration;
	private GaugeRotation gaugeRotation;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gyroscope);
		loadToolbar();
		textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
		textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
		textViewZAxis = (TextView) findViewById(R.id.value_z_axis);
		textViewHzFrequency = (TextView) findViewById(R.id.value_hz_frequency);

		gaugeAcceleration = (GaugeAcceleration) findViewById(R.id.gauge_acceleration);
		gaugeRotation = (GaugeRotation) findViewById(R.id.gauge_rotation);

		runable = new Runnable()
		{
			@Override
			public void run()
			{
				handler.postDelayed(this, 100);

				updateAccelerationText();
				updateGauges();
			}
		};
	}

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
		switch (item.getItemId()){
		// Log the data
		case R.id.action_settings_sensor:
			Intent intent = new Intent(this, FilterConfigActivity.class);
			startActivity(intent);
			return true;

			// Log the data
		case R.id.menu_settings_help:
			showHelpDialog("gyroscope");
			return true;

			case R.id.menu_settings_about:
				showAboutDialog("gyroscope");
				return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void updateGauges()
	{
		if (!lpfLinearAccelEnabled && !imuLaCfOrienationEnabled
				&& !imuLaCfRotationMatrixEnabled && !imuLaCfQuaternionEnabled
				&& !imuLaKfQuaternionEnabled && !androidLinearAccelEnabled)
		{
			gaugeAcceleration.updatePoint(acceleration[0], acceleration[1],
					Color.rgb(255, 61, 0));
			gaugeRotation.updateRotation(acceleration);
		}
		else
		{
			gaugeAcceleration.updatePoint(linearAcceleration[0],
					linearAcceleration[1], Color.rgb(255, 61, 0));
			gaugeRotation.updateRotation(linearAcceleration);
		}
	}
}
