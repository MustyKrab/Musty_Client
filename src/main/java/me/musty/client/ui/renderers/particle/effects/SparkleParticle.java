package me.musty.client.ui.renderers.particle.effects;

import me.musty.client.ui.renderers.particle.Particle;
import me.musty.client.ui.renderers.particle.ParticleType;
import java.awt.Color;

public class SparkleParticle extends Particle {
    private float rotation;
    private float rotationSpeed;
    public SparkleParticle(float x, float y, float vx, float vy, float life, Color color, float rotationSpeed) {
        super(x, y, vx, vy, life, color, ParticleType.SPARKLE);
        this.rotation = 0f;
        this.rotationSpeed = rotationSpeed;
    }
    @Override
    public void update() { super.update(); rotation += rotationSpeed; }
    public float getRotation() { return rotation; }
}
