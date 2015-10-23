package es.pabgarci.multisensortool.filter;

import java.util.Arrays;

import org.apache.commons.math3.complex.Quaternion;

import android.hardware.SensorManager;
import android.util.Log;

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

public class ImuLaCfQuaternion implements ImuLinearAccelerationInterface
{
	// Developer Note: The quaternions rely on the Apache Commons Math
	// Quaternion object. The dependency is already in the project because I use
	// it for statistical analysis else where. The object
	// creation is not idea, but I am not prioritizing performance. If you don't
	// want the dependency or want it to go faster, building your own quaternion
	// as a single array is trivial (and is already done in the gyroscope
	// integrations). Writing the quaternion functions (multiply and add) to use
	// the vectors is not as trivial as writing the vector, but is easy enough.

	private static final String tag = ImuLaCfQuaternion.class.getSimpleName();

	public static final float EPSILON = 0.000000001f;

	// private static final float NS2S = 1.0f / 10000.0f;
	// Nano-second to second conversion
	private static final float NS2S = 1.0f / 1000000000.0f;

	private boolean hasOrientation = false;

	// copy the new gyro values into the gyro array
	// convert the raw gyro data into a rotation vector
	private double[] deltaVectorGyro = new double[4];

	// The coefficient for the filter... 0.5 = means it is averaging the two
	// transfer functions (rotations from the gyroscope and
	// acceleration/magnetic, respectively).
	public float filterCoefficient = 0.5f;

	private float dT = 0;

	private float[] components = new float[3];

	// angular speeds from gyro
	private float[] gyroscope = new float[3];

	// rotation matrix from gyro data
	private float[] fusedMatrix = new float[9];

	// magnetic field vector
	private float[] magnetic = new float[3];

	// accelerometer vector
	private float[] acceleration = new float[3];

	private float[] baseOrientation = new float[3];

	// final orientation angles from sensor fusion
	private float[] fusedOrientation = new float[3];

	// accelerometer and magnetometer based rotation matrix
	private float[] rotationMatrix = new float[9];

	private float[] linearAcceleration = new float[3];

	private float[] fusedVector = new float[4];

	private long timeStamp;

	private Quaternion quatGyroDelta;
	private Quaternion quatGyro;
	private Quaternion quatAccelMag;


	public ImuLaCfQuaternion()
	{
		super();
	}

	private void calculateOrientation()
	{
		// To get the orientation vector from the acceleration and magnetic
		// sensors, we let Android do the heavy lifting. This call will
		// automatically compensate for the tilt of the compass and fail if the
		// magnitude of the acceleration is not close to 9.82m/sec^2. You could
		// perform these steps yourself, but in my opinion, this is the best way
		// to do it.
		if (SensorManager.getRotationMatrix(rotationMatrix, null, acceleration,
				magnetic))
		{
			SensorManager.getOrientation(rotationMatrix, baseOrientation);

			getRotationVectorFromAccelMag(baseOrientation);

			if (!hasOrientation)
			{
				quatGyro = new Quaternion(quatAccelMag.getScalarPart(),
						quatAccelMag.getVectorPart());
			}

			hasOrientation = true;
		}
	}

	public float[] getLinearAcceleration()
	{
		// values[0]: azimuth, rotation around the Z axis.
		// values[1]: pitch, rotation around the X axis.
		// values[2]: roll, rotation around the Y axis.

		// Find the gravity component of the X-axis
		// = g*-cos(pitch)*sin(roll);
		components[0] = (float) (SensorManager.GRAVITY_EARTH
				* -Math.cos(fusedOrientation[1]) * Math
				.sin(fusedOrientation[2]));

		// Find the gravity component of the Y-axis
		// = g*-sin(pitch);
		components[1] = (float) (SensorManager.GRAVITY_EARTH * -Math
				.sin(fusedOrientation[1]));

		// Find the gravity component of the Z-axis
		// = g*cos(pitch)*cos(roll);
		components[2] = (float) (SensorManager.GRAVITY_EARTH
				* Math.cos(fusedOrientation[1]) * Math.cos(fusedOrientation[2]));

		// Subtract the gravity component of the signal
		// from the input acceleration signal to get the
		// tilt compensated output.
		linearAcceleration[0] = (this.acceleration[0] - components[0]);
		linearAcceleration[1] = (this.acceleration[1] - components[1]);
		linearAcceleration[2] = (this.acceleration[2] - components[2]);

		return linearAcceleration;
	}

	public void setAcceleration(float[] acceleration)
	{
		// Get a local copy of the raw magnetic values from the device sensor.
		System.arraycopy(acceleration, 0, this.acceleration, 0,
				acceleration.length);

		// We fuse the orientation of the magnetic and acceleration sensor based
		// on acceleration sensor updates. It could be done when the magnetic
		// sensor updates or when they both have updated if you want to spend
		// the resources to make the checks.
		calculateOrientation();
	}

	public void setFilterCoefficient(float filterCoefficient)
	{
		this.filterCoefficient = filterCoefficient;
	}

