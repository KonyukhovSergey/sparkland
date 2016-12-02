package ru.serjik.windsimulator;

import ru.serjik.simulation.wind.ParticlesContainer;
import ru.serjik.simulation.wind.ParticlesSimulable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class WindView extends PausableView
{
	public WindView(Context context)
	{
		super(context);
		setBackgroundColor(Color.BLACK);
	}

	private ParticlesSimulable wind;

	@Override
	public void onDraw(Canvas canvas, float dt)
	{
		wind.tick(dt);

		paint.setColor(Color.rgb(255, 255, 255));
		paint.setStrokeWidth(2);
		canvas.drawPoints(wind.particles(), paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		wind = new ParticlesContainer();
		wind.init(w, h, w > h ? w / 16 : h / 16, 1024);
	}

	public void onDestroy()
	{
		wind.free();
	}
}
