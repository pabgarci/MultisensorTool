package es.pabgarci.multisensortool;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import es.pabgarci.multisensortool.config.FilterConfigActivity;
import es.pabgarci.multisensortool.filter.ImuLaCfOrientation;
import es.pabgarci.multisensortool.filter.ImuLaCfQuaternion;
import es.pabgarci.multisensortool.filter.ImuLaCfRotationMatrix;
import es.pabgarci.multisensortool.filter.ImuLaKfQuaternion;
import es.pabgarci.multisensortool.filter.ImuLinearAccelerationInterface;
import es.pabgarci.multisensortool.filter.LowPassFilterLinearAccel;
import es.pabgarci.multisensortool.filter.LowPassFilterSmoothing;
import es.pabgarci.multisensortool.filter.MeanFilterSmoothing;
import es.pabgarci.multisensortool.filter.MedianFilterSmoothing;
import es.pabgarci.multisensortool.prefs.PrefUtils;

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
 * A parent Activity for child classes that need to work with all the available
 * sensor filters. Deals with setting up all the sensors, the sensor
 * preferences, sensor state, etc...
 * 
 * @author Kaleb
 *
 */
public abstract class FilterActivity extends AppCompatActivity implements SensorEventListener{
	private final static String tag = FilterActivity.class.getSimpleName();
	
	protected boolean axisInverted = false;

	protected boolean meanFilterSmoothingEnabled;
	protected boolean medianFilterSmoothingEnabled;
	protected boolean lpfSmoothingEnabled;

	protected boolean lpfLinearAccelEnabled;
	protected boolean androidLinearAccelEnabled;

	protected boolean imuLaCfOrienationEnabled;
	protected boolean imuLaCfRotationMatrixEnabled;
	protected boolean imuLaCfQuaternionEnabled;
	protected boolean imuLaKfQuaternionEnabled;

	protected volatile boolean dataReady = false;

	private int count = 0;

	private float startTime = 0;
	private float timestamp = 0;
	protected float hz = 0;

	// Outputs for the acceleration and LPFs
	protected volatile float[] acceleration = new float[3];
	protected volatile float[] linearAcceleration = new float[3];
	protected float[] magnetic = new float[3];
	protected float[] rotation = new float[3];

	// Handler for the UI plots so everything plots smoothly
	protected Handler handler;

	protected ImuLinearAccelerationInterface imuLinearAcceleration;

	protected MeanFilterSmoothing meanFilterAccelSmoothing;
	protected MeanFilterSmoothing meanFilterMagneticSmoothing;
	protected MeanFilterSmoothing meanFilterRotationSmoothing;

	protected MedianFilterSmoothing medianFilterAccelSmoothing;
	protected MedianFilterSmoothing medianFilterMagneticSmoothing;
	protected MedianFilterSmoothing medianFilterRotationSmoothing;

	protected LowPassFilterSmoothing lpfAccelSmoothing;
	protected LowPassFilterSmoothing lpfMagneticSmoothing;
	protected LowPassFilterSmoothing lpfRotationSmoothing;

	protected LowPassFilterLinearAccel lpfLinearAcceleration;

	protected Runnable runable;

	// Sensor manager to access the accelerometer sensor
	protected SensorManager sensorManager;

	protected int frequencySelection;

	// Text views for real-time output
	protected TextView textViewXAxis;
	protected TextView textViewYAxis;
	protected TextView textViewZAxis;
	protected TextView textViewHzFrequency;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		meanFilterAccelSmoothing = new MeanFilterSmoothing();
		meanFilterMagneticSmoothing = new MeanFilterSmoothing();
		meanFilterRotationSmoothing = new MeanFilterSmoothing();

		medianFilterAccelSmoothing = new MedianFilterSmoothing();
		medianFilterMagneticSmoothing = new MedianFilterSmoothing();
		medianFilterRotationSmoothing = new MedianFilterSmoothing();

		lpfAccelSmoothing = new LowPassFilterSmoothing();
		lpfMagneticSmoothing = new LowPassFilterSmoothing();
		lpfRotationSmoothing = new LowPassFilterSmoothing();

		lpfLinearAcceleration = new LowPassFilterLinearAccel();

		sensorManager = (SensorManager) this
				.getSystemService(Context.SENSOR_SERVICE);

