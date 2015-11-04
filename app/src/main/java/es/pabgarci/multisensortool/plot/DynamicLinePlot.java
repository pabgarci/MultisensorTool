package es.pabgarci.multisensortool.plot;

import java.util.LinkedList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.util.TypedValue;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.PositionMetrics;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class DynamicLinePlot
{
	private static final int VERTEX_WIDTH = 4;
	private static final int LINE_WIDTH = 4;

	private int windowSize = 200;

	private double maxRange = 10;
	private double minRange = -10;

	private Context context;
	
	private XYPlot plot;

	private SparseArray<SimpleXYSeries> series;
	private SparseArray<LinkedList<Number>> history;

	public DynamicLinePlot(XYPlot plot, Context context)
	{
		this.plot = plot;
		this.context = context;

		series = new SparseArray<>();
		history = new SparseArray<>();

		initPlot();
	}


	public void setMaxRange(double maxRange)
	{
		this.maxRange = maxRange;
		plot.setRangeBoundaries(minRange, maxRange, BoundaryMode.FIXED);
	}

	public void setMinRange(double minRange)
	{
		this.minRange = minRange;
		plot.setRangeBoundaries(minRange, maxRange, BoundaryMode.FIXED);
	}

	public void setData(double data, int key)
	{

		if (history.get(key).size() > windowSize)
		{
			history.get(key).removeFirst();
		}

		history.get(key).addLast(data);

		series.get(key).setModel(history.get(key),
				SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
	}

	public synchronized void draw()
	{
		plot.redraw();
	}

	public void addSeriesPlot(String seriesName, int key, int color)
	{
		history.append(key, new LinkedList<Number>());

		series.append(key, new SimpleXYSeries(seriesName));

		LineAndPointFormatter formatter = new LineAndPointFormatter(Color.rgb(
				0, 153, 204), Color.rgb(0, 153, 204), Color.TRANSPARENT,
				new PointLabelFormatter(Color.TRANSPARENT));

		Paint linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setColor(color);
		linePaint.setStrokeWidth(LINE_WIDTH);

		formatter.setLinePaint(linePaint);

		Paint vertexPaint = new Paint();
		vertexPaint.setAntiAlias(true);
		vertexPaint.setStyle(Paint.Style.STROKE);
		vertexPaint.setColor(color);
		vertexPaint.setStrokeWidth(VERTEX_WIDTH);

		formatter.setVertexPaint(vertexPaint);

		plot.addSeries(series.get(key), formatter);

	}


	private void initPlot()
	{
		this.plot.setRangeBoundaries(minRange, maxRange,
				BoundaryMode.FIXED);
		this.plot.setDomainBoundaries(0, windowSize, BoundaryMode.FIXED);

		this.plot.setDomainStepValue(5);
		this.plot.setTicksPerRangeLabel(3);
		this.plot.getDomainLabelWidget().pack();
		this.plot.getRangeLabelWidget().pack();
		this.plot.getLegendWidget().setWidth(0.7f);
		this.plot.setGridPadding(15, 15, 15, 15);

		this.plot.getGraphWidget().setGridBackgroundPaint(null);
		this.plot.getGraphWidget().setBackgroundPaint(null);
		this.plot.getGraphWidget().setBorderPaint(null);

		Paint paint = new Paint();

		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.rgb(119, 119, 119));
		paint.setStrokeWidth(10);

		this.plot.getGraphWidget().setDomainOriginLinePaint(paint);
		this.plot.getGraphWidget().setRangeOriginLinePaint(paint);

		this.plot.setBorderPaint(null);
		this.plot.setBackgroundPaint(null);
		
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
				r.getDisplayMetrics());
		
		plot.getLegendWidget().getTextPaint().setTextSize(px);
		
		plot.getDomainLabelWidget().getLabelPaint().setTextSize(px);
		plot.getDomainLabelWidget().setSize(
				new SizeMetrics(0.05f, SizeLayoutType.RELATIVE, 0.08f,
						SizeLayoutType.RELATIVE));
		plot.getDomainLabelWidget().setPositionMetrics(
				new PositionMetrics(0.07f, XLayoutStyle.RELATIVE_TO_LEFT, 0,
						YLayoutStyle.RELATIVE_TO_BOTTOM,
						AnchorPosition.LEFT_BOTTOM));
		
		plot.getDomainLabelWidget().setClippingEnabled(false);

		plot.getRangeLabelWidget().getLabelPaint().setTextSize(px);
		plot.getRangeLabelWidget().setSize(
				new SizeMetrics(0.2f, SizeLayoutType.RELATIVE, 0.06f,
						SizeLayoutType.RELATIVE));
		plot.getRangeLabelWidget()
				.setPositionMetrics(
						new PositionMetrics(0.01f,
								XLayoutStyle.RELATIVE_TO_LEFT, 0.0f,
								YLayoutStyle.RELATIVE_TO_CENTER,
								AnchorPosition.CENTER));
		
		plot.getRangeLabelWidget().setClippingEnabled(false);
		
		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
				r.getDisplayMetrics());

		plot.getTitleWidget().getLabelPaint().setTextSize(px);

		plot.getTitleWidget().setPositionMetrics(
				new PositionMetrics(0.0f, XLayoutStyle.ABSOLUTE_FROM_CENTER,
						-0.06f, YLayoutStyle.RELATIVE_TO_TOP,
						AnchorPosition.TOP_MIDDLE));
		
		plot.getTitleWidget().setSize(
				new SizeMetrics(0.15f, SizeLayoutType.RELATIVE, 0.5f,
						SizeLayoutType.RELATIVE));
		
		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
				r.getDisplayMetrics());

		plot.getGraphWidget().getDomainLabelPaint().setTextSize(px);
		plot.getGraphWidget().getRangeLabelPaint().setTextSize(px);

		plot.getGraphWidget().position(0.0f, XLayoutStyle.RELATIVE_TO_LEFT,
				0.02f, YLayoutStyle.RELATIVE_TO_TOP);

		plot.getGraphWidget().setSize(
				new SizeMetrics(0.9f, SizeLayoutType.RELATIVE, 0.99f,
						SizeLayoutType.RELATIVE));

		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28,
				r.getDisplayMetrics());

		plot.getGraphWidget().setRangeLabelWidth(px);

		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
				r.getDisplayMetrics());

		plot.getGraphWidget().setDomainLabelWidth(px);

		plot.getLegendWidget().getTextPaint().setTextSize(px);

		plot.getLegendWidget().position(-0.4f, XLayoutStyle.RELATIVE_TO_CENTER,
				-0.13f, YLayoutStyle.RELATIVE_TO_BOTTOM);

		plot.getLegendWidget().setSize(
				new SizeMetrics(0.15f, SizeLayoutType.RELATIVE, 0.5f,
						SizeLayoutType.RELATIVE));

		this.plot.redraw();
	}
}
