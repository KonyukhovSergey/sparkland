package ru.serjik.simulation.wind;

public interface ParticlesSimulable
{
	void init(float width, float height, float size, int count);

	void free();

	void tick(float delta);

	float[] particles();
}
