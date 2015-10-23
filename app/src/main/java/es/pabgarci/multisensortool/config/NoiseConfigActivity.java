package es.pabgarci.multisensortool.config;

import android.os.Bundle;
import android.preference.PreferenceActivity;

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
 * Preferences for the smoothing filters specifically for the NoiseActivity.
 * 
 * @author Kaleb
 *
 */
public class NoiseConfigActivity extends PreferenceActivity
{
	public static final String MEAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY = "noise_mean_filter_smoothing_time_constant_preference";
	public static final String MEDIAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY = "noise_median_filter_smoothing_time_constant_preference";
	public static final String LPF_SMOOTHING_TIME_CONSTANT_KEY = "noise_lpf_smoothing_time_constant_preference";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preference_noise_filter);
	}
}
