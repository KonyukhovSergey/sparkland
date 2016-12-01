package ru.serjik.simulation.wind;

import ru.serjik.math.Vector2D;

class ParticlesCell
{
	Vector2D position = new Vector2D();

	Sparkle first;
	ParticlesCell[] neighbours = new ParticlesCell[8];
}
