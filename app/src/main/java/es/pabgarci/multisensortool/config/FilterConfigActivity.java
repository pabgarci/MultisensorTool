package es.pabgarci.multisensortool.config;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.widget.Toast;

import es.pabgarci.multisensortool.R;

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
 * Preferences for the smoothing and linear acceleration filters.
 * 
 * @author Kaleb
 *
 */
public class FilterConfigActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnPreferenceClickListener
{

	private static final String tag = FilterConfigActivity.class
			.getSimpleName();

	public static final String AXIS_INVERSION_ENABLED_KEY = "axis_inversion_enabled_preference";
	
	// Preference keys for smoothing filters
	public static final String MEAN_FILTER_SMOOTHING_ENABLED_KEY = "mean_filter_smoothing_enabled_preference";
	public static final String MEAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY = "mean_filter_smoothing_time_constant_preference";
	public static final String MEDIAN_FILTER_SMOOTHING_ENABLED_KEY = "median_filter_smoothing_enabled_preference";
	public static final String MEDIAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY = "median_filter_smoothing_time_constant_preference";
	public static final String LPF_SMOOTHING_ENABLED_KEY = "lpf_smoothing_enabled_preference";
	public static final String LPF_SMOOTHING_TIME_CONSTANT_KEY = "lpf_smoothing_time_constant_preference";

	// Preference keys for linear acceleration filters
	public static final String LPF_LINEAR_ACCEL_ENABLED_KEY = "lpf_linear_accel_enabled_preference";
	public static final String LPF_LINEAR_ACCEL_COEFF_KEY = "lpf_linear_accel_coeff_preference";

	public static final String ANDROID_LINEAR_ACCEL_ENABLED_KEY = "android_linear_accel_filter_preference";

	public static final String IMULACF_ORIENTATION_ENABLED_KEY = "imulacf_orienation_enabled_preference";
	public static final String IMULACF_ORIENTATION_COEFF_KEY = "imulacf_orienation_coeff_preference";

	public static final String IMULACF_ROTATION_MATRIX_ENABLED_KEY = "imulacf_rotation_matrix_enabled_preference";
	public static final String IMULACF_ROTATION_MATRIX_COEFF_KEY = "imulacf_rotation_matrix_coeff_preference";

	public static final String IMULACF_QUATERNION_ENABLED_KEY = "imulacf_quaternion_enabled_preference";
	public static final String IMULACF_QUATERNION_COEFF_KEY = "imulacf_quaternion_coeff_preference";

	public static final String IMULAKF_QUATERNION_ENABLED_KEY = "imulakf_quaternion_enabled_preference";

	private SwitchPreference spLpfLinearAccel;
	private SwitchPreference spAndroidLinearAccel;

