package es.pabgarci.multisensortool.filter;

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

/*
 * An implementation of the Android Developer low-pass filter. The Android
 * Developer LPF, is an IIR single-pole implementation. The coefficient, a
 * (alpha), can be adjusted based on the sample period of the sensor to produce
 * the desired time constant that the filter will act on. It is essentially the
 * same as the Wikipedia LPF. It takes a simple form of y[0] = alpha * y[0] + (1
 * - alpha) * x[0]. Alpha is defined as alpha = timeConstant / (timeConstant +
 * dt) where the time constant is the length of signals the filter should act on
 * and dt is the sample period (1/frequency) of the sensor.
 * 
 * 
 * @author Kaleb
 * @see http://developer.android.com/reference/android/hardware/SensorEvent.html
 * @version %I%, %G%
 */
public class LowPassFilterSmoothing
{
	// Constants for the low-pass filters
	private float timeConstant = 0.18f;
	private float alpha = 0.9f;
	private float dt = 0;

	// Timestamps for the low-pass filters
	private float timestamp = System.nanoTime();
	private float startTime = 0;

	private int count = 0;

	// Gravity and linear accelerations components for the
	// Wikipedia low-pass filter
	private float[] output = new float[]
	{ 0, 0, 0 };

	// Raw accelerometer data
	private float[] input = new float[]
	{ 0, 0, 0 };

	/*
	 * Add a sample.
	 * 
	 * @param acceleration
	 *            The acceleration data.
	 * @return Returns the output of the filter.
	 */
	public float[] addSamples(float[] acceleration)
	{
		// Initialize the start time.
		if (startTime == 0)
		{
			startTime = System.nanoTime();
		}

		timestamp = System.nanoTime();

		// Get a local copy of the sensor values
		System.arraycopy(acceleration, 0, this.input, 0, acceleration.length);

		// Find the sample period (between updates) and convert from
		// nanoseconds to seconds. Note that the sensor delivery rates can
		// individually vary by a relatively large time frame, so we use an
		// averaging technique with the number of sensor updates to
		// determine the delivery rate.
		dt = 1 / (count++ / ((timestamp - startTime) / 1000000000.0f));

		alpha = timeConstant / (timeConstant + dt);

		if (count > 5)
		{
			output[0] = alpha * output[0] + (1 - alpha) * input[0];
			output[1] = alpha * output[1] + (1 - alpha) * input[1];
			output[2] = alpha * output[2] + (1 - alpha) * input[2];
		}

		return output;
	}

	public void setTimeConstant(float timeConstant)
	{
		this.timeConstant = timeConstant;
	}

	public void reset()
	{
		startTime = 0;
		timestamp = 0;
		count = 0;
		dt = 0;
		alpha = 0;
	}
}
