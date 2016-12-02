package ru.serjik.windsimulator;

import ru.serjik.utils.Accelerometer;
import ru.serjik.utils.FrameRateCalculator;
import ru.serjik.utils.FrameRateCalculator.FrameRateUpdater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public abstract class PausableView extends View
{
	private long lastFrameTime;
	protected Paint paint;
	private FrameRateCalculator frc;
	private static Accelerometer acc;

	public PausableView(Context context)
	{
		super(context);


		paint = new Paint();

		acc = new Accelerometer(context);


		frc = new FrameRateCalculator(new FrameRateUpdater()
		{
			@Override
			public void onFrameRateUpdate(FrameRateCalculator frameRateCalculator)
			{
				Log.v("wind", frameRateCalculator.frameString());

			}
		});
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas)
	{
		frc.frameBegin();
		long currentTime = System.currentTimeMillis();
		float dt = 0.001f * (float) (currentTime - lastFrameTime);
		lastFrameTime = currentTime;
		if (dt > 0.15f)
		{
			dt = 0.15f;
		}
		onDraw(canvas, dt);
		invalidate();
		frc.frameDone();
	}

	public abstract void onDraw(Canvas canvas, float dt);

	public void onPause()
	{
		acc.stop();
	}

	public void onResume()
	{
		lastFrameTime = System.currentTimeMillis();
		acc.start();
	}

	public void onDestroy()
	{

	}
}
