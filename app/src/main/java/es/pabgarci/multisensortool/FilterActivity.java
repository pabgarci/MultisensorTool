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

public abstract class FilterActivity extends AppCompatActivity implements SensorEventListener{

	public SharedPreferences sharedPref;

	private int count = 0;
	private float FAIL;
	private float startTime = 0;
	private float timestamp = 0;
	protected float hz = 0;

	protected volatile float[] acceleration = new float[3];
	protected volatile float[] linearAcceleration = new float[3];

	protected Handler handler;
	protected Runnable runnable;

	protected SensorManager sensorManager;

	protected int frequencySelection;

	protected TextView textViewXAxis;
	protected TextView textViewYAxis;
	protected TextView textViewZAxis;
	protected TextView textViewHzFrequency;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		sensorManager = (SensorManager) this
				.getSystemService(Context.SENSOR_SERVICE);
		getFilterLevelPrefs();
		handler = new Handler();
	}

	@Override
	public void onPause(){
		super.onPause();
		sensorManager.unregisterListener(this);
		handler.removeCallbacks(runnable);
	}

	@Override
	public void onResume(){
		super.onResume();

		resetSensorFrequencyTimer();
		updateSensorDelay();
		getFilterLevelPrefs();
		handler.post(runnable);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy){

	}

	@Override
	public synchronized void onSensorChanged(SensorEvent event){
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			calculateSensorFrequency();

			if(event.values[0]>=acceleration[0]+FAIL || event.values[0]<=acceleration[0]-FAIL){
				acceleration[0]=event.values[0];
			}
			if(event.values[1]>=acceleration[1]+FAIL || event.values[1]<=acceleration[1]-FAIL){
				acceleration[1]=event.values[1];
			}
			if(event.values[2]>=acceleration[2]+FAIL || event.values[2]<=acceleration[2]-FAIL){
				acceleration[2]=event.values[2];
			}

			//System.arraycopy(event.values, 0, acceleration, 0, event.values.length);

		}
	}


	private void getSensorFrequencyPrefs(){
		frequencySelection = Integer.parseInt(sharedPref.getString("sensor_frequency_preference", "2"));
	}

	private void getFilterLevelPrefs(){
		FAIL = Float.parseFloat(sharedPref.getString("filter_level", "0.1"));
	}


	private void setSensorDelay(int position)
	{
		switch (position){
			case 0:
					sensorManager.registerListener(this, sensorManager
									.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							SensorManager.SENSOR_DELAY_NORMAL);
				break;
			case 1:
				sensorManager.registerListener(this, sensorManager
								.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_GAME);
				break;
			case 2:
				sensorManager.registerListener(this, sensorManager
								.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_FASTEST);
				break;
		}
	}

	protected void updateAccelerationText(){

			textViewXAxis.setText(String.format("%.2f", acceleration[0]));
			textViewYAxis.setText(String.format("%.2f", acceleration[1]));
			textViewZAxis.setText(String.format("%.2f", acceleration[2]));

		    textViewHzFrequency.setText(String.format("%.2f", hz));
	}

	public void calculateSensorFrequency()
	{
		// Initialize the start time.
		if (startTime == 0)
		{
			startTime = System.nanoTime();
		}

		timestamp = System.nanoTime();

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
