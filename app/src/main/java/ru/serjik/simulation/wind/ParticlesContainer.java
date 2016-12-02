package ru.serjik.simulation.wind;

import android.util.Log;

import java.util.Random;

public class ParticlesContainer implements ParticlesSimulable
{
	private int widthCount;
	private int heigthCount;
	private int cellsCount;

	private float cellSize;
	private float width;
	private float heigth;

	private ParticlesCell[] cells;
	private Sparkle[] particles;

	private float[] particlesVerticies;

	private Random rnd = new Random(System.currentTimeMillis());

	@Override
	public void init(float width, float heigth, float cellSize, int count)
	{
		this.widthCount = (int) (width / cellSize) + 1;
		this.heigthCount = (int) (heigth / cellSize) + 1;

		this.cellSize = cellSize;

		this.width = widthCount * cellSize;
		this.heigth = heigthCount * cellSize;

		this.cellsCount = widthCount * heigthCount;

		cells = new ParticlesCell[cellsCount];

		for (int i = 0; i < cellsCount; i++)
		{
			cells[i] = new ParticlesCell();
		}

		for (int i = 0; i < cellsCount; i++)
		{
			int x = i % widthCount;
			int y = i / widthCount;

			cells[i].position.set((x + 0.5f) * cellSize, (y + 0.5f) * cellSize);

			cells[i].neighbours[0] = cells[((x - 1 + widthCount) % widthCount) + ((y - 1 + heigthCount) % heigthCount) * widthCount];
			cells[i].neighbours[1] = cells[((x + widthCount) % widthCount) + ((y - 1 + heigthCount) % heigthCount) * widthCount];
			cells[i].neighbours[2] = cells[((x + 1 + widthCount) % widthCount) + ((y - 1 + heigthCount) % heigthCount) * widthCount];
			cells[i].neighbours[3] = cells[((x + 1 + widthCount) % widthCount) + ((y + heigthCount) % heigthCount) * widthCount];
			cells[i].neighbours[4] = cells[((x + 1 + widthCount) % widthCount) + ((y + 1 + heigthCount) % heigthCount) * widthCount];
			cells[i].neighbours[5] = cells[((x + widthCount) % widthCount) + ((y + 1 + heigthCount) % heigthCount) * widthCount];
			cells[i].neighbours[6] = cells[((x - 1 + widthCount) % widthCount) + ((y + 1 + heigthCount) % heigthCount) * widthCount];
			cells[i].neighbours[7] = cells[((x - 1 + widthCount) % widthCount) + ((y + heigthCount) % heigthCount) * widthCount];
		}

		particles = new Sparkle[count];
		particlesVerticies = new float[count * 2];

		for (int i = 0; i < particles.length; i++)
		{
			particles[i] = new Sparkle(this.width * rnd.nextFloat(), this.heigth * rnd.nextFloat(), cellSize);
			register(particles[i]);
		}
	}

	private boolean register(Sparkle particle)
	{
		ParticlesCell cell = getCellFor(particle.position.x, particle.position.y);

		if (cell != null)
		{
			addFirstToCell(particle, cell);
			return true;
		}

		return false;
	}

	@Override
	public void tick(float dt)
	{
		for (int i = particles.length - 1; i >= 0; i--)
		{
			particles[i].calcForces(cellSize * 0.5f);
		}

		for (int i = particles.length - 1; i >= 0; i--)
		{
			Sparkle particle = particles[i];

			particle.tick(dt);

			fix2Position(particle);

			ParticlesCell cell = getCellFor(particle.position.x, particle.position.y);

			if (cell != particle.cell)
			{
				if (particle.cell.first == particle)
				{
					particle.cell.first = particle.next;
				}

				particle.remove();

				addFirstToCell(particle, cell);
			}
		}

		// check();
	}

	private void fixPosition(Sparkle particle)
	{
		particle.position.x = fix(particle.position.x, width - 0.001f);
		particle.position.y = fix(particle.position.y, heigth - 0.001f);
	}

	private void fix2Position(Sparkle particle)
	{
		particle.position.x = fix2(particle.position.x, width);
		particle.position.y = fix2(particle.position.y, heigth);
	}

	private static float fix(float value, float range)
	{
		value = value % range;

		if (value < 0)
		{
			value += range;
		}

		return value;
	}

	private static float fix2(float value, float range)
	{
		value = value % range;

		if (value < 0.0f)
		{
			value = range * 0.0001f;
		}
		if (value > range)
		{
			value = range * 0.9999f;
		}

		return value;
	}

	private void addFirstToCell(Sparkle particle, ParticlesCell cell)
	{
		particle.next = cell.first;

		if (cell.first != null)
		{
			cell.first.prev = particle;
		}

		cell.first = particle;

		particle.cell = cell;
	}

	private ParticlesCell getCellFor(float x, float y)
	{
		int cx = (int) (x / cellSize);
		int cy = (int) (y / cellSize);

		return cells[cy * widthCount + cx];
	}

	@SuppressWarnings("unused")
	private void check()
	{
		Log.v("wind", "checking...");

		int count = 0;

		for (int i = 0; i < cellsCount; i++)
		{
			Sparkle particle = cells[i].first;

			while (particle != null)
			{
				count++;

				if (particle.cell != cells[i])
				{
					Log.e("wing", "particle from wrong cell");
				}

				particle = particle.next;
			}
		}

		if (count != particles.length)
		{
			Log.e("wind", "wrong cell particle count");
		}
	}

	@Override
	public void free()
	{
	}

	@Override
	public float[] particles()
	{
		int j = 0;
		for (int i = particles.length - 1; i >= 0; i--)
		{
			particlesVerticies[j++] = particles[i].position.x;
			particlesVerticies[j++] = particles[i].position.y;
		}
		return particlesVerticies;
	}
}
