package ru.serjik.simulation.wind;

import ru.serjik.math.Vector2D;

class Sparkle
{
	private static final byte[][] table = new byte[][]{{7, 0, 1}, {1, 2, 3}, {5, 6, 7}, {3, 4, 5}};

	Sparkle prev, next;

	Vector2D position = new Vector2D();
	Vector2D velocity = new Vector2D();
	float energy = 0.0f;

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
//		if (particle.needCalculation && particle != this)
//		{
//			float dx = particle.position.x - (position.x + velocity.x);
//			float dy = particle.position.y - (position.y + velocity.y);
//
//			float quadDistance = dx * dx + dy * dy;
//
//			if (size * size > quadDistance)
//			{
//				velocity.plus(-dx, -dy, 0.25f);
//				particle.velocity.plus(dx, dy, 0.25f);
//			}
//		}
	}

	final void calcForces(float size)
	{
		calcForces(cell.first, size);

//		int index = (position.x < cell.position.x ? 1 : 0) | (position.y < cell.position.y ? 2 : 0);
//		calcForces(cell.neighbours[table[index][0]].first, size);
//		calcForces(cell.neighbours[table[index][1]].first, size);
//		calcForces(cell.neighbours[table[index][2]].first, size);

		calcForces(cell.neighbours[0].first, size);
		calcForces(cell.neighbours[1].first, size);
		calcForces(cell.neighbours[2].first, size);
		calcForces(cell.neighbours[3].first, size);
		calcForces(cell.neighbours[4].first, size);
		calcForces(cell.neighbours[5].first, size);
		calcForces(cell.neighbours[6].first, size);
		calcForces(cell.neighbours[7].first, size);

		needCalculation = false;
	}

	final void tick(float dt)
	{
		position.plus(velocity, dt);
		velocity.plus();
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
