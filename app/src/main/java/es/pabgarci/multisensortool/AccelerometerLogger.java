package es.pabgarci.multisensortool;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import android.os.Bundle;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidplot.xy.XYPlot;
import es.pabgarci.multisensortool.plot.DynamicLinePlot;
import es.pabgarci.multisensortool.plot.PlotColor;

public class AccelerometerLogger extends Common implements Runnable{

    // Plot keys for the acceleration plot
    private final static int PLOT_ACCEL_X_AXIS_KEY = 0;
    private final static int PLOT_ACCEL_Y_AXIS_KEY = 1;
    private final static int PLOT_ACCEL_Z_AXIS_KEY = 2;

    // Color keys for the acceleration plot
    private int plotAccelXAxisColor;
    private int plotAccelYAxisColor;
    private int plotAccelZAxisColor;

    private DynamicLinePlot dynamicPlot;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accelerometer_logger);

        loadToolbar();

        DecimalFormat df;

        textViewXAxis = (TextView) findViewById(R.id.value_x_axis);
        textViewYAxis = (TextView) findViewById(R.id.value_y_axis);
        textViewZAxis = (TextView) findViewById(R.id.value_z_axis);
        textViewHzFrequency = (TextView) findViewById(R.id.value_hz_frequency);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        df = (DecimalFormat) nf;
        df.applyPattern("###.####");

        updateAccelerationText();

        initColor();
        initPlots();

        runable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 10);

                updateAccelerationText();
                plotData();
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

    /**
     * Event Handling for Individual menu item selected Identify single menu
     * item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // Log the data
            case R.id.action_settings_sensor:

                return true;

            // Start the vector activity
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

    /**
     * Create the output graph line chart.
     */
    private void addAccelerationPlot()
    {
        addGraphPlot("X-Axis", PLOT_ACCEL_X_AXIS_KEY,
                plotAccelXAxisColor);
        addGraphPlot("Y-Axis", PLOT_ACCEL_Y_AXIS_KEY,
                plotAccelYAxisColor);
        addGraphPlot("Z-Axis", PLOT_ACCEL_Z_AXIS_KEY,
                plotAccelZAxisColor);
    }

    /**
     * Add a plot to the graph.
     *
     * @param title
     *            The name of the plot.
     * @param key
     *            The unique plot key
     * @param color
     *            The color of the plot
     */
    private void addGraphPlot(String title, int key, int color)
    {
        dynamicPlot.addSeriesPlot(title, key, color);
    }

    /**
     * Create the plot colors.
     */
    private void initColor()
    {
        PlotColor color = new PlotColor(this);

        plotAccelXAxisColor = color.getLightBlue();
        plotAccelYAxisColor = color.getLightGreen();
        plotAccelZAxisColor = color.getLightRed();
    }

    /**
     * Initialize the plots.
     */
    private void initPlots()
    {
        // Create the graph plot
        XYPlot plot = (XYPlot) findViewById(R.id.plot_sensor);

        plot.setTitle("Acceleration");
        dynamicPlot = new DynamicLinePlot(plot, this);
        dynamicPlot.setMaxRange(20);
        dynamicPlot.setMinRange(-20);

        addAccelerationPlot();
    }



    private void plotData() {


            dynamicPlot.setData(acceleration[0], PLOT_ACCEL_X_AXIS_KEY);
            dynamicPlot.setData(acceleration[1], PLOT_ACCEL_Y_AXIS_KEY);
            dynamicPlot.setData(acceleration[2], PLOT_ACCEL_Z_AXIS_KEY);


        dynamicPlot.draw();
    }


}
