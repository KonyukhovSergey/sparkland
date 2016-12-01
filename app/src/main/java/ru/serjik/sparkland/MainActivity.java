package ru.serjik.sparkland;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import ru.serjik.windsimulator.PausableView;
import ru.serjik.windsimulator.WindView;

public class MainActivity extends Activity
{
	private PausableView view;
	private View mDecorView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mDecorView = getWindow().getDecorView();
		view = new WindView(this);
		setContentView(view);
	}

	@Override
	protected void onPause()
	{
		view.onPause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		view.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		view.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus)
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			}
		}
	}
}
