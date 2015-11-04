package es.pabgarci.multisensortool.plot;

import android.os.Bundle;
import com.androidplot.xy.XYPlot;
import es.pabgarci.multisensortool.Common;
import es.pabgarci.multisensortool.R;

public class Plot extends Common {

    private final static int PLOT_ACCEL_X_AXIS_KEY = 0;
    private final static int PLOT_ACCEL_Y_AXIS_KEY = 1;
    private final static int PLOT_ACCEL_Z_AXIS_KEY = 2;

    private final static int PLOT_AXIS_KEY = 0;

    private int plotAccelXAxisColor;
    private int plotAccelYAxisColor;
    private int plotAccelZAxisColor;
    private int plotGravityAxisColor;
    private int plotHumidityAxisColor;
    private int plotLightAxisColor;
    private int plotMagneticAxisColor;
    private int plotPressureAxisColor;
    private int plotTemperatureAxisColor;
    private int plotProximityAxisColor;

    private DynamicLinePlot dynamicPlot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initColor();
    }

    public void initPlot(String name){
        XYPlot plot;
        switch (name){
            case "accelerometer":
                plot = (XYPlot) findViewById(R.id.plot_sensor);
                plot.setTitle(getResources().getString(R.string.title_activity_accelerometer));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(30);
                dynamicPlot.setMinRange(-20);
                addGraphPlot(getResources().getString(R.string.label_x_axis), PLOT_ACCEL_X_AXIS_KEY,
                        plotAccelXAxisColor);
                addGraphPlot(getResources().getString(R.string.label_y_axis), PLOT_ACCEL_Y_AXIS_KEY,
                        plotAccelYAxisColor);
                addGraphPlot(getResources().getString(R.string.label_z_axis), PLOT_ACCEL_Z_AXIS_KEY,
                        plotAccelZAxisColor);
                break;
            case "magnetic":
                plot = (XYPlot) findViewById(R.id.plot_magnetic);
                plot.setTitle(getResources().getString(R.string.title_activity_magnetic_field));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(500);
                dynamicPlot.setMinRange(0);
                addGraphPlot(getResources().getString(R.string.title_activity_magnetic_field)+" (uT)", PLOT_AXIS_KEY,
                        plotMagneticAxisColor);
                break;
            case "light":
                plot = (XYPlot) findViewById(R.id.plot_light);
                plot.setTitle(getResources().getString(R.string.title_activity_light));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(300);
                dynamicPlot.setMinRange(0);
                addGraphPlot(getResources().getString(R.string.title_activity_light)+" (lux)", PLOT_AXIS_KEY,
                        plotLightAxisColor);
                break;
            case "pressure":
                plot = (XYPlot) findViewById(R.id.plot_pressure);
                plot.setTitle(getResources().getString(R.string.title_activity_pressure));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(1070);
                dynamicPlot.setMinRange(950);
                addGraphPlot(getResources().getString(R.string.title_activity_pressure)+" (hPa)", PLOT_AXIS_KEY,
                        plotPressureAxisColor);
                break;
            case "proximity":
                plot = (XYPlot) findViewById(R.id.plot_proximity);
                plot.setTitle(getResources().getString(R.string.title_activity_proximity));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(10);
                dynamicPlot.setMinRange(0);
                addGraphPlot(getResources().getString(R.string.title_activity_proximity)+" (cm)", PLOT_AXIS_KEY,
                        plotProximityAxisColor);
                break;
            case "temperature":
                plot = (XYPlot) findViewById(R.id.plot_temperature);
                plot.setTitle(getResources().getString(R.string.title_activity_temperature));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(75);
                dynamicPlot.setMinRange(0);
                addGraphPlot(getResources().getString(R.string.title_activity_temperature)+" (ºC)", PLOT_AXIS_KEY,
                        plotTemperatureAxisColor);
                break;
            case "humidity":
                plot = (XYPlot) findViewById(R.id.plot_humidity);
                plot.setTitle(getResources().getString(R.string.title_activity_humidity));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(100);
                dynamicPlot.setMinRange(0);
                addGraphPlot(getResources().getString(R.string.title_activity_humidity)+" (%)", PLOT_AXIS_KEY,
                        plotHumidityAxisColor);
                break;
            case "gravity":
                plot = (XYPlot) findViewById(R.id.plot_gravity);
                plot.setTitle(getResources().getString(R.string.title_activity_gravity));
                dynamicPlot = new DynamicLinePlot(plot, this);
                dynamicPlot.setMaxRange(25);
                dynamicPlot.setMinRange(-5);
                addGraphPlot(getResources().getString(R.string.title_activity_gravity)+" (m/s^2)", PLOT_AXIS_KEY,
                        plotGravityAxisColor);
                break;
        }
    }

   public void plot(String name, float value){
       switch (name){
           case "accelerometer":
               dynamicPlot.setData(acceleration[0], PLOT_ACCEL_X_AXIS_KEY);
               dynamicPlot.setData(acceleration[1], PLOT_ACCEL_Y_AXIS_KEY);
               dynamicPlot.setData(acceleration[2], PLOT_ACCEL_Z_AXIS_KEY);
               break;
           default:
               dynamicPlot.setData(value, PLOT_AXIS_KEY);
               break;
       }
       dynamicPlot.draw();
   }

    public void addGraphPlot(String title, int key, int color){
        dynamicPlot.addSeriesPlot(title, key, color);
    }

    public void initColor(){
        PlotColor color = new PlotColor(this);
        plotAccelXAxisColor = color.getLightBlue();
        plotAccelYAxisColor = color.getLightGreen();
        plotAccelZAxisColor = color.getLightRed();
        plotGravityAxisColor = color.getLightOrange();
        plotHumidityAxisColor = color.getLightBlue();
        plotLightAxisColor = color.getYellow();
        plotMagneticAxisColor = color.getDarkRed();
        plotPressureAxisColor = color.getLightGreen();
        plotProximityAxisColor = color.getDarkOrange();
        plotTemperatureAxisColor = color.getDarkRed();
    }




}