	private SwitchPreference spImuLaCfOrientation;
	private SwitchPreference spImuLaCfRotationMatrix;
	private SwitchPreference spImuLaCfQuaternion;
	private SwitchPreference spImuLaKfQuaternion;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preference_filter_simple);

		spLpfLinearAccel = (SwitchPreference) findPreference(LPF_LINEAR_ACCEL_ENABLED_KEY);

		spAndroidLinearAccel = (SwitchPreference) findPreference(ANDROID_LINEAR_ACCEL_ENABLED_KEY);

		spImuLaCfOrientation = (SwitchPreference) findPreference(IMULACF_ORIENTATION_ENABLED_KEY);

		spImuLaCfRotationMatrix = (SwitchPreference) findPreference(IMULACF_ROTATION_MATRIX_ENABLED_KEY);

		spImuLaCfQuaternion = (SwitchPreference) findPreference(IMULACF_QUATERNION_ENABLED_KEY);

		spImuLaKfQuaternion = (SwitchPreference) findPreference(IMULAKF_QUATERNION_ENABLED_KEY);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference)
	{
		return false;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key)
	{
		if (key.equals(LPF_LINEAR_ACCEL_ENABLED_KEY))
		{
			if (sharedPreferences.getBoolean(key, false))
			{
				Editor edit = sharedPreferences.edit();

				edit.putBoolean(IMULACF_ORIENTATION_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_ROTATION_MATRIX_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(IMULAKF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(ANDROID_LINEAR_ACCEL_ENABLED_KEY, false);

				edit.apply();

				spImuLaCfOrientation.setChecked(false);
				spImuLaCfRotationMatrix.setChecked(false);
				spImuLaCfQuaternion.setChecked(false);
				spImuLaKfQuaternion.setChecked(false);
				spAndroidLinearAccel.setChecked(false);
			}
		}

		if (key.equals(IMULACF_ORIENTATION_ENABLED_KEY))
		{
			if (sharedPreferences.getBoolean(key, false))
			{
				Editor edit = sharedPreferences.edit();

				edit.putBoolean(IMULACF_ROTATION_MATRIX_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(IMULAKF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(LPF_LINEAR_ACCEL_ENABLED_KEY, false);
				edit.putBoolean(ANDROID_LINEAR_ACCEL_ENABLED_KEY, false);

				edit.apply();

				spImuLaCfRotationMatrix.setChecked(false);
				spImuLaCfQuaternion.setChecked(false);
				spImuLaKfQuaternion.setChecked(false);
				spLpfLinearAccel.setChecked(false);
				spAndroidLinearAccel.setChecked(false);
			}
		}

		if (key.equals(IMULACF_ROTATION_MATRIX_ENABLED_KEY))
		{
			if (sharedPreferences.getBoolean(key, false))
			{
				Editor edit = sharedPreferences.edit();

				edit.putBoolean(IMULACF_ORIENTATION_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(IMULAKF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(LPF_LINEAR_ACCEL_ENABLED_KEY, false);
				edit.putBoolean(ANDROID_LINEAR_ACCEL_ENABLED_KEY, false);

				edit.apply();

				spImuLaCfOrientation.setChecked(false);
				spImuLaCfQuaternion.setChecked(false);
				spImuLaKfQuaternion.setChecked(false);
				spLpfLinearAccel.setChecked(false);
				spAndroidLinearAccel.setChecked(false);
			}
		}

		if (key.equals(IMULAKF_QUATERNION_ENABLED_KEY))
		{
			if (sharedPreferences.getBoolean(key, false))
			{
				Editor edit = sharedPreferences.edit();

				edit.putBoolean(IMULACF_ORIENTATION_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_ROTATION_MATRIX_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(LPF_LINEAR_ACCEL_ENABLED_KEY, false);
				edit.putBoolean(ANDROID_LINEAR_ACCEL_ENABLED_KEY, false);

				edit.apply();

				spImuLaCfOrientation.setChecked(false);
				spImuLaCfRotationMatrix.setChecked(false);
				spLpfLinearAccel.setChecked(false);
				spAndroidLinearAccel.setChecked(false);
				spImuLaCfQuaternion.setChecked(false);
			}
		}

		if (key.equals(IMULACF_QUATERNION_ENABLED_KEY))
		{
			if (sharedPreferences.getBoolean(key, false))
			{
				Editor edit = sharedPreferences.edit();

				edit.putBoolean(IMULACF_ORIENTATION_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_ROTATION_MATRIX_ENABLED_KEY, false);
				edit.putBoolean(IMULAKF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(LPF_LINEAR_ACCEL_ENABLED_KEY, false);
				edit.putBoolean(ANDROID_LINEAR_ACCEL_ENABLED_KEY, false);

				edit.apply();

				spImuLaCfOrientation.setChecked(false);
				spImuLaCfRotationMatrix.setChecked(false);
				spLpfLinearAccel.setChecked(false);
				spAndroidLinearAccel.setChecked(false);
				spImuLaKfQuaternion.setChecked(false);
			}
		}

		if (key.equals(ANDROID_LINEAR_ACCEL_ENABLED_KEY))
		{
			if (sharedPreferences.getBoolean(key, false))
			{
				Editor edit = sharedPreferences.edit();

				edit.putBoolean(LPF_LINEAR_ACCEL_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_ORIENTATION_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_ROTATION_MATRIX_ENABLED_KEY, false);
				edit.putBoolean(IMULACF_QUATERNION_ENABLED_KEY, false);
				edit.putBoolean(IMULAKF_QUATERNION_ENABLED_KEY, false);

				edit.apply();

				spImuLaCfOrientation.setChecked(false);
				spImuLaCfRotationMatrix.setChecked(false);
				spImuLaCfQuaternion.setChecked(false);
				spImuLaKfQuaternion.setChecked(false);
				spLpfLinearAccel.setChecked(false);
			}
		}

		if (key.equals(IMULACF_ORIENTATION_COEFF_KEY))
		{
			if (Double.valueOf(sharedPreferences.getString(key, "0.5")) > 1)
			{
				sharedPreferences.edit().putString(key, "0.5").apply();

				((EditTextPreference) findPreference(IMULACF_ORIENTATION_COEFF_KEY))
						.setText("0.5");

				Toast.makeText(
						getApplicationContext(),
						"Whoa! The filter constant must be less than or equal to 1",
						Toast.LENGTH_LONG).show();
			}
		}

		if (key.equals(IMULACF_ROTATION_MATRIX_COEFF_KEY))
		{
			if (Double.valueOf(sharedPreferences.getString(key, "0.5")) > 1)
			{
				sharedPreferences.edit().putString(key, "0.5").apply();

				((EditTextPreference) findPreference(IMULACF_ROTATION_MATRIX_COEFF_KEY))
						.setText("0.5");

				Toast.makeText(
						getApplicationContext(),
						"Whoa! The filter constant must be less than or equal to 1",
						Toast.LENGTH_LONG).show();
			}
		}

		if (key.equals(IMULACF_QUATERNION_COEFF_KEY))
		{
			if (Double.valueOf(sharedPreferences.getString(key, "0.5")) > 1)
			{
				sharedPreferences.edit().putString(key, "0.5").apply();

				((EditTextPreference) findPreference(IMULACF_QUATERNION_COEFF_KEY))
						.setText("0.5");

				Toast.makeText(
						getApplicationContext(),
						"Whoa! The filter constant must be less than or equal to 1",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