	public void setGyroscope(float[] gyroscope, long timeStamp)
	{
		// don't start until first accelerometer/magnetometer orientation has
		// been acquired
		if (!hasOrientation)
		{
			return;
		}

		if (this.timeStamp != 0)
		{
			dT = (timeStamp - this.timeStamp) * NS2S;

			System.arraycopy(gyroscope, 0, this.gyroscope, 0, 3);
			getRotationVectorFromGyro(dT);
		}

		// measurement done, save current time for next interval
		this.timeStamp = timeStamp;

		calculateFusedOrientation();
	}

	public void setMagnetic(float[] magnetic)
	{
		// Get a local copy of the raw magnetic values from the device sensor.
		System.arraycopy(magnetic, 0, this.magnetic, 0, magnetic.length);

	}

	private void getRotationVectorFromAccelMag(float[] orientation)
	{
		// Assuming the angles are in radians.

		// getOrientation() values:
		// values[0]: azimuth, rotation around the Z axis.
		// values[1]: pitch, rotation around the X axis.
		// values[2]: roll, rotation around the Y axis.

		// Heading, Azimuth, Yaw
		double c1 = Math.cos(orientation[0] / 2);
		double s1 = Math.sin(orientation[0] / 2);

		// Pitch, Attitude
		// The equation assumes the pitch is pointed in the opposite direction
		// of the orientation vector provided by Android, so we invert it.
		double c2 = Math.cos(-orientation[1] / 2);
		double s2 = Math.sin(-orientation[1] / 2);

		// Roll, Bank
		double c3 = Math.cos(orientation[2] / 2);
		double s3 = Math.sin(orientation[2] / 2);

		double c1c2 = c1 * c2;
		double s1s2 = s1 * s2;

		double w = c1c2 * c3 - s1s2 * s3;
		double x = c1c2 * s3 + s1s2 * c3;
		double y = s1 * c2 * c3 + c1 * s2 * s3;
		double z = c1 * s2 * c3 - s1 * c2 * s3;

		// The quaternion in the equation does not share the same coordinate
		// system as the Android gyroscope quaternion we are using. We reorder
		// it here.

		// Android X (pitch) = Equation Z (pitch)
		// Android Y (roll) = Equation X (roll)
		// Android Z (azimuth) = Equation Y (azimuth)

		quatAccelMag = new Quaternion(w, z, x, y);
	}

	private void getRotationVectorFromGyro(float timeFactor)
	{
		// Calculate the angular speed of the sample
		float magnitude = (float) Math.sqrt(Math.pow(gyroscope[0], 2)
				+ Math.pow(gyroscope[1], 2) + Math.pow(gyroscope[2], 2));

		// Normalize the rotation vector if it's big enough to get the axis
		if (magnitude > EPSILON)
		{
			gyroscope[0] /= magnitude;
			gyroscope[1] /= magnitude;
			gyroscope[2] /= magnitude;
		}

		// Integrate around this axis with the angular speed by the timestep
		// in order to get a delta rotation from this sample over the timestep
		// We will convert this axis-angle representation of the delta rotation
		// into a quaternion before turning it into the rotation matrix.
		float thetaOverTwo = magnitude * timeFactor / 2.0f;
		float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
		float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);

		deltaVectorGyro[0] = sinThetaOverTwo * gyroscope[0];
		deltaVectorGyro[1] = sinThetaOverTwo * gyroscope[1];
		deltaVectorGyro[2] = sinThetaOverTwo * gyroscope[2];
		deltaVectorGyro[3] = cosThetaOverTwo;

		// Create a new quaternion object base on the latest rotation
		// measurements...
		quatGyroDelta = new Quaternion(deltaVectorGyro[3], Arrays.copyOfRange(
				deltaVectorGyro, 0, 3));

		// Since it is a unit quaternion, we can just multiply the old rotation
		// by the new rotation delta to integrate the rotation.
		quatGyro = quatGyro.multiply(quatGyroDelta);
	}

	private void calculateFusedOrientation()
	{
		float oneMinusCoeff = (1.0f - filterCoefficient);

		// Apply the complementary filter. // We multiply each rotation by their
		// coefficients (scalar matrices)...

		// Scale our quaternion for the gyroscope
		quatGyro = quatGyro.multiply(filterCoefficient);

		// Scale our quaternion for the accel/mag
		quatAccelMag = quatAccelMag.multiply(1 - oneMinusCoeff);

		// ...and then add the two quaternions together.
		// output[0] = alpha * output[0] + (1 - alpha) * input[0];
		quatGyro = quatGyro.add(quatAccelMag);

		// Now we get a structure we can pass to get a rotation matrix, and then
		// an orientation vector from Android.
		fusedVector[0] = (float) quatGyro.getVectorPart()[0];
		fusedVector[1] = (float) quatGyro.getVectorPart()[1];
		fusedVector[2] = (float) quatGyro.getVectorPart()[2];
		fusedVector[3] = (float) quatGyro.getScalarPart();

		// We need a rotation matrix so we can get the orientation vector...
		// Getting Euler
		// angles from a quaternion is not trivial, so this is the easiest way,
		// but perhaps
		// not the fastest way of doing this.
		SensorManager.getRotationMatrixFromVector(fusedMatrix, fusedVector);

		// Get the fused orienatation
		SensorManager.getOrientation(fusedMatrix, fusedOrientation);
	}

}
