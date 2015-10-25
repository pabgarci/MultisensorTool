package es.pabgarci.multisensortool.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.RectF;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public final class AccelerationVectorView extends View{

	private static final String tag = AccelerationVectorView.class
			.getSimpleName();

	// holds the cached static part
	private Bitmap background;

	private Paint backgroundPaint;
	private Paint axisPaint;

	private Paint yAxisLengthPaint;
	private Paint xAxisLengthPaint;

	private Paint vectorPaint;

	private RectF rimRect;

	private float x;
	private float y;

	public AccelerationVectorView(Context context)
	{
		super(context);
		init();
	}


	public AccelerationVectorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}


	public AccelerationVectorView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}


	public void updatePoint(float x, float y)
	{

		// Bound the y-axis to +/- the gravity of earth
		if (x > SensorManager.GRAVITY_EARTH)
		{
			x = SensorManager.GRAVITY_EARTH;
		}
		if (x < -SensorManager.GRAVITY_EARTH)
		{
			x = -SensorManager.GRAVITY_EARTH;
		}

		// Divide by the length of our axis.
		this.x = (x / SensorManager.GRAVITY_EARTH) * 0.4f;

		// Bound the y-axis to +/- the gravity of earth
		if (y > SensorManager.GRAVITY_EARTH)
		{
			y = SensorManager.GRAVITY_EARTH;
		}
		if (y < -SensorManager.GRAVITY_EARTH)
		{
			y = -SensorManager.GRAVITY_EARTH;
		}

		// Normalize y to 1 and then scale to half the length of the y-axis.
		this.y = (y / SensorManager.GRAVITY_EARTH) * 0.4f;

		this.invalidate();
	}


	private void init()
	{
		initDrawingTools();
	}


	private void initDrawingTools()
	{
		// Leave a little bit of space between the side of the screen and the
		// rectangle...
		rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

		// the linear gradient is a bit skewed for realism
		axisPaint = new Paint();
		axisPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		axisPaint.setStrokeWidth(0.01f);
		axisPaint.setColor(Color.WHITE);
		axisPaint.setStyle(Paint.Style.STROKE);

		// the linear gradient is a bit skewed for realism
		vectorPaint = new Paint();
		vectorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		vectorPaint.setStrokeWidth(0.01f);
		vectorPaint.setColor(Color.RED);
		vectorPaint.setStyle(Paint.Style.STROKE);

		// the linear gradient is a bit skewed for realism
		yAxisLengthPaint = new Paint();
		yAxisLengthPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		yAxisLengthPaint.setStrokeWidth(0.01f);
		yAxisLengthPaint.setColor(Color.GREEN);
		yAxisLengthPaint.setStyle(Paint.Style.STROKE);

		// the linear gradient is a bit skewed for realism
		xAxisLengthPaint = new Paint();
		xAxisLengthPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		xAxisLengthPaint.setStrokeWidth(0.01f);
		xAxisLengthPaint.setColor(Color.BLUE);
		xAxisLengthPaint.setStyle(Paint.Style.STROKE);

		backgroundPaint = new Paint();
		backgroundPaint.setFilterBitmap(true);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);

		int chosenDimension = Math.min(chosenWidth, chosenHeight);

		setMeasuredDimension(chosenDimension, chosenDimension);
	}


	private int chooseDimension(int mode, int size)
	{
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY)
		{
			return size;
		}
		else
		{ // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		}
	}


	private int getPreferredSize()
	{
		return 300;
	}


	private void drawAxis(Canvas canvas)
	{
		// Draw the Y axis
		canvas.drawLine(rimRect.centerX(), rimRect.top, rimRect.centerX(),
				rimRect.bottom, axisPaint);

		// Draw the X axis
		canvas.drawLine(rimRect.left, rimRect.centerY(), rimRect.right,
				rimRect.centerY(), axisPaint);

		// Draw the Y axis arrow
		Path yArrowPath = new Path();
		yArrowPath.setFillType(FillType.EVEN_ODD);

		yArrowPath.moveTo(rimRect.centerX() - 0.002f, rimRect.top);
		yArrowPath.lineTo(rimRect.centerX() + 0.05f, rimRect.top + 0.05f);
		yArrowPath.moveTo(rimRect.centerX() + 0.002f, rimRect.top);
		yArrowPath.lineTo(rimRect.centerX() - 0.05f, rimRect.top + 0.05f);

		canvas.drawPath(yArrowPath, axisPaint);

		// Draw the Y axis arrow
		Path xArrowPath = new Path();
		xArrowPath.setFillType(FillType.EVEN_ODD);

		xArrowPath.moveTo(rimRect.right, rimRect.centerY() + 0.002f);
		xArrowPath.lineTo(rimRect.right - 0.05f, rimRect.centerY() - 0.05f);

		xArrowPath.moveTo(rimRect.right, rimRect.centerY() - 0.002f);
		xArrowPath.lineTo(rimRect.right - 0.05f, rimRect.centerY() + 0.05f);

		canvas.drawPath(xArrowPath, axisPaint);

	}


	private void drawBackground(Canvas canvas)
	{
		// Use the cached background bitmap.
		if (background == null)
		{
			Log.w(tag, "Background not created");
		}
		else
		{
			canvas.drawBitmap(background, 0, 0, backgroundPaint);
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		drawBackground(canvas);

		float scale = (float) getWidth();
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);

		drawAxisLength(canvas);
		drawVectorLength(canvas);

		canvas.restore();
	}

	private void drawVectorLength(Canvas canvas)
	{
		// Draw the vector.
		canvas.drawLine(rimRect.centerX(), rimRect.centerY(), rimRect.centerX()
				- this.x, rimRect.centerY() + this.y, vectorPaint);

		// Use the magnitude of the acceleration to determine the size of the
		// vectors arrow. We have to scale it up by 250% because we had
		// initially scaled it down by by 40% and we want to normalize to 1.
		float magnitude = (float) Math.sqrt(Math.pow(this.x, 2)
				+ Math.pow(this.y, 2)) * 2.5f;

		// Rotate the canvas so the arrows rotate with the vector. Note we have
		// to rotate by 90 degrees so 0 degrees is pointing in the positive Y
		// axis. We also change the rotation from counter clockwise to clockwise
		// with a negative out front. Also, note the atan2 produces a range from
		// 0 to 180 degrees and 0 to -180 degrees. I add 360 degrees and then
		// take mod 360 so the range is 0 to 360 degrees.
		canvas.rotate(
				(float) -((Math.toDegrees(Math.atan2(this.y, this.x)) + 450) % 360),
				rimRect.centerX() - this.x, rimRect.centerY() + this.y);

		// Draw the vector arrows. Note that the length of the arrows are scaled
		// by the magnitude.
		canvas.drawLine(rimRect.centerX() - this.x + 0.002f, rimRect.centerY()
				+ this.y, rimRect.centerX() - this.x - (0.05f * magnitude),
				rimRect.centerY() + this.y + (0.05f * magnitude), vectorPaint);

		canvas.drawLine(rimRect.centerX() - this.x - 0.002f, rimRect.centerY()
				+ this.y, rimRect.centerX() - this.x + (0.05f * magnitude),
				rimRect.centerY() + this.y + (0.05f * magnitude), vectorPaint);

		canvas.restore();
	}

	private void drawAxisLength(Canvas canvas)
	{
		// Draw the Y axis
		canvas.drawLine(rimRect.centerX(), rimRect.centerY(),
				rimRect.centerX(), rimRect.centerY() + this.y, yAxisLengthPaint);

		// Draw the X axis
		canvas.drawLine(rimRect.centerX(), rimRect.centerY(), rimRect.centerX()
				- this.x, rimRect.centerY(), xAxisLengthPaint);
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		Log.d(tag, "Size changed to " + w + "x" + h);

		regenerateBackground();
	}


	private void regenerateBackground()
	{
		// free the old bitmap
		if (background != null)
		{
			background.recycle();
		}

		background = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas backgroundCanvas = new Canvas(background);
		float scale = (float) getWidth();
		backgroundCanvas.scale(scale, scale);

		drawAxis(backgroundCanvas);
	}
}
