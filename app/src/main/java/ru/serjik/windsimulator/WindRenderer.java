package ru.serjik.windsimulator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import ru.serjik.engine.AtlasGenerator;
import ru.serjik.engine.BatchDrawer;
import ru.serjik.engine.RendererSpriteHolder;
import ru.serjik.engine.Sprite;
import ru.serjik.engine.gles20.Texture;
import ru.serjik.simulation.wind.ParticlesContainer;
import ru.serjik.simulation.wind.ParticlesSimulable;
import ru.serjik.utils.AssetsUtils;

public class WindRenderer extends RendererSpriteHolder
{
	public static Sprite background;
	public static Sprite sparkle, star;

	public WindRenderer(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	private BatchDrawer bd;
	private Texture atlas;

	private ParticlesSimulable wind;
	private long lastFrameTime;

	private float offset = 0.5f;
	private float offsetDest = 0.5f;

	public void offset(float value)
	{
		offsetDest = value;
	}

	@Override
	public void onCreated(GL10 gl, EGLConfig config)
	{
		AtlasGenerator ag = new AtlasGenerator(1024);
		AssetManager am = context.getAssets();
		background = new Sprite(ag.tile(AssetsUtils.loadBitmap("background.jpg", am), true));
		sparkle = new Sprite(ag.tile(AssetsUtils.loadBitmap("sparkle.png", am), true));

		atlas = new Texture(ag.atlas());
		ag.atlas().recycle();
		bd = new BatchDrawer(4096, gl);
		Log.v("wind", "created");
	}

	@Override
	public void onChanged(GL10 gl, int width, int height)
	{
		super.onChanged(gl, width, height);

		// Texture.enable(gl);
		atlas.bind();

		// Texture.filter(gl, GL10.GL_LINEAR, GL10.GL_LINEAR);
		gl.glShadeModel(GL10.GL_SMOOTH);
		Log.v("wind", "size = " + width + " " + height);

		wind = new ParticlesContainer();
		wind.init(width, height, width > height ? width / 40 : height / 40, 128);

		// app.background.scale((height() > width() ? (height() /
		// app.background.height()) : (width() / app.background
		// .width())));

		gl.glTranslatef(-40, 0, 0);

	}

	@Override
	public void onDrawFrame(GL10 gl, float deltaTime)
	{
		long currentTime = System.currentTimeMillis();

		float dt = 0.001f * (float) (currentTime - lastFrameTime);

		lastFrameTime = currentTime;

		if (dt > 0.15f)
		{
			dt = 0.15f;
		}

		// dt = 0.015f;

		offset += (offsetDest - offset) * 0.1f;

		gl.glDisable(GL10.GL_BLEND);
		background.position(width() / 2.0f, height() / 2.0f).draw(bd);

		bd.flush();

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_DST_ALPHA);

		wind.tick(dt);

		float[] particles = wind.particles();

		// float c = ColorTools.color(1, 1, 1, 0.1f);

		for (int i = 0; i < particles.length; i += 2)
		{
			sparkle.position(particles[i + 0], particles[i + 1]).draw(bd);
		}

		bd.flush();

	}
}
