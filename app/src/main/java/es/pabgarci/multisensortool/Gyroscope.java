package es.pabgarci.multisensortool;

import es.pabgarci.multisensortool.config.FilterConfigActivity;
import es.pabgarci.multisensortool.gauge.GaugeAcceleration;
import es.pabgarci.multisensortool.gauge.GaugeRotation;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/*
 * Acceleration Explorer
 * Copyright (C) 2013-2015, Kaleb Kircher - Kircher Engineering, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * A class that displays the acceleration sensor output in terms of tilt and
 * acceleration relative to gravity of earth.
 * 
 * @author Kaleb
 *
 */
public class Gyroscope extends FilterActivity {
	private GaugeAcceleration gaugeAcceleration;
	private GaugeRotation gaugeRotation;

	public void loadToolbar(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		assert getSupportActionBar() != null;
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);
	}

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
		inflater.inflate(R.menu.menu_gauges, menu);
		return true;
	}

	private void showAboutDialog()
	{
		Dialog helpDialog = new Dialog(this);

		helpDialog.setCancelable(true);
		helpDialog.setCanceledOnTouchOutside(true);
		helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = getLayoutInflater().inflate(R.layout.about, null);

		helpDialog.setContentView(view);

		helpDialog.show();
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
			showHelpDialog();
			return true;

			case R.id.menu_settings_about:
				showAboutDialog();
				return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showHelpDialog()
	{
		Dialog helpDialog = new Dialog(this);

		helpDialog.setCancelable(true);
		helpDialog.setCanceledOnTouchOutside(true);
		helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);

		helpDialog.setContentView(view);

		helpDialog.show();
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
