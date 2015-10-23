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
public class LowPassFilterLinearAccel
{
	private static final String tag = LowPassFilterLinearAccel.class
			.getSimpleName();

	private float filterCoefficient = 0.5f;

	// Gravity and linear accelerations components for the
	// Wikipedia low-pass filter
	private float[] output = new float[]
	{ 0, 0, 0 };

	private float[] gravity = new float[]
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
		System.arraycopy(acceleration, 0, input, 0, acceleration.length);

		float oneMinusCoeff = (1.0f - filterCoefficient);

		gravity[0] = filterCoefficient * gravity[0] + oneMinusCoeff * input[0];
		gravity[1] = filterCoefficient * gravity[1] + oneMinusCoeff * input[1];
		gravity[2] = filterCoefficient * gravity[2] + oneMinusCoeff * input[2];

		// Determine the linear acceleration
		output[0] = input[0] - gravity[0];
		output[1] = input[1] - gravity[1];
		output[2] = input[2] - gravity[2];

		return output;
	}

	/*
	 * The complementary filter coefficient, a floating point value between 0-1,
	 * exclusive of 0, inclusive of 1.
	 * 
	 * @param filterCoefficient
	 */
	public void setFilterCoefficient(float filterCoefficient)
	{
		this.filterCoefficient = filterCoefficient;
	}
}
