package ru.serjik.simulation.wind;

import ru.serjik.math.Vector2D;
import ru.serjik.utils.Accelerometer;

class Sparkle
{
	private static final byte[][] table = new byte[][]{{7, 0, 1}, {1, 2, 3}, {5, 6, 7}, {3, 4, 5}};
	private static final float VISCOSITY = 0.01f;

	Sparkle prev, next;

	Vector2D position = new Vector2D();
	Vector2D velocity = new Vector2D();
	Vector2D force = new Vector2D();

	ParticlesCell cell;

	boolean needCalculation = true;

	Sparkle(float x, float y, float cellSize)
	{
		position.set(x, y);
	}

	private void calcForces(Sparkle first, float size)
	{
		while (first != null)
		{
			calcForcesWith(first, size);
			first = first.next;
		}
	}

	private void calcForcesWith(Sparkle particle, float size)
	{
		if (particle.needCalculation && particle != this)
		{
			float fx = position.x - particle.position.x;
			float fy = position.y - particle.position.y;

			float distq = fx * fx + fy * fy;

			float dist = (float) Math.sqrt(distq);
			//float delta = (size - dist);

			if (distq > 0.01f)
			{
				float fk = size / (distq * dist);
				//float fk = 10.0f / dist;

				fx *= fk;
				fy *= fk;

				force.x += fx;
				force.y += fy;

				particle.force.x -= fx;
				particle.force.y -= fy;
			}
		}
	}

	final void calcForces(float size)
	{
		calcForces(cell.first, size);

		int index = (position.x < cell.position.x ? 1 : 0) | (position.y < cell.position.y ? 2 : 0);
		calcForces(cell.neighbours[table[index][0]].first, size);
		calcForces(cell.neighbours[table[index][1]].first, size);
		calcForces(cell.neighbours[table[index][2]].first, size);

//		calcForces(cell.neighbours[0].first, size);
//		calcForces(cell.neighbours[1].first, size);
//		calcForces(cell.neighbours[2].first, size);
//		calcForces(cell.neighbours[3].first, size);
//		calcForces(cell.neighbours[4].first, size);
//		calcForces(cell.neighbours[5].first, size);
//		calcForces(cell.neighbours[6].first, size);
//		calcForces(cell.neighbours[7].first, size);

		needCalculation = false;
	}

	final void tick(float dt)
	{
		force.minus(Accelerometer.ax,Accelerometer.ay);
		force.minus(velocity, VISCOSITY * velocity.length());

		position.plus(velocity, dt);
		velocity.plus(force, dt);
		force.set(0, 0);

		needCalculation = true;
	}

	final void remove()
	{
		if (prev != null)
		{
			prev.next = next;
		}

		if (next != null)
		{
			next.prev = prev;
		}

		prev = null;
		next = null;
	}
}