		handler = new Handler();
	}

	@Override
	public void onPause()
	{
		super.onPause();

		sensorManager.unregisterListener(this);

		handler.removeCallbacks(runable);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		resetSensorFrequencyTimer();
		initFilters();
		getAxisPrefs();
		updateSensorDelay();

		handler.post(runable);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{

	}

	@Override
	public synchronized void onSensorChanged(SensorEvent event)
	{
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER
				|| event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			calculateSensorFrequency();
			
			dataReady = true;
		}

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			// Get a local copy of the sensor values
			System.arraycopy(event.values, 0, acceleration, 0,
					event.values.length);

			if (axisInverted)
			{
				acceleration[0] = -acceleration[0];
				acceleration[1] = -acceleration[1];
				acceleration[2] = -acceleration[2];
			}

			if (meanFilterSmoothingEnabled)
			{
				acceleration = meanFilterAccelSmoothing
						.addSamples(acceleration);
			}

			if (medianFilterSmoothingEnabled)
			{
				acceleration = medianFilterAccelSmoothing
						.addSamples(acceleration);
			}

			if (lpfSmoothingEnabled)
			{
				acceleration = lpfAccelSmoothing.addSamples(acceleration);
			}

			if (lpfLinearAccelEnabled)
			{
				linearAcceleration = lpfLinearAcceleration
						.addSamples(acceleration);
			}

			if (imuLaCfOrienationEnabled || imuLaCfRotationMatrixEnabled
					|| imuLaCfQuaternionEnabled || imuLaKfQuaternionEnabled)
			{
				imuLinearAcceleration.setAcceleration(acceleration);
			}
		}

		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			// Get a local copy of the sensor values
			System.arraycopy(event.values, 0, linearAcceleration, 0,
					event.values.length);

			if (axisInverted)
			{
				linearAcceleration[0] = -linearAcceleration[0];
				linearAcceleration[1] = -linearAcceleration[1];
				linearAcceleration[2] = -linearAcceleration[2];
			}

			if (meanFilterSmoothingEnabled)
			{
				linearAcceleration = meanFilterAccelSmoothing
						.addSamples(linearAcceleration);
			}

			if (medianFilterSmoothingEnabled)
			{
				linearAcceleration = medianFilterAccelSmoothing
						.addSamples(linearAcceleration);
			}

			if (lpfSmoothingEnabled)
			{
				linearAcceleration = lpfAccelSmoothing
						.addSamples(linearAcceleration);
			}
		}

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		{

			// Get a local copy of the sensor values
			System.arraycopy(event.values, 0, magnetic, 0, event.values.length);

			if (meanFilterSmoothingEnabled)
			{
				magnetic = meanFilterMagneticSmoothing.addSamples(magnetic);
			}

			if (medianFilterSmoothingEnabled)
			{
				magnetic = medianFilterMagneticSmoothing.addSamples(magnetic);
			}

			if (lpfSmoothingEnabled)
			{
				magnetic = lpfMagneticSmoothing.addSamples(magnetic);
			}

			if (imuLaCfOrienationEnabled || imuLaCfRotationMatrixEnabled
					|| imuLaCfQuaternionEnabled || imuLaKfQuaternionEnabled)
			{
				imuLinearAcceleration.setMagnetic(magnetic);
			}
		}

		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
		{
			// Get a local copy of the sensor values
			System.arraycopy(event.values, 0, rotation, 0, event.values.length);

			if (meanFilterSmoothingEnabled)
			{
				rotation = meanFilterRotationSmoothing.addSamples(rotation);
			}

			if (medianFilterSmoothingEnabled)
			{
				rotation = medianFilterRotationSmoothing.addSamples(rotation);
			}

			if (lpfSmoothingEnabled)
			{
				rotation = lpfRotationSmoothing.addSamples(rotation);
			}

			if (imuLaCfOrienationEnabled || imuLaCfRotationMatrixEnabled
					|| imuLaCfQuaternionEnabled || imuLaKfQuaternionEnabled)
			{
				imuLinearAcceleration.setGyroscope(rotation, System.nanoTime());

				linearAcceleration = imuLinearAcceleration
						.getLinearAcceleration();
			}
		}
	}

	private void initFilters()
	{
		meanFilterSmoothingEnabled = getPrefMeanFilterSmoothingEnabled();

		medianFilterSmoothingEnabled = getPrefMedianFilterSmoothingEnabled();

		meanFilterAccelSmoothing
				.setTimeConstant(getPrefMeanFilterSmoothingTimeConstant());
		meanFilterMagneticSmoothing
				.setTimeConstant(getPrefMeanFilterSmoothingTimeConstant());
		meanFilterRotationSmoothing
				.setTimeConstant(getPrefMeanFilterSmoothingTimeConstant());

		medianFilterAccelSmoothing
				.setTimeConstant(getPrefMedianFilterSmoothingTimeConstant());
		medianFilterMagneticSmoothing
				.setTimeConstant(getPrefMedianFilterSmoothingTimeConstant());
		medianFilterRotationSmoothing
				.setTimeConstant(getPrefMedianFilterSmoothingTimeConstant());

		lpfSmoothingEnabled = getPrefLpfSmoothingEnabled();

		lpfAccelSmoothing.setTimeConstant(getPrefLpfSmoothingTimeConstant());
		lpfMagneticSmoothing.setTimeConstant(getPrefLpfSmoothingTimeConstant());
		lpfRotationSmoothing.setTimeConstant(getPrefLpfSmoothingTimeConstant());

		lpfLinearAccelEnabled = getPrefLpfLinearAccelEnabled();
		lpfLinearAcceleration
				.setFilterCoefficient(getPrefLpfLinearAccelCoeff());

		imuLaCfOrienationEnabled = getPrefImuLaCfOrientationEnabled();
		imuLaCfRotationMatrixEnabled = getPrefImuLaCfRotationMatrixEnabled();

		imuLaCfQuaternionEnabled = getPrefImuLaCfQuaternionEnabled();

		imuLaKfQuaternionEnabled = getPrefImuLaKfQuaternionEnabled();

		if (imuLaCfOrienationEnabled)
		{
			imuLinearAcceleration = new ImuLaCfOrientation();
			imuLinearAcceleration
					.setFilterCoefficient(getPrefImuLaCfOrienationCoeff());
		}
		else if (imuLaCfRotationMatrixEnabled)
		{
			imuLinearAcceleration = new ImuLaCfRotationMatrix();
			imuLinearAcceleration
					.setFilterCoefficient(getPrefImuLaCfRotationMatrixCoeff());
		}
		else if (imuLaCfQuaternionEnabled)
		{
			imuLinearAcceleration = new ImuLaCfQuaternion();
			imuLinearAcceleration
					.setFilterCoefficient(getPrefImuLaCfQuaternionCoeff());
		}
		else if (imuLaKfQuaternionEnabled)
		{
			imuLinearAcceleration = new ImuLaKfQuaternion();
		}

		androidLinearAccelEnabled = getPrefAndroidLinearAccelEnabled();
	}

	private boolean getPrefAndroidLinearAccelEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs.getBoolean(
				FilterConfigActivity.ANDROID_LINEAR_ACCEL_ENABLED_KEY, false);
	}

	private boolean getPrefImuLaCfOrientationEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs.getBoolean(
				FilterConfigActivity.IMULACF_ORIENTATION_ENABLED_KEY, false);
	}

	private boolean getPrefImuLaCfRotationMatrixEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs
				.getBoolean(
						FilterConfigActivity.IMULACF_ROTATION_MATRIX_ENABLED_KEY,
						false);
	}

	private boolean getPrefImuLaCfQuaternionEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs.getBoolean(
				FilterConfigActivity.IMULACF_QUATERNION_ENABLED_KEY, false);
	}

	private boolean getPrefImuLaKfQuaternionEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs.getBoolean(
				FilterConfigActivity.IMULAKF_QUATERNION_ENABLED_KEY, false);
	}

	private boolean getPrefLpfLinearAccelEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs.getBoolean(
				FilterConfigActivity.LPF_LINEAR_ACCEL_ENABLED_KEY, false);
	}

	private float getPrefLpfLinearAccelCoeff()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return Float.valueOf(prefs.getString(
				FilterConfigActivity.LPF_LINEAR_ACCEL_COEFF_KEY, "0.5"));
	}

	private float getPrefImuLaCfOrienationCoeff()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return Float.valueOf(prefs.getString(
				FilterConfigActivity.IMULACF_ORIENTATION_COEFF_KEY, "0.5"));
	}

	private float getPrefImuLaCfRotationMatrixCoeff()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return Float.valueOf(prefs.getString(
				FilterConfigActivity.IMULACF_ROTATION_MATRIX_COEFF_KEY, "0.5"));
	}

	private float getPrefImuLaCfQuaternionCoeff()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return Float.valueOf(prefs.getString(
				FilterConfigActivity.IMULACF_QUATERNION_COEFF_KEY, "0.5"));
	}

	private boolean getPrefLpfSmoothingEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs.getBoolean(FilterConfigActivity.LPF_SMOOTHING_ENABLED_KEY,
				false);
	}

	private float getPrefLpfSmoothingTimeConstant()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return Float.valueOf(prefs.getString(
				FilterConfigActivity.LPF_SMOOTHING_TIME_CONSTANT_KEY, "0.5"));
	}

	private boolean getPrefMeanFilterSmoothingEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs.getBoolean(
				FilterConfigActivity.MEAN_FILTER_SMOOTHING_ENABLED_KEY, false);
	}

	private float getPrefMeanFilterSmoothingTimeConstant()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return Float.valueOf(prefs.getString(
				FilterConfigActivity.MEAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY,
				"0.5"));
	}

	private boolean getPrefMedianFilterSmoothingEnabled()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return prefs
				.getBoolean(
						FilterConfigActivity.MEDIAN_FILTER_SMOOTHING_ENABLED_KEY,
						false);
	}

	private float getPrefMedianFilterSmoothingTimeConstant()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		return Float.valueOf(prefs.getString(
				FilterConfigActivity.MEDIAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY,
				"0.5"));
	}

	private void getAxisPrefs()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		axisInverted = prefs.getBoolean(
				FilterConfigActivity.AXIS_INVERSION_ENABLED_KEY, false);
	}

	/**
	 * Read in the current user preferences.
	 */
	private void getSensorFrequencyPrefs()
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		this.frequencySelection = Integer.parseInt(prefs.getString(PrefUtils.SENSOR_FREQUENCY_PREF,
				PrefUtils.SENSOR_FREQUENCY_FAST));
	}

	/**
	 * Set the sensor delay based on user preferences. 0 = slow, 1 = medium, 2 =
	 * fast.
	 * 
	 * @param position
	 *            The desired sensor delay.
	 */
	private void setSensorDelay(int position)
	{
		switch (position)
		{
		case 0:

			if (!androidLinearAccelEnabled)
			{
				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_NORMAL);
			}
			else
			{
				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
						SensorManager.SENSOR_DELAY_NORMAL);
			}

			if ((imuLaCfOrienationEnabled || imuLaCfRotationMatrixEnabled
					|| imuLaCfQuaternionEnabled || imuLaKfQuaternionEnabled)
					&& !androidLinearAccelEnabled)
			{

				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
						SensorManager.SENSOR_DELAY_NORMAL);

				// Register for sensor updates.
				sensorManager.registerListener(this,
						sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
						SensorManager.SENSOR_DELAY_NORMAL);
			}

			break;
		case 1:

			if (!androidLinearAccelEnabled)
			{

				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_GAME);
			}
			else
			{

				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
						SensorManager.SENSOR_DELAY_GAME);
			}

			if ((imuLaCfOrienationEnabled || imuLaCfRotationMatrixEnabled
					|| imuLaCfQuaternionEnabled || imuLaKfQuaternionEnabled)
					&& !androidLinearAccelEnabled)
			{

				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
						SensorManager.SENSOR_DELAY_GAME);

				// Register for sensor updates.
				sensorManager.registerListener(this,
						sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
						SensorManager.SENSOR_DELAY_GAME);
			}

			break;
		case 2:

			if (!androidLinearAccelEnabled)
			{

				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_FASTEST);
			}
			else
			{

				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
						SensorManager.SENSOR_DELAY_FASTEST);
			}

			if ((imuLaCfOrienationEnabled || imuLaCfRotationMatrixEnabled
					|| imuLaCfQuaternionEnabled || imuLaKfQuaternionEnabled)
					&& !androidLinearAccelEnabled)
			{

				// Register for sensor updates.
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
						SensorManager.SENSOR_DELAY_FASTEST);

				// Register for sensor updates.
				sensorManager.registerListener(this,
						sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
						SensorManager.SENSOR_DELAY_FASTEST);
			}

			break;
		}
	}

	/**
	 * Update the acceleration sensor output Text Views.
	 */
	protected void updateAccelerationText()
	{
		if (!lpfLinearAccelEnabled && !imuLaCfOrienationEnabled
				&& !imuLaCfRotationMatrixEnabled && !imuLaCfQuaternionEnabled
				&& !androidLinearAccelEnabled && !imuLaKfQuaternionEnabled)
		{
			// Update the acceleration data
			textViewXAxis.setText(String.format("%.2f", acceleration[0]));
			textViewYAxis.setText(String.format("%.2f", acceleration[1]));
			textViewZAxis.setText(String.format("%.2f", acceleration[2]));
		}
		else
		{
			// Update the acceleration data
			textViewXAxis.setText(String.format("%.2f", linearAcceleration[0]));
			textViewYAxis.setText(String.format("%.2f", linearAcceleration[1]));
			textViewZAxis.setText(String.format("%.2f", linearAcceleration[2]));
		}
		
		textViewHzFrequency.setText(String.format("%.2f", hz));
	}

	private void calculateSensorFrequency()
	{
		// Initialize the start time.
		if (startTime == 0)
		{
			startTime = System.nanoTime();
		}

		timestamp = System.nanoTime();

		// Find the sample period (between updates) and convert from
		// nanoseconds to seconds. Note that the sensor delivery rates can
		// individually vary by a relatively large time frame, so we use an
		// averaging technique with the number of sensor updates to
		// determine the delivery rate.
		hz = (count++ / ((timestamp - startTime) / 1000000000.0f));
	}

	private void resetSensorFrequencyTimer()
	{
		count = 0;
		startTime = 0;
		timestamp = 0;
		hz = 0;
	}

	/**
	 * Updates the sensor delay based on the user preference. 0 = slow, 1 =
	 * medium, 2 = fast.
	 */
	private void updateSensorDelay()
	{
		getSensorFrequencyPrefs();

		setSensorDelay(frequencySelection);
	}
}
